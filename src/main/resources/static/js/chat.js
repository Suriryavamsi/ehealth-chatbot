const CONTEXT_PATH = '/ehealth-chatbot';

async function sendMessage(conversationId = 1) {
    const input = document.getElementById('userInput');
    const message = input.value.trim();
    if (!message) return;

    appendMessage('You', message);
    input.value = '';

    const token = localStorage.getItem('token');
    if (!token) {
        appendMessage('System', 'You are not logged in. Please login to chat.');
        return;
    }

    try {
        const response = await fetch(`${CONTEXT_PATH}/api/chat/message?conversationId=${conversationId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({ message })
        });

        if (!response.ok) {
            const errText = await response.text();
            appendMessage('Bot', `Error: ${errText}`);
            return;
        }

        const data = await response.json();
        appendMessage('Bot', data.botReply); // display only botReply
    } catch (err) {
        console.error(err);
        appendMessage('System', 'Failed to send message. Server error.');
    }
}

function appendMessage(sender, text) {
    const chatbox = document.getElementById('chatbox');
    const div = document.createElement('div');
    div.className = sender.toLowerCase(); // user, bot, system
    div.innerHTML = `<strong>${sender}:</strong> ${text}`;
    chatbox.appendChild(div);
    chatbox.scrollTop = chatbox.scrollHeight;
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = CONTEXT_PATH + '/login';
}