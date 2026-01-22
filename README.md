# Ink Solution – Multi-Agent AI Chatbot

This repository contains a Java-based solution for the Ink coding task, implementing **two collaborating AI agents**:

- **Technical Specialist** – handles technical and product-related questions  
- **Billing Specialist** – handles pricing, invoices, and account-related topics  

The agents collaborate to route and respond to user queries appropriately, using the **Google Gemini model** via Google AI Studio.

A lightweight HTML frontend is included to allow interactive conversations through a browser, without manually sending JSON requests.


## Architecture Overview

- **Backend:** Java (Spring Boot)
- **LLM Provider:** Google Gemini (via Google AI Studio API)
- **Frontend:** Minimal HTML/JS chat interface


## Requirements

### 1. Google AI Studio API Key
An API key is required to access the Gemini model.

You can provide it in **one of two ways**:
- As an environment variable  
- By pasting it directly into the `apiKey` field in the `GeminiClient` class  

### 2. Google AI Studio Account
A valid Google AI Studio account is required to issue requests to the Gemini API.


## Running the Application

1. Clone the repository
2. Configure the API key, by setting an env variable or paste it directly
3. Start the application bu running the main function in **Application** class
4. Once the server is running, navigate to http://localhost:8080/chat.html to interact with the bot

## Known Issues 
Occasionally, the Gemini API returns **HTTP 503** responses. This behavior originates from the Google API, not the application logic.
Currently, no retry or fallback mechanism is implemented to handle this gracefully.
