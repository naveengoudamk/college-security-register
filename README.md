# 🏛️ College Security & Hostel Management System

A comprehensive digital solution to replace manual register books at college and hostel security cabins. This system enables efficient tracking of student and visitor movements with enhanced security features.

## ✨ Features

### 🔐 Student Entry-Exit Module
- Student ID / Roll Number based entry
- Hostel name & room number tracking
- Automatic in-time / out-time recording
- Late entry flag (after configured time)
- QR code support for quick entry

### 🧾 Visitor Register Module
- Visitor name & phone recording
- Purpose of visit tracking
- Student roll number association
- Visitor in/out time logging
- ID type verification (Aadhar/PAN/DL)
- Blacklist functionality

### 🛏️ Hostel Management Module
- Hostel-wise logs (Boys / Girls)
- Room-wise student mapping
- Configurable late entry time per hostel
- Active student tracking

### 🧑‍✈️ Security Guard Dashboard
- Quick entry/exit forms
- Visitor registration
- Shift login/logout tracking
- Today's entries at a glance
- Real-time updates

### 🧑‍🏫 Admin / Warden Panel
- View late-night entries
- Search student or visitor history
- Date range reports
- Blacklist visitor management
- Comprehensive statistics

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: MySQL
- **Build Tool**: Maven
- **Security**: Spring Security
- **ORM**: Spring Data JPA

### Frontend
- **HTML5** - Structure
- **CSS3** - Modern styling with gradients and animations
- **JavaScript** - Dynamic functionality
- **Responsive Design** - Mobile-first approach

### Additional Libraries
- **ZXing** - QR code generation
- **JavaMail** - Email notifications
- **iText** - PDF report generation
- **Apache POI** - Excel report generation

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Modern web browser

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/college-security-register.git
cd college-security-register
```

### 2. Configure Database
Create a MySQL database:
```sql
CREATE DATABASE college_security;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_security
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### 3. Configure Email (Optional)
For email notifications, update in `application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 👤 Demo Credentials

### Security Guard
- **Username**: GUARD001
- **Password**: guard123

### Admin
- **Username**: ADMIN001
- **Password**: admin123

### Warden
- **Username**: WARDEN001
- **Password**: warden123

## 📱 Usage

### Security Guard
1. Login with guard credentials
2. Record student entry by entering roll number
3. Record student exit
4. Register visitors with student details
5. View today's entries and active visitors

### Admin / Warden
1. Login with admin/warden credentials
2. Monitor late entries in real-time
3. Search student entry/exit history
4. Generate date range reports
5. Blacklist visitors if needed
6. View comprehensive statistics

## 🗂️ Project Structure

```
college-security-register/
├── src/
│   ├── main/
│   │   ├── java/com/college/security/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST API controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── model/           # Entity models
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── static/          # Frontend files
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   ├── index.html
│   │       │   ├── guard-dashboard.html
│   │       │   └── admin-panel.html
│   │       └── application.properties
│   └── test/                    # Test files
└── pom.xml                      # Maven configuration
```

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/login/guard` - Guard login
- `POST /api/auth/login/warden` - Warden/Admin login
- `POST /api/auth/logout/guard/{id}` - Guard logout

### Students
- `POST /api/students` - Register student
- `GET /api/students/{rollNumber}` - Get student details
- `GET /api/students/{rollNumber}/qr` - Get QR code
- `GET /api/students/search?name={name}` - Search students

### Entry Logs
- `POST /api/entry` - Record entry
- `POST /api/entry/exit` - Record exit
- `GET /api/entry/today` - Today's entries
- `GET /api/entry/late` - Late entries
- `GET /api/entry/student/{rollNumber}` - Student history
- `GET /api/entry/range` - Date range entries

### Visitors
- `POST /api/visitors` - Register visitor
- `POST /api/visitors/{id}/exit` - Record visitor exit
- `GET /api/visitors/active` - Active visitors
- `GET /api/visitors/today` - Today's visitors
- `POST /api/visitors/{id}/blacklist` - Blacklist visitor
- `GET /api/visitors/blacklisted` - Blacklisted visitors

## 🎨 Features Highlights

- ✅ **Modern UI** - Premium design with gradients and animations
- ✅ **Real-time Updates** - Auto-refresh every 30-60 seconds
- ✅ **Responsive Design** - Works on mobile, tablet, and desktop
- ✅ **QR Code Support** - Quick student identification
- ✅ **Email Notifications** - Late entry alerts to wardens
- ✅ **Comprehensive Reports** - Date range and student-wise reports
- ✅ **Visitor Blacklist** - Security enhancement
- ✅ **Role-based Access** - Guard, Warden, and Admin roles

## 🔒 Security Features

- Password-based authentication
- Role-based access control
- Session management
- CORS configuration
- Input validation

## 📊 Database Schema

- **students** - Student information
- **hostels** - Hostel details
- **entry_logs** - Entry/exit records
- **visitors** - Visitor information
- **security_guards** - Guard details
- **wardens** - Warden/Admin details

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Developer

**Naveengouda M K**

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- ZXing library for QR code generation
- All contributors and testers

---

**Note**: This is a college project designed to demonstrate a digital security management system. For production use, additional security measures and testing are recommended.
