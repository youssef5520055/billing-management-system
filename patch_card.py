import re

with open('src/main/java/com/billing/Main_APP.java', 'r', encoding='utf-8') as f:
    content = f.read()

card_method = '''    private VBox createCard() {
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setMaxHeight(VBox.USE_PREF_SIZE);
        card.setAlignment(Pos.CENTER);
        return card;
    }'''

content = re.sub(r'    private VBox createCard\(\) \{.*?    \}', card_method, content, flags=re.DOTALL)

with open('src/main/java/com/billing/Main_APP.java', 'w', encoding='utf-8') as f:
    f.write(content)

print("Card patched.")
