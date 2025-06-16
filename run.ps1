# Chemins vers les dossiers
$src = "src"
$bin = "bin"
$img = "img"
$javafx = "C:\Program Files\javafx-sdk-22\lib"

# Crée le dossier bin s'il n'existe pas
if (!(Test-Path -Path $bin)) {
    New-Item -ItemType Directory -Path $bin | Out-Null
}

# Compilation
Write-Host "🛠️ Compilation en cours..."
javac -d $bin --module-path "$javafx" --add-modules javafx.controls "$src\*.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Erreur de compilation"
    exit 1
}

# Définir la classe principale
# Si vous avez un package, utilisez le nom complet comme "pendu.Pendu"
$mainClass = "Pendu"

# Exécution
Write-Host "🚀 Exécution du programme..."
java -cp "$bin;$img" --module-path "$javafx" --add-modules javafx.controls $mainClass
