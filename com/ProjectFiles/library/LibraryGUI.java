package com.ProjectFiles.library;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Main GUI class for the library application.
 * Handles user interactions and displays information based on user roles.
 */
public class LibraryGUI extends JFrame {
    private Library library;// Access to the library's management system
    private JTextArea statusArea;// Area for displaying library status information
    private String userRole;// Role of the currently logged-in user
    private JLabel userLabel;// Label to display user information
    private int currentUserID;// ID of the currently logged-in user
    private String currentUserName;// Name of the currently logged-in user
    private JPanel topPanel;// Top panel for displaying user information and logout option
    private JPanel statusPanel;   // Panel for displaying status information of the library

    // Models for displaying list data
    DefaultListModel<Member> membersModel = new DefaultListModel<>();
    JLabel totalMembersLabel = new JLabel("Total Members: ");


    /**
     * Constructor initializes the library GUI by setting up the login dialog and main user interface.
     */
    public LibraryGUI() {
        library = Library.getInstance();  // Ensures a single instance of Library is used through
        setupLoginAndUI();
    }

    /**
     * Sets up the initial login dialog and initializes the user interface upon successful login.
     */
    private void setupLoginAndUI() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);

        // Retrieve values after the dialog is closed
        userRole = loginDialog.getUserRole();
        currentUserName = loginDialog.getUserName();
        String userIdText = loginDialog.getUserId();

        // Check if the dialog was closed without input or if the input was incomplete
        if (userIdText.isEmpty() || userRole == null || currentUserName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid input provided. Application will exit.", "Exit", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0); // Close application gracefully if no valid input
            return;
        }

        try {
            currentUserID = Integer.parseInt(userIdText);
            initializeUI();
            setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid user ID format. Application will exit.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


    /**
     * Initializes the main user interface components based on the user role.
     */
    private void initializeUI() {
        setTitle("Ariel Digital Library - User: " + currentUserName + " - Role: " + userRole);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        topPanel = new JPanel(new BorderLayout());
        userLabel = new JLabel("Welcome " + (userRole.equals("Librarian") ? "Librarian" : currentUserName + " - ID: " + currentUserID), SwingConstants.RIGHT);
        topPanel.add(userLabel, BorderLayout.NORTH);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize statusPanel using createStatusPanel() and store the reference
        statusPanel = createStatusPanel();
        tabbedPane.addTab("Library Status", null, statusPanel, "View library status");

        if (userRole.equals("Librarian")) {
            tabbedPane.addTab("Manage Members", null, createMemberPanel(), "Add or Remove Members");
            tabbedPane.addTab("Manage Books", null, createBookPanel(), "Add or Delete Books");
        }
        tabbedPane.addTab("Manage Loans", null, createLoanPanel(), "Borrow or Return Books");

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 0) {
                updateStatus();
            }
        });

        add(tabbedPane);
        updateStatus();  // Initial update of the library status
    }

    /**
     * Logs out the current user and restarts the application to show the login screen again.
     */
    private void logout() {
        this.dispose();
        new LibraryGUI().setVisible(true);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Ariel Digital Library", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        panel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);  // Scrollable content panel

        // Active loans counter
        long activeLoansCount = library.getLoans().stream().filter(loan -> loan.getReturnDate() == null).count();
        JLabel activeLoansLabel = new JLabel("Active Loans: " + activeLoansCount);
        contentPanel.add(activeLoansLabel);

        // Total books count and list
        JLabel totalBooksLabel = new JLabel("Total Books: ");
        contentPanel.add(totalBooksLabel);
        DefaultListModel<String> allBooksModel = new DefaultListModel<>();
        library.getBooks().forEach(book -> {
            String bookDetails = book.getTitle() + " by " + book.getAuthor() + " (" + book.getYear() + ")";
            allBooksModel.addElement(bookDetails);
        });
        JList<String> allBooksList = new JList<>(allBooksModel);
        allBooksList.setVisibleRowCount(5);
        contentPanel.add(new JScrollPane(allBooksList));

        // Available books count and list
        JLabel availableBooksLabel = new JLabel("Available Books: ");
        contentPanel.add(availableBooksLabel);
        DefaultListModel<String> availableBooksModel = new DefaultListModel<>();
        Map<String, Integer> bookAvailability = new HashMap<>();
        library.getBooks().forEach(book -> {
            if (book.isAvailable()) {
                bookAvailability.merge(book.getTitle() + " by " + book.getAuthor() + " (" + book.getYear() + ")", book.getAmount(), Integer::sum);
            }
        });
        bookAvailability.forEach((details, count) -> availableBooksModel.addElement(details + " - Copies: " + count));
        JList<String> availableBooksList = new JList<>(availableBooksModel);
        availableBooksList.setVisibleRowCount(5);
        contentPanel.add(new JScrollPane(availableBooksList));

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLibraryStatus(
                allBooksModel,
                availableBooksModel,
                totalBooksLabel,
                availableBooksLabel,
                membersModel,
                totalMembersLabel
        ));
        contentPanel.add(refreshButton);

        // Member details, only visible to the librarian
        if (userRole.equals("Librarian")) {
            contentPanel.add(totalMembersLabel);
            library.getMembers().forEach(membersModel::addElement);
            JList<Member> memberList = new JList<>(membersModel);
            memberList.setVisibleRowCount(5);
            contentPanel.add(new JScrollPane(memberList));
        } else {
            contentPanel.add(totalMembersLabel);
        }

        return panel;
    }

    private void refreshLibraryStatus(
            DefaultListModel<String> allBooksModel,
            DefaultListModel<String> availableBooksModel,
            JLabel totalBooksLabel,
            JLabel availableBooksLabel,
            DefaultListModel<Member> membersModel,
            JLabel totalMembersLabel) {

        System.out.println("Refreshing library status...");  // Debug statement

        // Clear existing models
        allBooksModel.removeAllElements();
        availableBooksModel.removeAllElements();
        membersModel.removeAllElements();

        // Fetch all books and populate lists
        List<Book> allBooks = library.getBooks();
        allBooks.forEach(book -> {
            String bookDetails = book.getTitle() + " by " + book.getAuthor() + " (" + book.getYear() + ")";
            allBooksModel.addElement(bookDetails);  // Add each book's details to the model
            System.out.println("Added book: " + bookDetails);  // Debug each book added
        });

        // Fetch available books and update the available books model
        Map<String, Integer> bookAvailability = new HashMap<>();
        allBooks.stream()
                .filter(Book::isAvailable)
                .forEach(book -> {
                    String details = book.getTitle() + " by " + book.getAuthor() + " (" + book.getYear() + ")";
                    bookAvailability.merge(details, book.getAmount(), Integer::sum);
                });
        bookAvailability.forEach((details, count) -> {
            availableBooksModel.addElement(details + " - Copies: " + count);
            System.out.println("Available book: " + details + " - Copies: " + count);  // Debug each available book added
        });

        // Fetch all members and update the members model
        List<Member> members = library.getMembers();
        members.forEach(member -> {
            membersModel.addElement(member);
            System.out.println("Added member: " + member);  // Debug each member added
        });

        // Update labels with the latest counts
        totalBooksLabel.setText("Total Books: " + allBooksModel.getSize());
        availableBooksLabel.setText("Available Books: " + availableBooksModel.getSize());
        totalMembersLabel.setText("Total Members: " + membersModel.getSize());

        System.out.println("Refresh complete.");  // Confirm completion
    }

    private void updateStatus() {
        if (statusArea != null) {
            statusArea.setText(library.getLibraryStatus());
        }
    }

    // Method to create GridBagConstraints
    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int gridwidth) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        return gbc;
    }

    private JPanel createMemberPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel nameLabel = new JLabel("Name:");
        JTextField memberNameField = new JTextField(10);
        panel.add(nameLabel, createGridBagConstraints(0, 0, 1));
        panel.add(memberNameField, createGridBagConstraints(1, 0, 2));

        JLabel idLabel = new JLabel("ID:");
        JTextField memberIdField = new JTextField(10);
        panel.add(idLabel, createGridBagConstraints(0, 1, 1));
        panel.add(memberIdField, createGridBagConstraints(1, 1, 2));

        JButton addButton = new JButton("Add Member");
        panel.add(addButton, createGridBagConstraints(0, 2, 2));
        JButton removeButton = new JButton("Remove Member");
        panel.add(removeButton, createGridBagConstraints(0, 3, 2));

        addButton.addActionListener(e -> {
            String name = memberNameField.getText().trim();
            String idText = memberIdField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Member name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int id = Integer.parseInt(idText);
                if (!library.isMemberIdUnique(id)) {
                    JOptionPane.showMessageDialog(this, "Error: Member with this ID already exists.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Member member = new Member(name, id);
                    library.addMember(member);
                    JOptionPane.showMessageDialog(this, "Member added: " + name);
                    memberNameField.setText("");
                    memberIdField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        removeButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(memberIdField.getText().trim());
                Member member = library.getMembers().stream().filter(m -> m.getId() == id).findFirst().orElse(null);
                if (member != null) {
                    library.removeMember(member);
                    JOptionPane.showMessageDialog(this, "Member removed: " + member.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "No such member exists!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JTextField yearField = new JTextField(15);
        JTextField amountField = new JTextField(15);
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");

        panel.add(new JLabel("Title:"), createGridBagConstraints(0, 0, 1));
        panel.add(titleField, createGridBagConstraints(1, 0, 2));
        panel.add(new JLabel("Author:"), createGridBagConstraints(0, 1, 1));
        panel.add(authorField, createGridBagConstraints(1, 1, 2));
        panel.add(new JLabel("Year:"), createGridBagConstraints(0, 2, 1));
        panel.add(yearField, createGridBagConstraints(1, 2, 2));
        panel.add(new JLabel("Amount:"), createGridBagConstraints(0, 3, 1)); // Label for amount
        panel.add(amountField, createGridBagConstraints(1, 3, 2)); // TextField for amount

        GridBagConstraints gbcAddButton = createGridBagConstraints(0, 4, 3);
        gbcAddButton.insets.top = 20;  // Increase top margin for add button
        panel.add(addButton, gbcAddButton);

        GridBagConstraints gbcDeleteButton = createGridBagConstraints(0, 5, 3);
        gbcDeleteButton.insets.top = 10;  // Increase top margin for delete button
        panel.add(deleteButton, gbcDeleteButton);

        addButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                int amount = Integer.parseInt(amountField.getText().trim()); // Parse the amount input
                if (title.isEmpty() || author.isEmpty() || amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Title, author and amount cannot be empty, and amount must be greater than zero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Book book = new Book(title, author, year, amount);
                library.addBook(book);
                JOptionPane.showMessageDialog(this, "Book added: " + title + ", Copies: " + amount);
                titleField.setText("");
                authorField.setText("");
                yearField.setText("");
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid year and amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            int year = !yearField.getText().trim().isEmpty() ? Integer.parseInt(yearField.getText().trim()) : -1;
            Book book = library.getBooks().stream()
                    .filter(b -> b.getTitle().equals(title) && b.getYear() == year)
                    .findFirst().orElse(null);
            if (book != null) {
                library.removeBook(book);
                JOptionPane.showMessageDialog(this, "Book deleted: " + title);
            } else {
                JOptionPane.showMessageDialog(this, "No such book exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createLoanPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JTextField bookTitleField = new JTextField(15);
        JButton borrowButton = new JButton("Borrow Book");
        JButton returnButton = new JButton("Return Book");

        panel.add(new JLabel("Book Title:"), createGridBagConstraints(0, 0, 1));
        panel.add(bookTitleField, createGridBagConstraints(1, 0, 2));

        if (userRole.equals("Librarian")) {
            JTextField memberIdField = new JTextField(15);
            panel.add(new JLabel("Member ID:"), createGridBagConstraints(0, 1, 1));
            panel.add(memberIdField, createGridBagConstraints(1, 1, 2));
        }

        panel.add(borrowButton, createGridBagConstraints(0, 2, 2));
        panel.add(returnButton, createGridBagConstraints(0, 3, 2));

        borrowButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            try {
                int memberId = userRole.equals("Librarian") ? Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Member ID:")) : currentUserID;
                Member member = library.getMembers().stream().filter(m -> m.getId() == memberId).findFirst().orElse(null);
                Book book = library.getBooks().stream().filter(b -> b.getTitle().equals(title) && b.isAvailable()).findFirst().orElse(null);
                if (member != null && book != null) {
                    member.borrowBook(book);
                    JOptionPane.showMessageDialog(this, "Book borrowed: " + title);
                } else {
                    JOptionPane.showMessageDialog(this, "Book not available or Member not found", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        returnButton.addActionListener(e -> {
            String title = bookTitleField.getText().trim();
            try {
                int memberId = userRole.equals("Librarian") ? Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Member ID:")) : currentUserID;
                Member member = library.getMembers().stream().filter(m -> m.getId() == memberId).findFirst().orElse(null);
                Book book = library.getBooks().stream().filter(b -> b.getTitle().equals(title) && !b.isAvailable()).findFirst().orElse(null);
                if (member != null && book != null) {
                    member.returnBook(book);
                    JOptionPane.showMessageDialog(this, "Book returned: " + title);
                } else {
                    JOptionPane.showMessageDialog(this, "This book isn't borrowed by this member", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Member ID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}

