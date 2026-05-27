"""
Download ChromeDriver for the installed Chrome version
"""
import os
import sys
import zipfile
import requests
from pathlib import Path

def get_chrome_version():
    """Get installed Chrome version"""
    chrome_path = r"C:\Program Files\Google\Chrome\Application\chrome.exe"
    if not os.path.exists(chrome_path):
        print("Chrome not found at default location")
        return None
    
    import subprocess
    result = subprocess.run(
        ['powershell', '-Command', 
         f"(Get-Item '{chrome_path}').VersionInfo.ProductVersion"],
        capture_output=True,
        text=True
    )
    
    version = result.stdout.strip()
    print(f"Chrome version: {version}")
    return version

def download_chromedriver(version):
    """Download ChromeDriver for the given Chrome version"""
    major_version = version.split('.')[0]
    
    # ChromeDriver download URL for Chrome 115+
    base_url = "https://googlechromelabs.github.io/chrome-for-testing/known-good-versions-with-downloads.json"
    
    print(f"Fetching ChromeDriver for Chrome {major_version}...")
    
    try:
        response = requests.get(base_url)
        data = response.json()
        
        # Find matching version
        matching_version = None
        for item in data['versions']:
            if item['version'].startswith(f"{major_version}."):
                matching_version = item
                break
        
        if not matching_version:
            print(f"No ChromeDriver found for Chrome {major_version}")
            return False
        
        # Get Windows chromedriver URL
        chromedriver_url = None
        for download in matching_version['downloads'].get('chromedriver', []):
            if download['platform'] == 'win64':
                chromedriver_url = download['url']
                break
        
        if not chromedriver_url:
            print("No Windows 64-bit ChromeDriver found")
            return False
        
        print(f"Downloading from: {chromedriver_url}")
        
        # Download the zip file
        zip_path = "chromedriver_win64.zip"
        response = requests.get(chromedriver_url, stream=True)
        response.raise_for_status()
        
        with open(zip_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
        
        print("Download complete. Extracting...")
        
        # Extract the zip file
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall('.')
        
        # Find the chromedriver.exe in extracted folder
        for root, dirs, files in os.walk('.'):
            if 'chromedriver.exe' in files:
                src = os.path.join(root, 'chromedriver.exe')
                dst = 'chromedriver.exe'
                if os.path.exists(dst):
                    os.remove(dst)
                os.rename(src, dst)
                print(f"ChromeDriver extracted to: {os.path.abspath(dst)}")
                break
        
        # Cleanup
        os.remove(zip_path)
        
        # Remove extracted folder
        import shutil
        for item in os.listdir('.'):
            if os.path.isdir(item) and 'chromedriver' in item.lower():
                shutil.rmtree(item)
        
        print("ChromeDriver installation complete!")
        return True
        
    except Exception as e:
        print(f"Error downloading ChromeDriver: {e}")
        return False

if __name__ == "__main__":
    version = get_chrome_version()
    if version:
        download_chromedriver(version)
    else:
        print("Could not determine Chrome version")
        sys.exit(1)
