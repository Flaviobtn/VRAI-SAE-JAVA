@echo off
:: attention à l'emplacement de javafx
javac --module-path lib\javafx-sdk-24.0.1\lib --add-modules javafx.controls -d bin src\ihm\Vue\*.java

java --module-path lib\javafx-sdk-24.0.1\lib --add-modules javafx.controls -cp bin ihm.Vue.LivreExpresss