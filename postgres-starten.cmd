```batch
@echo off
echo ========================================
echo   PostgreSQL für das Projekt starten
echo ========================================
echo.

REM Prüfen ob Docker Desktop läuft
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [FEHLER] Docker Desktop läuft nicht!
    echo.
    echo Bitte:
    echo 1. Öffne Docker Desktop aus dem Startmenü
    echo 2. Warte bis das Docker-Symbol in der Taskleiste grün ist
    echo 3. Führe dieses Skript erneut aus
    echo.
    pause
    exit /b 1
)

echo [OK] Docker Desktop läuft
echo.

REM Prüfen ob der Container bereits existiert
docker ps -a | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% equ 0 (
    echo PostgreSQL-Container gefunden...

    REM Prüfen ob er läuft
    docker ps | findstr "postgres-terminkalender" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [OK] PostgreSQL läuft bereits
    ) else (
        echo Vorhandener Container wird gestartet...
        docker start postgres-terminkalender
        echo [OK] PostgreSQL gestartet
    )
) else (
    echo Neuer PostgreSQL-Container wird erstellt...
    docker-compose up -d postgres
    echo [OK] PostgreSQL erstellt und gestartet

    REM Warten bis PostgreSQL bereit ist
    echo.
    echo Warte bis PostgreSQL bereit ist...
    timeout /t 5 /nobreak >nul
)

echo.
echo Status wird überprüft...
docker ps | findstr "postgres-terminkalender"

echo.
echo ========================================
echo   PostgreSQL ist bereit!
echo ========================================
echo.
echo Du kannst jetzt:
echo 1. Spring Boot von IntelliJ IDEA starten
echo 2. Oder ausführen: mvn spring-boot:run
echo.
echo PostgreSQL läuft auf: localhost:5432
echo Datenbank: reservations_db
echo Benutzer: reservations_user
echo Passwort: reservations_pass
echo.
pause
```