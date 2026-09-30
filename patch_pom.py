import re

with open('pom.xml', 'r', encoding='utf-8') as f:
    pom = f.read()

swing_dep = '''
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-swing</artifactId>
            <version>17.0.6</version>
        </dependency>
'''

pom = re.sub(r'</dependencies>', swing_dep + '    </dependencies>', pom)

with open('pom.xml', 'w', encoding='utf-8') as f:
    f.write(pom)
print("POM updated with javafx-swing.")
