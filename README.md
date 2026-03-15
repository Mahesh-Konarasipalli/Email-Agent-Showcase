<h1 align="center">🛡️ Project Sentinel: Autonomous AI Email Agent 🛡️</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Java--17-orange?style=for-the-badge" alt="Java 17" />
  <img src="https://img.shields.io/badge/Framework-Spring--Boot--v3.x-green?style=for-the-badge" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Deployment-Render-43c3d3?style=for-the-badge&logo=render&logoColor=white" alt="Render Deployment" />
  <img src="https://img.shields.io/badge/Repo--Status-Public--Showcase-blue?style=for-the-badge" alt="Public Showcase" />
</p>

<p align="center">
  <b>The sanitized core logic of a live, background-deployed AI assistant.</b><br>
  Built with Java and Spring Boot to monitor, analyze, and act on emails autonomously.
</p>

---

## 🚀 The Core Vision: An Intelligence Layer for your Inbox

`Project Sentinel` isn't just an email filter; it's a persistent digital aide. This repository showcases the "brain" of the operation: the Java code that connects to IMAP servers, processes natural language, and determines appropriate actions.

To maintain security and best practices, this project utilizes a dual-repository strategy:

| Repository Type | Purpose | Security |
| :--- | :--- | :--- |
| **🌎 Public Showcase (This Repo)** | Displays architecture, logic, and code quality to recruiters/developers. | **100% Sanitized.** No secrets, no personal data, placeholder `.env` files only. |
| **🔒 Private Runner** | The active production version that contains actual credentials. | **Private.** Connected to a live Render instance for background execution. |

---

## 🧠 System Intelligence & Data Flow

When `Project Sentinel` is active, it doesn't just read words; it understands context. The image below visualizes how the Java backend takes an incoming IMAP email packet and routes it through an AI model for deep analysis. It highlights the classification engine that determines if an email needs user intervention, automated reply, or summary.

<p align="center">
  <img src="screenshots/agent_analysis.png" alt="Sentinel AI: Backend Processing and Neural Analysis Visualization" width="850">
  <br>
  <i>Visualization: How the agent backend processes raw email data using AI.</i>
</p>

### 🛠️ Key Backend Features
* **Persistent Monitoring:** Uses a background `Scheduling` task in Spring Boot to continuously poll the configured IMAP server without manual triggers.
* **LLM Integration:** Leverages AI (via secure API connection) to perform sentiment analysis, intent classification, and summary generation.
* **Credential Security:** Built 100% on externalized environment variables (Spring Profiles and System Envs), ensuring zero secrets are stored in the codebase.
* **Maven Wrapper (`mvnw`):** Included for seamless, environment-agnostic builds and testing by third parties.

---

## 📡 Telegram Integration: The Live Control Panel

A critical part of a background agent is human-in-the-loop control. The live version of `Project Sentinel` communicates with the owner via a secure Telegram Bot.

The image below demonstrates a real interaction between the owner and the agent's background service. The backend uses the Telegram Bot API to send summaries of urgent emails or request permission before executing sensitive actions (like sending a draft).

<p align="center">
  <img src="screenshots/Telegrame_messages.jpg" alt="Telegram Mobile Interface showing Sentinel Agent Interaction" width="400">
  <br>
  <i>Live Interaction: Receiving summaries and controlling the background agent via Telegram.</i>
</p>

### How the Telegram Layer Works:
1.  The agent identifies an "Urgent" or "Action Required" email during its scheduled run.
2.  Instead of acting autonomously, it pauses and generates a simplified notification.
3.  This notification is pushed via the Telegram API to the owner's private chat.
4.  The owner can respond with simple commands (e.g., `/approve_send`, `/ignore`, `/summarize`) which the backend interprets and executes.

---

## 💻 Technical Deep Dive

### 🏗️ Tech Stack
* **Java 17** (LTS)
* **Spring Boot 3.x**
* **Spring Integration** (Mail/IMAP)
* **Maven** (Dependency & Build Management)
* **Telegram Bot API** (Notification/Control Layer)
* **[Insert AI Provider, e.g., OpenAI/HuggingFace] API** (Intelligence Layer)

### 📦 Local Setup for Public Exploration

While you cannot run *my* specific agent (as it requires my specific secrets), you can explore, build, and adapt the architecture.

1.  **Clone the Sanitized Repository:**
    ```bash
    git clone [https://github.com/](https://github.com/)[INSERT_YOUR_USERNAME]/[INSERT_PUBLIC_REPO_NAME].git
    cd [INSERT_PUBLIC_REPO_NAME]
    ```

2.  **Review the Configuration Template:**
    Inspect `src/main/resources/application.properties.example` or `.env.example`. This file shows *every* secret required by the system, fully sanitized. To run your own version, you must create a real `.env` file based on this template.

3.  **Build and Test (No Maven Installation Needed):**
    Use the included Maven Wrapper to compile and run the sanitized codebase.

    **Windows:**
    ```cmd
    mvnw.cmd clean install
    ```

    **Mac/Linux:**
    ```bash
    ./mvnw clean install
    ```

---

<p align="center">
  <b>Project Sentinel is a showcase of secure architecture, practical AI integration, and production-ready Java development.</b><br>
  Developed by Mahesh-Konarasipalli/Konarasipalli Mahesh. Connect with me on <a href=https://www.linkedin.com/in/mahesh-konarasipalli-6797882a2/>LinkedIn</a> or <a href=https://github.com/Mahesh-Konarasipalli>GitHub</a>.
</p>
