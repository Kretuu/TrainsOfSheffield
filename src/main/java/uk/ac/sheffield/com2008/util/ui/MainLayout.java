package uk.ac.sheffield.com2008.util.ui;

import javax.swing.*;
import java.awt.*;

public class MainLayout extends JPanel {
    private JPanel panel = new JPanel();

    public MainLayout() {
        this.setLayout(new BorderLayout());
    }
    public void setContent(JPanel p) {
        remove(panel);
        add(p);
        panel = p;
    }
}
