(function () {
    const TOKEN_KEY = "glowlogics_bookstore_token";
    const USER_KEY = "glowlogics_bookstore_user";

    function getToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function getUsername() {
        return localStorage.getItem(USER_KEY);
    }

    function setAuth(token, username) {
        localStorage.setItem(TOKEN_KEY, token);
        localStorage.setItem(USER_KEY, username);
    }

    function clearAuth() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
    }

    async function apiRequest(path, options) {
        const request = options || {};
        const headers = new Headers(request.headers || {});

        if (request.body && !(request.body instanceof FormData) && !headers.has("Content-Type")) {
            headers.set("Content-Type", "application/json");
        }

        const token = getToken();
        if (token) {
            headers.set("X-Auth-Token", token);
        }

        const payload = {
            method: request.method || "GET",
            headers,
            body: request.body && !(request.body instanceof FormData) ? JSON.stringify(request.body) : request.body
        };

        const response = await fetch(path, payload);
        const raw = await response.text();
        let data = null;

        if (raw) {
            try {
                data = JSON.parse(raw);
            } catch (error) {
                data = raw;
            }
        }

        if (!response.ok) {
            const message = (data && data.message) || (typeof data === "string" ? data : "Request failed");
            throw new Error(message);
        }

        return data;
    }

    function formatPrice(price) {
        return "₹ " + Number(price || 0).toFixed(0);
    }

    function calculateDiscount(price, originalPrice) {
        if (!originalPrice || originalPrice <= price) {
            return 0;
        }
        return Math.round(originalPrice - price);
    }

    function showToast(message, type) {
        const kind = type || "info";
        let stack = document.querySelector(".toast-stack");
        if (!stack) {
            stack = document.createElement("div");
            stack.className = "toast-stack";
            document.body.appendChild(stack);
        }

        const toast = document.createElement("div");
        toast.className = "toast " + kind;
        toast.textContent = message;
        stack.appendChild(toast);

        setTimeout(function () {
            toast.style.opacity = "0";
            toast.style.transform = "translateY(12px)";
            setTimeout(function () {
                toast.remove();
            }, 280);
        }, 2400);
    }

    function setupReveals() {
        const revealElements = document.querySelectorAll(".reveal");
        const observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add("in-view");
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.16 });

        revealElements.forEach(function (element) {
            observer.observe(element);
        });
    }

    function setupMobileNavDrawer() {
        const header = document.querySelector(".site-header");
        const navLinks = header ? header.querySelector(".nav-links") : null;

        if (!header || !navLinks || document.getElementById("mobileNavDrawer")) {
            return;
        }

        const openBtn = document.createElement("button");
        openBtn.id = "mobileNavOpen";
        openBtn.className = "mobile-nav-toggle";
        openBtn.type = "button";
        openBtn.setAttribute("aria-label", "Open navigation menu");
        openBtn.setAttribute("aria-expanded", "false");
        openBtn.innerHTML = "&#9776;";
        header.insertBefore(openBtn, header.firstChild);

        const overlay = document.createElement("div");
        overlay.id = "mobileNavOverlay";
        overlay.className = "mobile-nav-overlay";
        document.body.appendChild(overlay);

        const mobileNavLinks = Array.from(navLinks.querySelectorAll("a")).map(function (link) {
            const href = link.getAttribute("href") || "#";
            const activeClass = link.classList.contains("active") ? " active" : "";
            return '<a class="mobile-nav-link' + activeClass + '" href="' + href + '">' + link.textContent.trim() + "</a>";
        }).join("");

        const drawer = document.createElement("aside");
        drawer.id = "mobileNavDrawer";
        drawer.className = "mobile-nav-drawer";
        drawer.setAttribute("aria-hidden", "true");
        drawer.innerHTML =
            '<div class="mobile-nav-head">' +
                "<p>Menu</p>" +
                '<button id="mobileNavClose" class="mobile-nav-close" type="button" aria-label="Close navigation menu">&times;</button>' +
            "</div>" +
            '<nav class="mobile-nav-links">' + mobileNavLinks + "</nav>";

        document.body.appendChild(drawer);

        const closeBtn = drawer.querySelector("#mobileNavClose");

        function closeNav() {
            const wasOpen = document.body.classList.contains("mobile-nav-open");
            document.body.classList.remove("mobile-nav-open");
            openBtn.setAttribute("aria-expanded", "false");
            drawer.setAttribute("aria-hidden", "true");
            if (wasOpen && window.innerWidth <= 1040) {
                openBtn.focus();
            }
        }

        function openNav() {
            document.body.classList.add("mobile-nav-open");
            openBtn.setAttribute("aria-expanded", "true");
            drawer.setAttribute("aria-hidden", "false");
        }

        openBtn.addEventListener("click", function () {
            if (document.body.classList.contains("mobile-nav-open")) {
                closeNav();
                return;
            }
            openNav();
        });

        if (closeBtn) {
            closeBtn.addEventListener("click", closeNav);
        }

        overlay.addEventListener("click", closeNav);

        drawer.querySelectorAll("a").forEach(function (link) {
            link.addEventListener("click", closeNav);
        });

        document.addEventListener("keydown", function (event) {
            if (event.key === "Escape" && document.body.classList.contains("mobile-nav-open")) {
                closeNav();
            }
        });

        window.addEventListener("resize", function () {
            if (window.innerWidth > 1040 && document.body.classList.contains("mobile-nav-open")) {
                closeNav();
            }
        });
    }

    function setupClickSparks() {
        document.addEventListener("pointerdown", function (event) {
            if (!event.isPrimary) {
                return;
            }

            const burst = document.createElement("div");
            burst.className = "spark-burst";
            burst.style.left = event.clientX + "px";
            burst.style.top = event.clientY + "px";

            const sparkCount = 12;
            for (let index = 0; index < sparkCount; index++) {
                const spark = document.createElement("span");
                spark.className = "spark";
                const angle = (360 / sparkCount) * index + Math.random() * 14;
                const travel = 24 + Math.random() * 26;
                const hue = 28 + Math.round(Math.random() * 24);

                spark.style.setProperty("--spark-angle", angle + "deg");
                spark.style.setProperty("--spark-travel", travel + "px");
                spark.style.setProperty("--spark-hue", String(hue));
                burst.appendChild(spark);
            }

            document.body.appendChild(burst);
            setTimeout(function () {
                burst.remove();
            }, 620);
        });
    }

    async function refreshCartBadge() {
        const badge = document.getElementById("cartBadge");
        if (!badge) {
            return;
        }

        if (!getToken()) {
            badge.textContent = "0";
            return;
        }

        try {
            const cart = await apiRequest("/cart");
            badge.textContent = String(cart.totalItems || 0);
        } catch (error) {
            badge.textContent = "0";
        }
    }

    function setupHeader() {
        const authCta = document.getElementById("authCta");
        const logoutBtn = document.getElementById("logoutBtn");
        const activePath = window.location.pathname.split("/").pop() || "index.html";

        document.querySelectorAll(".nav-links a").forEach(function (link) {
            const href = link.getAttribute("href");
            if (href === activePath || (activePath === "" && href === "index.html")) {
                link.classList.add("active");
            }
        });

        if (getToken()) {
            if (authCta) {
                authCta.textContent = getUsername() || "My Account";
                authCta.setAttribute("href", "auth.html");
            }
            if (logoutBtn) {
                logoutBtn.classList.remove("hidden");
                logoutBtn.addEventListener("click", function () {
                    clearAuth();
                    showToast("Logged out successfully", "info");
                    refreshCartBadge();
                    window.location.href = "index.html";
                });
            }
        } else {
            if (authCta) {
                authCta.textContent = "Login / Signup";
                authCta.setAttribute("href", "auth.html");
            }
            if (logoutBtn) {
                logoutBtn.classList.add("hidden");
            }
        }
    }

    function ensureLoggedIn(nextPage) {
        if (getToken()) {
            return true;
        }
        const redirect = nextPage || window.location.pathname.split("/").pop() || "index.html";
        showToast("Please login to continue", "error");
        window.location.href = "auth.html?next=" + encodeURIComponent(redirect);
        return false;
    }

    document.addEventListener("DOMContentLoaded", function () {
        setupHeader();
        setupMobileNavDrawer();
        setupReveals();
        setupClickSparks();
        refreshCartBadge();
    });

    window.GlowStore = {
        getToken,
        getUsername,
        setAuth,
        clearAuth,
        apiRequest,
        formatPrice,
        calculateDiscount,
        showToast,
        refreshCartBadge,
        ensureLoggedIn
    };
})();
