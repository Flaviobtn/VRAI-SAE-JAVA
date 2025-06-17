@echo off
REM Compiler les fichiers Java avec le .jar
for /R src %%f in (*.java) do (
    echo Compilation de %%f
    javac -cp "lib\mariadb-java-client-3.5.3.jar" -d bin "%%f"
)

REM Lancer le programme
java -cp "bin;lib\mariadb-java-client-3.5.3.jar" app.AppMenu