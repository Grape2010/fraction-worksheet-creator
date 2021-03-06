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

import com.elementaryengineers.fwc.custom.DisabledTableModel;
import com.elementaryengineers.fwc.custom.ImageButton;
import com.elementaryengineers.fwc.custom.TitleLabel;
import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.model.Teacher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

/**The Password Reset button displays this page, where the Admin sees a list of 
 * teachers that have requested a password reset through the forgot password 
 * page. The Admin can reset the password of particular teachers or all teachers that 
 * requested it at once.
 * Created by sarahakk on 4/25/16.
 */
public class AdminManagePasswords extends JPanel {

    private JPanel pnNorth, pnCenter, pnButtons;
    private TitleLabel lblTitle;
    private ImageButton btnReset, btnResetAll;
    private DisabledTableModel tableModel;
    private JTable teachersTable;
    private JScrollPane tableScroll;
    private ArrayList<Teacher> teachers;

    public AdminManagePasswords() {
        super(new BorderLayout());
        setBackground(FWCConfigurator.bgColor);

        // Build title and north panel
        pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnNorth.setBackground(FWCConfigurator.bgColor);
        lblTitle = new TitleLabel("Reset Passwords", FWCConfigurator.RESET_PASSW_TITLE_IMG);
        pnNorth.add(lblTitle);

        // Build center panel
        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(FWCConfigurator.bgColor);

        // Build table of teachers
        tableModel = new DisabledTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Username"});

        teachersTable = new JTable(tableModel);
        teachersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        teachersTable.setFillsViewportHeight(true);
        teachersTable.getTableHeader().setFont(new Font("Calibri", Font.PLAIN, 18));
        teachersTable.setFont(new Font("Calibri", Font.PLAIN, 18));
        teachersTable.setRowHeight(teachersTable.getRowHeight() + 12);

        // Populate the table only with teachers that need a password reset
        teachers = FWCConfigurator.getAdmin().getTeachersRequestedReset();
        populateTable();

        tableScroll = new JScrollPane(teachersTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBackground(FWCConfigurator.bgColor);
        tableScroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 200, 10, 200),
                new LineBorder(Color.black, 1)));

        pnCenter.add(tableScroll, BorderLayout.CENTER);

        // Build south panel
        pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnButtons.setBackground(FWCConfigurator.bgColor);
        pnButtons.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
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
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        Collections.reverse(teachers);

        for (Teacher teacher : teachers) {
            tableModel.addRow(new String[]{teacher.getUsername()});
        }
    }

    private class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnReset) { // If reset selected teacher's password
                int index = teachersTable.getSelectedRow();

                // Check if selected a teacher
                if (index >= 0) {
                    Teacher teacher = teachers.get(index);
                    String newPassword = teacher.setRandomPassword();
                    teacher.setResetPassRequested(false);

                    // Check database update status
                    if (FWCConfigurator.getDbConn().updateTeacher(teacher)) {
                        // Popup with new password
                        JOptionPane.showMessageDialog(null, teacher.getUsername() + "'s password has been successfully " +
                                "reset to:\n" + newPassword, "Password Reset Successful", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Teacher could not be updated in the database.",
                                "Teacher Update Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                else { // No teacher is selected from the table
                    JOptionPane.showMessageDialog(null, "Please select a teacher from the table first.",
                            "Reset Password Error", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else if (e.getSource() == btnResetAll) { // If reset all teachers' passwords
                StringBuilder message = new StringBuilder("The following teachers' passwords have been reset.\n");
                message.append("Please keep the new passwords in a safe place" +
                        ".\n\nUsername Password\n-------- --------");
                String newPassword;
                boolean error = false;
                int count = 0;

                for (Teacher teacher : teachers) {
                    newPassword = teacher.setRandomPassword();
                    teacher.setResetPassRequested(false);

                    // Check database update status
                    if (!FWCConfigurator.getDbConn().updateTeacher(teacher)) {
                        JOptionPane.showMessageDialog(null, "Teacher could not be updated in the database.",
                                "Teacher Update Failed",
                                JOptionPane.ERROR_MESSAGE);
                        error = true;
                        break;
                    }
                    else {
                        message.append("\n")
                                .append(teacher.getUsername())
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
        teachers = FWCConfigurator.getAdmin().getTeachersRequestedReset();
        populateTable();
    }
}