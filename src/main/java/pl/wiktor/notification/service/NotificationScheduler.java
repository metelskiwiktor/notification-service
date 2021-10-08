package pl.wiktor.notification.service;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class NotificationScheduler {
    private static final Logger log = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationService notificationService;
    private final EmailSender emailSender;
    private final String itemURL;
    private WebDriver driver;

    public NotificationScheduler(NotificationService notificationService, EmailSender emailSender,
                                 @Value("${item.url}") String itemURL) {
        this.notificationService = notificationService;
        this.emailSender = emailSender;
        this.itemURL = itemURL;
    }

    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.SECONDS)
    public void checkItemStatus() throws MalformedURLException {
        log.info("New notification event triggered");
        driver = initializeDriver();
        try {
            boolean isAvailable = productCanBeOrdered();
            if (isAvailable) {
                log.info("Initialization of verify available status");
                isAvailable = verifyAvailableStatus();
            }
            boolean sentNotification = false;
            if (isAvailable && !notificationService.alreadySentNotification()) {
                emailSender.sendItemAvailableMessage(itemURL);
                sentNotification = true;
            } else if (isAvailable && notificationService.alreadySentNotification()) {
                log.info("Product is available, email already sent!");
            }
            notificationService.saveNotification(isAvailable, sentNotification, true);
            log.info("Product availability: {}", isAvailable);
        } catch (Exception e) {
            notificationService.saveNotification(false, false, false);
            log.info("Exception occurred: {}", e.getMessage());
        }
        driver.quit();
    }

    private boolean productCanBeOrdered() {
        boolean isAvailable = true;
        driver.get(itemURL);
        waitForPageLoad();
        WebElement orderButton = driver.findElement(By.className("buy-now-btn"));
        String classAttribute = orderButton.getAttribute("class");
        if (classAttribute.contains("gray")) {
            isAvailable = false;
        }
        return isAvailable;
    }

    //sometimes website sends invalid available status, so it's good to verify this
    private boolean verifyAvailableStatus() throws MalformedURLException {
        int attempts = 4;
        for (int i = 0; i < attempts; i++) {
            log.info("Verification status attempt: {}/{}", i + 1, attempts);
            driver.quit();
            driver = initializeDriver();
            boolean available = productCanBeOrdered();
            if (!available) {
                log.info("Auction item is still unavailable");
                return false;
            }
        }
        return true;
    }

    private void waitForPageLoad() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(driver -> String
                .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                .equals("complete"));
    }

    private WebDriver initializeDriver() throws MalformedURLException {
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver = new RemoteWebDriver(new URL("http://selenium-hub:4444/wd/hub"), chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }
}
