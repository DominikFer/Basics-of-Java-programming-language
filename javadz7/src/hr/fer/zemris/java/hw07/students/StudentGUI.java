package hr.fer.zemris.java.hw07.students;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * Class for drawing GUI for {@link StudentBrowser}. 
 */
public class StudentGUI extends JFrame {
	
	private static final long serialVersionUID = -1L;
	
	private StudentDatabase db;
	
	/**
	 * Constructor. Initializes the GUI.
	 */
	public StudentGUI() {
		setLocation(20, 20);
		setSize(400, 600);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Student browser v1.0");
		
		initGUI();
	}
	
	/**
	 * GUI initialization.
	 */
	private void initGUI() {
		getContentPane().setLayout(new GridLayout(3, 1));
		
		getContentPane().add(getListPanel());
		getContentPane().add(getTablePanel());
		getContentPane().add(getStudentInsertPanel());
	}
	
	/**
	 * @return New instance of the student list panel.
	 */
	private JPanel getListPanel() {
		final JList<Student> list = new JList<Student>(getListModel());
		list.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 127) {
					int[] rows = list.getSelectedIndices();
                    for (int i = 0; i < rows.length; ++i) {
                    	if(rows[i]-i < getDb().size())
                    		getDb().remove(rows[i]-i);
                    }
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BorderLayout());
		listPanel.setBorder(BorderFactory.createTitledBorder("Lista"));
		listPanel.add(new JScrollPane(list));
		
		return listPanel;
	}
	
	/**
	 * @return New instance of {@link ListModel} of students.
	 */
	private ListModel<Student> getListModel() {
		@SuppressWarnings("serial")
		ListModel<Student> listModel = new StudentListModel(this.getDb()) {

			@Override
			public int getSize() {
				return getDb().size();
			}
			
			@Override
			public Student getElementAt(int index) {
				return getDb().getStudent(index);
			}
		};
		
		return listModel;
	}
	
	/**
	 * @return New instance of the student list table.
	 */
	private JPanel getTablePanel() {
		final JTable table = new JTable(getTableModel()){
			private static final long serialVersionUID = 1L;

			@Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component compoenent = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)compoenent;

                jc.setBorder(new MatteBorder(0, 0, 1, 0, new Color(204, 204, 204)) );

                return compoenent;   
            }  
			
			@Override
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                this.revalidate();
            }
		};
		
		table.setFillsViewportHeight(true);
		
		table.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 127) {
					int[] rows = table.getSelectedRows();
                    Arrays.sort(rows);
                    for (int i = 0; i < rows.length; ++i) {
                    	if(rows[i]-i < getDb().size())
                    		getDb().remove(rows[i]-i);
                    }
				}
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
		});
		
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1,1));
		tablePanel.setBorder(BorderFactory.createTitledBorder("Tablica"));
		tablePanel.add(new JScrollPane(table));
		
		return tablePanel;
	}
	
	/**
	 * @return New instance of {@link TableModel} of students.
	 */
	private TableModel getTableModel() {
		@SuppressWarnings("serial")
		final TableModel tableModel = new StudentTableModel(this.getDb()) {
			private String[] columns = {"#", "Student ID", "Last name", "First name"};
			
			@Override
			public int getRowCount() {
				return getDb().size();
			}

			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Student student = getDb().getStudent(rowIndex);
				
				Object value = null;
				
				if(columnIndex == 0) value = rowIndex+1;
				if(columnIndex == 1) value = student.getStudentID();
				if(columnIndex == 2) value = student.getLastName();
				if(columnIndex == 3) value = student.getFirstName();
				
				return value;
			}

			@Override
			public String getColumnName(int columnIndex) {
				return columns[columnIndex];
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) { return; }
		};
		
		return tableModel;
	}
	
	/**
	 * @return	New instance of the student insertion panel.
	 */
	private JPanel getStudentInsertPanel() {
		JPanel insertStudentLayout = new JPanel();
		insertStudentLayout.setLayout(new BorderLayout());
		insertStudentLayout.setBorder(BorderFactory.createTitledBorder("Dodavanje"));
		
		JPanel input = new JPanel();
		input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
		
		JPanel labels = new JPanel();
		labels.setLayout(new GridLayout(4, 1));
		labels.add(new JLabel("Student ID:     "));
		labels.add(new JLabel("Last name:     "));
		labels.add(new JLabel("First name:     "));
		labels.add(new JLabel());
		
		final JTextField firstName = new JTextField(100);
		final JTextField lastName = new JTextField();
		final JTextField studentID = new JTextField();
		
		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(4, 1));
		fields.add(studentID);
		fields.add(lastName);
		fields.add(firstName);
		
		JButton button = new JButton("Dodaj");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!studentID.getText().equals("") && !lastName.getText().equals("") && !firstName.getText().equals(""))
					getDb().addStudent(studentID.getText(), lastName.getText(), firstName.getText());
			}
		});
		
		fields.add(button);
		
		input.add(labels);
		input.add(fields);
		
		insertStudentLayout.add(input, BorderLayout.PAGE_START);
		
		return insertStudentLayout;
	}

	/**
	 * @return {@link StudentDatabase} using 'students.txt' file.
	 */
	public StudentDatabase getDb() {
		if(db == null)
			db = new FileStudentDatabase("students.txt");
		
		return db;
	}
}
