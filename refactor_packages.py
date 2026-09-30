import os
import re
import shutil

base_dir = 'src/main/java/com/billing'
models = ['Admin.java', 'Bill.java', 'Cash.java', 'Credit.java', 'Customer.java', 'Order.java', 'Payment.java', 'Phone.java']
services = ['Files.java', 'Store.java']
ui = ['Main_APP.java']

os.makedirs(os.path.join(base_dir, 'models'), exist_ok=True)
os.makedirs(os.path.join(base_dir, 'services'), exist_ok=True)
os.makedirs(os.path.join(base_dir, 'ui'), exist_ok=True)

all_files = models + services + ui
file_to_pkg = {}
for m in models: file_to_pkg[m] = 'com.billing.models'
for s in services: file_to_pkg[s] = 'com.billing.services'
for u in ui: file_to_pkg[u] = 'com.billing.ui'

def process_file(filename, target_pkg):
    src_path = os.path.join(base_dir, filename)
    if not os.path.exists(src_path): return
    
    with open(src_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Replace package
    content = re.sub(r'package com\.billing;', f'package {target_pkg};\n\nimport com.billing.models.*;\nimport com.billing.services.*;', content)
    
    target_path = os.path.join(base_dir, target_pkg.split('.')[-1], filename)
    with open(target_path, 'w', encoding='utf-8') as f:
        f.write(content)
        
    os.remove(src_path)

for m in models: process_file(m, 'com.billing.models')
for s in services: process_file(s, 'com.billing.services')
for u in ui: process_file(u, 'com.billing.ui')

print("Refactored to enterprise packages.")
