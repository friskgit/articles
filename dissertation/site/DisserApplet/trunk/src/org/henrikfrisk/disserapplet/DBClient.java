package org.henrikfrisk.disserapplet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

public class DBClient
{

    public static final String DERBY_CLIENT_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    private static String DBNAME="DerbyDisserDB";
	private static int NETWORKSERVER_PORT=1527;
    private static final String DERBY_CLIENT_URL= "jdbc:derby://localhost:"+ NETWORKSERVER_PORT+"/"+DBNAME+";create=true";
    /* the default framework is embedded*/
    private String framework = "derbyclient";
    private String driver = "org.apache.derby.jdbc.ClientDriver";
    private String protocol = "jdbc:derby://localhost:1527/";
    private Connection conn = null;

//     public static void main(String[] args)
//     {
//         new DBClient().go();
//         System.out.println("DBClient finished");
//     }

    public DBClient() {
        loadDriver();
    }

    public Connection connect() {
        try {
            conn = getClientDriverManagerConnection();
            return conn;
        }
        catch(Exception e) {
            return null;
        }
    }

    public String requestArticleContent(int index) {
        
        Statement s = null;
        ResultSet rs = null;
        String content = "";
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM articles WHERE id="+index);
            while(rs.next()) {
                content = rs.getString("contents");
            }
        }
        catch(Exception e) {
            content = e.toString();
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(s != null)
                    s.close();
            }
            catch(Exception e) {
                content = e.toString();
            }
        }
        return content;
    }

    public String performTest()
    {

        loadDriver();

        Connection conn = null;
        String dbName = DBNAME;
        Statement s = null;
        ResultSet rs = null;

        String title = "";
        try {

            /*
             * This connection specifies create=true in the connection URL to
             * cause the database to be created when connecting for the first
             * time. To remove the database, remove the directory derbyDB (the
             * same as the database name) and its contents.
             *
             * The directory derbyDB will be created under the directory that
             * the system property derby.system.home points to, or the current
             * directory (user.dir) if derby.system.home is not set.
             */
            try {
                conn = getClientDriverManagerConnection();
            }
            catch(Exception e) {
                System.out.println("Error");
            }
            System.out.println("Connected to database " + dbName);

            /* Creating a statement object that we can use for running various
             * SQL statements commands against the database.*/

            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM articles");
            while(rs.next()) {
 //                System.out.println("Id: "+rs.getString("article_id")+"\nTitle: "+ rs.getString("title")+"\nContent: "+rs.getString("contents"));
                title = rs.getString("title");
            }
            rs.close();
            s.close();
            test(conn);
        }
        catch (SQLException sqle) {
            printSQLException(sqle);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
        finally {
            return title;
        }
    }    
    	/**
	 * Get a client connection using the DriverManager
	 * @pre The JDBC driver must have been loaded before calling this method
	 * @return Connection	client database connection
	 */
	public Connection getClientDriverManagerConnection()
		throws Exception
	{

		// See Derby documentation for description of properties that may be set
		//  in the context of the network server.
		Properties properties = new java.util.Properties();

		// The user and password properties are a must, required by JCC
		properties.setProperty("user","APP");
        //		properties.setProperty("password","");

		// Get database connection  via DriverManager api
		Connection conn = DriverManager.getConnection(DERBY_CLIENT_URL,properties); 

		return conn;
	}
    /**
     * Loads the appropriate JDBC driver for this environment/framework. For
     * example, if we are in an embedded environment, we load Derby's
     * embedded Driver, <code>org.apache.derby.jdbc.EmbeddedDriver</code>.
     */
    private void loadDriver() {
        /*
         *  The JDBC driver is loaded by loading its class.
         *  If you are using JDBC 4.0 (Java SE 6) or newer, JDBC drivers may
         *  be automatically loaded, making this code optional.
         *
         *  In an embedded environment, this will also start up the Derby
         *  engine (though not any databases), since it is not already
         *  running. In a client environment, the Derby engine is being run
         *  by the network server framework.
         *
         *  In an embedded environment, any static Derby system properties
         *  must be set before loading the driver to take effect.
         */
        try {
            Class.forName(DERBY_CLIENT_DRIVER).newInstance();
            System.out.println("Loaded the appropriate jdbcDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("\nUnable to load the JDBC driver " + DERBY_CLIENT_DRIVER);
            System.err.println("Please check your CLASSPATH.");
            cnfe.printStackTrace(System.err);
        } catch (InstantiationException ie) {
            System.err.println(
                               "\nUnable to instantiate the JDBC driver " + DERBY_CLIENT_DRIVER);
            ie.printStackTrace(System.err);
        } catch (IllegalAccessException iae) {
            System.err.println(
                               "\nNot allowed to access the JDBC driver " + DERBY_CLIENT_DRIVER);
            iae.printStackTrace(System.err);
        }
    }

    /**
     * Reports a data verification failure to System.err with the given message.
     *
     * @param message A message describing what failed.
     */
    private void reportFailure(String message) {
        System.err.println("\nData verification failed:");
        System.err.println('\t' + message);
    }

    /**
     * Prints details of an SQLException chain to <code>System.err</code>.
     * Details included are SQL State, Error code, Exception message.
     *
     * @param e the SQLException from which to print details.
     */
    public static void printSQLException(SQLException e)
    {
        // Unwraps the entire exception chain to unveil the real cause of the
        // Exception.
        while (e != null) {
            System.err.println("\n----- SQLException -----");
            System.err.println("  SQL State:  " + e.getSQLState());
            System.err.println("  Error Code: " + e.getErrorCode());
            System.err.println("  Message:    " + e.getMessage());
            // for stack traces, refer to derby.log or uncomment this:
            //e.printStackTrace(System.err);
            e = e.getNextException();
        }
    }

	/**
	 * Test a connection by executing a sample query
	 * @param	conn 	database connection
	 * @throws Exception if there is any error
	 */
	public void test(Connection conn)
		throws Exception
	{

	  Statement stmt = null;
	  ResultSet rs = null;
	  try
	  {
		// To test our connection, we will try to do a select from the system catalog tables
		stmt = conn.createStatement();
		rs = stmt.executeQuery("select count(*) from sys.systables");
		while(rs.next())
			System.out.println("number of rows in sys.systables = "+ rs.getInt(1));

	  }
	  catch(SQLException sqle)
	  {
		  System.out.println("SQLException when querying on the database connection; "+ sqle);
		  throw sqle;
  	  }
  	  finally
  	  {
		  if(rs != null)
		  	rs.close();
		  if(stmt != null)
		  	stmt.close();
 	  }
	}
}