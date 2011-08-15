package com.excelsior;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.excelsior.handler.ChannelManager;
import com.excelsior.pipeline.SecurePipelineFactory;
import com.excelsior.stats.StatsManager;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class APNSServer extends Thread {

    private final int port;
    private final CountDownLatch latch;
    private final String keyStorePath;
    private final String keyStorePassword;
    private ExecutorService bossThreadPool = Executors.newCachedThreadPool();
    private ExecutorService workerThreadPool = Executors.newCachedThreadPool();

    public APNSServer(String keyStorePath, String keyStorePassword, int port, CountDownLatch latch) {
        this.port = port;
        this.latch = latch;
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.setDaemon(false);
    }

    public void run() {
        NioServerSocketChannelFactory nioServerSocketChannelFactory = new NioServerSocketChannelFactory(bossThreadPool,workerThreadPool);
        ServerBootstrap bootstrap = new ServerBootstrap(nioServerSocketChannelFactory);

        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new SecurePipelineFactory(keyStorePath,keyStorePassword));
        bootstrap.setOption("tcpNoDelay",false);
        bootstrap.setOption("child.tcpNoDelay",false);
        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));

        latch.countDown();
    }

    public void stop(CountDownLatch latch) throws Exception {
        List<Channel> channels = ChannelManager.getChannels();
        for (Channel channel : channels) {
            ChannelFuture close = channel.close();
            close.awaitUninterruptibly();
        }
        bossThreadPool.shutdownNow();
        workerThreadPool.shutdownNow();
        bossThreadPool.awaitTermination(1L, TimeUnit.SECONDS);
        workerThreadPool.awaitTermination(1L,TimeUnit.SECONDS);
        latch.countDown();
    }

    public int getConnectionCount() {
        return StatsManager.getConnectionCount();
    }

    public long getAllTimeCount() {
        return StatsManager.getAllTimeCount();
    }

}

