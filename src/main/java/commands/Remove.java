package commands;

import client.Client;
import hashmap.HashMapOperationProtos;
import org.jgroups.Message;

public class Remove implements Command {

    private final Client client;
    private String key;

    public Remove(Client client, String key) {
        this.client = client;
        this.key = key;
    }

    @Override
    public void execute() {
        HashMapOperationProtos.HashMapOperation operation;
        operation = HashMapOperationProtos.HashMapOperation.newBuilder()
                .setType(HashMapOperationProtos.HashMapOperation.OperationType.REMOVE)
                .setKey(key)
                .build();

        byte[] sendBuffer = operation.toByteArray();

        Message message = new Message(null, null, sendBuffer);

        try {
            client.getChannel().send(message);
        } catch (Exception e) {
            client.getChannel().close();
        }

        client.getMap().remove(key);
    }
}
