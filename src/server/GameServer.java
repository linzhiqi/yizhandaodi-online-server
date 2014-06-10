package server;

import game.PairingQueueManager;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import services.OnlineListManager;
import services.RankingManager;

public class GameServer {

	public static int LISTEN_PORT = 50001;
	private int serverPort = LISTEN_PORT;
	private ServerSocket serverSocket;
	private OnlineListManager onlineListManager;
	private PairingQueueManager pairingQueueManager;
	private RankingManager rankingManager;
	private Logger logger = Logger.getLogger(GameServer.class);
	
	public GameServer() {

		this.onlineListManager = OnlineListManager.getInstance();
		this.pairingQueueManager = PairingQueueManager.getInstance();
		this.rankingManager = RankingManager.getInstance();
		logger.info("game server start listening on port "
				+ LISTEN_PORT + " ...");
		try {
			// rankingManager need to load all account info from db, some heavy
			// workload might has problem
			this.rankingManager.init();
		} catch (Exception e) {
			logger.error("GameServer constructor:" + e.getMessage());
			System.exit(-1);
		}

		try {
			serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-2);
		}
	}

	public void startServe() {
		while (true) {
			try {
				Socket clientSock = this.serverSocket.accept();
				clientSock.setKeepAlive(true);
				clientSock.setSoTimeout(20*60*1000);
				new Thread(new TransportWorker(clientSock)).start();
			} catch (IOException e) {
				logger.error("startServe(): " + e.getMessage());
			}
		}
	}

	public static final void main(String[] args) {
		GameServer server = new GameServer();
		server.startServe();
	}
}
