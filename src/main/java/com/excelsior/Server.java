package com.excelsior;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.concurrent.CountDownLatch;


public class Server {

    private static final Logger log = LogManager.getLogger(Server.class);

    public static void main(String...args) throws Exception{
        //TODO Add in apache-cli or something so the keystore port etc.. are loaded from a config file or
        // as startupLatch params.
        final CountDownLatch startupLatch = new CountDownLatch(1);
        final int port = 2195;
        APNSServer server = new APNSServer("/tmp/keystore.ks","password",port,startupLatch);
        log.info("Starting server on port "+port+"....");
        Thread serverThread = new Thread(server);
        serverThread.start();
        startupLatch.await();
        log.info("Server started");
        serverThread.join();
    }
}
