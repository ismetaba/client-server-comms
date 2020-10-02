import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

/**
 * @author ismet abacı
 */
public class Database {
    final static Logger logger = Logger.getLogger(String.valueOf(MyThread.class));
    String url ;
    String databaseName ;
    String userName;
    String password;
    Connection connect;
    PreparedStatement preparedStatement;

    /**
     * This contractor sets the db login credentials
     */
    public Database(String url,String databaseName,String userName, String password){
        this.url = url;
        this.databaseName = databaseName;
        this.userName=userName;
        this.password=password;
    }

    /**
     * this method inserts the messages into a database according to message's priority
     * @param msg this is the message we got from the user
     */
    public void insertIntoDatabase(JSONObject msg){
        try{
            String tableName = (String) msg.get("priority"); //get table name according to the message's priority
            if (tableName == null) return;
            connect = DriverManager.getConnection(url + databaseName +"?user="+ userName +"&password=" + password); //WARNING: I used xampp for the db Connection
            String sql = "insert into " + tableName + " (id,sender,receiver,subject,cc,message,priority) values (default, ?, ?, ?, ? , ?, ?);";
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, msg.get("sender").toString());
            preparedStatement.setString(2, msg.get("receiver").toString());
            preparedStatement.setString(3, msg.get("subject").toString());
            preparedStatement.setString(4, msg.get("cc").toString());
            preparedStatement.setString(5, msg.get("message").toString());
            preparedStatement.setString(6, msg.get("priority").toString());
            preparedStatement.executeUpdate();
        }catch(Exception e){
            logger.warning("Error on client-db conn -> " + e);
        }
    }
}
