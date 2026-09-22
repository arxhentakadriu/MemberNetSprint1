const form = document.querySelector("#login-form");
const loginMessage = document.querySelector("#form-message");
const loginButton = document.querySelector("#login-button");

const loginPanel = document.querySelector("#login-panel");
const applicationPanel = document.querySelector("#application-panel");
const applicationMessage = document.querySelector("#application-message");
const logoutButton = document.querySelector("#logout-button");
const activeGroupSelect = document.querySelector("#active-group");

let currentUser = null;
let associations = [];
let memberships = [];
let payments = [];
let guardianships = [];
let paymentView = "open";

form.addEventListener("submit", login);
logoutButton.addEventListener("click", logout);

document.querySelectorAll(".navigation-item").forEach((navigationItem) => {
  navigationItem.addEventListener("click", () => {
    showSection(navigationItem.dataset.section);
  });
});

document.querySelectorAll("[data-refresh]").forEach((button) => {
  button.addEventListener("click", () => {
    refreshSection(button.dataset.refresh);
  });
});

document.querySelectorAll("[data-payment-view]").forEach((button) => {
  button.addEventListener("click", () => {
    paymentView = button.dataset.paymentView;

    document.querySelectorAll("[data-payment-view]").forEach((item) => {
      item.classList.toggle("active", item.dataset.paymentView === paymentView);
    });

    renderPayments();
  });
});

activeGroupSelect?.addEventListener("change", () => {
  sessionStorage.setItem("membernetActiveGroupId", activeGroupSelect.value);

  updateActiveGroupOverview();
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

    await showApplication(data);
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

    await showApplication(data);
  } catch {
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

    resetApplicationData();
    showLogin();
  } catch (error) {
    applicationMessage.textContent = error.message || "Unable to log out.";
  } finally {
    logoutButton.disabled = false;
    logoutButton.textContent = "Log out";
  }
}

async function showApplication(data) {
  currentUser = data;

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

  loginPanel.classList.add("hidden");
  applicationPanel.classList.remove("hidden");

  showSection("overview");

  try {
    await loadGroupContext();
  } catch (error) {
    applicationMessage.textContent = error.message;
  }
}

function showLogin() {
  applicationPanel.classList.add("hidden");
  loginPanel.classList.remove("hidden");

  form.reset();
  clearMessages();

  document.querySelector("#login-email")?.focus();
}

async function showSection(sectionName) {
  document.querySelectorAll(".content-section").forEach((section) => {
    section.classList.add("hidden");
  });

  document.querySelectorAll(".navigation-item").forEach((item) => {
    item.classList.remove("active");
  });

  const selectedSection = document.querySelector(`#${sectionName}-section`);

  const selectedNavigationItem = document.querySelector(
    `[data-section="${sectionName}"]`,
  );

  selectedSection?.classList.remove("hidden");
  selectedNavigationItem?.classList.add("active");

  if (!currentUser) {
    return;
  }

  try {
    if (sectionName === "memberships") {
      await loadMemberships();
    }

    if (sectionName === "associations") {
      await loadAssociations();
    }

    if (sectionName === "payments") {
      await loadPayments();
    }

    if (sectionName === "guardianships") {
      await loadGuardianships();
    }
  } catch (error) {
    applicationMessage.textContent = error.message;
  }
}

async function refreshSection(sectionName) {
  applicationMessage.textContent = "";

  if (sectionName === "memberships") {
    await loadMemberships();
  } else if (sectionName === "associations") {
    await loadAssociations();
  } else if (sectionName === "payments") {
    await loadPayments();
  } else if (sectionName === "guardianships") {
    await loadGuardianships();
  } else {
    await loadGroupContext();
  }
}

async function loadGroupContext() {
  if (!currentUser?.userAccountId) {
    return;
  }

  const [associationData, membershipData] = await Promise.all([
    apiRequest("/api/associations"),
    apiRequest(`/api/memberships/user/${currentUser.userAccountId}`),
  ]);

  associations = asArray(associationData);
  memberships = asArray(membershipData);

  populateActiveGroupSelect();
  renderMemberships();
  renderAssociations();
}

async function loadAssociations() {
  setSectionMessage("associations", "Loading groups...");

  associations = asArray(await apiRequest("/api/associations"));

  renderAssociations();
}

async function loadMemberships() {
  if (!currentUser?.userAccountId) {
    return;
  }

  setSectionMessage("memberships", "Loading memberships...");

  memberships = asArray(
    await apiRequest(`/api/memberships/user/${currentUser.userAccountId}`),
  );

  if (associations.length === 0) {
    associations = asArray(await apiRequest("/api/associations"));
  }

  populateActiveGroupSelect();
  renderMemberships();
}

async function loadPayments() {
  if (!currentUser?.userAccountId) {
    return;
  }

  setSectionMessage("payments", "Loading payments...");

  payments = asArray(
    await apiRequest(`/api/payments/user/${currentUser.userAccountId}`),
  );

  renderPayments();
}

async function loadGuardianships() {
  if (!currentUser?.userAccountId) {
    return;
  }

  setSectionMessage("guardianships", "Loading guardianship relationships...");

  const userId = currentUser.userAccountId;

  const [asGuardian, asChild] = await Promise.all([
    apiRequest(`/api/guardianships/guardian/${userId}`),
    apiRequest(`/api/guardianships/child/${userId}`),
  ]);

  const combined = [...asArray(asGuardian), ...asArray(asChild)];

  guardianships = Array.from(
    new Map(
      combined.map((relationship) => [relationship.id, relationship]),
    ).values(),
  );

  renderGuardianships();
}

function populateActiveGroupSelect() {
  if (!activeGroupSelect) {
    return;
  }

  activeGroupSelect.replaceChildren();

  const membershipAssociationIds = new Set(
    memberships.map((membership) => membership.associationId),
  );

  const memberAssociations = associations.filter((association) =>
    membershipAssociationIds.has(association.id),
  );

  if (memberAssociations.length === 0) {
    activeGroupSelect.appendChild(createOption("", "No active group"));

    activeGroupSelect.disabled = true;
    updateActiveGroupOverview();
    return;
  }

  activeGroupSelect.disabled = false;

  memberAssociations.forEach((association) => {
    activeGroupSelect.appendChild(
      createOption(association.id, association.name),
    );
  });

  const storedGroupId = sessionStorage.getItem("membernetActiveGroupId");

  const storedGroupExists = memberAssociations.some(
    (association) => association.id === storedGroupId,
  );

  activeGroupSelect.value = storedGroupExists
    ? storedGroupId
    : memberAssociations[0].id;

  sessionStorage.setItem("membernetActiveGroupId", activeGroupSelect.value);

  updateActiveGroupOverview();
}

function updateActiveGroupOverview() {
  const output = document.querySelector("#overview-active-group");

  if (!output) {
    return;
  }

  const selectedAssociation = associations.find(
    (association) => association.id === activeGroupSelect?.value,
  );

  output.textContent = selectedAssociation?.name || "No active group";
}

function renderMemberships() {
  const container = document.querySelector("#memberships-list");

  if (!container) {
    return;
  }

  container.replaceChildren();
  setSectionMessage("memberships", "");

  if (memberships.length === 0) {
    setSectionMessage(
      "memberships",
      "You do not currently have any memberships.",
    );
    return;
  }

  memberships.forEach((membership) => {
    const association = associations.find(
      (item) => item.id === membership.associationId,
    );

    const card = createCard(association?.name || "Unknown group", [
      ["Status", membership.status],
      ["Valid from", formatDate(membership.validFrom)],
      [
        "Valid until",
        membership.validUntil
          ? formatDate(membership.validUntil)
          : "No expiry date",
      ],
      ["Membership ID", membership.id],
    ]);

    container.appendChild(card);
  });
}

function renderAssociations() {
  const container = document.querySelector("#associations-list");

  if (!container) {
    return;
  }

  container.replaceChildren();
  setSectionMessage("associations", "");

  if (associations.length === 0) {
    setSectionMessage("associations", "No groups are currently available.");
    return;
  }

  associations.forEach((association) => {
    const card = createCard(association.name, [
      ["Short name", association.shortName],
      ["Country", association.countryCode],
      ["Email", association.email],
      ["Status", association.status],
      ["Terms accepted", association.termsAccepted ? "Yes" : "No"],
    ]);

    container.appendChild(card);
  });
}

function renderPayments() {
  const container = document.querySelector("#payments-list");

  if (!container) {
    return;
  }

  container.replaceChildren();
  setSectionMessage("payments", "");

  const visiblePayments =
    paymentView === "open"
      ? payments.filter((payment) =>
          ["OPEN", "OVERDUE"].includes(payment.status),
        )
      : payments;

  if (visiblePayments.length === 0) {
    setSectionMessage(
      "payments",
      paymentView === "open"
        ? "You have no open payment obligations."
        : "No payment history is available.",
    );
    return;
  }

  visiblePayments.forEach((payment) => {
    const amount = formatMoney(payment.amount, payment.currencyCode);

    const card = createCard(payment.description || "Payment obligation", [
      ["Association", payment.associationName],
      ["Amount", amount],
      ["Reference", payment.paymentReference],
      ["Due date", formatDate(payment.dueDate)],
      ["Status", payment.status],
      ["Paid at", payment.paidAt ? formatDateTime(payment.paidAt) : "Not paid"],
    ]);

    container.appendChild(card);
  });
}

function renderGuardianships() {
  const container = document.querySelector("#guardianships-list");

  if (!container) {
    return;
  }

  container.replaceChildren();
  setSectionMessage("guardianships", "");

  if (guardianships.length === 0) {
    setSectionMessage(
      "guardianships",
      "No guardianship relationships were found.",
    );
    return;
  }

  guardianships.forEach((relationship) => {
    const currentUserIsGuardian =
      relationship.guardianUserAccountId === currentUser.userAccountId;

    const title = currentUserIsGuardian
      ? `Child: ${relationship.childName}`
      : `Guardian: ${relationship.guardianName}`;

    const card = createCard(title, [
      [
        "Relationship",
        currentUserIsGuardian ? "You are the guardian" : "You are the child",
      ],
      ["Status", relationship.status],
      ["Valid from", formatDate(relationship.validFrom)],
      [
        "Valid until",
        relationship.validUntil
          ? formatDate(relationship.validUntil)
          : "No expiry date",
      ],
    ]);

    container.appendChild(card);
  });
}

function createCard(title, fields) {
  const article = document.createElement("article");
  article.className = "data-card";

  const heading = document.createElement("h3");
  heading.textContent = title || "Not available";
  article.appendChild(heading);

  const details = document.createElement("dl");

  fields.forEach(([label, value]) => {
    const row = document.createElement("div");

    const term = document.createElement("dt");
    term.textContent = label;

    const description = document.createElement("dd");
    description.textContent =
      value === null || value === undefined || value === ""
        ? "Not available"
        : String(value);

    row.append(term, description);
    details.appendChild(row);
  });

  article.appendChild(details);
  return article;
}

function createOption(value, label) {
  const option = document.createElement("option");
  option.value = value;
  option.textContent = label;
  return option;
}

async function apiRequest(url, options = {}) {
  const response = await fetch(url, {
    credentials: "same-origin",
    ...options,
  });

  const data = await readJson(response);

  if (response.status === 401) {
    resetApplicationData();
    showLogin();
    throw new Error("Your session has expired. Please log in again.");
  }

  if (!response.ok) {
    throw new Error(
      data.message || `Request failed with status ${response.status}.`,
    );
  }

  return data;
}

function setSectionMessage(sectionName, text) {
  const sectionMessage = document.querySelector(`#${sectionName}-message`);

  if (sectionMessage) {
    sectionMessage.textContent = text;
  }
}

function setLoginLoading(loading) {
  loginButton.disabled = loading;
  loginButton.textContent = loading ? "Logging in..." : "Login";
}

function showLoginError(text) {
  loginMessage.textContent = text;
}

function clearMessages() {
  loginMessage.textContent = "";
  applicationMessage.textContent = "";

  ["memberships", "associations", "payments", "guardianships"].forEach(
    (sectionName) => {
      setSectionMessage(sectionName, "");
    },
  );
}

function resetApplicationData() {
  currentUser = null;
  associations = [];
  memberships = [];
  payments = [];
  guardianships = [];
  paymentView = "open";

  sessionStorage.removeItem("membernetActiveGroupId");
}

function asArray(value) {
  return Array.isArray(value) ? value : [];
}

function formatDate(value) {
  if (!value) {
    return "Not available";
  }

  const date = new Date(`${value}T00:00:00`);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return new Intl.DateTimeFormat("en-GB").format(date);
}

function formatDateTime(value) {
  if (!value) {
    return "Not available";
  }

  const date = new Date(value);

  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return new Intl.DateTimeFormat("en-GB", {
    dateStyle: "medium",
    timeStyle: "short",
  }).format(date);
}

function formatMoney(amount, currencyCode) {
  const numericAmount = Number(amount);

  if (Number.isNaN(numericAmount)) {
    return `${amount} ${currencyCode || ""}`.trim();
  }

  try {
    return new Intl.NumberFormat("en-IE", {
      style: "currency",
      currency: currencyCode || "EUR",
    }).format(numericAmount);
  } catch {
    return `${numericAmount.toFixed(2)} ${currencyCode || "EUR"}`;
  }
}

async function readJson(response) {
  const contentType = response.headers.get("content-type") || "";

  if (!contentType.includes("application/json")) {
    return {};
  }

  return response.json();
}
