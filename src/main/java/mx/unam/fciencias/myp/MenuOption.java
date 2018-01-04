package mx.unam.fciencias.myp;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;

public class MenuOption extends JPanel {
	interface Clickable {
		void click();
	}
	enum OptionType {
		EXIT("Exit the game", getImageIcon("/exit.png", "Exit")),
		NEW("Start a new game", getImageIcon("/new.png", "New game")),
		STATS("View your gaming stats", getImageIcon("/stats.png", "View stats")),
		CLEAR("Reset your stats", getImageIcon("/clear.png", "Clear history"));
		
		Icon icon;
		String caption;

		OptionType(String caption, Icon icon) {
			this.caption = caption;
			this.icon = icon;
		}
	}

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -3088890058631223710L;

	/**
	 * Create the panel.
	 */
	MenuOption(OptionType option, Clickable clickable) {
        super();
        // Add invisible border, tooltips and layout
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setToolTipText(option.caption);
		setLayout(new BorderLayout(0, 0));
		setBackground(Color.WHITE);

        // Add icon
        JLabel icon = new JLabel("");
		icon.setHorizontalAlignment(SwingConstants.CENTER);
        icon.setIcon(option.icon);
        add(icon, BorderLayout.CENTER);

        // Mouse events
		addMouseListener(new MouseListener() {
            final Color ENTERED = getBackground().darker();
            final Color EXITED = getBackground();
            final Color PRESSED = getBackground().darker().darker();
            final Color RELEASED = getBackground();
			@Override
			public void mouseClicked(MouseEvent arg0) {
				clickable.click();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(ENTERED);

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(EXITED);

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				setBackground(PRESSED);

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				setBackground(RELEASED);

			}
		});
    }
    
    private static ImageIcon getImageIcon(String path, String description) {
        java.net.URL imgURL = path.getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
