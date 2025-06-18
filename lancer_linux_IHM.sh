#!/bin/bash

# Répertoire JavaFX
JAVAFX_PATH="./lib/JavaFx/lib"

# Compiler tous les fichiers .java dans src
find src -name "*.java" | while read file; do
    echo "Compilation de $file"
    javac --module-path "$JAVAFX_PATH" --add-modules javafx.controls -d bin -cp src "$file"
done

# Exécuter la classe principale
echo
java --module-path "$JAVAFX_PATH" --add-modules javafx.controls -cp bin ihm.Vue.LivreExpresss