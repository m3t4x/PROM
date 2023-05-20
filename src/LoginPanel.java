import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;

import GUITools.GUITools;
import GUITools.TextPrompt;
import GUITools.DBTools;

import static GUITools.DBTools.CheckCredentials;

public class LoginPanel extends JFrame{
    LoginPanel(){
        getContentPane().setBackground(Color.white);
        setLayout(null);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setSize(500,650);
        setLocationRelativeTo(null);
        AddElements();
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", true);
        setVisible(true);
    }

    public void AddElements()
    {

        JLabel closeButton = GUITools.CreateImage("assets/close.png", 20, 20);
        closeButton.setBounds(470, 10, 20, 20);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                dispose();
            }
        });
        add(closeButton);


        JLabel picsLabel = GUITools.CreateImage("assets/logo.png", 500, 150);
        picsLabel.setBounds(0, 20, 500, 150);
        add(picsLabel);

        JTextField email = new JTextField("");
        email.setBounds(100, 280, 300, 30);
        TextPrompt emailPrompt = new TextPrompt("Adresse Mail", email);
        emailPrompt.setForeground(Color.lightGray);
        email.setBorder(BorderFactory.createMatteBorder(0,0, 1,0,  Color.decode("#db1512")));

        JPasswordField psw = new JPasswordField("");
        TextPrompt pswPrompt = new TextPrompt("Mot de Passe", psw);
        pswPrompt.setForeground(Color.lightGray);
        psw.setBounds(100, 330, 300, 30);
        psw.setBorder(BorderFactory.createMatteBorder(0,0, 1,0,  Color.decode("#db1512")));

        JLabel loginError = new JLabel("Adresse mail/mot de passe erroné !");
        loginError.setHorizontalAlignment(SwingConstants.CENTER);
        loginError.setVerticalAlignment(SwingConstants.CENTER);
        loginError.setBounds(100,370, 300, 30);
        loginError.setOpaque(true);
        loginError.setBackground(Color.white);
        loginError.setForeground(Color.decode("#db1512"));
        loginError.setVisible(false);

        JLabel loginBtn = new JLabel("Connexion");
        loginBtn.setHorizontalAlignment(SwingConstants.CENTER);
        loginBtn.setVerticalAlignment(SwingConstants.CENTER);
        loginBtn.setBounds(150,450, 200, 30);
        loginBtn.setOpaque(true);
        loginBtn.setBackground(Color.decode("#db1512"));
        loginBtn.setForeground(Color.white);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                //
                try {
                    String loginType = CheckLogin(email.getText(), String.valueOf(psw.getPassword()));
                    if(loginType == null)
                    {
                        loginError.setVisible(true);
                    }
                    else
                    {
                        dispose();
                        MainWindow mainWindow = new MainWindow();
                        mainWindow.setVisible(true);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        JLabel copyright = new JLabel("Polytech INTL® 2022-2023");
        copyright.setHorizontalAlignment(SwingConstants.CENTER);
        copyright.setVerticalAlignment(SwingConstants.CENTER);
        copyright.setBounds(150,610, 200, 30);
        copyright.setOpaque(true);
        copyright.setBackground(Color.white);
        copyright.setForeground(Color.black);

        add(email);
        add(psw);
        add(loginBtn);
        add(loginError);
        add(copyright);

    }
    public String CheckLogin(String email, String password) throws SQLException {
        String tableName = null;
        // Check in "utilisateurs" table
        if (CheckCredentials("utilisateurs", email, password)) {
            tableName = "utilisateurs";
        }

        // Check in "etudiants" table
        if (CheckCredentials("etudiants", email, password)) {
            tableName = "etudiants";
        }
        // Check in "enseignants" table
        if (CheckCredentials("enseignants", email, password)) {
            tableName = "enseignants";
        }
        return tableName;
    }
}