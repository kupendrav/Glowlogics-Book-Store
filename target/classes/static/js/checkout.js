document.addEventListener("DOMContentLoaded", function () {
    const authMessage = document.getElementById("checkoutAuthMessage");
    const checkoutGrid = document.getElementById("checkoutGrid");
    const checkoutItemsList = document.getElementById("checkoutItemsList");
    const checkoutForm = document.getElementById("checkoutForm");
    const checkoutConfirmation = document.getElementById("checkoutConfirmation");

    let currentCart = null;

    function renderCart(cart) {
        currentCart = cart;

        if (!cart.items || !cart.items.length) {
            checkoutItemsList.innerHTML = "<div class=\"empty-state\">Your cart is empty. Add books before checkout.</div>";
            checkoutForm.classList.add("hidden");
            return;
        }

        checkoutForm.classList.remove("hidden");

        checkoutItemsList.innerHTML = ""
            + cart.items.map(function (item) {
                return ""
                    + "<article class=\"cart-item\">"
                    + "<img src=\"" + item.book.imageUrl + "\" alt=\"" + item.book.title + " cover\">"
                    + "<div>"
                    + "<h3>" + item.book.title + "</h3>"
                    + "<p>" + item.book.author + "</p>"
                    + "<p>Quantity: " + item.quantity + "</p>"
                    + "<strong>" + GlowStore.formatPrice(item.lineTotal) + "</strong>"
                    + "</div>"
                    + "</article>";
            }).join("")
            + "<div class=\"summary-list\">"
            + "<div class=\"summary-row total\"><span>Total Payable</span><span>" + GlowStore.formatPrice(cart.totalPrice) + "</span></div>"
            + "</div>";
    }

    async function loadCart() {
        if (!GlowStore.getToken()) {
            checkoutGrid.classList.add("hidden");
            authMessage.classList.remove("hidden");
            authMessage.innerHTML = "Please <a href=\"auth.html?next=checkout.html\">login</a> to continue checkout.";
            return;
        }

        try {
            const cart = await GlowStore.apiRequest("/cart");
            renderCart(cart);
        } catch (error) {
            checkoutGrid.classList.add("hidden");
            authMessage.classList.remove("hidden");
            authMessage.textContent = error.message;
        }
    }

    checkoutForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (!currentCart || !currentCart.items || !currentCart.items.length) {
            GlowStore.showToast("Your cart is empty", "error");
            return;
        }

        const fullName = document.getElementById("fullName").value.trim();
        const email = document.getElementById("email").value.trim();
        const address = document.getElementById("address").value.trim();

        if (fullName.length < 2 || !email || !address) {
            GlowStore.showToast("Please fill all required fields", "error");
            return;
        }

        try {
            const response = await GlowStore.apiRequest("/checkout", {
                method: "POST",
                body: {
                    fullName: fullName,
                    email: email,
                    address: address
                }
            });

            checkoutForm.classList.add("hidden");
            checkoutConfirmation.classList.remove("hidden");
            checkoutConfirmation.innerHTML = ""
                + "<h3>Order Confirmed</h3>"
                + "<p>" + response.message + "</p>"
                + "<p><strong>Order ID:</strong> " + response.orderId + "</p>"
                + "<p><strong>Items:</strong> " + response.itemCount + "</p>"
                + "<p><strong>Total:</strong> " + GlowStore.formatPrice(response.totalAmount) + "</p>"
                + "<a class=\"btn\" href=\"index.html\">Continue Shopping</a>";

            checkoutItemsList.innerHTML = "<div class=\"empty-state\">Cart has been cleared after successful checkout.</div>";
            GlowStore.refreshCartBadge();
            GlowStore.showToast("Order placed successfully", "success");
        } catch (error) {
            GlowStore.showToast(error.message, "error");
        }
    });

    loadCart();
});
