#!/bin/bash
# Compiler le fichier Java avec le .jar
echo "Suppression du Bin"
rm -rf .bin/*
javac -cp "./lib/mariadb-java-client-3.5.3.jar" -d bin src/*/*.java
echo "Compilation terminée."
# Lancer le programme
echo "on tente de lancer le programme"
echo "----------------------------------------------------"
ls bin
echo "----------------------------------------------------"
#ls bin/app
java -cp "bin;lib/mariadb-java-client-3.5.3.jar" app.AppMenu

#java -cp "bin:lib/mariadb-java-client-3.5.3.jar" bin/app/AppMenu