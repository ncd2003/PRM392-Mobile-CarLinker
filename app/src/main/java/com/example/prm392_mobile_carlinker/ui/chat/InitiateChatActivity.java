package com.example.prm392_mobile_carlinker.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;
import com.example.prm392_mobile_carlinker.data.repository.ChatRepository;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.util.SessionManager;

/**
 * UC02: Initiate Chat with Garage
 * This activity allows a customer to start a new chat conversation with a garage
 */
public class InitiateChatActivity extends AppCompatActivity {

    public static final String EXTRA_GARAGE_ID = "garage_id";
    public static final String EXTRA_GARAGE_NAME = "garage_name";

    private TextView tvGarageName;
    private TextView tvGarageInfo;
    private Button btnStartChat;
    private ProgressBar progressBar;

    private ChatRepository chatRepository;
    private SessionManager sessionManager;
    private int garageId;
    private String garageName;
    private int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initiate_chat);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Get garage info from intent
        garageId = getIntent().getIntExtra(EXTRA_GARAGE_ID, 0);
        garageName = getIntent().getStringExtra(EXTRA_GARAGE_NAME);

        // Get customer ID from SessionManager
        customerId = sessionManager.getUserId();

        if (garageId == 0 || customerId == 0) {
            Toast.makeText(this, "Invalid garage or customer ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupClickListeners();

        chatRepository = new ChatRepository();
    }

    private void initializeViews() {
        tvGarageName = findViewById(R.id.tv_garage_name);
        tvGarageInfo = findViewById(R.id.tv_garage_info);
        btnStartChat = findViewById(R.id.btn_start_chat);
        progressBar = findViewById(R.id.progress_bar);

        // Set garage name
        if (garageName != null) {
            tvGarageName.setText(garageName);
            tvGarageInfo.setText("Start a conversation with " + garageName);
        }
    }

    private void setupClickListeners() {
        btnStartChat.setOnClickListener(v -> createOrGetChatRoom());
    }

    /**
     * UC02 Main Flow: Create or get existing chat room with the garage
     */
    private void createOrGetChatRoom() {
        showLoading(true);

        chatRepository.createOrGetChatRoom(garageId, customerId).observe(this, result -> {
            showLoading(false);

            if (result != null && result.status == Result.Status.SUCCESS) {
                ChatRoom chatRoom = result.data;

                if (chatRoom != null) {
                    // Chat room created or retrieved successfully
                    Toast.makeText(this, "Chat room ready!", Toast.LENGTH_SHORT).show();
                    openChatRoom(chatRoom);
                } else {
                    Toast.makeText(this, "Failed to create chat room", Toast.LENGTH_SHORT).show();
                }
            } else if (result != null && result.status == Result.Status.ERROR) {
                String error = result.message;
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Open the chat activity with the created/retrieved chat room
     */
    private void openChatRoom(ChatRoom chatRoom) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_ROOM_ID, chatRoom.getId());
        intent.putExtra(ChatActivity.EXTRA_GARAGE_ID, chatRoom.getGarageId());
        intent.putExtra(ChatActivity.EXTRA_GARAGE_NAME, chatRoom.getGarageName());
        intent.putExtra(ChatActivity.EXTRA_CUSTOMER_ID, customerId);
        startActivity(intent);
        finish(); // Close this activity after opening chat
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnStartChat.setEnabled(!show);
    }
}

