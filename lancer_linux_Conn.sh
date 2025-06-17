#!/bin/bash
# Compiler le fichier Java avec le .jar
javac -cp "./lib/mariadb-java-client-3.5.3.jar" -d bin src/bd/ConnectionBD.java

# Lancer le programme
java -cp "bin;lib/mariadb-java-client-3.5.3.jar" bd.ConnectionBD