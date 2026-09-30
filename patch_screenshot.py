import re

with open('src/main/java/com/billing/ui/Main_APP.java', 'r', encoding='utf-8') as f:
    content = f.read()

# Add imports
imports = '''
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;
'''

content = re.sub(r'import javafx\.', imports + '\nimport javafx.', content, count=1)

hook = '''
        primaryStage.show();
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(ev -> {
            try {
                WritableImage snapshot = scene.snapshot(null);
                File file = new File("docs/screenshot.png");
                file.getParentFile().mkdirs();
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                System.out.println("AUTOMATIC SCREENSHOT TAKEN AND SAVED TO docs/screenshot.png");
                Platform.exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pt.play();
'''

content = content.replace('primaryStage.show();', hook)

with open('src/main/java/com/billing/ui/Main_APP.java', 'w', encoding='utf-8') as f:
    f.write(content)
print("Screenshot hook added correctly.")
