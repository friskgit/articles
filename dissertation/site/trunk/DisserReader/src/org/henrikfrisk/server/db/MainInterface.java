package org.henrikfrisk.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

import org.henrikfrisk.server.db.xmlrpc.RpcServer;
import org.henrikfrisk.server.db.jetty.DisserServer;
import org.henrikfrisk.client.SectionInstance;

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

    private static MainInterface minter = null;
    /**
     * Starting point of the application. Calling main() brings up the
     * database server and then the XML-RPC server. After both servers
     * have been initialized and their functionality tested the
     * program goes into interaction mode waiting for commands or
     * external connections.
     */
    public static void main(String[] args) throws IOException {

        String db = "DerbyDisserDB";
        minter = new MainInterface();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Connection conn = null;
        
        /*
         * Instantiates and starts up the database server.
         * Performs initial tests to make sure databas is online.
         */
        DerbyServer s = new DerbyServer();    

        DisserServer d = new DisserServer();
        try {
            d.start();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }

//         RpcServer rpcServer = new RpcServer();
        
//         rpcServer.startServer();

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
        //        System.out.println(minter.getSectionTitle());
        //        System.out.println(minter.getSubArticle(0, 1, 110));
        //        minter.testSections(0);
        //        System.out.println(minter.getSection(0));
        //        System.out.println(minter.getAbstract(0));
        while(!(input.readLine().equalsIgnoreCase("exit")))
            ;
        System.out.flush();
        
        /*
         * Stop the RPC server
         */
//         rpcServer.stopServer();
        try {
            d.stop();
        }
        catch(Exception e) {
            System.out.println(e.toString());
        }
       
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


    public static MainInterface getMainInterface() {
        return minter;
    }

    public String testString() {
        return "Fuck you.";
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

    public String getAbstract(int id) {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        StringBuffer result = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT abstract FROM chapters WHERE id="+id);
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("abstract");
                reader = new BufferedReader(aclob.getCharacterStream());
                try {
                    while((line = reader.readLine()) != null)
                        result.append(line).append(" ");
                    reader.close();
                }
                catch(IOException ioe) {
                    System.out.println(ioe.toString());
                }
                aclob.free();
            }
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
            result.append(sqle.toString());
        }
        finally {
            try {
                conn.commit();
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                printSQLException(sqle);
            }
        }
        return result.toString();
    }

    public String getSection(int id) {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        StringBuffer result = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT content FROM sections WHERE id="+id);
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("content");
                reader = new BufferedReader(aclob.getCharacterStream());
                try {
                    while((line = reader.readLine()) != null)
                        result.append(line).append(" ");
                    reader.close();
                }
                catch(IOException ioe) {
                    System.out.println(ioe.toString());
                }
                aclob.free();
            }
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
            result.append(sqle.toString());
        }
        finally {
            try {
                conn.commit();
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                printSQLException(sqle);
            }
        }
        return result.toString();
    }

    public String getSection(String fileName) {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        StringBuffer result = new StringBuffer();
        String line = null;
        BufferedReader reader = null;
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT content FROM sections WHERE url LIKE '"+fileName+"%'");
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("content");
                reader = new BufferedReader(aclob.getCharacterStream());
                try {
                    while((line = reader.readLine()) != null)
                        result.append(line).append(" ");
                    reader.close();
                }
                catch(IOException ioe) {
                    System.out.println(ioe.toString());
                }
                aclob.free();
            }
        }
        catch(SQLException sqle) {
            printSQLException(sqle);
            result.append(sqle.toString());
        }
        finally {
            try {
                conn.commit();
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                printSQLException(sqle);
            }
        }
        return result.toString();
    }

    public String getSubArticle(int id, long position, int length) {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        String result = "";
        int l = length;
        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM chapters");
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("abstract");
                result = "Before test";
                result = aclob.getSubString((long)1, (int)aclob.length());
//                 if(aclob.length() > position + l) {
//                     result = aclob.getSubString(position, l);
//                     result = result + " Test OK";
//                 }
                aclob.free();
            }
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


    public String getSectionTitle() {
        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        String result = null;

        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT id, title FROM sections WHERE id=0");
            while(rs.next()) {
                    result = rs.getString("title");
            }
        }
        catch(SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            try {
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                System.out.println(sqle.toString());
            }
        }
        return result;
    }

    public void testSections(int chapterId) {
        Connection conn = DerbyServer.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        SectionInstance[] sections = null;
        String sqlString = "SELECT * FROM sections WHERE chapter_id="+chapterId;
        int i = 0;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sqlString);
            rs.last(); 
            
            // Get the row position which is also the number of rows in the ResultSet.
            sections = new SectionInstance[rs.getRow()]; 
            
            //        System.out.println("Total rows for the query: "+rowcount);
            // Reposition at the beginning of the ResultSet to take up rs.next() call.
            rs.beforeFirst();
            while(rs.next()) {
                sections[i] = new SectionInstance(rs.getInt("id"), 
                                                  rs.getInt("chapter_id"), 
                                                  null,
                                                  rs.getDate("created").toString(),
                                                  rs.getDate("updated").toString(),
                                                  rs.getString("tag_id"),
                                                  rs.getString("title"),
                                                  rs.getString("title_as_html"),
                                                  rs.getString("url"),
                                                  0);
                i++;
            }
        }
        catch(SQLException e) {
            System.out.println(e.toString());
        }
        finally {
            try {
                if(stmt != null)
                    stmt.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                System.out.println(sqle.toString());
            }
        }
        for(int j = 0; j<sections.length; j++)
            System.out.println(sections[j].title);
            
        //        return sections;
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