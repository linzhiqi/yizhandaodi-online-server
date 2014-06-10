package services;

import protocol.ClientSignal;


public class HeartBeatManager {

	private static HeartBeatManager manager = new HeartBeatManager();
	
	private HeartBeatManager(){
		
	}
	
	public static HeartBeatManager getInstance(){
		return manager;
	}

	public void heartBeatReceived(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}
}
