import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ScrollableRadioButtonPanel extends JPanel {
    private JPanel radioPanel;
    private JScrollPane scrollPane;
    private ButtonGroup buttonGroup;

    public int selectedValue;
    private String table;
    private DynamicJFrame parentFrame;
    public ScrollableRadioButtonPanel(String[][] data) {
        setLayout(new BorderLayout());
        // Create the radio panel
        radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

        // Create the scroll pane and add the radio panel to it
        scrollPane = new JScrollPane(radioPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        // Create the button group for radio buttons
        buttonGroup = new ButtonGroup();
        // Add radio buttons to the radio panel
        for (String[] rowData : data) {
                String joinedString = rowData[1] + " - " + rowData[2] + " " + rowData[3] + " - " + rowData[4];
                addRadioButton(joinedString, Integer.parseInt(rowData[0]));
        }
        setBackground(Color.white);
        radioPanel.setBackground(Color.white);
        scrollPane.setBackground(Color.white);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // Add the scroll pane to the panel
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addRadioButton(String text, int cours_id) {
        ActionListener radioButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedValue = cours_id;
            }
        };
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.addActionListener(radioButtonListener);
        buttonGroup.add(radioButton);
        radioPanel.add(radioButton);
    }
}
