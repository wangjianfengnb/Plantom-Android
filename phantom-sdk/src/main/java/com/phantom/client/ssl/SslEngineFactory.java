package com.phantom.client.ssl;

import android.content.Context;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;

import java.security.KeyStore;

/**
 * SSL 双向认证
 *
 * @author Jianfeng Wang
 * @since 2019/12/4 14:17
 */
public class SslEngineFactory {

    public static SSLEngine getEngine(Context context) {
        try {

            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(context.getAssets().open("phantom-client.bks"),
                    "phantom-client".toCharArray());

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "phantom-client".toCharArray());

            KeyStore trustStore = KeyStore.getInstance("BKS");
            trustStore.load(context.getAssets().open("phantom-client.bks"),
                    "phantom-client".toCharArray());
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setNeedClientAuth(true);
            sslEngine.setUseClientMode(true);
            return sslEngine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
