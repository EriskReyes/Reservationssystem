```batch
@echo off
echo ========================================
echo   Datenbank wird initialisiert
echo ========================================
echo.

REM Prüfen ob PostgreSQL läuft
docker ps | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% neq 0 (
    echo [FEHLER] PostgreSQL läuft nicht!
    echo.
    echo Bitte zuerst ausführen: iniciar-postgres.cmd
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL läuft
echo.
echo Tabellen werden initialisiert...
echo.

docker exec -i postgres-terminkalender psql -U reservations_user -d reservations_db < init.sql

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   Datenbank erfolgreich initialisiert!
    echo ========================================
) else (
    echo.
    echo [FEHLER] Problem beim Initialisieren der Datenbank
    echo Dies kann normal sein, wenn die Tabellen bereits existieren.
)

echo.
pause
```