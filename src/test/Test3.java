package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import system.DateByPlaceMaster;
import system.ReserveDateController;
import system.Yoyakukun;

public class Test3 {

    public static void main(String[] args) throws Exception, IOException {
        Yoyakukun yoyaku01 = new Yoyakukun("学校開放(屋内)", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",
                null);
        //割り当て↑("-施設区分-",    "-利用目的-",      "-札幌市（固定）-","-施設名称01-","-検索範囲開始日-","-検索範囲終了日-")
        String reserveMonth = "2"; //<-月を指定
        DateByPlaceMaster school01 = new DateByPlaceMaster("2", "4", "6", "", ""); //
        DateByPlaceMaster school02 = new DateByPlaceMaster("2", "6", "9", "13", "18");
        DateByPlaceMaster school03 = new DateByPlaceMaster("2", "6", "9", "13", "16");
        DateByPlaceMaster school04 = new DateByPlaceMaster("2", "9", "", "", "");

        List<String> places = new ArrayList<>();
        places.add("新琴似小");
        places.add("手稲山口小");
        places.add("新陵中");
        places.add("屯田北小");

        //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
        Workbook excel;
        excel = WorkbookFactory
                .create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
        Sheet sheet = excel.getSheet("Sheet2");// <--ここでシート名を指定**(自分の担当はsheet2)**
        for (int i = 1; i <= 2; i++) { //<----エクセルの範囲指定はここ！！1~26までの数字
            Row rowC = sheet.getRow(i); //行を読み込み
            Cell cellId = rowC.getCell(2); //Cellを指定(ここは固定)
            String id = cellId.getStringCellValue(); //指定した場所の文字列を取得
            Row rowD = sheet.getRow(i); //行を読み込み
            Cell cellPass = rowD.getCell(3); //Cellを指定(ここは固定)
            String pass = cellPass.getStringCellValue(); //指定した場所の文字列を取得
            System.out.println("No." + i); //件数
            System.out.println("ID:" + id + "/Password:" + pass); //取得したデータを出力
            if (id == null || id.equals("")) {
                System.out.println("シートにデータがありません");
                break;
            }
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
                List<String> reserveDays = new ArrayList<>();
                if (yoyaku01.getPlaceName() == places.get(0)) {
                    Collections.addAll(reserveDays,school01.getReserveDate01(),school01.getReserveDate02(),
                            school01.getReserveDate03(),school01.getReserveDate04(),
                            school01.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                }
                if (yoyaku01.getPlaceName() == places.get(1)) {
                    Collections.addAll(reserveDays,school02.getReserveDate01(),school02.getReserveDate02(),
                            school02.getReserveDate03(),school02.getReserveDate04(),
                            school02.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                }
                if (yoyaku01.getPlaceName() == places.get(2)) {
                    Collections.addAll(reserveDays,school03.getReserveDate01(),school03.getReserveDate02(),
                            school03.getReserveDate03(),school03.getReserveDate04(),
                            school03.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                }
                if (yoyaku01.getPlaceName() == places.get(3)) {
                    Collections.addAll(reserveDays,school04.getReserveDate01(),school04.getReserveDate02(),
                            school04.getReserveDate03(),school04.getReserveDate04(),
                            school04.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
                }

                for (String reserveDay : reserveDays) {
                    if(reserveDay.equals("") || reserveDay == null){
                        System.out.println("日にちの指定がありません");
                        break;
                    }
                    yoyaku01.setReserveDate(reserveMonth + "月" + reserveDay + "日");
                    ReserveDateController rdc = new ReserveDateController();
                    System.out.println("会場:" + yoyaku01.getPlaceName() + "/目的:"+yoyaku01.getUsePurpose()  + yoyaku01.getReserveDate());

                    String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay); //曜日を取得する
                    String cellNo = "0"; 
                    if(yoyaku01.getPlaceName() =="新陵中" || yoyaku01.getPlaceName().equals("新陵中")){
                        cellNo = "0";
                    }else if (youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
                        cellNo = "3";
                    } else { //平日の時id:ctl00_ContentPlaceHolder1_JikantaiSel0
                        cellNo = "0";
                    }
                    System.out.println("ctl00_ContentPlaceHolder1_JikantaiSel"+ cellNo +"を予約");

                }
            }
        }
    }

}
