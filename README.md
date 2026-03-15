<h1 align="center">🛡️ Project Sentinel: Autonomous AI Email Agent</h1>

<p align="center">
  <a href="https://git.io/typing-svg"><img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=22&pause=1000&color=F79A00&center=true&vCenter=true&width=600&lines=An+Intelligence+Layer+for+your+Inbox;Autonomous+Email+Processing;Built+with+Java+%2B+Spring+Boot" alt="Typing SVG" /></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Java--17-orange?style=for-the-badge&logo=java" alt="Java 17" />
  <img src="https://img.shields.io/badge/Framework-Spring--Boot--v3.x-green?style=for-the-badge&logo=springboot" alt="Spring Boot 3" />
  <img src="https://img.shields.io/badge/Deployment-Render-43c3d3?style=for-the-badge&logo=render&logoColor=white" alt="Render Deployment" />
</p>

> 💡 **Repository Status: Public Showcase** > This repo contains the sanitized, production-grade core logic of a live AI assistant. All sensitive credentials have been removed to demonstrate architecture safely.

<br>

<h2 align="center"> ⚡ The Core Vision ⚡ </h2>

`Project Sentinel` isn't just an email filter; it's a persistent digital aide. This repository showcases the "brain" of the operation: the Java backend that connects to IMAP servers, processes natural language, and determines appropriate actions.

To maintain security and enterprise best practices, this project utilizes a dual-repository strategy:

| 🗄️ Repository Type | 🎯 Purpose | 🔒 Security Posture |
| :--- | :--- | :--- |
| **🌎 Public Showcase (Here)** | Displays architecture, logic, and code quality. | **100% Sanitized.** Placeholder configurations only. |
| **🔐 Private Runner** | The active production environment. | **Private.** Contains live credentials, deployed on Render. |

<br>

<h2 align="center"> 🧠 System Intelligence & Data Flow 🧠 </h2>

When active, `Project Sentinel` doesn't just read words; it understands context. The pipeline takes an incoming IMAP email packet and routes it through an AI model for deep analysis, classifying if an email needs user intervention, an automated reply, or a quick summary.

<p align="center">
  <img src="screenshots/agent_analysis.png" alt="Sentinel AI: Backend Processing and Neural Analysis Visualization" width="800">
</p>

### 🔬 Core Capabilities
<table>
  <tr>
    <td><b>🔄 Persistent Monitoring</b><br>Uses a background <code>@Scheduled</code> task in Spring Boot to continuously poll the configured IMAP server.</td>
    <td><b>🤖 LLM Integration</b><br>Leverages AI via a secure REST API to perform sentiment analysis, intent classification, and summary generation.</td>
  </tr>
  <tr>
    <td colspan="2" align="center"><b>🔐 Credential Security</b><br>Built 100% on externalized environment variables ensuring zero secrets are hardcoded.</td>
  </tr>
</table>

<br>

<h2 align="center"> 📡 Telegram Integration: The Live Control Panel 📡 </h2>

A critical part of a background agent is human-in-the-loop control. The live version of `Project Sentinel` communicates with the owner via a secure Telegram Bot interface.

<table>
  <tr>
    <td width="50%">
      <h3>🕹️ Command Flow:</h3>
      <ol>
        <li>The agent identifies an "Urgent" email.</li>
        <li>It pauses execution and pushes a notification via the Telegram API.</li>
        <li>The owner responds with simple commands like <kbd>/approve</kbd> or <kbd>/ignore</kbd>.</li>
        <li>The Spring Boot backend intercepts the webhook and executes the action.</li>
      </ol>
    </td>
    <td width="50%" align="center">
      <img src="screenshots/Telegrame_messages.jpg" alt="Telegram Interface" width="250">
    </td>
  </tr>
</table>

<br>

<h2 align="center"> 💻 Technical Deep Dive 💻 </h2>

<p align="center">
  <a href="https://skillicons.dev">
    <img src="https://skillicons.dev/icons?i=java,spring,maven,git,github&perline=5" alt="Tech Stack Icons" />
  </a>
</p>

<details>
<summary><b>🛠️ Click to expand full Tech Stack details</b></summary>
<br>

* **Core:** Java 17 (LTS)
* **Framework:** Spring Boot 3.x
* **Integrations:** Spring Mail / IMAP protocols
* **Build Tool:** Maven 
* **Control Layer:** Telegram Bot API
* **Intelligence Layer:** Groq API / OpenAI 

</details>
