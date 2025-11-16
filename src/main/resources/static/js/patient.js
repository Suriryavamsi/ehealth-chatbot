 const token = localStorage.getItem('token');
const CONTEXT_PATH = "/ehealth-chatbot"; // If you have a context root, set here

    if (!token) {
        window.location.href = CONTEXT_PATH + "login";
    }

    function logout() {
        localStorage.removeItem("token");
        window.location.href = CONTEXT_PATH + "/login";
    }

    async function fetchWithAuth(url, options = {}) {
        options.headers = {
            ...(options.headers || {}),
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
        };
        return fetch(url, options);
    }

    // Load appointments
    async function loadAppointments() {
        const list = document.getElementById('appointments');

        try {
            const res = await fetchWithAuth(`${CONTEXT_PATH}/api/patient/appointments`);
            if (!res.ok) throw new Error("Failed");
            const data = await res.json();

            list.innerHTML = "";
            data.forEach(a => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <b>${a.datetime}</b><br>
                    Doctor: ${a.doctor?.user?.name ?? "Unknown"}<br>
                    Status: ${a.status}
                `;
                list.appendChild(li);
            });

        } catch (err) {
            list.innerHTML = "<li>Error loading appointments</li>";
        }
    }

    // Load lab results
    async function loadLabResults() {
        const list = document.getElementById('labResults');

        try {
            const res = await fetchWithAuth(`${CONTEXT_PATH}/api/patient/lab-results`);
            if (!res.ok) throw new Error("Failed");
            const data = await res.json();

            list.innerHTML = "";
            data.forEach(r => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <b>${r.testName}</b><br>
                    Value: ${r.value}<br>
                    Status: ${r.status}<br>
                    Date: ${r.date}
                `;
                list.appendChild(li);
            });

        } catch (err) {
            list.innerHTML = "<li>Error loading lab results</li>";
        }
    }

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

    loadAppointments();
    loadLabResults();