package org.henrikfrisk.server.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Clob;
import java.sql.Connection;
import java.util.Vector;

import org.henrikfrisk.client.ArticleService;
import org.henrikfrisk.client.SectionInstance;
import org.henrikfrisk.client.ChapterInstance;
import org.henrikfrisk.server.db.MainInterface;
import org.henrikfrisk.server.db.DerbyServer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ArticleServiceImpl extends RemoteServiceServlet 
    implements ArticleService {

	public String getArticleSection() {
        return MainInterface.getMainInterface().getSubArticle(0, 1, 1000);
    }

    public ChapterInstance[] getChapterInstances() {
        Connection conn = DerbyServer.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        ChapterInstance[] chapters = null;
        String sqlString = "SELECT * FROM chapters";
        int i = 0;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sqlString);
            rs.last(); 
            chapters = new ChapterInstance[rs.getRow()]; 
            rs.beforeFirst();
            while(rs.next()) {
                chapters[i] = new ChapterInstance(rs.getInt("id"), 
                                                  null,
                                                  rs.getDate("created").toString(),
                                                  rs.getDate("updated").toString(),
                                                  rs.getString("tag_id"),
                                                  rs.getString("title"),
                                                  rs.getString("url"));
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
        return chapters;
    }

    public SectionInstance[] getSectionInstances(int chapterId) {
        Connection conn = DerbyServer.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        SectionInstance[] sections = null;
        String sqlString = "SELECT * FROM sections WHERE chapter_id="+chapterId+" ORDER BY section_index";
        int i = 0;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sqlString);
            rs.last(); 
            sections = new SectionInstance[rs.getRow()]; 
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
                                                  rs.getInt("section_index"));
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
        return sections;
    }

    public String getSectionTitle(int id) {
        return MainInterface.getMainInterface().getSectionTitle();
    }

    public String getSectionContent(int id) {
        return MainInterface.getMainInterface().getSection(id);
    }

    public String getSectionContent(String fileName) {
        return MainInterface.getMainInterface().getSection(fileName);
    }

    public String getChapterAbstract(int id) {
        return MainInterface.getMainInterface().getAbstract(id);
    }
}

