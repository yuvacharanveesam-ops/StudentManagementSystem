@echo off
title Student Management System (Pure AWT)
cd /d "%~dp0"

echo ========================================================
echo   Launching Student Management System (Pure Java AWT)
echo ========================================================
echo.

if exist "C:\Program Files\Java\jdk-26.0.2\bin\java.exe" (
    set "JAVA_EXE=C:\Program Files\Java\jdk-26.0.2\bin\java.exe"
) else (
    set "JAVA_EXE=java"
)

set "CP=target\classes;lib\mysql-connector-j-8.0.33.jar;C:\Users\user\.m2\repository\com\mysql\mysql-connector-j\8.0.33\mysql-connector-j-8.0.33.jar"

"%JAVA_EXE%" -cp "%CP%" com.studentmanagement.desktop.AwtStudentApp

if errorlevel 1 (
    echo.
    echo [ERROR] Application exited with an error code.
    pause
)
