const form = document.querySelector("#login-form");
const message = document.querySelector("#form-message");
const button = document.querySelector("#login-button");

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const username = form.username.value.trim();
  const password = form.password.value;
  if (!username || !password)
    return showError("Enter both your username and password.");
  button.disabled = true;
  button.textContent = "Logging in...";
  message.textContent = "";
  try {
    const response = await fetch("/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    const data = await response.json();
    if (!response.ok)
      throw new Error(data.message || "Unable to login. Please try again.");
    showHome(data);
  } catch (error) {
    showError(error.message);
  } finally {
    button.disabled = false;
    button.textContent = "Login";
  }
});

document.querySelector("#logout-button").addEventListener("click", async () => {
  await fetch("/api/auth/logout", { method: "POST" });
  document.querySelector("#home-panel").classList.add("hidden");
  document.querySelector("#login-panel").classList.remove("hidden");
  form.reset();
  form.username.focus();
});

function showError(text) {
  message.textContent = text;
}
function showHome(data) {
  document.querySelector("#welcome-title").textContent =
    `Welcome, ${data.displayName}`;
  document.querySelector("#home-page").textContent = data.homePage;
  document.querySelector("#member-id").textContent = data.memberId;
  document.querySelector("#roles").textContent = data.roles.join(", ");
  document.querySelector("#login-panel").classList.add("hidden");
  document.querySelector("#home-panel").classList.remove("hidden");
}
