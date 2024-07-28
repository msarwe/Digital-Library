package com.ProjectFiles.library;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the central management system of a library.
 * This class is implemented as a Singleton to ensure that only one instance of the library exists throughout the application.
 * It handles the operations for managing books, members, loans, and user accounts.
 */
public class Library {
    private static Library instance;  // Singleton instance of the library for global access
    private List<Book> books;         // List to store all books in the library
    private List<Member> members;     // List to store all registered members
    private List<Loan> loans;         // List to store all loans
    private List<User> users;         // List to store all users including librarians and members

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes lists for managing books, members, loans, and users.
     */
    private Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        loans = new ArrayList<>();
        users = new ArrayList<>();
    }

    /**
     * Provides the global access point to the Singleton instance of the library.
     * If the instance doesn't exist, it initializes the library.
     *
     * @return the single, static instance of the Library
     */
    public static synchronized Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    /**
     * Adds a book to the library's book list.
     *
     * @param book the book to be added to the library
     */
    public void addBook(Book book) {
        books.add(book);
    }

    /**
     * Removes a book from the library.
     *
     * @param book the book to be removed from the library
     */
    public void removeBook(Book book) {
        books.remove(book);
    }

    /**
     * Adds a new member to the library. Ensures that each member has a unique ID.
     *
     * @param member the new member to add to the library
     * @throws IllegalArgumentException if the member ID is not unique
     */
    public void addMember(Member member) {
        if (!isMemberIdUnique(member.getId())) {
            throw new IllegalArgumentException("Member ID must be unique.");
        }
        members.add(member);
    }

    /**
     * Checks if a member ID is unique within the library.
     *
     * @param id the ID to check against existing member IDs
     * @return true if the ID is unique, false otherwise
     */
    public boolean isMemberIdUnique(int id) {
        return members.stream().noneMatch(m -> m.getId() == id);
    }

    /**
     * Removes a member from the library.
     *
     * @param member the member to remove
     */
    public void removeMember(Member member) {
        members.remove(member);
    }

    /**
     * Returns a list of all books in the library.
     *
     * @return a list containing all the books
     */
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Returns a list of all registered members.
     *
     * @return a list containing all the members
     */
    public List<Member> getMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Adds a loan record when a book is borrowed.
     *
     * @param loan the loan record to add
     */
    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    /**
     * Removes a loan record when a book is returned.
     *
     * @param loan the loan record to remove
     */
    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    /**
     * Returns a list of all active loans in the library.
     *
     * @return a list of all loans
     */
    public List<Loan> getLoans() {
        return new ArrayList<>(loans);
    }

    /**
     * Adds a user to the library system. This can be a librarian or a member based on the user role.
     *
     * @param user the user to add
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Returns a list of all users registered in the library system.
     *
     * @return a list containing all users
     */
    public List<User> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Generates a summary of the current status of the library, displaying counts of books, available books, members, and active loans.
     *
     * @return a formatted string representing the current status of the library
     */
    public String getLibraryStatus() {
        return String.format("Total Books: %d, Available Books: %d, Total Members: %d, Active Loans: %d",
                books.size(),
                books.stream().filter(Book::isAvailable).count(),
                members.size(),
                loans.stream().filter(loan -> loan.getReturnDate() == null).count());
    }
}
