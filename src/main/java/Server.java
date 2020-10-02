import org.json.JSONObject;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * This class listens a specific port then, when it receives a message, it writes it to a file and inserts it to a database according to its priority
 * @author ismet abacı
 */
public class Server {
    final static Logger logger = Logger.getLogger(String.valueOf(Server.class));

    /**
     * this method listens for a message on a port and process received messages
     */
    public static void main(String[] args) {
        int port = 49999;
        try {
            logger.info("Server started listening (port:" + port + ")...");
            runServer(port);
        } catch (Exception e) {
            logger.warning("Error on Server-client conn -> " + e);
        }
    }

    /**
     * This method listens the given port as long as it runs. when it receives a message, it processes it
     */
    private static void runServer(int portNumber) throws IOException, InterruptedException {
        ServerSocket ss = new ServerSocket(portNumber);
        Socket s;
        BufferedReader in;
        JSONObject msg;
        String input;
        int numberOfThreads = 3,threadIndex;
        MyThread [] myThreads = new MyThread[numberOfThreads];

        for (int i=0;i<numberOfThreads;i++){
            myThreads[i] = new MyThread();
            myThreads[i].start();
        }
        myThreads[0].setPriority(Thread.MAX_PRIORITY);
        myThreads[1].setPriority(Thread.NORM_PRIORITY);
        myThreads[2].setPriority(Thread.MIN_PRIORITY);

        while (true) {
            s = ss.accept();
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            if ((input = in.readLine()) != null) {
                msg = new JSONObject(input);
                threadIndex = getMessagePriority(msg.get("priority").toString());
                myThreads[threadIndex].addIntoQueue(msg);//msg.get("priority") --> high=2, normal=1, low=0
            } else {
                logger.warning("Error on server -> Received message is null");
            }
        }
    }

    /**
     * @return it return an index number of the priority for the Thread array
     */
    private static int getMessagePriority(String priority) {
        switch (priority){
            case "high":
                return 2;
            case "normal":
                return 1;
            default:
                return 0;
        }
    }
}
