import re

with open('src/main/java/com/billing/Main_APP.java', 'r', encoding='utf-8') as f:
    content = f.read()

# 1. Replace new Scene(...) with createScene(...)
# Wait, first define createScene at the bottom
scene_method = '''
    private Scene createScene(javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load application.css");
        }
        return scene;
    }
}
'''

content = re.sub(r'\}\s*$', scene_method, content)

# 2. Replace new Scene calls that assign to a variable scene
content = re.sub(r'new Scene\(([^,]+),\s*([^,]+),\s*([^\)]+)\)', r'createScene(\1, \2, \3)', content)

# 3. Clean up createAnimatedButton
animated_btn = '''    private Button createAnimatedButton(String text, String color) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        if (color.equals(ACCENT_COLOR)) {
            button.getStyleClass().add("danger-button");
        } else {
            button.getStyleClass().add("primary-button");
        }
        button.setPrefWidth(200);
        button.setPrefHeight(40);
'''
content = re.sub(r'    private Button createAnimatedButton\(String text, String color\) \{.*?(?=        button\.setOnMouseEntered)', animated_btn, content, flags=re.DOTALL)

# 4. Clean up createNavButton
nav_btn = '''    private Button createNavButton(String text) {
        Button button = createAnimatedButton(text, SECONDARY_COLOR);
        button.getStyleClass().remove("primary-button");
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }'''
content = re.sub(r'    private Button createNavButton\(String text\) \{.*?    \}', nav_btn, content, flags=re.DOTALL)

# 5. Clean up side nav and background
content = content.replace('sideNav.setStyle("-fx-background-color: rgba(0,0,0,0.2);");', 'sideNav.getStyleClass().add("sidebar");')
content = content.replace('contentArea.setStyle("-fx-background-color: rgba(255,255,255,0.1);");', 'contentArea.getStyleClass().add("card-elevated");')
content = content.replace('dashboard.setStyle(MAIN_GRADIENT);', '')

with open('src/main/java/com/billing/Main_APP.java', 'w', encoding='utf-8') as f:
    f.write(content)

print("Java file patched.")
