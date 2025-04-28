# ✍️ ProWrite — Smart Professional Writer Chrome Extension

<p align="center">
  <img src="./chrome-extension/img/icon.png" width="120" alt="ProWrite Logo">
</p>

> **ProWrite** helps you transform rough drafts into professional messages for LinkedIn, referrals, referral and more — powered by the **Gemini API**.

---

## 🚀 Features

- **📝 User Input Friendly** — Type your rough message or upload a file.
- **📄 Resume Integration** — Enriches prompts with resume data (optional).
- **🎯 Smart Prompting** — Combines your input and resume intelligently.
- **🔄 Flexible Handling** — Works even if no resume is uploaded.
- **⚡ Instant Output** — Get polished, formal content instantly.

---

## 🏩 File Structure

### Frontend (Chrome Extension)

```plaintext
chrome-extension/
├── manifest.json
├── popup.html
├── popup.js
├── style.css
├── img/
    ├── icon16.png
    ├── icon48.png
    └── icon128.png

```

- **manifest.json** – Extension metadata (permissions, background scripts, popup page).
- **popup.html / popup.css** – Main UI and logic.
- **popup.js** – Handles background operations like API calling and userId.
- **icons/** – Extension logos and images.

---
```
backend/
├── src/
│ ├── main/
│ │ ├── java/com/prowrite/
│ │ │ ├── controller/FormalController.java
│ │ │ ├── model/Resume.java
│ │ │ ├── repository/ResumeRepository.java
│ │ │ ├── service/FormalService.java
│ │ │ └── BackendApplication.java
│ │ └── resources/
│ │ ├── application.properties
│ │ └── static/
│ └── test/
└── pom.xml

```

- **controller/** – Handles incoming HTTP requests (userId,metaData,resumeFile).
- **service/** – Business logic for processing text (process data and create prompts and call Api).
- **resources/** – Configuration and static files.
- **pom.xml** – Maven build configuration.

```

```

Flowchart
    A[User Input Text + Resume Upload (Optional)] --> B[Chrome Extension (popup.js generates userId)]
    B --> C[POST Request to Backend API]
    C --> D[Spring Boot Controller]
    D --> E[Service Layer: Process & Enhance Text]
    E --> F[Generate Polished Output]
    F --> G[Return to Chrome Extension UI]


```

**Request Body Example:**

```json
{
  "userId": "abc123",
  "metaData": "I want to apply for a job at XYZ company.",
  "resumeFile": "abc.pdf"  (Optional)
}
```

**Working:**

- **userId** is used to associate the generation with a user (future tracking feature).
- **metaData** is mandatory.
- **resumeFile** upload is optional to personalize the generation using resume content.

---

## 🛠️ How to Setup and Run

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/ProWrite.git
cd ProWrite
```

---

### 2. Setup Backend

- Navigate to `/backend`
- Open in your favorite IDE (like IntelliJ or VS Code)
- Run:

```bash
./mvnw spring-boot:run
```

Backend will start at `http://localhost:8085`.

---

### 3. Setup Chrome Extension (Frontend)

- Go to `chrome://extensions/`
- Enable **Developer Mode**
- Click **Load unpacked** and select the `frontend` folder.
- Extension will now be active! 🎉

---

## 📸 Screenshots

### 🌟 Extension Popup (Light Mode)

![Extension Light Mode](./chrome-extension/img/DarkMode.png)

---

### 🌙 Extension Popup (Dark Mode)

![Extension Dark Mode](./chrome-extension/img/LightMode.png)

---

# ✨ Future Plans

- 🔹 Support of Highlighted Text Generation
- 🔹 Increase Response Time
- 🔹 Upload and Manage Multiple Files

---

# 💻Tech Satck

### 🛠️ Backend

<p align="left"> <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java" /> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" /> <img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="Postgres" /> <img src="https://img.shields.io/badge/Apache%20PDFBox-ECECEC?style=for-the-badge&logo=apache&logoColor=red" alt="Apache PDFBox" /> </p>

### 🎨 Frontend

<p align="left"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML" /> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS" /> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black" alt="JavaScript" /> <img src="https://img.shields.io/badge/Chrome%20Extension-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white" alt="Chrome Extension" /> </p>

### 🔗 API

<p align="left"> <img src="https://img.shields.io/badge/Gemini%20API-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Gemini API" /> <img src="https://img.shields.io/badge/REST%20API-25D366?style=for-the-badge&logo=api&logoColor=white" alt="REST API" /> </p>

---
