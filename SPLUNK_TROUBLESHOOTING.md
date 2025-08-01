# Splunk Logging Troubleshooting Guide

## Issues Fixed

### 1. **Configuration Issues**
- ✅ Updated logback-spring.xml with proper SSL settings
- ✅ Added connection timeout and retry settings
- ✅ Added fallback logging for Splunk failures
- ✅ Fixed dependency conflicts in build.gradle

### 2. **Dependency Issues**
- ✅ Removed conflicting Maven dependency
- ✅ Kept only the local JAR file in libs/
- ✅ Added required transitive dependencies

### 3. **Error Handling**
- ✅ Added fallback file appender for Splunk failures
- ✅ Added debug logging for Splunk library
- ✅ Added retry mechanism

## Testing Your Splunk Setup

### Step 1: Verify Splunk is Running
```bash
# Check if Splunk HTTP Event Collector is accessible
curl -k https://localhost:8088/services/collector
```

### Step 2: Test Your Application
```bash
# Build and run the application
./gradlew bootRun

# Test logging endpoints
curl http://localhost:8080/test-log
curl http://localhost:8080/test-splunk?level=info
curl http://localhost:8080/test-splunk-structured
```

### Step 3: Check Logs
- Check console output for Splunk connection messages
- Check `logs/splunk-fallback.log` for any failed Splunk attempts
- Check application logs for debug messages from `com.splunk.logging`

## Common Issues and Solutions

### Issue 1: Connection Refused
**Symptoms**: `java.net.ConnectException: Connection refused`
**Solutions**:
- Verify Splunk is running on port 8088
- Check if HTTPS is required (change URL to `https://`)
- Verify firewall settings

### Issue 2: Invalid Token
**Symptoms**: `HTTP 401 Unauthorized`
**Solutions**:
- Generate a new token in Splunk
- Update the token in `application.yml`
- Verify the token has proper permissions

### Issue 3: SSL Certificate Issues
**Symptoms**: `javax.net.ssl.SSLHandshakeException`
**Solutions**:
- Set `disable_certificate_validation=true` (already done)
- Use HTTP instead of HTTPS for testing
- Import Splunk's certificate

### Issue 4: Index Not Found
**Symptoms**: `HTTP 400 Bad Request`
**Solutions**:
- Verify the index "main" exists in Splunk
- Create the index if it doesn't exist
- Update the index name in configuration

## Configuration Checklist

- [ ] Splunk is running and accessible
- [ ] HTTP Event Collector is enabled
- [ ] Token is valid and has proper permissions
- [ ] Index "main" exists in Splunk
- [ ] SSL certificate is valid (or validation is disabled)
- [ ] Firewall allows connections to port 8088

## Debug Steps

1. **Enable Debug Logging**: Already configured in application.yml
2. **Check Network Connectivity**: Test with curl or telnet
3. **Verify Splunk Configuration**: Check Splunk admin interface
4. **Monitor Application Logs**: Look for Splunk debug messages
5. **Check Fallback Logs**: Review `logs/splunk-fallback.log`

## Alternative Configuration

If HTTPS doesn't work, try HTTP:
```xml
<url>http://localhost:8088</url>
```

If localhost doesn't work, try your Splunk server IP:
```xml
<url>https://your-splunk-server:8088</url>
```

## Next Steps

1. Start your application: `./gradlew bootRun`
2. Test the endpoints: `curl http://localhost:8080/test-log`
3. Check Splunk for incoming logs
4. If issues persist, check the fallback log file 