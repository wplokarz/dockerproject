
import org.junit.Test;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class LoginTest {

    @Test
    public void loginTest() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--enable-logging");
        options.addArguments("--v=1");

        // to use docker compose with tests to run inside
        options.setBinary("/opt/chrome/chrome-linux64/chrome");
        WebDriver driver = new ChromeDriver(options);

        // to use docker compose to create container with endpoint to pass tests

//        String gridUrl = "http://127.0.0.1:4444";
//        WebDriver driver = new RemoteWebDriver(new URL(gridUrl), options);
        driver.get("https://practicetestautomation.com/practice-test-login/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By usernameField = By.xpath("//input[@id='username']");
        By passwordField = By.xpath("//input[@id='password']");
        By submitButton = By.xpath("//button[@id='submit']");
        By successTitle = By.xpath("//h1[@class='post-title']");
        wait.until(ExpectedConditions.presenceOfElementLocated(usernameField));
        driver.findElement(usernameField).sendKeys("student");
        driver.findElement(passwordField).sendKeys("Password123");
        driver.findElement(submitButton).click();
        String actualText =  driver.findElement(successTitle).getText();
        String expectedText = "Logged In Successfully";
        driver.quit();
        Assert.assertEquals(expectedText, actualText);
    }
}
