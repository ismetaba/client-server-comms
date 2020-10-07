package com.clientserver;

import org.json.JSONObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * @author Ismet Abaci
 */
public class PriorityManager {

    private final static Logger logger = Logger.getLogger(String.valueOf(Server.class));
    private final int numberOfThreads = 3;
    private final ServerThread[] serverThreads = new ServerThread[numberOfThreads];
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numberOfThreads);

    /**
     * This method reads the priority of the message and it puts the message to a thread's queue according to its priority
     */
    public void manageMessage(JSONObject msg)  {
        int threadIndex;

        threadIndex = getMessagePriority(msg.get("priority").toString());
        serverThreads[threadIndex].addIntoQueue(msg);//msg.get("priority") --> high=2, normal=1, low=0
    }

    /**
     * @return It returns an index number of the priority for the Thread array
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

    /**
     * This method starts threads and sets its priorities
     */
    public void setThreads() {

        try{
            for (int i=0;i<numberOfThreads;i++){
                serverThreads[i] = new ServerThread();
                executor.execute(serverThreads[i]);
            }
            serverThreads[2].setPriority(Thread.MAX_PRIORITY);
            serverThreads[1].setPriority(Thread.NORM_PRIORITY);
            serverThreads[0].setPriority(Thread.MIN_PRIORITY);

        }catch(Exception e){
            logger.warning("Error on setThread() -> " + e);
        }
    }
}
