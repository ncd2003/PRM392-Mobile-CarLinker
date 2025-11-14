package com.example.prm392_mobile_carlinker.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;
import com.example.prm392_mobile_carlinker.data.repository.ChatRepository;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.ui.adapter.ChatRoomAdapter;
import com.example.prm392_mobile_carlinker.util.SessionManager;

import java.util.List;

/**
 * Activity for displaying list of chat rooms
 * Supports both Customer and Garage roles
 */
public class ChatRoomListActivity extends AppCompatActivity {

    public static final String EXTRA_CUSTOMER_ID = "customer_id";

    private RecyclerView rvChatRooms;
    private ProgressBar progressBar;
    private View emptyView;

    private ChatRoomAdapter adapter;
    private ChatRepository chatRepository;
    private SessionManager sessionManager;
    
    private int userId;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);
        
        // Get user info from SessionManager
        userId = sessionManager.getUserId();
        userRole = sessionManager.getUserRoleString();

        initializeViews();
        setupRecyclerView();

        chatRepository = new ChatRepository();
        
        loadChatRooms();
    }

    private void initializeViews() {
        rvChatRooms = findViewById(R.id.rv_chat_rooms);
        progressBar = findViewById(R.id.progress_bar);
        emptyView = findViewById(R.id.empty_view);
    }

    private void setupRecyclerView() {
        adapter = new ChatRoomAdapter(this);
        adapter.setOnChatRoomClickListener(this::openChatRoom);

        rvChatRooms.setLayoutManager(new LinearLayoutManager(this));
        rvChatRooms.setAdapter(adapter);
    }

    private void loadChatRooms() {
        showLoading(true);

        // Load chat rooms based on user role
        LiveData<Result<List<ChatRoom>>> chatRoomsLiveData;
        
        if ("STAFF".equalsIgnoreCase(userRole) || "GARAGE".equalsIgnoreCase(userRole)) {
            // For garage staff/owner, load garage chat rooms using garageId
            int garageId = sessionManager.getGarageId();
            if (garageId == -1) {
                Toast.makeText(this, "Lỗi: Không tìm thấy thông tin garage", Toast.LENGTH_SHORT).show();
                showLoading(false);
                rvChatRooms.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                return;
            }
            chatRoomsLiveData = chatRepository.getGarageChatRooms(garageId);
        } else {
            // For customers, load customer chat rooms
            chatRoomsLiveData = chatRepository.getCustomerChatRooms(userId);
        }

        chatRoomsLiveData.observe(this, result -> {
            showLoading(false);

            if (result != null && result.status == Result.Status.SUCCESS) {
                List<ChatRoom> chatRooms = result.data;
                
                if (chatRooms != null && !chatRooms.isEmpty()) {
                    adapter.setChatRooms(chatRooms);
                    rvChatRooms.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                } else {
                    rvChatRooms.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            } else if (result != null && result.status == Result.Status.ERROR) {
                String error = result.message;
                Toast.makeText(this, "Failed to load chat rooms: " + error, Toast.LENGTH_SHORT).show();
                rvChatRooms.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openChatRoom(ChatRoom chatRoom) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_ROOM_ID, chatRoom.getId());
        intent.putExtra(ChatActivity.EXTRA_GARAGE_ID, chatRoom.getGarageId());
        intent.putExtra(ChatActivity.EXTRA_GARAGE_NAME, chatRoom.getGarageName());
        intent.putExtra(ChatActivity.EXTRA_CUSTOMER_ID, chatRoom.getCustomerId());
        startActivity(intent);
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload chat rooms when returning to this activity
        loadChatRooms();
    }
}
