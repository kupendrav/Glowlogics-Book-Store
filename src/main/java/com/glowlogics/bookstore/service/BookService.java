package com.glowlogics.bookstore.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.glowlogics.bookstore.model.Book;
import com.glowlogics.bookstore.model.Review;

import jakarta.annotation.PostConstruct;

@Service
public class BookService {

    private final Map<Long, Book> books = new LinkedHashMap<>();
    private final Random random = new Random(20260415L);

    private static final List<String> SHOWCASE_CATEGORIES = List.of(
            "Finance",
            "Classics",
            "Manga",
            "Mythology",
            "Horror",
            "Biographies",
            "Mystery",
            "RomCom",
            "Self-Help"
    );

    private static final String[] TITLE_ADJECTIVES = {
            "Hidden", "Silent", "Golden", "Electric", "Velvet",
            "Eternal", "Crimson", "Broken", "Shifting", "Luminous",
            "Burning", "Falling", "Rising", "Wild", "Secret",
            "Curious", "Midnight", "Radiant", "Fearless", "Wandering"
    };

    private static final String[] TITLE_NOUNS = {
            "Chronicle", "Compass", "Labyrinth", "Promise", "Archive",
            "Whisper", "Theory", "Blueprint", "Ritual", "Memory",
            "Mirror", "Storm", "Legacy", "Code", "Journey",
            "Puzzle", "Pulse", "Letter", "Path", "Reckoning"
    };

    private static final String[] AUTHOR_FIRST_NAMES = {
            "Aarav", "Diya", "Ishan", "Naina", "Kabir", "Mira", "Reyansh", "Anaya",
            "Rohan", "Tara", "Vivaan", "Aisha", "Neil", "Kiara", "Arjun", "Siya"
    };

    private static final String[] AUTHOR_LAST_NAMES = {
            "Malhotra", "Sen", "Kapoor", "Raman", "Desai", "Verma", "Chopra", "Bose",
            "Rao", "Sharma", "Dutta", "Mehta", "Kulkarni", "Nair", "Saxena", "Khanna"
    };

    private static final Map<String, String> CATEGORY_PREFIX = Map.of(
            "Finance", "Wealth",
            "Classics", "Timeless",
            "Manga", "Neo Arc",
            "Mythology", "Legends",
            "Horror", "Midnight",
            "Biographies", "Life Notes",
            "Mystery", "Case Files",
            "RomCom", "Meet Cute",
            "Self-Help", "Daily Practice"
    );

    @PostConstruct
    @SuppressWarnings("unused")
    void seedBooks() {
        books.clear();

        long nextId = 1L;
        nextId = seedCuratedBooks(nextId);

        for (String category : SHOWCASE_CATEGORIES) {
            nextId = addRandomBooksForCategory(category, nextId, 20);
        }

        seedReviews();
    }

    private long seedCuratedBooks(long nextId) {
        nextId = addCuratedBook(
                nextId,
                "Never Lie",
                "Freida McFadden",
                "Horror",
                "A newlywed couple discovers dark secrets while snowed in at an isolated manor.",
                "A tense locked-room thriller where every clue points in the wrong direction until the final reveal.",
                "Freida McFadden is a bestselling thriller author known for sharp plot twists and fast-paced storytelling.",
                467,
                550,
                "https://covers.openlibrary.org/b/isbn/9781728296210-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "The Housemaid",
                "Freida McFadden",
                "Horror",
                "A twist-filled psychological thriller about a housemaid who uncovers unsettling truths.",
                "A suspense-heavy page-turner about identity, control, and survival inside a seemingly perfect home.",
                "Freida McFadden blends psychological tension with cinematic twists that keep readers guessing.",
                467,
                550,
                "https://covers.openlibrary.org/b/isbn/9780349132600-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "The Alchemist",
                "Paulo Coelho",
                "Self-Help",
                "A timeless story about pursuing dreams, listening to your heart, and finding purpose.",
                "A reflective journey that turns simple moments into practical lessons about courage and purpose.",
                "Paulo Coelho is a globally admired author whose philosophical stories inspire personal growth.",
                299,
                599,
                "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "No Escape",
                "Lucy Clarke",
                "Mystery",
                "A gripping thriller set on a luxury yacht with hidden motives and a dangerous journey.",
                "A layered mystery where trust erodes quickly and every chapter raises the emotional stakes.",
                "Lucy Clarke writes atmospheric mysteries that combine travel settings with tightly plotted suspense.",
                299,
                599,
                "https://covers.openlibrary.org/b/isbn/9780008408084-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "Dark Rooms",
                "Lynda La Plante",
                "Mystery",
                "A suspenseful crime novel where every clue points to an unexpected suspect.",
                "A dark investigative puzzle driven by character conflict, hidden motives, and procedural detail.",
                "Lynda La Plante is known for grounded crime writing and memorable investigative protagonists.",
                299,
                599,
                "https://covers.openlibrary.org/b/isbn/9781784752934-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "The Psychology of Money",
                "Morgan Housel",
                "Finance",
                "A practical guide to understanding behavior around wealth, greed, and happiness.",
                "A clear, story-driven explanation of how mindset often matters more than mathematics in finance.",
                "Morgan Housel writes practical finance ideas in a calm, narrative style accessible to all readers.",
                399,
                699,
                "https://covers.openlibrary.org/b/isbn/9780857197689-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "The Harry Potter Collection",
                "J. K. Rowling",
                "Classics",
                "A magical journey through friendship, courage, and the battle between good and evil.",
                "A beloved coming-of-age fantasy series with rich world-building, humor, and emotional depth.",
                "J. K. Rowling is one of the most influential modern storytellers in children's and fantasy literature.",
                799,
                1199,
                "https://covers.openlibrary.org/b/isbn/9781408856772-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "Wings of Fire",
                "A. P. J. Abdul Kalam",
                "Biographies",
                "The inspiring autobiography of Dr. A. P. J. Abdul Kalam, from humble beginnings to ISRO.",
                "An inspiring life story that highlights discipline, service, and the power of long-term dreams.",
                "A. P. J. Abdul Kalam was an aerospace scientist and statesman admired for humility and vision.",
                349,
                599,
                "https://covers.openlibrary.org/b/isbn/9788173711466-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "Rich Dad Poor Dad",
                "Robert T. Kiyosaki",
                "Finance",
                "A personal finance classic that shifts your mindset from working for money to building assets.",
                "A mindset-first introduction to personal finance, cash flow awareness, and long-term decision making.",
                "Robert T. Kiyosaki is widely known for introducing financial literacy concepts to mainstream readers.",
                329,
                499,
                "https://covers.openlibrary.org/b/isbn/9781612681139-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "Ikigai",
                "Hector Garcia & Francesc Miralles",
                "Self-Help",
                "Discover the Japanese concept of purpose and long, happy living through everyday habits.",
                "A gentle guide to purpose-building routines that improve energy, focus, and everyday joy.",
                "Hector Garcia and Francesc Miralles write practical wellbeing ideas with warmth and clarity.",
                279,
                499,
                "https://covers.openlibrary.org/b/isbn/9781786330895-L.jpg"
        );

        nextId = addCuratedBook(
                nextId,
                "The Magic of the Lost Temple",
                "Sudha Murty",
                "Mythology",
                "A young girl's summer adventure uncovers ancient stories, family values, and hidden courage.",
                "A heartfelt tale where curiosity leads to cultural roots, empathy, and discovery.",
                "Sudha Murty writes simple, values-driven stories that connect with readers across generations.",
                233,
                275,
                "https://picsum.photos/seed/glowlogics-mythology-curated-1/420/620"
        );

        nextId = addCuratedBook(
                nextId,
                "Amulet: Stonekeeper",
                "Kazu Kibuishi",
                "Manga",
                "A visually rich fantasy quest where siblings discover a magical world and dangerous responsibilities.",
                "A dynamic illustrated adventure balancing emotional storytelling with high-stakes action.",
                "Kazu Kibuishi is known for expressive visual storytelling and imaginative fantasy worlds.",
                499,
                699,
                "https://picsum.photos/seed/glowlogics-manga-curated-1/420/620"
        );

        nextId = addCuratedBook(
                nextId,
                "The Flatshare",
                "Beth O'Leary",
                "RomCom",
                "Two strangers share an apartment at different times and slowly fall in love through notes.",
                "A warm, funny romance built on everyday moments, honest conversations, and emotional growth.",
                "Beth O'Leary writes modern romantic fiction with humor, tenderness, and strong character arcs.",
                379,
                549,
                "https://picsum.photos/seed/glowlogics-romcom-curated-1/420/620"
        );

        nextId = addCuratedBook(
                nextId,
                "The Rabbit Listened",
                "Cori Doerrfeld",
                "Classics",
                "A comforting illustrated story about emotions, empathy, and the healing power of listening.",
                "A gentle reminder that understanding feelings often starts with presence, not advice.",
                "Cori Doerrfeld creates emotionally intelligent stories that support children and adults alike.",
                679,
                799,
                "https://picsum.photos/seed/glowlogics-classics-curated-1/420/620"
        );

        return nextId;
    }

    private long addCuratedBook(long id,
                                String title,
                                String author,
                                String category,
                                String description,
                                String shortSummary,
                                String authorIntroduction,
                                double price,
                                double originalPrice,
                                String imageUrl) {
        addBook(new Book(
                id,
                title,
                author,
                category,
                description,
                shortSummary,
                authorIntroduction,
                price,
                originalPrice,
                imageUrl
        ));
        return id + 1;
    }

    private long addRandomBooksForCategory(String category, long startId, int count) {
        for (int index = 1; index <= count; index++) {
            String title = generateRandomTitle(category, index);
            String author = generateRandomAuthor();
            double price = 199 + random.nextInt(701);
            double originalPrice = price + 60 + random.nextInt(401);

            addBook(new Book(
                    startId,
                    title,
                    author,
                    category,
                    buildDescription(category, title),
                    buildShortSummary(category, title),
                    buildAuthorIntroduction(author, category),
                    price,
                    originalPrice,
                    buildImageUrl(category, index)
            ));
            startId++;
        }
        return startId;
    }

    private String generateRandomTitle(String category, int index) {
        String prefix = CATEGORY_PREFIX.getOrDefault(category, category);
        int adjectiveIndex = Math.floorMod(index * 3 + category.length(), TITLE_ADJECTIVES.length);
        int nounIndex = Math.floorMod(index * 5 + category.hashCode(), TITLE_NOUNS.length);
        return prefix + " " + TITLE_ADJECTIVES[adjectiveIndex] + " " + TITLE_NOUNS[nounIndex];
    }

    private String generateRandomAuthor() {
        String firstName = AUTHOR_FIRST_NAMES[random.nextInt(AUTHOR_FIRST_NAMES.length)];
        String lastName = AUTHOR_LAST_NAMES[random.nextInt(AUTHOR_LAST_NAMES.length)];
        return firstName + " " + lastName;
    }

    private String buildImageUrl(String category, int index) {
        String slug = category.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        return "https://picsum.photos/seed/glowlogics-" + slug + "-" + index + "/420/620";
    }

    private String buildDescription(String category, String title) {
        return switch (category) {
            case "Finance" -> title + " explains money habits, risk awareness, and long-term wealth decisions in practical language.";
            case "Classics" -> title + " revisits timeless themes of identity, belonging, and resilience through memorable storytelling.";
            case "Manga" -> title + " combines stylized visuals, high-stakes arcs, and emotionally grounded character growth.";
            case "Mythology" -> title + " reimagines legends, moral dilemmas, and epic conflicts for modern readers.";
            case "Horror" -> title + " builds dread with atmospheric suspense, psychological twists, and unsettling discoveries.";
            case "Biographies" -> title + " traces defining life moments, setbacks, and breakthroughs with inspiring honesty.";
            case "Mystery" -> title + " follows layered clues, red herrings, and surprising reveals in a tightly plotted investigation.";
            case "RomCom" -> title + " delivers humor, heart, and imperfect relationships that evolve through meaningful conversations.";
            case "Self-Help" -> title + " offers actionable frameworks for focus, consistency, and personal growth in daily life.";
            default -> title + " offers an engaging reading experience with relatable characters and clear storytelling.";
        };
    }

    private String buildShortSummary(String category, String title) {
        return switch (category) {
                        case "Finance" -> title + " is a concise, practical read focused on smarter decisions with money, goals, and habits.";
                        case "Classics" -> title + " is a timeless narrative that remains relevant through universal themes and strong prose.";
                        case "Manga" -> title + " is a fast-moving illustrated story with emotional beats, style, and addictive momentum.";
                        case "Mythology" -> title + " is a modern retelling that makes ancient legends vivid, personal, and easy to follow.";
                        case "Horror" -> title + " is a short, chilling journey filled with tension, hidden truths, and high-impact twists.";
                        case "Biographies" -> title + " is an inspiring life portrait that highlights courage, discipline, and turning points.";
                        case "Mystery" -> title + " is a smart puzzle where every chapter deepens the mystery before a sharp resolution.";
                        case "RomCom" -> title + " is a light, funny romance with heartfelt character growth and memorable chemistry.";
                        case "Self-Help" -> title + " is a motivating guide with simple routines you can apply immediately to daily life.";
                        default -> title + " is a focused and engaging read designed for curious readers.";
        };
    }

    private String buildAuthorIntroduction(String author, String category) {
        return author + " is a contemporary " + category.toLowerCase(Locale.ROOT)
                + " writer known for accessible storytelling, relatable ideas, and reader-friendly pacing.";
    }

    private void seedReviews() {
        addReview(1L, new Review("Asha", "Unputdownable thriller with a sharp ending.", 5, LocalDateTime.now().minusDays(4)));
        addReview(2L, new Review("Rohit", "Great pacing and very addictive.", 4, LocalDateTime.now().minusDays(2)));
        addReview(3L, new Review("Dev", "Simple writing, deep message.", 5, LocalDateTime.now().minusDays(1)));
    }

    private void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public List<Book> getAllBooks() {
        return books.values().stream()
                .sorted(Comparator.comparing(Book::getId))
                .toList();
    }

    public List<Book> searchBooksByTitle(String searchTerm) {
        String normalized = searchTerm.toLowerCase(Locale.ROOT).trim();
        if (normalized.isEmpty()) {
            return getAllBooks();
        }
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(Book::getId))
                .toList();
    }

    public Optional<Book> findBookById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public Review addReview(Long bookId, Review review) {
        Book book = books.get(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }
        List<Review> current = new ArrayList<>(book.getReviews());
        current.add(review);
        book.setReviews(current);
        return review;
    }
}
