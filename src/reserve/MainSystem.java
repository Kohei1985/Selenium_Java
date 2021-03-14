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
import system.LineNotify;
import system.ReserveDateController;
import system.Yoyakukun;

public class MainSystem {

    public static void main(String[] args) throws InterruptedException {
        //インスタンスを生成
        //Line通知
        String トークン = "DedPHNC064l3dPTeH9RhPvougOz9TwWmscqroCZtBE8";
        LineNotify lineNotify = new LineNotify(トークン);

        //引数の中に以下の項目を番号で設定
        //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放(内)->03,学校開放(外)->04
        //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,
        //地域(要素2):指定なし（初期値:"札幌市")
        //施設名称:
        Yoyakukun yoyaku01 = new Yoyakukun("スポーツ（屋内）", "サロンフットボール・フットサル", "札幌市", null, "2021/04/01", "2021/04/30",
                null);
        //割り当て↑("-施設区分-","-利用目的-","-札幌市（固定）-","-施設名称01-","-検索範囲開始日-","-検索範囲終了日-")
        List<String> places = new ArrayList<>();
        places.add("中島"); //=hall01
        places.add("スポーツ交流"); //=hall02
        String reserveMonth = "4"; //<-月を指定
        DateByPlaceMaster hall01 = new DateByPlaceMaster("13", "18", "", "", ""); //
        DateByPlaceMaster hall02 = new DateByPlaceMaster("22", "", "", "", "");

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
            String sheetName = "sheet8";// <--ここでシート名を指定**(自分の担当はsheet2)**
            //          ...................................................................
            //          sheet1 = 雉子谷さん    sheet2 = 浩平     sheet3 = タオ・庄司コーチ
            //          sheet4 = 前田コーチ　
            //          ...................................................................
            Sheet sheet = excel.getSheet(sheetName);
            for (int i = 24; i <= 24; i++) { //<----エクセルの範囲指定はここ！！1~26までの数字
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
                        Thread.sleep(0500);
                        jse.executeScript("window.scrollBy(0,400)", "");//500px下にスクロール
                        //更新の場合
                        WebElement updtElement = driver
                                .findElement(By.id("ctl00_ContentPlaceHolder1_btnMenuUserEntry"));
                        String url = updtElement.getAttribute("onMouseout");

                        Thread.sleep(0500);
                        //更新のボタンがピンクになっている時更新を行う**************************************************
                        if (url.equals("this.style.background='url(images/menu02_red.jpg)'")) {
                            System.out.println("更新が必要です。更新します。");
                            WebElement updElm01 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$btnMenuUserEntry"));
                            updElm01.click(); //登録者情報 変更・削除クリック
                            Thread.sleep(0500);
                            jse.executeScript("window.scrollBy(0,1800)", "");//1800px下にスクロール
                            WebElement updElm02 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnEdit"));
                            updElm02.click(); //有効期限更新ボタンクリック
                            jse.executeScript("window.scrollBy(0,0600)", "");//600px下にスクロール
                            WebElement updElm03 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkUserKind"));
                            updElm03.click();
                            WebElement updElm04 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkDaihyousyaUserID"));
                            updElm04.click();
                            WebElement updElm05 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkFullName"));
                            updElm05.click();
                            WebElement updElm06 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkKanaName"));
                            updElm06.click();
                            WebElement updElm07 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkPostCode"));
                            updElm07.click();
                            WebElement updElm08 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkAddress"));
                            updElm08.click();
                            WebElement updElm09 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkBirthDay"));
                            updElm09.click();
                            WebElement updElm10 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkJitakuTel"));
                            updElm10.click();
                            WebElement updElm11 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkJitakuFax"));
                            updElm11.click();
                            WebElement updElm12 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkMoboleTel"));
                            updElm12.click();
                            WebElement updElm13 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkRenrakusakiTel"));
                            updElm13.click();
                            jse.executeScript("window.scrollBy(0,0400)", "");//600px下にスクロール
                            WebElement updElm14 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkEmail"));
                            updElm14.click();
                            WebElement updElm15 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkHogoshaName"));
                            updElm15.click();
                            WebElement updElm16 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkHogoshaTsudukigara"));
                            updElm16.click();
                            WebElement updElm17 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnGrpInp"));
                            updElm17.click();
                            //団体情報入力
                            jse.executeScript("window.scrollBy(0,0400)", "");//600px下にスクロール
                            WebElement updElm18 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkCorpFlag"));
                            updElm18.click();
                            WebElement updElm19 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkGroupName"));
                            updElm19.click();
                            WebElement updElm20 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkGroupKanaName"));
                            updElm20.click();
                            WebElement updElm21 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkGroupKind"));
                            updElm21.click();
                            WebElement updElm22 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkGroupNinzu"));
                            updElm22.click();
                            WebElement updElm23 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnFukuInp"));
                            updElm23.click();
                            //副代表者入力
                            WebElement updElm24 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuUserID"));
                            updElm24.click();
                            WebElement updElm25 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuName"));
                            updElm25.click();
                            WebElement updElm26 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuKanaName"));
                            updElm26.click();
                            WebElement updElm27 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuPostCode"));
                            updElm27.click();
                            WebElement updElm28 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuAddress"));
                            updElm28.click();
                            WebElement updElm29 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuBirthDay"));
                            updElm29.click();
                            jse.executeScript("window.scrollBy(0,0600)", "");//600px下にスクロール
                            WebElement updElm30 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuJitakuTel"));
                            updElm30.click();
                            WebElement updElm31 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuFax"));
                            updElm31.click();
                            WebElement updElm32 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuMobile"));
                            updElm32.click();
                            WebElement updElm33 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuTel"));
                            updElm33.click();
                            WebElement updElm34 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuEmail"));
                            updElm34.click();
                            WebElement updElm35 = driver
                                    .findElement(By.name("ctl00$ContentPlaceHolder1$chkFukuSoufusakiKbn"));
                            updElm35.click();
                            WebElement updElm36 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnConfirm"));
                            updElm36.click();
                            //確認画面
                            jse.executeScript("window.scrollBy(0,1800)", "");//600px下にスクロール
                            WebElement updElm37 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnInsert"));
                            updElm37.click();
                            //更新完了画面
                            WebElement updElm38 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnMenu"));//メニューに戻る
                            updElm38.click();
                        }
                        //                    System.out.println(url);

                        WebElement element05 = driver
                                .findElement(By.name("ctl00$ContentPlaceHolder1$btnMenuShinseiSrch"));
                        element05.click();//施設予約検索をクリック
                        //施設の予約検索オペレーション--------------------------------------------------------------------
                        Thread.sleep(0500);//3秒待機
                        driver.navigate().refresh(); //ページをリフレッシュ
                        jse.executeScript("window.scrollBy(0,300)", "");//500px下にスクロール
                        Select dropdown01 = new Select(driver
                                .findElement(
                                        By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$drpPurposeBunrui")));
                        dropdown01.selectByVisibleText(yoyaku01.getUsePlace());//施設区分の選択
                        Select dropdown02 = new Select(
                                driver.findElement(
                                        By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$drpPurpose")));
                        dropdown02.selectByVisibleText(yoyaku01.getUsePurpose());//利用目的の選択
                        WebElement element06 = driver
                                .findElement(
                                        By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtShisetsuMeisho"));
                        element06.clear();//すでに文字が入っている場合のため、クリア処理
                        element06.sendKeys(yoyaku01.getPlaceName());//施設名を入力する
                        WebElement element07 = driver
                                .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtFromDate"));
                        element07.sendKeys(yoyaku01.getStartDate());//検索範囲の開始日を入力する
                        WebElement element08 = driver
                                .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$txtToDate"));
                        element08.sendKeys(yoyaku01.getEndDate());//検索範囲の終了日を入力する
                        Thread.sleep(1000);//3秒待機
                        WebElement element09 = driver
                                .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$btnSearch"));
                        element09.click();//検索をクリック

                        //カレンダーが表示されて予約する日にちを指定--------------------------------------------------------------------------
                        Thread.sleep(0500);
                        WebElement element10 = driver.findElement(
                                By.xpath("//a[contains(@title,'" + yoyaku01.getReserveDate() + "')]"));
                        element10.click();//カレンダー上の日付をクリック

                        //つどーむ（スポーツ交流）の時はA面(=li.get(0))かB面(=li.get(1))を選択-------------------------
                        String placeName = yoyaku01.getPlaceName();
                        if (placeName.equals("スポーツ交流")) {
                            if (reserveDay.equals("13") || reserveDay.equals("29")) {

                                Thread.sleep(1000);
                                List<WebElement> li01 = driver.findElements(By.linkText("選択"));
                                ;
                                li01.get(0).click();
                            }
                            List<WebElement> li02 = driver.findElements(By.linkText("選択"));
                            ;
                            Thread.sleep(2500);
                            if (i <= 13) {
                                li02.get(0).click();
                            } else if (i >= 14) {
                                li02.get(1).click();
                            }
                        }
                        //曜日によって繰り返し処理の回数を変える(土日=2回,平日=4回)
                        ReserveDateController rdc = new ReserveDateController();
                        String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay);
                        int times = 0;
                        if (youbi.equals("日曜") || youbi.equals("土曜")) {
                            times = 2; //土日の繰り返し回数
                        } else {
                            times = 4; //平日の繰り返し回数
                        }
                        for (int j = 0; j < times; j++) {//予約を"j"回繰り返す <---ここの数字を変更で繰り返し回数指定
                            //時間帯を指定------------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                            //日付から曜日を取得して曜日によって指定する時間帯を変える　曜日の取得　土日は13時から・平日は18時から
                            String cellNo = "0"; //時間帯指定のテーブルデータの何番目かを指定。(0~5)
                            if (placeName == "スポーツ交流") {
                                cellNo = "6";
                            } else if (placeName == "中島" && youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
                                cellNo = "5";
                            } else { //平日の時id:ctl00_ContentPlaceHolder1_JikantaiSel5
                                cellNo = "5";
                            }
                            WebElement element11 = driver
                                    .findElement(By.id("ctl00_ContentPlaceHolder1_JikantaiSel" + cellNo));
                            element11.click();//時間帯を指定
                            WebElement element12 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnShinseiCnf"));
                            element12.click();//申請
                            //申し込み申請確認----------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,600)", "");//500px下にスクロール
                            WebElement element13 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnShinsei"));
                            element13.click();//予約完了
                            System.out.println(yoyaku01.getPlaceName() + "/" + yoyaku01.getReserveDate() + "をID:" + id
                                    + "で予約しました。");
                            //戻るボタン2回
                            driver.navigate().back();
                            driver.navigate().back();
                        }
                        jse.executeScript("window.scrollBy(0,300)", "");//600px下にスクロール
                        //メニューに戻って別の施設の予約をする
                        WebElement backToMenu = driver.findElement(By.linkText("メニューへ戻る"));
                        backToMenu.click();//メニューへ戻る
                    }

                }
                Thread.sleep(100);
                //ログアウト---------------------------------------------------------
                jse.executeScript("window.scrollBy(0,-600)", "");//600px上にスクロール
                WebElement logout = driver.findElement(By.name("ctl00$btnLogout"));
                logout.click();//ログアウトして次のID番号へ繰り返し

            }

            driver.quit();
            lineNotify.notify("sheet No." + sheetName + "の予約が完了しました。");

        } catch (EncryptedDocumentException |

                IOException e) {
            e.printStackTrace();
            lineNotify.notify("予約システムにエラーが発生しました。確認してください。");

        } catch (InterruptedException e) {
            e.printStackTrace();
            lineNotify.notify("予約システムにエラーが発生しました。確認してください。");
        } catch (NullPointerException e) {
            e.printStackTrace();
            lineNotify.notify("予約システムにエラーが発生しました。確認してください。");
        }
    }

}
