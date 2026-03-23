@echo off
echo Forcing Maven dependency refresh...
"C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.4\plugins\maven\lib\maven3\bin\mvn.cmd" dependency:resolve -U > refresh_deps.log 2>&1
echo Done. Exit code: %ERRORLEVEL%
type refresh_deps.log
