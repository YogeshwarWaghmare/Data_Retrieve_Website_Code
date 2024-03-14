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
 
 
 
class EPROC():
    def __init__(self):
        ### while when we create object of eproc class we intialize required variable
        self.sender_email = 'ypatel@phoenix.tech'
        self.sender_password = 'pfnkrpryqdmchjtk'
        self.recipient_emails = ['ypatel@phoenix.tech''sales@bitmapper.com']
        # self.recipient_emails = ['ypatel@phoenix.tech']
 
        self.custom_download_directory = '/home/ywaghmare/Data_Retrive_Websites/ISRO_Tenders'
        #/home/ywaghmare/Data_Retrive_Websites/ISRO_Tenders
        ## clear directory before downaload
        self.cleanup_download_directory()
        self.driver = self.set_up_chrome_driver(self.custom_download_directory)
        self.driver.get("https://eproc.isro.gov.in/home.html")
        self.subject = 'Downloaded PDF of Tenders/RFIs From Website- https://eproc.isro.gov.in/home.html'
        self.body = 'This is an auto-generated email.\n'
        self.body += 'Please find attached the zip folder containing the Tender and RFI PDFs.'
        self.body +="\n\n\nRegards,\nYamini Patel\nIntern"
    
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
 
    def set_up_chrome_driver(self,download_directory):
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
 
 
    def get_number_record(self,search_query):
        # Wait for the search bar element to be present
        wait = WebDriverWait(self.driver, 10)  # Adjust the timeout as needed
        search_bar = wait.until(EC.presence_of_element_located((By.XPATH, "//body/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/label[1]/input[1]")))
 
        search_bar.send_keys(search_query)
        search_bar.send_keys(Keys.RETURN)
 
        time.sleep(1)
 
        table_rows = self.driver.find_elements(By.XPATH, "//tbody/tr")
        
        # Check if the "No matching records found" message is present
        no_matching_records = self.driver.find_elements(By.XPATH, "//td[contains(text(),'No matching records found')]")
        
        if no_matching_records:
            print(f"No matching records found for keyword '{search_query}'.")
            return 0, self.driver
        
        num_rows = len(table_rows)
        print(f"Number of rows for keyword '{search_query}': {num_rows}")
 
        return num_rows, self.driver
 
 
    def download_pdf(self,search_query, row):
        print("Rows in a table: ", row)
 
        view_buttons = self.driver.find_elements(By.XPATH, f"//tbody/tr[{row}]/td[6]/div[1]/a[1]")
        if not view_buttons:
            print(f"Keyword '{search_query}' not found. Skipping to the next keyword.")
            return False
 
        print(len(view_buttons))
 
        view_buttons[0].click()
 
        time.sleep(5)
 
        # folder_path = '/home/ypatel/Documents/Web_Scrapping/Bitmapper/Tenders_1'
 
        # Wait for the file to be downloaded
        max_wait_time = 60  # Maximum time to wait for the file to be downloaded (in seconds)
        wait_interval = 1  # Check every 1 second
        elapsed_time = 0
 
        while elapsed_time < max_wait_time:
            # Find the most recent file downloaded
            downloaded_files = [filename for filename in os.listdir(self.custom_download_directory)]
            if downloaded_files:
                # Get the most recent file
                recent_file = max(downloaded_files, key=lambda f: os.path.getmtime(os.path.join(self.custom_download_directory, f)))
                recent_file_path = os.path.join(self.custom_download_directory, recent_file)
                print(f"Most recent file downloaded: {recent_file_path}")
 
                # Rename the file with a timestamp
                timestamp = int(time.time())
                new_filename = f"{search_query}_{row}.pdf"
                new_full_file_path = os.path.join(self.custom_download_directory, new_filename)
 
                os.rename(recent_file_path, new_full_file_path)
                print(f"File renamed to: {new_full_file_path}")
 
                return new_full_file_path
 
            time.sleep(wait_interval)
            elapsed_time += wait_interval
 
        print(f"Failed to download file for search query '{search_query}' within {max_wait_time} seconds.")
        return None
    
    def zip_folder(self):
        shutil.make_archive(self.custom_download_directory, 'zip', self.custom_download_directory)
 
 
 
 
 
if __name__ == "__main__":
    attachments = []
 
    ## Here we read all key word from keywords file and store it search_queries list
    with open('/home/ywaghmare/BitMapper_WebScrapping/keywords.txt', 'r') as file:
        search_queries = [line.strip() for line in file]
 
    pdf_paths = []
 
    ## here we have created EPROC object in which we set up chrome driver and go to https://eproc.isro.gov.in/home.html
    eproc=EPROC()
    
    ###### run loop for search keyword one by one and download all pdf
    for search_query in search_queries:
        # here we wait to page to load
        wait = WebDriverWait(eproc.driver, 10)  # Adjust the timeout as needed
        #  here we get serach bar object of website using xpath of search bar
        search_bar = wait.until(EC.presence_of_element_located((By.XPATH, "//body/div[1]/div[3]/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div[1]/label[1]/input[1]")))
 
        # Clear the search bar
        search_bar.clear()
 
        ## serach key word and get total number of pdf avaliable for that keyword
        numbers_of_pdf, driver = eproc.get_number_record(search_query)
 
        ## run loop to download all pdf avaliable for keyword one by one
        for i in range(0, min(11, numbers_of_pdf)):
 
            # here we download download pdf
            # rename pdf and store it in customse download path  
            attachment = eproc.download_pdf(search_query, i+1)
            # print(attachment)
            # if attachment:
            #     pdf_paths.append(attachment)
            # print(f"Waiting for 2 seconds before the next iteration...")
            time.sleep(2)
        print(f"Continuing with the next iteration...")
    ### compress custome download folder into zip file
    # eproc.zip_folder()
    
    # zip_filename = f'{eproc.custom_download_directory}.zip'
    # attachments.append(zip_filename)
    #doc_file='/home/ypatel/Documents/Web_Scrapping/Bitmapper/Testing_Analysis_new.xlsx'
    # attachments.append(doc_file)
    # print("List of attachments:", attachments)



    # if attachments:
    #     # send mail with attach downloaded zip file to mail
    #     eproc.send_email(attachments)
    
    # list=[doc_file]
    # if list:
    #     eproc.subject='Manual vs Automation Result Analysis for Website- https://eproc.isro.gov.in/home.html'
    #     eproc.body = 'This is an auto-generated email.\n'
    #     eproc.body += 'Please find attached  excel sheet containing result analysis.'
    #     eproc.body +="\n\n\nRegards,\nYamini Patel\nIntern"
    #     eproc.send_email(list)