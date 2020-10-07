package com.clientserver;

import org.json.JSONObject;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

/**
 * @author Ismet Abaci
 */
public class ServerThread extends Thread{
    final private static Logger logger = Logger.getLogger(String.valueOf(ServerThread.class));
    final private int CAPACITY = 10;
    private final Semaphore mutexForLineUpdate = new Semaphore(1);
    private final Semaphore mutexForQueue = new Semaphore(1);
    private int line=0,next=0;
    private Database db;
    private JSONObject [] msg = new JSONObject[CAPACITY];

    /**
     * This constructor locks the threads when initialized, so that they'd wait on a empty queue
     */
    public ServerThread () throws InterruptedException {
        db = new Database("jdbc:mysql://192.168.64.2:3306/","messages","root2","111");
        mutexForQueue.acquire();
    }

    /**
     * This method checks the queue and processes it
     */
    @Override
    public void run() {
        while(true){

            try{
                mutexForQueue.acquire();
                processTheMessage();
                mutexForLineUpdate.acquire();
                next = (next+1)%CAPACITY;
                line--;
                if(line!=0)// if there are more to process in the queue it will release so that thread won't wait. if there isn't any message in the queue it won't release the mutex, so that thread will wait
                    mutexForQueue.release();
                mutexForLineUpdate.release();

            }catch(Exception e){
                logger.warning("Error on thread acquire -> " + e);
            }
        }
    }

    /**
     * This method checks, is the queue full or not, if it is full then it waits. when it is not full, it adds a new message to the queue
     */
    public void addIntoQueue(JSONObject msg)  {
        while(line>=CAPACITY); // it waits when the capacity is full
        this.msg[next+line] = msg;
        try{
            mutexForLineUpdate.acquire();
            if(this.line==0)
                mutexForQueue.release();// it interrupt the thread if it was waiting on the mutex
            this.line++;
            mutexForLineUpdate.release();
        }catch(Exception e){
            logger.warning("Error on thread.addIntoQueue mutex lock -> " + e);
        }

    }

    /**
     * This method writes the message to a file according to message priority
     * @param m This is the message we got from the user
     */
    private static void writeMessageToFile(JSONObject m){
        String data = m.get("sender").toString() + "," + m.get("receiver").toString() + "," + m.get("subject").toString() + "," + m.get("cc").toString() + "," + m.get("message").toString() + "," + m.get("priority").toString();
        String fileName = m.get("priority").toString() + ".txt";

        try{
            FileWriter fw = new FileWriter(fileName,true);
            PrintWriter printWriter = new PrintWriter(fw);
            printWriter.println(data);
            printWriter.close();

        }catch(Exception e ){
            logger.warning("Error on server-fileWriter -> " + e);
        }
    }

    /**
     * This method does the processes for the given message
     */
    private void processTheMessage() {
        db.insertIntoDatabase(msg[next]);
        writeMessageToFile(msg[next]);
    }
}
