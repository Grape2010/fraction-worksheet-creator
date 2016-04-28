package com.elementaryengineers.fwc.panel;

import com.elementaryengineers.fwc.custom.ImageButton;
import com.elementaryengineers.fwc.custom.TitleLabel;
import com.elementaryengineers.fwc.db.FWCConfigurator;
import com.elementaryengineers.fwc.model.Student;
import com.elementaryengineers.fwc.model.Worksheet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by sarahakk on 4/27/16.
 */
public class TeacherStudentHistory extends JPanel {

    private JPanel pnNorth,pnCenter, pnButtons;
    private TitleLabel lblTitle;
    private JLabel lblName;
    private JButton btnPrint, btnAnswerKey;
    private DefaultTableModel tableModel;
    private JTable sheetsTable;
    private JScrollPane tableScroll;
    private ArrayList<Worksheet> sheets;
    private Student student;

    public TeacherStudentHistory(Student student) {
        super(new BorderLayout());
        setBackground(Color.WHITE);
        this.student = student;
        this.lblName = new JLabel(student.getFirstName() + " " + student
                .getLastName(),
                SwingConstants.RIGHT);

        // Build title and north panel
        pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnNorth.setBackground(Color.WHITE);
        lblTitle = new TitleLabel("Student Worksheet History",
                FWCConfigurator.STUDENT_WS_HISTORY_IMG);
        pnNorth.add(lblTitle);

        // Build center panel
        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(Color.WHITE);

        // Build table of worksheets
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Date", "Difficulty",
                "Exercise"});

        sheetsTable = new JTable(tableModel);
        sheetsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        sheetsTable.setFillsViewportHeight(true);
        sheetsTable.getTableHeader().setFont(new Font("Calibri",Font.PLAIN,
                18));

        // Populate the table with the student's worksheets
        sheets = student.getHistory();
        populateTable();

        tableScroll = new JScrollPane(sheetsTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        pnCenter.add(lblName, BorderLayout.NORTH);
        pnCenter.add(tableScroll, BorderLayout.CENTER);

        // Build south panel
        pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnButtons.setBackground(Color.WHITE);

        btnPrint = new ImageButton("Print Selected",
                FWCConfigurator.PRINT_SELECTED_IMG, 150, 50);
        btnPrint.addActionListener(new PrintListener());

        btnAnswerKey = new ImageButton("Answer Key",
                FWCConfigurator.ANSWER_IMG, 150, 50);
        btnAnswerKey.addActionListener(new AnswerKeyListener());

        pnButtons.add(btnPrint);
        pnButtons.add(btnAnswerKey);

        this.add(pnNorth, BorderLayout.NORTH);
        this.add(pnCenter, BorderLayout.CENTER);
        this.add(pnButtons, BorderLayout.SOUTH);
    }

    public void populateTable() {
        // Remove all rows first
        for (int i = 0, len = tableModel.getRowCount(); i < len; i++) {
            tableModel.removeRow(i);
        }

        for (Worksheet sheet : sheets) {
            tableModel.addRow(new String[]{sheet.getDateCreated(),
                    FWCConfigurator.getDifficulties().get(sheet.getDifficultyID()).
                            getDescription(),
                    sheet.getExercise()
            });
        }
    }

    public void setStudent(Student student) {
        this.student = student;
        this.lblName.setText(student.getFirstName() + " " + student
                .getLastName());
    }

    private class PrintListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = sheetsTable.getSelectedRow();

            // Check if selected a worksheet
            if (index > 0) {
                sheets.get(index).print(false);
            }
            else { // No worksheet is selected from the table
                JOptionPane.showMessageDialog(null,
                        "Please select a worksheet from the table first.",
                        "Print Worksheet Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class AnswerKeyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = sheetsTable.getSelectedRow();

            // Check if selected a worksheet
            if (index > 0) {
                sheets.get(index).print(true);
            }
            else { // No worksheet is selected from the table
                JOptionPane.showMessageDialog(null,
                        "Please select a worksheet from the table first.",
                        "Print Answer Key Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}