package hr.fer.zemris.java.tecaj_10.notepad;

import hr.fer.zemris.java.tecaj_10.local.ILocalizationListener;
import hr.fer.zemris.java.tecaj_10.local.swing.LJLabel;
import hr.fer.zemris.java.tecaj_10.notepad.JNotepad.JNotepadDocument;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JStatisticsDialog extends JDialog implements ILocalizationListener, KeyListener, ChangeListener {

	private static final long serialVersionUID = 197062733094385669L;

	private JNotepad parent;
	private JPanel textualStatistics;
	
	private JLabel documentLengthValue;
	private JLabel uppercaseLettersValue;
	private JLabel lowercaseLettersValue;
	private JLabel spacesCount;
	private JLabel wordsCount; 
	private StatisticsVisual graph;
	
	public JStatisticsDialog(JNotepad parent) {
		
		this.parent = parent;
		parent.getLocalizationProvider().addLocalizationListener(this);
		registerAsListener();
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		
		initGUI();
		pack();
		
	}
	
	private void registerAsListener() {
		
		parent.getActiveDocument().getTextarea().addKeyListener(this);
		
	}
	
	private void initGUI() {
		
		updateDialogTitle();
		this.setLayout(new BorderLayout());
		
		textualStatistics = new JPanel();
		this.add(textualStatistics, BorderLayout.PAGE_START);
		setStatisticsBorder();
		
		textualStatistics.setLayout(new GridLayout(5, 2));
		
		JLabel documentLengthCaption = new LJLabel(parent.getLocalizationProvider(), "documentLengthKey");
		documentLengthValue = new JLabel("0", SwingConstants.CENTER);
		textualStatistics.add(documentLengthCaption);
		textualStatistics.add(documentLengthValue);
		
		JLabel uppercaseLettersCaption = new LJLabel(parent.getLocalizationProvider(), "uppercaseLettersKey");
		uppercaseLettersValue = new JLabel("0", SwingConstants.CENTER);
		textualStatistics.add(uppercaseLettersCaption);
		textualStatistics.add(uppercaseLettersValue);
		
		JLabel lowercaseLettersCaption = new LJLabel(parent.getLocalizationProvider(), "lowercaseLettersKey");
		lowercaseLettersValue = new JLabel("0", SwingConstants.CENTER);
		textualStatistics.add(lowercaseLettersCaption);
		textualStatistics.add(lowercaseLettersValue);
		
		JLabel spacesCaption = new LJLabel(parent.getLocalizationProvider(), "spacesKey");
		spacesCount = new JLabel("0", SwingConstants.CENTER);
		textualStatistics.add(spacesCaption);
		textualStatistics.add(spacesCount);
		
		JLabel wordsCaption = new LJLabel(parent.getLocalizationProvider(), "wordsKey");
		wordsCount = new JLabel("0", SwingConstants.CENTER);
		textualStatistics.add(wordsCaption);
		textualStatistics.add(wordsCount);
		
		updateStatistics();
		
		graph = new StatisticsVisual(
				new LJLabel(parent.getLocalizationProvider(), "graphTitleKey", JLabel.CENTER),
				new JLabel[] {documentLengthValue,
					uppercaseLettersValue,
					lowercaseLettersValue,
					spacesCount,
					wordsCount
				}
		);
		this.add(graph, BorderLayout.CENTER);
		
	}
	
	private void updateDialogTitle() {
		setTitle(parent.getLocalizationProvider().getString("statisticsDialogTitleKey"));
	}
	
	private void setStatisticsBorder() {
		textualStatistics.setBorder(BorderFactory.createTitledBorder(
				parent.getLocalizationProvider().getString("statisticsTitleKey")));
	}
	
	private void updateStatistics() {
		
		JNotepadDocument activeDocument = parent.getActiveDocument();
				
		documentLengthValue.setText(
				new Integer(activeDocument.getTextarea().getText().length()).toString());
		uppercaseLettersValue.setText(
				new Integer(countUppercasedLetters(activeDocument.getTextarea().getText())).toString());
		lowercaseLettersValue.setText(
				new Integer(countLowercasedLetters(activeDocument.getTextarea().getText())).toString());
		spacesCount.setText(
				new Integer(countSpaces(activeDocument.getTextarea().getText())).toString());
		wordsCount.setText(
				new Integer(countWords(activeDocument.getTextarea().getText())).toString());
		
		if (graph != null) {
			graph.refresh();
		}
		
	}
	
	private int countUppercasedLetters(String text) {
		
		int uppercased = 0;
		
		for (int i = 0; i < text.length(); ++i) {
			char curr = text.charAt(i);
			if (Character.isUpperCase(curr))
				uppercased++;
		}
		
		return uppercased;
		
	}
	
	private int countLowercasedLetters(String text) {
		
		int lowercased = 0;
		
		for (int i = 0; i < text.length(); ++i) {
			char curr = text.charAt(i);
			if (Character.isLowerCase(curr))
				lowercased++;
		}
		
		return lowercased;
		
	}
	
	private int countSpaces(String text) {
		
		int spaces = 0;
		
		for (int i = 0; i < text.length(); ++i) {
			char curr = text.charAt(i);
			if (curr == ' ')
				spaces++;
		}
		
		return spaces;
		
	}
	
	private int countWords(String text) {
		
		int words = 0;
		
		boolean wordStarted = false;
		for (int i = 0; i < text.length(); ++i) {
			char curr = text.charAt(i);
			if (wordStarted == false && !Character.isSpaceChar(curr)) {
				wordStarted = true;
				words++;
			} else if (wordStarted == true && Character.isWhitespace(curr)) {
				wordStarted = false;
			}
		}
		
		return words;
		
	}

	@Override
	public void localizationChanged() {
		updateDialogTitle();
		setStatisticsBorder();
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		updateStatistics();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		registerAsListener();
		updateStatistics();
	}
		
}