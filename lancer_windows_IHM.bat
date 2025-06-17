@echo off
:: Répertoire JavaFX
set JAVAFX_PATH="C:\Program Files\javafx-sdk-22\lib"

:: Nettoyer puis recréer le dossier bin
if exist bin rd /s /q bin
mkdir bin

:: Compiler tous les fichiers .java dans src
for /R src %%f in (*.java) do (
    echo Compilation de %%f
    javac --module-path %JAVAFX_PATH% --add-modules javafx.controls -d bin -cp src lib\mariadb-java-client-3.5.3.jar %%f
)

:: Exécuter la classe principale
echo.
echo Lancement de l'application...
java --module-path %JAVAFX_PATH% --add-modules javafx.controls -cp lib\mariadb-java-client-3.5.3.jar bin ihm.Vue.LivreExpresss
pause
