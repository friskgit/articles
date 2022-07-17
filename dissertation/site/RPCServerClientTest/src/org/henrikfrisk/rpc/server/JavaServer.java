package org.henrikfrisk.rpc.server;

import java.net.InetAddress;

import org.apache.xmlrpc.common.TypeConverterFactoryImpl;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import org.henrikfrisk.rpc.proxy.AdderImpl;
import org.henrikfrisk.rpc.server.*;

public class JavaServer { 

    private static final int port = 8080;

    public static void main (String [] args) {
        try {

            System.out.println("Attempting to start XML-RPC Server...");

            WebServer webServer = new WebServer(port);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

            PropertyHandlerMapping phm = new PropertyHandlerMapping();
          /* Load handler definitions from a property file.
           * The property file might look like:
           *   Calculator=org.apache.xmlrpc.demo.Calculator
           *   org.apache.xmlrpc.demo.proxy.Adder=org.apache.xmlrpc.demo.proxy.AdderImpl
           */
          phm.load(Thread.currentThread().getContextClassLoader(), "org/henrikfrisk/rpc/server/XmlRpcServlet.properties");

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
}
