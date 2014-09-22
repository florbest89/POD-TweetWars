package gameClient;

import gameEssentials.GamePlayer;
import gameEssentials.Status;
import java.rmi.RemoteException;

import player.Player;
import gameEssentials.GameMaster;
import gameEssentials.TweetsProvider;

public class Game {

	private TweetsProvider tweetsProvider;
	private GameMaster gameMaster;
	private String clusterName;
	private final int top = 100;

	public Game(TweetsProvider tweetsProvider, GameMaster gameMaster,
			String clusterName) {
		this.tweetsProvider = tweetsProvider;
		this.gameMaster = gameMaster;
		this.clusterName = clusterName;
	}

	public void begin() {

		System.out.println("Empezo el juego!");
		
		//Creo al jugador
		Player player = new Player(new GamePlayer("florcha",
				"Ma. Florencia Besteiro. Legajo 51117"), "florcha-pod-51117",
				top);
		newGame(player);
		
		while (true) {
			
			//Obtengo tweets nuevos
			getNewTweets(player);
			//Los divulgo entre todos los jugadores
			publicize(player);
			//Si junte 100 tweets los chequeo
			checkTweets(player);
			//Si junte 100 tweets para notificar, lo hago
			notifyTweets(player);
			
					
		}
		
		

	}
	
	//Decidi no implementarlo por los riesgos que implican reportar tweets
	public void reportFake(Player player){
		
	}
	
	public void notifyTweets(Player player){
		
		Status[] tweetsToNotify = player.getCheckedTweets();
		
		if(tweetsToNotify == null){
			return;
		}
		
		try {
			gameMaster.tweetsReceived(player.getGamePlayer(), tweetsToNotify);
		} catch (RemoteException e) {
			System.out.println("No se pudieron reportar los tweets");
			e.printStackTrace();
		}
	}
	
	public void checkTweets(Player player){
		
		long[] tweetsToCheck = player.getTweetsToCheck();
		
		if(tweetsToCheck == null){
			return;
		}
		
		try {
			Status[] checked = tweetsProvider.getTweets(tweetsToCheck);
			player.addCheckedTweets(checked);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("No pudieron checkearse los tweets");
		}
				
	}
	
	public void newGame(Player player){
		try {
			gameMaster.newPlayer(player.getGamePlayer(), player.getHash());
			player.connectWithOthers(clusterName);
		} catch (RemoteException e) {
			System.out.println("No se pudo ingresar al jugador");
			e.printStackTrace();
		}
		
	}

	public void getNewTweets(Player player) {

		try {
			Status[] newTweets = tweetsProvider.getNewTweets(
					player.getGamePlayer(), player.getHash(), top);
			player.addNewTweets(newTweets);
		} catch (RemoteException e) {
			System.out.println("No se pudieron descargar los tweets");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void publicize(Player player) {
		player.sendTweetsToPlayers();
	}

}
