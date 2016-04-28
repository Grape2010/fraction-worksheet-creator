package com.elementaryengineers.fwc.panel;

import com.elementaryengineers.fwc.custom.ImageButton;
import com.elementaryengineers.fwc.custom.TitleLabel;
import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by sarahakk on 4/27/16.
 */
public class TeacherManagePasswords extends JPanel {

    private JPanel pnNorth, pnCenter, pnButtons;
    private TitleLabel lblTitle;
    private ImageButton btnReset, btnResetAll;
    private DefaultTableModel tableModel;
    private JTable studentsTable;
    private JScrollPane tableScroll;
    private ArrayList<Student> students;

    public TeacherManagePasswords() {
        super(new BorderLayout());
        setBackground(Color.WHITE);

        // Build title and north panel
        pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnNorth.setBackground(Color.WHITE);
        lblTitle = new TitleLabel("Reset Passwords", FWCConfigurator.RESET_PASSW_TITLE_IMG);
        pnNorth.add(lblTitle);

        // Build center panel
        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(Color.WHITE);

        // Build table of students
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"First Name", " Last " +
                "Name", "Username", "Class"});

        studentsTable = new JTable(tableModel);
        studentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentsTable.setFillsViewportHeight(true);
        studentsTable.getTableHeader().setFont(new Font("Calibri", Font.PLAIN, 18));

        // Populate the table only with students that need a password reset
        students = FWCConfigurator.getTeacher().getStudentsRequestedReset();
        populateTable();

        tableScroll = new JScrollPane(studentsTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        pnCenter.add(tableScroll, BorderLayout.CENTER);

        // Build south panel
        pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnButtons.setBackground(Color.WHITE);
        btnReset = new ImageButton("Reset Selected", FWCConfigurator.RESET_SELECTED_IMG, 150, 50);
        btnReset.addActionListener(new ResetListener());
        btnResetAll = new ImageButton("Reset All", FWCConfigurator.RESET_ALL_IMG, 150, 50);
        btnResetAll.addActionListener(new ResetListener());
        pnButtons.add(btnReset);
        pnButtons.add(btnResetAll);

        this.add(pnNorth, BorderLayout.NORTH);
        this.add(pnCenter, BorderLayout.CENTER);
        this.add(pnButtons, BorderLayout.SOUTH);
    }

    private void populateTable() {
        // Remove all rows first
        for (int i = 0, len = tableModel.getRowCount(); i < len; i++) {
            tableModel.removeRow(i);
        }

        for (Student student : students) {
            tableModel.addRow(new String[]{student.getFirstName(),
                    student.getLastName(), student.getUsername(),
                    student.getClassroom().getClassName()});
        }
    }

    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnReset) { // If reset selected student's password
                int index = studentsTable.getSelectedRow();

                // Check if selected a student
                if (index > 0) {
                    Student student = students.get(index);
                    String newPassword = student.setRandomPassword();

                    // Check database update status
                    if (FWCConfigurator.getDbConn().updateStudent(student)) {
                        // Popup with new password
                        JOptionPane.showMessageDialog(null, student.getUsername() + "'s password has been successfully " +
                                "reset to:\n" + newPassword, "Password Reset Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Student could " +
                                "not be updated in the database.",
                                "Student Update Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else { // No student is selected from the table
                    JOptionPane.showMessageDialog(null, "Please select a " +
                            "student" +
                            " from the table first.",
                            "Reset Password Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            // If reset all students' passwords
            else if (e.getSource() == btnResetAll) {
                StringBuilder message = new StringBuilder("The following students' passwords have been reset.\n");
                message.append("Please keep the new passwords in a safe place.\n\nUsername: Password");
                String newPassword;
                boolean error = false;
                int count = 0;

                for (Student student : students) {
                    newPassword = student.setRandomPassword();

                    // Check database update status
                    if (!FWCConfigurator.getDbConn().updateStudent(student)) {
                        JOptionPane.showMessageDialog(null, "Student could not be updated in the database.",
                                "Student Update Failed",
                                JOptionPane.ERROR_MESSAGE);
                        error = true;
                        break;
                    }
                    else {
                        message.append("\n")
                                .append(student.getUsername())
                                .append(": ")
                                .append(newPassword);
                        count++;
                    }
                }

                if (!error || count > 0) {
                    // Popup with new passwords
                    JOptionPane.showMessageDialog(null, message.toString(), "Password Reset Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }

            refresh();
        }
    }

    public void refresh() {
        students = FWCConfigurator.getTeacher().getStudentsRequestedReset();
        populateTable();
    }
}