@echo off
echo ========================================
echo   Iniciando PostgreSQL para el proyecto
echo ========================================
echo.

REM Verificar si Docker Desktop esta corriendo
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Docker Desktop no esta corriendo!
    echo.
    echo Por favor:
    echo 1. Abre Docker Desktop desde el menu de inicio
    echo 2. Espera que el icono de Docker en la barra de tareas este verde
    echo 3. Vuelve a ejecutar este script
    echo.
    pause
    exit /b 1
)

echo [OK] Docker Desktop esta corriendo
echo.

REM Verificar si el contenedor ya existe
docker ps -a | findstr "postgres-terminkalender" >nul 2>&1
if %errorlevel% equ 0 (
    echo Contenedor PostgreSQL encontrado...

    REM Verificar si esta corriendo
    docker ps | findstr "postgres-terminkalender" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [OK] PostgreSQL ya esta corriendo
    ) else (
        echo Iniciando contenedor existente...
        docker start postgres-terminkalender
        echo [OK] PostgreSQL iniciado
    )
) else (
    echo Creando nuevo contenedor PostgreSQL...
    docker-compose up -d postgres
    echo [OK] PostgreSQL creado e iniciado

    REM Esperar que PostgreSQL este listo
    echo.
    echo Esperando que PostgreSQL este listo...
    timeout /t 5 /nobreak >nul
)

echo.
echo Verificando estado...
docker ps | findstr "postgres-terminkalender"

echo.
echo ========================================
echo   PostgreSQL esta listo!
echo ========================================
echo.
echo Ahora puedes:
echo 1. Iniciar Spring Boot desde IntelliJ IDEA
echo 2. O ejecutar: mvn spring-boot:run
echo.
echo PostgreSQL corriendo en: localhost:5432
echo Base de datos: reservations_db
echo Usuario: reservations_user
echo Password: reservations_pass
echo.
pause
//