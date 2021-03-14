package checkReserve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import system.LineNotify;

public class CheckReseveMain {

    public static void main(String[] args) throws InterruptedException {
        //インスタンスを生成
        String トークン = "DedPHNC064l3dPTeH9RhPvougOz9TwWmscqroCZtBE8"; //自分のみのライン
        LineNotify lineNotify = new LineNotify(トークン);
        String msg = ""; //送信内容を格納する変数
        List<String> sheetNames = new ArrayList<String>();
        Collections.addAll(sheetNames,  "sheet1", "sheet2", "sheet3","sheet1","sheet4","sheet5","sheet6","sheet7","sheet8");//"sheet1", "sheet2", "sheet3",
        String Name = ""; //シートに合わせて名前を格納する変数

        try {

            //ChoromeDriverをセット＆定義
            System.setProperty("webdriver.chrome.driver", "./exe/chromedriver");
            WebDriver driver = new ChromeDriver();
            //指定したdriverに対して最大で10秒間待つように設定する
            WebDriverWait wait = new WebDriverWait(driver, 10);
            //札幌市施設予約のページを開く
            driver.get("https://www.city.sapporo.jp/shisetsu-yoyaku/");
            WebElement element = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.linkText("札幌市公共施設予約情報システムへ"))); //リンクテキスト名が"札幌市公共施設予約情報システムへ"の要素を取得
            element.click();//"札幌市公共施設予約情報システムへ"のリンクをクリック

            //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
            Workbook excel;
            excel = WorkbookFactory
                    .create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
            for (String sheetName : sheetNames) {
                try{
                //String sheetName = "sheet1"; // <--ここでシート名を指定**(自分の担当はsheet2)**
                switch(sheetName){
                case "sheet1":
                    Name = "雉子谷";
                    break;
                case "sheet2":
                    Name = "浩平";
                    break;
                case "sheet3":
                    Name = "タオ&庄司";
                    break;
                case "sheet4":
                    Name = "前田";
                    break;
                case "sheet5":
                    Name = "垣内";
                    break;
                case "sheet6":
                    Name = "池田";
                    break;
                case "sheet7":
                    Name = "健太";
                    break;
                case "sheet8":
                    Name = "阿部";
                    break;
                }
                msg = msg + Name + "コーチの抽選結果\r\n";
                //          ...................................................................
                //            sheet1 = 雉子谷さん    sheet2 = 浩平     sheet3 = タオ・庄司コーチ
                //            sheet4 = 前田コーチ
                //          ...................................................................
                Sheet sheet = excel.getSheet(sheetName);
                for (int i = 1; i <= 26; i++) { //<----エクセルの範囲指定はここ！！1~26までの数字
                        Row rowC = sheet.getRow(i); //行を読み込み
                        Cell cellId = rowC.getCell(2); //Cellを指定(ここは固定)
                        String id = cellId.getStringCellValue(); //指定した場所の文字列を取得
                        Row rowD = sheet.getRow(i); //行を読み込み
                        Cell cellPass = rowD.getCell(3); //Cellを指定(ここは固定)
                        String pass = cellPass.getStringCellValue(); //指定した場所の文字列を取得
                        System.out.println("No." + i); //件数
                        System.out.println("ID:" + id + "/Password:" + pass); //取得したデータを出力
                        if (cellId == null || cellId.equals("") || id == null || id.equals("")) {
                            System.out.println("ExcelFileにデータがありません");
                            break;
                        }
                    //ログイン画面を開く--------------------------------------------------------
                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                    WebElement element01 = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_btnMenuLogin"))); //施設予約ログインを探す
                    element01.click(); //"施設予約ログイン"のリンクをクリック
                    //指定された要素(検索テキストボックス)が表示状態になるまで待機する
                    WebElement element02 = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_txtRiyoushaID")));
                    element02.sendKeys(id);//IDボックスにエクセルから取得したID入力する
                    WebElement element03 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_txtPassword"));
                    element03.sendKeys(pass);//Passwordを入力
                    WebElement element04 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnLogin"));
                    element04.click();//"ログインをクリック"
                    Thread.sleep(0500);
                    jse.executeScript("window.scrollBy(0,800)", "");//500px下にスクロール

                    //当選が出ているかどうかを確認して当選があれば、申請処理を行い、コンソールに当選番号と施設名時間帯などを表示する。
                    WebElement element05 = driver.findElement(By.className("log-box"));
                    boolean check = element05.getText().contains("当選");
                    if (check == true) {
                        System.out.println("ID:" + id + "で当選しました!!!!");
                        msg = msg + "ID:" + id + "で当選しました。\r\n";
                    }
                    //else{
                    //  System.out.println("ID:"+ value + "は残念ながら当選がありませんでした");
                    //}

                    //ログアウト---------------------------------------------------------
                    jse.executeScript("window.scrollBy(0,-600)", "");//600px上にスクロール
                    WebElement logout = driver.findElement(By.name("ctl00$btnLogout"));
                    logout.click();//ログアウトして次のID番号へ繰り返し

                }
                msg = msg + "\r\n";
                lineNotify.notify(msg);
                msg = "";
                } catch (org.openqa.selenium.NoSuchElementException e) {
                    lineNotify.notify(sheetName + "で例外が発生しました。");
                    e.printStackTrace();
                    driver.quit();
                } catch (org.openqa.selenium.TimeoutException e) {
                lineNotify.notify(sheetName + "で時間切れ例外が発生しました。");
                e.printStackTrace();
                driver.quit();
            }
            }
            driver.quit();

        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
