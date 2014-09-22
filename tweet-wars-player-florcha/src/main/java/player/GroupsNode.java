package player;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class GroupsNode {

	private JChannel channel;

	public GroupsNode(String username) {
		try {
			channel = new JChannel();
			channel.setName(username);
		} catch (Exception e) {
			System.out.println("No se ha podido crear el channel.");
		}
	}

	public void connect(String clusterName) {
		try {
			channel.connect(clusterName);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No pudo conectarse a " + clusterName);
		}
	}

	public void disconnect() {
		channel.close();
	}

	public void send(Object message) {
		
		Message msg = new Message();
		msg = msg.setObject(message);
		
		try {
			channel.send(msg);
		} catch (Exception e) {
			System.out.println("No se ha podido enviar el mensaje");
			e.printStackTrace();
		};

	}
	
	Address getAddress(){
		return channel.getAddress();
	}
	
	public void setReceiver(ReceiverAdapter receiver){
		channel.setReceiver(receiver);
	}

}
