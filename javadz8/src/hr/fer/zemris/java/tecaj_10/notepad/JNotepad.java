package hr.fer.zemris.java.tecaj_10.notepad;

import hr.fer.zemris.java.tecaj_10.local.FormLocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.ILocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.LocalizationProvider;
import hr.fer.zemris.java.tecaj_10.local.swing.LJFileChooser;
import hr.fer.zemris.java.tecaj_10.local.swing.LJLabel;
import hr.fer.zemris.java.tecaj_10.local.swing.LJMenu;
import hr.fer.zemris.java.tecaj_10.local.swing.LJToolBar;
import hr.fer.zemris.java.tecaj_10.local.swing.LocalizableAction;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;

public class JNotepad extends JFrame{

	private static final long serialVersionUID = -5666200389216750221L;

	private JTabbedPane tabs;
	private List<JNotepadDocument> documents = new ArrayList<>();
	private JNotepadDocument activeDocument;
	
	private ILocalizationProvider localizationProvider = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
	
	public JNotepad() {
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		
		initGUI();
	}
	
	public void initGUI() {
				
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(createTabs(), BorderLayout.CENTER);
		
		createActions();
		createMenus();
		createToolbars();
		createBlankDocument();

	}
	
	public JTabbedPane createTabs() {
		
		tabs = new JTabbedPane();
		tabs.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				activeDocument = documents.get(tabs.getSelectedIndex());
			}
			
		});
		return tabs;
		
	}
	
	private void createBlankDocument() {
		JNotepadDocument blankDoc = new JNotepadDocument();
		documents.add(blankDoc);
		newTab(blankDoc);
	}
	
	private void newTab(JNotepadDocument document) {
		
		activeDocument = document;
		tabs.addTab("untitled", new JScrollPane(activeDocument.getTextarea()));
		synchronized (tabs.getTreeLock()) {
			if (document.getDocumentPath() == null)
				tabs.setTabComponentAt(tabs.getTabCount()-1, new LJLabel(localizationProvider, "untitledDocKey"));
			else
				tabs.setTabComponentAt(tabs.getTabCount()-1, new JLabel(document.getTitle()));
			tabs.setSelectedIndex(tabs.getTabCount()-1);
		}
		
	}
	
	private void updateTabTitle(JNotepadDocument document) {
		int index = documents.indexOf(document);
		if (tabs.getTabComponentAt(index) instanceof JLabel) {
			JLabel label = (JLabel) tabs.getTabComponentAt(index);
			label.setText(document.getTitle());
		}
	}
	
	private void createMenus() {
		
		JMenuBar menuBar = new JMenuBar();
	
		JMenu fileMenu = new LJMenu(localizationProvider, "fileKey");
		menuBar.add(fileMenu);
		
		fileMenu.add(new JMenuItem(newDocumentAction));
		fileMenu.add(new JMenuItem(openDocumentAction));
		fileMenu.add(new JMenuItem(saveDocumentAction));
		fileMenu.add(new JMenuItem(saveAsDocumentAction));
		fileMenu.add(new JMenuItem(closeDocumentAction));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(new LocalizableAction(localizationProvider, "exitKey") {
			private static final long serialVersionUID = -2078168285193231417L;
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}));
		
		JMenu editMenu = new LJMenu(localizationProvider, "editKey");
		menuBar.add(editMenu);
		editMenu.add(new JMenuItem(copyAction));
		localizationProvider.addLocalizationListener(copyActionListener);
		editMenu.add(new JMenuItem(cutAction));
		localizationProvider.addLocalizationListener(cutActionListener);
		editMenu.add(new JMenuItem(pasteAction));
		localizationProvider.addLocalizationListener(pasteActionListener);
		editMenu.add(new JMenuItem(deleteSelectedPartAction));
		editMenu.add(new JMenuItem(clearAllAction));
		
		JMenu editChangeMenu = new LJMenu(localizationProvider, "changeKey");
		editMenu.add(editChangeMenu);
		editChangeMenu.add(new JMenuItem(uppercaseAction));
		editChangeMenu.add(new JMenuItem(lowercaseAction));
		editChangeMenu.add(new JMenuItem(toggleCaseAction));
		editChangeMenu.add(new JMenuItem(wordcaseAction));
		
		JMenu editAdvancedMenu = new LJMenu(localizationProvider, "advancedKey");
		editMenu.add(editAdvancedMenu);
		editAdvancedMenu.add(new JMenuItem(sortLinesAscendingAction));
		editAdvancedMenu.add(new JMenuItem(sortLinesDescendingAction));
		editAdvancedMenu.add(new JMenuItem(removeEmptyLinesAction));
		
		JMenu langMenu = new LJMenu(localizationProvider, "languageKey");
		menuBar.add(langMenu);
		langMenu.add(new JMenuItem(changeLanguageToCroatianAction));
		langMenu.add(new JMenuItem(changeLanguageToEnglishAction));
		
		JMenu infoMenu = new LJMenu(localizationProvider, "infoKey");
		menuBar.add(infoMenu);
		infoMenu.add(new JMenuItem(openStatisticsAction));
		
		this.setJMenuBar(menuBar);
		
	}

	private void createToolbars() {
		
		JToolBar toolBar = new LJToolBar(localizationProvider, "toolsKey");
		toolBar.setFloatable(true);
		
		toolBar.add(new JButton(openDocumentAction));
		toolBar.add(new JButton(saveDocumentAction));
		toolBar.addSeparator();
		toolBar.add(new JButton(deleteSelectedPartAction));
		toolBar.add(new JButton(toggleCaseAction));
		
		this.add(toolBar, BorderLayout.PAGE_START);
		
	}

	private void createActions() {
		
		newDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control n"));
		newDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_N);
		newDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("newDocumentDescKey"));
		
		openDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		openDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_0);
		openDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("openDescKey"));
		
		saveDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		saveDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_S);
		saveDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("saveDescKey"));
		
		closeDocumentAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		closeDocumentAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_W);
		closeDocumentAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("closeDescKey"));
		
		deleteSelectedPartAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
		deleteSelectedPartAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_D);
		deleteSelectedPartAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("deleteSelectionDescKey"));
		
		toggleCaseAction.putValue(
				Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control F3"));
		toggleCaseAction.putValue(
				Action.MNEMONIC_KEY, KeyEvent.VK_T);
		toggleCaseAction.putValue(
				Action.SHORT_DESCRIPTION,
				localizationProvider.getString("toggleCaseDescKey"));
		
	}
	
	public ILocalizationProvider getLocalizationProvider() {
		return localizationProvider;
	}
	
	public JNotepadDocument getActiveDocument() {
		return activeDocument;
	}
	
	private Action newDocumentAction = new LocalizableAction(localizationProvider, "newDocumentKey") {
		
		private static final long serialVersionUID = 50715367381384764L;

		@Override
		public void actionPerformed(ActionEvent e) {
			createBlankDocument();
		}
		
	};
	
	private Action openDocumentAction = new LocalizableAction(localizationProvider, "openKey") {
		
		private static final long serialVersionUID = 5592623243149369251L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LJFileChooser fc = new LJFileChooser(localizationProvider, "openFileTitleKey");
			if (fc.showOpenDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			JNotepadDocument newDoc = new JNotepadDocument(fc.getSelectedFile().toPath());
 			if (!documents.contains(newDoc)) {
 				documents.add(newDoc);
 				newTab(newDoc);
 			} else {
 				tabs.setSelectedIndex(documents.indexOf(newDoc));
 			}
		}
		
	};
	
	private Action saveDocumentAction = new LocalizableAction(localizationProvider, "saveKey") {
		
		private static final long serialVersionUID = -785500953354483420L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (activeDocument.getDocumentPath() == null) {
				LJFileChooser jfc = new LJFileChooser(localizationProvider, "saveFileTitleKey");
				if (jfc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(JNotepad.this, 
							localizationProvider.getString("nothingSavedTextKey"),
							localizationProvider.getString("nothingSavedTitleKey"),
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				activeDocument.setDocumentPath(jfc.getSelectedFile().toPath());
			}
			activeDocument.save();
			return;
		}
		
	};
	
	private Action saveAsDocumentAction = new LocalizableAction(localizationProvider, "saveAsKey") {
		
		private static final long serialVersionUID = -9074899356550436236L;

		@Override
		public void actionPerformed(ActionEvent e) {

			LJFileChooser jfc = new LJFileChooser(localizationProvider, "saveFileTitleKey");
			if (jfc.showSaveDialog(JNotepad.this) != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						localizationProvider.getString("nothingSavedTextKey"),
						localizationProvider.getString("nothingSavedTitleKey"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			JNotepadDocument newDoc = new JNotepadDocument(jfc.getSelectedFile().toPath(),
					activeDocument.getTextarea().getText());
			
			documents.set(documents.indexOf(activeDocument), newDoc);
			activeDocument = newDoc;
			newDoc.save();
			
			return;
			
		}
		
	};
	
	private Action closeDocumentAction = new LocalizableAction(localizationProvider, "closeKey") {
		
		private static final long serialVersionUID = -9074899356550436236L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (activeDocument.isChanged()) {
				int action = JOptionPane.showConfirmDialog(null,
						localizationProvider.getString("saveBeforeCloseTextKey"),
						localizationProvider.getString("saveBeforeCloseTitleKey"),
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (action == JOptionPane.YES_OPTION) {
					if (activeDocument.getDocumentPath() == null) {
						// if file isn't saved yet
						LJFileChooser jfc = new LJFileChooser(localizationProvider, "saveFileTitleKey");
						if (jfc.showSaveDialog(JNotepad.this) == JFileChooser.APPROVE_OPTION) {
							activeDocument.setDocumentPath(jfc.getSelectedFile().toPath());
						}
					}
					// if user selected file for saving
					if (activeDocument.getDocumentPath() != null)
						activeDocument.save();
				} else if (action == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
			
			int currentIndex = tabs.getSelectedIndex();
			if (tabs.getTabCount() == 1)
				createBlankDocument();
			tabs.removeTabAt(currentIndex);
			documents.remove(currentIndex);
			
		}
		
	};
	
	private Action deleteSelectedPartAction = new LocalizableAction(localizationProvider, "deleteSelectedKey") {
		
		private static final long serialVersionUID = 3421759920027758623L;

		@Override
		public void actionPerformed(ActionEvent e) {
			Document doc = activeDocument.getTextarea().getDocument();
			int len = Math.abs(activeDocument.getTextarea().getCaret().getDot() - activeDocument.getTextarea().getCaret().getMark());
			if (len == 0) return;
			int offset = Math.min(activeDocument.getTextarea().getCaret().getDot(), activeDocument.getTextarea().getCaret().getMark());
			try {
				doc.remove(offset, len);				
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
		
	};
	
	private Action copyAction = new DefaultEditorKit.CopyAction();
	private ILocalizationListener copyActionListener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			copyAction.putValue(Action.NAME, localizationProvider.getString("copyKey"));
		}
	};
	
	private Action cutAction = new DefaultEditorKit.CutAction();
	private ILocalizationListener cutActionListener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			cutAction.putValue(Action.NAME, localizationProvider.getString("cutKey"));
		}
	};
	
	private Action pasteAction = new DefaultEditorKit.PasteAction();
	private ILocalizationListener pasteActionListener = new ILocalizationListener() {
		
		@Override
		public void localizationChanged() {
			pasteAction.putValue(Action.NAME, localizationProvider.getString("pasteKey"));
		}
	};
	
	private Action clearAllAction = new LocalizableAction(localizationProvider, "clearAllKey") {
		
		private static final long serialVersionUID = 3522216663586412376L;

		@Override
		public void actionPerformed(ActionEvent e) {
			activeDocument.getTextarea().setText("");
		}
		
	};
	
	private Action uppercaseAction = new LocalizableAction(localizationProvider, "uppercaseKey") {

		private static final long serialVersionUID = -397559536816934415L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			int dot = Math.min(selection.getMark(), selection.getDot());
			int selectionLength = Math.abs(selection.getDot() - selection.getMark());
			
			int len = Math.abs(selection.getDot() - activeDocument.getTextarea().getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(selection.getDot(), selection.getMark());
			} else {
				len = doc.getLength();
			}
			try {
				String text = doc.getText(offset, len);
				text = uppercase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
				selection.setDot(dot);
				selection.moveDot(dot + selectionLength);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
		}
		
		private String uppercase(String text) {
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < text.length(); ++i) {
				char c = text.charAt(i);
				if (Character.isLowerCase(c))
					c = Character.toUpperCase(c);
				sb.append(c);
			}
			
			return sb.toString();
			
		}
		
	};
	
	private Action lowercaseAction = new LocalizableAction(localizationProvider, "lowercaseKey") {

		private static final long serialVersionUID = 362687447949499550L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			int dot = Math.min(selection.getMark(), selection.getDot());
			int selectionLength = Math.abs(selection.getDot() - selection.getMark());
			
			int len = Math.abs(selection.getDot() - activeDocument.getTextarea().getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(selection.getDot(), selection.getMark());
			} else {
				len = doc.getLength();
			}
			try {
				String text = doc.getText(offset, len);
				text = lowercase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
				selection.setDot(dot);
				selection.moveDot(dot + selectionLength);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
		}
		
		private String lowercase(String text) {
			
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < text.length(); ++i) {
				char c = text.charAt(i);
				if (Character.isUpperCase(c))
					c = Character.toLowerCase(c);
				sb.append(c);
			}
			
			return sb.toString();
			
		}
		
	};
	
	private Action wordcaseAction = new LocalizableAction(localizationProvider, "wordcaseKey") {

		private static final long serialVersionUID = 2422728184930072782L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			int dot = Math.min(selection.getMark(), selection.getDot());
			int selectionLength = Math.abs(selection.getDot() - selection.getMark());
			
			int len = Math.abs(selection.getDot() - activeDocument.getTextarea().getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(selection.getDot(), selection.getMark());
			} else {
				len = doc.getLength();
			}
			try {
				String text = doc.getText(offset, len);
				text = wordcase(text);
				doc.remove(offset, len);
				doc.insertString(offset, text, null);
				selection.setDot(dot);
				selection.moveDot(dot + selectionLength);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
		}
		
		private String wordcase(String text) {
			
			StringBuilder sb = new StringBuilder();
			boolean wordStarted = false;
			for (int i = 0; i < text.length(); ++i) {
				char curr = text.charAt(i);
				if (Character.isAlphabetic(curr) && wordStarted == false) {
					wordStarted = true;
					curr = Character.toUpperCase(curr);
				} else if (wordStarted == true && Character.isWhitespace(curr)) {
					wordStarted = false;
				}
				sb.append(curr);
			}
			
			return sb.toString();
			
		}
		
	};
	
	private Action toggleCaseAction = new LocalizableAction(localizationProvider, "toggleCaseKey") {
		
		private static final long serialVersionUID = -9145797293141498518L;

		@Override
		public void actionPerformed(ActionEvent e) {
						
			Document doc = activeDocument.getTextarea().getDocument();
			int len = Math.abs(activeDocument.getTextarea().getCaret().getDot() - activeDocument.getTextarea().getCaret().getMark());
			int offset = 0;
			if (len != 0) {
				offset = Math.min(activeDocument.getTextarea().getCaret().getDot(), activeDocument.getTextarea().getCaret().getMark());
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
	
	private Action sortLinesAscendingAction = new LocalizableAction(localizationProvider, "sortAscKey") {
		
		private static final long serialVersionUID = -423200324138040989L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			
			Element startLine = Utilities.getParagraphElement(activeDocument.getTextarea(), 
					Math.min(selection.getDot(), selection.getMark()));
			Element endLine = Utilities.getParagraphElement(activeDocument.getTextarea(),
					Math.max(selection.getDot(), selection.getMark()));
			
			try {
				int offset = startLine.getStartOffset();
				int length = endLine.getEndOffset()-startLine.getStartOffset();
				String text = doc.getText(offset, length);
				text = sortLines(text);
				doc.remove(offset, length-1);
				doc.insertString(offset, text, null);
				/*selection.setDot(offset);
				selection.moveDot(length);/**/
			} catch (BadLocationException ignorable) {
				System.out.println(ignorable);
			}

		}
		
		public String sortLines(String text) {
			
			String[] lines = text.split("\\n");
			Arrays.sort(lines);
			StringBuilder sb = new StringBuilder(text.length());
			for (int i = 1; i < lines.length; ++i) {
				sb.append(lines[i-1]);
				sb.append("\n");
			}
			sb.append(lines[lines.length-1]);
			
			return sb.toString();
			
		}
		
	};
	
	private Action sortLinesDescendingAction = new LocalizableAction(localizationProvider, "sortDescKey") {
		
		private static final long serialVersionUID = 7739334307789794752L;

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			
			Element startLine = Utilities.getParagraphElement(activeDocument.getTextarea(), 
					Math.min(selection.getDot(), selection.getMark()));
			Element endLine = Utilities.getParagraphElement(activeDocument.getTextarea(),
					Math.max(selection.getDot(), selection.getMark()));
			
			try {
				int offset = startLine.getStartOffset();
				int length = endLine.getEndOffset()-startLine.getStartOffset();
				String text = doc.getText(offset, length);
				text = sortLines(text);
				doc.remove(offset, length-1);
				doc.insertString(offset, text, null);
				/*selection.setDot(offset);
				selection.moveDot(length);/**/
			} catch (BadLocationException ignorable) {
				System.out.println(ignorable);
			}

		}
		
		public String sortLines(String text) {
			
			String[] lines = text.split("\\n");
			Arrays.sort(lines, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					return -1*o1.compareTo(o2);
				}
			});
			StringBuilder sb = new StringBuilder(text.length());
			for (int i = 1; i < lines.length; ++i) {
				sb.append(lines[i-1]);
				sb.append("\n");
			}
			sb.append(lines[lines.length-1]);
			
			return sb.toString();
			
		}

	};	
	
	private Action removeEmptyLinesAction = new LocalizableAction(localizationProvider, "removeEmptyLinesKey") {
		
		private static final long serialVersionUID = -3673400262728500917L;

		public void actionPerformed(ActionEvent e) {
			
			Document doc = activeDocument.getTextarea().getDocument();
			Caret selection = activeDocument.getTextarea().getCaret();
			
			Element startLine = Utilities.getParagraphElement(activeDocument.getTextarea(), 
					Math.min(selection.getDot(), selection.getMark()));
			Element endLine = Utilities.getParagraphElement(activeDocument.getTextarea(),
					Math.max(selection.getDot(), selection.getMark()));
			
			try {
				int offset = startLine.getStartOffset();
				int length = endLine.getEndOffset()-startLine.getStartOffset();
				String text = doc.getText(offset, length);
				text = removeEmptyLines(text);
				doc.remove(offset, length-1);
				doc.insertString(offset, text, null);
				/*selection.setDot(offset);
				selection.moveDot(length);/**/
			} catch (BadLocationException ignorable) {
				System.out.println(ignorable);
			}

		}
		
		public String removeEmptyLines(String text) {
			
			String[] lines = text.split("\\n");
			StringBuilder sb = new StringBuilder(text.length());
			if (lines[0].length() > 0)
				sb.append(lines[0]);
			for (int i = 1; i < lines.length; ++i) {
				if (lines[i].length() == 0) continue;
				if (sb.length() > 0)
					sb.append("\n");
				sb.append(lines[i]);
			}
			
			return sb.toString();
			
		}

		
	};
	
	private Action openStatisticsAction = new LocalizableAction(localizationProvider, "statisticsKey") {
		
		private static final long serialVersionUID = -5135788976606985305L;

		@Override
		public void actionPerformed(ActionEvent e) {
		
			JDialog stats = new JStatisticsDialog(JNotepad.this);
			stats.setVisible(true);
			
			tabs.addChangeListener((ChangeListener) stats);
			
		}
	};
	
	private Action changeLanguageToCroatianAction = new LocalizableAction(localizationProvider, "hrLanguageKey") {
		
		private static final long serialVersionUID = -4551511827122638610L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	};
	
	private Action changeLanguageToEnglishAction = new LocalizableAction(localizationProvider, "enLanguageKey") {
		
		private static final long serialVersionUID = 3543256733094589608L;

		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	};
	
	/**
	 * @author mario
	 *
	 */
	public class JNotepadDocument {
		
		private Path documentPath;
		private JTextArea textarea;
		private boolean changed = false;
		
		public JNotepadDocument() {
			this.textarea = createTextarea();
		}
		
		public JNotepadDocument(Path path) {
			this.documentPath = path;
			this.textarea = createTextarea();
			readDocument();
		}
		
		public JNotepadDocument(Path path, String content) {
			this.documentPath = path;
			this.textarea = createTextarea();
			this.textarea.setText(content);
		}
		
		private void readDocument() {
			if (!Files.isReadable(documentPath)) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						localizationProvider.getString("fileNotReadableTextKey", new String[]{documentPath.toFile().getAbsolutePath()}),
						localizationProvider.getString("fileNotReadableTitleKey"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] octets;
			try {
				octets = Files.readAllBytes(documentPath);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						localizationProvider.getString("fileReadErrorTextKey", new String[]{documentPath.toFile().getAbsolutePath()}),
						localizationProvider.getString("fileReadErrorTitleKey"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			String text = new String(octets, StandardCharsets.UTF_8);
			textarea.setText(text);
			
		}
		
		public void save() {
			byte[] data = getTextarea().getText().getBytes(StandardCharsets.UTF_8);
			try {
				Files.write(getDocumentPath(), data);				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(JNotepad.this, 
						localizationProvider.getString("fileSaveErrorTitleText",
								new String[]{ getDocumentPath().toFile().getAbsolutePath() }),
						localizationProvider.getString("fileSaveErrorTitleKey"),
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			changed = false;
			
			JOptionPane.showMessageDialog(JNotepad.this, 
					localizationProvider.getString("fileSavedTextKey"),
					localizationProvider.getString("fileSavedTitleKey"),
					JOptionPane.INFORMATION_MESSAGE);
			updateTabTitle(this);
		}
		
		private JTextArea createTextarea() {
			JTextArea textarea = new JTextArea();
			
			textarea.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					changed = true;
				}
				
				@Override
				public void keyPressed(KeyEvent e) {					
				}
			});
			
			return textarea;
		}

		public String getTitle() {
			if (documentPath != null)
				return documentPath.getFileName().toString();
			return localizationProvider.getString("untitledDocKey");
		}
		
		/**
		 * @return the documentPath
		 */
		public Path getDocumentPath() {
			return documentPath;
		}

		/**
		 * @param documentPath the documentPath to set
		 */
		public void setDocumentPath(Path documentPath) {
			this.documentPath = documentPath;
		}

		/**
		 * @return the textarea
		 */
		public JTextArea getTextarea() {
			return textarea;
		}

		/**
		 * @return the changed
		 */
		public boolean isChanged() {
			return changed;
		}

		@Override
		public boolean equals(Object obj) {
			
			if (obj == null) return false;
			if (this == obj) return true;
			
			if (obj instanceof JNotepadDocument) {
				JNotepadDocument doc = (JNotepadDocument) obj;
				return this.documentPath.equals(doc.getDocumentPath());
			}
			
			return false;
			
		}
		
		
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new JNotepad().setVisible(true);
			}
		});
		
	}
	
}
