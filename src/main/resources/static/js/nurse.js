 const token = localStorage.getItem('token');
    const CONTEXT_PATH = "/ehealth-chatbot"; // set if needed

    if (!token) {
        window.location.href = CONTEXT_PATH + "login";
    }

    function logout() {
        localStorage.removeItem("token");
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

    // -------------------
    // Load Doctors List
    // -------------------
    async function loadDoctors() {
        try {
            const res = await fetchWithAuth(`${CONTEXT_PATH}/api/nurse/list-all`);
            if (!res.ok) return;

            const doctors = await res.json();
            const select = document.getElementById("doctorSelect");

            doctors.forEach(d => {
                const opt = document.createElement("option");
                opt.value = d.id;
                opt.textContent = d.user.email + " (" + d.specialization + ")";
                select.appendChild(opt);
            });

        } catch (e) {
            console.error(e);
        }
    }

    // -------------------
    // Load Doctor Appointments
    // -------------------
    async function loadDoctorAppointments() {
        const doctorId = document.getElementById("doctorSelect").value;
        const list = document.getElementById("doctorAppointments");

        if (!doctorId) {
            list.innerHTML = "<li>Select a doctor first</li>";
            return;
        }

        try {
            const res = await fetchWithAuth(
                `${CONTEXT_PATH}/api/nurse/appointments?doctorId=${doctorId}`
            );
            if (!res.ok) throw new Error();

            const data = await res.json();
            list.innerHTML = "";

            if (data.length === 0) {
                list.innerHTML = "<li>No appointments found</li>";
                return;
            }

            data.forEach(a => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <b>${a.datetime}</b><br>
                    Patient: ${a.patient.name}<br>
                    Status: ${a.status}
                `;
                list.appendChild(li);
            });

        } catch (err) {
            list.innerHTML = "<li>Error loading appointments</li>";
            console.error(err);
        }
    }

    // -------------------
    // Load Patients
    // -------------------
    async function loadPatients() {
        const list = document.getElementById("patients");

        try {
            const res = await fetchWithAuth(`${CONTEXT_PATH}/api/nurse/patients`);
            if (!res.ok) throw new Error();

            const patients = await res.json();
            list.innerHTML = "";

            patients.forEach(p => {
                const li = document.createElement("li");
                li.innerHTML = `
                    <b>${p.name}</b><br>
                    DOB: ${p.dob}<br>
                    Contact: ${p.contact}
                `;
                list.appendChild(li);
            });

        } catch (err) {
            list.innerHTML = "<li>Error loading patients</li>";
            console.error(err);
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

    function logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        window.location.href = CONTEXT_PATH + '/login';
    }

    // Initial load
    loadDoctors();
    loadPatients();
    loadLabResults();
