package chrome;


import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

@Component
public class ChromeDriverToolFactory {

    private final HashMap<String, ChromeDriverTool> factoryHashMap = new HashMap<String, ChromeDriverTool>();

    public ChromeDriverTool getChromeDriverTool(String key) {
        return factoryHashMap.get(key);
    }

    public void makeChromeDriverTool(String key) {
        ChromeDriver chromeDriver = new ChromeDriver(setOptions());
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofMillis(5000)); // 최대 5초 대기
        ChromeDriverTool chromeDriverTool = new ChromeDriverTool(chromeDriver, wait);

        factoryHashMap.put(key, chromeDriverTool);
    }

    public void makeChromeDriverTool(String key, long waitTime) {
        ChromeDriver chromeDriver = new ChromeDriver(setOptions());
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofMillis(waitTime)); // 최대 5초 대기
        ChromeDriverTool chromeDriverTool = new ChromeDriverTool(chromeDriver, wait);

        factoryHashMap.put(key, chromeDriverTool);
    }

    public void makePrivateChromeDriverTool(String key, long waitTime, String userDataDirRelativePath) {

        ChromeDriver chromeDriver = new ChromeDriver(setPrivateOptions(userDataDirRelativePath));
        WebDriverWait wait = new WebDriverWait(chromeDriver, Duration.ofMillis(waitTime)); // 최대 5초 대기
        ChromeDriverTool chromeDriverTool = new ChromeDriverTool(chromeDriver, wait);

        factoryHashMap.put(key, chromeDriverTool);

    }

    private ChromeOptions setPrivateOptions(String userDataDirRelativePath) {

        String currentDir = System.getProperty("user.dir");

        String userDataDir = currentDir + userDataDirRelativePath;
        ChromeOptions privateOptions = new ChromeOptions();

        System.out.println("userDataDir" + userDataDir);

        privateOptions.addArguments("--no-sandbox");
        privateOptions.addArguments("window-size=1920x1080");
        privateOptions.addArguments("start-maximized");
        privateOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        privateOptions.setExperimentalOption("useAutomationExtension", false);
        privateOptions.addArguments("--disable-automation");
        privateOptions.addArguments("--disable-blink-features=AutomationControlled");
        privateOptions.setExperimentalOption("detach", true);

        privateOptions.addArguments("--user-data-dir=/Users/hanhosung/Library/Application Support/Google/Chrome");
        privateOptions.addArguments("--profile-directory=Profile 1");
//        privateOptions.addArguments("--profile-directory=Profile 4");
//        privateOptions.addArguments("--user-data-dir=" + userDataDir);

//        privateOptions.addArguments("--user-data-dir='/Users/hanhosung/Library/Application Support/Google/Chrome/Default'");


        return privateOptions;
    }

    private ChromeOptions setOptions() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("window-size=1920x1080");
        options.addArguments("start-maximized");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--disable-automation");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("detach", true);
        return options;
    }

}
