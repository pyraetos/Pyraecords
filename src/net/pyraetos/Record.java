package net.pyraetos;

import java.util.HashMap;

public class Record {

	public String title;
	public HashMap<String, String> data = new HashMap<String, String>();
	
	public int size(){
		return data.size();
	}
	
}
