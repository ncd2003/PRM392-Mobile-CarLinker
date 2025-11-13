package com.example.prm392_mobile_carlinker.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatMessage;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying list of chat rooms
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private final Context context;
    private List<ChatRoom> chatRooms;
    private OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatRoom chatRoom);
    }

    public ChatRoomAdapter(Context context) {
        this.context = context;
        this.chatRooms = new ArrayList<>();
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms != null ? chatRooms : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnChatRoomClickListener(OnChatRoomClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
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

    class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvGarageName;
        private final TextView tvLastMessage;
        private final TextView tvLastMessageTime;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGarageName = itemView.findViewById(R.id.tv_garage_name);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvLastMessageTime = itemView.findViewById(R.id.tv_last_message_time);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onChatRoomClick(chatRooms.get(position));
                }
            });
        }

        public void bind(ChatRoom chatRoom) {
            // Set garage name
            if (chatRoom.getGarageName() != null) {
                tvGarageName.setText(chatRoom.getGarageName());
            } else {
                tvGarageName.setText("Unknown Garage");
            }

            // Set last message
            ChatMessage lastMessage = chatRoom.getLastMessage();
            if (lastMessage != null && lastMessage.getMessage() != null) {
                tvLastMessage.setText(lastMessage.getMessage());
                tvLastMessage.setVisibility(View.VISIBLE);
            } else {
                tvLastMessage.setText("No messages yet");
                tvLastMessage.setVisibility(View.VISIBLE);
            }

            // Set last message time
            if (chatRoom.getLastMessageAt() != null) {
                tvLastMessageTime.setText(formatTime(chatRoom.getLastMessageAt()));
                tvLastMessageTime.setVisibility(View.VISIBLE);
            } else {
                tvLastMessageTime.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Format timestamp to readable time
     */
    private String formatTime(String timestamp) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(timestamp);

            if (date != null) {
                // Check if today
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String today = dateFormat.format(now);
                String messageDate = dateFormat.format(date);

                if (today.equals(messageDate)) {
                    // Show time if today
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    return timeFormat.format(date);
                } else {
                    // Show date if not today
                    SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
                    return outputFormat.format(date);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }
}

