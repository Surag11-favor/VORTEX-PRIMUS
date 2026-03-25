import urllib.request
import json

url = "http://localhost:8080/api/analysis/scan"
data = json.dumps({"content": "https://www.youtube.com/@theformivelyris"}).encode("utf-8")
headers = {"Content-Type": "application/json"}

req = urllib.request.Request(url, data=data, headers=headers, method="POST")

try:
    with urllib.request.urlopen(req, timeout=10) as response:
        print("Status:", response.status)
        print("Body:", response.read().decode("utf-8"))
except Exception as e:
    print("Error:", e)
