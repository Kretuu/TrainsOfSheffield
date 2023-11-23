package uk.ac.sheffield.com2008.view.staff;
import uk.ac.sheffield.com2008.controller.staff.OrderListController;


import javax.swing.*;
import java.awt.*;

public class OrderListView extends StaffView {

    OrderListController orderListController;
    JLabel totalTextLabel;
    public OrderListView(OrderListController orderListController){
        super();
        this.orderListController = orderListController;
        InitializeUI();
    }

    public void onRefresh(){
        removeAll();
        InitializeUI();
        revalidate();
        repaint();

    }

    public void InitializeUI() {
        setLayout(new BorderLayout());

        int padding = 40;

        // top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //Create a label for Manage Orders
        JLabel viewLabel = new JLabel("OrderList");
        // Add the top panel to the top of the frame
        row1.add(viewLabel);
        topPanel.add(row1);
        add(topPanel, BorderLayout.NORTH);

    }
}
