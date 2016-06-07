package net.pyraetos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.pyraetos.util.Sys;
import net.pyraetos.util.Tuple;
import net.pyraetos.util.Tuple3;

@SuppressWarnings("serial")
public class Pyraecords extends JFrame{

	private static JPanel contentPane;
	
	private static JPanel recordsPanel;
	private static JScrollPane scrollPane;
	private static ArrayList<RecordPanel> recordPanels = new ArrayList<RecordPanel>();
	private static JButton addRecordButton;
	
	private static JPanel communicationPanel;
	private static JButton loadButton;
	private static JButton sendButton;
	private static JTextField passwordField;
	private static boolean pwClicked;
	
	private static Pyraecords instance;
	
	private class DeleteButton extends JButton{
		
		private String title;
		
		DeleteButton(String text, String title){
			super(text);
			this.title = title;
		}
		
	}
	
	private class RecordPanel extends JPanel{
		
		private JLabel titleLabel;
		private JTextField titleTextField;
		private JPanel titlePanel;
		
		private Component ra1;
		private JButton addDatumButton;
		private Component ra2;
		
		private boolean isLabel;
		private HashMap<String, Tuple<Tuple3<JLabel, JTextField, JPanel>, JTextField>> data = new HashMap<String, Tuple<Tuple3<JLabel, JTextField, JPanel>, JTextField>>();
		
		private String getUniqueName(){
			int i = 0;
			while(data.keySet().contains("Data" + i))
				i++;
			return "Data" + i;
		}
		
		RecordPanel(Record record){
			ra1 = Sys.space();
			ra2 = Sys.space();
			setLayout(new GridLayout(record.size() + 2, 3, 15, 5));
			
			titlePanel = new JPanel();
			titlePanel.setLayout(new BorderLayout());
			titleTextField = new JTextField();
			titleTextField.addKeyListener(new KeyAdapter(){
				@Override
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						switchToLabel();
				}
			});
			
			titleLabel = new JLabel(record.title, SwingConstants.CENTER);
			titleLabel.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseEntered(MouseEvent e){
					titleLabel.setBorder(BorderFactory.createLineBorder(Color.black));
				}
				@Override
				public void mouseExited(MouseEvent e){
					titleLabel.setBorder(null);
				}
				@Override
				public void mousePressed(MouseEvent e){
					switchToTextField();
				}
			});
			isLabel = true;
			titlePanel.add(titleLabel);
			add(titlePanel);
			
			//Delete record button
			//***************************************************
			
			JPanel drbPanel = new JPanel();
			JButton drButton = new JButton("Delete");
			drButton.addActionListener((e)->{
				recordPanels.removeIf((r)->r.titleLabel.getText() == titleLabel.getText());
				redisplay();
			});
			drbPanel.add(drButton);
			add(drbPanel);
			add(Sys.space());
			
			//Populates data
			//***************************************************
			for(String title : record.data.keySet()){
				JLabel datumLabel = new JLabel(title + ":");
				add(datumLabel);
				JTextField textField = new JTextField(record.data.get(title));
				textField.setPreferredSize(new Dimension(60,24));
				add(textField);
				data.put(title, new Tuple<Tuple3<JLabel, JTextField, JPanel>, JTextField>(new Tuple3<JLabel, JTextField, JPanel>(datumLabel, null, null), textField));
				JPanel temp = new JPanel();
				temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
				DeleteButton button = new DeleteButton("Delete", title);
				button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
				button.addActionListener((e)->{
					if(data.size() > 1){
						remove(data.get(button.title).a.a);
						remove(data.get(button.title).b);
						remove(temp);
						data.remove(button.title);
						setLayout(new GridLayout(data.size() + 2, 3, 15, 5));
						revalidate();
					}
				});
				temp.add(button);
				add(temp);
			}
			add(ra1);
			JPanel makeButtonSmaller = new JPanel();
			//***************************************************
			
			//Deals with adding a new data value
			//***************************************************
			addDatumButton = new JButton("+");
			addDatumButton.addActionListener((e)->{
				String uname = getUniqueName();
				JLabel l = new JLabel(uname + ":");
				JPanel datumPanel = new JPanel();
				datumPanel.setLayout(new BorderLayout());
				JTextField titletf = new JTextField(uname);
				JTextField tf = new JTextField("");
				tf.setPreferredSize(new Dimension(60,24));
				titletf.addKeyListener(new KeyAdapter(){
					@Override
					public void keyPressed(KeyEvent e){
						if(e.getKeyCode() == KeyEvent.VK_ENTER){
							data.remove(uname);
							data.put(titletf.getText(), new Tuple<Tuple3<JLabel, JTextField, JPanel>, JTextField>(new Tuple3<JLabel, JTextField, JPanel>(l, titletf, datumPanel), tf));
							l.setText(titletf.getText() + ":");
							datumPanel.remove(titletf);
							datumPanel.add(l);
							revalidate();
						}
					}
				});
				data.put(uname, new Tuple<Tuple3<JLabel, JTextField, JPanel>, JTextField>(new Tuple3<JLabel, JTextField, JPanel>(l, titletf, datumPanel), tf));
				remove(makeButtonSmaller);
				remove(ra1);
				remove(ra2);
				datumPanel.add(titletf);
				add(datumPanel);
				add(tf);
				JPanel temp = new JPanel();
				temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
				DeleteButton button = new DeleteButton("Delete", uname);
				button.setAlignmentX(JComponent.CENTER_ALIGNMENT);
				button.addActionListener((e0)->{
					if(data.size() > 1){
						remove(data.get(button.title).a.c);
						remove(data.get(button.title).b);
						remove(temp);
						data.remove(button.title);
						setLayout(new GridLayout(data.size() + 2, 3, 15, 5));
						revalidate();
					}
				});
				temp.add(button);
				add(temp);
				add(ra1);
				add(makeButtonSmaller);
				add(ra2);
				setLayout(new GridLayout(data.size() + 2, 3, 15, 5));
				titletf.requestFocus();
				revalidate();
			});
			makeButtonSmaller.add(addDatumButton);
			add(makeButtonSmaller);
			add(ra2);
			//***************************************************
		}
		
		private void switchToTextField(){
			if(!isLabel) return;
			String title = titleLabel.getText();
			titleTextField.setText(title);
			titlePanel.remove(titleLabel);
			titlePanel.add(titleTextField);
			isLabel = false;
			titleTextField.requestFocus();
			revalidate();
			repaint();
		}
		
		private void switchToLabel(){
			if(isLabel) return;
			String title = titleTextField.getText();
			titleLabel.setText(title);
			titleLabel.setBorder(null);
			titlePanel.remove(titleTextField);
			titlePanel.add(titleLabel);
			isLabel = true;
			revalidate();
			repaint();
		}
		
		Record toRecord(){
			Record result = new Record();
			switchToLabel();
			result.title = titleLabel.getText();
			for(String key : data.keySet()){
				result.data.put(key, data.get(key).b.getText());
			}
			return result;
		}
		
	}
	
	public static void main(String[] args) {
		instance = new Pyraecords();
	}
	
	public Pyraecords(){
		initWindow();
		initRecordsPanel();
		initCommunicationPanel();
		
		pack();
		passwordField.requestFocus();
		setVisible(true);
	}

	public static RecordFile getDisplayedRecordFile(){
		RecordFile rf = new RecordFile();
		for(RecordPanel rp : recordPanels){
			rf.records.add(rp.toRecord());
		}
		return rf;
	}
	
	private static void redisplay(){
		contentPane.remove(scrollPane);
		contentPane.remove(communicationPanel);
		instance.initRecordsPanel();
		//Loop through each RecordPanel and redisp
		for(RecordPanel recordPanel : recordPanels){
			recordsPanel.add(recordPanel);
			recordsPanel.add(Sys.space(3));
		}
		recordsPanel.add(addRecordButton);
		contentPane.add(scrollPane);
		contentPane.add(communicationPanel);
		instance.pack();
		instance.revalidate();
	}
	
	public static void display(RecordFile rf){
		recordPanels.clear();
		contentPane.remove(scrollPane);
		contentPane.remove(communicationPanel);
		instance.initRecordsPanel();
		//Loop through each record and create a panel for each
		for(Record record : rf.records){
			RecordPanel recordPanel = instance.new RecordPanel(record);
			
			recordsPanel.add(recordPanel);
			recordsPanel.add(Sys.space(3));
			recordPanels.add(recordPanel);
		}
		recordsPanel.add(addRecordButton);
		contentPane.add(scrollPane);
		contentPane.add(communicationPanel);
		instance.pack();
	}
	
	private void initWindow(){
		try {
			UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLocation(200, 100);
		this.setResizable(false);
		this.setTitle("Pyraecords");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		this.setContentPane(contentPane);
	}
	
	private void initRecordsPanel(){
		recordsPanel = new JPanel();
		recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));
		recordsPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));
		addRecordButton = new JButton("+");
		addRecordButton.addActionListener((e) -> {
			recordsPanel.remove(addRecordButton);
			Record newRecord = new Record();
			newRecord.title = "NewRecord";
			newRecord.data.put("Username", "User");
			newRecord.data.put("Password", "pw");
			RecordPanel rp = new RecordPanel(newRecord);
			recordPanels.add(rp);
			recordsPanel.add(rp);
			recordsPanel.add(Sys.space(3));
			recordsPanel.add(addRecordButton);
			revalidate();
			repaint();
		});
		scrollPane = new JScrollPane(recordsPanel);
		scrollPane.setPreferredSize(new Dimension(500,700));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		contentPane.add(scrollPane);
	}
	
	private static void checkClearPassword(){
		if(!pwClicked){
			passwordField.setText("");
			pwClicked = true;
		}
	}
	
	private void initCommunicationPanel(){
		communicationPanel = new JPanel();
		communicationPanel.setLayout(new BoxLayout(communicationPanel, BoxLayout.Y_AXIS));
		communicationPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
		
		JPanel commButtonPanel = new JPanel();
		commButtonPanel.setLayout(new BoxLayout(commButtonPanel, BoxLayout.X_AXIS));
		
		loadButton = new JButton("Load");
		loadButton.addActionListener((e) -> {
			Client.startInputClient();
		});
		commButtonPanel.add(loadButton);
		
		commButtonPanel.add(Sys.space());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener((e) -> {
			Client.startOutputClient();
		});
		commButtonPanel.add(sendButton);
		
		communicationPanel.add(commButtonPanel);
		
		passwordField = new JTextField("Enter password...");
		passwordField.setPreferredSize(new Dimension(200,35));
		passwordField.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				checkClearPassword();
			}
		});
		passwordField.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				checkClearPassword();
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					Client.startInputClient();
			}
		});
		communicationPanel.add(Sys.space());
		JPanel temp = new JPanel();
		temp.add(passwordField);
		communicationPanel.add(temp);
		
		contentPane.add(communicationPanel);
	}
	
	public static String passwordEntered(){
		return passwordField.getText();
	}
}
