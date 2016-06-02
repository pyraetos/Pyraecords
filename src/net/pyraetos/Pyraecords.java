package net.pyraetos;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.pyraetos.util.Sys;

@SuppressWarnings("serial")
public class Pyraecords extends JFrame{

	private static JPanel contentPane;
	
	private static JPanel recordsPanel;
	private static ArrayList<RecordPanel> recordPanels = new ArrayList<RecordPanel>();
	
	private static JPanel communicationPanel;
	private static JButton loadButton;
	private static JButton sendButton;
	
	private static Pyraecords instance;
	
	private class RecordPanel extends JPanel{
		
		private String title;
		private HashMap<String, JTextField> data = new HashMap<String, JTextField>();
		
		RecordPanel(Record record){
			setLayout(new GridLayout(record.size() + 1, 2, 5, 5));
			
			this.title = record.title;
			add(new JLabel(title));
			add(Sys.space());
			
			for(String title : record.data.keySet()){
				add(new JLabel(title));
				JTextField textField = new JTextField(record.data.get(title));
				add(textField);
				data.put(title, textField);
			}
		}
		
		Record toRecord(){
			Record result = new Record();
			result.title = this.title;
			for(String key : data.keySet())
				result.data.put(key, data.get(key).getText());
			return result;
		}
		
	}
	
	public static void main(String[] args) {
		instance = new Pyraecords();
		Record test1 = new Record();
		test1.title = "Facebook";
		test1.data.put("Username", "pyraetos");
		test1.data.put("Password", "watermelon2010");
		
		Record test2 = new Record();
		test2.title = "YouTube";
		test2.data.put("Channel Name", "NerdyCast");
		test2.data.put("Password", "BArFam3loN@018");
		
		RecordFile rf = new RecordFile();
		rf.records.add(test1);
		rf.records.add(test2);
		display(rf);
	}
	
	public Pyraecords(){
		initWindow();
		initRecordsPanel();
		initCommunicationPanel();
		
		pack();
		setVisible(true);
	}

	public static void display(RecordFile rf){
		recordPanels.clear();
		contentPane.remove(recordsPanel);
		contentPane.remove(communicationPanel);
		instance.initRecordsPanel();
		//Loop through each record and create a panel for each
		for(Record record : rf.records){
			RecordPanel recordPanel = instance.new RecordPanel(record);
			
			recordsPanel.add(recordPanel);
			recordsPanel.add(Sys.space(3));
			recordPanels.add(recordPanel);
		}
		contentPane.add(recordsPanel);
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
		contentPane.add(recordsPanel);
	}
	
	private void initCommunicationPanel(){
		communicationPanel = new JPanel();
		communicationPanel.setLayout(new BoxLayout(communicationPanel, BoxLayout.X_AXIS));
		
		loadButton = new JButton("Load");
		loadButton.addActionListener((e) -> {
			//Do stuff
		});
		communicationPanel.add(loadButton);
		
		communicationPanel.add(Sys.space());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener((e) -> {
			//Do stuff
		});
		communicationPanel.add(sendButton);
		
		contentPane.add(communicationPanel);
	}
}
