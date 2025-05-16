# Sport Booking App

Ứng dụng di động Android giúp kết nối người chơi thể thao với các sân, sân vận động và cơ sở thể thao. Mục tiêu của ứng dụng là tạo ra một nền tảng thuận tiện cho việc tìm kiếm, đặt sân và quản lý hoạt động thể thao.

## Tính năng chính

- Tìm kiếm sân thể thao theo vị trí
- Xem thông tin chi tiết sân thể thao
- Đặt sân và quản lý lịch đặt
- Tích hợp bản đồ Google Maps
- Hệ thống đánh giá và nhận xét
- Thanh toán trực tuyến
- Quản lý tài khoản người dùng

## Yêu cầu hệ thống

- Android Studio Hedgehog | 2023.1.1 hoặc cao hơn
- JDK 11 hoặc cao hơn
- Minimum SDK: API 26 (Android 8.0)
- Target SDK: API 34 (Android 14)

## Công nghệ sử dụng

- **Ngôn ngữ**: Java
- **Framework**: Android SDK
- **Database**: SQLite/Room
- **Network**: Retrofit2, OkHttp3
- **Image Loading**: Glide
- **Maps**: Google Maps SDK
- **UI Components**:
  - Material Design Components
  - RecyclerView
  - ViewPager2
  - FlexboxLayout
  - CircleImageView
  - CircleIndicator

## Cài đặt

1. Clone repository:

```bash
git clone [repository-url]
```

2. Mở project trong Android Studio

3. Sync Gradle và cài đặt dependencies

4. Cấu hình Google Maps API Key trong `local.properties`:

```properties
MAPS_API_KEY=your_api_key_here
```

5. Build và chạy ứng dụng

## Cấu trúc Project

```
app/
├── src/
│   ├── main/
│   │   ├── java/                 # Mã nguồn
│   │   ├── res/                  # Resources
│   │   └── AndroidManifest.xml   # Cấu hình ứng dụng
│   └── test/                     # Unit tests
├── build.gradle.kts              # Cấu hình build
└── proguard-rules.pro           # ProGuard rules
```
