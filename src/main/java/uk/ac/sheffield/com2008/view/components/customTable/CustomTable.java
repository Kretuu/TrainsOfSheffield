package uk.ac.sheffield.com2008.view.components.customTable;

import uk.ac.sheffield.com2008.config.Colors;
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
    private final JPanel mainPanel = new JPanel(new GridBagLayout());
    private Font defaultFont;
    private Border defaultBorder;

    public CustomTable(LinkedList<CustomColumn> columns) {
        this.weights = columns.stream().map(CustomColumn::getWeight).collect(Collectors.toCollection(LinkedList::new));
        this.headers = columns.stream().map(CustomColumn::getColumnName)
                .collect(Collectors.toCollection(LinkedList::new));
        this.columns = columns.size();
        initialiseVariables();
    }

    private void initialiseVariables() {
        setLayout(new FlowLayout(FlowLayout.CENTER));

        Font tableFont = getFont();
        String family = tableFont.getFamily();
        int style = tableFont.getStyle();
        defaultFont = new Font(family, style, 18);

        defaultBorder = BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, Color.BLACK),
                new EmptyBorder(10, 10, 10, 0)
        );

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(constructHeader(), BorderLayout.NORTH);
        panel.add(new JScrollPane(mainPanel), BorderLayout.CENTER);
        mainPanel.setBackground(Colors.TABLE_CONTENT);

        add(panel);
    }

    private JPanel constructHeader() {
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(Colors.TABLE_HEADER);

        int columnIndex = 0;
        GridBagConstraints c = new GridBagConstraints();
        for(String header : headers) {
            c.gridx = columnIndex;
            c.gridy = 0;
            c.weightx = weights.get(columnIndex);
            c.weighty = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.PAGE_START;

            JLabel jLabel = new JLabel(header);
            jLabel.setFont(defaultFont);
            jLabel.setBorder(defaultBorder);
            headerPanel.add(jLabel, c);
            columnIndex++;
        }
        return headerPanel;
    }

    public void populateTable(List<Type> objects, TableMapper<Type> mapper){
        List<LinkedList<Object>> orderColumns = objects.stream().map(mapper::constructColumns).toList();
        mainPanel.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        int rowIndex = 0;
        for(LinkedList<Object> row : orderColumns) {
            if(row.size() != columns) throw new RuntimeException("Size of list is not compatible with this table");
            int columnIndex = 0;
            for(Object column : row) {
                c.gridx = columnIndex;
                c.gridy = rowIndex;
                c.weightx = weights.get(columnIndex);
                c.weighty = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                JComponent cellElement;

                if(column instanceof JComponent component) {
                    cellElement = new JPanel();
                    cellElement.setBackground(Colors.TABLE_CONTENT);
                    component.setPreferredSize(new Dimension(component.getPreferredSize().width, 26));
                    cellElement.setBorder(BorderFactory.createCompoundBorder(
                            new MatteBorder(0, 0, 1, 0, Color.BLACK),
                            new EmptyBorder(4, 10, 4, 0)
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
        preferredDimension.width = 1500;
        mainPanel.setPreferredSize(preferredDimension);
    }
}
