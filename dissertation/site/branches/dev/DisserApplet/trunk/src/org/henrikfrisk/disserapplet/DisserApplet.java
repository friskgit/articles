package org.henrikfrisk.disserapplet;

import java.io.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.applet.*;
import java.sql.Connection;
 
// the start of an applet - HelloWorld will be the executable class
// Extends applet means that you will build the code on the standard Applet class
public class DisserApplet extends JApplet
{

    String aString = "loaded";
    DBClient db = null;
    Connection conn = null;

    // The method that will be automatically called  when the applet is started
    public void init() {
        db = new DBClient();
        conn = db.connect();
        //        aString = db.go();         
    }
 
    // This method gets called when the applet is terminated
    // That's when the user goes to another page or exits the browser.
    public void stop()
    {
        // no actions needed here now.
    }
 

    // The standard method that you have to use to paint things on screen
    // This overrides the empty Applet method, you can't called it "display" for example.

    public void paint(Graphics g)
    {
        //method to draw text on screen
        // String first, then x and y coordinate.
        g.drawString("Hey hey hey hey",20,20);
        g.drawString(aString,20,40);
    }

    public String getArticleContent(int id) {
        return db.requestArticleContent(0);
    }
}
