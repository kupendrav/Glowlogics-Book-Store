document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("searchInput");
    const booksCarousel = document.getElementById("booksCarousel");
    const categoryFilters = document.getElementById("categoryFilters");
    const resultCount = document.getElementById("bookResultCount");
    const catalogTitle = document.getElementById("catalogTitle");
    const categoryArrowLeft = document.getElementById("categoryArrowLeft");
    const categoryArrowRight = document.getElementById("categoryArrowRight");
    const booksArrowLeft = document.getElementById("booksArrowLeft");
    const booksArrowRight = document.getElementById("booksArrowRight");

    const CATEGORY_ORDER = [
        "Finance",
        "Classics",
        "Manga",
        "Mythology",
        "Horror",
        "Biographies",
        "Mystery",
        "RomCom",
        "Self-Help"
    ];

    const CATEGORY_THEME_MAP = {
        All: { soft: "#e0f2fe", primary: "#0284c7", dark: "#0c4a6e" },
        Finance: { soft: "#dcfce7", primary: "#16a34a", dark: "#166534" },
        Classics: { soft: "#ede9fe", primary: "#7c3aed", dark: "#4c1d95" },
        Manga: { soft: "#fee2e2", primary: "#ef4444", dark: "#991b1b" },
        Mythology: { soft: "#ffedd5", primary: "#ea580c", dark: "#9a3412" },
        Horror: { soft: "#e2e8f0", primary: "#334155", dark: "#0f172a" },
        Biographies: { soft: "#cffafe", primary: "#0891b2", dark: "#164e63" },
        Mystery: { soft: "#fce7f3", primary: "#db2777", dark: "#831843" },
        RomCom: { soft: "#ffe4e6", primary: "#e11d48", dark: "#881337" },
        "Self-Help": { soft: "#fef9c3", primary: "#ca8a04", dark: "#713f12" },
        Default: { soft: "#e2e8f0", primary: "#0f766e", dark: "#134e4a" }
    };

    let allBooks = [];
    let orderedCategories = [];
    let activeCategory = "All";

    function getCategoryTheme(category) {
        return CATEGORY_THEME_MAP[category] || CATEGORY_THEME_MAP.Default;
    }

    function sortCategories(categories) {
        return categories.slice().sort(function (first, second) {
            const firstIndex = CATEGORY_ORDER.indexOf(first);
            const secondIndex = CATEGORY_ORDER.indexOf(second);
            const firstScore = firstIndex === -1 ? Number.MAX_SAFE_INTEGER : firstIndex;
            const secondScore = secondIndex === -1 ? Number.MAX_SAFE_INTEGER : secondIndex;
            if (firstScore !== secondScore) {
                return firstScore - secondScore;
            }
            return first.localeCompare(second);
        });
    }

    function renderFilters() {
        const categories = ["All"].concat(orderedCategories);

        categoryFilters.innerHTML = categories.map(function (category) {
            const theme = getCategoryTheme(category);
            const activeClass = category === activeCategory ? "chip category-chip active" : "chip category-chip";
            const style = "--category-soft:" + theme.soft + ";--category-color:" + theme.primary + ";--category-color-dark:" + theme.dark + ";";
            return "<button type=\"button\" class=\"" + activeClass + "\" data-category=\"" + category + "\" style=\"" + style + "\">" + category + "</button>";
        }).join("");

        categoryFilters.querySelectorAll("button").forEach(function (button) {
            button.addEventListener("click", function () {
                activeCategory = button.getAttribute("data-category");
                renderFilters();
                renderBooks();
            });
        });

        updateArrowState(categoryFilters, categoryArrowLeft, categoryArrowRight);
    }

    function buildSummary(book) {
        const summary = (book.shortSummary || book.description || "").trim();
        if (!summary) {
            return "";
        }
        if (summary.length <= 110) {
            return summary;
        }
        return summary.slice(0, 107) + "...";
    }

    function buildBookCard(book) {
        const discount = GlowStore.calculateDiscount(book.price, book.originalPrice);
        const summary = buildSummary(book);
        return ""
            + "<article class=\"book-card\">"
            + "<img src=\"" + book.imageUrl + "\" alt=\"" + book.title + " cover\">"
            + "<div class=\"book-content\">"
            + "<h3 class=\"book-title\">" + book.title + "</h3>"
            + "<p class=\"book-author\">" + book.author + "</p>"
            + (summary ? "<p class=\"book-summary\">" + summary + "</p>" : "")
            + "<div class=\"badge-row\">"
            + "<span class=\"rating-badge\">" + book.category + "</span>"
            + "</div>"
            + "<div class=\"price-row\">"
            + "<span class=\"price-old\">" + GlowStore.formatPrice(book.originalPrice) + "</span>"
            + "<span>" + GlowStore.formatPrice(book.price) + "</span>"
            + (discount > 0 ? "<span class=\"discount-badge\">₹ " + discount + " Off</span>" : "")
            + "</div>"
            + "<div class=\"action-row\">"
            + "<a class=\"btn-outline\" href=\"book-details.html?id=" + book.id + "\">Details</a>"
            + "<button class=\"btn\" type=\"button\" data-add-cart=\"" + book.id + "\">Add To Bag</button>"
            + "</div>"
            + "</div>"
            + "</article>";
    }

    function getFilteredBooks() {
        const keyword = (searchInput.value || "").trim().toLowerCase();
        return allBooks.filter(function (book) {
            const titleMatch = book.title.toLowerCase().includes(keyword);
            const categoryMatch = activeCategory === "All" || book.category === activeCategory;
            return titleMatch && categoryMatch;
        });
    }

    function updateCatalogTitle(filteredCount) {
        if (!catalogTitle) {
            return;
        }

        if (activeCategory === "All") {
            catalogTitle.textContent = "All Categories";
        } else {
            catalogTitle.textContent = activeCategory + " Collection";
        }

        resultCount.textContent = filteredCount + " books found";
    }

    function renderBooks() {
        const filtered = getFilteredBooks();
        updateCatalogTitle(filtered.length);

        if (!filtered.length) {
            booksCarousel.innerHTML = "<div class=\"empty-state\">No books found for your search. Try another title or category.</div>";
            updateArrowState(booksCarousel, booksArrowLeft, booksArrowRight);
            return;
        }

        booksCarousel.innerHTML = filtered.map(buildBookCard).join("");
        booksCarousel.scrollTo({ left: 0, behavior: "smooth" });
        updateArrowState(booksCarousel, booksArrowLeft, booksArrowRight);

        booksCarousel.querySelectorAll("[data-add-cart]").forEach(function (button) {
            button.addEventListener("click", async function () {
                const bookId = Number(button.getAttribute("data-add-cart"));
                if (!GlowStore.ensureLoggedIn("index.html")) {
                    return;
                }
                try {
                    await GlowStore.apiRequest("/cart", {
                        method: "POST",
                        body: { bookId: bookId, quantity: 1 }
                    });
                    GlowStore.showToast("Book added to bag", "success");
                    GlowStore.refreshCartBadge();
                } catch (error) {
                    GlowStore.showToast(error.message, "error");
                }
            });
        });
    }

    function updateArrowState(target, leftButton, rightButton) {
        if (!target || !leftButton || !rightButton) {
            return;
        }

        const maxScrollLeft = Math.max(0, target.scrollWidth - target.clientWidth);
        leftButton.disabled = target.scrollLeft <= 4;
        rightButton.disabled = target.scrollLeft >= maxScrollLeft - 4;
    }

    function setupArrowButtons() {
        if (categoryArrowLeft && categoryArrowRight) {
            categoryArrowLeft.addEventListener("click", function () {
                categoryFilters.scrollBy({ left: -240, behavior: "smooth" });
            });
            categoryArrowRight.addEventListener("click", function () {
                categoryFilters.scrollBy({ left: 240, behavior: "smooth" });
            });
        }

        if (booksArrowLeft && booksArrowRight) {
            booksArrowLeft.addEventListener("click", function () {
                booksCarousel.scrollBy({ left: -320, behavior: "smooth" });
            });
            booksArrowRight.addEventListener("click", function () {
                booksCarousel.scrollBy({ left: 320, behavior: "smooth" });
            });
        }

        categoryFilters.addEventListener("scroll", function () {
            updateArrowState(categoryFilters, categoryArrowLeft, categoryArrowRight);
        });

        booksCarousel.addEventListener("scroll", function () {
            updateArrowState(booksCarousel, booksArrowLeft, booksArrowRight);
        });

        window.addEventListener("resize", function () {
            updateArrowState(categoryFilters, categoryArrowLeft, categoryArrowRight);
            updateArrowState(booksCarousel, booksArrowLeft, booksArrowRight);
        });
    }

    async function loadBooks() {
        try {
            allBooks = await GlowStore.apiRequest("/books");
            orderedCategories = sortCategories(Array.from(new Set(allBooks.map(function (book) {
                return book.category;
            }))));
            if (orderedCategories.length > 0) {
                activeCategory = orderedCategories[0];
            }
            renderFilters();
            renderBooks();
        } catch (error) {
            booksCarousel.innerHTML = "<div class=\"empty-state\">Unable to load books right now. Please refresh.</div>";
            GlowStore.showToast(error.message, "error");
        }
    }

    searchInput.addEventListener("input", renderBooks);

    setupArrowButtons();
    loadBooks();
});
