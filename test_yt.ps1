$body = @{content="https://www.youtube.com/@theformivelyris"} | ConvertTo-Json
$res = Invoke-RestMethod -Uri "http://localhost:8080/api/analysis/scan" -Method Post -Body $body -ContentType "application/json"
$res | ConvertTo-Json | Out-File "C:\Users\NKUNIM\Desktop\Enterprise Fraud Intelligence & Threat Analysis Platform\actual_result.txt" -Encoding utf8
