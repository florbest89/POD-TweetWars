package player;

//import gameEssentials.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

import ar.edu.itba.pod.mmxivii.tweetwars.GamePlayer;
import ar.edu.itba.pod.mmxivii.tweetwars.Status;

//import gameEssentials.GamePlayer;

public class Player {

	private GroupsNode node;
	private GamePlayer gp;
	private String hash;
	private int top;

	private Set<Status> checkedTweets;
	private List<Status> newTweets;
	// Guardo los id para checkear despues
	private long[] receivedTweets;
	private int receivedIndex;

	public Player(GamePlayer gameplayer, String hash, int top) {
		this.gp = gameplayer;
		this.hash = hash;
		this.top = top;
		node = new GroupsNode(gp.getId());
		node.setReceiver(new TweetReceiver());
		checkedTweets = new HashSet<Status>();
		newTweets = new ArrayList<Status>();
		receivedTweets = new long[top];
		receivedIndex = 0;

	}

	public String getHash() {
		return hash;
	}

	public GamePlayer getGamePlayer() {
		return gp;
	}

	public void connectWithOthers(String cluster) {
		node.connect(cluster);
	}

	public Status[] getCheckedTweets() {

		if (checkedTweets.size() == top) {
			Status[] resp = new Status[top];
			checkedTweets.toArray(resp);
			checkedTweets = new HashSet<Status>();
			return resp;
		}

		return null;

	}

	public void addCheckedTweets(Status[] validTweets) {

		int remaining = getRemaining();

		if (remaining == 0) {
			return;
		}

		List<Status> tweets = new ArrayList<Status>(Arrays.asList(validTweets));
		// Elimino los nulls (falsos)
		tweets.remove(null);
		if (tweets.size() < remaining) {
			checkedTweets.addAll(tweets);
		} else {
			checkedTweets.addAll(tweets.subList(0, remaining));
		}

	}

	public long[] getTweetsToCheck() {

		System.out.println("Mensajes recibidos " + receivedIndex);
		
		if (receivedIndex != top) {
			return null;
		}

		receivedIndex = 0;

		return receivedTweets;

	}

	public void addNewTweets(Status[] newTweets) {
		this.newTweets.addAll(Arrays.asList(newTweets));
	}

	private int getRemaining() {
		return top - checkedTweets.size();
	}

	public void sendTweetsToPlayers() {

		for (int i = 0; i < newTweets.size(); i++) {
			node.send(newTweets.get(i));
		}

		newTweets.clear();

	}

	private class TweetReceiver extends ReceiverAdapter {

		@Override
		public void receive(Message msg) {

			if (msg == null) {
				return;
			}

			if (msg.getSrc() != null && msg.getSrc().equals(node.getAddress())) {
				return;
			}

			
			if (receivedIndex == 100) {
				return;
			}

			Object msgObj = msg.getObject();
			
			//if (msgObj instanceof Status) {
				Status tweet = (Status) msgObj;
				System.out.println("Recibi un tweet");
				receivedTweets[receivedIndex] = tweet.getId();
				receivedIndex++;
			//}

		}

	}

}
