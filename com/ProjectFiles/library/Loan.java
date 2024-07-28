package com.ProjectFiles.library;

import java.util.Date;

/**
 * Represents a loan of a book to a member in the library.
 * This class tracks essential details such as the book loaned, the member to whom the book is loaned,
 * the date when the loan started, and the date when the book was returned.
 */
public class Loan {
    private Book book;            // The book that is loaned
    private Member member;        // The member who has taken the loan
    private Date loanDate;        // The date on which the loan was initiated
    private Date returnDate;      // The date on which the book was returned, null if the book is still out

    /**
     * Constructs a new Loan object for a given book and member.
     * Automatically sets the loan date to the current date, indicating when the loan was created.
     *
     * @param member the member to whom the book is loaned
     * @param book   the book that is being loaned
     */
    public Loan(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.loanDate = new Date();  // Capture the current time as the loan date
    }

    /**
     * Marks the loan as returned by setting the return date to the current date.
     * Also updates the book's status to indicate that it is available again.
     */
    public void markAsReturned() {
        this.returnDate = new Date();  // Record the current time as the return date
        book.checkIn();  // Notify the book that it has been returned
    }

    /**
     * Gets the return date of the book.
     * This method returns the date on which the book was returned. If the book has not yet been returned, this will be null.
     *
     * @return the return date, or null if the book has not been returned
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * Retrieves the book involved in this loan.
     * This method allows access to the book object that is associated with this loan.
     *
     * @return the loaned book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Retrieves the date the loan was made.
     * This method provides the date when the loan transaction was initiated.
     *
     * @return the date the loan was made
     */
    public Date getLoanDate() {
        return loanDate;
    }
}
