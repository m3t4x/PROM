import GUITools.DBTools;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DelegateTableView extends JPanel implements ActionListener {

    private static DefaultTableModel model;
    static JTable table;

    private static JButton addButton;
    private static JButton editButton;
    private static JButton deleteButton;

    private static String tableName;

    private static String[][] data;
    private static String myTableName;

    public DelegateTableView(String tableName) throws SQLException {
        myTableName = tableName;
        CreateView();
    }
    public void CreateView() throws SQLException {
        tableName = myTableName;
        String[] columnNames;
        switch (tableName)
        {
            case "cours":
            {
                data = DBTools.GetCours();
                break;
            }
            case "sessions":
                data = DBTools.RetrieveSessionData();
                break;

            default:
                data = DBTools.GetAllData(tableName);
        }
        String[][] shownData = data.clone();
        switch(tableName)
        {
            case "etudiants":
            {
                columnNames = new String[]{"Nom", "Prénom", "CIN", "Numéro d'inscription", "Email", "Mot de passe", "Téléphone", "Groupe"};
                shownData = removeIndexes(data);
                break;
            }
            case "groupes":
            {
                columnNames = new String[]{"Nom du groupe", "Nombre d'étudiants"};
                shownData = removeIndexes(data);
                break;
            }
            case "utilisateurs":
            {
                columnNames = new String[]{"Nom", "Prénom", "CIN", "Fonction", "Email", "Mot de passe", "Téléphone"};
                shownData = removeIndexes(data);
                break;
            }
            case "enseignants":
            {
                columnNames = new String[]{"Nom", "Prénom", "CIN", "Specialite", "Email", "Mot de passe", "Téléphone"};
                shownData = removeIndexes(data);
                break;
            }
            case "matieres":
            {
                columnNames = new String[]{"Nom du matiére", "Nombre d'heures", "Coefficient", "Groupe"};
                shownData = removeIndexes(data);
                break;
            }
            case "cours":
            {
                columnNames = new String[]{"Nom du matiére", "Enseignant", "Nombre d'heures a atteindre", "Groupe"};
                shownData = removeIndexes(data);
                break;
            }
            case "sessions":
            {
                columnNames = new String[]{"Nom du matiére", "Enseignant","Groupe","Durée du séance","Date"};
                shownData = removeIndexes(data);
                break;
            }
            default:
                columnNames = DBTools.GetColumnNames(tableName).toArray(new String[0]);
        }
        model = new DefaultTableModel(shownData, columnNames);
        table = new JTable(model);
        for (int c = 0; c < table.getColumnCount(); c++)
        {
            Class<?> col_class = table.getColumnClass(c);
            table.setDefaultEditor(col_class, null);        // remove editor
        }
        addButton = new JButton("Ajouter");
        addButton.setBackground(Color.red);
        addButton.setForeground(Color.white);
        addButton.setOpaque(true);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.addMouseListener(new MouseAdapter() {
                                       public void mouseClicked(MouseEvent e) {
                                           try{
                                               addRow();
                                           }
                                           catch (SQLException ex){
                                               throw new RuntimeException(ex);
                                           }

                                       }
                                   }
                                   );
        editButton = new JButton("Modifier");
        editButton.setBackground(Color.red);
        editButton.setForeground(Color.white);
        editButton.setFocusPainted(false);
        editButton.setOpaque(true);
        editButton.setBorderPainted(false);

        deleteButton = new JButton("Supprimer");
        deleteButton.setBackground(Color.red);
        deleteButton.setFocusPainted(false);
        deleteButton.setForeground(Color.white);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    deleteRow();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        addButton.addActionListener(this);
        editButton.addActionListener(this);

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
        table.setShowGrid(false);
        table.setBounds(0, 120, 1420, 600);
        setBounds(0, 120, 1420, 600);

        final DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBorder(null);
        headerRenderer.setBackground(Color.red);
        headerRenderer.setForeground(Color.white);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setVerticalAlignment(JLabel.CENTER);
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(headerRenderer);

        final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        cellRenderer.setVerticalAlignment(JLabel.CENTER);
        for(int x = 0; x<columnNames.length;x++) {
            table.getColumnModel().getColumn(x).setCellRenderer(cellRenderer);
        }
    }
    public void Refresh() throws SQLException {
        remove(table);
        revalidate();
        repaint();
        CreateView();
        revalidate();
        repaint();
    }

    public static String[][] removeIndexes(String[][] array) throws SQLException {
        String[][] newArray = new String[array.length][];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                List<String> list = new ArrayList<>(Arrays.asList(array[i]));
                newArray[i] = list.toArray(new String[0]);
                switch (tableName) {
                    case "etudiants","matieres":{
                        String groupID = newArray[i][newArray[i].length - 1];
                        newArray[i][newArray[i].length - 1] = DBTools.GetGroupName(Integer.parseInt(groupID));
                        newArray[i] = Arrays.copyOfRange(newArray[i],1,newArray[i].length);
                        break;
                    }
                    case "groupes": {
                        int count = DBTools.GetGroupCount(Integer.parseInt(array[i][0]));
                        newArray[i][0] = newArray[i][1];
                        newArray[i][1] = String.valueOf(count);
                        break;
                    }
                    case "utilisateurs", "enseignants":
                    {
                        newArray[i] = Arrays.copyOfRange(newArray[i],1,newArray[i].length);
                        break;
                    }
                    case "sessions", "cours":
                        newArray[i] = Arrays.copyOfRange(newArray[i],1,newArray[i].length);
                        break;

                }
            }
        }
        return newArray;
    }

    private void addRow() throws SQLException {
        switch (tableName)
        {
            case "etudiants":
            {
                String[] paramNames = {"Nom", "Prénom", "CIN", "Numéro d'inscription", "Email", "Mot de passe", "Téléphone"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter un étudiant", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
            case "groupes":
            {
                String[] paramNames = {"Nom de Groupe"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter un groupe d'étudiants", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
            case "utilisateurs":
            {
                String[] paramNames = {"Nom", "Prénom", "CIN", "Fonction", "Email", "Mot de passe", "Téléphone"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter un utilisateur", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
            case "enseignants":
            {
                String[] paramNames = {"Nom", "Prénom", "CIN", "Specilité", "Email", "Mot de passe", "Téléphone"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter un enseignant", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
            case "matieres":
            {
                String[] paramNames = {"Nom du matiére", "Nombre d'heures", "Coefficient"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter une matiére", paramNames, this);
                myFrame.setVisible(true);

                break;
            }
            case "cours":
            {
                String[] paramNames = {"Nom du matiére", "Nombre d'heures", "Coefficient"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter un cours", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
            case "sessions":
            {
                String[] paramNames = {"Nom du matiére", "Nombre d'heures", "Coefficient"};
                DynamicJFrame myFrame = new DynamicJFrame("Ajouter une seance de cours", paramNames, this);
                myFrame.setVisible(true);
                break;
            }
        }
    }

    private void editRow() {
        int selectedRow = table.getSelectedRow();
    }

    private void deleteRow() throws SQLException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            DBTools.DeleteRow(tableName, Integer.parseInt(data[selectedRow][0]));
            model.fireTableDataChanged();
            revalidate();
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}