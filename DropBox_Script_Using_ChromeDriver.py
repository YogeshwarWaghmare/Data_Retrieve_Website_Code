import time
import os
import traceback
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from selenium.common.exceptions import TimeoutException,  NoSuchElementException
from selenium.webdriver.chrome.service import Service

class DropBox():
    def __init__(self, password):
        print("in __init__")
        self.password = password
        self.custom_download_directory = '/home/ubuntu/test'
        self.driver = None
        self.driver = self.set_up_chrome_driver(self.custom_download_directory)
        self.driver.get("https://www.dropbox.com/scl/fo/dwkxq3f4kqa8vfwhvztkv/h?rlkey=hp0ypbafohxpqs5mjg7m5w79w&dl=0")
        self.driver.maximize_window()




    def set_up_chrome_driver(self,download_directory):
        chrome_options = webdriver.ChromeOptions()
        chrome_options.add_argument('--headless')
        prefs = {'download.default_directory': download_directory}
        print("download directory name is :",download_directory)
        chrome_options.add_experimental_option('prefs', prefs)
        service = Service(executable_path='/usr/bin/chromedriver')
        
        return webdriver.Chrome(service=service, options=chrome_options)


    def set_download_directory(self, download_directory):
        prefs = {'download.default_directory': download_directory}
        self.driver.command_executor._commands["send_command"] = (
            "POST",
            '/session/$sessionId/chromium/send_command',
        )
        params = {'cmd': 'Page.setDownloadBehavior', 'params': {'behavior': 'allow', 'downloadPath': download_directory}}
        self.driver.execute("send_command", params)

    def click_on_Accept_button(self):
        
        try:

                # Switch to the consent iframe
            consent_iframe = WebDriverWait(self.driver, 50).until(
                EC.frame_to_be_available_and_switch_to_it((By.ID, "consent-iframe"))
            )
            
            # Switch to the CCPA iframe within the consent iframe
            ccpa_iframe = WebDriverWait(self.driver, 10).until(
                EC.frame_to_be_available_and_switch_to_it((By.ID, "ccpa-iframe"))
            )
            
            # Locate the "Accept All" button inside the CCPA iframe
            accept_button = WebDriverWait(self.driver, 10).until(
                EC.element_to_be_clickable((By.ID, "accept_all_cookies_button"))
            )
            accept_button.click()
            print("Accept All button clicked successfully.")
            
            # Switch back to the default content
            self.driver.switch_to.default_content()

        except Exception as e:
            print("Error occurred while clicking Accept All button:", e)


    def get_total_records(self):
        
        try:
            
            
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
        print("folder name is :",folder_name)
        # total_expected_files = 0
        folder = WebDriverWait(self.driver, 50).until(
            EC.presence_of_element_located((By.XPATH, f"//a[@aria-label='{folder_name}']//div[@class='dig-LabelGroup']"))
            
        )

        folder.click()
         # Create the folder path
        folder_path = os.path.join(self.custom_download_directory, folder_name)
        if not os.path.exists(folder_path):
            os.makedirs(folder_path)

        # Set the download directory for the current folder
        self.set_download_directory(folder_path)

       
        time.sleep(5)

        number_of_pdf = self.get_total_records()

        row_counter = 0
        
        try:
            for i in range(0, number_of_pdf):
                self.download_one_by_one(i+1, folder_name)
                row_counter += 1 

                if row_counter == 15:
                    print("Waiting for 5 minutes...")
                    time.sleep(300)  # 300 seconds = 5 minutes
                    row_counter = 0  # Reset the counter

        


            if folder_name == "devicedb":
                
                time.sleep(15)
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


            self.wait_until_files_downloaded(number_of_pdf, folder_path)

        except Exception as e:
            print(f"Error while downloading PDF for search query : {e}")
            traceback.print_exc()
            self.driver.refresh()
            self.driver.get("https://www.dropbox.com/scl/fo/dwkxq3f4kqa8vfwhvztkv/h?rlkey=hp0ypbafohxpqs5mjg7m5w79w&dl=0")
            self.download_pdf(folder_name)

    


    def download_one_by_one(self, row, folder_name):

            print("row number :",row)



            try:

                google_signin_iframe = WebDriverWait(self.driver, 7).until(
                    EC.frame_to_be_available_and_switch_to_it((By.XPATH, '//iframe[contains(@src, "accounts.google.com/gsi/iframe")]'))
                )

                
                cross_button = WebDriverWait(self.driver, 5).until(
                    EC.element_to_be_clickable((By.CSS_SELECTOR, 'svg.Bz112c:nth-child(2)'))
                )
                cross_button.click()

                print("Clicked on the Continue button of Google sign-in popup.")
            except NoSuchElementException:
                print("Continue button not found. Google sign-in popup may not be present.")
            except TimeoutException:
                print("Continue button took too long to appear. Google sign-in popup may not be present.")

            # Switch back to the default content outside the iframe
            self.driver.switch_to.default_content()

            download_button = WebDriverWait(self.driver, 50).until(
                EC.visibility_of_element_located((By.XPATH, f"//div[@class='_sl-page-body-container_cfzbs_50']//div[2]//div[{row}]//div[3]"))
            )
            time.sleep(1)
           
            self.driver.execute_script("arguments[0].scrollIntoView(true);", download_button)

            # Move to the element
            ActionChains(self.driver).move_to_element(download_button).perform()
            
            time.sleep(1)
            actions = ActionChains(self.driver)
            actions.move_to_element(download_button).perform()

            download_button.click()
           


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

                        signin_popup_close_button = WebDriverWait(self.driver, 3).until(
                            EC.element_to_be_clickable((By.XPATH, "/html/body/div[4]/div/div/div/div[3]/button[1]/span"))
                        )
                        
                        # Click on the close button
                        signin_popup_close_button.click()
                        
                        print("Sign-in popup closed")
                    
            except TimeoutException:
                        # If the close button is not found, no sign-in popup is present
                        print("No sign-in popup close button found")

                      
                             

                       
    def wait_until_files_downloaded(self, num_files_expected,folder_path):
        while True:
            print("Inside wait_until_files_downloade block")
            
            # Get the list of files in the download directory
            downloaded_files = os.listdir(folder_path)
            
            time.sleep(10)
            # Filter the list to include only PDF files
            #pdf_files = [file for file in downloaded_files if file.endswith('.json')]
            pdf_files = [file for file in downloaded_files if file.endswith(('.json', '.xlsx'))]

            time.sleep(10)

            # Check if the number of PDF files matches the expected number
            if len(pdf_files) == num_files_expected:
                print("All PDFs have been downloaded.")
                break
            print("Not downloaded yet")

            # If not all files are downloaded, wait
            time.sleep(500)
           
    def move_downloaded_folders(self, folder_name, destination_path):

            source_path = os.path.join(self.custom_download_directory, folder_name)
            destination_folder = os.path.join(destination_path, folder_name)
            try:
                # Move the folder to the destination
                shutil.move(source_path, destination_folder)
                time.sleep(10)
                print(f"Folder '{folder_name}' moved successfully to '{destination_folder}'")
            except Exception as e:
                print(f"Error moving folder '{folder_name}': {e}")

if __name__ == "__main__":

    dropbox = DropBox(password="_riv9?u!i0Es")
 
    dropbox.click_on_Accept_button()


    dropbox.login_using_password()

    folders_to_download = ["devicedb", "shipdb"]
    for folder in folders_to_download:
            print("folder name - ",folder)
            dropbox.download_pdf(folder)
            dropbox.move_downloaded_folders(folder, "/Migration_Mg_Notification_PY_Code_1/Outbound/AutomationJSONs")


print("Program run successfully....!")
dropbox.driver.quit() 

