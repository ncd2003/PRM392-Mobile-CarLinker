# 📚 Hướng dẫn Structure Package - Carlinker Mobile App

## 🎯 Tổng quan Architecture

Project này sử dụng **MVVM Pattern** (Model-View-ViewModel) với Java:

```
app/src/main/java/com/example/prm392_mobile_carlinker/
├── data/           → Data Layer (Model + Repository + API)
├── ui/             → Presentation Layer (Activity + Fragment + ViewModel)
├── util/           → Utilities (Constants, Helpers)
└── MainActivity.java
```

---

## 📦 1. Package `data/` - Data Layer

### 🎯 Mục đích
Xử lý toàn bộ logic liên quan đến dữ liệu (API calls, database, caching...)

### 📁 Cấu trúc

```
data/
├── model/          → Các class đại diện cho entities
├── repository/     → Trung gian giữa ViewModel và API
└── remote/         → Kết nối API (Retrofit)
```

---

### 📋 `data/model/` - Data Models

**Nhiệm vụ:** Định nghĩa cấu trúc dữ liệu (POJO/JavaBean)

**Các file cần tạo:**

#### `User.java`
```java
public class User {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String role; // "Customer" hoặc "GarageOwner"
    
    // Constructors, Getters, Setters
}
```

#### `Garage.java`
```java
public class Garage {
    private int id;
    private String name;
    private String address;
    private String district;
    private String phone;
    private double rating;
    private String openTime;
    private String closeTime;
    private List<String> services;
    
    // Constructors, Getters, Setters
}
```

#### `Booking.java`
```java
public class Booking {
    private int id;
    private int userId;
    private int garageId;
    private String dateTime;
    private String status; // "Pending", "Confirmed", "Completed", "Cancelled"
    private String serviceType;
    private int vehicleId;
    
    // Constructors, Getters, Setters
}
```

#### `Product.java`
```java
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private List<ProductVariant> variants;
    
    // Constructors, Getters, Setters
}
```

#### `Order.java`
```java
public class Order {
    private int id;
    private int userId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // "Pending", "Processing", "Shipped", "Delivered"
    private String paymentMethod; // "COD", "VNPay"
    private String createdAt;
    
    // Constructors, Getters, Setters
}
```

#### `ServiceRecord.java`
```java
public class ServiceRecord {
    private int id;
    private int garageId;
    private int vehicleId;
    private String serviceName;
    private String status; // "InProgress", "Completed", "Cancelled"
    private double cost;
    private String createdAt;
    
    // Constructors, Getters, Setters
}
```

#### `Vehicle.java`
```java
public class Vehicle {
    private int id;
    private int userId;
    private String licensePlate;
    private String brand;
    private String model;
    private int year;
    private String color;
    
    // Constructors, Getters, Setters
}
```

#### `EmergencyRequest.java`
```java
public class EmergencyRequest {
    private int id;
    private int userId;
    private double latitude;
    private double longitude;
    private String description;
    private String status; // "Pending", "Responded", "Resolved"
    private String createdAt;
    
    // Constructors, Getters, Setters
}
```

---

### 🔄 `data/repository/` - Repositories

**Nhiệm vụ:** 
- Gọi API từ `ApiService`
- Xử lý response và error
- Trả về `LiveData<Resource<T>>` cho ViewModel

**Pattern:**
```java
public class ExampleRepository {
    private ApiService apiService;
    
    public ExampleRepository() {
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }
    
    public LiveData<Resource<List<Garage>>> getGarages() {
        MutableLiveData<Resource<List<Garage>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        
        apiService.getGarages().enqueue(new Callback<List<Garage>>() {
            @Override
            public void onResponse(Call<List<Garage>> call, Response<List<Garage>> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                } else {
                    result.setValue(Resource.error("Error: " + response.message(), null));
                }
            }
            
            @Override
            public void onFailure(Call<List<Garage>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });
        
        return result;
    }
}
```

**Các file cần tạo:**
- `AuthRepository.java` - Xử lý login, register, logout
- `GarageRepository.java` - Lấy danh sách garage, chi tiết garage
- `BookingRepository.java` - Đặt lịch, lấy lịch sử booking
- `ProductRepository.java` - Lấy danh sách sản phẩm, chi tiết sản phẩm
- `OrderRepository.java` - Tạo đơn hàng, lấy lịch sử đơn hàng
- `EmergencyRepository.java` - Gửi yêu cầu cứu hộ

---

### 🌐 `data/remote/` - API Connection

#### `ApiService.java`
**Nhiệm vụ:** Định nghĩa các endpoint API (Retrofit interface)

```java
public interface ApiService {
    // Auth
    @POST("auth/login")
    Call<User> login(@Body LoginRequest request);
    
    @POST("auth/register")
    Call<User> register(@Body RegisterRequest request);
    
    // Garage
    @GET("garages")
    Call<List<Garage>> getGarages();
    
    @GET("garages/{id}")
    Call<Garage> getGarageDetail(@Path("id") int id);
    
    // Booking
    @POST("bookings")
    Call<Booking> createBooking(@Body BookingRequest request);
    
    @GET("bookings/user/{userId}")
    Call<List<Booking>> getUserBookings(@Path("userId") int userId);
    
    // Product
    @GET("products")
    Call<List<Product>> getProducts();
    
    @GET("products/{id}")
    Call<Product> getProductDetail(@Path("id") int id);
    
    // Order
    @POST("orders")
    Call<Order> createOrder(@Body OrderRequest request);
    
    @GET("orders/user/{userId}")
    Call<List<Order>> getUserOrders(@Path("userId") int userId);
    
    // Emergency
    @POST("emergency")
    Call<EmergencyRequest> sendEmergencyRequest(@Body EmergencyRequest request);
    
    // Vehicle
    @GET("vehicles/user/{userId}")
    Call<List<Vehicle>> getUserVehicles(@Path("userId") int userId);
    
    @POST("vehicles")
    Call<Vehicle> addVehicle(@Body Vehicle vehicle);
}
```

#### `RetrofitClient.java`
**Nhiệm vụ:** Config Retrofit instance (Singleton pattern)

```java
public class RetrofitClient {
    private static final String BASE_URL = "https://api.carlinker.com/";
    private static Retrofit retrofit = null;
    
    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor())
                .build();
                
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
}
```

---

## 🎨 2. Package `ui/` - Presentation Layer

### 🎯 Mục đích
Chứa toàn bộ giao diện người dùng (Activity, Fragment, ViewModel, Adapter)

### 📁 Cấu trúc

```
ui/
├── auth/           → Đăng nhập, đăng ký
├── home/           → Màn hình chính
├── booking/        → Đặt lịch dịch vụ
├── shop/           → Mua linh kiện
├── service/        → Theo dõi dịch vụ
├── emergency/      → Cứu hộ khẩn cấp
├── vehicle/        → Quản lý xe
├── transaction/    → Lịch sử giao dịch
├── adapter/        → RecyclerView Adapters
└── fragment/       → Bottom Navigation Fragments
```

---

### 🔐 `ui/auth/` - Authentication (Luồng 1)

**Chức năng:** Đăng nhập, đăng ký tài khoản

**Files:**

#### `LoginActivity.java`
```java
public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private AuthViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Init views
        // Setup ViewModel
        // Observe LiveData
        // Handle login click
    }
}
```

**Layout:** `res/layout/activity_login.xml`
- EditText cho email/phone
- EditText cho password
- Button Login
- TextView chuyển sang Register

#### `RegisterActivity.java`
**Layout:** `res/layout/activity_register.xml`
- Form đăng ký (username, email, phone, password)

#### `AuthViewModel.java`
```java
public class AuthViewModel extends ViewModel {
    private AuthRepository repository;
    private MutableLiveData<Resource<User>> loginResult;
    
    public AuthViewModel() {
        repository = new AuthRepository();
        loginResult = new MutableLiveData<>();
    }
    
    public void login(String email, String password) {
        // Call repository.login()
    }
    
    public LiveData<Resource<User>> getLoginResult() {
        return loginResult;
    }
}
```

---

### 🏠 `ui/home/` - Dashboard

**Chức năng:** Màn hình chính sau khi login

#### `DashboardActivity.java`
- Hiển thị menu chính
- Điều hướng đến các tính năng

**Layout:** `res/layout/activity_dashboard.xml`
- CardView cho từng tính năng (Booking, Shop, Emergency...)

---

### 📅 `ui/booking/` - Booking Service (Luồng 2)

**Chức năng:** Đặt lịch dịch vụ tại garage

#### `GarageListActivity.java`
- RecyclerView hiển thị danh sách garage
- SearchView để tìm kiếm

**Layout:** `res/layout/activity_garage_list.xml`

#### `GarageDetailActivity.java`
- Hiển thị thông tin chi tiết garage
- Lịch trống (khung giờ)
- Button "Đặt lịch"

**Layout:** `res/layout/activity_garage_detail.xml`

#### `BookingActivity.java`
- Form đặt lịch (chọn xe, chọn dịch vụ, ghi chú)
- Button xác nhận

**Layout:** `res/layout/activity_booking.xml`

#### `BookingHistoryActivity.java`
- RecyclerView hiển thị lịch sử booking
- Filter theo trạng thái

**Layout:** `res/layout/activity_booking_history.xml`

#### `BookingViewModel.java`
```java
public class BookingViewModel extends ViewModel {
    private BookingRepository repository;
    private MutableLiveData<Resource<List<Garage>>> garages;
    
    public void loadGarages() {
        // Call repository
    }
    
    public void createBooking(BookingRequest request) {
        // Call repository
    }
}
```

---

### 🛒 `ui/shop/` - E-commerce (Luồng 3)

**Chức năng:** Mua linh kiện

#### `ProductListActivity.java`
- RecyclerView hiển thị sản phẩm
- Filter theo category

#### `ProductDetailActivity.java`
- Hiển thị chi tiết sản phẩm
- Chọn variant (size, color...)
- Button "Thêm vào giỏ"

#### `CartActivity.java`
- RecyclerView hiển thị sản phẩm trong giỏ
- Tính tổng tiền
- Button "Thanh toán"

#### `CheckoutActivity.java`
- Form nhập địa chỉ giao hàng
- Chọn phương thức thanh toán (COD/VNPay)
- Button "Đặt hàng"

#### `OrderHistoryActivity.java`
- RecyclerView hiển thị lịch sử đơn hàng

#### `ShopViewModel.java`
```java
public class ShopViewModel extends ViewModel {
    private ProductRepository repository;
    private MutableLiveData<List<Product>> cart;
    
    public void addToCart(Product product) {
        // Add product to cart
    }
    
    public void checkout(OrderRequest request) {
        // Create order
    }
}
```

---

### 🔧 `ui/service/` - Service Tracking (Luồng 4)

**Chức năng:** Theo dõi dịch vụ tại garage

#### `ServiceTrackingActivity.java`
- RecyclerView hiển thị danh sách service record
- Hiển thị trạng thái (đang xử lý/hoàn tất)

**Layout:** `res/layout/activity_service_tracking.xml`

---

### 🚨 `ui/emergency/` - Emergency Rescue (Luồng 5)

**Chức năng:** Gửi yêu cầu cứu hộ khẩn cấp

#### `EmergencyActivity.java`
- MapView hiển thị vị trí hiện tại
- EditText mô tả tình trạng
- Button "Gửi yêu cầu cứu hộ"

**Layout:** `res/layout/activity_emergency.xml`

#### `EmergencyViewModel.java`
```java
public class EmergencyViewModel extends ViewModel {
    private EmergencyRepository repository;
    
    public void sendEmergencyRequest(double lat, double lng, String description) {
        // Call repository
    }
}
```

---

### 🚗 `ui/vehicle/` - Vehicle Management (Luồng 6a)

**Chức năng:** Quản lý xe của user

#### `VehicleListActivity.java`
- RecyclerView hiển thị danh sách xe
- FloatingActionButton "Thêm xe mới"

#### `VehicleDetailActivity.java`
- Form thêm/sửa xe (biển số, hãng, model, năm sản xuất...)

---

### 💰 `ui/transaction/` - Transaction History (Luồng 6b)

**Chức năng:** Lịch sử giao dịch

#### `TransactionHistoryActivity.java`
- RecyclerView hiển thị lịch sử (đơn hàng, thanh toán, dịch vụ)

---

### 🎨 `ui/adapter/` - RecyclerView Adapters

**Nhiệm vụ:** Tạo adapter cho RecyclerView

**Files cần tạo:**
- `GarageAdapter.java` - Hiển thị danh sách garage
- `ProductAdapter.java` - Hiển thị danh sách sản phẩm
- `BookingAdapter.java` - Hiển thị lịch sử booking
- `OrderAdapter.java` - Hiển thị lịch sử đơn hàng
- `VehicleAdapter.java` - Hiển thị danh sách xe

**Pattern:**
```java
public class GarageAdapter extends RecyclerView.Adapter<GarageAdapter.ViewHolder> {
    private List<Garage> garages;
    private OnItemClickListener listener;
    
    public interface OnItemClickListener {
        void onItemClick(Garage garage);
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate item layout
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind data to views
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress;
        ImageView ivGarage;
        // ...
    }
}
```

---

### 🧩 `ui/fragment/` - Bottom Navigation Fragments

**Nhiệm vụ:** Chứa các Fragment cho Bottom Navigation Bar

#### `HomeFragment.java`
- Tab "Trang chủ"
- Hiển thị dashboard (banner, garage nổi bật, sản phẩm hot)

#### `BookingFragment.java`
- Tab "Đặt lịch"
- Hiển thị lịch hẹn sắp tới

#### `ShopFragment.java`
- Tab "Cửa hàng"
- Hiển thị danh mục sản phẩm

#### `ProfileFragment.java`
- Tab "Cá nhân"
- Hiển thị thông tin user, menu cài đặt

**Layout cho mỗi Fragment:**
- `res/layout/fragment_home.xml`
- `res/layout/fragment_booking.xml`
- `res/layout/fragment_shop.xml`
- `res/layout/fragment_profile.xml`

**Setup trong MainActivity:**
```java
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        
        // Load default fragment
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, new HomeFragment())
            .commit();
    }
    
    private BottomNavigationView.OnItemSelectedListener navListener =
        item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_booking:
                    selectedFragment = new BookingFragment();
                    break;
                case R.id.nav_shop:
                    selectedFragment = new ShopFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
            return true;
        };
}
```

---

## 🛠️ 3. Package `util/` - Utilities

### 🎯 Mục đích
Chứa các class tiện ích dùng chung trong toàn bộ app

---

### 📝 `Constants.java`

**Nhiệm vụ:** Chứa các hằng số

```java
public class Constants {
    // API
    public static final String BASE_URL = "https://api.carlinker.com/";
    
    // SharedPreferences
    public static final String PREF_NAME = "CarlinkerPrefs";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    
    // Intent Extras
    public static final String EXTRA_GARAGE_ID = "garage_id";
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_BOOKING_ID = "booking_id";
    
    // Request Codes
    public static final int REQUEST_LOGIN = 1001;
    public static final int REQUEST_LOCATION = 1002;
    
    // Booking Status
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_CONFIRMED = "Confirmed";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";
    
    // Payment Methods
    public static final String PAYMENT_COD = "COD";
    public static final String PAYMENT_VNPAY = "VNPay";
}
```

---

### 📦 `Resource.java`

**Nhiệm vụ:** Wrapper class để handle trạng thái API response

```java
public class Resource<T> {
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
    
    private Status status;
    private T data;
    private String message;
    
    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
    
    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }
    
    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message);
    }
    
    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null);
    }
    
    // Getters
    public Status getStatus() { return status; }
    public T getData() { return data; }
    public String getMessage() { return message; }
}
```

**Cách sử dụng:**
```java
viewModel.getGarages().observe(this, resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            showLoading();
            break;
        case SUCCESS:
            hideLoading();
            updateUI(resource.getData());
            break;
        case ERROR:
            hideLoading();
            showError(resource.getMessage());
            break;
    }
});
```

---

### 🔧 `Utils.java`

**Nhiệm vụ:** Các hàm tiện ích dùng chung

```java
public class Utils {
    
    // Format date
    public static String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }
    
    // Format currency
    public static String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }
    
    // Validate email
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    // Validate phone
    public static boolean isValidPhone(String phone) {
        return phone.matches("^0[0-9]{9}$");
    }
    
    // Show toast
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    // Get from SharedPreferences
    public static void saveUserSession(Context context, int userId, String token) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.putString(Constants.KEY_TOKEN, token);
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.apply();
    }
    
    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }
    
    public static void clearSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }
}
```

---

## 📱 4. MainActivity.java

**Nhiệm vụ:** Entry point của app, chứa Bottom Navigation

```java
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private FrameLayout fragmentContainer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check login status
        if (!Utils.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        setContentView(R.layout.activity_main);
        
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNav = findViewById(R.id.bottom_navigation);
        
        bottomNav.setOnItemSelectedListener(navListener);
        
        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
        }
    }
    
    private BottomNavigationView.OnItemSelectedListener navListener =
        item -> {
            Fragment selectedFragment = null;
            
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_booking:
                    selectedFragment = new BookingFragment();
                    break;
                case R.id.nav_shop:
                    selectedFragment = new ShopFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
            
            return true;
        };
}
```

**Layout:** `res/layout/activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout>
    
    <FrameLayout
        android:id="@+id/fragment_container"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottom_navigation" />
    
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"
        app:layout_constraintBottom_toBottomOf="parent" />
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

**Menu:** `res/menu/bottom_nav_menu.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_home"
        android:icon="@drawable/ic_home"
        android:title="Trang chủ" />
    <item
        android:id="@+id/nav_booking"
        android:icon="@drawable/ic_calendar"
        android:title="Đặt lịch" />
    <item
        android:id="@+id/nav_shop"
        android:icon="@drawable/ic_shopping"
        android:title="Cửa hàng" />
    <item
        android:id="@+id/nav_profile"
        android:icon="@drawable/ic_person"
        android:title="Cá nhân" />
</menu>
```

---

## 🔄 Luồng dữ liệu MVVM

```
View (Activity/Fragment)
    ↓
ViewModel (observe LiveData)
    ↓
Repository (call API)
    ↓
ApiService (Retrofit)
    ↓
Backend API
```

**Ví dụ cụ thể:**

1. **User click button "Đặt lịch"** trong `BookingActivity`
2. `BookingActivity` gọi `viewModel.createBooking(request)`
3. `BookingViewModel` gọi `repository.createBooking(request)`
4. `BookingRepository` gọi `apiService.createBooking(request)`
5. API trả về response
6. Repository xử lý response → trả về `LiveData<Resource<Booking>>`
7. ViewModel expose LiveData cho View
8. View observe LiveData → update UI (show success/error)

---

## 📦 Dependencies cần thêm

Thêm vào `app/build.gradle`:

```gradle
dependencies {
    // Retrofit
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // OkHttp
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
    
    // ViewModel & LiveData
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.1'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.6.1'
    
    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.3.0'
    
    // CardView
    implementation 'androidx.cardview:cardview:1.0.0'
    
    // Material Design
    implementation 'com.google.android.material:material:1.9.0'
    
    // Glide (load images)
    implementation 'com.github.bumptech.glide:glide:4.15.1'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.15.1'
    
    // Google Maps (for Emergency)
    implementation 'com.google.android.gms:play-services-maps:18.1.0'
    implementation 'com.google.android.gms:play-services-location:21.0.1'
}
```

---

## 📋 Checklist triển khai

### Phase 1: Setup cơ bản
- [ ] Tạo structure packages
- [ ] Tạo các Model classes
- [ ] Setup Retrofit + ApiService
- [ ] Tạo Constants, Resource, Utils

### Phase 2: Authentication
- [ ] LoginActivity + layout
- [ ] RegisterActivity + layout
- [ ] AuthViewModel
- [ ] AuthRepository

### Phase 3: Main Navigation
- [ ] MainActivity với BottomNavigationView
- [ ] 4 Fragments (Home, Booking, Shop, Profile)

### Phase 4: Booking Flow
- [ ] GarageListActivity + Adapter
- [ ] GarageDetailActivity
- [ ] BookingActivity
- [ ] BookingHistoryActivity
- [ ] BookingViewModel + Repository

### Phase 5: Shop Flow
- [ ] ProductListActivity + Adapter
- [ ] ProductDetailActivity
- [ ] CartActivity
- [ ] CheckoutActivity
- [ ] OrderHistoryActivity
- [ ] ShopViewModel + Repository

### Phase 6: Other Features
- [ ] ServiceTrackingActivity
- [ ] EmergencyActivity (with Google Maps)
- [ ] VehicleListActivity + VehicleDetailActivity
- [ ] TransactionHistoryActivity

### Phase 7: Testing & Polish
- [ ] Test tất cả luồng
- [ ] Xử lý error cases
- [ ] Optimize UI/UX

---

## 🎯 Mapping với 6 luồng nghiệp vụ

| Luồng | Package | Activities chính |
|-------|---------|------------------|
| 1️⃣ Login/Register | `ui/auth/` | LoginActivity, RegisterActivity |
| 2️⃣ Booking Service | `ui/booking/` | GarageListActivity, BookingActivity |
| 3️⃣ Buying Parts | `ui/shop/` | ProductListActivity, CartActivity, CheckoutActivity |
| 4️⃣ Tracking Service | `ui/service/` | ServiceTrackingActivity |
| 5️⃣ Emergency Rescue | `ui/emergency/` | EmergencyActivity |
| 6️⃣ Vehicle & Transaction | `ui/vehicle/`, `ui/transaction/` | VehicleListActivity, TransactionHistoryActivity |

---

## 💡 Tips & Best Practices

### 1. Naming Convention
- **Activity:** `[Feature]Activity.java` (e.g., `LoginActivity.java`)
- **Fragment:** `[Feature]Fragment.java` (e.g., `HomeFragment.java`)
- **ViewModel:** `[Feature]ViewModel.java` (e.g., `BookingViewModel.java`)
- **Repository:** `[Feature]Repository.java` (e.g., `GarageRepository.java`)
- **Adapter:** `[Item]Adapter.java` (e.g., `GarageAdapter.java`)
- **Layout:** `activity_[feature].xml`, `fragment_[feature].xml`, `item_[type].xml`

### 2. Error Handling
```java
// Trong Activity/Fragment
viewModel.getData().observe(this, resource -> {
    switch (resource.getStatus()) {
        case LOADING:
            progressBar.setVisibility(View.VISIBLE);
            break;
        case SUCCESS:
            progressBar.setVisibility(View.GONE);
            // Update UI with resource.getData()
            break;
        case ERROR:
            progressBar.setVisibility(View.GONE);
            Utils.showToast(this, resource.getMessage());
            break;
    }
});
```

### 3. Navigation giữa Activities
```java
// Chuyển màn hình với data
Intent intent = new Intent(this, GarageDetailActivity.class);
intent.putExtra(Constants.EXTRA_GARAGE_ID, garageId);
startActivity(intent);

// Nhận data trong Activity đích
int garageId = getIntent().getIntExtra(Constants.EXTRA_GARAGE_ID, -1);
```

### 4. RecyclerView Setup
```java
RecyclerView recyclerView = findViewById(R.id.recyclerView);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
GarageAdapter adapter = new GarageAdapter(garageList, garage -> {
    // Handle item click
    openGarageDetail(garage.getId());
});
recyclerView.setAdapter(adapter);
```

### 5. Load Image với Glide
```java
Glide.with(context)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error_image)
    .into(imageView);
```

---

## 📞 Kết luận

Structure này đã được thiết kế để:
- ✅ Dễ bảo trì và mở rộng
- ✅ Tách biệt rõ ràng giữa các tầng (Data - Logic - UI)
- ✅ Tuân theo MVVM pattern
- ✅ Cover đầy đủ 6 luồng nghiệp vụ từ Readme

**Bắt đầu từ đâu?**
1. Tạo structure packages
2. Implement Authentication (Login/Register) trước
3. Setup MainActivity với Bottom Navigation
4. Implement từng feature một theo thứ tự ưu tiên

**Cần hỗ trợ thêm?**
- Code mẫu chi tiết cho từng Activity
- Layout XML samples
- API integration examples

---

📅 **Last updated:** 2025-10-30
👨‍💻 **Team:** PRM392 Mobile - Carlinker Project

