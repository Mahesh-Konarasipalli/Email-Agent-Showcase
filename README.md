<h1 align="center">🛡️ Project Sentinel: Autonomous AI Email Agent</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Java--17-orange?style=for-the-badge&logo=java" alt="Java 17" />
  <img src="https://img.shields.io/badge/Framework-Spring--Boot--v3.x-green?style=for-the-badge&logo=springboot" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Deployment-Render-43c3d3?style=for-the-badge&logo=render&logoColor=white" alt="Render Deployment" />
  <img src="https://img.shields.io/badge/Repo--Status-Public--Showcase-blue?style=for-the-badge" alt="Public Showcase" />
</p>

> **The sanitized core logic of a live, background-deployed AI assistant.**
> Built with Java and Spring Boot to monitor, analyze, and act on emails autonomously.

---

## 🚀 The Core Vision: An Intelligence Layer for your Inbox

`Project Sentinel` isn't just an email filter; it's a persistent digital aide. This repository showcases the "brain" of the operation: the Java backend that connects to IMAP servers, processes natural language, and determines appropriate actions.

To maintain security and enterprise best practices, this project utilizes a dual-repository strategy:

| 🗄️ Repository Type | 🎯 Purpose | 🔒 Security Posture |
| :--- | :--- | :--- |
| **🌎 Public Showcase (This Repo)** | Displays architecture, logic, and code quality. | **100% Sanitized.** No secrets, no personal data. Placeholder configurations only. |
| **🔐 Private Runner** | The active production environment. | **Private.** Contains live credentials, connected to a Render instance for background execution. |

---

## 🧠 System Intelligence & Data Flow

When `Project Sentinel` is active, it doesn't just read words; it understands context. The pipeline takes an incoming IMAP email packet and routes it through an AI model for deep analysis, classifying if an email needs user intervention, an automated reply, or a quick summary.

<p align="center">
  <img src="screenshots/agent_analysis.png" alt="Sentinel AI: Backend Processing and Neural Analysis Visualization" width="800" style="border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
  <br>
  <i>Visualization: How the backend processes raw email data using AI.</i>
</p>

### 🛠️ Key Backend Features
* **Persistent Monitoring:** Uses a background `@Scheduled` task in Spring Boot to continuously poll the configured IMAP server.
* **LLM Integration:** Leverages AI via a secure REST API to perform sentiment analysis, intent classification, and summary generation.
* **Credential Security:** Built 100% on externalized environment variables ensuring zero secrets are hardcoded.

---

## 📡 Telegram Integration: The Live Control Panel

A critical part of a background agent is human-in-the-loop control. The live version of `Project Sentinel` communicates with the owner via a secure Telegram Bot interface.

<p align="center">
  <img src="screenshots/Telegrame_messages.jpg" alt="Telegram Mobile Interface showing Sentinel Agent Interaction" width="350" style="border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
  <br>
  <i>Live Interaction: Receiving summaries and controlling the background agent via Telegram.</i>
</p>

### 🕹️ How the Telegram Layer Works:
1. The agent identifies an "Urgent" or "Action Required" email.
2. It pauses execution and generates a simplified notification push via the Telegram API.
3. The owner responds with simple commands like <kbd>/approve</kbd>, <kbd>/ignore</kbd>, or <kbd>/summarize</kbd>.
4. The Spring Boot backend intercepts the webhook, interprets the command, and executes the action.

---

## 💻 Technical Deep Dive

<details>
<summary><b>Click to expand the Tech Stack details</b></summary>
<br>

* **Core:** Java 17 (LTS)
* **Framework:** Spring Boot 3.x
* **Integrations:** Spring Mail / IMAP protocols
* **Build Tool:** Maven 
* **Control Layer:** Telegram Bot API
* **Intelligence Layer:** Groq API / OpenAI *(Update to match your specific AI)*

</details>

<details>
<summary><b>Click to expand Local Setup Instructions</b></summary>
<br>

While you cannot run my personal deployment, you can explore and adapt the architecture locally.

**1. Clone the Sanitized Repository:**
```bash
git clone [https://github.com/Mahesh-Konarasipalli/AI-Email-Agent.git](https://github.com/Mahesh-Konarasipalli/AI-Email-Agent.git)
cd AI-Email-Agent
