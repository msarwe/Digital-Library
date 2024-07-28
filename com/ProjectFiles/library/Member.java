package com.ProjectFiles.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library member with a unique ID and name.
 * Members can borrow and return books, which are tracked as loans.
 */
public class Member {
    private String name;       // Name of the member
    private int id;            // Unique identifier for the member
    private List<Loan> loans;  // List of loans associated with the member, representing borrowed books

    /**
     * Constructs a new Member with the specified name and ID.
     * Initializes the member with an empty list of loans, indicating no current loans.
     *
     * @param name the name of the member
     * @param id   the unique identifier of the member
     */
    public Member(String name, int id) {
        this.name = name;
        this.id = id;
        this.loans = new ArrayList<>(); // Initializes with an empty list of loans
    }

    /**
     * Retrieves the member's name.
     *
     * @return the name of the member
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the unique identifier of the member.
     *
     * @return the ID of the member
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the list of all loans associated with the member.
     *
     * @return a list containing all loans of the member
     */
    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * Provides a string representation of the member.
     * Includes their name and ID, formatted for easy identification.
     *
     * @return a string representation of the member in the format "Name (ID)"
     */
    @Override
    public String toString() {
        return name + " (" + id + ")";
    }

    /**
     * Allows the member to borrow a book if it is available.
     * Adds a new loan to their list of loans and updates the book's availability.
     *
     * @param book the book that the member wishes to borrow
     */
    public void borrowBook(Book book) {
        if (book.isAvailable()) {
            Loan loan = new Loan(this, book);
            loans.add(loan);
            book.checkOut();
        }
    }

    /**
     * Allows the member to return a book they have borrowed.
     * Identifies the corresponding loan and marks it as returned.
     *
     * @param book the book that is being returned
     */
    public void returnBook(Book book) {
        loans.stream()
                .filter(loan -> loan.getBook().equals(book))
                .findFirst()
                .ifPresent(Loan::markAsReturned);  // If the loan is found, mark it as returned
    }
}
