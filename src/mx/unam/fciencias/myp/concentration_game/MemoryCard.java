package mx.unam.fciencias.myp.concentration_game;

import javax.swing.*;
import java.awt.*;

public class MemoryCard extends JPanel {
    private static final PreLoadedProperties PROPERTIES = PreLoadedProperties.getInstance();
    private static final Color C_DOWN = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.bg"), 16));
    private static final Color C_UP = new Color(Integer.parseInt(PROPERTIES.getProperty("card.color.fg"), 16));
    private final JLabel symbol;
    private MemoryCardState context;

    public MemoryCard(char symbol) {
        super();
        this.symbol = new JLabel(String.valueOf(symbol));
        this.setState(getDownState());
    }

    public MemoryCard(char symbol, int x, int y, int width, int height) {
        this(symbol);
        this.setPreferredSize(new Dimension(width, height));
        this.setLocation(x, y);
    }

    public MemoryCard(char symbol, int width, int height) {
        this(symbol);
        this.setPreferredSize(new Dimension(width, height));
    }

    public char getSymbol(){
        return this.symbol.getText().charAt(0);
    }

    boolean choose() {
        return this.context.choose();
    }

    void setFacingUp() {
        this.context.setFaceUp();
    }

    void setFacingDown() {
        this.context.setFaceDown();
    }

    void setFound() {
        this.context.setFound();
    }

    private void setState(MemoryCardState newState) {
        this.context = newState;
        this.context.format();
    }

    private MemoryCardState getUpState() {
        return new FacingUpState();
    }

    private MemoryCardState getDownState() {
        return new FacingDownState();
    }

    private MemoryCardState getFoundState() {
        return new FoundState();
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
            symbol.setVisible(true);
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
            symbol.setVisible(false);
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

