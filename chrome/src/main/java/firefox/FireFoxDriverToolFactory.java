package firefox;

import chrome.ChromeDriverTool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

@Component
public class FireFoxDriverToolFactory {
    private final HashMap<String, FireFoxDriverTool> factoryHashMap = new HashMap<String, FireFoxDriverTool>();

    public FireFoxDriverTool getFireFoxDriverTool(String key) {
        return factoryHashMap.get(key);
    }


    public void makeFireFoxDriverTool(String key) {
        FirefoxDriver firefoxDriver = new FirefoxDriver(setOptions());
        WebDriverWait wait = new WebDriverWait(firefoxDriver, Duration.ofMillis(5000)); // 최대 5초 대기
        FireFoxDriverTool fireFoxDriverTool = new FireFoxDriverTool(firefoxDriver, wait);

        factoryHashMap.put(key, fireFoxDriverTool);
    }

    public void makeFireFoxDriverTool(String key, long waitTime) {
        FirefoxDriver firefoxDriver = new FirefoxDriver(setOptions());
        WebDriverWait wait = new WebDriverWait(firefoxDriver, Duration.ofMillis(waitTime)); // 최대 5초 대기
        FireFoxDriverTool fireFoxDriverTool = new FireFoxDriverTool(firefoxDriver, wait);
        factoryHashMap.put(key, fireFoxDriverTool);
    }


    private FirefoxOptions setOptions() {

        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--width=1920");
        options.addArguments("--height=1080");
        options.addPreference("dom.webdriver.enabled", false);
        options.addPreference("useAutomationExtension", false);

        return options;
    }
}
