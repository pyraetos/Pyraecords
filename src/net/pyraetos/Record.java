package net.pyraetos;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Record implements Serializable{

	public String title;
	public HashMap<String, String> data = new HashMap<String, String>();
	
	public int size(){
		return data.size();
	}
	
}
