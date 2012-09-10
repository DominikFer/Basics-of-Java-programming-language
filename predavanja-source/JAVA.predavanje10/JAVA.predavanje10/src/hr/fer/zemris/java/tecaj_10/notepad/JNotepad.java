package hr.fer.zemris.java.tecaj_10.notepad;

import hr.fer.zemris.java.tecaj_10.local.LocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.swing.LocalizableAction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class JNotepad extends JFrame{

	private static final long serialVersionUID = -5666200389216750221L;

	private JTextArea editor;
	private Path openedFilePath;
	
	public JNotepad() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		
		initGUI();
	}
	
	public void initGUI() {
		
		editor = new JTextArea();
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolbars();
	}
	
	private void createMenus() {
		JMenuBar menuBar = new JMenuBar();
	
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(new AbstractAction("exit") {

			private static final long serialVersionUID = -2078168285193231417L;

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));
		
		JMenu editMenu = new JMenu("Edit");
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem(deleteSelectedPartAction));
		editMenu.add(new JMenuItem(toggleCaseAction));
		
		this.setJMenuBar(menuBar);
		
	}

	private void createToolbars() {
		
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(deleteSelectedPartAction));
		toolBar.add(new JButton(toggleCaseAction));
		
		this.add(toolBar, BorderLayout.PAGE_START);
		
	}

	private void createActions() {
	
		
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				"Used to open existing file from disk.");
		
		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(
				Action.NAME,
				LocalizationProvider.getInstance().getText("saveKey"));
		saveDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				"Used to save current file to disk.");
		
		deleteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
		deleteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_D);
		deleteSelectedPartAction.putValue(
				Action.NAME,
				"Delete selected part");
		deleteSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION,
				"Used to delete selected part of text");
		
		toggleCaseAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
		toggleCaseAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_T);
		toggleCaseAction.putValue(
				Action.NAME,
				"Toggle case");
		toggleCaseAction.putValue(
				Action.SHORT_DESCRIPTION,
				"Used to toggle character case in selected part of text or in whole document");
		
	}
	
	private Action openDocumentAction = new LocalizableAction(LocalizationProvider.getInstance(), "openKey") {
		
		private static final long serialVersionUID = 5592623243149369251L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Open File");
			if (fc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File fileName = fc.getSelectedFile();
			Path filePath = fileName.toPath();
			if (!Files.isReadable(filePath)) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						"Datoteka " + fileName.getAbsolutePath() + " ne postoji!",
						"Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] octets;
			try {
				octets = Files.readAllBytes(filePath);;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						"Pogreška prilikom čitanja datoteke " + fileName.getAbsolutePath(),
						"Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String text = new String(octets, StandardCharsets.UTF_8);
			editor.setText(text);
			openedFilePath = filePath;
		}
		
	};
	
	private Action saveDocumentAction = new AbstractAction() {
		
		private static final long serialVersionUID = -785500953354483420L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (openedFilePath == null) {
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save document");
				if (jfc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(JNotepad.this, 
							"Ništa nije snimljeno.",
							"Upozorenje",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				openedFilePath = jfc.getSelectedFile().toPath();
			}
			byte[] data = editor.getText().getBytes(StandardCharsets.UTF_8);
			try {
				Files.write(openedFilePath, data);				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						"Pogreška prilikom zapisivanja datoteke " + openedFilePath.toFile().getAbsolutePath()
							+ "\nPažnja. Nije jasno u kojem je stanju datoteka na disku.",
						"Pogreška",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			JOptionPane.showMessageDialog(JNotepad.this, 
					"Datoteka je snimljena.",
					"Informacija",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
	};
	
	private Action deleteSelectedPartAction = new AbstractAction() {
		
		private static final long serialVersionUID = 3421759920027758623L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
			if (len == 0) return;
			int offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			try {
				doc.remove(offset, len);				
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		
	};
	
	private Action toggleCaseAction = new AbstractAction("Toggle case") {
		
		private static final long serialVersionUID = -9145797293141498518L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//LocalizationProvider.getInstance().setLanguage("de");
			
			Document doc = editor.getDocument();
			int len = Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(editor.getCaret().getDot(), editor.getCaret().getMark());
			} else {
				len = doc.getLength();
			}
			try {
				String text = doc.getText(offset, len);
				text = changeCase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);				
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}

		private String changeCase(String text) {
			char[] znakovi = text.toCharArray();
			for (int i = 0; i < znakovi.length; ++i) {
				char c = znakovi[i];
				if (Character.isLowerCase(c)) {
					znakovi[i] = Character.toUpperCase(c);
				} else if (Character.isUpperCase(c)) {
					znakovi[i] = Character.toLowerCase(c);
				}
			}
			return new String(znakovi);
		}
		
	};
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new JNotepad().setVisible(true);
			}
		});
		
	}
	
}
