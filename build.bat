@echo off
REM Configuration
set "PROJECT_NAME=projet_1_s6"
set "SRC_DIR=src\main\java"
set "WEBAPP_DIR=src\main\webapp"
set "LIB_DIR=lib"
set "BUILD_DIR=build"
set "CLASSES_DIR=%BUILD_DIR%\WEB-INF\classes"
set "WAR_FILE=%PROJECT_NAME%.war"

REM Créer les répertoires de build
if not exist "%CLASSES_DIR%" mkdir "%CLASSES_DIR%"
if not exist "%BUILD_DIR%\WEB-INF\lib" mkdir "%BUILD_DIR%\WEB-INF\lib"

REM Copier les fichiers webapp
xcopy /E /Y "%WEBAPP_DIR%\*" "%BUILD_DIR%\"

REM Copier les librairies
xcopy /Y "%LIB_DIR%\*.jar" "%BUILD_DIR%\WEB-INF\lib\"

REM Télécharger le driver PostgreSQL si nécessaire
set "POSTGRES_JAR=%BUILD_DIR%\WEB-INF\lib\postgresql-42.7.1.jar"
if not exist "%POSTGRES_JAR%" (
    echo Téléchargement du driver PostgreSQL...
    powershell -Command "try { Invoke-WebRequest -Uri 'https://jdbc.postgresql.org/download/postgresql-42.7.1.jar' -OutFile '%POSTGRES_JAR%' } catch { exit 1 }"
    if exist "%POSTGRES_JAR%" (
        echo ✓ Driver PostgreSQL téléchargé
    ) else (
        echo ✗ Erreur lors du téléchargement du driver PostgreSQL
        echo   Veuillez télécharger manuellement depuis https://jdbc.postgresql.org/download/
    )
)

REM Compiler les sources Java
set "CLASSPATH=%LIB_DIR%\*;%BUILD_DIR%\WEB-INF\lib\*"
dir /B /S "%SRC_DIR%\*.java" > sources.txt
javac -cp "%CLASSPATH%" -d "%CLASSES_DIR%" @sources.txt
if %ERRORLEVEL%==0 (
    echo ✓ Compilation réussie
    del sources.txt
) else (
    echo ✗ Erreur de compilation
    del sources.txt
    exit /b 1
)

REM Créer le fichier WAR
cd "%BUILD_DIR%"
jar -cvf "..\%WAR_FILE%" . > nul
if %ERRORLEVEL%==0 (
    echo ✓ Fichier WAR créé: %WAR_FILE%
) else (
    echo ✗ Erreur lors de la création du WAR
    exit /b 1
)
cd ..

echo.
echo Pour déployer:
echo   1. Copiez %WAR_FILE% dans le dossier webapps de Tomcat
echo   2. Démarrez Tomcat
echo   3. Accédez à http://localhost:8080/%PROJECT_NAME%/api/tests
echo.
echo Endpoints API disponibles:
echo   GET /api/tests      - Liste tous les tests
echo   GET /api/tests/{id} - Récupère un test par ID
echo.