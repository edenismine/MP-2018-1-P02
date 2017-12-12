package fciencias.myp.concentration;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.CardLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginPanel {

	private JFrame frame;
	private JTextField fieldUsername;
	private JPasswordField fieldPassword;
	private JLabel labelOutput;
	private static final Color ERR = new Color(0xE95B62);
	// private static final Color SCC = new Color(0x5DE864);
	private static final Color BGC = new Color(0x303030);
	private static final HashMap<Character, String> REGEX;
	private static final HashMap<Character, String> ERRMessages;

	static {
		Properties prop = new Properties();
		InputStream input = null;
		String filename = "src\\fciencias\\myp\\concentration\\settings.properties";
		try {
			input = new FileInputStream(filename);
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		REGEX = new HashMap<>();
		REGEX.put('u', prop.getProperty("username.regexp"));
		REGEX.put('p', prop.getProperty("password.regexp"));

		ERRMessages = new HashMap<>();
		ERRMessages.put('u', prop.getProperty("username.error"));
		ERRMessages.put('p', prop.getProperty("password.error"));
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPanel window = new LoginPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Change style
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel header = new JPanel();
		header.setBackground(new Color(0x202020));
		frame.getContentPane().add(header, BorderLayout.NORTH);
		header.setLayout(new CardLayout(0, 0));

		JLabel title = new JLabel("CONCENTRATION");
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Tahoma", Font.BOLD, 30));
		title.setHorizontalAlignment(SwingConstants.CENTER);
		header.add(title, "name_362615496362526");

		JPanel panel = new JPanel();
		panel.setBackground(new Color(0x303030));
		panel.setLayout(null);
		frame.getContentPane().add(panel);

		JLabel labelUsername = new JLabel("Username");
		labelUsername.setForeground(Color.WHITE);
		labelUsername.setBounds(177, 54, 144, 14);
		labelUsername.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(labelUsername);

		fieldUsername = new JTextField();
		fieldUsername.setName("username");
		fieldUsername.setBounds(177, 74, 144, 20);
		fieldUsername.setHorizontalAlignment(SwingConstants.LEFT);
		fieldUsername.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		panel.add(fieldUsername);

		JLabel labelPassword = new JLabel("Password");
		labelPassword.setForeground(Color.WHITE);
		labelPassword.setBounds(177, 100, 144, 14);
		labelPassword.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(labelPassword);

		fieldPassword = new JPasswordField();
		fieldPassword.setName("password");
		fieldPassword.setBounds(177, 120, 144, 20);
		fieldPassword.setHorizontalAlignment(SwingConstants.LEFT);
		fieldPassword.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		panel.add(fieldPassword);

		JButton buttonLogIn = new JButton("Log in");
		buttonLogIn.setBounds(177, 166, 65, 23);
		buttonLogIn.addActionListener(e -> login(getCredentials()));
		panel.add(buttonLogIn);

		JButton buttonRegister = new JButton("Register");
		buttonRegister.setBounds(248, 166, 73, 23);
		buttonRegister.addActionListener(e -> register(getCredentials()));
		panel.add(buttonRegister);

		labelOutput = new JLabel();
		labelOutput.setHorizontalAlignment(SwingConstants.RIGHT);
		labelOutput.setBounds(177, 146, 144, 14);
		panel.add(labelOutput);

		// Geometry
		frame.setSize(500, 300);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		int xPosition = dim.width / 2 - frame.getWidth() / 2;
		int yPosition = dim.height / 2 - frame.getHeight() / 2;
		frame.setLocation(xPosition, yPosition);

		// Settings
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private void login(String[] credentials) {
		if (credentials != null) {
            System.out.println(String.format("LOG IN\nusername:\t%s\npassword:\t%s",
                    credentials[0], credentials[1]));
        }
    }

	private void register(String[] credentials) {
        if (credentials != null) {
            if (checkSecureCredentials()) {
                System.out.println(String.format("SIGN UP\nusername:\t%s\npassword:\t%s",
                        credentials[0], credentials[1]));
                JOptionPane.showMessageDialog(frame,
                        String.format("LoginApp, %s.\nYou've been registered, you can now log in.", credentials[0]));
            }
        }
    }

	private boolean checkSecureCredentials() {
		// Setup
        StringBuilder warning = new StringBuilder();
        boolean valid = true;

        // Hide output label
        labelOutput.setText("null");
        labelOutput.setForeground(BGC);

        // Visits every field
        JTextField[] fields = { fieldUsername, fieldPassword };
        for (JTextField field : fields) {
            String txt = labelOutput.getText();
            String name = field.getName();
            char option = name.charAt(0);

            // Reset field's border
            field.setBorder(javax.swing.BorderFactory.createEmptyBorder());

            // If a field doesn't match its corresponding regex, make `valid` false, add corresponding message, modify
            // the output text and update the field's style
            if (!field.getText().matches(REGEX.get(option))) {
                valid = false;

                // format output label
                labelOutput.setText(txt.equals("null") ? "Invalid " + name : txt + ", invalid " + name);
                labelOutput.setForeground(ERR);

                // format field
                field.setBorder(javax.swing.BorderFactory.createLineBorder(ERR, 1));

                // build warning as necessary
                warning.append(ERRMessages.get(option));
            }
        }

        // If unsuccessful show a dialog
        if (!valid) {
            JOptionPane.showMessageDialog(frame,
                    warning.toString().replace(". ", "\n"),
                    labelOutput.getText(), JOptionPane.INFORMATION_MESSAGE);
        }
        return valid;
	}

	private String[] getCredentials() {
		String[] credentials = null;
		if (!emptyFields()) {
			credentials = new String[2];
			credentials[0] = fieldUsername.getText();
			credentials[1] = new String(fieldPassword.getPassword());
			labelOutput.setForeground(BGC);
		} else {
			labelOutput.setText("Fields cannot be empty.");
			labelOutput.setForeground(ERR);
		}
		return credentials;
	}

	private boolean emptyFields() {
		LinkedList<Boolean> all = new LinkedList<>();
		boolean result;
		JTextField[] fields = { fieldUsername, fieldPassword };
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
}
