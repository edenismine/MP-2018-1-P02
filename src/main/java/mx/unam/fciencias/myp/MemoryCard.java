package mx.unam.fciencias.myp;

import javax.swing.*;
import java.awt.*;

public class MemoryCard extends JPanel {
    private static final GameProperties PROPERTIES = GameProperties.getInstance();
    private static final Color C_DOWN = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.bg"), 16));
    private static final Color C_UP = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.fg"), 16));
    private final JLabel symbol;
    private final MemoryCardState memoryCardFacingUp;
    private final MemoryCardState memoryCardFacingDown;
    private final MemoryCardState memoryCardFound;
    private MemoryCardState memoryCardState;

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

    MemoryCard(char symbol, int width, int height) {
        this(symbol);
        Dimension dim = new Dimension(width, height);
        setSize(dim);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setMaximumSize(dim);

    }

    public void place(int x, int y) {
        setLocation(x, y);
    }

    public char getSymbol() {
        return symbol.getText().charAt(0);
    }

    boolean choose() {
        return memoryCardState.choose();
    }

    void setFacingUp() {
        memoryCardState.setFaceUp();
    }

    void setFacingDown() {
        memoryCardState.setFaceDown();
    }

    void setFound() {
        memoryCardState.setFound();
    }

    private void setState(MemoryCardState newState) {
        memoryCardState = newState;
        memoryCardState.format();
    }

    private MemoryCardState getUpState() {
        return memoryCardFacingUp;
    }

    private MemoryCardState getDownState() {
        return memoryCardFacingDown;
    }

    private MemoryCardState getFoundState() {
        return memoryCardFound;
    }

    private interface MemoryCardState {
        boolean choose();

        void setFaceUp();

        void setFaceDown();

        void setFound();

        void format();
    }

    private class FacingUpState implements MemoryCardState {

        @Override
        public boolean choose() {
            return false; // can't choose a face up card
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

    private class FacingDownState implements MemoryCardState {
        @Override
        public boolean choose() {
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

