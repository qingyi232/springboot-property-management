"""Inspect the login page to find correct element selectors"""
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import os
import time

# Setup Chrome
chrome_options = Options()
chrome_options.add_argument("--start-maximized")

chromedriver_path = os.path.join(os.path.dirname(__file__), "chromedriver.exe")
service = Service(chromedriver_path)
driver = webdriver.Chrome(service=service, options=chrome_options)

try:
    print("Navigating to login page...", flush=True)
    driver.get("http://localhost:8081")
    time.sleep(3)
    
    print(f"\nPage Title: {driver.title}", flush=True)
    print(f"Current URL: {driver.current_url}", flush=True)
    
    # Find all input fields
    print("\n=== All Input Fields ===", flush=True)
    inputs = driver.find_elements(By.TAG_NAME, "input")
    for i, inp in enumerate(inputs):
        print(f"\nInput {i+1}:", flush=True)
        print(f"  Type: {inp.get_attribute('type')}", flush=True)
        print(f"  Name: {inp.get_attribute('name')}", flush=True)
        print(f"  ID: {inp.get_attribute('id')}", flush=True)
        print(f"  Class: {inp.get_attribute('class')}", flush=True)
        print(f"  Placeholder: {inp.get_attribute('placeholder')}", flush=True)
        print(f"  Visible: {inp.is_displayed()}", flush=True)
    
    # Find all buttons
    print("\n=== All Buttons ===", flush=True)
    buttons = driver.find_elements(By.TAG_NAME, "button")
    for i, btn in enumerate(buttons):
        print(f"\nButton {i+1}:", flush=True)
        print(f"  Text: {btn.text}", flush=True)
        print(f"  Type: {btn.get_attribute('type')}", flush=True)
        print(f"  Class: {btn.get_attribute('class')}", flush=True)
        print(f"  Visible: {btn.is_displayed()}", flush=True)
    
    # Save page source
    with open("page_source.html", "w", encoding="utf-8") as f:
        f.write(driver.page_source)
    print("\nPage source saved to: page_source.html", flush=True)
    
    # Take screenshot
    driver.save_screenshot("login_page_inspect.png")
    print("Screenshot saved to: login_page_inspect.png", flush=True)
    
    print("\nKeeping browser open for 10 seconds for manual inspection...", flush=True)
    time.sleep(10)
    
finally:
    driver.quit()
    print("Browser closed.", flush=True)
