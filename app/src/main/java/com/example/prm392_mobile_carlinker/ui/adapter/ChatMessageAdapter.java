package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatMessage;
import com.example.prm392_mobile_carlinker.data.model.chat.MessageType;
import com.example.prm392_mobile_carlinker.data.model.chat.FileType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying chat messages in RecyclerView
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final Context context;
    private final List<ChatMessage> messages;
    private final int currentUserId;
    private final SimpleDateFormat timeFormat;

    public ChatMessageAdapter(Context context, int currentUserId) {
        this.context = context;
        this.messages = new ArrayList<>();
        this.currentUserId = currentUserId;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getSenderId() == currentUserId) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<ChatMessage> newMessages) {
        messages.clear();
        if (newMessages != null) {
            // Reverse the list to show oldest first (API returns newest first)
            for (int i = newMessages.size() - 1; i >= 0; i--) {
                messages.add(newMessages.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void addMessages(List<ChatMessage> newMessages) {
        if (newMessages != null && !newMessages.isEmpty()) {
            int startPosition = messages.size();
            // Reverse and add
            for (int i = newMessages.size() - 1; i >= 0; i--) {
                messages.add(newMessages.get(i));
            }
            notifyItemRangeInserted(startPosition, newMessages.size());
        }
    }

    /**
     * ViewHolder for sent messages (right side)
     */
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvTime;
        ImageView ivMediaImage;
        View layoutMedia;

        SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMediaImage = itemView.findViewById(R.id.iv_media_image);
            layoutMedia = itemView.findViewById(R.id.layout_media);
        }

        void bind(ChatMessage message) {
            // Show timestamp
            tvTime.setText(formatTime(message.getCreatedAt()));

            MessageType messageType = message.getMessageTypeEnum();
            
            if (messageType == MessageType.TEXT) {
                // Text message
                tvMessage.setVisibility(View.VISIBLE);
                layoutMedia.setVisibility(View.GONE);
                tvMessage.setText(message.getMessage());
            } else if (messageType == MessageType.MEDIA) {
                // Media message
                FileType fileType = message.getFileTypeEnum();
                
                if (fileType == FileType.IMAGE) {
                    // Show image
                    tvMessage.setVisibility(message.getMessage() != null ? View.VISIBLE : View.GONE);
                    layoutMedia.setVisibility(View.VISIBLE);
                    
                    if (message.getMessage() != null) {
                        tvMessage.setText(message.getMessage());
                    }
                    
                    Glide.with(context)
                            .load(message.getFileUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_error)
                            .into(ivMediaImage);
                } else {
                    // Video or File - show as text with icon for now
                    tvMessage.setVisibility(View.VISIBLE);
                    layoutMedia.setVisibility(View.GONE);
                    String fileTypeText = fileType == FileType.VIDEO ? "📹 Video" : "📄 File";
                    tvMessage.setText(message.getMessage() != null ? 
                            fileTypeText + ": " + message.getMessage() : fileTypeText);
                }
            }
        }
    }

    /**
     * ViewHolder for received messages (left side)
     */
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderName;
        TextView tvMessage;
        TextView tvTime;
        ImageView ivMediaImage;
        View layoutMedia;

        ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMediaImage = itemView.findViewById(R.id.iv_media_image);
            layoutMedia = itemView.findViewById(R.id.layout_media);
        }

        void bind(ChatMessage message) {
            // Show sender name
            if (message.getSenderName() != null) {
                tvSenderName.setText(message.getSenderName());
            }

            // Show timestamp
            tvTime.setText(formatTime(message.getCreatedAt()));

            MessageType messageType = message.getMessageTypeEnum();
            
            if (messageType == MessageType.TEXT) {
                // Text message
                tvMessage.setVisibility(View.VISIBLE);
                layoutMedia.setVisibility(View.GONE);
                tvMessage.setText(message.getMessage());
            } else if (messageType == MessageType.MEDIA) {
                // Media message
                FileType fileType = message.getFileTypeEnum();
                
                if (fileType == FileType.IMAGE) {
                    // Show image
                    tvMessage.setVisibility(message.getMessage() != null ? View.VISIBLE : View.GONE);
                    layoutMedia.setVisibility(View.VISIBLE);
                    
                    if (message.getMessage() != null) {
                        tvMessage.setText(message.getMessage());
                    }
                    
                    Glide.with(context)
                            .load(message.getFileUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_error)
                            .into(ivMediaImage);
                } else {
                    // Video or File - show as text with icon for now
                    tvMessage.setVisibility(View.VISIBLE);
                    layoutMedia.setVisibility(View.GONE);
                    String fileTypeText = fileType == FileType.VIDEO ? "📹 Video" : "📄 File";
                    tvMessage.setText(message.getMessage() != null ? 
                            fileTypeText + ": " + message.getMessage() : fileTypeText);
                }
            }
        }
    }

    /**
     * Format timestamp to time string
     */
    private String formatTime(String timestamp) {
        try {
            SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = iso8601Format.parse(timestamp);
            return timeFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}
