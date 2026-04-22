document.addEventListener("DOMContentLoaded", function () {
    const tabButtons = document.querySelectorAll(".tab-btn");
    const loginForm = document.getElementById("loginForm");
    const signupForm = document.getElementById("signupForm");
    const query = new URLSearchParams(window.location.search);
    const nextPage = query.get("next") || "index.html";

    function setTab(tabName) {
        tabButtons.forEach(function (button) {
            const isActive = button.getAttribute("data-tab") === tabName;
            button.classList.toggle("active", isActive);
        });
        loginForm.classList.toggle("active", tabName === "login");
        signupForm.classList.toggle("active", tabName === "signup");
    }

    tabButtons.forEach(function (button) {
        button.addEventListener("click", function () {
            setTab(button.getAttribute("data-tab"));
        });
    });

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("loginEmail").value.trim();
        const password = document.getElementById("loginPassword").value.trim();

        if (!email || !password) {
            GlowStore.showToast("Email and password are required", "error");
            return;
        }

        try {
            const response = await GlowStore.apiRequest("/auth/login", {
                method: "POST",
                body: {
                    email: email,
                    password: password
                }
            });
            GlowStore.setAuth(response.token, response.username);
            GlowStore.showToast("Login successful", "success");
            window.location.href = nextPage;
        } catch (error) {
            GlowStore.showToast(error.message, "error");
        }
    });

    signupForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const username = document.getElementById("signupUsername").value.trim();
        const email = document.getElementById("signupEmail").value.trim();
        const password = document.getElementById("signupPassword").value.trim();

        if (username.length < 2) {
            GlowStore.showToast("Username should be at least 2 characters", "error");
            return;
        }

        if (password.length < 6) {
            GlowStore.showToast("Password should be at least 6 characters", "error");
            return;
        }

        try {
            const response = await GlowStore.apiRequest("/auth/signup", {
                method: "POST",
                body: {
                    username: username,
                    email: email,
                    password: password
                }
            });
            GlowStore.setAuth(response.token, response.username);
            GlowStore.showToast("Signup successful", "success");
            window.location.href = nextPage;
        } catch (error) {
            GlowStore.showToast(error.message, "error");
        }
    });

    if (GlowStore.getToken()) {
        setTab("login");
    }
});
