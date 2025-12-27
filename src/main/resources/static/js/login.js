const API_URL = 'http://localhost:8080/api';
let currentUserType = 'guard';

function switchTab(type) {
    currentUserType = type;
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    event.target.classList.add('active');
    
    const usernameInput = document.getElementById('username');
    if (type === 'guard') {
        usernameInput.placeholder = 'Enter Guard ID';
    } else {
        usernameInput.placeholder = 'Enter Warden/Admin ID';
    }
    
    document.getElementById('errorMessage').classList.remove('show');
}

document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');
    
    try {
        const endpoint = currentUserType === 'guard' ? '/auth/login/guard' : '/auth/login/warden';
        const response = await fetch(`${API_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });
        
        const data = await response.json();
        
        if (data.success) {
            sessionStorage.setItem('user', JSON.stringify(data));
            
            if (data.userType === 'GUARD') {
                window.location.href = 'guard-dashboard.html';
            } else {
                window.location.href = 'admin-panel.html';
            }
        } else {
            errorMessage.textContent = data.message || 'Invalid credentials';
            errorMessage.classList.add('show');
        }
    } catch (error) {
        errorMessage.textContent = 'Login failed. Please try again.';
        errorMessage.classList.add('show');
    }
});
