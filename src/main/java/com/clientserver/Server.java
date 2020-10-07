package com.clientserver;

import org.json.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class listens a specific port then, when it receives a message, it writes it to a file and inserts it to a database according to its priority
 * @author Ismet Abaci
 */
public class Server {
    private final static Logger logger = Logger.getLogger(String.valueOf(Server.class));
    private static int port = 49999; //defauld = 49999

    /**
     * This method listens for a message on a port and process received messages
     */
    public static void main(String[] args) {
        try {
            logger.info("Server started listening (port:" + port + ")...");
            runServer();
        } catch (Exception e) {
            logger.warning("Error on Server-client conn -> " + e);
        }
    }

    /**
     * This method listens the given port as long as it runs. when it receives a message, it sends it to PriorityManager class to processes it
     */
    private static void runServer() throws IOException {
        boolean isOn = true;
        ServerSocket ss = new ServerSocket(port);
        PriorityManager priorityManager = new PriorityManager();
        priorityManager.setThreads();
        Socket s = ss.accept();

        while (isOn) {
            try{
                DataInputStream in = new DataInputStream(s.getInputStream());
                String input = in.readUTF();
                if (input != null) {
                    System.out.println(input);
                    priorityManager.manageMessage(new JSONObject(input));
                } else {
                    logger.warning("Error on server -> Received message is null");
                }
            }catch (Exception e ){
                s = ss.accept();
            }

        }
    }

}
