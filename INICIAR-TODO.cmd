@echo off
chcp 65001 >nul
echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                                                            ║
echo ║         SISTEMA DE RESERVACIONES - INICIO COMPLETO         ║
echo ║                                                            ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo Este script hará TODO automáticamente:
echo   ✓ Verificar Docker
echo   ✓ Iniciar PostgreSQL
echo   ✓ Crear tablas si no existen
echo   ✓ Verificar conexión
echo.
pause
echo.

REM ===== PASO 1: Verificar Docker =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ PASO 1/4: Verificando Docker Desktop...                   │
echo └────────────────────────────────────────────────────────────┘
echo.

docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [✗] ERROR: Docker Desktop no está corriendo!
    echo.
    echo 📌 SOLUCIÓN:
    echo    1. Busca "Docker Desktop" en el menú de inicio
    echo    2. Ábrelo y espera que cargue completamente
    echo    3. El ícono de Docker en la barra debe estar verde
    echo    4. Vuelve a ejecutar este script
    echo.
    pause
    exit /b 1
)

echo [✓] Docker Desktop está corriendo
echo.
timeout /t 2 /nobreak >nul

REM ===== PASO 2: Iniciar PostgreSQL =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ PASO 2/4: Iniciando PostgreSQL...                         │
echo └────────────────────────────────────────────────────────────┘
echo.

docker ps -a | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% equ 0 (
    docker ps | findstr "postgres-terminkalender" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [✓] PostgreSQL ya está corriendo
    ) else (
        echo Iniciando contenedor existente...
        docker start postgres-terminkalender >nul 2>&1
        echo [✓] PostgreSQL iniciado
        timeout /t 3 /nobreak >nul
    )
) else (
    echo Creando nuevo contenedor PostgreSQL...
    docker-compose up -d postgres
    echo [✓] PostgreSQL creado
    echo.
    echo Esperando que PostgreSQL esté listo...
    timeout /t 8 /nobreak >nul
)

echo.

REM ===== PASO 3: Verificar Base de Datos =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ PASO 3/4: Verificando Base de Datos...                    │
echo └────────────────────────────────────────────────────────────┘
echo.

REM Verificar si las tablas existen
docker exec postgres-terminkalender psql -U reservations_user -d reservations_db -c "\dt" 2>nul | findstr "reservationen" >nul 2>&1
if %errorlevel% neq 0 (
    echo Las tablas no existen. Inicializando...
    docker exec -i postgres-terminkalender psql -U reservations_user -d reservations_db < init.sql >nul 2>&1
    if %errorlevel% equ 0 (
        echo [✓] Base de datos inicializada
    ) else (
        echo [!] No se pudo inicializar (puede ser que ya exista)
    )
) else (
    echo [✓] Base de datos ya está inicializada
)

echo.
timeout /t 2 /nobreak >nul

REM ===== PASO 4: Verificación Final =====
echo ┌────────────────────────────────────────────────────────────┐
echo │ PASO 4/4: Verificación Final...                           │
echo └────────────────────────────────────────────────────────────┘
echo.

echo Estado del contenedor:
docker ps --filter "name=postgres-terminkalender" --format "  - {{.Names}}: {{.Status}}"

echo.
echo Tablas en la base de datos:
docker exec postgres-terminkalender psql -U reservations_user -d reservations_db -c "\dt" 2>nul | findstr "reservationen\|teilnehmer" | findstr /v "row"

echo.
echo.
echo ╔════════════════════════════════════════════════════════════╗
echo ║                     ✓ TODO LISTO ✓                         ║
echo ╚════════════════════════════════════════════════════════════╝
echo.
echo 📊 INFORMACIÓN DE CONEXIÓN:
echo    Host:     localhost
echo    Puerto:   5432
echo    Database: reservations_db
echo    Usuario:  reservations_user
echo    Password: reservations_pass
echo.
echo 🚀 PRÓXIMOS PASOS:
echo.
echo    OPCIÓN 1 - IntelliJ IDEA (Recomendado):
echo       1. Abre IntelliJ IDEA
echo       2. Abre el proyecto
echo       3. Ve a: src/main/java/com/terminkalender/
echo       4. Abre: TerminkalenderApplication.java
echo       5. Click derecho → Run 'TerminkalenderApplication'
echo       6. Espera y abre: http://localhost:8080
echo.
echo    OPCIÓN 2 - Maven:
echo       1. Abre CMD en esta carpeta
echo       2. Ejecuta: mvn spring-boot:run
echo       3. Espera y abre: http://localhost:8080
echo.
echo 💡 CONSEJO: Mantén esta ventana abierta para ver el estado
echo.
pause
@REM
