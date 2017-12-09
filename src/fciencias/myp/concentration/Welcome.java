package fciencias.myp.concentration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Daniel Aragon
 * @since 12/8/2017
 */
public class Welcome extends JFrame {
    private JPanel mainPanel;
    private JTextField username;
    private JPasswordField password;
    private JButton logInButton;
    private JButton registerButton;
    private JLabel output;
    private JTextField[] fields = {username, password};
    private static final Color ERR = new Color(0xE95B62);
    private static final Color SCC = new Color(0x5DE864);
    private static final Color BGC = new Color(0x303030);
    private static final HashMap<Character, String> REGEX;
    private static final HashMap<Character, String> ERRMessages;

    static {
        REGEX = new HashMap<>();
        REGEX.put('u', "^(?=.{4,15}$)[a-zA-Z0-9]+(?:_?[a-zA-Z0-9])*$");
        REGEX.put('p', "^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8,}$");

        ERRMessages = new HashMap<>();
        ERRMessages.put('u', "Only alphanumerical characters and infix underscores are admissible as usernames. " +
                "Usernames must be at least 4 and at most 15 characters long. ");
        ERRMessages.put('p', "A strong password must contain lower and upper case letters, special characters and " +
                "numbers, and be at least 8 characters long. ");
    }

    public Welcome() {

        // Change style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Components
        for (JComponent field : fields) {
            field.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            field.setMinimumSize(new Dimension(-1, 20));
        }
        username.setName("username");
        password.setName("password");

        // Log in button
        logInButton.addActionListener(e -> logIn(getCredentials()));

        // Sign up button
        registerButton.setForeground(Color.white);
        registerButton.setBorder(null);
        registerButton.setBackground(null);
        registerButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register(getCredentials());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Nothing
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setForeground(Color.cyan);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setForeground(Color.white);
            }
        });

        // Geometry
        this.setContentPane(mainPanel);
        this.setSize(500, 300);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPosition = dim.width / 2 - this.getWidth() / 2;
        int yPosition = dim.height / 2 - this.getHeight() / 2;
        this.setLocation(xPosition, yPosition);

        // Settings
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private boolean allEmpty() {
        LinkedList<Boolean> all = new LinkedList<>();
        boolean result;
        for (JTextField field : fields) {
            result = field.getText().isEmpty();
            if (result) {
                field.setBorder(javax.swing.BorderFactory.createLineBorder(ERR, 1));
            } else {
                field.setBorder(javax.swing.BorderFactory.createEmptyBorder());
            }
            all.add(result);
        }

        return all.contains(Boolean.TRUE);
    }

    private String[] getCredentials() {
        String[] credentials = null;
        if (!allEmpty()) {
            credentials = new String[2];
            credentials[0] = username.getText();
            credentials[1] = new String(password.getPassword());
            output.setForeground(BGC);
        } else {
            output.setText("Fields cannot be empty.");
            output.setForeground(ERR);
        }
        return credentials;
    }

    private boolean checkSecureCredentials() {
        // Setup
        StringBuilder warning = new StringBuilder();
        boolean valid = true;

        // Hide output label
        output.setText("null");
        output.setForeground(BGC);

        // Visits every field
        for(JTextField field : fields){
            String txt = output.getText();
            String name = field.getName();
            char option = name.charAt(0);

            // Reset field's border
            field.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // If a field doesn't match its corresponding regex, make `valid` false, add corresponding message, modify
            // the output text and update the field's format
            if(!field.getText().matches(REGEX.get(option))){
                valid = false;

                // format output label
                output.setText(txt.equals("null") ? "Invalid " + name : txt + ", invalid " + name);
                output.setForeground(ERR);

                // format field
                field.setBorder(javax.swing.BorderFactory.createLineBorder(ERR, 1));

                // build warning as necessary
                warning.append(ERRMessages.get(option));
            }
        }

        // If unsuccessful show a dialog
        if(!valid){
            JOptionPane.showMessageDialog(this,
                    warning.toString().replace(". ", "\n"),
                    output.getText(), JOptionPane.INFORMATION_MESSAGE);
        }
        return valid;
    }

    private void logIn(String[] credentials) {
        if (credentials != null) {
            System.out.println(String.format("LOG IN\nusername:\t%s\npassword:\t%s",
                    credentials[0], credentials[1]));
        }
    }

    private void register(String[] credentials) {
        if (credentials != null) {
            if(checkSecureCredentials()){
                System.out.println(String.format("SIGN UP\nusername:\t%s\npassword:\t%s",
                        credentials[0], credentials[1]));
                JOptionPane.showMessageDialog(this,
                        String.format("Welcome, %s.\nYou've been registered, you can now log in.", credentials[0]));
            }
        }
    }

    public static void main(String[] args) {
        new Welcome();
    }
}
