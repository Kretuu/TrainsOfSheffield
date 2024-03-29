package uk.ac.sheffield.com2008.view.components.customTable;

import uk.ac.sheffield.com2008.config.Colors;
import uk.ac.sheffield.com2008.controller.ViewController;
import uk.ac.sheffield.com2008.view.components.Panel;
import uk.ac.sheffield.com2008.view.components.customTable.config.CustomColumn;
import uk.ac.sheffield.com2008.view.components.customTable.mappers.TableMapper;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomTable<Type> extends JPanel {
    private final int columns;
    private final LinkedList<String> headers;
    private final LinkedList<Double> weights;
    private final JFrame frame;
    private JPanel mainPanel = new Panel(new GridBagLayout());
    private Font defaultFont;
    private Border defaultBorder;

    /**
     * Create Custom Table. Type of object used to populate the table needs to be given
     * and corresponding mapper
     *
     * @param columns LinkedList of CustomColumn which includes weight and colum name. Weight
     *                determines how column widths should be weighted.
     */
    public CustomTable(LinkedList<CustomColumn> columns, ViewController controller) {
        this.weights = columns.stream().map(CustomColumn::weight).collect(Collectors.toCollection(LinkedList::new));
        this.headers = columns.stream().map(CustomColumn::columnName)
                .collect(Collectors.toCollection(LinkedList::new));
        this.columns = columns.size();
        this.frame = controller.getNavigation().getFrame();

//        size = getPreferredSize();
        initialiseVariables();
    }

    private void initialiseVariables() {
        setLayout(new BorderLayout());

        Font tableFont = getFont();
        String family = tableFont.getFamily();
        int style = tableFont.getStyle();
        defaultFont = new Font(family, style, 18);

        defaultBorder = BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.BLACK),
                new EmptyBorder(10, 10, 10, 0)
        );

        JPanel panel = new Panel(new BorderLayout());
        panel.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        mainPanel.setBackground(Colors.TABLE_CONTENT);

        add(new JScrollPane(mainPanel));
    }

    private void constructHeader() {
        int columnIndex = 0;
        GridBagConstraints c = new GridBagConstraints();
        for (String header : headers) {
            c.gridx = columnIndex;
            c.gridy = 0;
            c.weightx = weights.get(columnIndex);
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;

            JPanel panel = new Panel(new BorderLayout());
            panel.setBackground(Colors.TABLE_HEADER);
            JLabel jLabel = new JLabel(header);
            jLabel.setFont(defaultFont);
            jLabel.setBorder(defaultBorder);
            panel.add(jLabel);
            mainPanel.add(panel, c);
            columnIndex++;
        }
    }

    /**
     * Function used to populate table with list of objects. Take in mind that this function
     * purges the table and generates it from the very beginning.
     *
     * @param objects List of objects of type defined when initialising the Custom Table
     * @param mapper  Mapper associated with this object Type
     */
    public void populateTable(List<Type> objects, TableMapper<Type> mapper) {
        List<LinkedList<Object>> orderColumns = objects.stream().map(mapper::constructColumns).toList();
        removeAll();
        mainPanel = new Panel(new GridBagLayout());
        mainPanel.setBackground(Colors.TABLE_CONTENT);
        constructHeader();

        GridBagConstraints c = new GridBagConstraints();
        int rowIndex = 1;
        for (LinkedList<Object> row : orderColumns) {
            if (row.size() != columns) throw new RuntimeException("Size of list is not compatible with this table");
            int columnIndex = 0;
            for (Object column : row) {
                c.gridx = columnIndex;
                c.gridy = rowIndex;
                c.weightx = weights.get(columnIndex);
                c.weighty = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                JComponent cellElement;

                if (column instanceof JComponent component) {
                    cellElement = new JPanel();
                    cellElement.setBackground(Colors.TABLE_CONTENT);
                    component.setPreferredSize(new Dimension(component.getPreferredSize().width, 25));
                    component.setMaximumSize(new Dimension(component.getPreferredSize().width, 25));
                    cellElement.setBorder(BorderFactory.createCompoundBorder(
                            new MatteBorder(0, 0, 1, 0, Color.BLACK),
                            new EmptyBorder(4, 10, 3, 0)
                    ));
                    cellElement.add(component);
                } else {
                    cellElement = new JLabel(column.toString());
                    cellElement.setBorder(defaultBorder);
                }
                cellElement.setFont(defaultFont);

                mainPanel.add(cellElement, c);
                columnIndex++;
            }
            rowIndex++;
        }

        Dimension preferredDimension = mainPanel.getPreferredSize();
        preferredDimension.width = frame.getWidth() - 100;
        mainPanel.setPreferredSize(preferredDimension);

        JPanel panel = new Panel(new FlowLayout());
        panel.add(mainPanel);

        JScrollPane scrollPane = new JScrollPane(panel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
//        scrollPane.setPreferredSize(size);
        add(scrollPane);
        revalidate();
        repaint();
    }

    /**
     * This function updates its size based on the frame size and height which is passed
     *
     * @param controller Any ViewController
     * @param height     Height of the table
     */
    public void updateDimension(ViewController controller, int height) {
    }
}
