@echo off
title Student Management System
cd /d "%~dp0"

echo ========================================================
echo   Launching Student Management System (Swing Desktop)
echo ========================================================
echo.

set JAVA_EXE=C:\Program Files\Java\jdk-26.0.2\bin\java.exe
set CP=target\classes;C:\Users\user\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar

"%JAVA_EXE%" -cp "%CP%" com.studentmanagement.desktop.Main

pause
