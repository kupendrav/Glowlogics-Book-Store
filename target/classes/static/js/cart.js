document.addEventListener("DOMContentLoaded", function () {
    const authMessage = document.getElementById("cartAuthMessage");
    const cartGrid = document.getElementById("cartGrid");
    const cartItemsList = document.getElementById("cartItemsList");
    const cartSummary = document.getElementById("cartSummary");
    const checkoutButton = document.getElementById("checkoutButton");

    function renderSummary(cart) {
        cartSummary.innerHTML = ""
            + "<div class=\"summary-row\"><span>Total Items</span><span>" + (cart.totalItems || 0) + "</span></div>"
            + "<div class=\"summary-row\"><span>Subtotal</span><span>" + GlowStore.formatPrice(cart.totalPrice || 0) + "</span></div>"
            + "<div class=\"summary-row total\"><span>Total</span><span>" + GlowStore.formatPrice(cart.totalPrice || 0) + "</span></div>";
    }

    function renderItems(cart) {
        if (!cart.items || !cart.items.length) {
            cartItemsList.innerHTML = "<div class=\"empty-state\">Your cart is empty. Add books from the homepage.</div>";
            checkoutButton.classList.add("hidden");
            renderSummary({ totalItems: 0, totalPrice: 0 });
            return;
        }

        checkoutButton.classList.remove("hidden");

        cartItemsList.innerHTML = cart.items.map(function (item) {
            return ""
                + "<article class=\"cart-item\">"
                + "<img src=\"" + item.book.imageUrl + "\" alt=\"" + item.book.title + " cover\">"
                + "<div>"
                + "<h3>" + item.book.title + "</h3>"
                + "<p>" + item.book.author + "</p>"
                + "<p>Qty: " + item.quantity + "</p>"
                + "<strong>" + GlowStore.formatPrice(item.lineTotal) + "</strong>"
                + "</div>"
                + "<button class=\"btn-outline\" type=\"button\" data-remove=\"" + item.book.id + "\">Remove</button>"
                + "</article>";
        }).join("");

        renderSummary(cart);

        cartItemsList.querySelectorAll("[data-remove]").forEach(function (button) {
            button.addEventListener("click", async function () {
                const bookId = Number(button.getAttribute("data-remove"));
                try {
                    const updatedCart = await GlowStore.apiRequest("/cart/" + bookId, {
                        method: "DELETE"
                    });
                    GlowStore.showToast("Book removed from cart", "info");
                    renderItems(updatedCart);
                    GlowStore.refreshCartBadge();
                } catch (error) {
                    GlowStore.showToast(error.message, "error");
                }
            });
        });
    }

    async function loadCart() {
        if (!GlowStore.getToken()) {
            cartGrid.classList.add("hidden");
            authMessage.classList.remove("hidden");
            authMessage.innerHTML = "Please <a href=\"auth.html?next=cart.html\">login</a> to view your cart.";
            return;
        }

        try {
            const cart = await GlowStore.apiRequest("/cart");
            renderItems(cart);
        } catch (error) {
            cartGrid.classList.add("hidden");
            authMessage.classList.remove("hidden");
            authMessage.textContent = error.message;
        }
    }

    loadCart();
});
