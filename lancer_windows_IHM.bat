@echo off
:: attention à l'emplacement de javafx
javac --module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.controls -d bin src\ihm\Vue\*.java

java --module-path "C:\Program Files\javafx-sdk-22\lib" --add-modules javafx.controls -cp bin ihm.Vue.LivreExpresss