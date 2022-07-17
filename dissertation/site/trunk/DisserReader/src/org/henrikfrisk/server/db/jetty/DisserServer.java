package org.henrikfrisk.server.db.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.handler.AbstractHandler;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.mortbay.jetty.Request;
import javax.servlet.ServletException;
import org.mortbay.jetty.Connector;

public class DisserServer {

    // Output fields:
    public int effectivePort;

    private Server server;
    private SelectChannelConnector connector;

    public void start() throws Exception {

        server = new Server();
        Connector connector=new SelectChannelConnector();
        connector.setPort(Integer.getInteger("jetty.port",8080).intValue());
        server.setConnectors(new Connector[]{connector});

        String disser_home = System.getProperty("disser.home",".");
        
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(disser_home + "/www");
        webapp.setDefaultsDescriptor(disser_home+"/etc/webdefault.xml");
        
        server.setHandler(webapp);

        server.start();
    }
    
    public void stop() throws Exception {
        if (server != null) 
            server.stop(); 
    }
}
