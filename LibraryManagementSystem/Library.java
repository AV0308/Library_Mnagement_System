import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Core service class that manages the library's book collection.
 * <p>
 * Demonstrates DATA HANDLING: books are stored in a Map keyed by ID for
 * fast O(1) lookup/removal, while insertion order is preserved (LinkedHashMap)
 * so listings display in a predictable order.
 * <p>
 * Demonstrates PROBLEM STRUCTURING: this class owns all business rules
 * (what counts as "available", how IDs are generated, etc.) so the console
 * UI layer only has to call simple methods and doesn't touch raw data.
 */
public class Library {

    private final Map<Integer, Book> booksById = new LinkedHashMap<>();
    private int nextId = 101; // IDs are auto-generated, starting from 101

    /**
     * Adds a new book to the library. The ID is generated automatically
     * so callers never have to worry about ID collisions.
     */
    public Book addBook(String title, String author, String isbn, String genre) {
        int id = nextId++;
        Book book = new Book(id, title, author, isbn, genre);
        booksById.put(id, book);
        return book;
    }

    /** Removes a book from the library by its ID. */
    public void removeBook(int bookId) throws BookNotFoundException {
        Book removed = booksById.remove(bookId);
        if (removed == null) {
            throw new BookNotFoundException(bookId);
        }
    }

    /** Looks up a single book by ID. */
    public Book getBookById(int bookId) throws BookNotFoundException {
        Book book = booksById.get(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        return book;
    }

    /** Case-insensitive partial search of books by title. */
    public List<Book> searchByTitle(String keyword) {
        String needle = keyword.toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book book : booksById.values()) {
            if (book.getTitle().toLowerCase().contains(needle)) {
                results.add(book);
            }
        }
        return results;
    }

    /** Returns every book currently in the library. */
    public List<Book> getAllBooks() {
        return new ArrayList<>(booksById.values());
    }

    /** Returns only the books that are currently available to issue. */
    public List<Book> getAvailableBooks() {
        List<Book> available = new ArrayList<>();
        for (Book book : booksById.values()) {
            if (book.isAvailable()) {
                available.add(book);
            }
        }
        return available;
    }

    /** Issues a book to a member, after checking it exists and is available. */
    public void issueBook(int bookId, String memberName) throws BookNotFoundException, BookNotAvailableException {
        Book book = getBookById(bookId);
        if (!book.isAvailable()) {
            throw new BookNotAvailableException(book.getTitle());
        }
        book.markIssued(memberName);
    }

    /**
     * Returns a book and reports how many days late it was, if any.
     *
     * @return days overdue (0 means on time)
     */
    public long returnBook(int bookId) throws BookNotFoundException, BookNotIssuedException {
        Book book = getBookById(bookId);
        if (book.isAvailable()) {
            throw new BookNotIssuedException(book.getTitle());
        }
        return book.markReturned();
    }

    public int totalBooks() {
        return booksById.size();
    }
}
