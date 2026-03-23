@echo off
"C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2.4\plugins\maven\lib\maven3\bin\mvn.cmd" clean package -DskipTests > build_output.log 2>&1
