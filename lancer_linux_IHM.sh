#!/bin/bash
# on se rend dans le dossier src


#on compile les fichiers .java
javac -d bin --module-path ./lib/ --add-modules javafx.controls ./src/ihm/*/*.java


#on lance l'application
java -cp bin:img --module-path ./lib/ --add-modules javafx.controls LivreExpresss