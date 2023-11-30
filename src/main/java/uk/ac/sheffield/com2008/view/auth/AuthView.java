package uk.ac.sheffield.com2008.view.auth;

import uk.ac.sheffield.com2008.view.View;
import uk.ac.sheffield.com2008.view.components.Panel;

import javax.swing.*;
import java.awt.*;

public abstract class AuthView extends View {
    protected JPanel panel;
    public AuthView() {
        setLayout(new GridBagLayout());

        JPanel centerPane = new Panel();
        centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.Y_AXIS));
        centerPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(centerPane);
        this.panel = centerPane;
    }

    @Override
    public void updateNavigation() {

    }
}
