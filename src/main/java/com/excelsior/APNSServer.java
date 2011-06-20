package com.excelsior;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import com.excelsior.pipeline.SecurePipelineFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class APNSServer implements Runnable {

    private final int port;
    private final CountDownLatch latch;

    public APNSServer(int port, CountDownLatch latch) {
        this.port = port;
        this.latch = latch;
    }

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new SecurePipelineFactory());

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));
        latch.countDown();
    }
}
