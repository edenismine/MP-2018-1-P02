package mx.unam.fciencias.myp;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * The Home page of the game.
 *
 * The user can select any of the following options:
 * <ol>
 * <li>Start a new game.</li>
 * <li>View the player's stats.</li>
 * <li>Reset the player's stats.</li>
 * <li>Exit the program.</li>
 * </ol>
 */
class HomePage extends JFrame {
    /**
     * Generated serialVersionUID
     */
    private static final long serialVersionUID = -6831370209295904244L;
    /**
     * Reference to the game's database manager.
     */
    private static final GameDatabaseManager DB = GameDatabaseManager.getInstance();

    HomePage() {
        super();
        // initial settings
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        HomePage thisPage = this;
        setTitle("Concentration game");
        setLayout(new GridLayout(1, 4));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set up buttons
        MenuOption newGameButton = new MenuOption(MenuOption.OptionType.NEW, () -> {
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {

                @Override
                protected Boolean doInBackground() {
                    return Concentration.playNewGame();
                }

                @Override
                protected void done() {
                    setEnabled(true);
                    setVisible(true);
                    try {
                        boolean playerWon = get();
                        DB.updateStats(getName(), playerWon);
                    } catch (InterruptedException | ExecutionException | SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            };
            setEnabled(false);
            worker.execute();
        });
        MenuOption gameStatsButton = new MenuOption(MenuOption.OptionType.STATS, () -> {
            String message = "Something went wrong, could not retrieve game stats for "
                    + getName();
            try {
                message = DB.getStats(getName());
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, message,getName()
                    + "'s game stats", JOptionPane.INFORMATION_MESSAGE);
        });
        MenuOption resetStatsButton = new MenuOption(MenuOption.OptionType.CLEAR, () -> {
            int n = JOptionPane.showConfirmDialog(thisPage, "Are you sure you want to reset your game stats? All your progress will be lost and it cannot be undone.", "Reset stats",
                    JOptionPane.OK_CANCEL_OPTION);
            if (n == JOptionPane.OK_OPTION) {
                try {
                    DB.resetStats(getName());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        MenuOption exitButton = new MenuOption(MenuOption.OptionType.EXIT, () -> {
            dispose();
            System.exit(0);
        });
        add(newGameButton);
        add(gameStatsButton);
        add(resetStatsButton);
        add(exitButton);
        pack();

        // Geometry and position
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        setLocation(dim.width / 2 - getWidth() / 2, dim.height / 2 - getHeight() / 2);
        // final settings
        setResizable(false);
        setVisible(true);
    }
}