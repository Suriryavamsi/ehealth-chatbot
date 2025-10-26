<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>eHealth AI Chatbot</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        #chatbox { width: 500px; height: 400px; border: 1px solid #ccc; padding: 10px; overflow-y: scroll; }
        #userInput { width: 400px; }
        .message { margin: 5px 0; }
        .user { color: blue; }
        .bot { color: green; }
    </style>
</head>
<body>
<h2>eHealth AI Chatbot</h2>

<div id="chatbox"></div>

<input type="text" id="userInput" placeholder="Type your message..." />
<button onclick="sendMessage()">Send</button>

<script>
    const contextPath = window.location.pathname.replace(/\/$/, ''); // removes trailing slash

    async function sendMessage() {
        const input = document.getElementById('userInput');
        const message = input.value.trim();
        if (!message) return;

        appendMessage('user', message);
        input.value = '';

        const response = await fetch(`${contextPath}/api/chat/message?conversationId=1`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(message)
        });

        if (!response.ok) {
            appendMessage('bot', 'Error: ' + response.status + ' ' + response.statusText);
            return;
        }

        const reply = await response.text();
        appendMessage('bot', reply);
    }

    function appendMessage(sender, text) {
        const chatbox = document.getElementById('chatbox');
        const msgDiv = document.createElement('div');
        msgDiv.className = 'message ' + sender;
        msgDiv.textContent = sender + ': ' + text;
        chatbox.appendChild(msgDiv);
        chatbox.scrollTop = chatbox.scrollHeight;
    }
</script>


</body>
</html>
