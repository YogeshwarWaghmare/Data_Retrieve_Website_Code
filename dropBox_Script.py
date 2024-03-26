from ssl import Options
import sys
import datetime
import re
import shutil
import time
import os
import traceback
from selenium import webdriver
import requests
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from email import encoders
import smtplib
import pyautogui
import zipfile
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from msal import ConfidentialClientApplication, ConfidentialClientApplication, PublicClientApplication, TokenCache
from selenium.common.exceptions import TimeoutException, NoAlertPresentException, NoSuchElementException, UnexpectedAlertPresentException, StaleElementReferenceException


class DropBox():
    def __init__(self, password):
        print("in __init__")
        self.password = password
        self.custom_download_directory = '/home/ywaghmare/Drop_Box_Pdf'
        self.driver = None
        self.driver = self.set_up_firefox_driver()
        self.driver.get("https://www.dropbox.com/scl/fo/dwkxq3f4kqa8vfwhvztkv/h?rlkey=hp0ypbafohxpqs5mjg7m5w79w&dl=0")
        self.driver.maximize_window()




    def set_up_firefox_driver(self):
        
        # firefox_options = Options()
        # firefox_profile = FirefoxProfile()
        firefox_options = webdriver.FirefoxOptions()
        
         # Set the download directory
        firefox_options.set_preference("browser.download.folderList", 2)  # Use custom directory
        firefox_options.set_preference("browser.download.dir", self.custom_download_directory)
        firefox_options.set_preference("browser.download.useDownloadDir", True)
        firefox_options.set_preference("browser.helperApps.neverAsk.saveToDisk", "application/pdf")  # Set MIME type for PDF files
        
        # Create Firefox driver with customized options
        return webdriver.Firefox(options=firefox_options)

    def click_on_Accept_button(self):
        time.sleep(15)
        for _ in range(17):
            pyautogui.sleep(1)
            pyautogui.press('tab')
            print("tab 1")

        # Press Enter
        print("tab 17")
        pyautogui.press('enter')
        
        time.sleep(5)


    def get_total_records(self):
        
        try:
            #
            #//body/div[@id='root']/span[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[3]/div[1]/div[1]/div[2]
            main_table_body_xpath = "//div[@class='dig-Table-body']//div[contains(@class, 'dig-Table-row') and contains(@class, 'dig-Table-row--has') and contains(@class, 'Divider')]"

              # Find the total number of rows initially loaded
            initial_rows = WebDriverWait(self.driver, 50).until(
                EC.presence_of_all_elements_located((By.XPATH, main_table_body_xpath))
            )

            # Keep track of the total number of rows
            total_rows = len(initial_rows)

            # Scroll down the page to load more rows until no new rows are loaded
            while True:
                print("Scrolling down to load more rows...")
                # Scroll down to the bottom of the page
                self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
                
                # Wait for some time to let new rows load
                time.sleep(20)  # You may need to adjust the wait time
                
                # Find all rows currently available
                rows = self.driver.find_elements(By.XPATH, main_table_body_xpath)
                
                # If no new rows are loaded, break the loop
                if len(rows) == total_rows:
                    break
                
                # Update the total count of rows
                total_rows = len(rows)

            # Once all rows are loaded, print the total number of rows
            print("Total Rows:", total_rows)

            return total_rows
        
        except TimeoutException:
            print(f"Timed out waiting for table to load for table'. Exiting the code.")
            traceback.print_exc()


    def login_using_password(self):
        # Locate password input field and submit button, and enter the password
        time.sleep(5)
        password_input = WebDriverWait(self.driver, 80).until(EC.presence_of_element_located((By.ID, "shared-link-password")))
        password_input.clear()
        
       #password = "_riv9?u!i0Es"  
        password_input.send_keys(self.password)
        submit_button = WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//span[contains(text(),'Continue')]")))
        submit_button.click()

    def download_pdf(self,folder_name):

        folder = WebDriverWait(self.driver, 50).until(
            EC.presence_of_element_located((By.XPATH, f"//a[@aria-label='{folder_name}']//div[@class='dig-LabelGroup']"))
            #//a[@aria-label='devicedb']//div[@class='dig-LabelGroup']
        )

        folder.click()
        time.sleep(10)

        number_of_pdf = self.get_total_records()
        row_counter = 0

        for i in range(0, number_of_pdf):
            self.download_one_by_one(i+1, folder_name)
            row_counter += 1 

            if row_counter == 10:
                print("Waiting for 5 minutes...")
                time.sleep(300)  # 300 seconds = 5 minutes
                row_counter = 0  # Reset the counter
            
            time.sleep(1)

        if folder_name == "devicedb":
             
            time.sleep(15)
            print("inside if block for devicedb")
           # self.driver.execute_script("window.scrollTo(0, 0);")
            script = "window.scrollTo(0, -document.body.scrollHeight);"
            
            self.driver.execute_script(script)
            time.sleep(15)
            # Wait for the element to be clickable
            back_to_folder = WebDriverWait(self.driver, 20).until(
                EC.element_to_be_clickable((By.XPATH, "//div[@class='dig-Breadcrumb-inner']//span[@class='dig-Breadcrumb-link-text'][normalize-space()='mongo_dump_1']"))
            )

            # Click on the element
            back_to_folder.click()
            time.sleep(5)

        if folder_name == "shipdb": 
             
             self.wait_until_files_downloaded(number_of_pdf)



    def download_one_by_one(self, row, folder_name):

            print("row number :",row)

           # Scroll to the element
            

            download_button = WebDriverWait(self.driver, 50).until(
                EC.visibility_of_element_located((By.XPATH, f"//div[@class='_sl-page-body-container_cfzbs_50']//div[2]//div[{row}]//div[3]"))
            )
            time.sleep(1)
            # Wait until the iframe with id "consent-iframe" is not present
            #WebDriverWait(self.driver, 10).until(EC.invisibility_of_element_located((By.ID, "consent-iframe")))
            #//div[14]//div[3]-//span[@class='dig-Button-content'][normalize-space()='Download']
            self.driver.execute_script("arguments[0].scrollIntoView(true);", download_button)

            # Move to the element
            ActionChains(self.driver).move_to_element(download_button).perform()
            
            time.sleep(1)
            actions = ActionChains(self.driver)
            actions.move_to_element(download_button).perform()
            # Click on the download button
            download_button.click()
            time.sleep(1)

            # popup_to_download =WebDriverWait(self.driver, 50).until(
            #     EC.visibility_of_element_located((By.XPATH, "//span[contains(text(),'Or continue with download only')]"))
            # )
            # time.sleep(1)
            # popup_to_download.click()

            try:
        # Wait for the popup button to appear within 5 seconds
                popup_to_download = WebDriverWait(self.driver, 8).until(
                    EC.visibility_of_element_located((By.XPATH, "//span[contains(text(),'Or continue with download only')]"))
                )

                # If the button appears, click on it
                popup_to_download.click()
                print("Popup button for download clicked")

            except TimeoutException:
                # If the button doesn't appear within 5 seconds, move to the next row
                print("Popup button for download not found within 5 seconds, moving to the next row")

            try:

                        # Try to find the close button of the sign-in popup
                        #signin_popup_close_button = self.driver.find_element(By.XPATH, "//div[@class='ReactModal_Content']//button[contains(@class, 'dig-Button--transparent') and contains(text(), 'Close')]")
                        signin_popup_close_button = WebDriverWait(self.driver, 8).until(
                            EC.element_to_be_clickable((By.XPATH, "/html/body/div[4]/div/div/div/div[3]/button[1]/span"))
                        )
                        
                        # Click on the close button
                        signin_popup_close_button.click()
                        
                        print("Sign-in popup closed")
                    
            except TimeoutException:
                        # If the close button is not found, no sign-in popup is present
                        print("No sign-in popup close button found")

                       
    def wait_until_files_downloaded(self, num_files_expected):
        while True:
            print("Inside wait_until_files_downloade block")
            # Get the list of files in the download directory
            downloaded_files = os.listdir(self.custom_download_directory)

            # Filter the list to include only PDF files
            pdf_files = [file for file in downloaded_files if file.endswith('.pdf')]

            # Check if the number of PDF files matches the expected number
            if len(pdf_files) == num_files_expected:
                print("All PDFs have been downloaded.")
                break
            print("Not downloaded yet")

            # If not all files are downloaded, wait for some time before checking again
            time.sleep(100)
            

if __name__ == "__main__":

    dropbox = DropBox(password="_riv9?u!i0Es")
   # //button[@id="accept_all_cookies_button"]
    dropbox.click_on_Accept_button()


    dropbox.login_using_password()
    #//a[@aria-label='devicedb']//div[@class='dig-LabelGroup']
    #//a[@aria-label='shipdb']//div[@class='dig-LabelGroup']
    folders_to_download = ["devicedb", "shipdb"]
    for folder in folders_to_download:
            print("folder name - ",folder)
            dropbox.download_pdf(folder)


print("Program run successfully....!")
dropbox.driver.quit() 

