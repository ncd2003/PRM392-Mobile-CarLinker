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
import com.example.prm392_mobile_carlinker.data.model.chat.FileType;
import com.example.prm392_mobile_carlinker.data.model.chat.MessageStatus;
import com.example.prm392_mobile_carlinker.data.model.chat.MessageType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying chat messages in a RecyclerView
 * Shows sent messages on the right and received messages on the left
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private final Context context;
    private final int currentUserId;
    private List<ChatMessage> messages;
    private OnMessageLongClickListener longClickListener;

    public interface OnMessageLongClickListener {
        void onMessageLongClick(ChatMessage message, int position);
    }

    public ChatMessageAdapter(Context context, int currentUserId) {
        this.context = context;
        this.currentUserId = currentUserId;
        this.messages = new ArrayList<>();
    }

    public void setOnMessageLongClickListener(OnMessageLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages != null ? messages : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addMessage(ChatMessage message) {
        this.messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void updateMessage(ChatMessage updatedMessage) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == updatedMessage.getId()) {
                messages.set(i, updatedMessage);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeMessage(long messageId) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId() == messageId) {
                messages.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        // If sender ID matches current user, it's a sent message
        if (message.getSenderId() == currentUserId) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message_received, parent, false);
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

    /**
     * ViewHolder for sent messages (displayed on the right)
     */
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final TextView tvTime;
        private final ImageView ivMedia;
        private final View mediaContainer;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMedia = itemView.findViewById(R.id.iv_media);
            mediaContainer = itemView.findViewById(R.id.media_container);
        }

        public void bind(ChatMessage message) {
            // Set timestamp with "Edited" indicator if message was edited
            String timeText = formatTime(message.getCreatedAt());
            if (message.getMessageStatusEnum() == MessageStatus.EDITED) {
                timeText = "Edited " + formatTime(message.getUpdatedAt());
            }
            tvTime.setText(timeText);

            // Check message type
            MessageType messageType = message.getMessageTypeEnum();

            if (messageType == MessageType.MEDIA && message.getFileUrl() != null) {
                // Show media message
                mediaContainer.setVisibility(View.VISIBLE);

                FileType fileType = message.getFileTypeEnum();
                if (fileType == FileType.IMAGE) {
                    // Load image
                    Glide.with(context)
                            .load(message.getFileUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_error)
                            .into(ivMedia);
                }

                // Show caption if exists
                if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(message.getMessage());
                } else {
                    tvMessage.setVisibility(View.GONE);
                }
            } else {
                // Show text message
                mediaContainer.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(message.getMessage());
            }

            // Set long click listener for edit/delete options
            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onMessageLongClick(message, getAdapterPosition());
                    return true;
                }
                return false;
            });
        }
    }

    /**
     * ViewHolder for received messages (displayed on the left)
     */
    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSenderName;
        private final TextView tvMessage;
        private final TextView tvTime;
        private final ImageView ivMedia;
        private final View mediaContainer;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSenderName = itemView.findViewById(R.id.tv_sender_name);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivMedia = itemView.findViewById(R.id.iv_media);
            mediaContainer = itemView.findViewById(R.id.media_container);
        }

        public void bind(ChatMessage message) {
            // Set sender name
            if (message.getSenderName() != null) {
                tvSenderName.setText(message.getSenderName());
            } else {
                tvSenderName.setText("Unknown");
            }

            // Set timestamp with "Edited" indicator if message was edited
            String timeText = formatTime(message.getCreatedAt());
            if (message.getMessageStatusEnum() == MessageStatus.EDITED) {
                timeText = "Edited " + formatTime(message.getUpdatedAt());
            }
            tvTime.setText(timeText);

            // Check message type
            MessageType messageType = message.getMessageTypeEnum();

            if (messageType == MessageType.MEDIA && message.getFileUrl() != null) {
                // Show media message
                mediaContainer.setVisibility(View.VISIBLE);

                FileType fileType = message.getFileTypeEnum();
                if (fileType == FileType.IMAGE) {
                    // Load image
                    Glide.with(context)
                            .load(message.getFileUrl())
                            .placeholder(R.drawable.ic_image_placeholder)
                            .error(R.drawable.ic_image_error)
                            .into(ivMedia);
                }

                // Show caption if exists
                if (message.getMessage() != null && !message.getMessage().isEmpty()) {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText(message.getMessage());
                } else {
                    tvMessage.setVisibility(View.GONE);
                }
            } else {
                // Show text message
                mediaContainer.setVisibility(View.GONE);
                tvMessage.setVisibility(View.VISIBLE);
                tvMessage.setText(message.getMessage());
            }
        }
    }

    /**
     * Format timestamp to readable time
     */
    private String formatTime(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);
            return date != null ? outputFormat.format(date) : timestamp;
        } catch (ParseException e) {
            return timestamp;
        }
    }
}

