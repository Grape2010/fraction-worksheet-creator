package com.elementaryengineers.fwc.panel;

import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.model.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 **/
public class FWCMainFrame extends JFrame {

    private CardLayout cardLayout;
    private CommonHeaderPanel header;
    private LoginPanel login;
    private TeacherHome teacherHome;
    private TeacherMenu teacherMenu;
    private JPanel pnCard;

    /**
     *
     **/
    public FWCMainFrame() {
        super("Fraction Worksheet Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildPanels();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildPanels() {
        header = new CommonHeaderPanel();
        header.setSize(1000, 300);

        // Use card layout for central area of frame
        cardLayout = new CardLayout();
        pnCard = new JPanel(cardLayout); // Set card layout for card panel

        login = new LoginPanel();
        login.setPreferredSize(new Dimension(50, 250));
        login.setLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: replace with database username and password verification
                if (login.getUsernameText().equals("shakkoum") &&
                        login.getPasswordText().equals("password")) {
                    login.clearFields();
                    FWCConfigurator.setTeacher(new Teacher(0, "shakkoum", "Sara", "Hakkoum", "salt", "hash",
                            1, 10, 3, 10));
                            //1, 16, 2, 16));
                    buildTeacherPanels();
                    setSize(new Dimension(1000, 700));
                    setLocationRelativeTo(null);
                    cardLayout.show(pnCard, "TeacherHome");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please check your username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add panels to card layout
        pnCard.add(login, "LoginPanel");

        this.add(header, BorderLayout.NORTH); // Add common header panel to top of frame
        this.add(pnCard, BorderLayout.CENTER); // Add card panel to center of frame
    }

    private void buildTeacherPanels() {
        teacherMenu = new TeacherMenu();
        header.setMenu(teacherMenu);
        header.showButtonsLoggedIn();
        header.setLogoutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch back to login page
                header.showButtonsLoggedOut();
                header.hideMenu();
                pnCard.remove(teacherHome);
                FWCConfigurator.setTeacher(null);
                pack();
                setLocationRelativeTo(null);
                cardLayout.show(pnCard, "LoginPanel");
            }
        });

        teacherHome = new TeacherHome();
        pnCard.add(teacherHome, "TeacherHome");
    }
}