import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a single book in the library.
 * <p>
 * Demonstrates ENCAPSULATION: all fields are private and only accessed
 * through getters, or through controlled methods (markIssued/markReturned)
 * that keep the object's internal state consistent.
 */
public class Book {

    private static final int LOAN_PERIOD_DAYS = 14;

    private final int bookId;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private BookStatus status;

    // Issue-related details; only meaningful while status == ISSUED
    private String issuedTo;
    private LocalDate issueDate;
    private LocalDate dueDate;

    public Book(int bookId, String title, String author, String isbn, String genre) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.status = BookStatus.AVAILABLE;
    }

    // ---- Getters ----
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getGenre() { return genre; }
    public BookStatus getStatus() { return status; }
    public String getIssuedTo() { return issuedTo; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getDueDate() { return dueDate; }

    // ---- Setters for details that can be edited after creation ----
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setGenre(String genre) { this.genre = genre; }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    /**
     * Marks this book as issued to a member and starts the loan period.
     * Package-private: only the Library service is meant to change status,
     * which keeps business rules (e.g. availability checks) in one place.
     */
    void markIssued(String memberName) {
        this.status = BookStatus.ISSUED;
        this.issuedTo = memberName;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusDays(LOAN_PERIOD_DAYS);
    }

    /**
     * Marks this book as returned and clears its issue-related data.
     *
     * @return number of days the book was overdue (0 if on time or early)
     */
    long markReturned() {
        long daysLate = 0;
        if (dueDate != null) {
            daysLate = Math.max(ChronoUnit.DAYS.between(dueDate, LocalDate.now()), 0);
        }
        this.status = BookStatus.AVAILABLE;
        this.issuedTo = null;
        this.issueDate = null;
        this.dueDate = null;
        return daysLate;
    }

    @Override
    public String toString() {
        String base = String.format(
                "ID: %-5d | %-28s | %-20s | ISBN: %-14s | Genre: %-12s | Status: %s",
                bookId, title, author, isbn, genre, status);
        if (status == BookStatus.ISSUED) {
            base += String.format(" (Issued to: %s, Due: %s)", issuedTo, dueDate);
        }
        return base;
    }
}
