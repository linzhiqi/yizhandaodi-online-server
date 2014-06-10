package services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import server.Session;

import dao.AccountJDBCImpl;

public class OnlineListManager {
	private HashMap<String,Session> playerStatusList;
	private static OnlineListManager manager = new OnlineListManager();
	
	private OnlineListManager(){	
		playerStatusList = new HashMap<String,Session>();
	}

	public static OnlineListManager getInstance() {
		return OnlineListManager.manager;
	}
	
	public boolean hasPlayer(String uuid){
		return this.playerStatusList.containsKey(uuid);
	}
	
	public Session getPlayer(String uuid){
		return this.playerStatusList.get(uuid);
	}
	
	public boolean hasAccountId(long id){
		Set<String> set = playerStatusList.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			if((id+"").equals(it.next().split("-")[1])){
				return true;
			}
		}
		return false;
	}
	
	public void addPlayer(String uuid, Session player){
		this.playerStatusList.put(uuid, player);
	}
	
	public void removePlayer(String uuid){
		this.playerStatusList.remove(uuid);
	}
}
