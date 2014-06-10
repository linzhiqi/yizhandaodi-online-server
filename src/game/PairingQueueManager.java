package game;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import server.Session;

public class PairingQueueManager {
	
	private LinkedBlockingQueue<Session> pairingPlayerQueue;
	private static PairingQueueManager manager = new PairingQueueManager();
	private Logger logger = Logger.getLogger(PairingQueueManager.class);
	private PairingQueueManager(){
		pairingPlayerQueue = new LinkedBlockingQueue<Session>();
	}
	

	public static PairingQueueManager getInstance() {
		// TODO Auto-generated method stub
		return manager;
	}
	
	public void addOneWaitingPlayer(Session player){
		this.pairingPlayerQueue.add(player);
		logger.info(player.getAccount().id + " is added to waitingList");
	}
	
	public Session removeWaitingPlayer() throws InterruptedException{
		Session ret = this.pairingPlayerQueue.take();
		logger.info(ret.getAccount().id + " is removed from waitingList");
		return ret;
	}
	
	public boolean isQueueEmpty(){
		return this.pairingPlayerQueue.isEmpty();
	}
	
}
