package com.example.prm392_mobile_carlinker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.RemoveMemberResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.RoomMember;
import com.example.prm392_mobile_carlinker.data.repository.ChatRepository;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.ui.adapter.RoomMemberAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * UC-04: Activity for managing chat room members
 * Features:
 * - Display list of room members
 * - Add new members (staff/admin only)
 * - Remove members with confirmation
 */
public class RoomMembersActivity extends AppCompatActivity implements RoomMemberAdapter.OnMemberActionListener {
    
    private static final String TAG = "RoomMembersActivity";
    
    // UI Components
    private RecyclerView recyclerViewMembers;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddMember;
    
    // Data
    private ChatRepository chatRepository;
    private RoomMemberAdapter memberAdapter;
    private long roomId;
    private List<RoomMember> memberList = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_members);
        
        // Get room ID from intent
        roomId = getIntent().getLongExtra("ROOM_ID", -1);
        if (roomId == -1) {
            Toast.makeText(this, "Invalid room ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Room Members");
        }
        
        // Initialize repository
        chatRepository = new ChatRepository();
        
        // Initialize views
        initViews();
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Load members
        loadRoomMembers();
        
        // Setup FAB
        fabAddMember.setOnClickListener(v -> showAddMemberDialog());
    }
    
    private void initViews() {
        recyclerViewMembers = findViewById(R.id.recyclerViewMembers);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddMember = findViewById(R.id.fabAddMember);
    }
    
    private void setupRecyclerView() {
        memberAdapter = new RoomMemberAdapter(this, memberList, this);
        recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMembers.setAdapter(memberAdapter);
    }
    
    private void loadRoomMembers() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        
        chatRepository.getRoomMembers(roomId).observe(this, new Observer<Result<List<RoomMember>>>() {
            @Override
            public void onChanged(Result<List<RoomMember>> result) {
                progressBar.setVisibility(View.GONE);
                
                if (result != null && result.status == Result.Status.SUCCESS) {
                    List<RoomMember> members = result.data;
                    if (members != null && !members.isEmpty()) {
                        memberList.clear();
                        memberList.addAll(members);
                        memberAdapter.notifyDataSetChanged();
                        tvEmptyState.setVisibility(View.GONE);
                    } else {
                        memberList.clear();
                        memberAdapter.notifyDataSetChanged();
                        tvEmptyState.setVisibility(View.VISIBLE);
                    }
                } else if (result != null && result.status == Result.Status.ERROR) {
                    String errorMessage = result.message;
                    Toast.makeText(RoomMembersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    tvEmptyState.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    private void showAddMemberDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_member, null);
        
        EditText etUserId = dialogView.findViewById(R.id.etUserId);
        RadioGroup rgUserType = dialogView.findViewById(R.id.rgUserType);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Member")
                .setView(dialogView)
                .setPositiveButton("Add", null) // Set to null to override later
                .setNegativeButton("Cancel", null)
                .create();
        
        dialog.setOnShowListener(dialogInterface -> {
            Button btnAdd = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnAdd.setOnClickListener(v -> {
                String userIdStr = etUserId.getText().toString().trim();
                
                if (userIdStr.isEmpty()) {
                    etUserId.setError("User ID is required");
                    return;
                }
                
                try {
                    int userId = Integer.parseInt(userIdStr);
                    
                    // Get user type from radio group
                    int userType = 1; // Default to STAFF
                    int selectedId = rgUserType.getCheckedRadioButtonId();
                    if (selectedId == R.id.rbAdmin) {
                        userType = 2; // ADMIN
                    }
                    
                    // Add member
                    addMember(userId, userType);
                    dialog.dismiss();
                    
                } catch (NumberFormatException e) {
                    etUserId.setError("Invalid user ID");
                }
            });
        });
        
        dialog.show();
    }
    
    private void addMember(int userId, int userType) {
        progressBar.setVisibility(View.VISIBLE);
        
        chatRepository.addRoomMember(roomId, userId, userType).observe(this, new Observer<Result<RoomMember>>() {
            @Override
            public void onChanged(Result<RoomMember> result) {
                progressBar.setVisibility(View.GONE);
                
                if (result != null && result.status == Result.Status.SUCCESS) {
                    RoomMember newMember = result.data;
                    if (newMember != null) {
                        memberList.add(newMember);
                        memberAdapter.notifyItemInserted(memberList.size() - 1);
                        tvEmptyState.setVisibility(View.GONE);
                        Toast.makeText(RoomMembersActivity.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                    }
                } else if (result != null && result.status == Result.Status.ERROR) {
                    String errorMessage = result.message;
                    Toast.makeText(RoomMembersActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    
    @Override
    public void onRemoveMember(RoomMember member, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Member")
                .setMessage("Are you sure you want to remove " + member.getUserName() + " from this room?")
                .setPositiveButton("Remove", (dialog, which) -> removeMember(member, position))
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void removeMember(RoomMember member, int position) {
        progressBar.setVisibility(View.VISIBLE);
        
        chatRepository.removeRoomMember(roomId, member.getId()).observe(this, new Observer<Result<RemoveMemberResponse>>() {
            @Override
            public void onChanged(Result<RemoveMemberResponse> result) {
                progressBar.setVisibility(View.GONE);
                
                if (result != null && result.status == Result.Status.SUCCESS) {
                    memberList.remove(position);
                    memberAdapter.notifyItemRemoved(position);
                    Toast.makeText(RoomMembersActivity.this, "Member removed successfully", Toast.LENGTH_SHORT).show();
                    
                    if (memberList.isEmpty()) {
                        tvEmptyState.setVisibility(View.VISIBLE);
                    }
                } else if (result != null && result.status == Result.Status.ERROR) {
                    String errorMessage = result.message;
                    Toast.makeText(RoomMembersActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
