package com.storyiq.mavenplugin.qunit;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.FileResource;

public class HttpService {

    private final Server server = new Server();
    private final Map<String, URL> resources;
    
    public HttpService(int port, Map<String, URL> resources) {
        this.resources = resources;
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector); 
    }

    public void start() throws Exception {   
        HandlerList handlers = new HandlerList();
        for (Entry<String, URL> entry:resources.entrySet()) {
            ContextHandler urlContext = new ContextHandler(entry.getKey());
            URL directory = entry.getValue();
            ResourceHandler directoryHandler = new ResourceHandler();        
            directoryHandler.setDirectoriesListed(true);            
            directoryHandler.setBaseResource(new FileResource(directory));            
            urlContext.setHandler(directoryHandler);
            handlers.addHandler(urlContext);
        }

        handlers.addHandler(new DefaultHandler()); // Returns 404 for unknown resource        
        server.setHandler(handlers);        
        server.start();
    }

    public void stop() throws Exception {
        server.stop();        
    }

    public void waitUntilFinished() throws InterruptedException {
        server.join();
    }
}
