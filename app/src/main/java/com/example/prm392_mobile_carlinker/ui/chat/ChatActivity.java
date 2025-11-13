package com.example.prm392_mobile_carlinker.ui.chat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_mobile_carlinker.R;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatMessage;
import com.example.prm392_mobile_carlinker.data.model.chat.EditMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.FileType;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.MessageType;
import com.example.prm392_mobile_carlinker.data.model.chat.SendMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.UploadFileResponse;
import com.example.prm392_mobile_carlinker.data.repository.ChatRepository;
import com.example.prm392_mobile_carlinker.data.repository.Result;
import com.example.prm392_mobile_carlinker.ui.adapter.ChatMessageAdapter;

import java.io.File;
import java.util.List;

/**
 * Activity for displaying and sending chat messages
 */
public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_ROOM_ID = "room_id";
    public static final String EXTRA_GARAGE_ID = "garage_id";
    public static final String EXTRA_GARAGE_NAME = "garage_name";
    public static final String EXTRA_CUSTOMER_ID = "customer_id";

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private ImageButton btnAttach;
    private ProgressBar progressBar;
    private TextView tvGarageName;

    private ChatMessageAdapter adapter;
    private ChatRepository chatRepository;

    private long roomId;
    private int garageId;
    private int customerId;
    private String garageName;

    private Uri selectedImageUri;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get intent extras
        roomId = getIntent().getLongExtra(EXTRA_ROOM_ID, 0);
        garageId = getIntent().getIntExtra(EXTRA_GARAGE_ID, 0);
        customerId = getIntent().getIntExtra(EXTRA_CUSTOMER_ID, 0);
        garageName = getIntent().getStringExtra(EXTRA_GARAGE_NAME);

        initializeViews();
        setupRecyclerView();
        setupImagePicker();
        
        chatRepository = new ChatRepository();

        // Load messages
        loadMessages();

        // Setup button listeners
        btnSend.setOnClickListener(v -> sendTextMessage());
        btnAttach.setOnClickListener(v -> checkPermissionAndPickImage());
    }

    private void initializeViews() {
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        btnAttach = findViewById(R.id.btn_attach);
        progressBar = findViewById(R.id.progress_bar);
        tvGarageName = findViewById(R.id.tv_garage_name);

        // Set garage name in toolbar
        if (garageName != null) {
            tvGarageName.setText(garageName);
        }
    }

    private void setupRecyclerView() {
        adapter = new ChatMessageAdapter(this, customerId);
        
        // Set long click listener for edit/delete
        adapter.setOnMessageLongClickListener(this::showMessageOptionsDialog);
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        uploadAndSendImage();
                    }
                }
        );
    }

    private void loadMessages() {
        showLoading(true);
        
        chatRepository.getChatMessages(roomId, 1, 50).observe(this, result -> {
            showLoading(false);
            
            if (result != null && result.status == Result.Status.SUCCESS) {
                List<ChatMessage> messages = result.data;
                adapter.setMessages(messages);
                scrollToBottom();
            } else if (result != null && result.status == Result.Status.ERROR) {
                String error = result.message;
                Toast.makeText(this, "Failed to load messages: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendTextMessage() {
        String messageText = etMessage.getText().toString().trim();
        
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        
        SendMessageRequest request = new SendMessageRequest(
                roomId,
                0, // CUSTOMER
                customerId,
                messageText,
                MessageType.TEXT.getValue(),
                null,
                null
        );

        chatRepository.sendMessage(request).observe(this, result -> {
            showLoading(false);
            
            if (result != null && result.status == Result.Status.SUCCESS) {
                ChatMessage message = result.data;
                adapter.addMessage(message);
                etMessage.setText("");
                scrollToBottom();
            } else if (result != null && result.status == Result.Status.ERROR) {
                String error = result.message;
                Toast.makeText(this, "Failed to send message: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void uploadAndSendImage() {
        if (selectedImageUri == null) {
            return;
        }

        showLoading(true);

        // Convert URI to File
        File imageFile = getFileFromUri(selectedImageUri);
        if (imageFile == null) {
            showLoading(false);
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image
        chatRepository.uploadChatMedia(imageFile, FileType.IMAGE.getValue())
                .observe(this, uploadResult -> {
                    if (uploadResult != null && uploadResult.status == Result.Status.SUCCESS) {
                        UploadFileResponse uploadResponse = uploadResult.data;
                        sendMediaMessage(uploadResponse.getFileUrl(), FileType.IMAGE.getValue());
                    } else if (uploadResult != null && uploadResult.status == Result.Status.ERROR) {
                        showLoading(false);
                        String error = uploadResult.message;
                        Toast.makeText(this, "Upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendMediaMessage(String fileUrl, int fileType) {
        String caption = etMessage.getText().toString().trim();
        
        SendMessageRequest request = new SendMessageRequest(
                roomId,
                0, // CUSTOMER
                customerId,
                caption.isEmpty() ? null : caption,
                MessageType.MEDIA.getValue(),
                fileUrl,
                fileType
        );

        chatRepository.sendMessage(request).observe(this, result -> {
            showLoading(false);
            
            if (result != null && result.status == Result.Status.SUCCESS) {
                ChatMessage message = result.data;
                adapter.addMessage(message);
                etMessage.setText("");
                selectedImageUri = null;
                scrollToBottom();
            } else if (result != null && result.status == Result.Status.ERROR) {
                String error = result.message;
                Toast.makeText(this, "Failed to send image: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getFileFromUri(Uri uri) {
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                return new File(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scrollToBottom() {
        if (adapter.getItemCount() > 0) {
            rvMessages.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSend.setEnabled(!show);
        btnAttach.setEnabled(!show);
    }

    /**
     * UC-03: Show options dialog for message (Edit/Delete)
     */
    private void showMessageOptionsDialog(ChatMessage message, int position) {
        // Only show options for user's own messages
        if (message.getSenderId() != customerId) {
            return;
        }

        // Can only edit text messages
        boolean canEdit = message.getMessageTypeEnum() == MessageType.TEXT;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message Options");

        String[] options;
        if (canEdit) {
            options = new String[]{"Edit", "Delete"};
        } else {
            options = new String[]{"Delete"};
        }

        builder.setItems(options, (dialog, which) -> {
            if (canEdit && which == 0) {
                // Edit
                showEditMessageDialog(message);
            } else {
                // Delete (which == 1 if canEdit, which == 0 if !canEdit)
                showDeleteConfirmationDialog(message);
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    /**
     * UC-03: Show edit message dialog
     */
    private void showEditMessageDialog(ChatMessage message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_message, null);
        builder.setView(dialogView);

        EditText etEditMessage = dialogView.findViewById(R.id.et_edit_message);
        etEditMessage.setText(message.getMessage());
        etEditMessage.setSelection(message.getMessage().length()); // Move cursor to end

        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        
        dialogView.findViewById(R.id.btn_save).setOnClickListener(v -> {
            String newMessage = etEditMessage.getText().toString().trim();
            
            if (newMessage.isEmpty()) {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newMessage.equals(message.getMessage())) {
                Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                return;
            }

            // Call edit API
            editMessage(message.getId(), newMessage);
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * UC-03: Edit message
     */
    private void editMessage(long messageId, String newContent) {
        showLoading(true);

        chatRepository.editMessage(messageId, newContent, customerId, 0) // 0 = CUSTOMER
                .observe(this, result -> {
                    showLoading(false);

                    if (result != null && result.status == Result.Status.SUCCESS) {
                        ChatMessage updatedMessage = result.data;
                        adapter.updateMessage(updatedMessage);
                        Toast.makeText(this, "Message edited", Toast.LENGTH_SHORT).show();
                    } else if (result != null && result.status == Result.Status.ERROR) {
                        String error = result.message;
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * UC-03: Show delete confirmation dialog
     */
    private void showDeleteConfirmationDialog(ChatMessage message) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Message")
                .setMessage("Are you sure you want to delete this message?")
                .setPositiveButton("Delete", (dialog, which) -> deleteMessage(message.getId()))
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * UC-03: Delete message (hide message)
     */
    private void deleteMessage(long messageId) {
        showLoading(true);

        chatRepository.hideMessage(messageId, customerId, 0) // 0 = CUSTOMER
                .observe(this, result -> {
                    showLoading(false);

                    if (result != null && result.status == Result.Status.SUCCESS) {
                        HideMessageResponse response = result.data;
                        adapter.removeMessage(messageId);
                        Toast.makeText(this, "Message deleted", Toast.LENGTH_SHORT).show();
                    } else if (result != null && result.status == Result.Status.ERROR) {
                        String error = result.message;
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_manage_members) {
            openRoomMembersActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * UC-04: Open Room Members Activity
     */
    private void openRoomMembersActivity() {
        Intent intent = new Intent(this, com.example.prm392_mobile_carlinker.ui.RoomMembersActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
