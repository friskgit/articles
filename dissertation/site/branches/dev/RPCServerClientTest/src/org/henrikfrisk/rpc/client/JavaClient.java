package org.henrikfrisk.rpc.client;

import org.henrikfrisk.disserdb.xmlrpc.proxy.Adder;

import java.util.*;
import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.util.ClientFactory;


public class JavaClient {
    public static void main (String [] args) {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);
 
            ClientFactory factory = new ClientFactory(client);
            Adder adder = (Adder) factory.newInstance(Adder.class);
            int sum = adder.add(4, 4);
            sum = adder.add(sum, 4);
            System.out.println("The sum is: "+ sum);

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
        }
    }
}