const form = document.querySelector("#login-form");
const message = document.querySelector("#form-message");
const button = document.querySelector("#login-button");

const loginPanel = document.querySelector("#login-panel");
const applicationPanel = document.querySelector("#application-panel");

const applicationMessage = document.querySelector("#application-message");

const logoutButton = document.querySelector("#logout-button");

form.addEventListener("submit", login);
logoutButton.addEventListener("click", logout);

document.querySelectorAll(".navigation-item").forEach((navigationItem) => {
  navigationItem.addEventListener("click", () => {
    showSection(navigationItem.dataset.section);
  });
});

checkCurrentSession();

async function login(event) {
  event.preventDefault();

  const loginEmail = form.loginEmail.value.trim();
  const password = form.password.value;

  if (!loginEmail || !password) {
    showLoginError("Enter both your email address and password.");
    return;
  }

  setLoginLoading(true);
  clearMessages();

  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        loginEmail,
        password,
      }),
    });

    const data = await readJson(response);

    if (!response.ok) {
      throw new Error(data.message || "Unable to login. Please try again.");
    }

    showApplication(data);
  } catch (error) {
    showLoginError(error.message || "Unable to login. Please try again.");
  } finally {
    setLoginLoading(false);
  }
}

async function checkCurrentSession() {
  try {
    const response = await fetch("/api/auth/session", {
      method: "GET",
      credentials: "same-origin",
    });

    if (response.status === 401) {
      showLogin();
      return;
    }

    const data = await readJson(response);

    if (!response.ok) {
      showLogin();
      return;
    }

    showApplication(data);
  } catch (error) {
    showLogin();
  }
}

async function logout() {
  logoutButton.disabled = true;
  logoutButton.textContent = "Logging out...";
  applicationMessage.textContent = "";

  try {
    const response = await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "same-origin",
    });

    const data = await readJson(response);

    if (!response.ok) {
      throw new Error(data.message || "Unable to log out.");
    }

    showLogin();
  } catch (error) {
    applicationMessage.textContent = error.message || "Unable to log out.";
  } finally {
    logoutButton.disabled = false;
    logoutButton.textContent = "Log out";
  }
}

function showApplication(data) {
  document.querySelector("#welcome-title").textContent =
    `Welcome, ${data.displayName}`;

  document.querySelector("#home-page").textContent =
    data.homePage || "MemberNet home";

  document.querySelector("#user-account-id").textContent =
    data.userAccountId || "Not available";

  document.querySelector("#account-email").textContent =
    data.loginEmail || "Not available";

  document.querySelector("#display-name").textContent =
    data.displayName || "Not available";

  document.querySelector("#account-status").textContent =
    data.accountStatus || "UNKNOWN";

  form.reset();
  clearMessages();
  showSection("overview");

  loginPanel.classList.add("hidden");
  applicationPanel.classList.remove("hidden");
}

function showLogin() {
  applicationPanel.classList.add("hidden");
  loginPanel.classList.remove("hidden");

  form.reset();
  clearMessages();

  document.querySelector("#login-email").focus();
}

function showSection(sectionName) {
  document.querySelectorAll(".content-section").forEach((section) => {
    section.classList.add("hidden");
  });

  document.querySelectorAll(".navigation-item").forEach((navigationItem) => {
    navigationItem.classList.remove("active");
  });

  const selectedSection = document.querySelector(`#${sectionName}-section`);

  const selectedNavigationItem = document.querySelector(
    `[data-section="${sectionName}"]`,
  );

  if (selectedSection) {
    selectedSection.classList.remove("hidden");
  }

  if (selectedNavigationItem) {
    selectedNavigationItem.classList.add("active");
  }
}

function setLoginLoading(loading) {
  button.disabled = loading;
  button.textContent = loading ? "Logging in..." : "Login";
}

function showLoginError(text) {
  message.textContent = text;
}

function clearMessages() {
  message.textContent = "";
  applicationMessage.textContent = "";
}

async function readJson(response) {
  const contentType = response.headers.get("content-type") || "";

  if (!contentType.includes("application/json")) {
    return {};
  }

  return response.json();
}
