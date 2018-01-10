package mx.unam.fciencias.myp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * A simple login panel that handles login and register operations. It should be used to lock
 * a Frame until a user has logged in successfully. The way this panel communicates succcess
 * to the owner frame is via changing its name to the user's username.
 *
 * @author Daniel Aragon
 */
public class LoginPanel extends JDialog {
    private static final GameProperties PROPERTIES = GameProperties.getInstance();
    private static final GameDatabaseManager DB = GameDatabaseManager.getInstance();
    private static final Color C_IDLE = new Color(Integer.parseInt
            (PROPERTIES.getProperty("color.idle"), 16));
    private static final Color C_ERROR = new Color(Integer.parseInt
            (PROPERTIES.getProperty("color.error"), 16));
    private static final Color C_SUCCESS = new Color(Integer.parseInt
            (PROPERTIES.getProperty("color.success"), 16));
    private static final HashMap<Character, String> REGEX;
    private static final HashMap<Character, String> ERRORS;
    private static boolean success = false;

    static {
        REGEX = new HashMap<>();
        REGEX.put('u', PROPERTIES.getProperty("username.regexp"));
        REGEX.put('p', PROPERTIES.getProperty("password.regexp"));

        ERRORS = new HashMap<>();
        ERRORS.put('u', PROPERTIES.getProperty("username.error"));
        ERRORS.put('p', PROPERTIES.getProperty("password.error"));
    }

    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JLabel labelOutput;

    /**
     * Initialize the contents of the dialog.
     */
    private LoginPanel(Frame owner) {
        // Change style
        super(owner, "Login or register", true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JDialog loginPanel = this;

        setModal(true);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel header = new JPanel();
        header.setBackground(new Color(0x202020));
        getContentPane().add(header, BorderLayout.NORTH);
        header.setLayout(new CardLayout(0, 0));

        JLabel title = new JLabel("CONCENTRATION");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(title, "name_362615496362526");

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x303030));
        panel.setLayout(null);
        getContentPane().add(panel);

        JLabel labelUsername = new JLabel("Username");
        labelUsername.setForeground(Color.WHITE);
        labelUsername.setBounds(111, 54, 276, 14);
        labelUsername.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(labelUsername);

        fieldUsername = new JTextField();
        fieldUsername.setName("username");
        fieldUsername.setBounds(111, 74, 276, 20);
        fieldUsername.setHorizontalAlignment(SwingConstants.LEFT);
        fieldUsername.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        panel.add(fieldUsername);

        JLabel labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.WHITE);
        labelPassword.setBounds(111, 100, 276, 14);
        labelPassword.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(labelPassword);

        fieldPassword = new JPasswordField();
        fieldPassword.setName("password");
        fieldPassword.setBounds(111, 120, 276, 20);
        fieldPassword.setHorizontalAlignment(SwingConstants.LEFT);
        fieldPassword.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        panel.add(fieldPassword);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBounds(111, 166, 276, 23);
        buttonPanel.setBackground(C_IDLE);
        JButton buttonLogIn = new JButton("Log in");
        JButton buttonRegister = new JButton("Register");
        //buttonLogIn.setBounds(111, 166, 65, 23);
        //buttonRegister.setBounds(248, 166, 73, 23);
        buttonLogIn.addActionListener(e -> login(getFieldsText()));
        buttonRegister.addActionListener(e -> register(getFieldsText()));
        buttonPanel.add(buttonLogIn);
        buttonPanel.add(buttonRegister);
        panel.add(buttonPanel);

        labelOutput = new JLabel();
        labelOutput.setHorizontalAlignment(SwingConstants.RIGHT);
        labelOutput.setBounds(111, 146, 276, 14);
        panel.add(labelOutput);

        // Geometry
        setSize(500, 300);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPosition = dim.width / 2 - getWidth() / 2;
        int yPosition = dim.height / 2 - getHeight() / 2;
        setLocation(xPosition, yPosition);

        // Settings
        addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                if (!success) {
                    // close the owner frame if the user was not able to log in.
                    owner.dispatchEvent(new WindowEvent(loginPanel, WindowEvent.WINDOW_CLOSING));
                } else {
                    // else communicate success by changing the owner frame's title.
                    owner.setName(getFieldsText()[0]);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }
        });
        setResizable(false);
        setVisible(true);
        toFront();
    }

    static void lock(Frame owner) {
        new LoginPanel(owner);
    }

    /**
     * Tries to log in using the credentials inside the provided array.
     *
     * @param fieldsText the text inside the fields as an array, null if any of the two fields is empty.
     */
    private void login(String[] fieldsText) {
        if (fieldsText != null) {
            int authenticate = -1;
            try {
                authenticate = DB.authenticate(fieldsText[0], fieldsText[1]);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Hide output label
            labelOutput.setText("null");
            labelOutput.setForeground(C_IDLE);

            // message
            String message;

            switch (authenticate) {
                case GameDatabaseManager.LOGIN_SUCCESSFUL:
                    message = String.format("Welcome, %s!", fieldsText[0]);
                    // format output label
                    labelOutput.setText(message);
                    labelOutput.setForeground(C_SUCCESS);

                    // format fields
                    fieldUsername.setBorder(javax.swing.BorderFactory.createLineBorder(C_SUCCESS, 1));
                    fieldPassword.setBorder(javax.swing.BorderFactory.createLineBorder(C_SUCCESS, 1));

                    // welcome user
                    JOptionPane.showMessageDialog(this, message, "Welcome!", JOptionPane.INFORMATION_MESSAGE);

                    // go to owner
                    success = true;
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                    break;
                case GameDatabaseManager.USER_DOES_NOT_EXIST:
                    message = String.format("'%s' does not exist!", fieldsText[0]);
                    // format output label
                    labelOutput.setText(message);
                    labelOutput.setForeground(C_ERROR);

                    // format fields
                    fieldUsername.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));
                    fieldPassword.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));

                    // communicate error to user
                    JOptionPane.showMessageDialog(this, message, "Unable to log in", JOptionPane.WARNING_MESSAGE);
                    break;
                case GameDatabaseManager.WRONG_PASSWORD:
                    message = "Incorrect password!";
                    // format output label
                    labelOutput.setText(message);
                    labelOutput.setForeground(C_ERROR);

                    // format fields
                    fieldUsername.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));
                    fieldPassword.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));

                    // communicate error to user
                    JOptionPane.showMessageDialog(this, message, "Unable to log in", JOptionPane.WARNING_MESSAGE);
                    break;
                default:
                    message = String.format("Unkown error code %d", authenticate);
                    // format output label
                    labelOutput.setText(message);
                    labelOutput.setForeground(C_ERROR);

                    // format fields
                    fieldUsername.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));
                    fieldPassword.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));

                    // communicate error to user
                    JOptionPane.showMessageDialog(this, message, "Unable to log in", JOptionPane.WARNING_MESSAGE);
                    break;
            }
        }
    }

    /**
     * Tries to register the credentials inside the provided array.
     *
     * @param fieldsText the text inside the fields as an array, null if any of the two fields is empty.
     */
    private void register(String[] fieldsText) {
        if (fieldsText != null) {
            if (validFields()) {
                try {
                    if (DB.addUser(fieldsText[0], fieldsText[1])) {
                        JOptionPane.showMessageDialog(this, String
                                        .format("Welcome, %s.\nYou've been registered, you can now log in.", fieldsText[0]),
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                String.format("Sorry, '%s' is already taken. Try again using a diferent username.",
                                        fieldsText[0]),
                                "Unable to register this username!", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (HeadlessException | SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if the fields' content is valid according to its corresponding regex.
     * If any of the fields is invalid it shows a message, changes the output label
     * and the fields' style.
     *
     * @return true if the fields's content is valid, false otherwise
     */
    private boolean validFields() {
        // Setup
        StringBuilder warning = new StringBuilder();
        boolean valid = true;

        // Hide output label
        labelOutput.setText("null");
        labelOutput.setForeground(C_IDLE);

        // Visits every field
        JTextField[] fields = {fieldUsername, fieldPassword};
        for (JTextField field : fields) {
            String txt = labelOutput.getText();
            String name = field.getName();
            char option = name.charAt(0);

            // Reset field's border
            field.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // If a field doesn't match its corresponding regex, make `valid` false, add
            // corresponding message, modify the output text and update the field's style
            if (!field.getText().matches(REGEX.get(option))) {
                valid = false;

                // format output label
                labelOutput.setText(txt.equals("null") ? "Invalid " + name : txt + ", invalid " + name);
                labelOutput.setForeground(C_ERROR);

                // format field
                field.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));

                // build warning as necessary
                warning.append(ERRORS.get(option));
            }
        }

        // If unsuccessful show a dialog
        if (!valid) {
            JOptionPane.showMessageDialog(this, warning.toString().replace(". ", "\n"), labelOutput.getText(),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return valid;
    }

    /**
     * Extracts the fields' contents.
     *
     * @return the fields' contents as an array or null if any of the fields are
     * empty
     */
    private String[] getFieldsText() {
        String[] fieldsText = null;
        if (!checkEmptyFields()) {
            fieldsText = new String[2];
            fieldsText[0] = fieldUsername.getText();
            fieldsText[1] = new String(fieldPassword.getPassword());
            labelOutput.setForeground(C_IDLE);
        }
        return fieldsText;
    }

    /**
     * This method checks if any of the form fields is empty, and formats them
     * accordingly.
     *
     * @return true if any of the fields is empty
     */
    private boolean checkEmptyFields() {
        LinkedList<Boolean> all = new LinkedList<>();
        boolean result;
        JTextField[] fields = {fieldUsername, fieldPassword};
        for (JTextField field : fields) {
            result = field.getText().isEmpty();
            if (result) {
                field.setBorder(javax.swing.BorderFactory.createLineBorder(C_ERROR, 1));
                labelOutput.setText("Fields cannot be empty.");
                labelOutput.setForeground(C_ERROR);
            } else {
                field.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            }
            all.add(result);
        }
        return all.contains(Boolean.TRUE);
    }
}
