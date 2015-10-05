/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author datht2@fpt.edu.vn
 */
public class AutoIAP {

    public static WebDriver driver;
    private static WebElement el;
    public static String[] slots;
    public static List<Student> students;
    public static int count;
    public static boolean autoCheckSlot1 = false;

    public static Config loadConfigFile() throws IOException, ClassNotFoundException {
        File file = new File("config.dat");
        if (file.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Config con = (Config) ois.readObject();
            return con;
        }
        return null;
    }

    public static void writeConfigFile(Config con) throws FileNotFoundException, IOException {
        File file = new File("config.dat");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(con);
    }

    public static void loginGoogle(String user, String pass) {
        System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");

        driver = new ChromeDriver();

        //  driver = new HtmlUnitDriver(true);
        driver.navigate().to("http://iap.fpt.edu.vn/hlogin.php?provider=Google");

        new WebDriverWait(driver, 360).until(ExpectedConditions.titleIs("Sign in - Google Accounts"));
        el = driver.findElement(By.id("Email"));
        el.sendKeys(user);
        el = driver.findElement(By.id("next"));
        el.click();

        new WebDriverWait(driver, 360).until(ExpectedConditions.presenceOfElementLocated(By.id("Passwd")));

        el = driver.findElement(By.id("Passwd"));
        el.sendKeys(pass);
        el = driver.findElement(By.id("signIn"));
        el.click();

    }

    public static void getDataFromServer(String className) {
        int c = 0;
        slots = new String[10];
        String main = "";
        students = new LinkedList<>();
        new WebDriverWait(driver, 360).until(ExpectedConditions.titleIs("IAP"));
        driver.get("http://iap.fpt.edu.vn/activity");

        new WebDriverWait(driver, 360).until(ExpectedConditions.urlToBe("http://iap.fpt.edu.vn/activity/"));

        List<WebElement> els = driver.findElements(By.tagName("a"));
        System.out.println("Size a:" + els.size());
        for (WebElement e : els) {
            //  System.out.println(e.getText());
            if (e.getText().contains(className)) {

                main = e.getAttribute("href").replaceAll("http://iap.fpt.edu.vn/activity/index.php", "http://iap.fpt.edu.vn/attendance/add.php");

                slots[c] = main;
                c++;
                count = c;
                // System.out.println(main);

            }
        }

        driver.get(slots[0]);

        try {
            new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//font[@type='submit']")));

        } catch (TimeoutException ex) {
            driver.get(slots[0].replaceAll("add.php", "edit.php"));

        }
        //Auto Check Slot 1
        List<WebElement> elsInput = driver.findElements(By.xpath(".//input[@type='radio' and @value='1' ]"));

        if (autoCheckSlot1) {
            autoCheckAll(elsInput);
        }

        //Wating for submit
        new WebDriverWait(driver, 5000).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//font[@color='green']")));

        //GET All checked
        List<WebElement> elsInputChecked = driver.findElements(By.xpath(".//input[@type='radio' and @value='1' and @checked]"));

        System.out.println("Size input checked:" + elsInputChecked.size());

        elsInputChecked.stream().map((e) -> {
            Student student = new Student();
            student.name = e.getAttribute("name");
            return student;
        }).forEach((student) -> {
            students.add(student);
        });

    }

    public static void autoCheckAll(List<WebElement> wels) {
        WebElement first = wels.get(0);

        wels.stream().forEach((e) -> {
            e.click();
        });
        // sroll to top
        first.click();

    }

    public static void autoCheck() {

        for (int c = 1; c < count; c++) {

            driver.get(slots[c]);
            try {
                new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@type='submit']")));

            } catch (TimeoutException ex) {
                driver.get(slots[c].replaceAll("add.php", "edit.php"));

            }
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//input[@type='submit']")));
            for (Student st : students) {

                List<WebElement> els = driver.findElements(By.name(st.name));
                for (WebElement e : els) {
                    if (e.getAttribute("value").equals("1")) {
                        e.click();

                    }

                }
            }
            el = driver.findElement(By.xpath(".//input[@type='submit']"));
            el.click();
            try {
                new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//input[@type='submit']")));
            } catch (TimeoutException ex) {
                break;

            }

        }

    }

}
