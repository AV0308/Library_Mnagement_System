import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Library Management System.
 * <p>
 * This class is intentionally thin: it only handles user input/output
 * and delegates every real decision to the {@link Library} service class.
 */
public class LibraryManagementSystem {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Library library = new Library();

    public static void main(String[] args) {
        seedSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addBookFlow();
                    break;
                case 2:
                    removeBookFlow();
                    break;
                case 3:
                    displayAvailableBooks();
                    break;
                case 4:
                    displayAllBooks();
                    break;
                case 5:
                    searchByTitleFlow();
                    break;
                case 6:
                    searchByIdFlow();
                    break;
                case 7:
                    issueBookFlow();
                    break;
                case 8:
                    returnBookFlow();
                    break;
                case 9:
                    running = false;
                    System.out.println("AV Library Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 9.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Book");
        System.out.println("2. Remove Book");
        System.out.println("3. Display Available Books");
        System.out.println("4. Display All Books");
        System.out.println("5. Search Book by Title");
        System.out.println("6. Search Book by ID");
        System.out.println("7. Issue Book");
        System.out.println("8. Return Book");
        System.out.println("9. Exit");
        System.out.println("======================================");
    }

    // ---- Menu actions ----

    private static void addBookFlow() {
        System.out.println("\n-- Add New Book --");
        String title = readLine("Title: ");
        String author = readLine("Author: ");
        String isbn = readLine("ISBN: ");
        String genre = readLine("Genre: ");
        Book book = library.addBook(title, author, isbn, genre);
        System.out.println("Book added successfully with ID: " + book.getBookId());
    }

    private static void removeBookFlow() {
        System.out.println("\n-- Remove Book --");
        int id = readInt("Enter Book ID to remove: ");
        try {
            library.removeBook(id);
            System.out.println("Book removed successfully.");
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAvailableBooks() {
        System.out.println("\n-- Available Books --");
        List<Book> available = library.getAvailableBooks();
        if (available.isEmpty()) {
            System.out.println("No books are currently available.");
        } else {
            for (Book book : available) {
                System.out.println(book);
            }
        }
    }

    private static void displayAllBooks() {
        System.out.println("\n-- All Books in Library (" + library.totalBooks() + ") --");
        List<Book> all = library.getAllBooks();
        if (all.isEmpty()) {
            System.out.println("The library has no books yet.");
        } else {
            for (Book book : all) {
                System.out.println(book);
            }
        }
    }

    private static void searchByTitleFlow() {
        System.out.println("\n-- Search by Title --");
        String keyword = readLine("Enter title keyword: ");
        List<Book> results = library.searchByTitle(keyword);
        if (results.isEmpty()) {
            System.out.println("No books found matching: " + keyword);
        } else {
            for (Book book : results) {
                System.out.println(book);
            }
        }
    }

    private static void searchByIdFlow() {
        System.out.println("\n-- Search by ID --");
        int id = readInt("Enter Book ID: ");
        try {
            System.out.println(library.getBookById(id));
        } catch (BookNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void issueBookFlow() {
        System.out.println("\n-- Issue Book --");
        int id = readInt("Enter Book ID to issue: ");
        String member = readLine("Issue to (member name): ");
        try {
            library.issueBook(id, member);
            System.out.println("Book issued successfully to " + member + ".");
        } catch (BookNotFoundException | BookNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void returnBookFlow() {
        System.out.println("\n-- Return Book --");
        int id = readInt("Enter Book ID to return: ");
        try {
            long daysLate = library.returnBook(id);
            if (daysLate > 0) {
                double fine = daysLate * 5.0;
                System.out.println("Book returned " + daysLate + " day(s) late. Fine due: " + fine);
            } else {
                System.out.println("Book returned successfully. Thank you!");
            }
        } catch (BookNotFoundException | BookNotIssuedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void seedSampleData() {
        library.addBook("The Hobbit", "J.R.R. Tolkien", "978-0345339683", "Fantasy");
        library.addBook("Clean Code", "Robert C. Martin", "978-0132350884", "Programming");
        library.addBook("A Brief History of Time", "Stephen Hawking", "978-0553380163", "Science");
    }

    // ---- Input helpers ----

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}
