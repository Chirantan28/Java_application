Write-Host "Testing Splunk Logging Configuration..." -ForegroundColor Green
Write-Host ""

Write-Host "Step 1: Building the application..." -ForegroundColor Yellow
& .\gradlew.bat clean build --quiet
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed!" -ForegroundColor Red
    Read-Host "Press Enter to continue"
    exit 1
}

Write-Host "Step 2: Starting the application..." -ForegroundColor Yellow
Start-Process -FilePath ".\gradlew.bat" -ArgumentList "bootRun" -WindowStyle Hidden

Write-Host "Step 3: Waiting for application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Step 4: Testing logging endpoints..." -ForegroundColor Yellow
Write-Host "Testing basic logging..."
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/test-log" -Method Get
    Write-Host $response -ForegroundColor Green
} catch {
    Write-Host "Failed to connect to application: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Testing structured logging..."
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/test-splunk-structured" -Method Get
    Write-Host $response -ForegroundColor Green
} catch {
    Write-Host "Failed to test structured logging: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Testing different log levels..."
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/test-splunk?level=error" -Method Get
    Write-Host $response -ForegroundColor Green
} catch {
    Write-Host "Failed to test log levels: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "Step 5: Checking for fallback logs..." -ForegroundColor Yellow
if (Test-Path "logs\splunk-fallback.log") {
    Write-Host "Found fallback log file. Checking recent entries..." -ForegroundColor Yellow
    Get-Content "logs\splunk-fallback.log" -Tail 10
} else {
    Write-Host "No fallback log file found - this might be good!" -ForegroundColor Green
}

Write-Host ""
Write-Host "Test completed! Check your Splunk instance for incoming logs." -ForegroundColor Green
Write-Host "If you see errors, check the troubleshooting guide: SPLUNK_TROUBLESHOOTING.md" -ForegroundColor Yellow
Read-Host "Press Enter to continue" 