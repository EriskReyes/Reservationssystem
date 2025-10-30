@echo off
echo ========================================
echo   Inicializando Base de Datos
echo ========================================
echo.

REM Verificar si PostgreSQL esta corriendo
docker ps | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] PostgreSQL no esta corriendo!
    echo.
    echo Por favor ejecuta primero: iniciar-postgres.cmd
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL esta corriendo
echo.
echo Inicializando tablas...
echo.

docker exec -i postgres-terminkalender psql -U reservations_user -d reservations_db < init.sql

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   Base de datos inicializada!
    echo ========================================
) else (
    echo.
    echo [ERROR] Hubo un problema al inicializar la base de datos
    echo Esto puede ser normal si las tablas ya existen.
)

echo.
pause
