package com.github.ijazfx.urlproxy.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class CustomSSLContext {

	public static SSLContext createCustomSSLContext(InputStream certificateInputStream, String certificatePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {
		// Initialize KeyStore with your custom SSL certificate
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(certificateInputStream, certificatePassword.toCharArray());

		// Initialize KeyManagerFactory with your SSL certificate
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keyManagerFactory.init(keyStore, certificatePassword.toCharArray());

		// Initialize TrustManagerFactory with default trust manager (or custom if required)
		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init((KeyStore) null);

		// Create custom SSL context and initialize it with the custom KeyManager and TrustManager
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

		return sslContext;
	}
}
