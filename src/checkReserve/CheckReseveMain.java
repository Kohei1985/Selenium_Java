package checkReserve;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckReseveMain {

    public static void main(String[] args) throws InterruptedException {
        //インスタンスを生成

                //引数の中に以下の項目を番号で設定
                //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放(内)->03,学校開放(外)->04
                //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,
                //地域(要素2):指定なし（初期値:"札幌市")
                //施設名称:

        try {

            //ChoromeDriverをセット＆定義
            System.setProperty("webdriver.chrome.driver", "./exe/chromedriver");
            WebDriver driver = new ChromeDriver();
            //指定したdriverに対して最大で10秒間待つように設定する
            WebDriverWait wait = new WebDriverWait(driver, 10);
            //札幌市施設予約のページを開く
            driver.get("https://www.city.sapporo.jp/shisetsu-yoyaku/");
            WebElement element = wait.until(ExpectedConditions.
                    visibilityOfElementLocated(By.linkText("札幌市公共施設予約情報システムへ")));   //リンクテキスト名が"札幌市公共施設予約情報システムへ"の要素を取得
            element.click();//"札幌市公共施設予約情報システムへ"のリンクをクリック

            //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
            Workbook excel;
            excel = WorkbookFactory.create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
            Sheet sheet = excel.getSheet("Sheet4");// <--ここでシート名を指定**(自分の担当はsheet2)**
            for (int i = 1; i <= 25; i++) { //<----エクセルの範囲指定はここ！！1~26までの数字
                Row row = sheet.getRow(i); //行を読み込み
                Cell cell = row.getCell(2); //Cellを指定(ここは固定)
                String value = cell.getStringCellValue(); //指定した場所の文字列を取得
                System.out.println(i); //件数
                System.out.println(value); //取得したデータを出力

             //ログイン画面を開く--------------------------------------------------------
                JavascriptExecutor jse = (JavascriptExecutor)driver;
                jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                WebElement element01 = wait.until(ExpectedConditions.
                        visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_btnMenuLogin"))); //施設予約ログインを探す
                element01.click(); //"施設予約ログイン"のリンクをクリック
                //指定された要素(検索テキストボックス)が表示状態になるまで待機する
                WebElement element02 = wait.until(ExpectedConditions.
                                                 visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_txtRiyoushaID")));
                element02.sendKeys(value);//IDボックスにエクセルから取得したID入力する
                WebElement element03 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_txtPassword"));
                element03.sendKeys("000000");//Password"0000000"を入力
                WebElement element04 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnLogin"));
                element04.click();//"ログインをクリック"
                Thread.sleep(2000);
                jse.executeScript("window.scrollBy(0,800)", "");//500px下にスクロール

             //当選が出ているかどうかを確認して当選があれば、申請処理を行い、コンソールに当選番号と施設名時間帯などを表示する。
                WebElement element05 = driver.findElement(By.className("log-box"));
                boolean check = element05.getText().contains("当選");
                if(check == true){
                    System.out.println("!!!!!ID:"+ value + "で当選しました!!!!");
                }
                //else{
                  //  System.out.println("ID:"+ value + "は残念ながら当選がありませんでした");
                //}



             //ログアウト---------------------------------------------------------
                jse.executeScript("window.scrollBy(0,-600)", "");//600px上にスクロール
                WebElement logout = driver.findElement(By.name("ctl00$btnLogout"));
                logout.click();//ログアウトして次のID番号へ繰り返し

            }

            driver.quit();

        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
