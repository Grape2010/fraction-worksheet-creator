package com.elementaryengineers.fwc.panel;

import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.db.FWCDatabaseConnection;
import com.elementaryengineers.fwc.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * TODO
 **/
public class FWCMainFrame extends JFrame {

    private FWCDatabaseConnection dbConn;
    private CardLayout cardLayout;
    private JPanel pnCard;
    private CommonHeader header;
    private Account myAccount;
    private Login login;
    private ForgotPassword forgotPass;
    private AdminResetPassword adminResetPass;
    private AdminRegistration adminReg;
    private AllTutorials tutorials; // Shared by teachers and students

    // Admin panels
    private AdminMenu adminMenu;
    private AdminHome adminHome;
    private AdminManagePasswords adminPasswords;
    private TeacherProfile adminTeacherProfile;
    private TeacherRegistration adminTeacherReg;

    // Teacher panels
    private TeacherMenu teacherMenu;
    private TeacherHome teacherHome;
    private TeacherHistory teacherHistory;
    private StudentRegistration studentReg;

    // Student panels
    private StudentMenu studentMenu;
    private StudentHome studentHome;
    private StudentHistory studentHistory;

    // Dimensions of different panels to be used when switching between them
    private static final int loginW = 534, loginH = 487,
            adminRegW = 900, adminRegH = 629,
            adminForgotW = 850, adminForgotH = 579,
            standardW = 1000, standardH = 800;

    /**
     * Frame constructor
     **/
    public FWCMainFrame() {
        super("Fraction Worksheet Creator");

        dbConn = FWCConfigurator.connectToDatabase();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        buildPanels();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildPanels() {
        header = new CommonHeader();
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        header.setLogoutListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch back to login page
                header.showButtonsLoggedOut();
                header.hideMenu();
                cardLayout.show(pnCard, "Login");
                setSize(new Dimension(loginW, loginH));
                setLocationRelativeTo(null);
                FWCConfigurator.setCurrentPage(Page.LOGIN);
                FWCConfigurator.logout();
            }
        });
             
        header.setAccountListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FWCConfigurator.getCurrentPage() != Page.MY_ACCOUNT) {
                    if (myAccount == null) { // Create panel if first time
                        myAccount = new Account();
                        pnCard.add(myAccount, "Account");
                    }

                    cardLayout.show(pnCard, "Account");
                    FWCConfigurator.setCurrentPage(Page.MY_ACCOUNT);
                }
            }
        });

        // Use card layout for central area of frame
        cardLayout = new CardLayout();
        pnCard = new JPanel(cardLayout); // Set card layout for card panel
        pnCard.setBackground(Color.WHITE);
        pnCard.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(5, 0, 0, 0, Color.BLACK),
                new EmptyBorder(10, 0, 0, 0))
        );

        login = new Login();
        login.setLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameText = login.getUsernameText(),
                        passwordText = login.getPasswordText();

                if (usernameText.equals("") || passwordText.equals("")) {
                    JOptionPane.showMessageDialog(null, 
                            "Please check your username or password.",
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = dbConn.getUser(usernameText);

                if (user != null) { // If user exists
                    if (user.verifyLogin(passwordText)) {
                        // Build panels specific to the user that logged in
                        switch (user.getType()) {
                            case TEACHER: {
                                FWCConfigurator.setTeacher((Teacher) user);
                                buildTeacherPanels();
                                cardLayout.show(pnCard, "TeacherHome");
                                FWCConfigurator
                                        .setCurrentPage(Page.TEACHER_HOME);
                                break;
                            }

                            case STUDENT: {
                                FWCConfigurator.setStudent((Student) user);
                                buildStudentPanels();
                                cardLayout.show(pnCard, "StudentHome");
                                FWCConfigurator
                                        .setCurrentPage(Page.STUDENT_HOME);
                                break;
                            }

                            case ADMIN: {
                                FWCConfigurator.setAdmin((Admin) user);
                                buildAdminPanels();
                                cardLayout.show(pnCard, "AdminHome");
                                FWCConfigurator.setCurrentPage(Page.ADMIN_HOME);
                                break;
                            }
                        }

                        // Reset components to display home page
                        header.showButtonsLoggedIn();
                        setSize(new Dimension(standardW, standardH));
                        setLocationRelativeTo(null);
                        login.clearFields();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, 
                                "Please check your password and try again.", 
                                "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, 
                            "Please check your username and try again.", 
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        login.setForgotPassListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create forgot password panel if first time clicking it
                if (forgotPass == null) {
                    forgotPass = new ForgotPassword();
                    forgotPass.setBackListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cardLayout.show(pnCard, "Login");
                            FWCConfigurator.setCurrentPage(Page.LOGIN);
                            forgotPass.clearFields();
                        }
                    });

                    forgotPass.setSubmitListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String usernameText = forgotPass.getUsernameText();

                            if (usernameText.equals("")) {
                                JOptionPane.showMessageDialog(null, 
                                        "Please check your username.", 
                                        "Username Error",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            User user = dbConn.getUser(usernameText);

                            if (user != null) { // If user exists
                                switch (user.getType()) {
                                    case TEACHER: {
                                        // Set reset password requested to true
                                        Teacher teacher = (Teacher) user;
                                        teacher.setResetPassRequested(true);

                                        // Check database update status
                                        if (dbConn.updateTeacher(teacher)) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Password reset request " +
                                                            "was sent to the" +
                                                            " administrator.",
                                                    "Request Sent",
                                                    JOptionPane.PLAIN_MESSAGE);
                                        }
                                        else {
                                            JOptionPane.showMessageDialog(null,
                                                    "Password reset request " +
                                                            "could not be " +
                                                            "sent to the " +
                                                            "administrator.",
                                                    "Request Failed",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                        break;
                                    }

                                    case STUDENT: {
                                        // Set reset password requested to true
                                        Student student = (Student) user;
                                        student.setResetPassRequested(true);

                                        // Check database update status
                                        if (dbConn.updateStudent(student)) {
                                            JOptionPane.showMessageDialog(null,
                                                     "Password reset request " +
                                                             "was sent to " +
                                                             "your teacher.",
                                                     "Request Sent",
                                                    JOptionPane.PLAIN_MESSAGE);
                                        }
                                        else {
                                            JOptionPane.showMessageDialog(null,
                                                    "Password reset request " +
                                                            "could not be " +
                                                            "sent to your " +
                                                            "teacher.",
                                                    "Request Failed",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }

                                        break;
                                    }

                                    case ADMIN: {
                                        Admin admin = (Admin) user;

                                        // Create panel if first time
                                        if (adminResetPass == null) {
                                            adminResetPass = new AdminResetPassword();
                                            adminResetPass.setBackListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    cardLayout.show(pnCard, "Login");
                                                    setSize(new Dimension(loginW, loginH));
                                                    setLocationRelativeTo(null);
                                                    FWCConfigurator.setCurrentPage(Page.LOGIN);
                                                }
                                            });

                                            adminResetPass.setSubmitListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    // Go back to login panel if password reset was successful
                                                    if (adminResetPass.verifyAndPerformReset()) {
                                                        cardLayout.show(pnCard, "Login");
                                                        setSize(new Dimension(loginW, loginH));
                                                        setLocationRelativeTo(null);
                                                        FWCConfigurator.setCurrentPage(Page.LOGIN);
                                                    }
                                                }
                                            });

                                            pnCard.add(adminResetPass,
                                                    "AdminResetPassword");
                                        }

                                        adminResetPass.setAdmin(admin);

                                        // Switch to admin reset password panel
                                        cardLayout.show(pnCard,
                                                "AdminResetPassword");
                                        setSize(new Dimension(adminForgotW,
                                                adminForgotH));
                                        setLocationRelativeTo(null);
                                        FWCConfigurator.setCurrentPage(
                                                Page.ADMIN_RESET_PASSWORD);
                                        // Reset forgot password username field
                                        forgotPass.clearFields();
                                        break;
                                    }
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(null,
                                        "Username does not exist.",
                                        "Login Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                    pnCard.add(forgotPass, "ForgotPassword");
                }

                // Switch to forgot password panel
                cardLayout.show(pnCard, "ForgotPassword");
                FWCConfigurator.setCurrentPage(Page.FORGOT_PASSWORD);
                login.clearFields();
            }
        });
        
        // Add login panel to card layout
        pnCard.add(login, "Login");
        setSize(new Dimension(loginW, loginH));

        // If not admin exists in the system, show admin registration panel
        if (!dbConn.doesAdminExist()) {
            adminReg = new AdminRegistration();
            adminReg.setSubmitListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (adminReg.verifyRegistration()) {
                        Admin newAdmin = adminReg.getNewAdmin();

                        // If adding new admin to database works
                        if (dbConn.createAdmin(newAdmin)) {
                            JOptionPane.showMessageDialog(null, 
                                    
                                   "Administrator was successfully registered.",
                                   "Administrator Registration Successful",
                                    JOptionPane.PLAIN_MESSAGE);

                            // Switch to login page
                            cardLayout.show(pnCard, "Login");
                            setSize(new Dimension(loginW, loginH));
                            setLocationRelativeTo(null);
                            FWCConfigurator.setCurrentPage(Page.LOGIN);
                        }
                        else {
                            JOptionPane.showMessageDialog(null,
                                    "Administrator could not be registered "
                                       + "in the database. Please try again.",
                                    "Administrator Registration Failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            pnCard.add(adminReg, "AdminRegistration");
            cardLayout.show(pnCard, "AdminRegistration");
            setSize(new Dimension(adminRegW, adminRegH));
        }

        // Add common header panel to top of frame
        this.add(header, BorderLayout.NORTH);
        // Add card panel to center of frame
        this.add(pnCard, BorderLayout.CENTER);
    }

    private void buildTeacherPanels() {
        if (teacherMenu == null) { // Create panels if first time
            teacherMenu = new TeacherMenu();
            teacherMenu.setHistoryListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            // Add other action listeners

          
            teacherHome = new TeacherHome();
            pnCard.add(teacherHome, "TeacherHome");

            // Add other teacher panels
        }

        // Needs to be set each time a teacher logs in
        header.setMenu(teacherMenu);
    }

    private void buildStudentPanels() {
        if (studentMenu == null) { // Create panels if first time
            studentMenu = new StudentMenu();
            studentMenu.setHomeListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on home page
                    if (FWCConfigurator.getCurrentPage() != Page.STUDENT_HOME) {
                        cardLayout.show(pnCard, "StudentHome");
                        FWCConfigurator.setCurrentPage(Page.STUDENT_HOME);
                    }
                }
            });

            studentMenu.setTutorialsListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on tutorials page
                    if (FWCConfigurator.getCurrentPage() != Page.TUTORIALS) {
                        cardLayout.show(pnCard, "AllTutorials");
                        FWCConfigurator.setCurrentPage(Page.TUTORIALS);
                    }
                }
            });

            studentMenu.setHistoryListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on history page
                    if (FWCConfigurator.getCurrentPage() !=
                            Page.STUDENT_HISTORY) {
                        cardLayout.show(pnCard, "StudentHistory");
                        FWCConfigurator.setCurrentPage(Page.STUDENT_HISTORY);
                    }
                }
            });

            studentHome = new StudentHome();
            studentHistory = new StudentHistory();

            if (tutorials == null) { // Tutorials panel needs to be created
                tutorials = new AllTutorials();
            }

            pnCard.add(studentHome, "StudentHome");
            pnCard.add(studentHistory, "StudentHistory");
            pnCard.add(tutorials, "AllTutorials");
        }
        else { // Refresh/reset panels for new student session
            studentHistory.refresh();
        }

        // Needs to be set each time a student logs in
        header.setMenu(studentMenu);
    }

    private void buildAdminPanels() {
        if (adminMenu == null) { // Create panels if first time
            adminMenu = new AdminMenu();
            adminMenu.setHomeListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on admin home page
                    if (FWCConfigurator.getCurrentPage() != Page.ADMIN_HOME) {
                        cardLayout.show(pnCard, "AdminHome");
                        FWCConfigurator.setCurrentPage(Page.ADMIN_HOME);
                    }
                }
            });

            adminMenu.setPasswordResetListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on the page
                    if (FWCConfigurator.getCurrentPage() !=
                            Page.ADMIN_MANAGE_PASSWORDS) {
                        cardLayout.show(pnCard, "AdminManagePasswords");
                        FWCConfigurator
                                .setCurrentPage(Page.ADMIN_MANAGE_PASSWORDS);
                    }
                }
            });

            adminMenu.setAddTeacherListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // If not already on the page
                    if (FWCConfigurator.getCurrentPage() !=
                            Page.TEACHER_REGISTRATION) {
                        cardLayout.show(pnCard, "TeacherRegistration");
                        FWCConfigurator
                                .setCurrentPage(Page.TEACHER_REGISTRATION);
                    }
                }
            });

            adminHome = new AdminHome();
            adminHome.setProfileListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = adminHome.getSelectedTeacher();

                    // Check if selected a teacher
                    if (index > 0) {
                        adminTeacherProfile.populateFields(FWCConfigurator.
                        getAdmin().getTeachers().get(index));
                        adminTeacherProfile.setTeacherIndex(index);
                        cardLayout.show(pnCard, "TeacherProfile");
                        FWCConfigurator.setCurrentPage(Page.TEACHER_PROFILE);
                    }
                    else { // No teacher is selected from the table
                        JOptionPane.showMessageDialog(null,
                        "Please select a teacher from the table first.",
                        "View Profile", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            adminPasswords = new AdminManagePasswords();
            adminTeacherProfile = new TeacherProfile();
            adminTeacherProfile.setDeleteListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this teacher?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        int index = adminTeacherProfile.getTeacherIndex();
                        Teacher teacher = FWCConfigurator.getAdmin()
                                .getTeachers().get(index);

                        // Check database update status
                        if (dbConn.deleteTeacher(teacher)) {
                            JOptionPane.showMessageDialog(null,
                            "Teacher was successfully deleted.",
                            "Teacher Delete Successful",
                            JOptionPane.PLAIN_MESSAGE);

                            // Remove from list of teachers
                            FWCConfigurator.getAdmin().getTeachers()
                                    .remove(index);
                            // Refresh list of teachers on home page
                            adminHome.refresh();
                            // Go back to home page
                            cardLayout.show(pnCard, "AdminHome");
                            FWCConfigurator.setCurrentPage(Page.ADMIN_HOME);
                        }
                        else {
                            JOptionPane.showMessageDialog(null,
                            "Teacher could not be deleted from the database.",
                            "Teacher Delete Failed",
                            JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

            adminTeacherReg = new TeacherRegistration();

            pnCard.add(adminHome, "AdminHome");
            pnCard.add(adminPasswords, "AdminManagePasswords");
            pnCard.add(adminTeacherProfile, "TeacherProfile");
            pnCard.add(adminTeacherReg, "TeacherRegistration");
        }
        else { // Refresh/reset panels for new admin session
            adminHome.refresh();
            adminPasswords.refresh();
        }

        // Needs to be set each time an admin logs in
        header.setMenu(adminMenu);
    }
}