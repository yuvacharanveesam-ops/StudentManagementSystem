@echo off
title Student Management System (Pure AWT)
cd /d "%~dp0"

echo ========================================================
echo   Launching Student Management System (Pure Java AWT)
echo ========================================================
echo.

:: 1. Detect Java Runtime
set "JAVA_CMD="
where java >nul 2>nul
if %errorlevel% equ 0 (
    set "JAVA_CMD=java"
) else (
    for /d %%D in ("C:\Program Files\Java\jdk*") do (
        if exist "%%D\bin\java.exe" set "JAVA_CMD=%%D\bin\java.exe"
    )
)

if "%JAVA_CMD%"=="" (
    echo [ERROR] Java runtime not found. Please install Java JDK or add java to your PATH.
    pause
    exit /b 1
)

:: 2. Auto-compile if classes are missing
if not exist "bin\com\studentmanagement\desktop\AwtStudentApp.class" if not exist "target\classes\com\studentmanagement\desktop\AwtStudentApp.class" (
    echo [INFO] Compiled classes not found. Auto-compiling source files...
    set "JAVAC_CMD="
    where javac >nul 2>nul
    if %errorlevel% equ 0 (
        set "JAVAC_CMD=javac"
    ) else (
        for /d %%D in ("C:\Program Files\Java\jdk*") do (
            if exist "%%D\bin\javac.exe" set "JAVAC_CMD=%%D\bin\javac.exe"
        )
    )

    if "%JAVAC_CMD%"=="" (
        echo [ERROR] javac compiler not found. Please ensure JDK is installed.
        pause
        exit /b 1
    )

    if not exist "bin" mkdir "bin"
    "%JAVAC_CMD%" -d bin -cp "lib\mysql-connector-j-8.0.33.jar" src\main\java\com\studentmanagement\model\*.java src\main\java\com\studentmanagement\util\*.java src\main\java\com\studentmanagement\dao\*.java src\main\java\com\studentmanagement\desktop\*.java
    if errorlevel 1 (
        echo [ERROR] Compilation failed.
        pause
        exit /b 1
    )
    echo [INFO] Compilation successful!
    echo.
)

:: 3. Run application with classpath checking bin, target\classes, and lib
set "CP=bin;target\classes;lib\mysql-connector-j-8.0.33.jar"

"%JAVA_CMD%" -cp "%CP%" com.studentmanagement.desktop.AwtStudentApp

if errorlevel 1 (
    echo.
    echo [ERROR] Application exited with error code %errorlevel%.
    pause
)
