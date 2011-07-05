package com.excelsior.pipeline;

import com.excelsior.decoder.EnhancedPushDecoder;
import com.excelsior.handler.EnhancedPushReceivedHandler;
import com.excelsior.handler.traffic.GlobalTrafficShapingHandler;
import com.excelsior.stats.StatsManager;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

import static org.jboss.netty.channel.Channels.*;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SecurePipelineFactory implements ChannelPipelineFactory {

    private final String keyStorePath;
    private final String keyStorePassword;

    public SecurePipelineFactory(String keyStorePath, String keyStorePassword) {
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();

        SSLEngine engine =
                SSLContextFactory.getServerContext(keyStorePath, keyStorePassword).createSSLEngine();
        engine.setUseClientMode(false);

        pipeline.addLast("ssl", new SslHandler(engine, true));

        // On top of the SSL handler, add the text line codec.
        //  pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
        //          8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new EnhancedPushDecoder());
        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("traffic", StatsManager.getGlobalTrafficHandler());

        // and then business logic.
        pipeline.addLast("handler", new EnhancedPushReceivedHandler());

        return pipeline;
    }
}
