# Run script (in src/dependencies): runs the Main class as a module (preferred)
# Works when invoked from the dependencies folder or from repo root.
$scriptDir = $PSScriptRoot
$repoRoot = Split-Path $scriptDir -Parent -Parent
$gson = Join-Path $scriptDir "gson-2.13.1.jar"
# Preferred (module-based):
java --module-path "$(Join-Path $repoRoot 'out');$scriptDir" -m POO/poo.Main
# Alternative (classpath):
# java -cp "$(Join-Path $repoRoot 'out');$gson" poo.Main
