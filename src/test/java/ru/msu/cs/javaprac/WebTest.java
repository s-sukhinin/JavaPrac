package ru.msu.cs.javaprac;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebTest
{
    private final String rootTitle = "Веб-Форум";
    private final String loginTitle = "Login";
    private final String regTitle = "Registration";

    @Test
    void MainPage()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        assertEquals(rootTitle, driver.getTitle());
        driver.quit();
    }

    @Test
    void Header()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");

        WebElement login = driver.findElement(By.id("login"));
        login.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(loginTitle, driver.getTitle());

        WebElement rootButton = driver.findElement(By.id("mainPage"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        WebElement reg = driver.findElement(By.id("reg"));
        reg.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(regTitle, driver.getTitle());
    }
}
