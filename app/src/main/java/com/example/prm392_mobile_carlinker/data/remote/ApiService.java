package com.example.prm392_mobile_carlinker.data.remote;

import com.example.prm392_mobile_carlinker.data.model.auth.LoginRequest;
import com.example.prm392_mobile_carlinker.data.model.auth.LoginResponse;
import com.example.prm392_mobile_carlinker.data.model.brand.BrandResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.AddToCartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.BaseResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.CartResponse;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartRequest;
import com.example.prm392_mobile_carlinker.data.model.cart.UpdateCartResponse;
import com.example.prm392_mobile_carlinker.data.model.order.CreateOrderRequest;
import com.example.prm392_mobile_carlinker.data.model.order.OrderResponse;
import com.example.prm392_mobile_carlinker.data.model.order.OrderListResponse;
import com.example.prm392_mobile_carlinker.data.model.order.UpdateOrderStatusRequest;
import com.example.prm392_mobile_carlinker.data.model.payment.VNPayResponse;
import com.example.prm392_mobile_carlinker.data.model.product.ProductDetailResponse;
import com.example.prm392_mobile_carlinker.data.model.product.ProductResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleListResponse;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleRequest;
import com.example.prm392_mobile_carlinker.data.model.vehicle.VehicleResponse;
import com.example.prm392_mobile_carlinker.data.model.category.CategoryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
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
    // Backend returns plain text URL, not JSON
    @GET("api/Vnpay/CreatePaymentUrl")
    Call<String> createVNPayPaymentUrl(
            @Query("orderId") int orderId,
            @Query("moneyToPay") double moneyToPay,
            @Query("description") String description
    );

    // Confirm VNPay payment - Xác nhận thanh toán thành công từ mobile app
    @POST("api/Order/confirm-payment/{orderId}")
    Call<BaseResponse> confirmPayment(@Path("orderId") int orderId);

    // Login user
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

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

    // get all Category
    @GET("api/Category")
    Call<CategoryResponse> getAllCategory();

    @GET("api/Brand")
    Call<BrandResponse> getAllBrand();
}

