# Ink Solution task

This repository contains my solution for the coding task of implementing 2 collaborating AI agents, Technical Specialist and Billing specialist. The code is written almost entirely in java, with some minor HTML implemented to allow the user to conversate with the bot without the need to send jsons directly to the endpoint. 

# Requirements 
1. Google AI studio API key, to access the gemini model. In order to utilize the key, either enter it as an env variable, or paste it directly into the field apiKey in GeminiClient class.
2. Google AI studio account, to be able to send requests to the model.

# Usage 
In order to run the application, it is enough to clone the repository, fill in the api key, and run the main function which can be found in the **Application** class. If everything turns on properly, to open the chat window you have to paste the endpoint containing the chat window **http://localhost:8080/chat.html**. After that, you can have conversation with the already running chatbot.

# Known issues
Sometimes, without a clear reason, Google AI studio seems to return an error code 503 instead of a normal response, which cause the bot to always answer *undefined*. This is not a problem of the code, but one that is on google's side. This cannot be controlled in the code.
