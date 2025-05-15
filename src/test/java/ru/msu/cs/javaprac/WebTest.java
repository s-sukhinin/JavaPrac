package ru.msu.cs.javaprac;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

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

        WebElement login = driver.findElement(By.id("login_header"));
        login.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(loginTitle, driver.getTitle());

        WebElement rootButton = driver.findElement(By.id("mainPage_header"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        WebElement reg = driver.findElement(By.id("reg_header"));
        reg.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(regTitle, driver.getTitle());
    }

    @Test
    void testAuth()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("login_header")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("login")).sendKeys("user1");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("submit_btn")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        assertFalse(driver.findElements(By.id("profile_header")).isEmpty());
    }

    @Test
    void testReg()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("reg_header")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("login")).sendKeys("user1");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("name")).sendKeys("Ivan");
        driver.findElement(By.className("btn-primary")).click();

        assertEquals(regTitle, driver.getTitle());

        driver.findElement(By.id("login")).sendKeys("user10");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("name")).sendKeys("Ivan");
        driver.findElement(By.className("btn-primary")).click();

        assertEquals("Profile", driver.getTitle());

    }

    @Test
    void testCreation()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.findElement(By.id("login_header")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("login")).sendKeys("admin1");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("submit_btn")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("create_category")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("New category", driver.getTitle());

        driver.findElement(By.id("title")).sendKeys("new category");
        driver.findElement(By.id("description")).sendKeys("description");
        driver.findElement(By.className("btn-primary")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("new category", driver.getTitle());

        driver.findElement(By.id("create_thread")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("New thread", driver.getTitle());

        driver.findElement(By.id("title")).sendKeys("new thread");
        driver.findElement(By.className("btn-primary")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        assertEquals("new thread", driver.getTitle());

        driver.findElement(By.className("form-control")).sendKeys("hi");
        driver.findElement(By.className("btn-success")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        List<WebElement> listItems = driver.findElements(By.cssSelector("#list_of_posts li"));
        assertEquals(1, listItems.size());

    }

    @Test
    void testListOfUsers()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1920,1080));
        driver.findElement(By.id("users")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("name_filter")).sendKeys("ra");
        driver.findElement(By.id("thread-2")).click();
        driver.findElement(By.id("filter_btn")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        List<WebElement> rows = driver.findElements(By.cssSelector("table tbody tr"));

        assertEquals(2, rows.size()); // заме
    }

    @Test
    void testBan()
    {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");

        driver.findElement(By.id("login_header")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("login")).sendKeys("user3");
        driver.findElement(By.id("password")).sendKeys("pass123");
        driver.findElement(By.id("submit_btn")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("category-2")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        driver.findElement(By.id("thread-3")).click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

        assertTrue(driver.findElements(By.className("form-control")).isEmpty());
    }



}
