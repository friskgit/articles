package org.henrikfrisk.server.db;

import java.sql.*;
import org.apache.derby.drda.NetworkServerControl;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Class responsible for starting the data base server and check its
 * functionality. It creates the data base DerbyDisserDB (it it
 * doesn't already exist) and creates an embedded connection to it for
 * testing purposes, but aloso to use for inserting new data.
 * <p>
 * The performance of te embedded driver is superior to a network
 * client connection so this embedded connection is well suited for
 * inserting large chunks of data in the database.
 * <p>
 * It's only constructor starts up the server and throws an exception
 * if something went wrong.
 * <p>
 * This class draws on the SimpleNetworkServerSample included with the 
 * Apache Derby download (see {@link http://db.apache.org}).
 * 
 * @author 07 February 2008	Henrik Frisk	mail@henrikfrisk.com
 * @version 0.0.1
 */
public class DerbyServer {

    private static final String DBNAME="DerbyDisserDB";
	private static final int NETWORKSERVER_PORT=1527;
    private static final String DERBY_SERVER_EMBEDDED_URL= "jdbc:derby:"+DBNAME+";create=true";
    private static final String DERBY_EMBEDDED_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    private String dbName = DBNAME;
    private int serverPort = NETWORKSERVER_PORT;
    private static String serverEmbeddedUrl = DERBY_SERVER_EMBEDDED_URL;
    private static Connection conn = null;
    private static org.apache.derby.drda.NetworkServerControl server = null;

    /**
     * 
     * @throws Exception if there's an error starting the server.
     */
    public DerbyServer() {

        try {
            startServer();
            testServer();
            conn = establishConnection();
            conn.setAutoCommit(false);
        }
        catch(Exception e) {
            System.out.println("Server not created.");
        }
    }

    /**
     * By setting the property <code>derby.drda.startNetworkServer</code> to <code>true</code>
     * the server is started. An option is to use an instance of the
     * NetworkServerControl to start the server:
     * <code>
     * org.apache.derby.drda.NetworkServerControl server = new NetworkServerControl
     * server.start (null);
     * </code>
     * @throws Exception if an error was encountered in starting the server.
     */
    public static void startServer() 
        throws Exception {
        System.out.println("Trying to start the server.");
        System.setProperty("derby.drda.startNetworkServer","true");        
        Class.forName(DERBY_EMBEDDED_DRIVER).newInstance();
    }

    private static void testServer() 
        throws Exception {
        
        server = new NetworkServerControl();
        
        System.out.println("Testing if Network Server is up and running!");
        for (int i = 0; i < 10 ; i ++) {
            try {
                Thread.currentThread().sleep(1000);
                server.ping();
            }
            catch (Exception e) {
                System.out.println("Try #" + i + " " +e.toString());
                if (i == 9 ) {
                    System.out.println("Giving up trying to connect to Network Server!");
                    throw e;
                }
            }
        }
        System.out.println("Derby Network Server now running");
    }

    public static Connection establishConnection()
        throws Exception {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        /* Get a connection */
        conn = DriverManager.getConnection(serverEmbeddedUrl);

        /* Test the connection */
        try {
            // To test our connection, we will try to do a select from the system catalog tables
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select count(*) from sys.systables");
            while(rs.next())
                System.out.println("number of rows in sys.systables = "+ rs.getInt(1));
            
        }
        catch(SQLException sqle) {
            System.out.println("SQLException when querying on the database connection; "+ sqle);
            throw sqle;
        }
        finally {
            if(rs != null)
                rs.close();
            if(stmt != null)
                stmt.close();
            return conn;
        }
    }

    public static Connection getConnection() {
        if(conn != null)
            return conn;
        else return null;
    }
 
    /**
     * Closes the embedded connection and stops the server.
     *
     * @throws Exception in case something went wrong or if the server isn't running.
     */
    public void stopServer() {
        boolean gotSQLExc = false;
        try {
            if(conn != null)
                conn.close();
            System.out.println("Trying to shutdown the database server.");
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            System.out.println("Server has been stopped safely.");
        }
        catch (SQLException se)  {
            if ( se.getSQLState().equals("XJ015") ) {
                gotSQLExc = true;
            }
        }
        if (!gotSQLExc) {
            System.out.println("Database did not shut down normally");
        }  else  {
            System.out.println("Database shut down normally");
        }
    }

    public NetworkServerControl getServer()
    {
        if(server != null)
            return server;
        else return null;
    }

    /**
     * Test if the server is responding to a ping. Note: This method
     * only tries to <code>ping</code> once and failes at first
     * attempt if the server is not responding.
     */
    public boolean isRunning() {
        try {
            server.ping();
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
}