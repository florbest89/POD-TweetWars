package ar.edu.itba.pod.mmxivii.florcha;

//import gameEssentials.GamePlayer;
//import gameEssentials.Status;
import java.rmi.RemoteException;

import player.Player;
import ar.edu.itba.pod.mmxivii.tweetwars.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;
//import gameEssentials.GameMaster;
import ar.edu.itba.pod.mmxivii.tweetwars.TweetsProvider;

//import gameEssentials.TweetsProvider;

public class Game {

	private TweetsProvider tweetsProvider;
	private GameMaster gameMaster;
	private String clusterName;
	private static int top = 100;

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
		//Lo conecto al Game Master
		Player player2 = new Player(new GamePlayer("muni","Muni Milo Aio RIni"),"gorditos",top);
		newGame(player);
		newGame(player2);
		
		int count = 200;
		while (count != 0) {
			
			//Obtengo tweets nuevos
			getNewTweets(player);
			//Los divulgo entre todos los jugadores
			publicize(player);
			//Si junte 100 tweets los chequeo
			checkTweets(player);
			//Si junte 100 tweets para notificar, lo hago
			notifyTweets(player);
			
			//PARA PRUEBA
			//Obtengo tweets nuevos
			getNewTweets(player2);
			//Los divulgo entre todos los jugadores
			publicize(player2);
			//Si junte 100 tweets los chequeo
			checkTweets(player2);
			//Si junte 100 tweets para notificar, lo hago
			notifyTweets(player2);
			
			count--;
			
		}
		
		try {
			System.out.println("Puntaje de " + player.getGamePlayer().getId() + ", es " + gameMaster.getScore(player.getGamePlayer()));
			System.out.println("Puntaje de " + player2.getGamePlayer().getId() + ", es " + gameMaster.getScore(player2.getGamePlayer()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			int count = gameMaster.tweetsReceived(player.getGamePlayer(), tweetsToNotify);
			System.out.println("notifique " + count + " tweets");
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
