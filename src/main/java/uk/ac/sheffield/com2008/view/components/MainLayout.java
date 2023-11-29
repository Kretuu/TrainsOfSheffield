package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.cache.AppSessionCache;
import uk.ac.sheffield.com2008.navigation.Navigation;
import uk.ac.sheffield.com2008.navigation.NavigationManager;
import uk.ac.sheffield.com2008.view.View;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Type;

public class MainLayout extends JPanel {
    private final JPanel panel = new JPanel(new BorderLayout());
    private JPanel view = new JPanel();
    private JButton logoutButton;
    private final JPanel navigation = new JPanel();
    private final NavigationManager navigationManager;

    public MainLayout(NavigationManager navigationManager) {
        this.setLayout(new BorderLayout());
        this.navigationManager = navigationManager;
        createLayout();
    }
    public void setContent(View v) {
        updateLayout(v);
        panel.remove(view);
        panel.add(v);
        this.view = v;
    }

    public void purgeContent() {
        panel.remove(view);
        this.view = new JPanel();
    }

    private void createLayout() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome to Trains Of Sheffield shop ");
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        navigation.setLayout(new BoxLayout(navigation, BoxLayout.X_AXIS));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            AppSessionCache.getInstance().logoutUser();
            navigationManager.navigate(Navigation.LOGIN);
        });
        topPanel.add(navigation, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void updateLayout(View v) {
        Type oldViewType = view.getClass().getGenericSuperclass();
        Type newViewType = v.getClass().getGenericSuperclass();

        if(!newViewType.equals(oldViewType)) {
            setNavigationBar(v);
        }
    }

    public void setNavigationBar(View v) {
        navigation.removeAll();
        v.getNavigation().forEach((key, value) -> {
            value.addActionListener(e -> navigationManager.navigate(key));
            navigation.add(value);
            navigation.add(Box.createRigidArea(new Dimension(20, 0)));
        });
        navigation.add(logoutButton);
    }

    public void updateMessage(String header, String text, boolean isError) {
        if(isError){
            JOptionPane.showMessageDialog(this, text, header, JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, text, header, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
