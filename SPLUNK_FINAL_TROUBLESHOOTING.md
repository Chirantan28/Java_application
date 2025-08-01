# Splunk Logging - Final Troubleshooting Guide

## 🚀 **Quick Start - Test Everything**

### Step 1: Run the Comprehensive Test
```bash
.\test-splunk-comprehensive.bat
```

### Step 2: Check Application Console
Look for these messages in your application console:
- `=== SPLUNK CONNECTION TEST STARTED ===`
- `Testing connection to Splunk at: http://localhost:8088`
- `✅ Splunk is accessible and responding` (or error messages)

### Step 3: Check Splunk Interface
In Splunk, search for:
```
index=main source="spring-boot-multi-api"
```

## 🔍 **Detailed Troubleshooting**

### **Issue 1: Application Won't Start**
**Symptoms**: Build errors or startup failures
**Solution**:
1. Run: `.\gradlew.bat clean build`
2. Check for dependency conflicts
3. Ensure Java 21 is installed

### **Issue 2: No Connection to Splunk**
**Symptoms**: "Failed to connect to Splunk" messages
**Solutions**:
1. **Verify Splunk is running**:
   ```bash
   curl -k http://localhost:8088/services/collector
   ```
2. **Check if HTTP Event Collector is enabled** in Splunk
3. **Try different URLs**:
   - `http://localhost:8088`
   - `https://localhost:8088`
   - `http://your-splunk-server:8088`

### **Issue 3: Authentication Failed**
**Symptoms**: "401 Unauthorized" or "authentication failed"
**Solutions**:
1. **Generate a new token** in Splunk:
   - Go to Settings → Data Inputs → HTTP Event Collector
   - Create a new token
2. **Update the token** in `application.yml` and `logback-spring.xml`
3. **Verify token permissions** in Splunk

### **Issue 4: Index Not Found**
**Symptoms**: "404 Not Found" or logs not appearing
**Solutions**:
1. **Create the "main" index** in Splunk:
   - Go to Settings → Indexes
   - Create new index named "main"
2. **Check index permissions** for your token

### **Issue 5: SSL Certificate Issues**
**Symptoms**: SSL handshake errors
**Solutions**:
1. **Use HTTP instead of HTTPS** (already configured)
2. **Import Splunk's certificate** if using HTTPS
3. **Disable certificate validation** in Splunk settings

## 📊 **Verification Steps**

### **Step 1: Check Application Logs**
Look for these messages in console:
```
✅ Splunk is accessible and responding
✅ Test logs generated successfully
```

### **Step 2: Check Fallback Logs**
If Splunk fails, check `logs/splunk-fallback.log`:
```bash
type logs\splunk-fallback.log
```

### **Step 3: Test Endpoints Manually**
```bash
# Test basic logging
curl http://localhost:8080/splunk-test

# Test structured logging
curl http://localhost:8080/splunk-structured

# Test bulk logging
curl "http://localhost:8080/splunk-bulk?count=3"
```

### **Step 4: Check Splunk Interface**
Search for these patterns in Splunk:
```
index=main source="spring-boot-multi-api"
```

Look for logs containing:
- `SPLUNK_TEST`
- `USER_ACTION`
- `PERFORMANCE`
- `ERROR`
- `HEALTH_CHECK`
- `BULK_TEST`

## 🔧 **Configuration Checklist**

### **Splunk Setup**
- [ ] Splunk is running and accessible
- [ ] HTTP Event Collector is enabled
- [ ] Token is valid and has proper permissions
- [ ] Index "main" exists
- [ ] SSL is properly configured (or using HTTP)

### **Application Configuration**
- [ ] `logback-spring.xml` has correct URL and token
- [ ] `application.yml` has Splunk configuration
- [ ] All dependencies are resolved
- [ ] Application starts without errors

### **Network/Firewall**
- [ ] Port 8088 is accessible
- [ ] No firewall blocking the connection
- [ ] Network connectivity is working

## 🎯 **Expected Behavior**

### **When Working Correctly:**
1. ✅ Application starts without errors
2. ✅ Console shows "Splunk is accessible and responding"
3. ✅ Test endpoints return JSON responses
4. ✅ Logs appear in Splunk interface
5. ✅ No errors in fallback log file

### **When There Are Issues:**
1. ❌ Application won't start → Check dependencies
2. ❌ Connection failed → Check Splunk URL and status
3. ❌ Authentication failed → Check token
4. ❌ No logs in Splunk → Check index and permissions

## 🚨 **Emergency Fixes**

### **If Nothing Works:**
1. **Try HTTP instead of HTTPS** (already done)
2. **Use a different port** if 8088 is blocked
3. **Generate a new token** in Splunk
4. **Create a new index** in Splunk
5. **Check Splunk logs** for errors

### **Last Resort:**
1. **Restart Splunk** completely
2. **Restart your application**
3. **Check Splunk system logs** for errors
4. **Verify network connectivity** between app and Splunk

## 📞 **Getting Help**

If you're still having issues:
1. Check the console output for specific error messages
2. Look at the fallback log file for details
3. Verify your Splunk configuration
4. Test with a simple curl command to Splunk

The application is now fully configured for Splunk logging! 🎉 