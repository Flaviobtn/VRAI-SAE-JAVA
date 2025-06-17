#!/bin/bash
# on se rend dans le dossier src


#on compile les fichiers .java
javac -d bin --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls -d bin src/ihm/*/*.java


#on lance l'application
java -cp bin:img --module-path /usr/share/openjfx/lib/ --add-modules javafx.controls -cp bin ihm.Vue.LivreExpresss