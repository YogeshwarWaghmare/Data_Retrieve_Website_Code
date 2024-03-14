import base64
import shutil
import time
import os
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
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from PIL import Image
import pytesseract
from PIL import Image, ImageOps, ImageFilter
from selenium.common.exceptions import TimeoutException, NoSuchElementException
import re
from bs4 import BeautifulSoup
 
class DEFPROC():
    def __init__(self):
        print("in init")
        self.sender_email = 'ypatel@phoenix.tech'
        self.sender_password = 'pfnkrpryqdmchjtk'
        self.recipient_emails = ['ypatel@phoenix.tech']
        self.custom_download_directory = '/home/ywaghmare/Data_Retrive_Websites/DeFProc_Tenders'
        self.cleanup_download_directory()
        self.driver = self.set_up_chrome_driver(self.custom_download_directory)
        self.driver.get("https://defproc.gov.in/nicgep/app")
        # self.subject = 'Downloaded PDF of Tenders/RFIs From Website- https://defproc.gov.in/nicgep/app'
        # self.body = 'This is an auto-generated email.\n'
        # self.body += 'Please find attached the zip folder containing the Tender and RFI PDFs.'
        # self.body += "\n\n\nRegards,\nYamini Patel\nIntern"
        self.is_captcha = True
 
    def cleanup_download_directory(self):
        # Delete all files in the custom download directory
        for filename in os.listdir(self.custom_download_directory):
            file_path = os.path.join(self.custom_download_directory, filename)
            try:
                if os.path.isfile(file_path):
                    os.unlink(file_path)
                # elif os.path.isdir(file_path): shutil.rmtree(file_path)
            except Exception as e:
                print(f"Failed to delete {file_path}. Reason: {e}")
 
    def set_up_chrome_driver(self, download_directory):
        chrome_options = webdriver.ChromeOptions()
        prefs = {'download.default_directory': download_directory}
        chrome_options.add_experimental_option('prefs', prefs)
        return webdriver.Chrome(options=chrome_options)
 
    def send_email(self, attachment_paths):
        message = MIMEMultipart()
        message['From'] = self.sender_email
        message['To'] = ", ".join(self.recipient_emails)
        message['Subject'] = self.subject
 
        message.attach(MIMEText(self.body, 'plain'))
 
        for attachment_path in attachment_paths:
            with open(attachment_path, 'rb') as attachment:
                part = MIMEBase('application', 'octet-stream')
                part.set_payload(attachment.read())
                encoders.encode_base64(part)
                part.add_header('Content-Disposition', f"attachment; filename={os.path.basename(attachment_path)}")
                message.attach(part)
 
        try:
            with smtplib.SMTP('smtp-mail.outlook.com', 587) as smtp_server:
                smtp_server.ehlo()
                smtp_server.starttls()
                smtp_server.login(self.sender_email, self.sender_password)
                smtp_server.sendmail(self.sender_email, self.recipient_emails, message.as_string())
            print("Email sent successfully!")
        except Exception as e:
            print(f"Failed to send email: {e}")
 
    def get_number_record(self, search_query):
        try:
            # Wait for the search bar element to be present
            wait = WebDriverWait(self.driver, 10)  # Adjust the timeout as needed
            search_bar_xpath = "//input[@id='SearchDescription']"
            search_bar = wait.until(EC.presence_of_element_located((By.XPATH, search_bar_xpath)))
 
            search_bar.send_keys(search_query)
            search_bar.send_keys(Keys.RETURN)
 
            time.sleep(1)
 
            # Check if the "No Tenders found" message is present
            error_rows = self.driver.find_elements(By.CLASS_NAME, "error")
            if error_rows:
                print(f"No Tenders found for keyword '{search_query}'.")
                back_button = self.driver.find_element(By.ID, "DirectLink")
                back_button.click()
 
                return 0, self.driver
            # Replace 'your_table_id' with the actual ID of the table element
            table = self.driver.find_element(By.ID, 'table')
 
            # Find the table rows
            records = table.find_elements(By.XPATH, '//tr[@class="even" or @class="odd"]')
 
            num_rows = len(records)
            print(f"Number of rows for keyword '{search_query}': {num_rows}")
 
            return num_rows, self.driver
 
        except Exception as e:
            print(f"An error occurred: {e}")
            return 0, self.driver
 
    def solve_captcha(self, captcha_file_path):
        # Upload the CAPTCHA image to 2Captcha
        api_key = "194e36da223bfdd074c8f0e060d84a6d"
        with open(captcha_file_path, 'rb') as file:
            response = requests.post(
                'http://2captcha.com/in.php',
                files={'file': file},
                data={'key': "194e36da223bfdd074c8f0e060d84a6d", 'method': 'base64'}
            )
 
        # Parse the 2Captcha response
        request_result = response.text.split('|')
        if request_result[0] != 'OK':
            raise Exception(f"Failed to upload CAPTCHA: {request_result[0]}")
 
        # Get the CAPTCHA ID
        captcha_id = request_result[1]
 
        # Poll 2Captcha for the solved CAPTCHA
        for _ in range(30):  # Polling for a maximum of 30 seconds
            response = requests.get(f'http://2captcha.com/res.php?key={api_key}&action=get&id={captcha_id}')
            result = response.text
 
            if result.startswith('OK'):
                # Extract the solved CAPTCHA response
                return result.split('|')[1]
 
            time.sleep(5)  # Wait for 5 seconds before polling again
 
        raise Exception("Failed to solve CAPTCHA within the time limit")
 
    def scroll_down(self):
        # Scroll down using JavaScript
        self.driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(2)  # Add a short delay for the scroll to take effect
 
    def download_pdf(self, search_query, row,total_no, max_captcha_attempts=10):
        print("Rows in a table: ", row)
        time.sleep(2)
 
        if row == 1:
            view_buttons = self.driver.find_elements(By.ID, f"DirectLink_0")
        else:
            view_buttons = self.driver.find_elements(By.ID, f"DirectLink_0_{row - 2}")
        time.sleep(2)
        # Check if view_buttons is empty and handle the case if needed
        print(f"DirectLink_0_{row - 2}")
        print(view_buttons)
 
        view_buttons[0].click()
        time.sleep(5)
        # Scroll down before finding the download button
        self.scroll_down()
        if self.is_captcha:
            self.is_captcha = False
            download_button = self.driver.find_element(By.ID, "docDownoad")
            download_button.click()
            captcha_attempts = 0
            while captcha_attempts < max_captcha_attempts:
                try:
                    # After clicking the download button and getting CAPTCHA image
                    captcha_image = self.driver.find_element(By.ID, 'captchaImage')
                    WebDriverWait(self.driver, 10).until(EC.visibility_of(captcha_image))
 
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
                    time.sleep(2)
 
                    # Now you have the captcha text in the variable captcha_text
                    print(f"Captcha Text: {captcha_text}")
 
                    modified_captcha = re.sub(r'[^A-Za-z0-9]', '', captcha_text)
                    time.sleep(3)
                    print("Modified Captcha Text:", modified_captcha)
 
                    time.sleep(2)
 
                    # Enter the CAPTCHA text in the input field
                    captcha_input = self.driver.find_element(By.ID, 'captchaText')
                    captcha_input.clear()
                    captcha_input.send_keys(modified_captcha)
                    time.sleep(2)
 
                    # Wait for the CAPTCHA submission button to be present
                    submit_button = WebDriverWait(self.driver, 10).until(
                        EC.presence_of_element_located((By.ID, 'Submit'))
                    )
 
                    # Click the CAPTCHA submission button
                    submit_button.click()
                    print(self.driver.page_source.lower())
                    if "invalid captcha!" in self.driver.page_source.lower():
                        print("CAPTCHA validation failed. The CAPTCHA is invalid.")
                        captcha_attempts += 1
                        continue
                    else:
                        # # Wait for CAPTCHA validation
                        # WebDriverWait(self.driver, 10).until(
                        #     EC.url_contains("success")
                        # )
                        time.sleep(5)
                        print("CAPTCHA validation successful.")
                        break
 
                except  TimeoutException:
                    # Check if the page responds with an "invalid" message
                    if "invalid" in self.driver.page_source.lower():
                        print("CAPTCHA validation failed. The CAPTCHA is invalid.")
                    else:
                        print("CAPTCHA validation failed. Retrying...")
 
                    captcha_attempts += 1
            # time.sleep(20)
            if captcha_attempts == max_captcha_attempts:
                print(f"Failed to validate CAPTCHA after {max_captcha_attempts} attempts.")
                return None
        # Find all <a> elements on the page
 
        directlink_elements=self.driver.find_elements(By.XPATH, '//*[starts-with(@id, "DirectLink_")]')
        # # Print the links associated with each element
        # for element in directlink_elements:
        #     print(element.text)
        #     print(element.id)
 
        count=1
        for element in directlink_elements:
            print(element.text)
            link = element.get_attribute('href')
            element_text = element.text
            if "Tendernotice_"in element_text :
                is_error = False
 
                try:
                    # download_button = self.driver.find_element(By.ID, 'DirectLink_0')
                    element.click()
                    time.sleep(7)
 
                    # If the element is found, print a message
                    print("Download button is present on the page.")
 
                except NoSuchElementException:
                    is_error = True
                    # If the element is not found, print an error message
                    print("Download button is not present on the page.")
 
                # for i in range(0,len(informal_records)-1):
                #     if i==0:
                #         download_button = self.driver.find_element(By.ID, 'DirectLink_0')
                #         download_button.click()
                #         time.sleep(7)
                #     else:
                #         download_button = self.driver.find_element(By.ID, f'DirectLink_0_{i-1}')
                #         download_button.click()
                #         time.sleep(7)
 
                # folder_path = '/home/ypatel/Documents/Web_Scrapping/Bitmapper/Tenders_1'
 
                # Wait for the file to be downloaded
                max_wait_time = 60  # Maximum time to wait for the file to be downloaded (in seconds)
                wait_interval = 3  # Check every 1 second
                elapsed_time = 0
                if not is_error:
                    while elapsed_time < max_wait_time:
                        # Find the most recent file downloaded
                        downloaded_files = [filename for filename in os.listdir(self.custom_download_directory)]
                        if downloaded_files:
                            # Get the most recent file
                            recent_file = max(downloaded_files,
                                              key=lambda f: os.path.getmtime(
                                                  os.path.join(self.custom_download_directory, f)))
                            recent_file_path = os.path.join(self.custom_download_directory, recent_file)
                            print(f"Most recent file downloaded: {recent_file_path}")
 
                            new_filename = f"{search_query}_{total_no}_{count}.pdf"
                            count=count+1
                            new_full_file_path = os.path.join(self.custom_download_directory, new_filename)
 
                            os.rename(recent_file_path, new_full_file_path)
                            print(f"File renamed to: {new_full_file_path}")
                            # self.driver.find_element(By.CLASS_NAME, 'customButton_link').click()
                            #
                            # return new_full_file_path
                            break
 
                        time.sleep(wait_interval)
                        elapsed_time += wait_interval
        # self.driver.find_element(By.CLASS_NAME, 'customButton_link').click()
        #
        # return new_full_file_path
 
        self.driver.find_element(By.CLASS_NAME, 'customButton_link').click()
        # print(f"Failed to download file for search query '{search_query}' within {max_wait_time} seconds.")
        return None
 
    def zip_folder(self):
        shutil.make_archive(self.custom_download_directory, 'zip', self.custom_download_directory)
 
 
if __name__ == "__main__":
    attachments = []
 
    with open('/home/ywaghmare/BitMapper_WebScrapping/keywords.txt', 'r') as file:
        search_queries = [line.strip() for line in file]
 
    pdf_paths = []
    eproc = DEFPROC()
 
    for search_query in search_queries:
        # back_button = eproc.driver.find_element(By.ID,"DirectLink")
        # back_button.click()
 
        wait = WebDriverWait(eproc.driver, 10)  # Adjust the timeout as needed
        search_bar = wait.until(EC.presence_of_element_located((By.XPATH, "//input[@id='SearchDescription']")))
        # Clear the search bar
        search_bar.clear()
        numbers_of_pdf, driver = eproc.get_number_record(search_query)
        total_no=0
        if numbers_of_pdf!=0:
            # Locate the footer element using its class name
            footer_element = eproc.driver.find_element(By.CLASS_NAME,"list_footer")
 
            # Find all the elements with the ID "linkPage" within the footer
            link_page_elements = footer_element.find_elements(By.CSS_SELECTOR,"[id^='linkPage']")
 
            # Get the number of linkPage elements
            number_of_link_pages = len(link_page_elements)
 
            for i in range(0, numbers_of_pdf):
                # time.sleep(20)
                total_no = total_no + 1
                attachment = eproc.download_pdf(search_query, i + 1, total_no)
                print(attachment)
                if attachment:
                    pdf_paths.append(attachment)
                print(f"Waiting for 2 seconds before the next iteration...")
                time.sleep(5)
 
            # Print or use the number as needed
            print("Number of linkPage elements in the footer:", number_of_link_pages)
            for i in range(0,number_of_link_pages-1):
                # Find the element with the ID "linkFwd"
                link_fwd_element = driver.find_element(By.ID, "linkFwd")
                # Perform actions with the link_fwd_element if needed
                # For example, you can click on the element
                link_fwd_element.click()
                for i in range(0,numbers_of_pdf):
                    # time.sleep(20)
                    total_no=total_no+1
                    attachment = eproc.download_pdf(search_query, i + 1,total_no)
                    print(attachment)
                    if attachment:
                        pdf_paths.append(attachment)
                    print(f"Waiting for 2 seconds before the next iteration...")
                    time.sleep(5)
 
 
            back_button = eproc.driver.find_element(By.ID, "DirectLink")
            back_button.click()
 
        print(f"Continuing with the next iteration...")
 
    #eproc.zip_folder()
 
    # zip_filename = f'{eproc.custom_download_directory}.zip'
    # attachments.append(zip_filename)
    #doc_file='/home/ypatel/Documents/Web_Scrapping/Bitmapper/Testing_Analysis.xlsx'
    # attachments.append(doc_file)
    # print("List of attachments:", attachments)
    # if attachments:
    #     eproc.send_email(attachments)
 
    # list=[doc_file]
    # if list:
    #     eproc.subject='Manual vs Automation Result Analysis for Website- https://eproc.isro.gov.in/home.html'
    #     eproc.body = 'This is an auto-generated email.\n'
    #     eproc.body += 'Please find attached  excel sheet containing result analysis.'
    #     eproc.body +="\n\n\nRegards,\nYamini Patel\nIntern"
    #     eproc.send_email(list)
 
    driver.quit()