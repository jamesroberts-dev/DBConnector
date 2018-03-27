import android.os.StrictMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * REQUIRES JAR Dependency: mysql-connector-java-3.0.17-ga-bin.jar
 *
 * Class that provides the functionality to connect to a remote
 * MySQL DB server.
 *
 * Created by James Roberts.
 *
 */
public class DBConnector {

    //CONNECTION DETAILS: These details specify the database to connect to.
    private String url;
    private String username;
    private String password;

    //Initialize Connection and Statement.
    private Connection connection = null;
    private Statement statement = null;

    /**
     * Constructor that initializes the object and calls a method that connects to the database.
     */
    public DBConnector(String url, String username, String password) {
      //Populate variables with provided credentials
      this.url = url;
      this.username = username;
      this.password = password;

      connectToDB();
    }

    /**
     * Method that makes the connection to the database using the provided
     * details under 'Connection Details' above.
     * PRE: Connection has not been made and is set to null.
     *      Statement is still null.
     * POST: Connection is made and is initialised with the provided connection
     *       details.
     *       Statement is created on the created Connection.
     *
     * Should also be called again in the onResume() method in the Activity.
     */
    public void connectToDB() {
        //Set the current thread to run in Strict Mode
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //Load the JDBC driver from the .jar file
            Class.forName("com.mysql.jdbc.Driver");
            //Connect to the database using provided details
            connection = DriverManager.getConnection(url, username, password);
            //Set up the statement on the connection provided
            statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that executes a query on database and returns a ResultSet.
     * @param sql String containing the SQL Statement you want to perform.
     * @return ResultSet from the executed SQL statement.
     * SQL operations allowed:
     *         SELECT.
     */
    public ResultSet Query(String sql) {
        try {
            //Execute the SQL statement and return the results
            statement = connection.createStatement();
            return statement.executeQuery(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that executes a SQL statement on the database.
     * Note: This method does NOT return a ResultSet.
     * SQL Operations allowed:
     *      INSERT, UPDATE, CREATE, DROP, ALTER.
     * @param sql String containing the SQL Statement you want to perform.
     */
    public void Execute(String sql) {
        try {
            //Create the statement on the connection
            statement = connection.createStatement();

            //Perform the SQL statement on the database
            statement.execute(sql);

            //Close the statement
            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the connection to the Database.
     * Call in the onPause() method of the Activity.
     */
    public void disconnectDB() {
        try {
            //Close connection
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
