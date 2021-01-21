package reserve;

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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import system.DateByPlaceMaster;
import system.ReserveDateController;
import system.Yoyakukun;

public class MainSystem {

    public static void main(String[] args) throws InterruptedException {
        //インスタンスを生成

        //引数の中に以下の項目を番号で設定
        //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放(内)->03,学校開放(外)->04
        //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,
        //地域(要素2):指定なし（初期値:"札幌市")
        //施設名称:
        Yoyakukun yoyaku01 = new Yoyakukun("スポーツ（屋内）", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",
                null);
        //割り当て↑("-施設区分-","-利用目的-","-札幌市（固定）-","-施設名称01-","-検索範囲開始日-","-検索範囲終了日-")
        List<String> places = new ArrayList<>();
        places.add("中島");   //=hall01
        places.add("スポーツ交流");   //=hall02
        String reserveMonth = "2"; //<-月を指定
        DateByPlaceMaster hall01 = new DateByPlaceMaster("", "", "", "", ""); //
        DateByPlaceMaster hall02 = new DateByPlaceMaster("27", "", "", "", "");

        try {
            System.out.println(yoyaku01.getPlaceName());

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
            Sheet sheet = excel.getSheet("sheet2");// <--ここでシート名を指定**(自分の担当はsheet2)**
            for (int i = 1; i <= 25; i++) { //<----エクセルの範囲指定はここ！！1~26までの数字
                Row rowC = sheet.getRow(i); //行を読み込み
                Cell cellId = rowC.getCell(2); //Cellを指定(ここは固定)
                String id = cellId.getStringCellValue(); //指定した場所の文字列を取得
                Row rowD = sheet.getRow(i); //行を読み込み
                Cell cellPass = rowD.getCell(3); //Cellを指定(ここは固定)
                String pass = cellPass.getStringCellValue(); //指定した場所の文字列を取得
                System.out.println("No." + i); //件数
                System.out.println("ID:" + id + "/Password:" + pass); //取得したデータを出力
                if (id == null || id.equals("")) {
                    System.out.println("ExcelFileにデータがありません");
                    break;
                }
                //ログイン画面を開く--------------------------------------------------------
                JavascriptExecutor jse = (JavascriptExecutor) driver;
                jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                WebElement element01 = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_btnMenuLogin"))); //施設予約ログインを探す
                element01.click(); //"施設予約ログイン"のリンクをクリック
                //指定された要素(検索テキストボックス)が表示状態になるまで待機する
                WebElement element02 = wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_txtRiyoushaID")));
                element02.sendKeys(id);//IDボックスにエクセルから取得したID入力する
                WebElement element03 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_txtPassword"));
                element03.sendKeys(pass);//Passwordにエクセルから取得した値を入力
                WebElement element04 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnLogin"));
                element04.click();//"ログインをクリック"

                for (String place : places) {
                    yoyaku01.setPlaceName(place);
                    if (yoyaku01.getPlaceName() == null || yoyaku01.getPlaceName().equals("")) {
                        int noPlace = places.indexOf("") + 1;
                        System.out.println("会場リストの" + noPlace + "番目に会場の指定がありません");
                    }

                    Thread.sleep(2000);
                    jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                    WebElement element05 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnMenuShinseiSrch"));
                    element05.click();//施設予約検索をクリック
                    //施設の予約検索オペレーション--------------------------------------------------------------------
                    Thread.sleep(3000);//3秒待機
                    driver.navigate().refresh(); //ページをリフレッシュ
                    jse.executeScript("window.scrollBy(0,300)", "");//500px下にスクロール
                    Select dropdown01 = new Select(driver
                            .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$drpPurposeBunrui")));
                    dropdown01.selectByVisibleText(yoyaku01.getUsePlace());//施設区分の選択
                    Select dropdown02 = new Select(
                            driver.findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$drpPurpose")));
                    dropdown02.selectByVisibleText(yoyaku01.getUsePurpose());//利用目的の選択
                    WebElement element06 = driver
                            .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtShisetsuMeisho"));
                    element06.clear();//すでに文字が入っている場合のため、クリア処理
                    element06.sendKeys(yoyaku01.getPlaceName());//施設名を入力する
                    WebElement element07 = driver
                            .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtFromDate"));
                    element07.sendKeys(yoyaku01.getStartDate());//検索範囲の開始日を入力する
                    WebElement element08 = driver
                            .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtToDate"));
                    element08.sendKeys(yoyaku01.getEndDate());//検索範囲の終了日を入力する
                    Thread.sleep(3000);//3秒待機
                    WebElement element09 = driver
                            .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$btnSearch"));
                    element09.click();//検索をクリック
                    //List　"reserveDays"に施設ごとの予約してい日を格納し、拡張for文でreserveDayを繰り返す処理
                    List<String> reserveDays = new ArrayList<>();
                    if (yoyaku01.getPlaceName() == "中島") {
                        Collections.addAll(reserveDays, hall01.getReserveDate01(), hall01.getReserveDate02(),
                                hall01.getReserveDate03(), hall01.getReserveDate04(),
                                hall01.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                    }
                    if (yoyaku01.getPlaceName() == "スポーツ交流") {
                        Collections.addAll(reserveDays, hall02.getReserveDate01(), hall02.getReserveDate02(),
                                hall02.getReserveDate03(), hall02.getReserveDate04(),
                                hall02.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                    }

                    //ここから繰り返し処理
                    for (String reserveDay : reserveDays) {
                        if (reserveDay.equals("") || reserveDay == null) {
                            System.out.println("日にちの指定がありません。次の施設の予約に進みます。");
                            break;
                        }
                        yoyaku01.setReserveDate(reserveMonth + "月" + reserveDay + "日");//YoyakukunインスタンスにReseveDateをセット
                        //カレンダーが表示されて予約する日にちを指定--------------------------------------------------------------------------
                        Thread.sleep(2000);
                        WebElement element10 = driver.findElement(
                                By.xpath("//a[contains(@title,'" + yoyaku01.getReserveDate() + "')]"));
                        element10.click();//カレンダー上の日付をクリック

                        //つどーむ（スポーツ交流）の時はA面(=li.get(0))かB面(=li.get(1))を選択-------------------------
                                String placeName = yoyaku01.getPlaceName();
                                if (placeName.equals("スポーツ交流")) {
                                    Thread.sleep(2000);
                                    List<WebElement> li = driver.findElements(By.linkText("選択"));
                                    ;
                                    Thread.sleep(2000);
                                    li.get(1).click();
                                }
                        //曜日によって繰り返し処理の回数を変える(土日=2回,平日=4回)
                        ReserveDateController rdc = new ReserveDateController();
                        String youbi = rdc.getYoubi("2021",reserveMonth,reserveDay);
                        int times = 0;
                        if(youbi.equals("日曜") || youbi.equals("土曜")){
                            times = 2; //土日の繰り返し回数
                        }else{
                            times = 4; //平日の繰り返し回数
                        }
                            for (int j = 0; j < times ; j++) {//予約を"j"回繰り返す <---ここの数字を変更で繰り返し回数指定
                        //時間帯を指定------------------------------------------------------------------------

                            jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                            WebElement element11 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_JikantaiSel6"));
                            element11.click();//時間帯を指定
                            WebElement element12 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnShinseiCnf"));
                            element12.click();//申請
                            //申し込み申請確認----------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,600)", "");//500px下にスクロール
                            WebElement element13 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnShinsei"));
                            element13.click();//予約完了
                            System.out.println(yoyaku01.getPlaceName() + "/" + yoyaku01.getReserveDate() + "をID:" + id + "で予約しました。");
                            //戻るボタン2回
                            driver.navigate().back();
                            driver.navigate().back();
                        }
                    }
                }
                Thread.sleep(3000);
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
