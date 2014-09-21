package ar.edu.itba.pod.mmxivii.florcha;

import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class App {
	public static final String TWEETS_PROVIDER_NAME = "tweetsProvider";
	public static final String GAME_MASTER_NAME = "gameMaster";


	private App() {
	}

	public static void main(String[] args)
	{
		//final GamePlayer gp = new GamePlayer("yo", "aquel");
		final GamePlayer gp_florcha = new GamePlayer("florcha", "Ma. Florencia Besteiro. Legajo 51117");
		final String hash = "florcha-pod-51117";
		System.out.println("empezando!");
		
		try {
			
			final Registry registry = LocateRegistry.getRegistry(args[0], 7242);
			//Obtengo el proveedor de tweets
			final TweetsProvider tweetsProvider = (TweetsProvider) registry.lookup(TWEETS_PROVIDER_NAME);
			//Obtengo el GameMaster (arbitro)
			final GameMaster gameMaster = (GameMaster) registry.lookup(GAME_MASTER_NAME);

			try {
				gameMaster.newPlayer(gp_florcha, hash);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			/*try {
				gameMaster.newPlayer(gp2, hash);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}*/

			/*final Status[] tweets = tweetsProvider.getNewTweets(gp, hash, 10);
			for (Status tweet : tweets) {
				System.out.println("tweet = " + tweet);
				gameMaster.tweetReceived(gp2, tweet);
			}

			for (int i = 0; i < 10; i++) {
				System.out.println("new tweets " + i);
				final Status[] newTweets = tweetsProvider.getNewTweets(gp, hash, 100);
				gameMaster.tweetsReceived(gp2, newTweets);
			}*/

		} catch (RemoteException | NotBoundException e) {
			System.err.println("App Error: " + e.getMessage());
			System.exit(-1);
		}
		System.out.println("Hola alumno " + gp_florcha + "!");
		
	}
}
