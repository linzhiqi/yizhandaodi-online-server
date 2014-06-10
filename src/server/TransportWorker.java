package server;

import game.Account;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import dao.AccountJDBCImpl;

import protocol.*;
import services.LoginManager;
import services.OnlineListManager;
import services.PasswordResetManager;
import services.RegistrationManager;

public class TransportWorker implements Runnable {
	private Socket socket;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private boolean stopFlag = false;
	private Session session = null;
	private Logger logger = Logger.getLogger(TransportWorker.class);

	public TransportWorker(Socket socket) throws IOException {
		this.socket = socket;
		os = new ObjectOutputStream(socket.getOutputStream());
		is = new ObjectInputStream(socket.getInputStream());

	}

	@Override
	public void run() {
		// doThings();
		while (true) {
			Object o = null;
			try {
				o = receive();
			} catch (EOFException e) {
				break;
			} catch (IOException e) {
				if (this.session != null) {
					logger.error("uid:" + session.getAccount().id
							+ " IOException when receive(). " + e.getMessage(),
							e);

				} else {
					logger.error(
							"uid:null IOException when receive(). "
									+ e.getMessage(), e);
				}
				break;
			} catch (ClassNotFoundException e) {
				if (this.session != null) {
					logger.error(
							"uid:"
									+ session.getAccount().id
									+ " ClassNotFoundException when receive(). "
									+ e.getMessage(), e);
				} else {
					logger.error(
							"uid:null ClassNotFoundException when receive(). "
									+ e.getMessage(), e);
				}
				break;
			}
			if (o instanceof ClientSignal) {
				if (o instanceof ClientLoginSignal) {
					serveLogin((ClientLoginSignal) o);
				} else if (o instanceof ClientRegistrationSignal) {
					serverRegistration((ClientRegistrationSignal) o);
				} else if (o instanceof ClientSendPasswordSignal) {
					serveSendPassword((ClientSendPasswordSignal) o);
				} else {
					dispatch((ClientSignal) o);
				}
			} else {
				// log the error, and the IP address, also the object content,
				// it might be an attack
			}
		}

		try {
			os.close();
			is.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException when closing port in run().", e);
		}

	}

	private void serveSendPassword(ClientSendPasswordSignal cSig) {
		ServerSendPasswordSignal sSig = PasswordResetManager.serveRegistration(
				cSig, new AccountJDBCImpl(), this.socket
						.getRemoteSocketAddress().toString());
		try {
			send(sSig);
		} catch (IOException e) {
			logger.error("serveSendPassword:" + e.getMessage(), e);
		}
	}

	private void serverRegistration(ClientRegistrationSignal cSig) {
		Account acc = new Account();
		// ServerLoginSignal sig = LoginManager.serveLogin(clientSig, new
		// AccountJDBCImpl(), acc);
		ServerRegistrationSignal sig = RegistrationManager.serveRegistration(
				cSig, new AccountJDBCImpl(), acc, this.socket
						.getRemoteSocketAddress().toString());
		if (acc != null) {
			Session session = new Session(this, acc);
			session.registerOnline();
			this.session = session;
			sig.sessionId = session.getSessionId();
		}
		try {
			send(sig);
		} catch (IOException e) {
			logger.error("serveRegistration:" + e.getMessage(), e);
		}

	}

	public void finalize() {
		this.stopFlag = true;
	}

	private void doThings() {

	}

	private void serveLogin(ClientLoginSignal clientSig) {
		Account acc = new Account();
		ServerLoginSignal sig = LoginManager.serveLogin(clientSig,
				new AccountJDBCImpl(), acc, this.socket
						.getRemoteSocketAddress().toString());

		if (acc != null) {
			Session session = new Session(this, acc);
			session.registerOnline();
			this.session = session;
			sig.setSessionId(session.getSessionId());
		}
		try {
			send(sig);
		} catch (IOException e) {
			logger.error("serveLogin:" + e.getMessage(), e);
		}
	}

	private void dispatch(ClientSignal sig) {
		String uuid = sig.getSessionId() + "-" + sig.getAccountId();
		// System.out.println("get sig from " + uuid);
		OnlineListManager playerList = OnlineListManager.getInstance();
		if (playerList.hasPlayer(uuid)) {
			Session session = playerList.getPlayer(uuid);
			this.session = session;
			session.setTransport(this);
			session.receive(sig);
		} else {
			logger.error("Playlist doesn't have this uid:" + uuid);
		}
	}

	public void send(ServerSignal signal) throws IOException {
		os.writeObject(signal);
	}

	public Object receive() throws IOException, ClassNotFoundException {
		return is.readObject();
	}

	public void closeConnections() {
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			logger.error("Couldn’t close socket:" + e.getMessage());
		}
	}
}
