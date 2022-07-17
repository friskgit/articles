package org.henrikfrisk.client.proxy;

import org.henrikfrisk.disserdb.DerbyServer;
import org.henrikfrisk.disserdb.MainInterface;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Clob;

public class ArticleServiceImpl extends RemoteServiceServlet 
    implements ArticleService {

	public String getArticleSection() {

        Connection conn = DerbyServer.getConnection();
        Statement s = null;
        ResultSet rs = null;
        String result = "";
        int id = 0;
        long position = 110;

        try {
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM articles WHERE article_id="+id);
            while (rs.next()) {
                java.sql.Clob aclob = rs.getClob("contents");
                if(aclob.length() > position + length)
                    result = aclob.getSubString(position, length);
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
        finally {
            try {
                conn.commit();
                if(s != null)
                    s.close();
                if(rs != null)
                    rs.close();
            }
            catch(SQLException sqle) {
                result = sqle.toString();
            }
        }
        return result;
    }
}

