package org.henrikfrisk.disserdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Clob;

import java.util.ArrayList;
import java.util.Properties;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.henrikfrisk.disserdb.xmlrpc.RpcServer;
/**
 * <p> The starting point for the server application that responds to
 * XML-RPC requests and pulls data out of the (embedded) Derby
 * database. This program also works as a utility for adding and
 * updating the database.
 * <p>
 *
 * As default the database starts up and runs on localhost on port
 * 1527. Though it is possible to connext to it on this port the
 * program is designed to serve clients through XML-RPC requests
 * rather than SQL queries. Rather than utilizing the network
 * interface the embedded driver is accessing the database directly,
 * resulting in better performance.  <p>
 * 
 * The minimal seb server (an instance of the <code>org.apache.xmlrpc.webserver.WebServer</code> 
 * starts up and runs on localhost port 8080. It is through 
 * requests on <code>http://127.0.0.1:8080/xmlrpc</code> 
 * that a client can aquire information from the database. All access
 * is restricted to the proxy interfaces defined in the package
 * <code>org.henrikfrisk.disserdb.xmlrpc.proxy</code>. When a client
 * imports this package (disser-proxy.jar) it gets access to all the
 * methods through the
 * <code>org.apache.xmlrpc.client.util.ClientFactory</code>.
 *
 * @author 12 February 2008	Henrik Frisk	mail@henrikfrisk.com
 * @version 0.0.1
 */
public class MainInterface
{
    static private String ARTICLE_TABLE = "CREATE TABLE articles (article_id INT NOT NULL, title VARCHAR(512), contents CLOB, created TIMESTAMP, updated TIMESTAMP, tab_id VARCHAR(20), PRIMARY KEY (article_id))";

    /* the default framework is embedded*/
    private String driver = "org.apache.derby.jdbc.ClientDriver";
    private String protocol = "jdbc:derby://localhost:1527/";
    private String dbName = "derbyDB";

    /**
     * Starting point of the application. Calling main() brings up the
     * database server and then the XML-RPC server. After both servers
     * have been initialized and their functionality tested the
     * program goes into interaction mode waiting for commands or
     * external connections.
     */
    public static void main(String[] args) throws IOException {

        String db = "DerbyDisserDB";
        MainInterface minter = new MainInterface();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Connection conn = null;
        
        /*
         * Instantiates and starts up the database server.
         * Performs initial tests to make sure databas is online.
         */
        DerbyServer s = new DerbyServer();    

        RpcServer rpcServer = new RpcServer();
        
        rpcServer.startServer();

        if(s.isRunning()) {
            //            minter.connect(db);
            conn = s.getConnection();
            minter.createTables(conn);
            try {
                minter.insertArticle(0, 
                                     "test", 
                                     new InputStreamReader(new FileInputStream("/home/henrikfr/test.html")),
                                     new Date(System.currentTimeMillis()),
                                     new Date(System.currentTimeMillis()),
                                     "1,2,3",
                                     conn);
            }
            catch(FileNotFoundException fnfe) {
                System.out.println("No file found.");
            }
        }
        else System.out.println("We are not running");

        System.out.println("Clients can continue to connect: ");
        System.out.println("Print 'exit' to stop Server");
        System.out.print("\n\n> ");

        //        minter.getArticle(0, conn);
        System.out.println(minter.getSubArticle(0, 1, 110));
        while(!(input.readLine().equalsIgnoreCase("exit")))
            ;
        System.out.flush();
        
        /*
         * Stop the RPC server
         */
        rpcServer.stopServer();
        
        /*
         * Stop the database server.
         */
        minter.dropTable("articles", conn);
        
        s.stopServer();
        conn = null;
        s = null;
        System.out.println("DisserDB finished");
        System.exit(0);
    }

    public void insertArticle(int id,
                              String title,
                              Reader content,
                              Date created,
                              Date updated,
                              String tag_id,
                              Connection conn) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO articles VALUES(?, ?, ?, ?, ?, ?)");
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setClob(3, content);
            ps.setDate(4, created);
            ps.setDate(5, updated);
            ps.setString(6, tag_id);
            ps.executeUpdate();
            ps.close();
            conn.commit();
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
        }
        System.out.println("Inserted one row in table.");
    }
    
    public void getArticle(int id, Connection conn) {
        try {
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM articles WHERE article_id = "+id);
            System.out.println("SELECT * FROM articles WHERE id = "+id);
            while(rs.next()) {
                System.out.println("Id: "+rs.getString("article_id")+"\nTitle: "+ rs.getString("title")+"\nContent: "+rs.getString("contents"));
            }
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
        }
    }

    public String getSubArticle(int id, long position, int length) {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        //        Clob clob = null;
        String result = "hej";
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM articles WHERE article_id="+id);
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("contents");
                //                System.out.println("length = " + aclob.length());
                if(aclob.length() > position + length)
                    result = aclob.getSubString(position, length);
//                 java.io.InputStream ip = rs.getAsciiStream("contents");
//                 int c = ip.read();
//                 while (c > 0) {
//                     System.out.print((char)c);
//                     c = ip.read();
//                 }
//                 System.out.print("\n");
//                 System.out.flush();
                aclob.free();
            }
 //             if(clob.length() > position + length) {
//                 return clob.getSubString(position, length);
//             }
//             else return clob.getSubString(position, (int)clob.length());
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
            result = sqle.toString();
        }
//         catch(IOException ioe) {
//             System.out.println(ioe.toString());
//         }
        finally {
            try {
                conn.commit();
//                 if(clob != null)
//                     clob.free();
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                printSQLException(sqle);
            }
        }
        return result;
    }

    public void updateData(PreparedStatement ps) {

    }

    private void createTables(Connection conn) {
        Statement s = null;
        try {
            s = conn.createStatement();
            /* articles */
            s.execute(ARTICLE_TABLE);
            conn.commit();
            System.out.println("Table created.");
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
        }
        finally {
            try {
                if(s != null)
                    s.close();
            }
            catch(SQLException sqle) {
                System.out.println("Error trying to close the statement.");
            }
        }
    }

    private void dropTable(String tableName, Connection conn) {
        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE "+tableName);
            conn.commit();
            System.out.println("Table "+tableName+" deleted.");
            s.close();
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
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
        while (e != null)
            {
                System.err.println("\n----- SQLException -----");
                System.err.println("  SQL State:  " + e.getSQLState());
                System.err.println("  Error Code: " + e.getErrorCode());
                System.err.println("  Message:    " + e.getMessage());
                // for stack traces, refer to derby.log or uncomment this:
                //e.printStackTrace(System.err);
                e = e.getNextException();
            }
    }
}