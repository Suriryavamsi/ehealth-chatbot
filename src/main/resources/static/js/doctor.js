const token = localStorage.getItem('token');
const CONTEXT_PATH = "/ehealth-chatbot"; // <-- set your base path if needed

    if (!token) {
        window.location.href = CONTEXT_PATH + "login";
    }

    async function fetchWithAuth(url, options = {}) {
        options.headers = {
            ...(options.headers || {}),
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        };
        return fetch(url, options);
    }

    // -----------------------------
    // Load Appointments
    // -----------------------------
    async function loadAppointments() {
        const loading = document.getElementById("appointments-loading");

        try {
            const response = await fetchWithAuth(`${CONTEXT_PATH}/api/doctor/appointments`);
            if (!response.ok) throw new Error("Failed");

            const data = await response.json();
            const list = document.getElementById("appointments");
            loading.style.display = "none";
            list.innerHTML = "";

            data.forEach(a => {
                const li = document.createElement("li");
                li.textContent = `${a.datetime} - Patient: ${a.patient.name}`;
                list.appendChild(li);
            });

        } catch (err) {
            loading.textContent = "Error loading appointments.";
        }
    }

    // -----------------------------
    // Load Patients
    // -----------------------------
    async function loadPatients() {
        const loading = document.getElementById("patients-loading");

        try {
            const response = await fetchWithAuth(`${CONTEXT_PATH}/api/doctor/patients`);
            if (!response.ok) throw new Error("Failed");

            const data = await response.json();
            const list = document.getElementById("patients");
            loading.style.display = "none";
            list.innerHTML = "";

            data.forEach(p => {
                const li = document.createElement("li");
                li.innerHTML = `
                    ${p.name} - DOB: ${p.dob}
                    <button class="small-btn" onclick="showPatientDetails(${p.id})">Details</button>
                    <button class="small-btn" onclick="showLabResults(${p.id})">Lab Results</button>
                `;
                list.appendChild(li);
            });

        } catch (err) {
            loading.textContent = "Error loading patients.";
        }
    }

    // -----------------------------
    // Show Patient Details
    // -----------------------------
    async function showPatientDetails(id) {
        const box = document.getElementById("patient-details");
        box.textContent = "Loading details...";

        try {
            const response = await fetchWithAuth(`${CONTEXT_PATH}/api/doctor/patients/${id}`);
            if (!response.ok) throw new Error();

            const p = await response.json();

            box.innerHTML = `
                <b>Name:</b> ${p.name}<br>
                <b>DOB:</b> ${p.dob}<br>
                <b>Gender:</b> ${p.gender}<br>
                <b>Contact:</b> ${p.contact}<br>
                <b>Address:</b> ${p.address}<br>
                <b>Emergency Contact:</b> ${p.emergencyContact}
            `;

        } catch {
            box.textContent = "Error loading patient details.";
        }
    }

    // -----------------------------
    // Show Lab Results
    // -----------------------------
    async function showLabResults(id) {
        const box = document.getElementById("lab-results");
        box.textContent = "Loading lab results...";

        try {
            const response = await fetchWithAuth(`${CONTEXT_PATH}/api/doctor/patients/${id}/lab-results`);
            if (!response.ok) throw new Error();

            const results = await response.json();

            if (!results.length) {
                box.textContent = "No lab results available.";
                return;
            }

            box.innerHTML = results
                .map(r => `
                    <div style="margin-bottom: 10px;">
                        <b>Test:</b> ${r.test.name}<br>
                        <b>Status:</b> ${r.status}<br>
                        <b>Date:</b> ${r.createdAt}<br>
                    </div>
                `)
                .join("");

        } catch {
            box.textContent = "Error loading lab results.";
        }
    }

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    window.location.href = CONTEXT_PATH + '/login';
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
    // -----------------------------
    // Initial Load
    // -----------------------------
    loadAppointments();
    loadPatients();