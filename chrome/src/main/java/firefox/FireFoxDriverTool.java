package firefox;

import lombok.Getter;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class FireFoxDriverTool {
    private final FirefoxDriver firefoxDriver;

    private final WebDriverWait webDriverWait;

    private boolean isLoadData = false;

    private boolean isRunning = true;

    public FireFoxDriverTool(FirefoxDriver firefoxDriver, WebDriverWait webDriverWait) {
        this.firefoxDriver = firefoxDriver;
        this.webDriverWait = webDriverWait;
    }

    public void isLoadData(boolean bool) {
        isLoadData = bool;
    }

    public void isRunning(boolean bool) {
        isRunning = bool;
    }
}
