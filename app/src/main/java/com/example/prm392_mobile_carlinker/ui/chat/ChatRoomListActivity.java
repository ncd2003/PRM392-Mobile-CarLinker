package com.example.prm392_mobile_carlinker.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageResponse;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;
import com.example.prm392_mobile_carlinker.data.repository.ChatRepository;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.ui.adapter.ChatRoomAdapter;
import com.example.prm392_mobile_carlinker.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if ("GARAGE".equalsIgnoreCase(userRole)) {
            // For garage owner, load ALL garages owned by this user first
            loadGarageOwnerChatRooms();
        } else if ("STAFF".equalsIgnoreCase(userRole)) {
            // For garage staff, load chat rooms using garageId from session
            int garageId = sessionManager.getGarageId();
            Log.d("ChatRoomList", "Loading garage chats - Role: " + userRole + ", GarageId: " + garageId);
            
            if (garageId == -1 || garageId == 0) {
                Toast.makeText(this, "Lỗi: Tài khoản chưa được gán vào garage. Vui lòng đăng nhập lại hoặc liên hệ quản trị viên.", Toast.LENGTH_LONG).show();
                showLoading(false);
                rvChatRooms.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                return;
            }
            
            LiveData<Result<List<ChatRoom>>> chatRoomsLiveData = chatRepository.getGarageChatRooms(garageId);
            observeChatRooms(chatRoomsLiveData);
        } else {
            // For customers, load customer chat rooms
            LiveData<Result<List<ChatRoom>>> chatRoomsLiveData = chatRepository.getCustomerChatRooms(userId);
            observeChatRooms(chatRoomsLiveData);
        }
    }
    
    /**
     * Load chat rooms for garage owner - owner có thể có nhiều garage
     */
    private void loadGarageOwnerChatRooms() {
        Log.d("ChatRoomList", "Loading garages for owner - UserId: " + userId);
        
        // Gọi API lấy tất cả garage
        RetrofitClient.getApiService().getAllGarages().enqueue(new Callback<GarageResponse>() {
            @Override
            public void onResponse(Call<GarageResponse> call, Response<GarageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GarageResponse garageResponse = response.body();
                    
                    if (garageResponse.getStatus() == 200 && garageResponse.getData() != null) {
                        GarageResponse.GarageData garageData = garageResponse.getData();
                        List<com.example.prm392_mobile_carlinker.data.model.garage.Garage> allGarages = garageData.getItems();
                        
                        if (allGarages == null || allGarages.isEmpty()) {
                            Log.w("ChatRoomList", "No garages found in system");
                            Toast.makeText(ChatRoomListActivity.this, 
                                "Bạn chưa có garage nào. Vui lòng tạo garage trước.", 
                                Toast.LENGTH_LONG).show();
                            showLoading(false);
                            rvChatRooms.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            return;
                        }
                        
                        // Filter các garage thuộc về user này
                        List<Integer> ownedGarageIds = new ArrayList<>();
                        for (com.example.prm392_mobile_carlinker.data.model.garage.Garage garage : allGarages) {
                            if (garage.getUserId() == userId) {
                                ownedGarageIds.add(garage.getId());
                                Log.d("ChatRoomList", "Found owned garage: " + garage.getName() + " (ID: " + garage.getId() + ")");
                            }
                        }
                        
                        if (ownedGarageIds.isEmpty()) {
                            Log.w("ChatRoomList", "No garages found for user " + userId);
                            Toast.makeText(ChatRoomListActivity.this, 
                                "Bạn chưa có garage nào. Vui lòng tạo garage trước.", 
                                Toast.LENGTH_LONG).show();
                            showLoading(false);
                            rvChatRooms.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            return;
                        }
                        
                        // Load chat rooms cho tất cả garage của owner
                        loadChatRoomsForMultipleGarages(ownedGarageIds);
                    } else {
                        Log.e("ChatRoomList", "Failed to get garages: " + garageResponse.getMessage());
                        Toast.makeText(ChatRoomListActivity.this, 
                            "Lỗi: " + garageResponse.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                        showLoading(false);
                        rvChatRooms.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("ChatRoomList", "Failed to load garages: " + response.code());
                    Toast.makeText(ChatRoomListActivity.this, 
                        "Lỗi khi tải danh sách garage", 
                        Toast.LENGTH_SHORT).show();
                    showLoading(false);
                    rvChatRooms.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<GarageResponse> call, Throwable t) {
                Log.e("ChatRoomList", "Network error loading garages: " + t.getMessage());
                Toast.makeText(ChatRoomListActivity.this, 
                    "Lỗi mạng: " + t.getMessage(), 
                    Toast.LENGTH_SHORT).show();
                showLoading(false);
                rvChatRooms.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }
    
    /**
     * Load và merge chat rooms từ nhiều garage
     */
    private void loadChatRoomsForMultipleGarages(List<Integer> garageIds) {
        Log.d("ChatRoomList", "Loading chat rooms for " + garageIds.size() + " garages");
        
        final List<ChatRoom> allChatRooms = new ArrayList<>();
        final int[] completedRequests = {0};
        final int totalRequests = garageIds.size();
        
        for (Integer garageId : garageIds) {
            chatRepository.getGarageChatRooms(garageId).observe(ChatRoomListActivity.this, result -> {
                completedRequests[0]++;
                
                if (result != null && result.status == Result.Status.SUCCESS && result.data != null) {
                    allChatRooms.addAll(result.data);
                    Log.d("ChatRoomList", "Loaded " + result.data.size() + " chat rooms from garage " + garageId);
                }
                
                // Khi đã load xong tất cả garage
                if (completedRequests[0] == totalRequests) {
                    showLoading(false);
                    
                    if (!allChatRooms.isEmpty()) {
                        adapter.setChatRooms(allChatRooms);
                        rvChatRooms.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);
                        Log.d("ChatRoomList", "Total chat rooms loaded: " + allChatRooms.size());
                    } else {
                        rvChatRooms.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        Log.d("ChatRoomList", "No chat rooms found");
                    }
                }
            });
        }
    }
    
    /**
     * Observe chat rooms LiveData (dùng cho customer và staff)
     */
    private void observeChatRooms(LiveData<Result<List<ChatRoom>>> chatRoomsLiveData) {
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
