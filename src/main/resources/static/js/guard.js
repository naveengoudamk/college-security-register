const API_URL = 'http://localhost:8080/api';
let guardData = null;

window.onload = function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user || user.userType !== 'GUARD') {
        window.location.href = 'index.html';
        return;
    }

    guardData = user;
    document.getElementById('userName').textContent = user.name;
    document.getElementById('userShift').textContent = `${user.shift} Shift`;

    loadDashboardData();
    setInterval(loadDashboardData, 30000);
};

async function loadDashboardData() {
    await loadTodayEntries();
    await loadActiveStudents();
    await loadTodayVisitors();
    await loadActiveVisitors();
}

async function loadTodayEntries() {
    try {
        const response = await fetch(`${API_URL}/entry/today`);
        const entries = await response.json();

        document.getElementById('todayEntries').textContent = entries.length;

        const tbody = document.getElementById('entryTableBody');
        if (entries.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No entries today</td></tr>';
            return;
        }

        tbody.innerHTML = entries.map(entry => `
            <tr>
                <td>${formatTime(entry.entryTime)}</td>
                <td>${entry.studentRollNumber}</td>
                <td>${entry.studentName}</td>
                <td>${entry.hostelName}</td>
                <td>
                    ${entry.exitTime
                ? `<span class="badge badge-success">Exited at ${formatTime(entry.exitTime)}</span>`
                : '<span class="badge badge-warning">Inside</span>'}
                </td>
                <td>
                    ${entry.lateEntry
                ? '<span class="badge badge-danger">Late</span>'
                : '<span class="badge badge-success">On Time</span>'}
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading entries:', error);
    }
}

async function loadActiveStudents() {
    try {
        const response = await fetch(`${API_URL}/entry/active`);
        const active = await response.json();
        document.getElementById('activeStudents').textContent = active.length;
    } catch (error) {
        console.error('Error loading active students:', error);
    }
}

async function loadTodayVisitors() {
    try {
        const response = await fetch(`${API_URL}/visitors/today`);
        const visitors = await response.json();

        const tbody = document.getElementById('visitorTableBody');
        if (visitors.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No visitors today</td></tr>';
            return;
        }

        tbody.innerHTML = visitors.map(visitor => `
            <tr>
                <td>${formatTime(visitor.entryTime)}</td>
                <td>${visitor.name}</td>
                <td>${visitor.phone}</td>
                <td>${visitor.studentToMeet} (${visitor.studentRollNumber})</td>
                <td>${visitor.purpose}</td>
                <td>
                    ${!visitor.exitTime
                ? `<button class="btn-secondary" onclick="recordVisitorExit(${visitor.id})">Exit</button>`
                : `<span class="badge badge-success">Exited</span>`}
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading visitors:', error);
    }
}

async function loadActiveVisitors() {
    try {
        const response = await fetch(`${API_URL}/visitors/active`);
        const active = await response.json();
        document.getElementById('activeVisitors').textContent = active.length;
    } catch (error) {
        console.error('Error loading active visitors:', error);
    }
}

document.getElementById('entryForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const rollNumber = document.getElementById('entryRollNumber').value;

    try {
        const response = await fetch(`${API_URL}/entry?rollNumber=${rollNumber}&guardId=${guardData.id}`, {
            method: 'POST'
        });

        if (response.ok) {
            alert('Entry recorded successfully!');
            document.getElementById('entryRollNumber').value = '';
            loadDashboardData();
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        alert('Failed to record entry');
    }
});

document.getElementById('exitForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const rollNumber = document.getElementById('exitRollNumber').value;

    try {
        const response = await fetch(`${API_URL}/entry/exit?rollNumber=${rollNumber}`, {
            method: 'POST'
        });

        if (response.ok) {
            alert('Exit recorded successfully!');
            document.getElementById('exitRollNumber').value = '';
            loadDashboardData();
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        alert('Failed to record exit');
    }
});

document.getElementById('visitorForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const visitor = {
        name: document.getElementById('visitorName').value,
        phone: document.getElementById('visitorPhone').value,
        idType: document.getElementById('visitorIdType').value,
        idNumber: document.getElementById('visitorIdNumber').value,
        purpose: document.getElementById('visitorPurpose').value
    };

    const studentRoll = document.getElementById('visitorStudentRoll').value;

    try {
        const response = await fetch(`${API_URL}/visitors?studentRollNumber=${studentRoll}&guardId=${guardData.id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(visitor)
        });

        if (response.ok) {
            alert('Visitor registered successfully!');
            document.getElementById('visitorForm').reset();
            loadDashboardData();
        } else {
            const error = await response.text();
            alert('Error: ' + error);
        }
    } catch (error) {
        alert('Failed to register visitor');
    }
});

async function recordVisitorExit(visitorId) {
    try {
        const response = await fetch(`${API_URL}/visitors/${visitorId}/exit`, {
            method: 'POST'
        });

        if (response.ok) {
            alert('Visitor exit recorded!');
            loadDashboardData();
        } else {
            alert('Failed to record exit');
        }
    } catch (error) {
        alert('Error recording exit');
    }
}

function formatTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
}

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        fetch(`${API_URL}/auth/logout/guard/${guardData.id}`, { method: 'POST' })
            .finally(() => {
                sessionStorage.clear();
                window.location.href = 'index.html';
            });
    }
}
