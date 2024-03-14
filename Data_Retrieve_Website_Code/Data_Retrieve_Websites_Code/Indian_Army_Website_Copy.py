import time
import requests
import os
from selenium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from email.mime.base import MIMEBase
from email import encoders
import smtplib
import zipfile


def send_email(sender_email, sender_password, recipient_emails, subject, body, attachment_paths):
    message = MIMEMultipart()
    message['From'] = sender_email
    message['To'] = ','.join(recipient_emails)
    message['Subject'] = subject
    
    message.attach(MIMEText(body, 'plain'))

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
            smtp_server.login(sender_email, sender_password)
            smtp_server.sendmail(sender_email, recipient_emails, message.as_string())
        print("Email sent successfully!")
    except Exception as e:
        print(f"Failed to send email: {e}")

def get_number_record(search_Keyword, driver, tender=True):
    if tender:
        driver.get("https://indianarmy.nic.in/Tenderrfi/Tender")
        search_bar_xpath = "//input[@type='search']"
        
    else:
        driver.get("https://indianarmy.nic.in/Tenderrfi/RFI")
        search_bar_xpath = "//input[@type='search']"
        

    driver.maximize_window()

    try:
        search_bar = WebDriverWait(driver, 10).until(
            EC.visibility_of_element_located((By.XPATH, search_bar_xpath))
        )
    except TimeoutException as e:
        print(f"TimeoutException occurred while waiting for the search bar: {e}")
        return 0, driver

    search_bar.send_keys(search_Keyword)
    search_bar.send_keys(Keys.RETURN)

    time.sleep(1)

    table_rows = driver.find_elements(By.XPATH, "//tbody/tr")

    # Check if the "No matching records found" message is present
    no_matching_records = driver.find_elements(By.XPATH, "//td[contains(text(),'No matching records found')]")
        
    if no_matching_records:
        print(f"No matching records found for keyword '{search_Keyword}'.")
        return 0, driver
        
    num_rows = len(table_rows)
    print(f"Number of rows for keyword '{search_Keyword}': {num_rows}")
 
    return num_rows, driver

def download_pdf(search_Keyword, row, driver, tender=True):
    print("Row in a table: ", row)

    if tender:
        name_xpath = f"//tbody/tr[{row}]/td[2]"
        view_buttons_xpath = f"//tbody/tr[{row}]/td[5]/a[1]"
    else:
        name_xpath = f"//tbody/tr[{row}]/td[2]"
        view_buttons_xpath = f"//tbody/tr[{row}]/td[5]/a[1]"

    name_element = driver.find_element(By.XPATH, name_xpath)
    name_text = name_element.text.lower()
    print("name text of pdf : ", name_text)

    # Check if the search keyword is exactly present in the name
    if f" {search_Keyword.lower()} " not in f" {name_text} ":
        print(f"Keyword '{search_Keyword}' not found in the name. Skipping to the next keyword.")
        return False
    
    view_buttons = driver.find_elements(By.XPATH, view_buttons_xpath)
    
    if not view_buttons:
        print(f"Keyword '{search_Keyword}' not found. Skipping to the next keyword.")
        return False

    print("Length of view buttons: ", len(view_buttons))

    ActionChains(driver).move_to_element(view_buttons[0]).click().perform()

    time.sleep(1)
    
    download_links = driver.find_elements(By.XPATH, "//a[contains(text(),'Download')]")
    if not download_links:
        print(f"Download link not found for keyword '{search_Keyword}'. Skipping to the next keyword.")
        return False

    download_link = download_links[0].get_attribute("href")
    print(f"Download link: {download_link}")

    folder_path = '/home/ywaghmare/Data_Retrive_Websites/Indian_Army_Tenders/Indian_Army_Tenders' if tender else '/home/ywaghmare/Data_Retrive_Websites/Indian_Army_Tenders/Indian_Army_RFI'

    response = requests.get(download_link)

    if response.status_code == 200:
        filename = f"{search_Keyword.replace(' ', '_').lower()}_downloaded_file_{row}.pdf"

        full_file_path = os.path.join(folder_path, filename)
        print(f"Full file path: {full_file_path}")

        # Check if the file already exists
        if os.path.exists(full_file_path):
            print(f"File '{full_file_path}' already exists. Replacing it.")
            os.remove(full_file_path)

        with open(full_file_path, 'wb') as f:
            f.write(response.content)

        print(f"File downloaded and saved to: {full_file_path}")

    else:
        print(f"Failed to download file for search Keyword '{search_Keyword}'. Status code: {response.status_code}")
        return None

   
    return full_file_path


options = webdriver.ChromeOptions()
options.page_load_strategy = 'normal'  # or 'eager'
driver = webdriver.Chrome(options=options)
#driver = webdriver.Chrome()

sender_email = 'ywaghmare@phoenix.tech'
sender_password = 'kjcpdyvhcdmfzrfq'
#recipient_emails = ['sales@bitmapper.com','smandke@phoenix.tech','mdesai@phoenix.tech','mmulik@phoenix.tech','ywaghmare@phoenix.tech']
recipient_emails = ['sales@bitmapper.com','ywaghmare@phoenix.tech']



with open("/home/ywaghmare/BitMapper_WebScrapping/keywords.txt", "r") as file:
    search_Keywords_tenders = [line.strip() for line in file]

# List to store downloaded PDF paths for tenders
pdf_paths_tenders = []
attachments_tenders = []

for search_Keyword_tender in search_Keywords_tenders:
    numbers_of_pdf_tender, driver = get_number_record(search_Keyword_tender, driver, tender=True)
    print("Number of pdfs for tenders are: ", numbers_of_pdf_tender)

    for i_tender in range(0, min(20, numbers_of_pdf_tender)):
        attachment_tender = download_pdf(search_Keyword_tender, i_tender+1, driver, tender=True)
        if attachment_tender:
            pdf_paths_tenders.append(attachment_tender)
        print(f"Waiting for 2 seconds before the next iteration...")
        time.sleep(2)
        print(f"Continuing with the next iteration...")

# zip_filename_tenders = '/home/ywaghmare/BitMapper_WebScrapping/Tenders.zip'
# with zipfile.ZipFile(zip_filename_tenders, 'w') as zip_file_tenders:
#     for pdf_path_tender in pdf_paths_tenders:
#         zip_file_tenders.write(pdf_path_tender, os.path.basename(pdf_path_tender))
        
# # Check if the 'Tenders' zip file is empty
# if zip_file_tenders.namelist():
#     attachments_tenders.append(zip_filename_tenders)
#     print("List of attachments for Tenders:", attachments_tenders)
# else:
#     print("No files to attach for Tenders.")


# List to store downloaded PDF paths for RFI
pdf_paths_rfi = []
attachments_rfi = []

with open('/home/ywaghmare/BitMapper_WebScrapping/keywords.txt', 'r') as file_rfi:
    search_Keywords_rfi = [line.strip() for line in file_rfi]

for search_Keyword_rfi in search_Keywords_rfi:
    numbers_of_pdf_rfi, driver = get_number_record(search_Keyword_rfi, driver, tender=False)
    print("Number of pdfs for RFI are: ", numbers_of_pdf_rfi)

    for i_rfi in range(0, min(20, numbers_of_pdf_rfi)):
        attachment_rfi = download_pdf(search_Keyword_rfi, i_rfi+1, driver, tender=False)
        if attachment_rfi:
            pdf_paths_rfi.append(attachment_rfi)
        print(f"Waiting for 2 seconds before the next iteration...")
        time.sleep(2)
        print(f"Continuing with the next iteration...")

# Zip the 'RFI' directory
# zip_filename_rfi = '/home/ywaghmare/BitMapper_WebScrapping/RFI.zip'
# with zipfile.ZipFile(zip_filename_rfi, 'w') as zip_file_rfi:
#     for pdf_path_rfi in pdf_paths_rfi:
#         zip_file_rfi.write(pdf_path_rfi, os.path.basename(pdf_path_rfi))

# Check if the 'RFI' zip file is empty
# if zip_file_rfi.namelist():
#     attachments_rfi.append(zip_filename_rfi)
#     print("List of attachments for RFI:", attachments_rfi)
# else:
#     print("No files to attach for RFI.")

all_attachments = attachments_tenders + attachments_rfi


print("List of all attachments:", all_attachments)

# message = '''
# THIS IS AN AUTO-GENERATED EMAIL.
# Please find attached the zip folder containing the tender and RFI PDFs.

# Regards
# Yogeshwar Waghmare(Intern)
# '''

# if all_attachments:
#     send_email(sender_email, sender_password, recipient_emails, 'Downloaded PDF of Tenders/RFIs From website - https://indianarmy.nic.in', message, all_attachments)

print("Program run successfully....!")
driver.quit()