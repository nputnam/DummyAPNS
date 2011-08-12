package com.excelsior;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.excelsior.pipeline.SecurePipelineFactory;
import com.excelsior.stats.StatsManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class APNSServer implements Runnable {

    private final int port;
    private final CountDownLatch latch;
    private final String keyStorePath;
    private final String keyStorePassword;

    public APNSServer(String keyStorePath, String keyStorePassword, int port, CountDownLatch latch) {
        this.port = port;
        this.latch = latch;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new SecurePipelineFactory(keyStorePath,keyStorePassword));
        bootstrap.setOption("tcpNoDelay",false);
        bootstrap.setOption("child.tcpNoDelay",false);
        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));
        latch.countDown();
    }

    public void stop(CountDownLatch latch) {
        Thread.currentThread().interrupt();
        latch.countDown();
    }

    public int getConnectionCount() {
        return StatsManager.getConnectionCount();
    }

    public long getAllTimeCount() {
        return StatsManager.getAllTimeCount();
    }

}

