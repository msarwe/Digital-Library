package com.ProjectFiles.library;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a login dialog in the library system where users authenticate themselves.
 * It captures the user's name and ID and determines their role based on the input.
 * This class handles user authentication and conditional member creation.
 */
public class LoginDialog extends JDialog {
    private JTextField userNameField;  // Field for user to input their name
    private JTextField userIdField;    // Field for user to input their ID
    private JButton loginButton;       // Button to initiate the login process
    private String userRole;           // Role determined based on the user credentials
    private Library library;           // Reference to the singleton instance of Library

    /**
     * Constructs a new LoginDialog attached to a parent JFrame.
     * Initializes the user interface and sets dialog properties.
     *
     * @param parent the parent frame to which this dialog is attached
     */
    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);  // Set modal to true to block other windows until this dialog is dismissed
        this.library = Library.getInstance();  // Access the singleton Library instance
        setupUI();
        pack();
        setLocationRelativeTo(parent);  // Center the dialog relative to the parent frame
    }

    /**
     * Sets up the user interface components and their layout.
     */
    private void setupUI() {
        setLayout(new GridLayout(3, 2, 10, 10));  // Use GridLayout for an organized layout of labels and text fields

        add(new JLabel("User Name:"));
        userNameField = new JTextField(10);
        add(userNameField);

        add(new JLabel("User ID:"));
        userIdField = new JTextField(10);
        add(userIdField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> performLogin());  // Attach action listener to handle login
        add(loginButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);  // Dispose of the dialog when closing
    }

    /**
     * Handles the login process by validating the inputs and determining the user's role.
     * It checks if the credentials match existing members or if it's a new, unique member.
     */
    private void performLogin() {
        String userName = userNameField.getText().trim();
        String userId = userIdField.getText().trim();

        // Validate inputs are not empty
        if (userName.isEmpty() || userId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both name and ID must be provided.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Attempt to parse user ID and handle member authentication or creation
        try {
            int parsedId = Integer.parseInt(userId);
            Member existingMember = library.getMembers().stream()
                    .filter(m -> m.getId() == parsedId)
                    .findFirst().orElse(null);

            if (existingMember != null) {
                if (!existingMember.getName().equals(userName)) {
                    JOptionPane.showMessageDialog(this, "Member name does not match the ID.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                userRole = "Member";
            } else {
                if (userName.equals("0") && userId.equals("0")) {
                    userRole = "Librarian";
                } else {
                    userRole = "Member";
                    library.addMember(new Member(userName, parsedId));  // Add new member if unique
                }
            }
            dispose();  // Close the dialog upon successful login
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid user ID format.", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Returns the user role determined by the login process.
     *
     * @return the user's role as a String
     */
    public String getUserRole() {
        return userRole;
    }

    /**
     * Retrieves the user's name from the text field.
     *
     * @return the user's name as a String
     */
    public String getUserName() {
        return userNameField.getText().trim();
    }

    /**
     * Retrieves the user's ID from the text field.
     *
     * @return the user's ID as a String
     */
    public String getUserId() {
        return userIdField.getText().trim();
    }
}
