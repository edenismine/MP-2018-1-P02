package mx.unam.fciencias.myp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.*;

class Concentration extends JFrame {
    private static final Dimension defaultSize = new Dimension(500, 500);
    private static final Character[] defaultCharacters = {'\u2600', '\u2709', '\u25c6', '\u260e', '\u2708', '\u2764',
            '\u265a', '\u2601'};
    private static final int BOARD_SIDE = 4;
    private final JLabel output;
    private JPanel contentPane;
    private MemoryCard[][] cards;
    private HashSet<Character> deck;
    private HashSet<Character> found;

    private MemoryCard choseA;
    private MemoryCard choseB;
    private int lives = 3;
    private boolean won = false;
    private boolean played;

    private Concentration() {
        this(defaultCharacters, defaultSize.width, defaultSize.height);
    }

    private Concentration(Character[] characters, int width, int height) {
        setTitle("Concentration game");
        setLayout(new BorderLayout(0, 20));
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        Dimension gameSize = new Dimension(width, height);
        int posX = screenSize.width / 2 - gameSize.width / 2;
        int posY = screenSize.height / 2 - gameSize.height / 2;
        setBounds(posX, posY, width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 10));

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (characters == null) {
            characters = defaultCharacters;
        }

        // Create output panel
        JPanel footer = new JPanel(new GridBagLayout());
        output = new JLabel("Setting up the game...");
        output.setMinimumSize(new Dimension(width, 0));
        footer.add(output);

        // Create board panel
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(BOARD_SIDE, BOARD_SIDE));

        // Create empty sets
        deck = new HashSet<>();
        found = new HashSet<>();

        // Create list from provided deck
        ArrayList<Character> symbolsList = new ArrayList<>(BOARD_SIDE * BOARD_SIDE);
        symbolsList.addAll(Arrays.asList(characters));

        // Populate the deck.
        deck.addAll(Arrays.asList(characters));

        // Create pairs and shuffle the deck
        symbolsList.addAll(Arrays.asList(characters));
        Collections.shuffle(symbolsList);

        // Create cards and set them on the board
        Iterator<Character> iterator = symbolsList.iterator();
        int cardWidth = width / BOARD_SIDE;
        int cardHeight = height / BOARD_SIDE;
        cards = new MemoryCard[BOARD_SIDE][BOARD_SIDE];
        for (int i = 0; i < BOARD_SIDE; i++) {
            for (int j = 0; j < BOARD_SIDE; j++) {
                MemoryCard card = new MemoryCard(iterator.next(), cardWidth, cardHeight);
                int finalI = i;
                int finalJ = j;
                card.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectCard(finalI, finalJ);
                        board.paintImmediately(board.getBounds());
                        matchCards();
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                });
                this.cards[i][j] = card; // card dimensions
                board.add(card);
            }
        }

        // Add board and footer to the game
        contentPane.add(board, BorderLayout.CENTER);
        contentPane.add(footer, BorderLayout.SOUTH);
        setContentPane(contentPane);
        pack();
        setVisible(true);
        play();
    }

    static boolean playNewGame() {
        return new Concentration().playerWon();
    }

    private boolean playerWon() {
        if (!played) {
            throw new IllegalStateException("Game has not finished playing");
        }
        return won;
    }

    private void play() {
        if (!played) {
            contentPane.paintImmediately(0, 0, getWidth(), getHeight());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (MemoryCard[] row : cards) {
                for (MemoryCard card : row) {
                    card.setFacingDown();
                }
            }
            output.setText("Click a card to select it.");
            while (lives > 0 && isDisplayable()) {
                if (found.equals(deck)) {
                    won = true;
                    break;
                } else {
                    System.out.print("");
                }
            }
            played = true;
        } else {
            throw new IllegalStateException("Trying to start a game more than once.");
        }
        JOptionPane.showMessageDialog(this, this.won ? "You won!" : "You lost!", "GAME OVER",
                JOptionPane.INFORMATION_MESSAGE);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private void selectCard(int i, int j) {
        MemoryCard selected, card;
        selected = null;
        while (selected == null) {
            card = cards[i][j];
            if (card.select()) {
                selected = card;
            } else {
                output.setText("You can't select a card that is facing up.");
                return;
            }
        }
        if (choseA == null) {
            choseA = selected;
        } else {
            choseB = selected;
        }
    }

    private void matchCards() {
        if (choseA != null && choseB != null) {
            if (choseA.getSymbol() == choseB.getSymbol()) {
                output.setText("You found a pair!");
                choseA.setFound();
                choseB.setFound();
                found.add(choseA.getSymbol());
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                output.setText("The cards you selected didn't match, sorry.");
                choseA.setFacingDown();
                choseB.setFacingDown();
                lives--;
            }
            choseA = choseB = null;
        }
    }
}