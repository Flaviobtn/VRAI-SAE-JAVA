@echo off

REM === CONFIGURATION ===
set "JAVAFX_PATH=C:\Program Files\javafx-sdk-22\lib"
set "JAR_PATH=.\lib\mariadb-java-client-3.5.3.jar"

REM === NETTOYAGE ===
if exist bin rd /s /q bin
mkdir bin

REM === GÉNÉRATION DE LA LISTE DES FICHIERS JAVA ===
echo Recherche des fichiers source...
dir /S /B src\*.java > sources.txt

REM === COMPILATION GLOBALE ===
echo Compilation de tous les fichiers Java...
javac -cp "bin;%JAR_PATH%" --module-path "%JAVAFX_PATH%" --add-modules javafx.controls -d bin @sources.txt

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Erreurs de compilation. Vérifie tes imports ou tes chemins de fichiers.
    pause
    exit /b
)

REM === LANCEMENT ===
echo.
echo ✅ Lancement de l'application...
java --module-path "%JAVAFX_PATH%" --add-modules javafx.controls -cp "bin;%JAR_PATH%" ihm.Vue.LivreExpresss

pause
