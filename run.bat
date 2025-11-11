@echo off
setlocal enabledelayedexpansion

REM --- Check for --mobile flag ---
set MOBILE_FLAG=0
for %%a in (%*) do (
    if "%%a"=="--mobile" set MOBILE_FLAG=1
)

if %MOBILE_FLAG% equ 1 (
    echo --mobile flag detected.
    
    REM --- Path to your Android project ---
    set PROJECT_DIR="\mobile"
    
    REM --- Check if Android Studio exists in common locations ---
    set "ANDROID_STUDIO_FOUND=0"
    if EXIST "C:\Program Files\Android\Android Studio\bin\studio64.exe" set ANDROID_STUDIO_FOUND=1
    if EXIST "C:\Program Files (x86)\Android\Android Studio\bin\studio64.exe" set ANDROID_STUDIO_FOUND=1
    
    if "%ANDROID_STUDIO_FOUND%"=="0" (
        echo Android Studio not found. Please install it first.
        exit /b 1
    ) else (
        echo Android Studio found.
    )

    REM --- Check if Gradle wrapper exists ---
    if NOT EXIST ".\mobile\gradlew.bat" (
        echo Gradle wrapper not found. Are you in an Android project?
        exit /b 1
    )

    REM --- Launch Android Studio in this folder ---
    if EXIST "C:\Program Files\Android\Android Studio\bin\studio64.exe" (
        start "" "C:\Program Files\Android\Android Studio\bin\studio64.exe" ".\mobile"
    ) else (
        start "" "C:\Program Files (x86)\Android\Android Studio\bin\studio64.exe" ".\mobile"
    )
)


REM --- Exit on error ---
set error_flag=0

REM --- Check if Docker is running ---
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker is not running. Please start Docker and try again.
    exit /b 1
) else (
    echo Docker is running.
)

REM --- Function to check if a port is free ---
call :check_port_free 8000
if !error_flag! equ 1 exit /b 1

call :check_port_free 8080
if !error_flag! equ 1 exit /b 1

call :check_port_free 3306
if !error_flag! equ 1 exit /b 1

REM --- Move into frontend directory ---
if exist "frontend\" (
    cd frontend
    echo Changed directory to ./frontend.
) else (
    echo Directory ./frontend not found.
    exit /b 1
)

REM --- Handle .env file creation ---
if exist ".env" (
    echo .env already exists. Skipping setup environment
) else if exist ".env.template" (
    copy /Y .env.template .env >nul
    echo Copy .env.template to .env.
) else (
    echo No .env or .env.template file found.
)

REM --- Go back to project root ---
cd ..
echo Returned to project root.

REM --- Move into backend directory ---
if exist "backend\" (
    cd backend
    echo Changed directory to ./backend.
) else (
    echo Directory ./backend not found.
    exit /b 1
)

REM --- Handle .env file creation ---
if exist ".env" (
    echo .env already exists. Skipping setup environment
) else if exist ".env.template" (
    copy /Y .env.template .env >nul
    echo Copy .env.template to .env.
) else (
    echo No .env or .env.template file found.
)

REM --- Go back to project root ---
cd ..
echo Returned to project root.

REM --- Detect docker compose command ---
where docker-compose >nul 2>&1
if %errorlevel% equ 0 (
    set COMPOSE_CMD=docker-compose
) else (
    docker compose version >nul 2>&1
    if %errorlevel% equ 0 (
        set COMPOSE_CMD=docker compose
    ) else (
        echo Neither 'docker-compose' nor 'docker compose' is available.
        exit /b 1
    )
)

echo Using '%COMPOSE_CMD%' to start containers.

REM --- Run docker compose ---
echo Starting Docker containers...
%COMPOSE_CMD% up --build

exit /b 0

REM --- Port check function ---
:check_port_free
set port=%1
netstat -an | find ":%port% " >nul
if %errorlevel% equ 0 (
    echo Port %port% is currently in use. Please free it before continuing.
    set error_flag=1
) else (
    echo Port %port% is free.
    set error_flag=0
)
exit /b 0