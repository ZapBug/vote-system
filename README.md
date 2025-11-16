# Vote System



[English](README.md) | [中文](README_zh.md)



This is a Web-based voting system built with Java Servlet and MyBatis, supporting user registration, login, voting, and admin management features.

## Features

- **User Management**
  - User registration
  - User login/logout
- **Voting Functionality**
  - View candidate list and current vote counts
  - Cast votes
- **Admin Management**
  - View all voting results
  - User management
  - Candidate management
  - Voting record management

## Tech Stack



- **Backend**: Java Servlet

- **Frontend**: JSP, Bootstrap

- **Database**: MySQL

- **ORM Framework**: MyBatis

- **JDK Version**: Java 8 or higher

## Project Structure

```
vote-system/
├── sql/                 # Database scripts
├── src/                 # Source code
│   ├── bean/            # Entity classes
│   ├── config/          # MyBatis configuration
│   ├── controller/      # Servlet controllers
│   ├── dao/             # Data Access Objects
│   ├── filter/          # Filters
│   ├── mapper/          # MyBatis mapping files
│   ├── service/         # Business logic layer
│   ├── test/            # Test code
│   ├── util/            # Utility classes
│   ├── db.properties    # Database configuration file
│   └── sqlMapConfig.xml # MyBatis main configuration file
├── WebContent/          # Web resources
│   ├── admin/           # Admin pages
│   ├── css/             # Stylesheets
│   ├── img/             # Image resources
│   ├── js/              # JavaScript files
│   ├── index.jsp        # Home page
│   ├── login.jsp        # Login page
│   ├── register.jsp     # Registration page
│   ├── vote.jsp         # Voting page
│   └── WEB-INF/
└── README.md
```

## Database Configuration

Configure database connection information in `src/db.properties`:

```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/vote-system?useSSL=false&characterEncoding=UTF-8
username=root
password=root
```

## Installation and Running

1. Clone the project locally
2. Create database `vote-system`
3. Execute `sql/database_schema.sql` script to create table structure
4. Configure database connection information in `src/db.properties`
5. Deploy the project to a Web server that supports Servlet (e.g., Tomcat)

## Security Features

- Uses CSP (Content Security Policy) filter to enhance Web security

## License

This project is licensed under the MIT License.

---

### Connect with Me

Let's learn and grow together.

![QR Code](qrcode.png)