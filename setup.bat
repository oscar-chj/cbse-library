@echo off
echo ========================================
echo   Library Catalog Management System
echo ========================================
echo.
echo Starting Docker containers...
echo (First run will take a few minutes to build)
echo.
docker compose up --build -d
echo.
echo Containers started! Waiting for the application...
echo The app will be available at: http://localhost:8080
echo REST API endpoint: http://localhost:8080/api/books/{isbn}
echo.
echo Showing application logs (Ctrl+C to stop viewing logs):
echo ----------------------------------------
docker compose logs -f app
