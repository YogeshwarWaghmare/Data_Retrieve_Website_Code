#Client_secret - 'IQm8Q~J1Pg8kDv.nJJZOFPIqFzjtY5PuHhBkZdip'
#app_id - 'b5b1f333-b03e-414f-925a-ed4b46d629a1'
#user_ID - BMITGEMSEC
#password - BITmap@12
import datetime
import re
import shutil
import glob
import pytesseract
from PIL import Image
import time
import os
import traceback
from selenium import webdriver
import requests
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
import smtplib
import zipfile
from PIL import Image, ImageEnhance
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from msal import ConfidentialClientApplication, ConfidentialClientApplication, PublicClientApplication, TokenCache
from selenium.common.exceptions import TimeoutException,NoAlertPresentException, NoSuchElementException, UnexpectedAlertPresentException,StaleElementReferenceException
 
class Gem_Captcha():
    
    def __init__(self, user_id, password, password_oneDrive):
        print("in init")
        self.pervious_rename_file=None
        self.user_id = user_id
        self.password = password
        self.password_oneDrive = password_oneDrive
        self.sender_email = 'ywaghmare@phoenix.tech'
        self.sender_password = 'kjcpdyvhcdmfzrfq'
        self.recipient_emails = ['ypatel@phoenix.tech','ywaghmare@phoenix.tech']
        self.custom_download_directory = '/home/ywaghmare/BitMapper_Gem_Website/GEM_Tenders'
        self.cleanup_download_directory()
        self.driver = None  # Initialize the driver attribute
        self.driver = self.set_up_chrome_driver(self.custom_download_directory)
        self.driver.get("https://gem.gov.in/")
       
        self.wait = WebDriverWait(self.driver, 10)  
        self.wait.until(EC.invisibility_of_element_located((By.CLASS_NAME, "ct-modal")))
 
        self.subject = 'Downloaded PDF of Tenders/RFIs From Website- https://gem.gov.in/'
        # self.body = 'THIS IS AN AUTO-GENERATED EMAIL.\n'
        # self.body += f"Please find the link to the uploaded folder on OneDrive:\n\n{link}"
        # self.body += "\n\n\nRegards,\nYogeshwar Waghmare And Yamini Patel\nIntern"
 
    def cleanup_download_directory(self):
        # Delete all files in the custom download directory
        try:
            shutil.rmtree(self.custom_download_directory)
            os.makedirs(self.custom_download_directory)  # Recreate the empty directory
            print(f"Download directory '{self.custom_download_directory}' cleaned up successfully.")
        except Exception as e:
            print(f"Failed to cleanup download directory. Reason: {e}")
 
 
    def set_up_chrome_driver(self,download_directory):
        chrome_options = webdriver.ChromeOptions()
        prefs = {'download.default_directory': download_directory}
        print("download directory name is :",download_directory)
        chrome_options.add_experimental_option('prefs', prefs)
        return webdriver.Chrome(options=chrome_options)

    
    # def send_email(self, onedrive_file_link):
    #     message = MIMEMultipart()
    #     message['From'] = self.sender_email
    #     message['To'] = ", ".join(self.recipient_emails)
    #     message['Subject'] = self.subject
        
    #     body = f"THIS IS AN AUTO-GENERATED EMAIL.\n\nPlease find the link to the uploaded folder on OneDrive:\n\n{onedrive_file_link}\n\nBest regards,\nYogeshwar Waghmare"
    #     message.attach(MIMEText(body, 'plain'))
 
    #     # for attachment_path in attachment_paths:
    #     #     with open(attachment_path, 'rb') as attachment:
    #     #         part = MIMEBase('application', 'octet-stream')
    #     #         part.set_payload(attachment.read())
    #     #         encoders.encode_base64(part)
    #     #         part.add_header('Content-Disposition', f"attachment; filename={os.path.basename(attachment_path)}")
    #     #         message.attach(part)
 
    #     try:
    #         with smtplib.SMTP('smtp-mail.outlook.com', 587) as smtp_server:
    #             smtp_server.ehlo()
    #             smtp_server.starttls()
    #             smtp_server.login(self.sender_email, self.sender_password)
    #             smtp_server.sendmail(self.sender_email, self.recipient_emails, message.as_string())
    #         print("Email sent successfully!")
    #     except Exception as e:
    #         print(f"Failed to send email: {e}")
 
    def get_number_record(self, search_query):
        try:
            WebDriverWait(self.driver, 100).until(
                EC.presence_of_element_located((By.ID, "bidCard"))
            )
            
            no_data_alerts = self.driver.find_elements(By.XPATH, "//div[contains(text(),'No data found')]")
 
            if no_data_alerts:
                print(f"No matching records found for keyword '{search_query}'.")
                return 0 # Return 0 when no records are found
 
            WebDriverWait(self.driver, 100).until(
                EC.presence_of_element_located((By.CLASS_NAME, "card-body"))
            )
 
            # Get the number of records
            records_element = self.driver.find_element(By.CLASS_NAME, "totalRecord")
            records_text = records_element.text
            total_records = int(records_text.split()[-2])  # Extracting the number of records
 
            return total_records
 
        except TimeoutException:
            # print("Timed out waiting for elements.")
            # return 0
            print(f"Timed out waiting for table to load for keyword '{search_query}'. Exiting the code.")
            raise SystemExit
 
        except Exception as e:
            print(f"An error occurred while getting the number of records: {e}")
            return 0
 
    def zip_folder(self):
        shutil.make_archive(self.custom_download_directory, 'zip', self.custom_download_directory)   
 
 
    # Function to process each page
    def process_page(self,download_directory):
        # Locate the table and iterate through records
        #table = self.driver.find_element(By.CLASS_NAME, "col-md-10.bids")
        print("loading table")
        try:

            table = WebDriverWait(self.driver, 1000).until(
                EC.presence_of_element_located((By.CLASS_NAME, "col-md-10.bids"))
            )
            #records = table.find_elements(By.CLASS_NAME, "card")
            records = WebDriverWait(self.driver, 200).until(
                EC.presence_of_all_elements_located((By.CLASS_NAME, "card"))
            )
    
            for i, record in enumerate(records):
                    time.sleep(3)
                    data = self.extract_data(record,download_directory)
                    print(data)
                    print("---------------")


        except TimeoutException:
            print(f"Timeout exception occurred...refreshing the page")
            
            return None

    def check_download_status(self, download_directory):

        downloaded_files = [filename for filename in os.listdir(download_directory)]

        if not downloaded_files:
            return False, None  # No PDF file found in the directory
        
        print("pervious filename is: ",self.pervious_rename_file) 

        recent_file = max(downloaded_files, key=lambda f: os.path.getmtime(os.path.join(download_directory, f)))

        if self.pervious_rename_file == recent_file:
            print("Inside comparing new with last file")
            return True, None
          
        recent_file_path = os.path.join(download_directory, recent_file)
        print("recent filepath is :",recent_file_path)
        time.sleep(2)
        # Check if the PDF is still being downloaded (has a '.crdownload' extension)
        if recent_file.endswith('.crdownload'):
            return True,None  # PDF is still downloading
        else:
            return False,recent_file_path # PDF download is complete
        
   
    def extract_data(self,record,download_directory, max_attempts=3):

        print("extract data from each row")

        try:

            bid_number = WebDriverWait(self.driver, 300).until(
                    EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover")),
                    EC.element_to_be_clickable((By.CLASS_NAME, "bid_no_hover"))
                    
            )
            time.sleep(2)
            # Click to download the PDF
            bid_number_element = record.find_element(By.CLASS_NAME, "bid_no_hover")

            if bid_number_element.is_displayed():
                # Click to download the PDF
                    
                bid_number_element.click()
            else:
                time.sleep(20)
                bid_number_element.click()

              # bid_number = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            bid_number_text = WebDriverWait(self.driver, 80).until(
                EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover"))
            )
            bid_number_text = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            time.sleep(3)

            # Wait for the file to be downloaded
            max_wait_time = 100  # Maximum time to wait for the file to be downloaded (in seconds)
            wait_interval = 4  # Check every 1 second
            elapsed_time = 0

            while elapsed_time < max_wait_time:
                try:
                     # Check if an alert is present
                    alert = self.driver.switch_to.alert
                    alert.accept()  # Dismiss the alert
                    print("Alert handled. Re-downloading the PDF.")
                    time.sleep(4)
                    bid_number_element.click()  # Re-attempt to download the PDF
                  
                except NoAlertPresentException:
                        
                        download_status, recent_file_path = self.check_download_status(download_directory)
                        if not download_status:
                            print(f"Download complete: {recent_file_path}")
                            initial_new_filename = f"{bid_number_text}.pdf"
                            last_seven_digits = initial_new_filename[-11:]
                            new_filename = f"GeM_{last_seven_digits}"
                            print("new filename is :",new_filename)
                            time.sleep(2)
                            new_full_file_path = os.path.join(download_directory, new_filename)
                            print("new filepath is :", new_full_file_path)
                            time.sleep(2)

                            os.rename(recent_file_path, new_full_file_path)
                            print(f"File renamed to: {new_full_file_path}")
                            break  # Exit the loop if the download is complete



                time.sleep(wait_interval)
                elapsed_time += wait_interval

            if elapsed_time >= max_wait_time and max_attempts > 0:
                # If the download takes too long, attempt to click on the same bid_number again
                print("Download taking too long. Re-attempting to click on bid_number.")
                self.extract_data(record, download_directory, max_attempts - 1)


            self.pervious_rename_file=new_filename

            return {
                "Bid Number": bid_number_text
            }

            # # bid_number = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            # bid_number_text = WebDriverWait(self.driver, 80).until(
            #     EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover"))
            # )
            # bid_number_text = record.find_element(By.CLASS_NAME, "bid_no_hover").text

            
            # return {
            #         "Bid Number": bid_number_text
            #     }
        
        except Exception as e:
            print(f"Error while downloading PDF for search query : {e}")
            traceback.print_exc()
            return None

    def login(self):
 
        login_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//header/section[2]/section[1]/div[1]/div[1]/div[3]/ul[1]/li[4]/a[1]")))
        login_button.click()
 
        for attempt in range(1, 10):
            try:
                print(f"Attempting login - Attempt {attempt}")
                time.sleep(3)  
                user_id_input = self.wait.until(EC.presence_of_element_located((By.XPATH, "//input[@id='loginid']")))
                user_id_input.clear()  
                user_id_input.send_keys(self.user_id)
 
                captcha_image = self.driver.find_element(By.XPATH, "//img[@id='captcha1']")
                WebDriverWait(self.driver, 60).until(EC.visibility_of(captcha_image))
 
                            # Get the location and size of the captcha image
                location = captcha_image.location
                size = captcha_image.size
 
                self.driver.save_screenshot("screenshot.png")
 
                        # Crop the screenshot to get only the captcha image
                im = Image.open("screenshot.png")
                left = location['x']
                top = location['y']
                right = location['x'] + size['width']
                bottom = location['y'] + size['height']
                im = im.crop((left, top, right, bottom))
               
 
                im.save("captcha.png")
 
                            # Use pytesseract to extract text from the captcha image
                captcha_text = pytesseract.image_to_string(im, config='--psm 8')
                time.sleep(5)
 
                          
                print(f"Captcha Text: {captcha_text}")
                     
                modified_captcha = re.sub(r'[^A-Z0-9]', '', captcha_text)
                time.sleep(3)
 
                print("Modified Captcha Text:", modified_captcha)
                     
                time.sleep(2)
 
                captcha_input = self.driver.find_element(By.XPATH, "//input[@id='captcha_math']")
                captcha_input.send_keys(modified_captcha)
                time.sleep(2)
 
 
                Submit_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='arxLoginSubmit']")))
                Submit_button.click()
 
                    
                password_input = self.wait.until(EC.presence_of_element_located((By.XPATH, "//input[@id='password']")))
                    
 
                password_input.clear()
                password_input.send_keys(self.password)
                time.sleep(3)
                

                    # Submit the form
                submit_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='arxLoginSubmit']")))
                submit_button.click()
                print("Login Successful....!")
                    #time.sleep(5)
                break
              

 
            except Exception as e:
                print(f"Login unsuccessful. Retrying... Error: {e}")
            
 
    def download_latest_bid(self):
        time.sleep(3)
        try:
                # Wait for the alert to be present
                # Wait for the "OK" button to be clickable
                
                ok_button = WebDriverWait(self.driver, 30).until(
                    EC.element_to_be_clickable((By.CLASS_NAME, "dialogBtnOk"))
                )
                time.sleep(2)
                # Click on the "OK" button
                ok_button.click()
 
                time.sleep(2)  # You can adjust this sleep time if needed

        except TimeoutException:
                print("Alert not present within the expected time. Continuing without handling.")
          
        bids_link = WebDriverWait(self.driver, 200).until(
            EC.element_to_be_clickable((By.XPATH, "//body/div[@id='HEADER_DIV']/section[3]/section[1]/div[1]/div[1]/div[2]/ul[1]/li[5]/a[1]"))
        )

        bids_link.click()
        time.sleep(3)
        
        
        # Assuming there's a link or button to go to the List of Bids
        list_of_bids_link = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//body/div[@id='HEADER_DIV']/section[3]/section[1]/div[1]/div[1]/div[2]/ul[1]/li[5]/ul[1]/li[1]/a[1]")))
        list_of_bids_link.click()
        time.sleep(5)
        self.contains_search()
       
        
    def contains_search(self):
        print("inside contains search function")
        base_download_directory = self.custom_download_directory+"/Contains_Search/"
        # Use os.makedirs to create the folder along with any necessary parent folders
        os.makedirs(base_download_directory, exist_ok=True)
 
        none_selected_button = WebDriverWait(self.driver, 100).until(EC.element_to_be_clickable((By.XPATH, "//span[contains(text(),'None selected')]")))
 
        none_selected_button.click()
        time.sleep(3)
 
                # Click on the checkboxes labeled as the first and second options
        first_checkbox = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//body/div[2]/div[5]/div[1]/div[1]/div[5]/span[1]/div[1]/ul[1]/li[1]/a[1]/label[1]")))
        second_checkbox = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//body/div[2]/div[5]/div[1]/div[1]/div[5]/span[1]/div[1]/ul[1]/li[2]/a[1]/label[1]")))
 
                # Check if the checkboxes are not selected before clicking
        if not first_checkbox.is_selected():
            first_checkbox.click()
 
        time.sleep(3)
 
        if not second_checkbox.is_selected():
            second_checkbox.click()
            time.sleep(3)

        time.sleep(2)     
        # Open FromEndDate calendar and select today's date
        from_end_date_input = self.driver.find_element(By.XPATH, "//input[@id='fromEndDate']")
        time.sleep(3)
 
        # Click on the input to open the calendar
        from_end_date_input.click()
 
        time.sleep(2)
 
        # # Assuming today's date is present in the calendar and can be selected directly
        # today_date_xpath = "//a[@data-date='" + time.strftime("%Y-%m-%d") + "']"
        # today_date_element = self.driver.find_element(By.XPATH, today_date_xpath)
        # today_date_element.click()
        # time.sleep(3)
        # Get today's day in two-digit format (e.g., '11' for March 11)
        today_day = time.strftime("%d")
        print("todays day is :",today_day)
        time.sleep(2)
        # Build the XPATH to find an element with today's day //a[contains(text(),'12')]
        today_date_xpath = f"//a[contains(text(),'{today_day}') and @href='#']"
        time.sleep(2)
        # Now you can use this XPATH to find the element in your Selenium code
        today_date_element = WebDriverWait(self.driver, 20).until(
         EC.element_to_be_clickable((By.XPATH, today_date_xpath))
        )
        today_date_element.click()
    
        # Open ToEndDate calendar and select a date 4 days from today
        to_end_date_input = self.driver.find_element(By.XPATH, "//input[@id='toEndDate']")
 
        # Click on the input to open the calendar
        to_end_date_input.click()
        time.sleep(3)
 
     
       
        # Calculate the date 4 days from today
        four_days_later = (datetime.datetime.now() + datetime.timedelta(days=4))
        print("four days later from today ", four_days_later)
        time.sleep(3)
        # Get only the day part in two-digit format (e.g., '15' for March 15)
        four_days_later_day = four_days_later.strftime("%d")
        print("four days later from today only day ", four_days_later_day)
        time.sleep(3)
        # Build the XPATH to find an element with the day four days later
        four_days_later_xpath = f"//a[contains(text(),'{four_days_later_day}') and @href='#']"
        time.sleep(3)
                # Wait for the element to be clickable
        four_days_later_element = WebDriverWait(self.driver, 20).until(
            EC.element_to_be_clickable((By.XPATH, four_days_later_xpath))
        )

        four_days_later_element.click()
        #sort_dropdown = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='currentSort']")))
        sort_dropdown = WebDriverWait(self.driver, 120).until(
            EC.visibility_of_element_located((By.XPATH, "//button[@id='currentSort']"))
        )
        ActionChains(self.driver).move_to_element(sort_dropdown).click().perform()
        time.sleep(3)
 
        #latest_first_option = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//a[@id='Bid-Start-Date-Latest']")))
 
        #OR
 
        latest_first_option = WebDriverWait(self.driver, 50).until(
            EC.element_to_be_clickable((By.XPATH, "//a[@id='Bid-Start-Date-Latest']"))
        )
 
        # #OR
        # self.retry_click(latest_first_option)
        
        latest_first_option.click()
        time.sleep(3)
 
 
        try:
            for search_Keyword in search_Keywords:
 
 
                time.sleep(4)
                keyword_input = self.wait.until(EC.presence_of_element_located((By.XPATH, "//input[@id='searchBid']")))
                keyword_input.clear()
                keyword_input.send_keys(search_Keyword)
                time.sleep(3)
            
 
                search_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='searchBidRA']")))
                search_button.click()
                time.sleep(5)
                                # numbers_of_pdf, _ = eproc.get_number_record(search_query)
                # Wait for the search results to load
                total_records=self.get_number_record(search_query=search_Keyword)
                print(f"Total records : {total_records} of the keyword {search_Keyword}")
 
                
             
                if total_records!=0:
                    download_directory = os.path.join(base_download_directory, search_Keyword)
                    # Ensure that the download directory exists
                    os.makedirs(download_directory, exist_ok=True)
                    self.set_download_directory(download_directory)
 
                    self.process_page(download_directory)
                    # Navigate through pagination
                    while True:
                        try:
                            
                            if total_records <= 5:
                                print("Single page. Breaking the loop.")
                                break
  
                            next_page_link = WebDriverWait(self.driver, 30).until(
                                EC.presence_of_element_located((By.CLASS_NAME, "next"))
                            )

                            next_page_link.click()
                            time.sleep(3)
                            self.scroll_up()
                            print("Next button is clicked for contains keyword...!")
 
                            # Wait for the table to load on the next page
                            time.sleep(5)
 
                            # Process the current page
                            self.process_page(download_directory)
                            time.sleep(10)
                            # Check if the "Next" button has the class "current next"
                            next_button = self.driver.find_element(By.CLASS_NAME, "next")
                            if "current" in next_button.get_attribute("class") and "next" in next_button.get_attribute("class"):
                                print("Reached the last page. Breaking the loop.")
                                break

                        except NoSuchElementException as e:
                            print(f"error occur for contains search  : {e}")
                            # Break the loop if there is no 'Next' link
                            break
                # time.sleep(10)
        
        except Exception as e:
          print(f"An error occurred: {e}")
          traceback.print_exc()

        self.exact_search()
          
                # time.sleep(10)
    def process_page_for_exact(self,download_directory):
         # Function to process each page
    
        # Locate the table and iterate through records
        #table = self.driver.find_element(By.CLASS_NAME, "col-md-10.bids")
        print("loading table")
        try:

            table = WebDriverWait(self.driver, 1000).until(
                EC.presence_of_element_located((By.CLASS_NAME, "col-md-10.bids"))
            )
            #records = table.find_elements(By.CLASS_NAME, "card")
            records = WebDriverWait(self.driver, 200).until(
                EC.presence_of_all_elements_located((By.CLASS_NAME, "card"))
            )
    
            for i, record in enumerate(records):
                    time.sleep(3)
                    data = self.extract_data(record,download_directory)
                    print(data)
                    print("---------------")


        except TimeoutException:
            print(f"Timeout exception occurred...")
            return None
        
    def check_download_status_for_exact(self, download_directory):

        downloaded_files = [filename for filename in os.listdir(download_directory)]

        if not downloaded_files:
            return False, None  # No PDF file found in the directory
        
        print("pervious filename is: ",self.pervious_rename_file) 

        recent_file = max(downloaded_files, key=lambda f: os.path.getmtime(os.path.join(download_directory, f)))

        if self.pervious_rename_file == recent_file:
            print("Inside comparing new with last file")
            return True, None
          
        recent_file_path = os.path.join(download_directory, recent_file)
        print("recent filepath is :",recent_file_path)
        time.sleep(2)
        # Check if the PDF is still being downloaded (has a '.crdownload' extension)
        if recent_file.endswith('.crdownload'):
            return True,None  # PDF is still downloading
        else:
            return False,recent_file_path # PDF download is complete


    def extract_data_for_exact(self,record,download_directory, max_attempts=3):
        print("extract data from each row of exact_search")

        try:
            bid_number_element = WebDriverWait(self.driver, 300).until(
                EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover")),
                EC.element_to_be_clickable((By.CLASS_NAME, "bid_no_hover"))
            )
            
            bid_number_element = record.find_element(By.CLASS_NAME, "bid_no_hover")

            if bid_number_element.is_displayed():
                # Click to download the PDF
                    
                bid_number_element.click()
            else:
                time.sleep(20)
                bid_number_element.click()

              # bid_number = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            bid_number_text = WebDriverWait(self.driver, 80).until(
                EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover"))
            )
            bid_number_text = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            time.sleep(3)

            # Wait for the file to be downloaded
            max_wait_time = 100  # Maximum time to wait for the file to be downloaded (in seconds)
            wait_interval = 4  # Check every 1 second
            elapsed_time = 0

            while elapsed_time < max_wait_time:
                try:
                     # Check if an alert is present
                    alert = self.driver.switch_to.alert
                    alert.accept()  # Dismiss the alert
                    print("Alert handled. Re-downloading the PDF.")
                    time.sleep(4)
                    bid_number_element.click()  # Re-attempt to download the PDF
                  
                except NoAlertPresentException:
                        
                        download_status, recent_file_path = self.check_download_status_for_exact(download_directory)
                        if not download_status:
                            print(f"Download complete: {recent_file_path}")
                            initial_new_filename = f"{bid_number_text}.pdf"
                            last_seven_digits = initial_new_filename[-11:]
                            new_filename = f"GeM_{last_seven_digits}"
                            print("new filename is :",new_filename)
                            time.sleep(2)
                            new_full_file_path = os.path.join(download_directory, new_filename)
                            print("new filepath is :", new_full_file_path)
                            time.sleep(2)

                            os.rename(recent_file_path, new_full_file_path)
                            print(f"File renamed to: {new_full_file_path}")
                            break  # Exit the loop if the download is complete



                time.sleep(wait_interval)
                elapsed_time += wait_interval

            if elapsed_time >= max_wait_time and max_attempts > 0:
                # If the download takes too long, attempt to click on the same bid_number again
                print("Download taking too long. Re-attempting to click on bid_number.")
                self.extract_data(record, download_directory, max_attempts - 1)


            self.pervious_rename_file=new_filename

            return {
                "Bid Number": bid_number_text
            }

            # # bid_number = record.find_element(By.CLASS_NAME, "bid_no_hover").text
            # bid_number_text = WebDriverWait(self.driver, 80).until(
            #     EC.visibility_of_element_located((By.CLASS_NAME, "bid_no_hover"))
            # )
            # bid_number_text = record.find_element(By.CLASS_NAME, "bid_no_hover").text

            
            # return {
            #         "Bid Number": bid_number_text
            #     }
        
        except Exception as e:
            print(f"Error while downloading PDF for search query : {e}")
            traceback.print_exc()
            return None


    def exact_search(self):
        print("In exact search function....")
 
        time.sleep(5)
        dropdown_button = WebDriverWait(self.driver, 100).until(
            EC.element_to_be_clickable((By.CLASS_NAME, "searchtype"))
        )
        time.sleep(3)
        # Click the dropdown button to open the options
        dropdown_button.click()
        exact_search_option = WebDriverWait(self.driver, 20).until(
            EC.element_to_be_clickable((By.XPATH, "//a[text()='Exact Search']"))
        )
        time.sleep(3)
        exact_search_option.click()
        time.sleep(3)
        base_download_directory = self.custom_download_directory+"/Exact_Search/"
 
        #none_selected_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//span[contains(text(),'None selected')]")))
 
        none_selected_button = WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//span[contains(text(),'BOQ, Customitem prod test 7OCT')]")))
 
        none_selected_button.click()
        time.sleep(4)
 
             # Click on the checkboxes labeled as the first and second options
        first_checkbox = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//body/div[2]/div[5]/div[1]/div[1]/div[5]/span[1]/div[1]/ul[1]/li[1]/a[1]/label[1]")))
        ActionChains(self.driver).double_click(first_checkbox).perform()
 
        time.sleep(3)
 
        second_checkbox = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//body/div[2]/div[5]/div[1]/div[1]/div[5]/span[1]/div[1]/ul[1]/li[2]/a[1]/label[1]")))
        ActionChains(self.driver).double_click(second_checkbox).perform()
 
           # Open FromEndDate calendar and select today's date
        from_end_date_input = self.driver.find_element(By.XPATH, "//input[@id='fromEndDate']")

        # Click on the input to open the calendar
        from_end_date_input.click()

        time.sleep(2)

        # # Assuming today's date is present in the calendar and can be selected directly
        # today_date_xpath = "//a[@data-date='" + time.strftime("%Y-%m-%d") + "']"
        # today_date_element = self.driver.find_element(By.XPATH, today_date_xpath)
        # today_date_element.click()
        # time.sleep(3)
        # Get today's day in two-digit format (e.g., '11' for March 11)
        today_day = time.strftime("%d")

        # Build the XPATH to find an element with today's day
        today_date_xpath = "//a[contains(text(),'" + today_day + "')]"

        # Now you can use this XPATH to find the element in your Selenium code
        today_date_element = self.driver.find_element(By.XPATH, today_date_xpath)
        today_date_element.click()

        # Open ToEndDate calendar and select a date 4 days from today
        to_end_date_input = self.driver.find_element(By.XPATH, "//input[@id='toEndDate']")

        # Click on the input to open the calendar
        to_end_date_input.click()
        time.sleep(3)

        # # Calculate the date 4 days from today
        # four_days_later = (datetime.datetime.now() + datetime.timedelta(days=4)).strftime("%Y-%m-%d")

        # # Select the calculated date in the calendar
        # four_days_later_xpath = "//td[@data-date='" + four_days_later + "']"
        # four_days_later_element = self.driver.find_element(By.XPATH, four_days_later_xpath)
        # four_days_later_element.click()

                # Calculate the date 4 days from today
        four_days_later = (datetime.datetime.now() + datetime.timedelta(days=4))

        # Get only the day part in two-digit format (e.g., '15' for March 15)
        four_days_later_day = four_days_later.strftime("%d")

        # Build the XPATH to find an element with the day four days later
        four_days_later_xpath = "//a[contains(text(),'" + four_days_later_day + "')]"
        four_days_later_element = self.driver.find_element(By.XPATH, four_days_later_xpath)
        four_days_later_element.click()
 
        time.sleep(10)
      
         #sort_dropdown = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='currentSort']")))
        sort_dropdown = WebDriverWait(self.driver, 120).until(
            EC.visibility_of_element_located((By.XPATH, "//button[@id='currentSort']"))
        )
        ActionChains(self.driver).move_to_element(sort_dropdown).click().perform()
        time.sleep(3)
 
        #latest_first_option = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//a[@id='Bid-Start-Date-Latest']")))
 
        #OR
 
        latest_first_option = WebDriverWait(self.driver, 20).until(
            EC.element_to_be_clickable((By.XPATH, "//a[@id='Bid-Start-Date-Latest']"))
        )
        time.sleep(2)
       
        latest_first_option.click()
        time.sleep(12)
 
        for search_Keyword in search_Keywords:
            download_directory = os.path.join(base_download_directory, search_Keyword)
            os.makedirs(download_directory, exist_ok=True)
            self.set_download_directory(download_directory)
 
            keyword_input = self.wait.until(EC.presence_of_element_located((By.XPATH, "//input[@id='searchBid']")))
            time.sleep(3)
            keyword_input.clear()
            keyword_input.send_keys(search_Keyword)
            time.sleep(3)
            search_button = WebDriverWait(self.driver, 100).until(EC.element_to_be_clickable((By.XPATH,"//button[@id='searchBidRA']")))
           # search_button = self.wait.until(EC.element_to_be_clickable((By.XPATH, "//button[@id='searchBidRA']")))
            search_button.click()
            time.sleep(10)
                
                # Wait for the search results to load
            total_records=self.get_number_record(search_query=search_Keyword)
            print(total_records)
            
            if total_records!=0:
              
                self.process_page_for_exact(download_directory)
                # Navigate through pagination
                while True:
                    try:
                            
                            if total_records <= 5:
                                print("Single page. Breaking the loop.")
                                break
  
                        # Click on the next page link
                            next_page_link = WebDriverWait(self.driver, 30).until(
                                EC.presence_of_element_located((By.CLASS_NAME, "next"))
                            )
                            next_page_link.click()
                            time.sleep(3)
                            self.scroll_up()
                            print("Next button is clicked for contains keyword...!")
 
                            # Wait for the table to load on the next page
                            time.sleep(5)
 
                            # Process the current page
                            self.process_page_for_exact(download_directory)
                            time.sleep(10)
                            # Check if the "Next" button has the class "current next"
                            next_button = self.driver.find_element(By.CLASS_NAME, "next")
                            if "current" in next_button.get_attribute("class") and "next" in next_button.get_attribute("class"):
                                print("Reached the last page. Breaking the loop.")
                                break
                            
                    except NoSuchElementException as e:
                        print(f"error occur at exact search  : {e}")
                        # Break the loop if there is no 'Next' link
                        break
 
    def scroll_up(self):
        # Use JavaScript to scroll up
        script = "window.scrollTo(0, -document.body.scrollHeight);"
        self.driver.execute_script(script)
            
    
    def set_download_directory(self, download_directory):
        prefs = {'download.default_directory': download_directory}
        self.driver.command_executor._commands["send_command"] = (
            "POST",
            '/session/$sessionId/chromium/send_command',
        )
        params = {'cmd': 'Page.setDownloadBehavior', 'params': {'behavior': 'allow', 'downloadPath': download_directory}}
        self.driver.execute("send_command", params)
 
 
    def close_driver(self):
        self.driver.quit()
 
 
if __name__ == "__main__":
        attachments = []
 
        with open("/home/ywaghmare/BitMapper_WebScrapping/keywords.txt", "r") as file:
            search_Keywords = [line.strip() for line in file]
 
        pdf_paths= []
        gem = Gem_Captcha(user_id="BMITGEMSEC", password="BITmap@12", password_oneDrive="Yogi@9860")
 
        
        try:
         
            gem.login()
            time.sleep(4)
            gem.download_latest_bid()
           # gem.zip_folder()
           # zip_filename = f'{gem.custom_download_directory}.zip'
            # gem.upload_to_onedrive()

            gem.close_driver()
    
        except Exception as e:
            print(f"An error occurred: {e}")
            traceback.print_exc()
 
    # finally:
    #     # gem.exact_search()
 
 
print("Program run successfully....!")
gem.driver.quit()

