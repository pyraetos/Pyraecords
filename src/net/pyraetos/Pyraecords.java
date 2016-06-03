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
	}
	
	public Pyraecords(){
		initWindow();
		initRecordsPanel();
		initCommunicationPanel();
		
		pack();
		setVisible(true);
		
		Client.startInputClient();
	}

	public static RecordFile getDisplayedRecordFile(){
		RecordFile rf = new RecordFile();
		for(RecordPanel rp : recordPanels){
			rf.records.add(rp.toRecord());
		}
		return rf;
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
			Client.startInputClient();
		});
		communicationPanel.add(loadButton);
		
		communicationPanel.add(Sys.space());
		
		sendButton = new JButton("Send");
		sendButton.addActionListener((e) -> {
			Client.startOutputClient();
		});
		communicationPanel.add(sendButton);
		
		contentPane.add(communicationPanel);
	}
}
