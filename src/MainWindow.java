import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import GUITools.GUITools;

public class MainWindow extends JFrame {
    JPanel homePanel = new JPanel();
    JLabel wlcMessage;
    JLabel wlcUser;
    String[] sectionsList =  {
            "Gestion des etudiants", "Gestion des utilisateurs", "Gestion des enseignants", "Gestion des séances" ,"Gestion des groupes", "Gestion des matiéres", "Gestion des cours",
            "Gestion des notes/résultats", "Gestion des absences"
    };
    String[] sectionsIcons =  {
            "assets/student.png",
            "assets/users.png",
            "assets/teacher.png",
            "assets/session.png",
            "assets/users.png",
            "assets/courses.png",
            "assets/book.png",
            "assets/notes.png",
            "assets/missing.png",

    };
    JPanel centerPanel;
    JLabel backButton;
    MainWindow() {
        UIManager.put("RadioButton.select", Color.RED);
        getContentPane().setBackground(Color.white);
        setLayout(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setSize(1420, 768);
        setLocationRelativeTo(null);
        ShowHome();
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", true);
        setVisible(true);
    }

    public void ShowHome()
    {
        homePanel.setBounds(0, 100, 1420, 680);
        homePanel.setOpaque(true);
        homePanel.setBackground(Color.white);
        homePanel.setLayout(null);
        AddMainElements();
        AddAppSections();
        add(homePanel);
        homePanel.setVisible(true);
    }
    public void AddMainElements() {
        JLabel closeButton = GUITools.CreateImage("assets/close.png", 30, 30);
        closeButton.setBounds(1380, 10, 30, 30);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                dispose();
            }
        });

        backButton = GUITools.CreateImage("assets/back.png", 30, 30);
        backButton.setBounds(15, 10, 30, 30);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setVisible(false);
        backButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                centerPanel.setVisible(false);
                homePanel.setVisible(true);
                backButton.setVisible(false);
                wlcUser.setBounds(630, 50, 300, 50);
                wlcMessage.setBounds(65, 50, 1000, 50);
                wlcMessage.setText("Bienvenue");
                wlcUser.setText("Dsouli Youssef");
                add(wlcMessage);
            }
        });


        wlcMessage = new JLabel("Bienvenue");
        wlcMessage.setFont(new Font("Arial", Font.PLAIN, 36));
        wlcMessage.setBounds(65, 50, 1000, 50);
        wlcMessage.setHorizontalAlignment(SwingConstants.CENTER);
        wlcMessage.setVerticalAlignment(SwingConstants.CENTER);

        wlcUser = new JLabel("Dsouli Youssef");
        wlcUser.setFont(new Font("Arial", Font.PLAIN, 36));
        wlcUser.setBounds(630, 50, 300, 50);
        wlcUser.setHorizontalAlignment(SwingConstants.CENTER);
        wlcUser.setForeground(Color.decode("#db1512"));
        wlcUser.setVerticalAlignment(SwingConstants.CENTER);

        JLabel chooseMessage = new JLabel("Que voulez-vous faire en premier ?");
        chooseMessage.setFont(new Font("Arial", Font.PLAIN, 38));
        chooseMessage.setBounds(30, 140, 1366, 50);
        chooseMessage.setHorizontalAlignment(SwingConstants.CENTER);
        chooseMessage.setVerticalAlignment(SwingConstants.CENTER);

        JLabel copyright = new JLabel("Polytech INTL® 2022-2023");
        copyright.setHorizontalAlignment(SwingConstants.CENTER);
        copyright.setVerticalAlignment(SwingConstants.CENTER);
        copyright.setBounds(600,730, 200, 30);
        copyright.setOpaque(true);
        copyright.setBackground(Color.white);
        copyright.setForeground(Color.black);

        add(wlcMessage);
        add(wlcUser);
        homePanel.add(chooseMessage);
        add(copyright);
        add(closeButton);
        add(backButton);

    }
    public void AddAppSections() {
        int posX = 200;
        int posY = 280;
        for(int i=0;i<4;i++) {
            JLabel section = CreateSection(i);
            section.setBounds(posX, posY, 250, 70);
            homePanel.add(section);
            posX = posX + 260;
        }
        posY += 100;
        posX = 80;
        for(int i=4;i<9;i++) {
            JLabel section = CreateSection(i);
            section.setBounds(posX, posY, 250, 70);
            homePanel.add(section);
            posX = posX + 260;
        }
    }
    public JLabel CreateSection(int index)
    {
        JLabel section = new JLabel(sectionsList[index]);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(sectionsIcons[index]).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        section.setIcon(imageIcon);
        section.setFont(new Font("Arial", Font.PLAIN, 16));
        section.setOpaque(true);
        section.setBackground(Color.decode("#db1512"));
        section.setForeground(Color.white);
        section.setHorizontalAlignment(SwingConstants.CENTER);
        section.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        section.setVerticalAlignment(SwingConstants.CENTER);
        section.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                String tableName = null;
                switch(index)
                {
                    case 0: tableName = "etudiants";break;
                    case 1: tableName = "utilisateurs";break;
                    case 2: tableName = "enseignants";break;
                    case 3: tableName = "sessions";break;
                    case 4: tableName = "groupes";break;
                    case 5: tableName = "matieres";break;
                    case 6: tableName = "cours";break;
                    case 7: tableName = "abscence";break;
                    default: tableName = "etudiants";break;
                }
                try {
                    centerPanel = new DelegateTableView(tableName);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                homePanel.setVisible(false);
                backButton.setVisible(true);
                String titleString = sectionsList[index].split(" ")[2];
                String upperTitleString = titleString.substring(0, 1).toUpperCase() + titleString.substring(1);;
                wlcUser.setText(upperTitleString);
                wlcUser.setBounds(334, 50, 768, 50);
                remove(wlcMessage);
                revalidate();
                repaint();
                centerPanel.setVisible(true);
                add(centerPanel);
            }
        }
        );
        return section;
    }
}