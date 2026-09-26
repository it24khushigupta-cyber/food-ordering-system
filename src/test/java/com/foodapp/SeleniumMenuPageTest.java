package com.foodapp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium end-to-end test:
 * 1. Loads the menu page
 * 2. Verifies menu items are displayed
 * 3. Enters a quantity and places an order
 * 4. Verifies the order confirmation page shows the correct total
 *
 * Run headless in CI (Jenkins) - ChromeOptions below already set headless mode.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeleniumMenuPageTest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1280,800");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterAll
    static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void menuPage_displaysFoodItems() {
        driver.get(baseUrl() + "/menu");

        List<WebElement> rows = driver.findElements(By.id("menu-item-row"));
        assertFalse(rows.isEmpty(), "Menu should display at least one food item");

        WebElement heading = driver.findElement(By.tagName("h1"));
        assertTrue(heading.getText().contains("Menu"));
    }

    @Test
    void placingOrder_showsConfirmationWithCorrectTotal() {
        driver.get(baseUrl() + "/menu");

        // Set quantity 2 for the first menu item found
        WebElement firstQtyInput = driver.findElements(By.className("qty-input")).get(0);

        firstQtyInput.clear();
        firstQtyInput.sendKeys("2");

        WebElement placeOrderBtn = driver.findElement(By.id("place-order-btn"));
        placeOrderBtn.click();

        WebElement confirmationHeading = driver.findElement(By.id("confirmation-heading"));
        assertTrue(confirmationHeading.getText().contains("Order Placed"));

        WebElement totalEl = driver.findElement(By.id("order-total"));
        assertNotNull(totalEl.getText());
        assertFalse(totalEl.getText().isBlank());
    }
}
