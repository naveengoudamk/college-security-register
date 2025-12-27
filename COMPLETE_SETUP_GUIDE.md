# 🎓 College Security & Hostel Management System - Complete Setup Guide

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Installation Steps](#installation-steps)
4. [Running the Application](#running-the-application)
5. [Setting Up Demo Data](#setting-up-demo-data)
6. [Testing the Application](#testing-the-application)
7. [Troubleshooting](#troubleshooting)
8. [GitHub Repository](#github-repository)

---

## 🎯 Project Overview

A comprehensive digital security and hostel management system that replaces manual register books at college security cabins.

**Features:**
- ✅ Student Entry/Exit Tracking
- ✅ Visitor Registration & Management
- ✅ Hostel Management with Late Entry Detection
- ✅ Security Guard Dashboard
- ✅ Admin/Warden Panel with Reporting
- ✅ QR Code Support
- ✅ Email Notifications
- ✅ Real-time Updates

**Tech Stack:**
- Backend: Spring Boot 3.2.0 + Java 17
- Database: H2 (In-Memory) / MySQL
- Frontend: HTML5, CSS3, JavaScript
- Build Tool: Maven

---

## 📦 Prerequisites

Before you begin, ensure you have:

- ✅ **Java 17 or higher** installed
  ```bash
  java -version
  ```

- ✅ **Maven 3.6 or higher** installed
  ```bash
  mvn -version
  ```

- ✅ **Git** installed (for cloning from GitHub)
  ```bash
  git --version
  ```

- ✅ **Modern Web Browser** (Chrome, Firefox, Safari, Edge)

---

## 🚀 Installation Steps

### Step 1: Clone the Repository

```bash
# Clone from GitHub
git clone https://github.com/naveengoudamk/college-security-register.git

# Navigate to project directory
cd college-security-register
```

### Step 2: Build the Project

```bash
# Clean and build the project
mvn clean install
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: ~3-5 seconds
```

---

## ▶️ Running the Application

### Option A: Using H2 Database (Recommended for Testing)

**Current Configuration** - No MySQL installation required!

```bash
# Run the application
mvn spring-boot:run
```

**Expected Output:**
```
Started SecurityRegisterApplication in X.XXX seconds
Tomcat started on port 8080 (http)
```

### Option B: Using MySQL Database

If you prefer MySQL:

1. **Install and Start MySQL**
   ```bash
   brew install mysql
   brew services start mysql
   ```

2. **Create Database**
   ```sql
   CREATE DATABASE college_security;
   ```

3. **Update Configuration**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   # Comment out H2 configuration
   # spring.datasource.url=jdbc:h2:mem:college_security
   
   # Uncomment MySQL configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/college_security
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

4. **Enable Demo Data Loader**
   
   In `src/main/java/com/college/security/config/DataLoader.java`:
   ```java
   // Uncomment this line:
   @Component
   ```

5. **Restart Application**
   ```bash
   mvn spring-boot:run
   ```

---

## 🗄️ Setting Up Demo Data

### For H2 Database (Current Setup)

Since demo data loader is disabled, you need to insert data manually:

#### Step 1: Access H2 Console

1. **Open Browser**: http://localhost:8080/h2-console

2. **Login Credentials**:
   - JDBC URL: `jdbc:h2:mem:college_security`
   - Username: `sa`
   - Password: (leave blank)

3. **Click "Connect"**

#### Step 2: Insert Demo Data

Copy and paste this SQL into the H2 console:

```sql
-- Insert Hostels
INSERT INTO hostels (name, type, total_rooms, capacity, late_entry_time, active) 
VALUES ('Boys Hostel A', 'BOYS', 100, 200, '22:00:00', true);

INSERT INTO hostels (name, type, total_rooms, capacity, late_entry_time, active) 
VALUES ('Girls Hostel A', 'GIRLS', 80, 160, '21:30:00', true);

-- Insert Security Guards
INSERT INTO security_guards (guard_id, name, phone, password, shift, active, created_at) 
VALUES ('GUARD001', 'Ramesh Kumar', '9876543210', 'guard123', 'MORNING', true, CURRENT_TIMESTAMP);

INSERT INTO security_guards (guard_id, name, phone, password, shift, active, created_at) 
VALUES ('GUARD002', 'Suresh Patel', '9876543211', 'guard123', 'NIGHT', true, CURRENT_TIMESTAMP);

-- Insert Admin
INSERT INTO wardens (warden_id, name, email, phone, password, role, active, created_at) 
VALUES ('ADMIN001', 'Dr. Priya Sharma', 'admin@college.edu', '9876543212', 'admin123', 'ADMIN', true, CURRENT_TIMESTAMP);

-- Insert Warden
INSERT INTO wardens (warden_id, name, email, phone, password, role, hostel_id, active, created_at) 
VALUES ('WARDEN001', 'Mr. Rajesh Verma', 'warden@college.edu', '9876543213', 'warden123', 'WARDEN', 1, true, CURRENT_TIMESTAMP);

-- Insert Students
INSERT INTO students (roll_number, name, email, phone, department, year, hostel_id, room_number, active, created_at) 
VALUES ('CS2021001', 'Amit Patel', 'amit@student.college.edu', '9876543214', 'Computer Science', 3, 1, '101', true, CURRENT_TIMESTAMP);

INSERT INTO students (roll_number, name, email, phone, department, year, hostel_id, room_number, active, created_at) 
VALUES ('EC2021002', 'Priya Singh', 'priya@student.college.edu', '9876543215', 'Electronics', 2, 2, '201', true, CURRENT_TIMESTAMP);
```

Click **"Run"** to execute the SQL.

---

## 🧪 Testing the Application

### Step 1: Access the Application

**URL**: http://localhost:8080

You should see the **College Security Register** login page.

### Step 2: Login as Security Guard

1. **Select Tab**: "Security Guard"
2. **Credentials**:
   - User ID: `GUARD001`
   - Password: `guard123`
3. **Click**: "Login"

**Expected**: Guard Dashboard with:
- Statistics (Today's Entries, Active Students, Active Visitors)
- Forms for Student Entry/Exit
- Visitor Registration Form
- Real-time logs

### Step 3: Test Student Entry

1. **In "Record Student Entry" form**:
   - Roll Number: `CS2021001`
   - Click "Record Entry"

2. **Expected**: Success message and entry appears in "Today's Entry Log"

### Step 4: Test Visitor Registration

1. **In "Register Visitor" form**:
   - Visitor Name: `John Doe`
   - Phone: `9999999999`
   - Purpose: `Meeting`
   - Student Roll Number: `CS2021001`
   - ID Type: `Aadhar`
   - Click "Register Visitor"

2. **Expected**: Success message and visitor appears in "Today's Visitors"

### Step 5: Login as Admin

1. **Logout** from guard dashboard
2. **Select Tab**: "Warden / Admin"
3. **Credentials**:
   - User ID: `ADMIN001`
   - Password: `admin123`
4. **Click**: "Login"

**Expected**: Admin Panel with:
- Statistics Dashboard
- Late Entries View
- Active Visitors with Blacklist Option
- Search Functionality
- Report Generation

### Step 6: Test Search Functionality

1. **In "Search Student History" form**:
   - Roll Number: `CS2021001`
   - Click "Search"

2. **Expected**: Student's entry/exit history displayed

---

## 🔧 Troubleshooting

### Problem 1: Port 8080 Already in Use

**Error**: `Web server failed to start. Port 8080 was already in use.`

**Solution**:
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Then restart
mvn spring-boot:run
```

### Problem 2: Java Version Mismatch

**Error**: `Unsupported class file major version`

**Solution**:
```bash
# Check Java version
java -version

# Should be Java 17 or higher
# If not, install Java 17:
brew install openjdk@17
```

### Problem 3: Maven Not Found

**Error**: `mvn: command not found`

**Solution**:
```bash
# Install Maven
brew install maven

# Verify installation
mvn -version
```

### Problem 4: Build Failures

**Solution**:
```bash
# Clean Maven cache and rebuild
mvn clean install -U

# If still fails, delete target folder
rm -rf target
mvn clean install
```

### Problem 5: Database Connection Issues (MySQL)

**Error**: `Communications link failure`

**Solution**:
```bash
# Check if MySQL is running
brew services list | grep mysql

# Start MySQL if not running
brew services start mysql

# Verify connection
mysql -u root -p
```

---

## 📱 Application URLs

| Service | URL | Description |
|---------|-----|-------------|
| **Main Application** | http://localhost:8080 | Login page and dashboards |
| **H2 Console** | http://localhost:8080/h2-console | Database management (H2 only) |
| **API Endpoints** | http://localhost:8080/api/* | REST API endpoints |

---

## 🔑 Demo Credentials

### Security Guards
| User ID | Password | Shift |
|---------|----------|-------|
| GUARD001 | guard123 | Morning |
| GUARD002 | guard123 | Night |

### Admin/Warden
| User ID | Password | Role |
|---------|----------|------|
| ADMIN001 | admin123 | Admin |
| WARDEN001 | warden123 | Warden |

### Students
| Roll Number | Name | Department | Hostel |
|-------------|------|------------|--------|
| CS2021001 | Amit Patel | Computer Science | Boys Hostel A |
| EC2021002 | Priya Singh | Electronics | Girls Hostel A |

---

## 🌐 GitHub Repository

**Repository URL**: https://github.com/naveengoudamk/college-security-register

### Clone from GitHub
```bash
git clone https://github.com/naveengoudamk/college-security-register.git
cd college-security-register
mvn clean install
mvn spring-boot:run
```

### Repository Contents
- ✅ Complete Source Code
- ✅ Documentation (README, Setup Guides)
- ✅ Configuration Files
- ✅ Frontend Assets (HTML/CSS/JS)
- ✅ Maven Build Configuration

---

## 📊 Project Statistics

- **Total Files**: 42
- **Lines of Code**: ~3,500+
- **Backend Classes**: 27
- **REST Endpoints**: 25+
- **Database Tables**: 6
- **Frontend Pages**: 3

---

## 🛑 Stopping the Application

### Method 1: Terminal
Press `Ctrl + C` in the terminal where Maven is running

### Method 2: Force Kill
```bash
lsof -ti:8080 | xargs kill -9
```

---

## 📝 Additional Resources

- **README.md** - Project overview and features
- **RUNNING.md** - Quick start guide
- **DEPLOYMENT_SUCCESS.md** - Deployment summary
- **GITHUB_SETUP.md** - GitHub setup instructions

---

## ✅ Quick Start Checklist

- [ ] Java 17+ installed
- [ ] Maven 3.6+ installed
- [ ] Project cloned from GitHub
- [ ] `mvn clean install` completed successfully
- [ ] `mvn spring-boot:run` started without errors
- [ ] Application accessible at http://localhost:8080
- [ ] Demo data inserted via H2 console
- [ ] Successfully logged in as Guard
- [ ] Successfully logged in as Admin
- [ ] Tested student entry/exit
- [ ] Tested visitor registration

---

## 🎉 Success!

If you've completed all the steps above, your **College Security & Hostel Management System** is fully operational!

**Next Steps:**
1. Explore all features in Guard and Admin dashboards
2. Test different scenarios (late entries, visitor blacklist, etc.)
3. Customize the application for your specific needs
4. Deploy to production server if needed

---

**Developed by**: Naveengouda M K  
**GitHub**: https://github.com/naveengoudamk/college-security-register  
**License**: MIT

---

*For any issues or questions, refer to the troubleshooting section or check the GitHub repository's Issues page.*
