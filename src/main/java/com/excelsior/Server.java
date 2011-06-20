package com.excelsior;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.CountDownLatch;


public class Server {

    private static final Logger log = LogManager.getLogger(Server.class);

    public static void main(String...args) throws Exception{
        CountDownLatch startup = new CountDownLatch(1);
        APNSServer server = new APNSServer(2195,startup);
        log.info("Starting server on port 2195");
        Thread serverThread = new Thread(server);
        serverThread.start();
        startup.await();
        log.info("Server started");
        serverThread.join();
    }
}
