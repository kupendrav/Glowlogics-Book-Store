document.addEventListener("DOMContentLoaded", function () {
    const titleElement = document.getElementById("bookTitle");
    const imageElement = document.getElementById("bookImage");
    const authorElement = document.getElementById("bookAuthor");
    const priceRowElement = document.getElementById("bookPriceRow");
    const summaryElement = document.getElementById("bookSummary");
    const authorIntroductionElement = document.getElementById("authorIntroduction");
    const descriptionElement = document.getElementById("bookDescription");
    const addToCartButton = document.getElementById("addToCartButton");
    const reviewsList = document.getElementById("reviewsList");
    const reviewForm = document.getElementById("reviewForm");

    const params = new URLSearchParams(window.location.search);
    const bookId = Number(params.get("id"));

    if (!bookId) {
        titleElement.textContent = "Book not found";
        summaryElement.textContent = "Please go back and pick a valid book to see the summary.";
        authorIntroductionElement.textContent = "Author introduction will be available once a valid book is selected.";
        descriptionElement.textContent = "Please go back and pick a valid book.";
        addToCartButton.disabled = true;
        reviewForm.classList.add("hidden");
        return;
    }

    let currentBook = null;

    function renderBook(book) {
        currentBook = book;
        const discount = GlowStore.calculateDiscount(book.price, book.originalPrice);

        titleElement.textContent = book.title;
        imageElement.src = book.imageUrl;
        imageElement.alt = book.title + " cover";
        authorElement.textContent = "by " + book.author + " | " + book.category;
        summaryElement.textContent = (book.shortSummary || book.description || "Summary will be updated soon.").trim();
        authorIntroductionElement.textContent = (book.authorIntroduction
            || (book.author + " is a noted writer in " + book.category + ", known for engaging storytelling.")).trim();
        descriptionElement.textContent = book.description;

        priceRowElement.innerHTML = ""
            + "<span class=\"price-old\">" + GlowStore.formatPrice(book.originalPrice) + "</span>"
            + "<span>" + GlowStore.formatPrice(book.price) + "</span>"
            + (discount > 0 ? "<span class=\"discount-badge\">₹ " + discount + " Off</span>" : "");

        renderReviews(book.reviews || []);
    }

    function renderReviews(reviews) {
        if (!reviews.length) {
            reviewsList.innerHTML = "<div class=\"empty-state\">No reviews yet. Be the first to add one.</div>";
            return;
        }

        reviewsList.innerHTML = reviews
            .slice()
            .reverse()
            .map(function (review) {
                const createdAt = review.createdAt ? new Date(review.createdAt).toLocaleDateString() : "Today";
                return ""
                    + "<article class=\"review-item\">"
                    + "<div class=\"review-meta\"><span>" + review.username + "</span><span>⭐ " + review.rating + " | " + createdAt + "</span></div>"
                    + "<p>" + review.comment + "</p>"
                    + "</article>";
            })
            .join("");
    }

    async function loadBook() {
        try {
            const book = await GlowStore.apiRequest("/books/" + bookId);
            renderBook(book);
        } catch (error) {
            titleElement.textContent = "Unable to load book";
            summaryElement.textContent = "We could not load the short summary right now.";
            authorIntroductionElement.textContent = "We could not load the author introduction right now.";
            descriptionElement.textContent = error.message;
            addToCartButton.disabled = true;
            reviewForm.classList.add("hidden");
            GlowStore.showToast(error.message, "error");
        }
    }

    addToCartButton.addEventListener("click", async function () {
        if (!GlowStore.ensureLoggedIn("book-details.html?id=" + bookId)) {
            return;
        }
        try {
            await GlowStore.apiRequest("/cart", {
                method: "POST",
                body: { bookId: bookId, quantity: 1 }
            });
            GlowStore.showToast("Added to cart", "success");
            GlowStore.refreshCartBadge();
        } catch (error) {
            GlowStore.showToast(error.message, "error");
        }
    });

    reviewForm.addEventListener("submit", async function (event) {
        event.preventDefault();
        if (!currentBook) {
            return;
        }

        const comment = document.getElementById("comment").value.trim();
        const rating = Number(document.getElementById("rating").value);
        if (!comment) {
            GlowStore.showToast("Please write a comment", "error");
            return;
        }

        try {
            await GlowStore.apiRequest("/books/" + bookId + "/reviews", {
                method: "POST",
                body: {
                    comment: comment,
                    rating: rating
                }
            });
            GlowStore.showToast("Review added", "success");
            reviewForm.reset();
            loadBook();
        } catch (error) {
            GlowStore.showToast(error.message, "error");
        }
    });

    loadBook();
});
