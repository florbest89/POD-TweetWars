package gameClient;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;



public class ClientApp {
	
	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";
	public static String groupsClusterName;


	private ClientApp() {
	}

	public static void main(String[] args)
	{
		
		groupsClusterName = args[1];
		
		//final GamePlayer gp = new GamePlayer("yo", "aquel");
		
		try {
			
			final Registry registry = LocateRegistry.getRegistry(args[0], 7242);
			//Obtengo el proveedor de tweets
			final TweetsProvider tweetsProvider = (TweetsProvider) registry.lookup(TWEETS_PROVIDER_NAME);
			//Obtengo el GameMaster (arbitro)
			final GameMaster gameMaster = (GameMaster) registry.lookup(GAME_MASTER_NAME);
			
			final Game game = new Game(tweetsProvider,gameMaster,groupsClusterName);
			game.begin();


		} catch (RemoteException | NotBoundException e) {
			System.err.println("App Error: " + e.getMessage());
			System.exit(-1);
		}
		
		
	}
	
	
}
