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
import com.elementaryengineers.fwc.model.Classroom;
import com.elementaryengineers.fwc.model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class ClassRoster displays a roster for that class, a list of students
 * that have been assigned to that class.From here, teachers can search 
 * for students and view their profiles and worksheet history.
 * 
 * @author olgasheehan
 */
public class ClassRoster extends JPanel {

    private JPanel pnNorth, pnCenter, pnTools, pnSearch, pnButtons;
    private TitleLabel lblTitle;
    private JLabel lblSearch, lblName;
    private JTextField txtSearch;
    private ImageButton btnBack, btnProfile, btnHistory;
    private DisabledTableModel tableModel;
    private JTable studentsTable;
    private JScrollPane tableScroll;
    private Classroom classroom;
    private int classIndex;
    private ArrayList<Student> currentList;

    public ClassRoster() {
        super(new BorderLayout());
        setBackground(FWCConfigurator.bgColor);

        // Build title and north panel
        pnNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnNorth.setBackground(FWCConfigurator.bgColor);
        lblTitle = new TitleLabel("Class Roster",
                FWCConfigurator.CLASS_ROSTER_IMG);
        pnNorth.add(lblTitle);

        // Build center panel
        pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(FWCConfigurator.bgColor);

        // Build buttons and search
        pnTools = new JPanel(new BorderLayout());
        pnTools.setBackground(FWCConfigurator.bgColor);
        pnTools.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 100));

        pnSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnSearch.setBackground(FWCConfigurator.bgColor);

        lblSearch = new JLabel("Search: ");
        lblSearch.setFont(new Font("Calibri", Font.PLAIN, 18));
        txtSearch = new JTextField(24);
        txtSearch.setColumns(10);

        // Add listener to show search results as soon as user starts typing
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                search();
            }
            public void removeUpdate(DocumentEvent e) {
                search();
            }
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            public void search() {
                String keyword = txtSearch.getText();

                // Populate with search results, or all classes if empty
                currentList = !keyword.equals("") ?
                        classroom.searchStudents(keyword) :
                        classroom.getStudents();
                populateTable();
            }
        });

        pnSearch.add(lblSearch);
        pnSearch.add(txtSearch);

        pnButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnButtons.setBackground(FWCConfigurator.bgColor);
        pnButtons.setBorder(BorderFactory.createEmptyBorder(0, 100, 20, 100));

        // Create buttons
        btnBack = new ImageButton("Back",
                FWCConfigurator.BACK_IMG, 150, 50);
        btnProfile = new ImageButton("Profile",
                FWCConfigurator.PROFILE_IMG, 150, 50);
        btnHistory = new ImageButton("Worksheet History",
                FWCConfigurator.WS_HISTORY_IMG, 150, 50);

        pnButtons.add(btnBack);
        pnButtons.add(btnProfile);
        pnButtons.add(btnHistory);

        // Create class name label
        lblName = new JLabel("", SwingConstants.CENTER);
        lblName.setFont(new Font("Calibri", Font.PLAIN, 18));

        pnTools.add(lblName, BorderLayout.WEST);
        pnTools.add(pnSearch, BorderLayout.EAST);

        // Build table of classes
        tableModel = new DisabledTableModel();
        tableModel.setColumnIdentifiers(new String[]{"First Name",
                "Last Name", "Difficulty Level"});

        studentsTable = new JTable(tableModel);
        studentsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentsTable.setFillsViewportHeight(true);
        studentsTable.getTableHeader().setFont(new Font("Calibri",
                Font.PLAIN, 18));
        studentsTable.setFont(new Font("Calibri", Font.PLAIN, 18));
        studentsTable.setRowHeight(studentsTable.getRowHeight() + 12);

        tableScroll = new JScrollPane(studentsTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScroll.setBackground(FWCConfigurator.bgColor);
        tableScroll.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 100, 10, 100),
                new LineBorder(Color.black, 1)));

        pnCenter.add(pnTools, BorderLayout.NORTH);
        pnCenter.add(tableScroll, BorderLayout.CENTER);

        this.add(pnNorth, BorderLayout.NORTH);
        this.add(pnCenter, BorderLayout.CENTER);
        this.add(pnButtons, BorderLayout.SOUTH);
    }

    private void populateTable() {
        // Remove all rows first
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }

        for (Student student : currentList) {
            tableModel.addRow(new String[]{student.getFirstName(),
                    student.getLastName(),
                    FWCConfigurator.getDifficulties().
                            get(student.getDifficultyID()).getDescription()});
        }
    }

    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
        this.classroom = FWCConfigurator.getTeacher().getClasses()
                .get(classIndex);
        lblName.setText("Class: " + classroom.getClassName());
        currentList = classroom.getStudents();
        populateTable();
        txtSearch.setText("");
    }

    public int getClassIndex() {
        return classIndex;
    }

    public int getSelectedStudent() {
        return studentsTable.getSelectedRow();
    }

    public ArrayList<Student> getCurrentList() {
        return currentList;
    }

    public void setBackListener(ActionListener backListener) {
        btnBack.addActionListener(backListener);
    }

    public void setProfileListener(ActionListener profileListener) {
        btnProfile.addActionListener(profileListener);
    }

    public void setHistoryListener(ActionListener historyListener) {
        btnHistory.addActionListener(historyListener);
    }
}