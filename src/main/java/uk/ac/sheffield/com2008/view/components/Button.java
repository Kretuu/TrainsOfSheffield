package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.config.Colors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {
    private boolean isFocused = false;
    private Color background = Colors.BUTTON_CONTENT;

    public Button(String content, int height) {

        setContentAreaFilled(false);
        setBorderPainted(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isEnabled()) return;
                setBackground(Colors.BUTTON_CONTENT_CLICK);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!isEnabled()) return;
                if (isFocused) {
                    setBackground(Colors.BUTTON_CONTENT_FOCUS);
                } else {
                    setBackground(Colors.BUTTON_CONTENT);
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isEnabled()) return;
                isFocused = true;
                setBackground(Colors.BUTTON_CONTENT_FOCUS);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isEnabled()) return;
                isFocused = false;
                setBackground(Colors.BUTTON_CONTENT);
            }
        });
        addPropertyChangeListener("enabled", evt -> {
            if (evt.getNewValue() instanceof Boolean b) {
                if (b) {
                    setBackground(Colors.BUTTON_CONTENT);
                } else {
                    setBackground(Colors.BUTTON_DISABLED);
                }
            }
        });
        setForeground(Colors.BUTTON_TEXT);
        setText(content);

        setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        revalidate();
        repaint();
    }

    public Button(String text) {
        this(text, 30);
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Colors.BUTTON_BORDER);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.setColor(background);
        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
        super.paintComponent(graphics);
    }
}
