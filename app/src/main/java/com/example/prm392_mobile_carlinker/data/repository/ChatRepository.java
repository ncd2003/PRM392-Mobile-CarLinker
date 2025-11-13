package com.example.prm392_mobile_carlinker.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prm392_mobile_carlinker.data.model.chat.ChatApiResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatMessage;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;
import com.example.prm392_mobile_carlinker.data.model.chat.CreateChatRoomRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.EditMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.SendMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.UploadFileResponse;
import com.example.prm392_mobile_carlinker.data.remote.ApiService;
import com.example.prm392_mobile_carlinker.data.remote.RetrofitClient;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for Chat operations
 * Handles all chat-related API calls
 */
public class ChatRepository {

    private final ApiService apiService;

    public ChatRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * Upload a media file (image/video/document) for chat
     * @param file The file to upload
     * @param fileType 0=IMAGE, 1=VIDEO, 2=FILE
     */
    public LiveData<Result<UploadFileResponse>> uploadChatMedia(File file, int fileType) {
        MutableLiveData<Result<UploadFileResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        try {
            // Determine media type based on file extension
            String mimeType = getMimeType(file, fileType);
            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody fileTypePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(fileType));

            apiService.uploadChatMedia(filePart, fileTypePart).enqueue(new Callback<ChatApiResponse<UploadFileResponse>>() {
                @Override
                public void onResponse(Call<ChatApiResponse<UploadFileResponse>> call, Response<ChatApiResponse<UploadFileResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ChatApiResponse<UploadFileResponse> apiResponse = response.body();
                        if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                            result.setValue(Result.success(apiResponse.getData()));
                        } else {
                            result.setValue(Result.error(apiResponse.getMessage(), null));
                        }
                    } else {
                        result.setValue(Result.error("Failed to upload file: " + response.message(), null));
                    }
                }

                @Override
                public void onFailure(Call<ChatApiResponse<UploadFileResponse>> call, Throwable t) {
                    result.setValue(Result.error("Network error: " + t.getMessage(), null));
                }
            });
        } catch (Exception e) {
            result.setValue(Result.error("Error preparing file: " + e.getMessage(), null));
        }

        return result;
    }

    /**
     * Create or get a chat room between customer and garage
     * UC02: This is the main method for initiating a chat with a garage
     */
    public LiveData<Result<ChatRoom>> createOrGetChatRoom(int garageId, int customerId) {
        MutableLiveData<Result<ChatRoom>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        CreateChatRoomRequest request = new CreateChatRoomRequest(garageId, customerId);

        apiService.createOrGetChatRoom(request).enqueue(new Callback<ChatApiResponse<ChatRoom>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<ChatRoom>> call, Response<ChatApiResponse<ChatRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<ChatRoom> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Result.error("Failed to get chat room: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<ChatRoom>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Send a message (text or media) in a chat room
     */
    public LiveData<Result<ChatMessage>> sendMessage(SendMessageRequest request) {
        MutableLiveData<Result<ChatMessage>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        apiService.sendMessage(request).enqueue(new Callback<ChatApiResponse<ChatMessage>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<ChatMessage>> call, Response<ChatApiResponse<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<ChatMessage> apiResponse = response.body();
                    if (apiResponse.getStatus() == 201 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Result.error("Failed to send message: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<ChatMessage>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get messages from a chat room with pagination
     */
    public LiveData<Result<List<ChatMessage>>> getChatMessages(long roomId, int page, int pageSize) {
        MutableLiveData<Result<List<ChatMessage>>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        apiService.getChatMessages(roomId, page, pageSize).enqueue(new Callback<ChatApiResponse<List<ChatMessage>>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<List<ChatMessage>>> call, Response<ChatApiResponse<List<ChatMessage>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<List<ChatMessage>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Result.error("Failed to get messages: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<List<ChatMessage>>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get all chat rooms for a customer
     */
    public LiveData<Result<List<ChatRoom>>> getCustomerChatRooms(int customerId) {
        MutableLiveData<Result<List<ChatRoom>>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        apiService.getCustomerChatRooms(customerId).enqueue(new Callback<ChatApiResponse<List<ChatRoom>>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<List<ChatRoom>>> call, Response<ChatApiResponse<List<ChatRoom>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<List<ChatRoom>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Result.error("Failed to get chat rooms: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<List<ChatRoom>>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Get all chat rooms for a garage
     */
    public LiveData<Result<List<ChatRoom>>> getGarageChatRooms(int garageId) {
        MutableLiveData<Result<List<ChatRoom>>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        apiService.getGarageChatRooms(garageId).enqueue(new Callback<ChatApiResponse<List<ChatRoom>>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<List<ChatRoom>>> call, Response<ChatApiResponse<List<ChatRoom>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<List<ChatRoom>> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Result.error("Failed to get chat rooms: " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<List<ChatRoom>>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Helper method to determine MIME type from file and type
     */
    private String getMimeType(File file, int fileType) {
        String extension = getFileExtension(file.getName()).toLowerCase();

        switch (fileType) {
            case 0: // IMAGE
                switch (extension) {
                    case "jpg":
                    case "jpeg":
                        return "image/jpeg";
                    case "png":
                        return "image/png";
                    case "gif":
                        return "image/gif";
                    case "webp":
                        return "image/webp";
                    default:
                        return "image/*";
                }
            case 1: // VIDEO
                switch (extension) {
                    case "mp4":
                        return "video/mp4";
                    case "avi":
                        return "video/x-msvideo";
                    case "mov":
                        return "video/quicktime";
                    case "wmv":
                        return "video/x-ms-wmv";
                    default:
                        return "video/*";
                }
            case 2: // FILE/DOCUMENT
                switch (extension) {
                    case "pdf":
                        return "application/pdf";
                    case "doc":
                    case "docx":
                        return "application/msword";
                    case "xls":
                    case "xlsx":
                        return "application/vnd.ms-excel";
                    case "txt":
                        return "text/plain";
                    case "zip":
                        return "application/zip";
                    case "rar":
                        return "application/x-rar-compressed";
                    default:
                        return "application/octet-stream";
                }
            default:
                return "application/octet-stream";
        }
    }

    /**
     * Helper method to get file extension
     */
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * UC-03: Edit message content
     * Only the original sender can edit their message
     * @param messageId The ID of the message to edit
     * @param newContent The new message content
     * @param senderId The ID of the sender
     * @param senderType The type of sender (0=CUSTOMER, 1=STAFF, 2=ADMIN)
     */
    public LiveData<Result<ChatMessage>> editMessage(long messageId, String newContent, int senderId, int senderType) {
        MutableLiveData<Result<ChatMessage>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        EditMessageRequest request = new EditMessageRequest(newContent, senderId, senderType);

        apiService.editMessage(messageId, request).enqueue(new Callback<ChatApiResponse<ChatMessage>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<ChatMessage>> call, Response<ChatApiResponse<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<ChatMessage> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Handle HTTP error responses
                    if (response.code() == 403) {
                        result.setValue(Result.error("You don't have permission to edit this message", null));
                    } else if (response.code() == 404) {
                        result.setValue(Result.error("Message not found", null));
                    } else if (response.code() == 400) {
                        result.setValue(Result.error("Cannot edit this message", null));
                    } else {
                        result.setValue(Result.error("Failed to edit message: " + response.message(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<ChatMessage>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * UC-03: Hide message (soft delete)
     * Only the original sender can hide their message
     * @param messageId The ID of the message to hide
     * @param senderId The ID of the sender
     * @param senderType The type of sender (0=CUSTOMER, 1=STAFF, 2=ADMIN)
     */
    public LiveData<Result<HideMessageResponse>> hideMessage(long messageId, int senderId, int senderType) {
        MutableLiveData<Result<HideMessageResponse>> result = new MutableLiveData<>();
        result.setValue(Result.loading(null));

        HideMessageRequest request = new HideMessageRequest(senderId, senderType);

        apiService.hideMessage(messageId, request).enqueue(new Callback<ChatApiResponse<HideMessageResponse>>() {
            @Override
            public void onResponse(Call<ChatApiResponse<HideMessageResponse>> call, Response<ChatApiResponse<HideMessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatApiResponse<HideMessageResponse> apiResponse = response.body();
                    if (apiResponse.getStatus() == 200 && apiResponse.getData() != null) {
                        result.setValue(Result.success(apiResponse.getData()));
                    } else {
                        result.setValue(Result.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Handle HTTP error responses
                    if (response.code() == 403) {
                        result.setValue(Result.error("You don't have permission to delete this message", null));
                    } else if (response.code() == 404) {
                        result.setValue(Result.error("Message not found", null));
                    } else if (response.code() == 400) {
                        result.setValue(Result.error("Message is already deleted", null));
                    } else {
                        result.setValue(Result.error("Failed to delete message: " + response.message(), null));
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatApiResponse<HideMessageResponse>> call, Throwable t) {
                result.setValue(Result.error("Network error: " + t.getMessage(), null));
            }
        });

        return result;
    }
}

