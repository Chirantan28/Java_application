@echo off
echo ========================================
echo COMPREHENSIVE SPLUNK LOGGING TEST
echo ========================================
echo.

echo Step 1: Building the application...
call .\gradlew.bat clean build --quiet
if %ERRORLEVEL% neq 0 (
    echo ERROR: Build failed!
    pause
    exit /b 1
)

echo Step 2: Starting the application...
start /B .\gradlew.bat bootRun

echo Step 3: Waiting for application to start...
timeout /t 15 /nobreak > nul

echo Step 4: Testing Splunk logging endpoints...
echo.

echo Testing basic Splunk logging...
curl -s http://localhost:8080/splunk-test
echo.

echo Testing structured logging...
curl -s http://localhost:8080/splunk-structured
echo.

echo Testing bulk log generation...
curl -s "http://localhost:8080/splunk-bulk?count=5"
echo.

echo Testing health check logging...
curl -s http://localhost:8080/splunk-health
echo.

echo Step 5: Checking for fallback logs...
if exist "logs\splunk-fallback.log" (
    echo Found fallback log file. Recent entries:
    type "logs\splunk-fallback.log"
) else (
    echo No fallback log file found - this might be good!
)

echo.
echo ========================================
echo TEST COMPLETED
echo ========================================
echo.
echo Next steps:
echo 1. Check your Splunk interface
echo 2. Search for: index=main source="spring-boot-multi-api"
echo 3. Look for logs with these patterns:
echo    - SPLUNK_TEST
echo    - USER_ACTION
echo    - PERFORMANCE
echo    - ERROR
echo    - HEALTH_CHECK
echo    - BULK_TEST
echo.
echo If you don't see logs in Splunk, check:
echo - Splunk is running on http://localhost:8088
echo - HTTP Event Collector is enabled
echo - Token is valid
echo - Index "main" exists
echo.
pause 