package reserve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import persons.Person;
import system.DateByPlaceMaster;
import system.LineNotify;
import system.ReserveDateController;
import system.Yoyakukun;

public class MainSystemOpenSchoolAll {

    public static void main(String[] args) throws InterruptedException {
//インスタンス生成
        Yoyakukun yoyaku01 = new Yoyakukun
                ("学校開放（屋内）", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",null);
        //割り当て↑("-施設区分-",    "-利用目的-",      "-札幌市（固定）-","-施設名称[null]","-検索範囲開始日-","-検索範囲終了日-")
        //引数の中に以下の項目を番号で設定
        //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放（屋内）->03,学校開放（屋外）->04
        //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,

        String reserveMonth = "3"; //<-月を指定
        //各コーチの担当日時場所を格納　データ無しの場合はnoneを入れる。
        Person person1 = new Person("雉子谷","sheet1","新琴似小/1/2/3","手稲山口小/4/5/6","新陵中/7/8/9","none/none/none/none");
        Person person2 = new Person("浩平","sheet2","新琴似小/1/2/3","手稲山口小/4/5/6","新陵中/7/8/9","none/none/none/none");
        Person person3 = new Person("タオ・庄司","sheet3","新琴似小/1/2/3","手稲山口小/4/5/6","新陵中/7/8/9","none/none/none/none");
        Person person4 = new Person("前田","sheet4","新琴似小/1/2/3","手稲山口小/4/5/6","新陵中/7/8/9","none/none/none/none");

//---------------------------------------------------------------------------------------------
//              日付を格納するインスタンスを生成
        DateByPlaceMaster school01 = new DateByPlaceMaster("","","","",""); //
        DateByPlaceMaster school02 = new DateByPlaceMaster("","","","","");
        DateByPlaceMaster school03 = new DateByPlaceMaster("","","","","");
        DateByPlaceMaster school04 = new DateByPlaceMaster("","","","","");
//---------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------------------------
        //定数定義
        String トークン = "DedPHNC064l3dPTeH9RhPvougOz9TwWmscqroCZtBE8";
        LineNotify lineNotify = new LineNotify(トークン);//ライン
        String msg = ""; //送信内容を格納する変数
        String Name = ""; //シートに合わせて名前を格納する変数
        List<String> sheetNames = new ArrayList<String>();
        Collections.addAll(sheetNames, person1.getSheet_nm(), person2.getSheet_nm(), person3.getSheet_nm(),person4.getSheet_nm());//"sheet1", "sheet2", "sheet3",
//          ...................................................................
//          |sheet1 = 雉子谷さん    sheet2 = 浩平     sheet3 = タオ・庄司コーチ    |
//          |sheet4 = 前田コーチ                                                 |
//          ...................................................................

//        String sheetName = "sheet2";// <--ここでシート名を指定**(自分の担当はsheet2)**
//------------------------------------------------------------------------------------------------------------------

        try {

            //ChoromeDriverをセット＆定義
            System.setProperty("webdriver.chrome.driver", "./exe/chromedriver");
            WebDriver driver = new ChromeDriver();
            WebDriverWait wait = new WebDriverWait(driver, 10);//指定したdriverに対して最大で10秒間待つように設定する
            //札幌市施設予約のページを開く
            driver.get("https://www.city.sapporo.jp/shisetsu-yoyaku/");
            WebElement element = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.linkText("札幌市公共施設予約情報システムへ"))); //リンクテキスト名が"札幌市公共施設予約情報システムへ"の要素を取得
            element.click();//"札幌市公共施設予約情報システムへ"のリンクをクリック

            //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
            Workbook excel;
            excel = WorkbookFactory
                    .create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
            //人数分のエクセルシートを繰り返し処理
            for (String sheetName : sheetNames){
                Sheet sheet = excel.getSheet(sheetName);
                List<String> chk_places = new ArrayList<>();
                if(sheetName == "sheet1"){
                    Name = person1.getCoach_nm();
                    String rsv1 = person1.getRsv_info01();
                    String rsv2 = person1.getRsv_info02();
                    String rsv3 = person1.getRsv_info03();
                    String rsv4 = person1.getRsv_info04();
                    List<String> instracts01 = Arrays.asList(rsv1.split("/"));
                    List<String> instracts02 = Arrays.asList(rsv2.split("/"));
                    List<String> instracts03 = Arrays.asList(rsv3.split("/"));
                    List<String> instracts04 = Arrays.asList(rsv4.split("/"));
                    Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
                    school01.setReserveDate01(instracts01.get(1));
                    school01.setReserveDate02(instracts01.get(2));
                    school01.setReserveDate03(instracts01.get(3));
           //         school01.setReserveDate04(instracts01.get(4));
                    school02.setReserveDate01(instracts02.get(1));
                    school02.setReserveDate02(instracts02.get(2));
                    school02.setReserveDate03(instracts02.get(3));
           //        school02.setReserveDate04(instracts02.get(4));
                    school03.setReserveDate01(instracts03.get(1));
                    school03.setReserveDate02(instracts03.get(2));
                    school03.setReserveDate03(instracts03.get(3));
           //         school03.setReserveDate04(instracts03.get(4));
                    school04.setReserveDate01(instracts04.get(1));
                    school04.setReserveDate02(instracts04.get(2));
                    school04.setReserveDate03(instracts04.get(3));
           //         school04.setReserveDate04(instracts04.get(4));
                }else if(sheetName == "sheet2"){
                    Name = person2.getCoach_nm();
                    String rsv1 = person2.getRsv_info01();
                    String rsv2 = person2.getRsv_info02();
                    String rsv3 = person2.getRsv_info03();
                    String rsv4 = person2.getRsv_info04();
                    List<String> instracts01 = Arrays.asList(rsv1.split("/"));
                    List<String> instracts02 = Arrays.asList(rsv2.split("/"));
                    List<String> instracts03 = Arrays.asList(rsv3.split("/"));
                    List<String> instracts04 = Arrays.asList(rsv4.split("/"));
                    Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
                    school01.setReserveDate01(instracts01.get(1));
                    school01.setReserveDate02(instracts01.get(2));
                    school01.setReserveDate03(instracts01.get(3));
           //         school01.setReserveDate04(instracts01.get(4));
                    school02.setReserveDate01(instracts02.get(1));
                    school02.setReserveDate02(instracts02.get(2));
                    school02.setReserveDate03(instracts02.get(3));
           //        school02.setReserveDate04(instracts02.get(4));
                    school03.setReserveDate01(instracts03.get(1));
                    school03.setReserveDate02(instracts03.get(2));
                    school03.setReserveDate03(instracts03.get(3));
           //         school03.setReserveDate04(instracts03.get(4));
                    school04.setReserveDate01(instracts04.get(1));
                    school04.setReserveDate02(instracts04.get(2));
                    school04.setReserveDate03(instracts04.get(3));
           //         school04.setReserveDate04(instracts04.get(4));
                }else if(sheetName == "sheet3"){
                    Name = person3.getCoach_nm();
                    String rsv1 = person3.getRsv_info01();
                    String rsv2 = person3.getRsv_info02();
                    String rsv3 = person3.getRsv_info03();
                    String rsv4 = person3.getRsv_info04();
                    List<String> instracts01 = Arrays.asList(rsv1.split("/"));
                    List<String> instracts02 = Arrays.asList(rsv2.split("/"));
                    List<String> instracts03 = Arrays.asList(rsv3.split("/"));
                    List<String> instracts04 = Arrays.asList(rsv4.split("/"));
                    Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
                    school01.setReserveDate01(instracts01.get(1));
                    school01.setReserveDate02(instracts01.get(2));
                    school01.setReserveDate03(instracts01.get(3));
           //         school01.setReserveDate04(instracts01.get(4));
                    school02.setReserveDate01(instracts02.get(1));
                    school02.setReserveDate02(instracts02.get(2));
                    school02.setReserveDate03(instracts02.get(3));
           //        school02.setReserveDate04(instracts02.get(4));
                    school03.setReserveDate01(instracts03.get(1));
                    school03.setReserveDate02(instracts03.get(2));
                    school03.setReserveDate03(instracts03.get(3));
           //         school03.setReserveDate04(instracts03.get(4));
                    school04.setReserveDate01(instracts04.get(1));
                    school04.setReserveDate02(instracts04.get(2));
                    school04.setReserveDate03(instracts04.get(3));
           //         school04.setReserveDate04(instracts04.get(4));
                }else if(sheetName == "sheet4"){
                    Name = person4.getCoach_nm();
                    String rsv1 = person4.getRsv_info01();
                    String rsv2 = person4.getRsv_info02();
                    String rsv3 = person4.getRsv_info03();
                    String rsv4 = person4.getRsv_info04();
                    List<String> instracts01 = Arrays.asList(rsv1.split("/"));
                    List<String> instracts02 = Arrays.asList(rsv2.split("/"));
                    List<String> instracts03 = Arrays.asList(rsv3.split("/"));
                    List<String> instracts04 = Arrays.asList(rsv4.split("/"));
                    Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
                    school01.setReserveDate01(instracts01.get(1));
                    school01.setReserveDate02(instracts01.get(2));
                    school01.setReserveDate03(instracts01.get(3));
           //         school01.setReserveDate04(instracts01.get(4));
                    school02.setReserveDate01(instracts02.get(1));
                    school02.setReserveDate02(instracts02.get(2));
                    school02.setReserveDate03(instracts02.get(3));
           //        school02.setReserveDate04(instracts02.get(4));
                    school03.setReserveDate01(instracts03.get(1));
                    school03.setReserveDate02(instracts03.get(2));
                    school03.setReserveDate03(instracts03.get(3));
           //         school03.setReserveDate04(instracts03.get(4));
                    school04.setReserveDate01(instracts04.get(1));
                    school04.setReserveDate02(instracts04.get(2));
                    school04.setReserveDate03(instracts04.get(3));
           //         school04.setReserveDate04(instracts04.get(4));
                }
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
                    //                Thread.sleep(3000);//3秒待機

                    JavascriptExecutor jse = (JavascriptExecutor) driver;
                    jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                    WebElement element01 = wait.until(
                            ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_btnMenuLogin"))); //施設予約ログインを探す
                    element01.click(); //"施設予約ログイン"のリンクをクリック
                    //ログイン画面でID/Passwordを入力する
                    WebElement element02 = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_txtRiyoushaID"))); //指定された要素(検索テキストボックス)が表示状態になるまで待機する
                    element02.sendKeys(id);//IDボックスにエクセルから取得したID入力する
                    WebElement element03 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_txtPassword"));
                    element03.sendKeys(pass);//エクセルから取得したPasswordを入力
                    WebElement element04 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnLogin"));
                    element04.click();//"ログインをクリック"

                    List<String> places = new ArrayList<>();
                    Collections.addAll(places,"新琴似小","手稲山口小","新陵中","屯田北小");  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
                    for (String place : places) {
                        yoyaku01.setPlaceName(place);
                        if(yoyaku01.getPlaceName() == null || yoyaku01.getPlaceName().equals("")){
                            int noPlace = places.indexOf("")+1;
                            System.out.println("会場リストの" + noPlace + "番目に会場の指定がありません");
                        }else if(yoyaku01.getPlaceName() == "屯田北小" || yoyaku01.getPlaceName().equals("屯田北小")) {
                            yoyaku01.setUsePurpose("トレーニング");
                        }else{
                            yoyaku01.setUsePurpose("サロンフットボール・フットサル");
                        }
                    //メインメニュー画面で施設予約検索をクリック
                        jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                        Thread.sleep(1500);
                        WebElement element05 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnMenuShinseiSrch"));
                        element05.click();//施設予約検索をクリック
                    //施設の予約検索オペレーション--------------------------------------------------------------------
                        driver.navigate().refresh(); //ページをリフレッシュ
                        Thread.sleep(1000);//1秒待機
                        jse.executeScript("window.scrollBy(0,300)", "");//500px下にスクロール
                        Select dropdown01 = new Select(
                                driver.findElement(
                                        By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$drpPurposeBunrui")));
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
                        Thread.sleep(1500);//3秒待機
                        WebElement element09 = driver
                                .findElement(By.name("ctl00$ContentPlaceHolder1$ShinseiKumiawaseInp1$btnSearch"));
                        element09.click();//検索をクリック
                        //List　"reserveDays"に施設ごとの予約してい日を格納し、拡張for文でreserveDayを繰り返す処理


                        List<String> reserveDays = new ArrayList<>();
                        //Place(会場）のリストの順番と名前が予約設定情報と一致しているかチェックして一致していれば日付を格納する。-----
                        if (yoyaku01.getPlaceName().equals(chk_places.get(0))) {
                            Collections.addAll(reserveDays,school01.getReserveDate01(),school01.getReserveDate02(),
                                    school01.getReserveDate03(),school01.getReserveDate04(),
                                    school01.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                        }
                        if (yoyaku01.getPlaceName().equals(chk_places.get(1))) {
                            Collections.addAll(reserveDays,school02.getReserveDate01(),school02.getReserveDate02(),
                                    school02.getReserveDate03(),school02.getReserveDate04(),
                                    school02.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                        }
                        if (yoyaku01.getPlaceName().equals(chk_places.get(2))) {
                            Collections.addAll(reserveDays,school03.getReserveDate01(),school03.getReserveDate02(),
                                    school03.getReserveDate03(),school03.getReserveDate04(),
                                    school03.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                        }
                        if (yoyaku01.getPlaceName().equals(chk_places.get(3))) {
                            Collections.addAll(reserveDays,school04.getReserveDate01(),school04.getReserveDate02(),
                                    school04.getReserveDate03(),school04.getReserveDate04(),
                                    school04.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                        }

                        //ここから繰り返し処理
                        for (String reserveDay : reserveDays) {
                            if(reserveDay.equals("") || reserveDay == null ||reserveDay.equals("none")){
                                System.out.println("日にちの指定がありません");
                                break;
                            }

                         //カレンダーが表示されて予約する日にちを指定--------------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,300)", "");//300px下にスクロール
                            Thread.sleep(2000);
                            WebElement element10 = driver
                                    .findElement(
                                            By.xpath("//a[contains(@title,'" + reserveMonth + "月" + reserveDay + "日')]"));
                            element10.click();//カレンダー上の日付をクリック
                            //時間帯を指定------------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,500)", "");//500px下にスクロール
                            //日付から曜日を取得して曜日によって指定する時間帯を変える　曜日の取得　土日は13時から・平日は18時から
                            ReserveDateController rdc = new ReserveDateController(); //曜日取得のオブジェクトを生成
                            String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay); //曜日を取得する
                            String cellNo = "0"; //時間帯指定のテーブルデータの何番目かを指定。(0~5)
                            if(yoyaku01.getPlaceName() =="新陵中" || yoyaku01.getPlaceName().equals("新陵中")){
                                cellNo = "0";
                            }else if (youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
                                cellNo = "3";
                            } else { //平日の時id:ctl00_ContentPlaceHolder1_JikantaiSel0
                                cellNo = "0";
                            }

                            WebElement element11 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_JikantaiSel"+ cellNo));
                            element11.click();//時間帯を指定

                            WebElement element12 = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnShinseiCnf"));
                            element12.click();//申請
                            //申し込み申請確認----------------------------------------------------------------------
                            jse.executeScript("window.scrollBy(0,600)", "");//600px下にスクロール
                            WebElement element13 = driver.findElement(By.name("ctl00$ContentPlaceHolder1$btnShinsei"));
                            element13.click();//予約完了
                            System.out.println(yoyaku01.getPlaceName() + "/" + reserveMonth + "月" + reserveDay + "日" + youbi + "をID"
                                    + id + "で予約しました。");
                            //”別の日を指定して予約する”ボタンをクリックする
                            jse.executeScript("window.scrollBy(0,300)", "");//600px下にスクロール
                            WebElement otherdate = driver.findElement(By.id("ctl00_ContentPlaceHolder1_btnDateYoyaku"));
                            otherdate.click();//別日程を検索
                        }
                        jse.executeScript("window.scrollBy(0,300)", "");//600px下にスクロール
                        //メニューに戻って別の施設の予約をする
                        WebElement backToMenu = driver.findElement(By.linkText("メニューへ戻る"));
                        backToMenu.click();//メニューへ戻る
                    }

                    Thread.sleep(1500);
                    //ログアウト---------------------------------------------------------
                    jse.executeScript("window.scrollBy(0,-600)", "");//600px上にスクロール
                    WebElement logout = driver.findElement(By.name("ctl00$btnLogout"));
                    logout.click();//ログアウトして次のID番号へ繰り返し

                }
                msg = msg + Name + "コーチの抽選申し込み完了しました\r\n";
            }
            lineNotify.notify(msg);
            driver.quit();

        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
