package mx.unam.fciencias.myp;

import javax.swing.*;
import java.awt.*;

/**
 * A memory card.
 * <p>
 * Uses the state design pattern to change between states:
 * <ol>
 * <li>A card can be facing up. A card is in this state at the beginning
 * of the game or when a player has selected it. A player can't select
 * a card that's in this state.</li>
 * <li>A card can be facing down. A card is in this state when it
 * hasn't been selected by the player. When in this state, a card can
 * be selected by the player.</li>
 * <li>A card can be matched (found). A card is in this state when the
 * player has matched the card with its pair. If a card has entered
 * this state it will remain facing up, and therefore, the player won't be
 * able to select it.
 * </li>
 * </ol>
 */
public class MemoryCard extends JPanel {
    /**
     * Reference to the game's properties.
     */
    private static final GameProperties PROPERTIES = GameProperties.getInstance();
    /**
     * The color of the back of the card as well as the card's symbol.
     */
    private static final Color C_DOWN = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.bg"), 16));
    /**
     * The color of the front of the card.
     */
    private static final Color C_UP = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.fg"), 16));
    /**
     * The symbol displayed on the front of the card.
     */
    private final JLabel symbol;
    /**
     * Cached state: facing-up.
     */
    private final MemoryCardState memoryCardFacingUp;
    /**
     * Cached state: facing-down.
     */
    private final MemoryCardState memoryCardFacingDown;
    /**
     * Cached state: found.
     */
    private final MemoryCardState memoryCardFound;
    /**
     * The card's current state.
     */
    private MemoryCardState memoryCardState;

    /**
     * Creates a card with no size and initializes the cached states. Sets
     * the card facing up.
     *
     * @param symbol the symbol displayed on the front of the card.
     */
    private MemoryCard(char symbol) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.white, 5));

        setLayout(new GridBagLayout());
        memoryCardFacingUp = new FacingUpState();
        memoryCardFacingDown = new FacingDownState();
        memoryCardFound = new FoundState();

        this.symbol = new JLabel(String.valueOf(symbol));
        this.symbol.setFont(new Font(null, Font.PLAIN, 60));
        this.symbol.setForeground(C_DOWN);
        add(this.symbol);
        setState(getUpState());
    }

    /**
     * Creates a card with fixed dimensions.
     *
     * @param symbol the symbol displayed on the front of the card.
     * @param width  the card's width.
     * @param height the card's height.
     */
    MemoryCard(char symbol, int width, int height) {
        this(symbol);
        Dimension dim = new Dimension(width, height);
        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);

    }

    /**
     * Retrieves the card's symbol.
     *
     * @return the card's symbol.
     */
    public char getSymbol() {
        return symbol.getText().charAt(0);
    }

    /**
     * Context-dependent method. Tries to select the card.
     *
     * @return if the current state allows selection.
     */
    boolean select() {
        return memoryCardState.select();
    }

    /**
     * Context-dependent method. Tries to set the card facing up.
     */
    void setFacingUp() {
        memoryCardState.setFaceUp();
    }

    /**
     * Context-dependent method. Tries to set the card facing down.
     */
    void setFacingDown() {
        memoryCardState.setFaceDown();
    }

    /**
     * Context-dependent method. Tries to set the card to a found state.
     */
    void setFound() {
        memoryCardState.setFound();
    }

    /**
     * Context-dependent method. Tries to set the card to a found state.
     */
    private void setState(MemoryCardState newState) {
        memoryCardState = newState;
        memoryCardState.format();
    }

    /**
     * Retrieves the facing-up context (cached state).
     *
     * @return the facing-up context (cached state).
     */
    private MemoryCardState getUpState() {
        return memoryCardFacingUp;
    }

    /**
     * Retrieves the facing-down context (cached state).
     *
     * @return the facing-down context (cached state).
     */
    private MemoryCardState getDownState() {
        return memoryCardFacingDown;
    }

    /**
     * Retrieves the found context (cached state).
     *
     * @return the found context (cached state).
     */
    private MemoryCardState getFoundState() {
        return memoryCardFound;
    }

    /**
     * A MemoryCardState implements the methods that every card context
     * should be able to call.
     */
    private interface MemoryCardState {
        /**
         * Tries to select the card.
         *
         * @return if the current state allows selection.
         */
        boolean select();

        /**
         * Tries to set the card to a facing-up state.
         */
        void setFaceUp();

        /**
         * Tries to set the card to a facing-down state.
         */
        void setFaceDown();

        /**
         * Tries to set the card to a found state.
         */
        void setFound();
        /**
         * Formats the card according to this state.
         */
        void format();
    }

    /**
     * This is the context of a facing-up card.
     */
    private class FacingUpState implements MemoryCardState {

        @Override
        public boolean select() {
            return false; // can't select a face up card
        }

        @Override
        public void setFaceUp() {
            // Card already up, do nothing.
        }

        @Override
        public void setFaceDown() {
            setState(getDownState());
        }

        @Override
        public void setFound() {
            setState(getFoundState());
        }

        @Override
        public void format() {
            setBackground(C_UP);
        }
    }

    /**
     * This is the context of a facing-down card.
     */
    private class FacingDownState implements MemoryCardState {
        @Override
        public boolean select() {
            setState(getUpState());
            return true;
        }

        @Override
        public void setFaceUp() {
            setState(getUpState());
        }

        @Override
        public void setFaceDown() {
            // card already facing down, do nothing.
        }

        @Override
        public void setFound() {
            setState(getFoundState());
        }

        @Override
        public void format() {
            setBackground(C_DOWN);
        }
    }

    /**
     * This is the context of a card that's been found.
     */
    private class FoundState extends FacingUpState {
        @Override
        public void setFaceDown() {
            // Can't set down a card that's been found.
        }

        @Override
        public void setFound() {
            // card already found, do nothing.
        }
    }
}

