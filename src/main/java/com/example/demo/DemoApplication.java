
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // Disable SSL certificate validation for Splunk self-signed certificate
        System.setProperty("com.sun.net.ssl.checkRevocation", "false");
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        
        // Create a trust manager that trusts all certificates
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };

            // Create SSL context that trusts all certificates
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            
            // Trust all hostnames
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            
            System.out.println("SSL certificate validation disabled successfully");
        } catch (Exception e) {
            System.err.println("Failed to disable SSL certificate validation: " + e.getMessage());
        }
        
        SpringApplication.run(DemoApplication.class, args);
    }

}