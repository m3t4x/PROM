import GUITools.GUITools.*;
import GUITools.DBTools;
import GUITools.TextPrompt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import static GUITools.GUITools.CreateImage;

public class DynamicJFrame extends JFrame {

    private JPanel panel;
    private JTextField[] inputFields;
    private JButton submitButton;

    private Map<String, String> Groupes = new HashMap<>();
    private Map<String, String> Matieres = new HashMap<>();
    private Map<String, String> Ensignants = new HashMap<>();

    private JComboBox groupComboBox;
    private JComboBox ensComboBox;
    private JComboBox matComboBox;

    private ScrollableRadioButtonPanel radioGroupes;
    private ScrollableRadioButtonPanel radioMatieres;
    private ScrollableRadioButtonPanel radioEns;

    private TimeInputWidget duree;
    private DateSelector dateSelect;
    private DelegateTableView parentPanel;
    public DynamicJFrame(String title, String[] paramNames, DelegateTableView tablePanel) throws SQLException {
        int height = 120;
        parentPanel = tablePanel;
        inputFields = new JTextField[paramNames.length];
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", true);
        getContentPane().setBackground(Color.white);
        setLayout(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        if ((paramNames.length*50)+30+100 > 120)
        {
            height = (paramNames.length*50)+30+100;
        }
        JLabel closeButton = CreateImage("assets/close.png", 30, 30);
        closeButton.setBounds(360, 10, 30, 30);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                dispose();
            }
        });

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 22));
        titleLabel.setBounds(0, 10, 360, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel submitButton = new JLabel("Ajouter");
        submitButton.setHorizontalAlignment(SwingConstants.CENTER);
        submitButton.setVerticalAlignment(SwingConstants.CENTER);
        submitButton.setBounds(100,height-40, 200, 30);
        submitButton.setOpaque(true);
        submitButton.setBackground(Color.decode("#db1512"));
        submitButton.setForeground(Color.white);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        int posY = 50;
        switch(title)
        {
            case "Ajouter un cours":
            {
                matComboBox = new JComboBox<>();
                matComboBox.setBounds(40,posY,320,30);
                String[][] matieresData = DBTools.GetAllData("matieres");
                for (String[] rowData : matieresData) {
                    Matieres.put(rowData[1], rowData[0]);
                    matComboBox.addItem(rowData[1]);
                }
                add(matComboBox);
                posY+= 50;
                ensComboBox = new JComboBox<>();
                ensComboBox.setBounds(40,posY,320,30);
                String[][] ensData = DBTools.GetAllData("enseignants");
                for (String[] rowData : ensData) {
                    Ensignants.put(rowData[1]+" "+rowData[2], rowData[0]);
                    ensComboBox.addItem(rowData[1]+" "+rowData[2]);
                }
                add(ensComboBox);
                posY+= 50;
                groupComboBox = new JComboBox<>();
                groupComboBox.setBounds(40,posY,320,30);
                String[][] groupesData = DBTools.GetAllData("groupes");
                for (String[] rowData : groupesData) {
                    Groupes.put(rowData[1], rowData[0]);
                    groupComboBox.addItem(rowData[1]);
                }
                add(groupComboBox);
                break;
            }
            case "Ajouter une seance de cours":
            {
                closeButton.setBounds(760, 10, 30, 30);
                titleLabel.setBounds(0, 10, 760, 30);
                submitButton.setBounds(300,350, 200, 30);

                String[][] groupesData = DBTools.GetCoursData();
                for (String[] rowData : groupesData) {
                    Groupes.put(rowData[1], rowData[0]);
                }
                radioGroupes = new ScrollableRadioButtonPanel(groupesData);
                radioGroupes.setBounds(70,50,400,280);
                radioGroupes.setVisible(true);

                JLabel dureeLabel = new JLabel();
                dureeLabel.setText("Durée de la seance");
                dureeLabel.setBounds(450,120,400,30);
                dureeLabel.setBackground(Color.white);
                dureeLabel.setOpaque(true);
                dureeLabel.setForeground(Color.decode("#db1512"));
                dureeLabel.setVisible(true);
                dureeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dureeLabel.setVerticalAlignment(SwingConstants.CENTER);

                JLabel dateLabel = new JLabel();
                dateLabel.setText("Date de la seance");
                dateLabel.setBounds(450,200,400,30);
                dateLabel.setBackground(Color.white);
                dateLabel.setOpaque(true);
                dateLabel.setForeground(Color.decode("#db1512"));
                dateLabel.setVisible(true);
                dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                dateLabel.setVerticalAlignment(SwingConstants.CENTER);

                duree = new TimeInputWidget();
                duree.setBounds(450, 150, 400, 40);

                dateSelect = new DateSelector();
                dateSelect.setBounds(450, 220, 400, 40);

                add(dateLabel);
                add(dateSelect);
                add(dureeLabel);
                add(duree);
                add(radioGroupes);
                break;
            }
            default:
            {
                for(int i=0; i<paramNames.length;i++)
                {
                    JTextField inputField = new JTextField("");
                    inputField.setBounds(50, posY, 300, 30);
                    TextPrompt inputFieldPrompt = new TextPrompt(paramNames[i], inputField);
                    inputFieldPrompt.setForeground(Color.lightGray);
                    inputField.setBorder(BorderFactory.createMatteBorder(0,0, 1,0,  Color.decode("#db1512")));
                    add(inputField);
                    inputFields[i] = inputField;
                    posY += 50;
                }
            }
        }
        submitButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                switch(title)
                {
                    case "Ajouter un étudiant": {
                        String groupID= Groupes.get(groupComboBox.getSelectedItem());
                        Map<String, String> data = new HashMap();
                        String[] columns = new String[]{"nom", "prenom", "cin", "num_inscription", "email", "mdp", "tel"};
                        int i;
                        for (i = 0; i < columns.length;i++)
                        {
                            data.put(columns[i],inputFields[i].getText());
                        }
                        data.put("id_groupe",groupID);
                        try {
                            DBTools.InsertData("etudiants",data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    case "Ajouter un groupe d'étudiants":
                    {
                        try {
                            DBTools.AddGroup(inputFields[0].getText());
                            break;
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    case "Ajouter un utilisateur": {
                        Map<String, String> data = new HashMap();
                        String[] columns = new String[]{"nom", "prenom", "cin", "fonction", "email", "mdp", "tel"};
                        int i;
                        for (i = 0; i < columns.length;i++)
                        {
                            data.put(columns[i],inputFields[i].getText());
                        }
                        try {
                            DBTools.InsertData("utilisateurs",data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    case "Ajouter un enseignant": {
                        Map<String, String> data = new HashMap();
                        String[] columns = new String[]{"nom", "prenom", "cin", "specialite", "email", "mdp", "tel"};
                        int i;
                        for (i = 0; i < columns.length;i++)
                        {
                            data.put(columns[i],inputFields[i].getText());
                        }
                        try {
                            DBTools.InsertData("enseignants",data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    case "Ajouter une matiére": {
                        String groupID= Groupes.get(groupComboBox.getSelectedItem());
                        Map<String, String> data = new HashMap();
                        String[] columns = new String[]{"nom_matiere", "nb_heures", "coef"};
                        int i;
                        for (i = 0; i < columns.length;i++)
                        {
                            data.put(columns[i],inputFields[i].getText());
                        }
                        data.put("id_groupe",groupID);
                        try {
                            DBTools.InsertData("matieres",data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    case "Ajouter un cours":
                    {
                        String groupID= Groupes.get(groupComboBox.getSelectedItem());
                        String ensID= Ensignants.get(ensComboBox.getSelectedItem());
                        String matID= Matieres.get(matComboBox.getSelectedItem());
                        Map<String, String> data = new HashMap();
                        data.put("id_enseignant",ensID);
                        data.put("id_matiere",matID);
                        data.put("id_groupe",groupID);
                        try {
                            DBTools.InsertData("cours",data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    case "Ajouter une seance de cours":
                    {
                        HashMap<String, Object> data = new HashMap();
                        data.put("id_cours",  radioGroupes.selectedValue);
                        data.put("nb_heures",duree.getSelectedHour());
                        data.put("date",dateSelect.getSelectedDate());
                        try {
                            DBTools.InsertSessionData(data);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    }
                    default:break;
                }
                try {
                    parentPanel.Refresh();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        }
        );
        add(titleLabel);
        add(closeButton);
        add(submitButton);
        switch(title)
        {
            case "Ajouter un étudiant", "Ajouter une matiére":
            {
                groupComboBox = new JComboBox<>();
                groupComboBox.setBounds(40,posY,320,30);
                List<List<String>> groupesData = DBTools.GetGroupes();
                for (List<String> rowData : groupesData) {
                    Groupes.put(rowData.get(1), rowData.get(0));
                    groupComboBox.addItem(rowData.get(1));
                }
                add(groupComboBox);
                break;
            }
        }
        switch(title)
        {
            case "Ajouter une seance de cours":
            {
                setSize(800,400);
                break;
            }
            default:
            {
                setSize(400,height);
                break;
            }
        }
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
/*
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        panel.add(submitButton);

 */