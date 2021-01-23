package test;

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

import system.DateByPlaceMaster;
import system.LineNotify;
import system.ReserveDateController;
import system.Yoyakukun;

public class Test4 {
    public static void main(String[] args) throws InterruptedException {
        //インスタンスを生成
        String トークン = "DedPHNC064l3dPTeH9RhPvougOz9TwWmscqroCZtBE8";
        LineNotify lineNotify = new LineNotify(トークン);
        //引数の中に以下の項目を番号で設定
        //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放(内)->03,学校開放(外)->04
        //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,
        //地域(要素2):指定なし（初期値:"札幌市")
        //施設名称:
        Yoyakukun yoyaku01 = new Yoyakukun("スポーツ（屋内）", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",
                null);
        //割り当て↑("-施設区分-","-利用目的-","-札幌市（固定）-","-施設名称01-","-検索範囲開始日-","-検索範囲終了日-")
        List<String> places = new ArrayList<>();
        places.add("中島");
        places.add("");
        places.add("スポーツ交流");
        String reserveMonth = "2"; //<-月を指定
        DateByPlaceMaster hall01 = new DateByPlaceMaster("2", "", "6", "", ""); //
        DateByPlaceMaster hall02 = new DateByPlaceMaster("9", "", "13", "", "27");

        try {

            //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
            Workbook excel;
            excel = WorkbookFactory
                    .create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
            List<String> sheets = new ArrayList<String>();
            Collections.addAll(sheets, "sheet1", "sheet2");// <--ここでシート名を指定**(自分の担当はsheet2)**
            for (String sheetName : sheets) {
                Sheet sheet = excel.getSheet(sheetName);
                for (int i = 24; i <= 25; i++) { //<----エクセルの範囲指定はここ！！1~25までの数字
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
                        continue;
                    }

                    for (String place : places) {
                        yoyaku01.setPlaceName(place);
                        if (yoyaku01.getPlaceName() == null || yoyaku01.getPlaceName().equals("")) {
                            int noPlace = places.indexOf("") + 1;
                            System.out.println("会場リストの" + noPlace + "番目に会場の指定がありません");
                            continue;
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
                                System.out.println("日にちの指定がありません");
                                continue;
                            }
                            yoyaku01.setReserveDate(reserveMonth + "月" + reserveDay + "日");
                            //カレンダーが表示されて予約する日にちを指定--------------------------------------------------------------------------
                            //つどーむ（スポーツ交流）の時はA面(=li.get(0))かB面(=li.get(1))を選択-------------------------
                            String placeName = yoyaku01.getPlaceName();
                            if (placeName.equals("スポーツ交流")) {
                            }
                            //曜日によって繰り返し処理の回数を変える(土日=2回,平日4回)
                            ReserveDateController rdc = new ReserveDateController();
                            String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay);
                            int times = 0;
                            if (youbi.equals("日曜") || youbi.equals("土曜")) {
                                times = 2;
                            } else {
                                times = 4;
                            }
                            for (int j = 0; j < times; j++) {//予約を"j"回繰り返す <---ここの数字を変更で繰り返し回数指定
                                //予約処理------------------------------------------------------------------------
                                System.out.println("会場:" + yoyaku01.getPlaceName() + "/目的:" + yoyaku01.getUsePurpose()
                                        + yoyaku01.getReserveDate() + "(" + youbi + ")");
                            }
                        }

                        //ログアウト---------------------------------------------------------
                    }

                }
                 lineNotify.notify("sheetName:"+ sheetName +"の予約が完了しました。");

            }

        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();

        }

    }

}
