package com.ProjectFiles.library;

/**
 * Represents a user of the library system, which can be either a Librarian or a Member.
 * The role of the user is determined based on their unique identifier (userID).
 */
public class User {
    private String userName;  // The name of the user
    private String userID;    // Unique identifier for the user, used to determine their role
    private String role;      // Role of the user, either "Librarian" or "Member"

    /**
     * Constructs a new User with the specified name and userID.
     * Automatically determines the user's role based on the userID.
     *
     * @param userName the name of the user
     * @param userID   the unique identifier for the user
     */
    public User(String userName, String userID) {
        this.userName = userName;
        this.userID = userID;
        this.role = determineRole(userID);  // Determines role based on userID
    }

    /**
     * Determines the role of the user based on their userID.
     * Assumes that a userID of "0" is reserved for the Librarian.
     *
     * @param userID the userID to evaluate
     * @return the role as "Librarian" if userID is "0", otherwise "Member"
     */
    private String determineRole(String userID) {
        return userID.equals("0") ? "Librarian" : "Member";
    }

    /**
     * Retrieves the user's name.
     *
     * @return the name of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Retrieves the user's unique identifier.
     *
     * @return the userID of the user
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Retrieves the user's role within the library system.
     *
     * @return the role of the user, either "Librarian" or "Member"
     */
    public String getRole() {
        return role;
    }

    /**
     * Provides a string representation of the user, combining their name and userID.
     *
     * @return a string representation of the user in the format "Name (userID)"
     */
    @Override
    public String toString() {
        return userName + " (" + userID + ")";
    }
}
