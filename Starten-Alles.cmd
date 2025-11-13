```batch
@echo off
chcp 65001 >nul
echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                                                            ║
echo ║      RESERVIERUNGSSYSTEM - VOLLSTÄNDIGER START             ║
echo ║                                                            ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo Dieses Skript führt ALLES automatisch aus:
echo   ✓ Docker überprüfen
echo   ✓ PostgreSQL starten
echo   ✓ Tabellen erstellen falls nicht vorhanden
echo   ✓ Verbindung überprüfen
echo.
pause
echo.

REM ===== SCHRITT 1: Docker überprüfen =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ SCHRITT 1/4: Docker Desktop wird überprüft...             │
echo └────────────────────────────────────────────────────────────┘
echo.

docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [✗] FEHLER: Docker Desktop läuft nicht!
    echo.
    echo 📌 LÖSUNG:
    echo    1. Suche "Docker Desktop" im Startmenü
    echo    2. Öffne es und warte bis es vollständig geladen ist
    echo    3. Das Docker-Symbol in der Taskleiste muss grün sein
    echo    4. Führe dieses Skript erneut aus
    echo.
    pause
    exit /b 1
)

echo [✓] Docker Desktop läuft
echo.
timeout /t 2 /nobreak >nul

REM ===== SCHRITT 2: PostgreSQL starten =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ SCHRITT 2/4: PostgreSQL wird gestartet...                 │
echo └────────────────────────────────────────────────────────────┘
echo.

docker ps -a | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% equ 0 (
    docker ps | findstr "postgres-terminkalender" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [✓] PostgreSQL läuft bereits
    ) else (
        echo Vorhandener Container wird gestartet...
        docker start postgres-terminkalender >nul 2>&1
        echo [✓] PostgreSQL gestartet
        timeout /t 3 /nobreak >nul
    )
) else (
    echo Neuer PostgreSQL-Container wird erstellt...
    docker-compose up -d postgres
    echo [✓] PostgreSQL erstellt
    echo.
    echo Warte bis PostgreSQL bereit ist...
    timeout /t 8 /nobreak >nul
)

echo.

REM ===== SCHRITT 3: Datenbank überprüfen =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ SCHRITT 3/4: Datenbank wird überprüft...                  │
echo └────────────────────────────────────────────────────────────┘
echo.

REM Prüfen ob Tabellen existieren
docker exec postgres-terminkalender psql -U reservations_user -d reservations_db -c "\dt" 2>nul | findstr "reservationen" >nul 2>&1
if %errorlevel% neq 0 (
    echo Tabellen existieren nicht. Initialisierung läuft...
    docker exec -i postgres-terminkalender psql -U reservations_user -d reservations_db < init.sql >nul 2>&1
    if %errorlevel% equ 0 (
        echo [✓] Datenbank initialisiert
    ) else (
        echo [!] Konnte nicht initialisiert werden (existiert möglicherweise bereits)
    )
) else (
    echo [✓] Datenbank ist bereits initialisiert
)

echo.
timeout /t 2 /nobreak >nul

REM ===== SCHRITT 4: Abschließende Überprüfung =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ SCHRITT 4/4: Abschließende Überprüfung...                 │
echo └────────────────────────────────────────────────────────────┘
echo.

echo Container-Status:
docker ps --filter "name=postgres-terminkalender" --format "  - {{.Names}}: {{.Status}}"

echo.
echo Tabellen in der Datenbank:
docker exec postgres-terminkalender psql -U reservations_user -d reservations_db -c "\dt" 2>nul | findstr "reservationen\|teilnehmer" | findstr /v "row"

echo.
echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                    ✓ ALLES BEREIT ✓                        ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo 📊 VERBINDUNGSINFORMATIONEN:
echo    Host:     localhost
echo    Port:     5432
echo    Datenbank: reservations_db
echo    Benutzer:  reservations_user
echo    Passwort:  reservations_pass
echo.
echo 🚀 NÄCHSTE SCHRITTE:
echo.
echo    OPTION 1 - IntelliJ IDEA (Empfohlen):
echo       1. Öffne IntelliJ IDEA
echo       2. Öffne das Projekt
echo       3. Gehe zu: src/main/java/com/terminkalender/
echo       4. Öffne: TerminkalenderApplication.java
echo       5. Rechtsklick → Run 'TerminkalenderApplication'
echo       6. Warte und öffne: http://localhost:8080
echo.
echo    OPTION 2 - Maven:
echo       1. Öffne CMD in diesem Ordner
echo       2. Führe aus: mvn spring-boot:run
echo       3. Warte und öffne: http://localhost:8080
echo.
echo 💡 TIPP: Halte dieses Fenster offen um den Status zu sehen
echo.
pause
@REM
```