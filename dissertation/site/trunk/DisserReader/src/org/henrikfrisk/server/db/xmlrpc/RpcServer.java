package org.henrikfrisk.server.db.xmlrpc;

import java.net.InetAddress;

import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import org.henrikfrisk.server.db.xmlrpc.*;

public class RpcServer { 

    private static final int port = 8080;
    private static WebServer webServer = null;

    public static void startServer () {
        
        try {

            System.out.println("Attempting to start XML-RPC Server...");

            webServer = new WebServer(port);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

            PropertyHandlerMapping phm = new PropertyHandlerMapping();

            phm.load(Thread.currentThread().getContextClassLoader(), 
                     "org/henrikfrisk/server/db/xmlrpc/XmlRpcServlet.properties");

            xmlRpcServer.setHandlerMapping(phm);
        
            XmlRpcServerConfigImpl serverConfig =
                (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);

            webServer.start();

            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");

        } catch (Exception exception) {
            
            System.err.println("JavaServer: " + exception);

        }
    }

    public static void stopServer() {
        
        try {
            webServer.shutdown();
            System.out.println("RPC Server stopped.");
        }
        catch(RuntimeException re) {
            System.out.println(re.toString());
        }
    }
}
