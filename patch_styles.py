import re

with open('src/main/java/com/billing/Main_APP.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Replace setStyle with class additions
replacements = [
    (r'container\.setStyle\(MAIN_GRADIENT\);', r'container.getStyleClass().add("root");'),
    (r'topBar\.setStyle\("-fx-background-color: rgba\(0,0,0,0\.3\);"\);', r'topBar.getStyleClass().add("top-bar");'),
    (r'welcomeLabel\.setStyle\("-fx-text-fill: white; -fx-font-size: 16px;"\);', r'welcomeLabel.getStyleClass().add("subtitle-label");'),
    (r'phoneTable\.setStyle\("-fx-background-color: rgba\(255,255,255,0\.9\);"\);', r'phoneTable.getStyleClass().add("table-view");'),
    (r'billsTable\.setStyle\("-fx-background-color: rgba\(255,255,255,0\.9\);"\);', r'billsTable.getStyleClass().add("table-view");'),
    (r'ordersTable\.setStyle\("-fx-background-color: rgba\(255,255,255,0\.9\);"\);', r'ordersTable.getStyleClass().add("table-view");'),
    (r'reportsArea\.setStyle\("-fx-background-color: rgba\(255,255,255,0\.9\);"\);', r'reportsArea.getStyleClass().add("text-area");'),
    (r'paymentMethod\.setStyle\("-fx-background-color: white;"\);', r'paymentMethod.getStyleClass().add("combo-box");'),
    (r'monthComboBox\.setStyle\("-fx-background-color: white;"\);', r'monthComboBox.getStyleClass().add("combo-box");'),
    (r'yearComboBox\.setStyle\("-fx-background-color: white;"\);', r'yearComboBox.getStyleClass().add("combo-box");'),
    (r'field\.setStyle\("-fx-background-radius: 5;"\);', r'field.getStyleClass().add("text-field");')
]

for old, new in replacements:
    content = re.sub(old, new, content)

with open('src/main/java/com/billing/Main_APP.java', 'w', encoding='utf-8') as f:
    f.write(content)

print("Styles patched.")
