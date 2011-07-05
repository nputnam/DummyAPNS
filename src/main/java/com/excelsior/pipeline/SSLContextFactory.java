package com.excelsior.pipeline;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.*;

public class SSLContextFactory {

    private static final Logger log = LogManager.getLogger(SSLContextFactory.class);


    private static final String PROTOCOL = "TLS";
    private static final SSLContext CLIENT_CONTEXT = null;

    static {
        /*SSLContext tlsContext = null;
        try {
            char[] passphrase = "password".toCharArray();
            // First initialize the key and trust material.
            KeyStore ks = KeyStore.getInstance("JKS");
            //  KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream("/tmp/keystore.ks"), passphrase);

            tlsContext = SSLContext.getInstance(PROTOCOL);


            // KeyManager's decide which key material to use.
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);
            tlsContext.init(kmf.getKeyManagers(), null, null);
            tlsContext.createSSLEngine();

        } catch (Exception e) {
            log.error("Error loading cert", e);
        }
        SERVER_CONTEXT = tlsContext;        */
    }

    public static SSLContext getServerContext(String keyStorePath, String keyStorePassword) {
        SSLContext tlsContext = null;
        try {

            char[] passphrase = keyStorePassword.toCharArray();
            // First initialize the key and trust material.
            KeyStore ks = KeyStore.getInstance("JKS");
            //  KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(keyStorePath), passphrase);

            tlsContext = SSLContext.getInstance(PROTOCOL);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);
            tlsContext.init(kmf.getKeyManagers(), null, null);
            tlsContext.createSSLEngine();
        } catch (Exception e) {
            log.error("Error loading keystore", e);
        }
        return tlsContext;
    }

    public static SSLContext getClientContext() {
        return CLIENT_CONTEXT;
    }

}
