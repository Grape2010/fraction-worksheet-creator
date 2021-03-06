/****************************************************************************
 * Name: Fraction Worksheet Creator
 * Team: Elementary Engineers 
 * Date produced: 04/28/2016
 * ________________________________
 * Purpose of program:
 * The Fraction Worksheet Creator (FWC) is a new stand-alone product 
 * that allows teachers and students to create random exercise worksheets 
 * to practice operations with fractions.The generated worksheets can contain 
 * fraction problems of various difficulty levels, from basic addition and 
 * subtraction problems with visuals and images suitable for small children, 
 * to quite advanced fraction equations. 
 * ****************************************************************************
 */


package com.elementaryengineers.fwc.panel;

import com.elementaryengineers.fwc.custom.ImageButton;
import com.elementaryengineers.fwc.custom.TitleLabel;
import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.model.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**Teachers are registered by the Admin using this page, which is reached by 
 * clicking the Add Teacher button in the menu.
 * Created by sarahakk on 4/25/16.
 */
public class TeacherRegistration extends JPanel {

    private JPanel pnNorth, pnFields, pnSouth;
    private TitleLabel lblTitle;
    private JLabel lblFirst, lblLast, lblUser, lblPass, lblConfirm;
    private JTextField txtFirst, txtLast, txtUser;
    private JPasswordField txtPass, txtConfirm;
    private ImageButton btnSubmit;

    public TeacherRegistration() {
        super(new BorderLayout());
        setBackground(FWCConfigurator.bgColor);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Build title and north panel
        pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnNorth.setBackground(FWCConfigurator.bgColor);
        lblTitle = new TitleLabel("Teacher Registration", 
                FWCConfigurator.TEACHER_REG_IMG);
        pnNorth.add(lblTitle);

        // Build center panel and form
        lblFirst = new JLabel("First name:", SwingConstants.RIGHT);
        lblFirst.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblLast = new JLabel("Last name:", SwingConstants.RIGHT);
        lblLast.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblUser = new JLabel("Username:", SwingConstants.RIGHT);
        lblUser.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblPass = new JLabel("Password:", SwingConstants.RIGHT);
        lblPass.setFont(new Font("Calibri", Font.PLAIN, 18));
        lblConfirm = new JLabel("Confirm password:", SwingConstants.RIGHT);
        lblConfirm.setFont(new Font("Calibri", Font.PLAIN, 18));
        txtFirst = new JTextField(24);
        txtLast = new JTextField(24);
        txtUser = new JTextField(24);
        txtPass = new JPasswordField(24);
        txtConfirm = new JPasswordField(24);

        // Use GridBagLayout
        pnFields = new JPanel(new GridBagLayout());
        pnFields.setBackground(FWCConfigurator.bgColor);
        pnFields.setBorder(BorderFactory.createEmptyBorder(10, 200, 20, 200));
        GridBagConstraints c = new GridBagConstraints();
        c.ipady = 5;

        // First name
        c.anchor = GridBagConstraints.EAST;
        pnFields.add(lblFirst, c);

        c.gridx = 1;
        c.insets = new Insets(0, 10, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnFields.add(txtFirst, c);

        // Last name
        c.gridy = 1;
        c.gridx = 0;
        c.insets.left = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pnFields.add(lblLast, c);

        c.gridx = 1;
        c.insets.left = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnFields.add(txtLast, c);

        // Username
        c.gridy = 2;
        c.gridx = 0;
        c.insets.left = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pnFields.add(lblUser, c);

        c.gridx = 1;
        c.insets.left = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnFields.add(txtUser, c);

        // Password
        c.gridy = 3;
        c.gridx = 0;
        c.insets.left = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pnFields.add(lblPass, c);

        c.gridx = 1;
        c.insets.left = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnFields.add(txtPass, c);

        // Confirm password
        c.gridy = 4;
        c.gridx = 0;
        c.insets.left = 0;
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        pnFields.add(lblConfirm, c);

        c.gridx = 1;
        c.insets.left = 10;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        pnFields.add(txtConfirm, c);

        // Build south panel and submit button
        pnSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnSouth.setBackground(FWCConfigurator.bgColor);
        pnSouth.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        btnSubmit = new ImageButton("Submit",
                FWCConfigurator.SUBMIT_IMG, 150, 50);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (verifyRegistration()) {
                    Teacher newTeacher = getNewTeacher();

                    // If adding new teacher to database works
                    if (FWCConfigurator.getDbConn().createTeacher(newTeacher)){

                        // Add teacher to admin's list of teachers
                       FWCConfigurator.getAdmin().getTeachers().add(newTeacher);

                        // Clear fields in teacher registration form
                        clearFields();

                        JOptionPane.showMessageDialog(null,
                                "Teacher was successfully registered.",
                                "Teacher Registration Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,
                                "Teacher could not be registered "
                                        + "in the database. Please try again.",
                                "Teacher Registration Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        pnSouth.add(btnSubmit);

        add(pnNorth, BorderLayout.NORTH);
        add(pnFields, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);
    }

    private boolean verifyRegistration() {
        if (txtFirst.getText().equals("") || txtLast.getText().equals("") ||
                txtUser.getText().equals("") || String.valueOf(txtPass.
                        getPassword()).equals("") ||
                String.valueOf(txtConfirm.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(null, 
                    "Please enter all required information.", 
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if username is available
     if (!FWCConfigurator.getDbConn().isUsernameAvailable(txtUser.getText())) {
            JOptionPane.showMessageDialog(null, "Username is not available.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check if passwords match
        if (!String.valueOf(txtPass.getPassword()).equals(
                String.valueOf(txtConfirm.getPassword()))) {
            JOptionPane.showMessageDialog(null, "Passwords do not match.", 
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private Teacher getNewTeacher() {
        return new Teacher(txtUser.getText(), txtFirst.getText(), 
                txtLast.getText(),
                String.valueOf(txtPass.getPassword()));
    }

    public void clearFields() {
        txtFirst.setText("");
        txtLast.setText("");
        txtUser.setText("");
        txtPass.setText("");
        txtConfirm.setText("");
    }
}