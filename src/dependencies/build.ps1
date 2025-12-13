# Build script (in src/dependencies): compiles project using module-path (JPMS)
# Works when invoked from the dependencies folder or from repo root.
$scriptDir = $PSScriptRoot
$repoRoot = Split-Path $scriptDir -Parent -Parent
$gson = Join-Path $scriptDir "gson-2.13.1.jar"
$srcDir = Join-Path $repoRoot "src"
$src = Get-ChildItem -Recurse -Filter *.java -Path $srcDir | ForEach-Object { $_.FullName }
Remove-Item -Recurse -Force (Join-Path $repoRoot "out") -ErrorAction SilentlyContinue
# Compile with module-path so `requires com.google.gson` is resolved
javac -d (Join-Path $repoRoot "out") --module-path $scriptDir $src
Write-Host "Compiled to $repoRoot/out"
