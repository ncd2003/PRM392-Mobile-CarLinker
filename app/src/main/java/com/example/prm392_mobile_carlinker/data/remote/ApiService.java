package com.example.prm392_mobile_carlinker.data.remote;

import com.example.prm392_mobile_carlinker.data.model.auth.LoginRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.model.auth.PartnerRegisterRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.PartnerRegisterResponse;
import com.example.prm392_mobile_carlinker.data.model.auth.RegisterRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.CartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatApiResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatMessage;
import com.example.prm392_mobile_carlinker.data.model.chat.ChatRoom;
import com.example.prm392_mobile_carlinker.data.model.chat.CreateChatRoomRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.EditMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.HideMessageResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.AddRoomMemberRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.RoomMember;
import com.example.prm392_mobile_carlinker.data.model.chat.RemoveMemberResponse;
import com.example.prm392_mobile_carlinker.data.model.chat.SendMessageRequest;
import com.example.prm392_mobile_carlinker.data.model.chat.UploadFileResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageListResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageCreateResponse;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageUpdateServiceItem;
import com.example.prm392_mobile_carlinker.data.model.garage.GarageServiceItemResponse;
import com.example.prm392_mobile_carlinker.data.model.order.CreateOrderRequest;
import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.data.model.order.OrderListResponse;
import com.example.prm392_mobile_carlinker.data.model.order.UpdateOrderStatusRequest;
import com.example.prm392_mobile_carlinker.data.model.payment.VNPayResponse;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.product.ProductResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryResponse;
import com.example.prm392_mobile_carlinker.data.model.servicecategory.ServiceCategoryUpdateRequest;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemResponse;
import com.example.prm392_mobile_carlinker.data.model.serviceitem.ServiceItemUpdateRequest;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffListResponse;
import com.example.prm392_mobile_carlinker.data.model.garagestaff.GarageStaffUpdateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordCreateRequest;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordResponse;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.ServiceRecordListResponse;
import com.example.prm392_mobile_carlinker.data.model.servicerecord.UpdateServiceRecordStatusRequest;
import com.example.prm392_mobile_carlinker.data.model.user.UserResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleListResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ============== PRODUCT APIs ==============

    // Get all products - Lấy danh sách sản phẩm (có thể filter theo CategoryId, BrandId, SortBy)
    @GET("api/Product/product-list")
    Call<ProductResponse> getProducts(
            @Query("CategoryId") Integer categoryId,
            @Query("BrandId") Integer brandId,
            @Query("SortBy") String sortBy
    );

    // Search products - Tìm kiếm sản phẩm
    @GET("api/Product/search")
    Call<ProductResponse> searchProducts(@Query("keyword") String keyword);

    // Get product detail by ID - Lấy chi tiết sản phẩm theo ID
    @GET("api/Product/{productId}")
    Call<ProductDetailResponse> getProductDetail(@Path("productId") int productId);

    // ============== CART APIs ==============

    // Get cart items - Lấy danh sách sản phẩm trong giỏ hàng
    @GET("api/Cart/get-list-cart-items")
    Call<CartResponse> getCartItems();

    // Add product to cart - Thêm sản phẩm vào giỏ hàng
    @POST("api/Cart/Add-product-to-cart")
    Call<AddToCartResponse> addToCart(@Body AddToCartRequest request);

    // Update cart item quantity - Cập nhật số lượng sản phẩm trong giỏ
    @PUT("api/Cart/update-quantity-item")
    Call<UpdateCartResponse> updateCartItem(@Body UpdateCartRequest request);

    // Remove item from cart - Xóa sản phẩm khỏi giỏ hàng
    @DELETE("api/Cart/remove-item/{productVariantId}")
    Call<BaseResponse> removeFromCart(@Path("productVariantId") int productVariantId);

    // ============== ORDER APIs ==============

    // Get my orders - Lấy danh sách đơn hàng của người dùng
    @GET("api/Order/my-orders")
    Call<OrderListResponse> getMyOrders();

    // Get order detail by ID - Lấy chi tiết đơn hàng theo ID
    @GET("api/Order/{orderId}")
    Call<OrderResponse> getOrderDetail(@Path("orderId") int orderId);

    // Create order - Tạo đơn hàng từ giỏ hàng
    @POST("api/Order/create-order")
    Call<OrderResponse> createOrder(@Body CreateOrderRequest request);

    // Cancel order - Hủy đơn hàng
    @PUT("api/Order/cancel/{orderId}")
    Call<BaseResponse> cancelOrder(@Path("orderId") int orderId);

    // ============== DEALER ORDER APIs ==============

    // Get all orders for dealer - Lấy tất cả đơn hàng cho dealer
    @GET("api/Order/all-orders")
    Call<OrderListResponse> getAllOrders();

    // Update order status - Cập nhật trạng thái đơn hàng (Dealer)
    @PUT("api/Order/update-status")
    Call<BaseResponse> updateOrderStatus(@Body UpdateOrderStatusRequest request);

    // ============== VNPAY PAYMENT APIs ==============

    // Create VNPay payment URL - Tạo URL thanh toán VNPay
    // Backend returns JSON object with payment URL in "data" field
    @GET("api/Vnpay/CreatePaymentUrl")
    Call<VNPayResponse> createVNPayPaymentUrl(
            @Query("orderId") int orderId,
            @Query("moneyToPay") double moneyToPay,
            @Query("description") String description
    );

    // Confirm VNPay payment - Xác nhận thanh toán thành công từ mobile app
    @POST("api/Order/confirm-payment/{orderId}")
    Call<BaseResponse> confirmPayment(@Path("orderId") int orderId);

    // ============== GARAGE APIs ==============

    // Get all garages - Lấy danh sách tất cả garage
    @GET("api/Garage")
    Call<GarageResponse> getAllGarages();

    // Get garage by id - Lấy garage by id
    @GET("api/Garage/{garageId}")
    Call<GarageResponse> getGarageById(int garageId);

    // Get garage details by id - Lấy chi tiết garage by id
    @GET("api/Garage/details/{garageId}")
    Call<GarageDetailResponse> getGarageDetailsById(@Path("garageId") int garageId);

    // Get garages by user ID - Lấy tất cả garage của một user
    @GET("api/Garage/user/{userId}")
    Call<GarageListResponse> getGaragesByUserId(@Path("userId") int userId);

    // Create garage - Tạo garage mới (GARAGE role after partner registration)
    @Multipart
    @POST("api/Garage")
    Call<GarageCreateResponse> createGarage(
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("description") RequestBody description,
            @Part("operatingTime") RequestBody operatingTime,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part MultipartBody.Part imageFile
    );

    // ============== SERVICE CATEGORY APIs ==============

    // Get all service categories - Lấy danh sách tất cả service categories (ADMIN only)
    @GET("api/ServiceCategory")
    Call<ServiceCategoryResponse> getAllServiceCategories(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("isAsc") boolean isAsc
    );

    // Get service category by id - Lấy service category theo id (ADMIN, GARAGE)
    @GET("api/ServiceCategory/{id}")
    Call<ServiceCategoryDetailResponse> getServiceCategoryById(@Path("id") int id);

    // Create service category - Tạo service category mới (ADMIN only)
    @POST("api/ServiceCategory")
    Call<ServiceCategoryDetailResponse> createServiceCategory(@Body ServiceCategoryCreateRequest request);

    // Update service category - Cập nhật service category (ADMIN only)
    @PATCH("api/ServiceCategory/{id}")
    Call<ServiceCategoryDetailResponse> updateServiceCategory(
            @Path("id") int id,
            @Body ServiceCategoryUpdateRequest request
    );

    // Delete service category - Xóa service category (ADMIN only)
    @DELETE("api/ServiceCategory/{id}")
    Call<BaseResponse> deleteServiceCategory(@Path("id") int id);

    // ============== SERVICE ITEM APIs ==============

    // Get all service items - Lấy danh sách tất cả service items (ADMIN only)
    @GET("api/ServiceItem")
    Call<ServiceItemResponse> getAllServiceItems(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("isAsc") boolean isAsc
    );

    // Get service item by id - Lấy service item theo id (ADMIN, GARAGE)
    @GET("api/ServiceItem/{id}")
    Call<ServiceItemDetailResponse> getServiceItemById(@Path("id") int id);

    // Create service item - Tạo service item mới (ADMIN only)
    @POST("api/ServiceItem")
    Call<ServiceItemDetailResponse> createServiceItem(@Body ServiceItemCreateRequest request);

    // Update service item - Cập nhật service item (ADMIN only)
    @PATCH("api/ServiceItem/{id}")
    Call<ServiceItemDetailResponse> updateServiceItem(
            @Path("id") int id,
            @Body ServiceItemUpdateRequest request
    );

    // Delete service item - Xóa service item (ADMIN only)
    @DELETE("api/ServiceItem/{id}")
    Call<BaseResponse> deleteServiceItem(@Path("id") int id);

    // ============== USER APIs ==============

    // Get user by id - Lấy thông tin user theo ID
    @GET("api/User/{id}")
    Call<UserResponse> getUserById(@Path("id") int id);

    // Login user
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Staff login
    @POST("api/Auth/Staff/login")
    Call<LoginResponse> staffLogin(@Body LoginRequest request);

    // Register new customer account
    @POST("api/Auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    // Register new partner (Garage Owner) account
    @POST("api/Auth/partner/register")
    Call<PartnerRegisterResponse> partnerRegister(@Body PartnerRegisterRequest request);

    // get all vehicles
    @GET("api/Vehicle/user")
    Call<VehicleListResponse> getAllVehicles();

    @GET("api/Vehicle/{id}")
    Call<VehicleResponse> getVehicleById(@Path("id") int id);

    @Multipart
    @POST("api/Vehicle")
    Call<VehicleResponse> addVehicle(
            @Part("licensePlate") RequestBody licensePlate,
            @Part("fuelType") RequestBody fuelType,
            @Part("transmissionType") RequestBody transmissionType,
            @Part("brand") RequestBody brand,
            @Part("model") RequestBody model,
            @Part("year") RequestBody year,
            @Part MultipartBody.Part imageFile
    );


    @Multipart
    @PATCH("api/Vehicle/{id}")
    Call<VehicleResponse> updateVehicle(
            @Path("id") int id,
            @Part("licensePlate") RequestBody licensePlate,
            @Part("fuelType") RequestBody fuelType,
            @Part("transmissionType") RequestBody transmissionType,
            @Part("brand") RequestBody brand,
            @Part("model") RequestBody model,
            @Part("year") RequestBody year,
            @Part MultipartBody.Part imageFile
    );
    @DELETE("api/Vehicle/{id}")
    Call<VehicleResponse> deleteVehicle(@Path("id") int id);

    // ============== CHAT APIs ==============

    // Upload media file (image/video/document) for chat
    @Multipart
    @POST("api/chat/upload")
    Call<ChatApiResponse<UploadFileResponse>> uploadChatMedia(
            @Part MultipartBody.Part file,
            @Part("fileType") RequestBody fileType
    );

    // Create or get chat room between customer and garage
    @POST("api/chat/rooms")
    Call<ChatApiResponse<ChatRoom>> createOrGetChatRoom(@Body CreateChatRoomRequest request);

    // Send message (text or media) in a chat room
    @POST("api/chat/messages")
    Call<ChatApiResponse<ChatMessage>> sendMessage(@Body SendMessageRequest request);

    // Get messages from a chat room with pagination
    @GET("api/chat/rooms/{roomId}/messages")
    Call<ChatApiResponse<List<ChatMessage>>> getChatMessages(
            @Path("roomId") long roomId,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    // Get all chat rooms for a customer
    @GET("api/chat/rooms/customer/{customerId}")
    Call<ChatApiResponse<List<ChatRoom>>> getCustomerChatRooms(@Path("customerId") int customerId);

    // Get all chat rooms for a garage
    @GET("api/chat/rooms/garage/{garageId}")
    Call<ChatApiResponse<List<ChatRoom>>> getGarageChatRooms(@Path("garageId") int garageId);

    // ============== UC-03: Edit / Hide Message APIs ==============

    // Edit message content (only by sender)
    @PATCH("api/chat/messages/{messageId}/edit")
    Call<ChatApiResponse<ChatMessage>> editMessage(
            @Path("messageId") long messageId,
            @Body EditMessageRequest request
    );

    // Hide message (soft delete - only by sender)
    @PATCH("api/chat/messages/{messageId}/hide")
    Call<ChatApiResponse<HideMessageResponse>> hideMessage(
            @Path("messageId") long messageId,
            @Body HideMessageRequest request
    );

    // ============== UC-04: Room Member Management APIs ==============

    // Get all members of a chat room
    @GET("api/chat/rooms/{roomId}/members")
    Call<ChatApiResponse<List<RoomMember>>> getRoomMembers(
            @Path("roomId") long roomId
    );

    // Add a member to a chat room
    @POST("api/chat/rooms/{roomId}/members")
    Call<ChatApiResponse<RoomMember>> addRoomMember(
            @Path("roomId") long roomId,
            @Body AddRoomMemberRequest request
    );

    // Remove a member from a chat room
    @DELETE("api/chat/rooms/{roomId}/members/{memberId}")
    Call<ChatApiResponse<RemoveMemberResponse>> removeRoomMember(
            @Path("roomId") long roomId,
            @Path("memberId") long memberId
    );

    // ============== SERVICE RECORD APIs ==============

    // Create service record - Đặt lịch dịch vụ mới (CUSTOMER only)
    // garageId được truyền vào path parameter
    @POST("api/ServiceRecord/{garageId}")
    Call<ServiceRecordResponse> createServiceRecord(
            @Path("garageId") int garageId,
            @Body ServiceRecordCreateRequest request
    );

    // Get all service records - Lấy danh sách tất cả service records (STAFF only)
    @GET("api/ServiceRecord")
    Call<ServiceRecordListResponse> getAllServiceRecords(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("isAsc") boolean isAsc
    );

    // Update service record status - Cập nhật trạng thái service record (STAFF only)
    @PATCH("api/ServiceRecord/{id}")
    Call<BaseResponse> updateServiceRecordStatus(
            @Path("id") int id,
            @Body UpdateServiceRecordStatusRequest request
    );

    // ============== GARAGE STAFF APIs ==============

    // Get all garage staff - Lấy danh sách nhân viên garage (GARAGE only)
    @GET("api/GarageStaff")
    Call<GarageStaffListResponse> getAllGarageStaff(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("isAsc") boolean isAsc
    );

    // Get garage staff by id - Lấy chi tiết nhân viên (GARAGE only)
    @GET("api/GarageStaff/{id}")
    Call<GarageStaffDetailResponse> getGarageStaffById(@Path("id") int id);

    // Create garage staff - Tạo nhân viên mới (GARAGE only)
    @POST("api/GarageStaff")
    Call<GarageStaffDetailResponse> createGarageStaff(@Body GarageStaffCreateRequest request);

    // Update garage staff - Cập nhật thông tin nhân viên (GARAGE only)
    @PATCH("api/GarageStaff/{id}")
    Call<GarageStaffDetailResponse> updateGarageStaff(
            @Path("id") int id,
            @Body GarageStaffUpdateRequest request
    );

    // Update garage staff image - Cập nhật ảnh nhân viên (GARAGE only)
    @Multipart
    @PATCH("api/GarageStaff/image/{id}")
    Call<GarageStaffDetailResponse> updateGarageStaffImage(
            @Path("id") int id,
            @Part MultipartBody.Part imageFile
    );

    // Delete garage staff - Xóa nhân viên (GARAGE only)
    @DELETE("api/GarageStaff/{id}")
    Call<BaseResponse> deleteGarageStaff(@Path("id") int id);

    // ============== GARAGE SERVICE ITEMS APIs ==============

    // Update garage service items - Cập nhật danh sách dịch vụ và giá của garage (GARAGE role)
    @PATCH("api/Garage/service-items")
    Call<BaseResponse> updateGarageServiceItems(@Body List<GarageUpdateServiceItem> request);
}
