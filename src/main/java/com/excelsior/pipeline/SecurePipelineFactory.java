package com.excelsior.pipeline;

import com.excelsior.decoder.EnhancedPushDecoder;
import com.excelsior.handler.EnhancedPushReceivedHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
 import static org.jboss.netty.channel.Channels.*;

import javax.net.ssl.SSLEngine;

public class SecurePipelineFactory implements ChannelPipelineFactory {

      public ChannelPipeline getPipeline() throws Exception {
          ChannelPipeline pipeline = pipeline();

          SSLEngine engine =
              SSLContextFactory.getServerContext().createSSLEngine();
         engine.setUseClientMode(false);

         pipeline.addLast("ssl", new SslHandler(engine,true));

        // On top of the SSL handler, add the text line codec.
        //  pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
        //          8192, Delimiters.lineDelimiter()));
          pipeline.addLast("decoder", new EnhancedPushDecoder());
          pipeline.addLast("encoder", new StringEncoder());

          // and then business logic.
          pipeline.addLast("handler", new EnhancedPushReceivedHandler());

          return pipeline;
}
}
