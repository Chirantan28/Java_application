@echo off
echo Starting Java application with SSL certificate validation disabled...
echo.

set JAVA_OPTS=-Dcom.sun.net.ssl.checkRevocation=false -Djavax.net.ssl.trustStoreType=JKS -Djavax.net.ssl.trustStore=none -Djavax.net.ssl.trustStorePassword= -Djavax.net.ssl.keyStoreType=JKS -Djavax.net.ssl.keyStore=none -Djavax.net.ssl.keyStorePassword= -Djavax.net.ssl.trustAll=true

echo JVM Options: %JAVA_OPTS%
echo.

gradlew bootRun -Dorg.gradle.jvmargs="%JAVA_OPTS%"

pause 