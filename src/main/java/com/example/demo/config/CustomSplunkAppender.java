package com.example.demo.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CustomSplunkAppender extends AppenderBase<ILoggingEvent> {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomSplunkAppender.class);
    
    private String url = "https://localhost:8088/services/collector/event";
    private String token = "8357121e-7e1b-4000-aa1d-f3b482531210";
    private String source = "spring-boot-multi-api";
    private String sourcetype = "_json";
    private String index = "main";
    
    private OkHttpClient httpClient;
    private Gson gson = new Gson();
    
    @Override
    public void start() {
        super.start();
        httpClient = createHttpClient();
    }
    
    private OkHttpClient createHttpClient() {
        try {
            // Create a trust manager that trusts all certificates
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
            
            // Create SSLSocketFactory with the trust manager
            SSLSocketFactory sslSocketFactory = new SSLSocketFactory() {
                @Override
                public String[] getDefaultCipherSuites() {
                    return new String[0];
                }

                @Override
                public String[] getSupportedCipherSuites() {
                    return new String[0];
                }

                @Override
                public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
                    return sslContext.getSocketFactory().createSocket(s, host, port, autoClose);
                }

                @Override
                public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
                    return sslContext.getSocketFactory().createSocket(host, port);
                }

                @Override
                public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
                    return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
                }

                @Override
                public Socket createSocket(InetAddress host, int port) throws IOException {
                    return sslContext.getSocketFactory().createSocket(host, port);
                }

                @Override
                public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
                    return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
                }
            };
            
            return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
                
        } catch (Exception e) {
            logger.error("Failed to create HTTP client: {}", e.getMessage());
            return new OkHttpClient();
        }
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        try {
            // Create Splunk event
            SplunkEvent splunkEvent = new SplunkEvent();
            splunkEvent.time = System.currentTimeMillis() / 1000; // Unix timestamp
            splunkEvent.host = "localhost";
            splunkEvent.source = source;
            splunkEvent.sourcetype = sourcetype;
            splunkEvent.index = index;
            
            // Create event data
            EventData eventData = new EventData();
            eventData.message = event.getFormattedMessage();
            eventData.level = event.getLevel().toString();
            eventData.logger = event.getLoggerName();
            eventData.thread = event.getThreadName();
            eventData.timestamp = event.getTimeStamp();
            
            splunkEvent.event = eventData;
            
            // Convert to JSON
            String json = gson.toJson(splunkEvent);
            
            // Send to Splunk
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Splunk " + token)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
                
            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    logger.debug("Successfully sent log to Splunk");
                } else {
                    logger.warn("Failed to send log to Splunk: {} - {}", response.code(), response.body().string());
                }
            }
            
        } catch (Exception e) {
            logger.error("Error sending log to Splunk: {}", e.getMessage());
        }
    }
    
    // Getters and setters for configuration
    public void setUrl(String url) { this.url = url; }
    public void setToken(String token) { this.token = token; }
    public void setSource(String source) { this.source = source; }
    public void setSourcetype(String sourcetype) { this.sourcetype = sourcetype; }
    public void setIndex(String index) { this.index = index; }
    
    // Inner classes for JSON structure
    private static class SplunkEvent {
        public long time;
        public String host;
        public String source;
        public String sourcetype;
        public String index;
        public EventData event;
    }
    
    private static class EventData {
        public String message;
        public String level;
        public String logger;
        public String thread;
        public long timestamp;
    }
} 