package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying chat room list
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private final Context context;
    private final List<ChatRoom> chatRooms;
    private final SimpleDateFormat timeFormat;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ChatRoomAdapter(Context context) {
        this.context = context;
        this.chatRooms = new ArrayList<>();
        this.timeFormat = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
    }

    public void setOnChatRoomClickListener(OnChatRoomClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public void setChatRooms(List<ChatRoom> newChatRooms) {
        chatRooms.clear();
        if (newChatRooms != null) {
            chatRooms.addAll(newChatRooms);
        }
        notifyDataSetChanged();
    }

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvGarageName;
        TextView tvLastMessage;
        TextView tvTime;
        View unreadIndicator;

        ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGarageName = itemView.findViewById(R.id.tv_garage_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTime = itemView.findViewById(R.id.tv_time);
            unreadIndicator = itemView.findViewById(R.id.unread_indicator);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onChatRoomClick(chatRooms.get(position));
                    }
                }
            });
        }

        void bind(ChatRoom chatRoom) {
            // Display garage name
            if (chatRoom.getGarageName() != null) {
                tvGarageName.setText(chatRoom.getGarageName());
            }

            // Display last message preview
            if (chatRoom.getLastMessage() != null) {
                String messageText = chatRoom.getLastMessage().getMessage();
                if (messageText != null && !messageText.isEmpty()) {
                    tvLastMessage.setText(messageText);
                } else {
                    // Media message without caption
                    switch (chatRoom.getLastMessage().getMessageTypeEnum()) {
                        case MEDIA:
                            if (chatRoom.getLastMessage().getFileTypeEnum() != null) {
                                switch (chatRoom.getLastMessage().getFileTypeEnum()) {
                                    case IMAGE:
                                        tvLastMessage.setText("📷 Image");
                                        break;
                                    case VIDEO:
                                        tvLastMessage.setText("📹 Video");
                                        break;
                                    case FILE:
                                        tvLastMessage.setText("📄 File");
                                        break;
                                }
                            } else {
                                tvLastMessage.setText("Media");
                            }
                            break;
                        case SYSTEM:
                            tvLastMessage.setText("System message");
                            break;
                        default:
                            tvLastMessage.setText("");
                    }
                }

                // Show unread indicator if message is unread and not from current user
                if (!chatRoom.getLastMessage().isRead() && 
                    chatRoom.getLastMessage().getSenderType() != 0) { // Not from customer
                    unreadIndicator.setVisibility(View.VISIBLE);
                } else {
                    unreadIndicator.setVisibility(View.GONE);
                }
            } else {
                tvLastMessage.setText("No messages yet");
                unreadIndicator.setVisibility(View.GONE);
            }

            // Display time of last message
            if (chatRoom.getLastMessageAt() != null) {
                tvTime.setText(formatTime(chatRoom.getLastMessageAt()));
            } else {
                tvTime.setText("");
            }
        }
    }

    /**
     * Format timestamp to readable time
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
