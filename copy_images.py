import shutil
import os

src_dir = r"C:\Users\NKUNIM\.gemini\antigravity\brain\7ff5819e-7e8f-4975-85e4-652bff77aed6"
dest_dir = r"C:\Users\NKUNIM\Desktop\CYBER-COMMAND\src\main\resources\static\images"

files = {
    "ceo_commanding_v2_1774572260862.png": "ceo_commander.png",
    "command_center_1774571707917.png": "command_center.png",
    "neural_network_1774571803890.png": "neural_network.png",
    "security_shield_1774571723035.png": "security_shield.png",
    "threat_globe_1774571755734.png": "threat_globe.png"
}

for src, dest in files.items():
    try:
        shutil.copyfile(os.path.join(src_dir, src), os.path.join(dest_dir, dest))
        print(f"Copied {src} to {dest}")
    except Exception as e:
        print(f"Failed to copy {src}: {e}")
