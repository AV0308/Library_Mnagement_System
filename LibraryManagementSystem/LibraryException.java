/**
 * Base class for every custom exception thrown by the Library system.
 * Grouping errors under one hierarchy keeps error handling structured:
 * callers can catch specific subtypes, or LibraryException to handle
 * any library-related problem generically.
 */
public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

/** Thrown when no book exists with the given ID. */
class BookNotFoundException extends LibraryException {
    public BookNotFoundException(int bookId) {
        super("No book found with ID: " + bookId);
    }
}

/** Thrown when trying to issue a book that is already issued to someone. */
class BookNotAvailableException extends LibraryException {
    public BookNotAvailableException(String title) {
        super("\"" + title + "\" is already issued and not available right now.");
    }
}

/** Thrown when trying to return a book that isn't currently issued. */
class BookNotIssuedException extends LibraryException {
    public BookNotIssuedException(String title) {
        super("\"" + title + "\" was not issued, so it cannot be returned.");
    }
}
