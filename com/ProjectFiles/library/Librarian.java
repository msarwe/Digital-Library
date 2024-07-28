package com.ProjectFiles.library;

/**
 * Represents a librarian in the library management system.
 * This class utilizes the Factory Method design pattern for creating instances of books and members,
 * ensuring that object creation is centralized and can be managed or modified independently of the system's use cases.
 */
public class Librarian {

    /**
     * Creates a new book with the specified title, author, year, and amount of copies.
     * This factory method abstracts the instantiation of Book objects, ensuring any required validation or setup
     * can be handled centrally.
     *
     * @param title  the title of the book to be created, should be non-null and non-empty
     * @param author the author of the book, should be non-null and non-empty
     * @param year   the year of publication of the book
     * @param amount the initial number of copies of the book, must be non-negative
     * @return a new instance of Book
     * @throws IllegalArgumentException if title or author are null/empty or amount is negative
     */
    public Book createBook(String title, String author, int year, int amount) {
        if (title == null || title.isEmpty() || author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Book title and author must not be empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Book amount cannot be negative.");
        }
        return new Book(title, author, year, amount);
    }

    /**
     * Creates a new member with the specified name and ID.
     * This method centralizes the creation of Member objects, allowing for uniform initialization and
     * potentially incorporating additional setup or checks in the future.
     *
     * @param name the name of the member to be created, should not be null or empty
     * @param id   the unique identifier for the member, must be a positive integer
     * @return a new instance of Member
     * @throws IllegalArgumentException if the name is null/empty or if the ID is not positive
     */
    public Member createMember(String name, int id) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Member name must not be empty.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Member ID must be positive.");
        }
        return new Member(name, id);
    }
}
