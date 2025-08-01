@echo off
echo Testing Splunk Logging Configuration...
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
timeout /t 10 /nobreak > nul

echo Step 4: Testing logging endpoints...
echo Testing basic logging...
curl -s http://localhost:8080/test-log
echo.

echo Testing structured logging...
curl -s http://localhost:8080/test-splunk-structured
echo.

echo Testing different log levels...
curl -s "http://localhost:8080/test-splunk?level=error"
echo.

echo Step 5: Checking for fallback logs...
if exist "logs\splunk-fallback.log" (
    echo Found fallback log file. Checking recent entries...
    type "logs\splunk-fallback.log"
) else (
    echo No fallback log file found - this might be good!
)

echo.
echo Test completed! Check your Splunk instance for incoming logs.
echo If you see errors, check the troubleshooting guide: SPLUNK_TROUBLESHOOTING.md
pause 