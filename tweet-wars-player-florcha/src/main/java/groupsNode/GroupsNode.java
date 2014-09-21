package groupsNode;

import org.jgroups.JChannel;
import org.jgroups.Message;

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
		Message groupMessage = new Message(channel.getAddress());
		try {
			channel.send(groupMessage.setObject(message));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("No se pudo enviar el mensaje");
		}

	}

}
