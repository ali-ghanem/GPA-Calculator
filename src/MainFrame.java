import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * @author Ali Ghanem
 *
 */
public class MainFrame extends JFrame implements ActionListener {
	private JPanel pActions;
	private JButton btnCalculate, btnAdd, btnDelete, btnClear, btnSave;
	private JScrollPane sc;
	private JTable table;
	private String[] columnNames = { "COURSE NAME", "CREDIT", "GRADE" };
	private DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

	public MainFrame() {
		setLayout(new BorderLayout());

		// ============================================//
		// Buttons Panel //
		// ============================================//
		pActions = new JPanel();
		pActions.setLayout(new FlowLayout());

		btnCalculate = new JButton("Calculate GPA");
		btnCalculate.addActionListener(this);
		btnAdd = new JButton("Add Course");
		btnAdd.addActionListener(this);
		btnDelete = new JButton("Delete Course");
		btnDelete.addActionListener(this);
		btnClear = new JButton("Delete All");
		btnClear.addActionListener(this);
		btnSave = new JButton("Save and Exit");
		btnSave.addActionListener(this);

		pActions.add(btnCalculate);
		pActions.add(btnAdd);
		pActions.add(btnDelete);
		pActions.add(btnClear);
		pActions.add(btnSave);

		// ============================================//
		// Table Panel //
		// ============================================//
		readSavedData();

		table = new JTable(tableModel);
		sc = new JScrollPane(table);

		// ============================================//
		// Main Frame //
		// ============================================//
		add(pActions, BorderLayout.NORTH);
		add(sc);
		setTitle("GPA Calculator");
		setSize(600, 700);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public double toNumericGrade(String g) {
		if (g.equalsIgnoreCase("A")) {
			return 4.0;
		} else if (g.equalsIgnoreCase("A-")) {
			return 3.7;
		} else if (g.equalsIgnoreCase("B+")) {
			return 3.3;
		} else if (g.equalsIgnoreCase("B")) {
			return 3.0;
		} else if (g.equalsIgnoreCase("B-")) {
			return 2.7;
		} else if (g.equalsIgnoreCase("C+")) {
			return 2.3;
		} else if (g.equalsIgnoreCase("C")) {
			return 2.0;
		} else if (g.equalsIgnoreCase("C-")) {
			return 1.7;
		} else if (g.equalsIgnoreCase("D+")) {
			return 1.3;
		} else if (g.equalsIgnoreCase("D")) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	public void calculateGpa() {
		int credit = 0;
		double grade = 0.0;
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			int tableCredit = Integer.parseInt((String) tableModel.getValueAt(i, 1));
			String tableGrade = (String) tableModel.getValueAt(i, 2);
			credit += tableCredit;
			grade += toNumericGrade(tableGrade.trim()) * tableCredit;
		}
		double result = Math.round((grade / credit) * 100.0) / 100.0;
		JOptionPane.showMessageDialog(this, "Your GPA is " + result);
	}

	public void addCourse() {
		JTextField tfName = new JTextField();
		JTextField tfCredit = new JTextField();
		JTextField tfGrade = new JTextField();
		Object[] message = { "Name:", tfName, "Credit:", tfCredit, "Grade:", tfGrade };
		int option = JOptionPane.showConfirmDialog(this, message, "Enter all course properties",
				JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			String courseName = tfName.getText();
			String courseCredit = tfCredit.getText();
			String courseGrade = tfGrade.getText();
			if (courseName != null && courseCredit != null && courseGrade != null) {
				try {
					Object[] data = { courseName.toUpperCase(), courseCredit, courseGrade.toUpperCase() };
					tableModel.addRow(data);
					JOptionPane.showMessageDialog(this, "Course has been successfully added");
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "An error ocurred. please try again.");
				}

			}
		}
	}

	public void deleteCourse() {
		String name = JOptionPane.showInputDialog(this, "Enter course name");
		String message = "No such course was found.";
		if (name != null) {
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (tableModel.getValueAt(i, 0).equals(name.toUpperCase())) {
					tableModel.removeRow(i);
					message = "Course has been successfully deleted.";
				}
			}
			JOptionPane.showMessageDialog(this, message);
		}
	}

	public void saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream("savedData.txt"));
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				String name = (String) tableModel.getValueAt(i, 0);
				String credit = (String) tableModel.getValueAt(i, 1);
				String grade = (String) tableModel.getValueAt(i, 2);
				pw.print(name + "," + credit + "," + grade + "\n");
			}
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void readSavedData() {
		try {
			Scanner s = new Scanner(new FileInputStream("savedData.txt"));
			while (s.hasNext()) {
				String line = s.nextLine();
				String[] lineElements = line.split(",");
				String name = lineElements[0];
				String credit = lineElements[1];
				String grade = lineElements[2];
				Object[] data = { name, credit, grade };
				tableModel.addRow(data);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void clearAll() {
		int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete all courses?", "Clear All",
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			tableModel.setRowCount(0);
			JOptionPane.showMessageDialog(this, "Course list has been cleared.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnCalculate)) {
			calculateGpa();
		}

		else if (e.getSource().equals(btnAdd)) {
			addCourse();
		}

		else if (e.getSource().equals(btnDelete)) {
			deleteCourse();
		}

		else if (e.getSource().equals(btnClear)) {
			clearAll();
		}

		else {
			saveData();
			System.exit(0);
		}

	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
