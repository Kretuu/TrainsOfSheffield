package uk.ac.sheffield.com2008.view.components;

import uk.ac.sheffield.com2008.config.Colors;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {
    public Panel(LayoutManager layout) {
        super(layout);
        setBackground(Colors.BACKGROUND);
    }

    public Panel() {
        super();
        setBackground(Colors.BACKGROUND);
    }

//    @Override
//    public void setBorder(Border border) {
////        Border newBorder = border;
////        if(newBorder instanceof EmptyBorder) {
////            newBorder = new
////        }
//        super.setBorder(border);
//        super.setBackground(Colors.BACKGROUND);
//    }
}
