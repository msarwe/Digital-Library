package com.ProjectFiles.library;

/**
 * Represents a book in the library management system.
 * Each book is defined by its title, author, publication year, and the number of copies available.
 * This class provides methods to manage the checkout and return processes by adjusting the number of available copies.
 */
public class Book {
    private String title;       // Title of the book
    private String author;      // Author of the book
    private int year;           // Publication year of the book
    private int amount;         // Number of copies of the book available

    /**
     * Constructs a new Book with the specified title, author, year, and initial number of copies.
     * This constructor initializes a book with all its fundamental properties set.
     *
     * @param title  the title of the book, not null or empty
     * @param author the author of the book, not null or empty
     * @param year   the year the book was published
     * @param amount the initial number of copies available; should be non-negative
     */
    public Book(String title, String author, int year, int amount) {
        if (title == null || title.isEmpty() || author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Title and author cannot be null or empty.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        this.title = title;
        this.author = author;
        this.year = year;
        this.amount = amount;
    }

    /**
     * Checks out one copy of the book if available. Decreases the amount of available copies by one.
     * This method ensures that the amount of books does not fall below zero.
     */
    public void checkOut() {
        if (amount > 0) {
            amount--;
        }
    }

    /**
     * Returns a copy of the book to the library, increasing the available amount by one.
     */
    public void checkIn() {
        amount++;
    }

    /**
     * Checks if at least one copy of the book is available for checkout.
     *
     * @return true if at least one copy is available, false otherwise
     */
    public boolean isAvailable() {
        return amount > 0;
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the publication year of the book.
     *
     * @return the year the book was published
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the number of available copies of the book.
     *
     * @return the current number of available copies
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Provides a string representation of the book, including its title, author, year of publication,
     * and the number of copies currently available.
     *
     * @return a formatted string with the book's details
     */
    @Override
    public String toString() {
        return String.format("%s by %s (%d) - Copies: %d", title, author, year, amount);
    }
}
