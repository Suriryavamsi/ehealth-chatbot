const CONTEXT_PATH = '/ehealth-chatbot';

// ===== LOGIN =====
async function login() {
    const username = document.getElementById('login-username').value.trim();
    const password = document.getElementById('login-password').value.trim();
    const errorEl = document.getElementById('login-error');
    errorEl.textContent = '';

    if (!username || !password) {
        errorEl.textContent = "Username and password required";
        return;
    }

    try {
        const response = await fetch(`${CONTEXT_PATH}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        const data = await response.json();

        if (!response.ok) {
            errorEl.textContent = data.error || 'Login failed';
            return;
        }

        // Save JWT + role in localStorage
        localStorage.setItem('token', data.token);
        localStorage.setItem('role', data.role);

        // Redirect based on role
        const dashboardMap = {
            'DOCTOR': '/doctor/dashboard',
            'PATIENT': '/patient/dashboard',
            'NURSE': '/nurse/dashboard'
        };

        const redirectUrl = dashboardMap[data.role];
        if (redirectUrl) {
            window.location.href = CONTEXT_PATH + redirectUrl;
        } else {
            errorEl.textContent = 'Invalid role';
        }

    } catch (err) {
        console.error(err);
        errorEl.textContent = 'Server error';
    }
}

// ===== REGISTER =====
async function register() {
    const username = document.getElementById('reg-username').value.trim();
    const email = document.getElementById('reg-email').value.trim();
    const password = document.getElementById('reg-password').value.trim();
    const role = document.getElementById('reg-role').value;
    const errorEl = document.getElementById('reg-error');
    errorEl.textContent = '';

    if (!username || !email || !password) {
        errorEl.textContent = "All fields are required";
        return;
    }

    try {
        const response = await fetch(`${CONTEXT_PATH}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password, roleName: role })
        });

        const data = await response.json();

        if (!response.ok) {
            errorEl.textContent = data.error || 'Registration failed';
            return;
        }

        alert('Registration successful! You can now log in.');
        document.getElementById('reg-username').value = '';
        document.getElementById('reg-email').value = '';
        document.getElementById('reg-password').value = '';
        document.getElementById('reg-role').value = 'PATIENT';

    } catch (err) {
        console.error(err);
        errorEl.textContent = 'Server error';
    }
}

// ===== FETCH UTILITY FOR AUTHORIZED REQUESTS =====
async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem('token');
    if (!token) throw new Error("No token found. User not logged in.");

    options.headers = {
        ...(options.headers || {}),
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
    };

    const response = await fetch(url, options);

    if (response.status === 403) {
        console.warn('Access denied (403) for', url);
    }

    return response;
}
