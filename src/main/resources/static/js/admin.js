const API_URL = 'http://localhost:8080/api';
let userData = null;

window.onload = function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    if (!user || (user.userType !== 'ADMIN' && user.userType !== 'WARDEN')) {
        window.location.href = 'index.html';
        return;
    }

    userData = user;
    document.getElementById('userName').textContent = user.name;
    document.getElementById('userRole').textContent = user.userType;

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').value = today;
    document.getElementById('endDate').value = today;

    loadDashboardData();
    setInterval(loadDashboardData, 60000);
};

async function loadDashboardData() {
    await loadLateEntries();
    await loadActiveVisitors();
    await loadBlacklistedVisitors();
    await loadStats();
}

async function loadStats() {
    try {
        const todayResponse = await fetch(`${API_URL}/entry/today`);
        const todayEntries = await todayResponse.json();
        document.getElementById('todayEntries').textContent = todayEntries.length;

        const lateResponse = await fetch(`${API_URL}/entry/late`);
        const lateEntries = await lateResponse.json();
        document.getElementById('lateEntries').textContent = lateEntries.length;

        const activeVisResponse = await fetch(`${API_URL}/visitors/active`);
        const activeVis = await activeVisResponse.json();
        document.getElementById('activeVisitors').textContent = activeVis.length;

        document.getElementById('totalStudents').textContent = '150';
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

async function loadLateEntries() {
    try {
        const response = await fetch(`${API_URL}/entry/late`);
        const entries = await response.json();

        const tbody = document.getElementById('lateEntriesBody');
        if (entries.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No late entries today</td></tr>';
            return;
        }

        tbody.innerHTML = entries.map(entry => `
            <tr>
                <td>${formatTime(entry.entryTime)}</td>
                <td>${entry.studentRollNumber}</td>
                <td>${entry.studentName}</td>
                <td>${entry.hostelName}</td>
                <td>${entry.roomNumber}</td>
                <td>${entry.guardName || 'N/A'}</td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading late entries:', error);
    }
}

async function loadActiveVisitors() {
    try {
        const response = await fetch(`${API_URL}/visitors/active`);
        const visitors = await response.json();

        const tbody = document.getElementById('activeVisitorsBody');
        if (visitors.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No active visitors</td></tr>';
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
                    <button class="btn-icon" onclick="blacklistVisitor(${visitor.id}, '${visitor.name}')">🚫 Blacklist</button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading active visitors:', error);
    }
}

async function loadBlacklistedVisitors() {
    try {
        const response = await fetch(`${API_URL}/visitors/blacklisted`);
        const visitors = await response.json();

        const tbody = document.getElementById('blacklistedBody');
        if (visitors.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">No blacklisted visitors</td></tr>';
            return;
        }

        tbody.innerHTML = visitors.map(visitor => `
            <tr>
                <td>${visitor.name}</td>
                <td>${visitor.phone}</td>
                <td>${formatDateTime(visitor.entryTime)}</td>
                <td>${visitor.remarks || 'N/A'}</td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error loading blacklisted visitors:', error);
    }
}

document.getElementById('searchForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const rollNumber = document.getElementById('searchRollNumber').value;

    try {
        const response = await fetch(`${API_URL}/entry/student/${rollNumber}`);

        if (response.ok) {
            const history = await response.json();

            const resultsDiv = document.getElementById('searchResults');
            const tbody = document.getElementById('searchResultsBody');

            if (history.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">No history found</td></tr>';
            } else {
                tbody.innerHTML = history.map(entry => `
                    <tr>
                        <td>${formatDate(entry.entryTime)}</td>
                        <td>${formatTime(entry.entryTime)}</td>
                        <td>${entry.exitTime ? formatTime(entry.exitTime) : 'Not exited'}</td>
                        <td>
                            ${entry.lateEntry
                        ? '<span class="badge badge-danger">Late</span>'
                        : '<span class="badge badge-success">On Time</span>'}
                        </td>
                        <td>${entry.guardName || 'N/A'}</td>
                    </tr>
                `).join('');
            }

            resultsDiv.classList.remove('hidden');
        } else {
            alert('Student not found');
        }
    } catch (error) {
        alert('Error searching student history');
    }
});

document.getElementById('reportForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    try {
        const response = await fetch(`${API_URL}/entry/range?startDate=${startDate}&endDate=${endDate}`);
        const entries = await response.json();

        const resultsDiv = document.getElementById('reportResults');
        const tbody = document.getElementById('reportResultsBody');
        const summary = document.getElementById('reportSummary');

        const lateCount = entries.filter(e => e.lateEntry).length;
        summary.textContent = `Total Entries: ${entries.length} | Late Entries: ${lateCount}`;

        if (entries.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align: center;">No entries found</td></tr>';
        } else {
            tbody.innerHTML = entries.map(entry => `
                <tr>
                    <td>${formatDate(entry.entryTime)}</td>
                    <td>${entry.studentRollNumber}</td>
                    <td>${entry.studentName}</td>
                    <td>${formatTime(entry.entryTime)}</td>
                    <td>${entry.exitTime ? formatTime(entry.exitTime) : 'Not exited'}</td>
                    <td>
                        ${entry.lateEntry
                    ? '<span class="badge badge-danger">Yes</span>'
                    : '<span class="badge badge-success">No</span>'}
                    </td>
                </tr>
            `).join('');
        }

        resultsDiv.classList.remove('hidden');
    } catch (error) {
        alert('Error generating report');
    }
});

async function blacklistVisitor(visitorId, visitorName) {
    const reason = prompt(`Enter reason for blacklisting ${visitorName}:`);
    if (!reason) return;

    try {
        const response = await fetch(`${API_URL}/visitors/${visitorId}/blacklist?reason=${encodeURIComponent(reason)}`, {
            method: 'POST'
        });

        if (response.ok) {
            alert('Visitor blacklisted successfully');
            loadDashboardData();
        } else {
            alert('Failed to blacklist visitor');
        }
    } catch (error) {
        alert('Error blacklisting visitor');
    }
}

function formatTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString('en-IN', { hour: '2-digit', minute: '2-digit' });
}

function formatDate(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleDateString('en-IN');
}

function formatDateTime(dateTimeString) {
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-IN');
}

function logout() {
    if (confirm('Are you sure you want to logout?')) {
        sessionStorage.clear();
        window.location.href = 'index.html';
    }
}
