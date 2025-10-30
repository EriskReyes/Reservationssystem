@echo off
echo ========================================
echo   Deteniendo PostgreSQL
echo ========================================
echo.

docker stop postgres-terminkalender

echo.
echo [OK] PostgreSQL detenido
echo.
pause
