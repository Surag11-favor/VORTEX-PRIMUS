@echo off
curl -X POST http://localhost:8080/api/analysis/scan -H "Content-Type: application/json" -d "{\"content\":\"https://www.youtube.com/@theformivelyris\"}" > final_out.txt
