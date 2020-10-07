package com.clientserver;

import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;


/**
 * @author Ismet Abaci
 */
public class Database {

    private final static Logger logger = Logger.getLogger(String.valueOf(Database.class));
    private String url ;
    private String databaseName ;
    private String userName;
    private String password;
    private Connection connect;
    private boolean isConnected=false;

    /**
     * This contractor sets the db login credentials then it tries to connect it
     */
    public Database(String url,String databaseName,String userName, String password)  {
        this.url = url;
        this.databaseName = databaseName;
        this.userName=userName;
        this.password=password;
        connectToDatabase();
    }

    /**
     * This method inserts the messages into a database according to message's priority
     * @param msg This is the message we got from the user
     */
    public void insertIntoDatabase(JSONObject msg){

        try{
            String tableName = (String) msg.get("priority"); //get table name according to the message's priority
            if (tableName == null) {
                logger.warning("Priority can not be empty");
                return;
            }
            if(!isConnected){
                //connectToDatabase();
                if(!isConnected){
                    logger.warning("not connected to the database");
                    return;
                }
            }

            String sql = "insert into " + tableName + " (id,sender,receiver,subject,cc,message,priority) values (default, ?, ?, ?, ? , ?, ?);";
            PreparedStatement preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, msg.get("sender").toString());
            preparedStatement.setString(2, msg.get("receiver").toString());
            preparedStatement.setString(3, msg.get("subject").toString());
            preparedStatement.setString(4, msg.get("cc").toString());
            preparedStatement.setString(5, msg.get("message").toString());
            preparedStatement.setString(6, msg.get("priority").toString());
            preparedStatement.executeUpdate();

        }catch(Exception e){
            logger.warning("Error on database connection -> " + e);
        }
    }

    /**
     * This method asks for login credentials and tries to connect to the database
     * @return It returns the result of whether the connection is established or not
     */
    public boolean setDatabaseCredentials(String url,String databaseName,String userName, String password){
        this.url = url;
        this.databaseName = databaseName;
        this.userName=userName;
        this.password=password;
        connectToDatabase();
        return isConnected;
    }

    /**
     * Getter method for instance isConnected
     */
    public boolean getIsConnected(){
        return isConnected;
    }

    /**
     * This method tries to connect to the database, and it sets isConnected instance whether the connection is established or not
     */
    private void connectToDatabase(){

        try{
            connect = DriverManager.getConnection(this.url + this.databaseName +"?user="+ this.userName +"&password=" + this.password); //WARNING: I used xampp for the db Connection
            isConnected = true;

        } catch (SQLException e) {
            logger.warning("Error on sql connection -> " + e);
            isConnected = false;
        }
    }
}
