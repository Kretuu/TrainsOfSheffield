package uk.ac.sheffield.com2008.view;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class FrameManager {

    // Needed for serialisation
    private static final long serialVersionUID = 1L;
    private static JPanel currentView;

    private static JFrame mainFrame;

    public static JFrame getMainFrame() {
        if (mainFrame == null) {
            mainFrame = new JFrame("Trains of Sheffield");
            // Set the frame to fullscreen windowed mode
            mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        }
        return mainFrame;
    }

    public static void setView(JPanel view){
        if(currentView != null){
            mainFrame.remove(currentView);
        }
        currentView = view;
        mainFrame.add(view);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
