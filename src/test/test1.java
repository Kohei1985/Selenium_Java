package test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class test1 {
    @Test
    public void testGoogleSearch() throws InterruptedException {
      // Optional, if not specified, WebDriver will search your path for chrome driver.
      System.setProperty("webdriver.chrome.driver", "./exe/chromedriver");

      WebDriver driver = new ChromeDriver();
      driver.get("https://www.city.sapporo.jp/shisetsu-yoyaku/");
      Thread.sleep(5000);  // Let the user actually see something!
    //リンクテキスト名が"画像"の要素を取得
      WebElement element = driver.findElement(By.linkText("札幌市公共施設予約情報システムへ"));
      //画像のリンクをクリック
      element.click();
      
//      WebElement searchBox = driver.findElement(By.name("q"));
//      searchBox.sendKeys("ChromeDriver");
//      searchBox.submit();
      Thread.sleep(5000);  // Let the user actually see something!
      driver.quit();
    }
}