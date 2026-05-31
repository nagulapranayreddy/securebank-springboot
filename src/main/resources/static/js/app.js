const BASE_URL = "";

// SHOW TOAST MESSAGE
function showToast(message, type = "success") {
    let toast = document.getElementById("toast");

    if (!toast) {
        toast = document.createElement("div");
        toast.id = "toast";
        document.body.appendChild(toast);
    }

    toast.className = `toast ${type}`;
    toast.innerText = message;
    toast.style.display = "block";

    setTimeout(() => {
        toast.style.display = "none";
    }, 3000);
}

// LOGOUT
function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userEmail");

    showToast("Logged out successfully", "success");

    setTimeout(() => {
        window.location.href = "/pages/login.html";
    }, 800);
}

// CHECK AUTHENTICATION
function checkAuthentication() {
    const token = localStorage.getItem("token");

    if (!token) {
        window.location.href = "/pages/login.html";
    }
}

// REGISTER
async function register() {
    const data = {
        name: document.getElementById("name").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch(`${BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

	const result = await getResponseMessage(response);
	showToast(result, response.ok ? "success" : "error");

    if (response.ok) {
        setTimeout(() => {
            window.location.href = "/pages/login.html";
        }, 1000);
    }
}

// LOGIN
async function login() {
    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    const response = await fetch(`${BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    const token = await response.text();

    if (!token.startsWith("ey")) {
        showToast(token, "error");
        return;
    }

    localStorage.setItem("token", token);
    localStorage.setItem("userEmail", data.email);

    showToast("Login successful", "success");

    setTimeout(() => {
        window.location.href = "/pages/dashboard.html";
    }, 800);
}

// LOAD DASHBOARD ACCOUNTS
async function loadAccounts() {
    const token = localStorage.getItem("token");

    const response = await fetch(`${BASE_URL}/api/accounts/my`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const accounts = await response.json();
    const container = document.getElementById("accounts-container");

    container.innerHTML = "";

    const welcomeMessage = document.getElementById("welcome-message");
    if (welcomeMessage) {
        welcomeMessage.innerText = "Welcome " + localStorage.getItem("userEmail");
    }

    if (accounts.length === 0) {
        container.innerHTML = "<p>You do not have any accounts yet.</p>";
        return;
    }

    accounts.forEach(account => {
        const card = document.createElement("div");
        card.className = "modern-account-card";

        card.innerHTML = `
            <div class="account-left">
                <h2>${account.accountType}</h2>
                <p>
                    <span>Account Number</span><br>
                    ${account.accountNumber}
                </p>
            </div>

            <div class="account-middle">
                <p>
                    <span>Balance</span><br>
                    <span class="balance">$${account.balance}</span>
                </p>
            </div>

            <div class="account-status">
                <p>
                    <span>Status</span><br>
                    <span class="status-badge">${account.accountStatus}</span>
                </p>
            </div>

            <div class="account-right">
                <button onclick="openAccountDetails('${account.accountNumber}')">
                    View Details
                </button>
            </div>
        `;

        container.appendChild(card);
    });
}

// OPEN ACCOUNT DETAILS PAGE
function openAccountDetails(accountNumber) {
    window.location.href = `/pages/account-details.html?accountNumber=${accountNumber}`;
}

// CREATE ACCOUNT
async function createAccount() {
    const token = localStorage.getItem("token");
    const accountType = document.getElementById("accountType").value;

    const response = await fetch(`${BASE_URL}/api/accounts/create`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ accountType })
    });

	const result = await getResponseMessage(response);
	showToast(result, response.ok ? "success" : "error");

    if (response.ok) {
        loadAccounts();
    }
}

// GET CURRENT ACCOUNT NUMBER FROM URL
function getCurrentAccountNumber() {
    const params = new URLSearchParams(window.location.search);
    return params.get("accountNumber");
}

// LOAD ACCOUNT DETAILS
async function loadAccountDetails() {
    const token = localStorage.getItem("token");
    const accountNumber = getCurrentAccountNumber();

    const response = await fetch(`${BASE_URL}/api/accounts/${accountNumber}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const account = await response.json();
    const details = document.getElementById("account-details");

    details.innerHTML = `
        <div>
            <h2>${account.accountType} Account</h2>

            <p>
                <strong>Account Number:</strong>
                ${account.accountNumber}
            </p>

            <p>
                <strong>Status:</strong>
                <span class="status-badge">${account.accountStatus}</span>
            </p>
        </div>

        <div>
            <p>Available Balance</p>
            <h1 class="balance">$${account.balance}</h1>
        </div>
    `;
}

// DEPOSIT MONEY
async function depositMoney() {
    const token = localStorage.getItem("token");
    const accountNumber = getCurrentAccountNumber();
    const amount = document.getElementById("depositAmount").value;

    const response = await fetch(`${BASE_URL}/api/accounts/deposit`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ accountNumber, amount })
    });

	const result = await getResponseMessage(response);
	showToast(result, response.ok ? "success" : "error");

    if (response.ok) {
        loadAccountDetails();
        loadTransactionHistory();
    }
}

// WITHDRAW MONEY
async function withdrawMoney() {
    const token = localStorage.getItem("token");
    const accountNumber = getCurrentAccountNumber();
    const amount = document.getElementById("withdrawAmount").value;

    const response = await fetch(`${BASE_URL}/api/accounts/withdraw`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ accountNumber, amount })
    });

	const result = await getResponseMessage(response);
	showToast(result, response.ok ? "success" : "error");

    if (response.ok) {
        loadAccountDetails();
        loadTransactionHistory();
    }
}

// TRANSFER MONEY
async function transferMoney() {
    const token = localStorage.getItem("token");
    const fromAccountNumber = getCurrentAccountNumber();
    const toAccountNumber = document.getElementById("toAccountNumber").value;
    const amount = document.getElementById("transferAmount").value;

    const response = await fetch(`${BASE_URL}/api/accounts/transfer`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            fromAccountNumber,
            toAccountNumber,
            amount
        })
    });

	const result = await getResponseMessage(response);
	showToast(result, response.ok ? "success" : "error");

    if (response.ok) {
        loadAccountDetails();
        loadTransactionHistory();
    }
}

// LOAD TRANSACTION HISTORY
async function loadTransactionHistory() {
    const token = localStorage.getItem("token");
    const accountNumber = getCurrentAccountNumber();

    const response = await fetch(`${BASE_URL}/api/transactions/account/${accountNumber}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const transactions = await response.json();
    const container = document.getElementById("transaction-history");

    container.innerHTML = "";

    if (transactions.length === 0) {
        container.innerHTML = "<p>No transactions yet.</p>";
        return;
    }

    transactions.reverse().forEach(txn => {
        const div = document.createElement("div");
        div.className = "transaction-card";

        div.innerHTML = `
            <h4>${txn.transactionType} - ${txn.transactionStatus}</h4>

            <p>
                <strong>Amount:</strong>
                $${txn.amount}
            </p>

            <p>${txn.description}</p>

            <p>
                <small>${txn.createdAt}</small>
            </p>
        `;

        container.appendChild(div);
    });
}

// PAGE LOAD
window.onload = function () {
    if (
        window.location.pathname.includes("dashboard.html") ||
        window.location.pathname.includes("account-details.html")
    ) {
        checkAuthentication();
    }

    if (window.location.pathname.includes("dashboard.html")) {
        loadAccounts();
    }

    if (window.location.pathname.includes("account-details.html")) {
        loadAccountDetails();
        loadTransactionHistory();
    }
};




async function getResponseMessage(response) {
    const text = await response.text();

    try {
        const data = JSON.parse(text);

        if (data.message) {
            return data.message;
        }

        if (data.error) {
            return data.error;
        }

        return text;
    } catch (e) {
        return text;
    }
}