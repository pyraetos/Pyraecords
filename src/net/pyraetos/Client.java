package net.pyraetos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.pyraetos.util.Sys;

public class Client extends Socket {

	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	private Client() throws Exception{
		super("pyraetos.net", 525);
		in = new BufferedInputStream(this.getInputStream());
		out = new BufferedOutputStream(this.getOutputStream());
	}
	
	public static Client startInputClient(){
		try {
			Client c = new Client();
			Sys.thread(c.new InputThread());
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Client startOutputClient(){
		try {
			Client c = new Client();
			Sys.thread(c.new OutputThread());
			return c;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private class InputThread implements Runnable{

		@Override
		public void run() {
			byte buf[] = new byte[4];
			try{
				in.read(buf);
				int flag = Sys.toInt(buf);
				while(flag != 0xcdcdcdcd){
					in.read(buf);
					flag = Sys.toInt(buf);
				}
				RecordFile recordFile = new RecordFile();
				recordLoop:
					while(true){
						Record record = new Record();
						in.read(buf);
						int size = Sys.toInt(buf);
						byte title[] = new byte[size];
						in.read(title);
						record.title = new String(title);
						while(true){
							in.read(buf);
							flag = Sys.toInt(buf);
							if(flag == 0xdddddddd)
								break;
							if(flag == 0xeeeeeeee)
								break recordLoop;
							size = flag;
							title = new byte[size];
							in.read(title);
							String key = new String(title);
							in.read(buf);
							size = Sys.toInt(buf);
							byte data[] = new byte[size];
							in.read(data);
							String value = new String(data);
							record.data.put(key, value);
						}
						recordFile.records.add(record);
					}
				Pyraecords.display(recordFile);
				in.close();
			}catch(Exception e){
				try{
					in.close();
					in = new BufferedInputStream(getInputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class OutputThread implements Runnable{

		@Override
		public void run() {
			try{
				RecordFile rf = Pyraecords.getDisplayedRecordFile();
				out.write(Sys.toBytes(0xcdcdcdcd));
				for(int i = 0; i < rf.records.size(); i++){
					Record record = rf.records.get(i);
					int length = record.title.length();
					out.write(Sys.toBytes(length));
					out.write(record.title.getBytes());
					for(String key : record.data.keySet()){
						length = key.length();
						out.write(Sys.toBytes(length));
						out.write(key.getBytes());
						length = record.data.get(key).length();
						out.write(Sys.toBytes(length));
						out.write(record.data.get(key).getBytes());
					}
					if(i != rf.records.size() - 1)
						out.write(Sys.toBytes(0xdddddddd));
					else
						out.write(Sys.toBytes(0xeeeeeeee));
				}
				out.flush();
				out.close();
			}catch(Exception e){
				try{
					in.close();
					in = new BufferedInputStream(getInputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
