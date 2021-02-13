package test;



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

import persons.Person;
import system.DateByPlaceMaster;
import system.ReserveDateController;
import system.Yoyakukun;


        public class Test5 {
            public static void main(String[] args) throws InterruptedException {
              //インスタンス生成
                Yoyakukun yoyaku01 = new Yoyakukun
                        ("学校開放（屋内）", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",null);
                //割り当て↑("-施設区分-",    "-利用目的-",      "-札幌市（固定）-","-施設名称[null]","-検索範囲開始日-","-検索範囲終了日-")
                //引数の中に以下の項目を番号で設定
                //利用場所(要素0):スポーツ屋外->01,スポーツ屋内->02,学校開放（屋内）->03,学校開放（屋外）->04
                //利用目的(要素1):屋外サッカー->004,サロンフットボール・フットサル->029,サロンフットボール・フットサル->064,サッカー->052,
                //地域(要素2):指定なし（初期値:"札幌市")
                //施設名称:
                Person person1 = new Person("雉子谷","sheet1","新琴似小/2/2/2","手稲山口小/3/3/3","新陵中/4/4/4","none/none/none/none");
                Person person2 = new Person("浩平","sheet2","新琴似小/6/6/6","手稲山口小/7/7/7","新陵中/8/8/8","none/none/none/none/");
                Person person3 = new Person("タオ・庄司","sheet3","新琴似小/9/9/9","手稲山口小/10/10/10","新陵中/11/11/11","none/none/none/none/");
                Person person4 = new Person("前田","sheet4","新琴似小/12/12/12","手稲山口小/13/13/13","新陵中/14/14/14","none/none/none/none/");
                String reserveMonth = "3"; //<-月を指定

        //---------------------------------------------------------------------------------------------
//                      日付を格納するインスタンスを生成
                DateByPlaceMaster school01 = new DateByPlaceMaster("","","","",""); //
                DateByPlaceMaster school02 = new DateByPlaceMaster("","","","","");
                DateByPlaceMaster school03 = new DateByPlaceMaster("","","","","");
                DateByPlaceMaster school04 = new DateByPlaceMaster("","","","","");
        //---------------------------------------------------------------------------------------------

        //------------------------------------------------------------------------------------------------------------------
                //定数定義
               // String トークン = "DedPHNC064l3dPTeH9RhPvougOz9TwWmscqroCZtBE8";
            //    LineNotify lineNotify = new LineNotify(トークン);//ライン
                String msg = ""; //送信内容を格納する変数
                String Name = ""; //シートに合わせて名前を格納する変数
                List<String> sheetNames = new ArrayList<String>();
                Collections.addAll(sheetNames, person1.getSheet_nm(), person2.getSheet_nm(), person3.getSheet_nm(),person4.getSheet_nm());//"sheet1", "sheet2", "sheet3",
//                  ...................................................................
//                  |sheet1 = 雉子谷さん    sheet2 = 浩平     sheet3 = タオ・庄司コーチ    |
//                  |sheet4 = 前田コーチ                                                 |
//                  ...................................................................

//                String sheetName = "sheet2";// <--ここでシート名を指定**(自分の担当はsheet2)**
        //------------------------------------------------------------------------------------------------------------------

                try {


                    //エクセルファイルへアクセスしてID番号を取得する--------------------------------------
                    Workbook excel;
                    excel = WorkbookFactory
                            .create(new File("/Users/yamamotokouhei/Documents/Selenium_Java/ReserveDataSeparated.xlsx"));//Excelfileにアクセス
                    for (String sheetName : sheetNames){
                        Sheet sheet = excel.getSheet(sheetName);
                        List<String> places = new ArrayList<>();
                        Collections.addAll(places, "新琴似小","手稲山口小","新陵中","屯田北小");  //,<-ここに予約したい施設名を追加*138行目以降のschoolの個数と確認
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
                            Collections.addAll(chk_places ,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));
                            school01.setReserveDate01(instracts01.get(1));
                            school01.setReserveDate02(instracts01.get(2));
                            school01.setReserveDate03(instracts01.get(3));
                            school02.setReserveDate01(instracts02.get(1));
                            school02.setReserveDate02(instracts02.get(2));
                            school02.setReserveDate03(instracts02.get(3));
                            school03.setReserveDate01(instracts03.get(1));
                            school03.setReserveDate02(instracts03.get(2));
                            school03.setReserveDate03(instracts03.get(3));
                            school04.setReserveDate01(instracts04.get(1));
                            school04.setReserveDate02(instracts04.get(2));
                            school04.setReserveDate03(instracts04.get(3));
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
                            Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));
                            school01.setReserveDate01(instracts01.get(1));
                            school01.setReserveDate02(instracts01.get(2));
                            school01.setReserveDate03(instracts01.get(3));
                            school02.setReserveDate01(instracts02.get(1));
                            school02.setReserveDate02(instracts02.get(2));
                            school02.setReserveDate03(instracts02.get(3));
                            school03.setReserveDate01(instracts03.get(1));
                            school03.setReserveDate02(instracts03.get(2));
                            school03.setReserveDate03(instracts03.get(3));
                            school04.setReserveDate01(instracts04.get(1));
                            school04.setReserveDate02(instracts04.get(2));
                            school04.setReserveDate03(instracts04.get(3));
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
                            Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));
                            school01.setReserveDate01(instracts01.get(1));
                            school01.setReserveDate02(instracts01.get(2));
                            school01.setReserveDate03(instracts01.get(3));
                            school02.setReserveDate01(instracts02.get(1));
                            school02.setReserveDate02(instracts02.get(2));
                            school02.setReserveDate03(instracts02.get(3));
                            school03.setReserveDate01(instracts03.get(1));
                            school03.setReserveDate02(instracts03.get(2));
                            school03.setReserveDate03(instracts03.get(3));
                            school04.setReserveDate01(instracts04.get(1));
                            school04.setReserveDate02(instracts04.get(2));
                            school04.setReserveDate03(instracts04.get(3));
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
                            Collections.addAll(chk_places,instracts01.get(0),instracts02.get(0),instracts03.get(0),instracts04.get(0));
                            school01.setReserveDate01(instracts01.get(1));
                            school01.setReserveDate02(instracts01.get(2));
                            school01.setReserveDate03(instracts01.get(3));
                            school02.setReserveDate01(instracts02.get(1));
                            school02.setReserveDate02(instracts02.get(2));
                            school02.setReserveDate03(instracts02.get(3));
                            school03.setReserveDate01(instracts03.get(1));
                            school03.setReserveDate02(instracts03.get(2));
                            school03.setReserveDate03(instracts03.get(3));
                            school04.setReserveDate01(instracts04.get(1));
                            school04.setReserveDate02(instracts04.get(2));
                            school04.setReserveDate03(instracts04.get(3));
                        }
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
                                System.out.println("ExcelFileにデータがありません");
                                break;
                            }


//                            System.out.println("デバック"+places);
                            for (String place : places) {
                                yoyaku01.setPlaceName(place);
//                                System.out.println("デバック２"+yoyaku01.getPlaceName());
                                if(yoyaku01.getPlaceName() == null || yoyaku01.getPlaceName().equals("")){
                                    int noPlace = places.indexOf("")+1;
                                    System.out.println("会場リストの" + noPlace + "番目に会場の指定がありません");
                                }else if(yoyaku01.getPlaceName() == "屯田北小" || yoyaku01.getPlaceName().equals("屯田北小")) {
                                    yoyaku01.setUsePurpose("トレーニング");
                                }else{
                                    yoyaku01.setUsePurpose("サロンフットボール・フットサル");
                                }
                                //List　"reserveDays"に施設ごとの予約してい日を格納し、拡張for文でreserveDayを繰り返す処理
                                List<String> reserveDays = new ArrayList<>();
                                if (yoyaku01.getPlaceName().equals(chk_places.get(0))) {
                                    Collections.addAll(reserveDays,school01.getReserveDate01(),school01.getReserveDate02(),
                                            school01.getReserveDate03(),school01.getReserveDate04(),
                                            school01.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
//                                    System.out.println("デバック３"+yoyaku01.getPlaceName());
                                }
                                if (yoyaku01.getPlaceName().equals(chk_places.get(1))) {
                                    Collections.addAll(reserveDays,school02.getReserveDate01(),school02.getReserveDate02(),
                                            school02.getReserveDate03(),school02.getReserveDate04(),
                                            school02.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
//                                    System.out.println("デバック４"+yoyaku01.getPlaceName());
                                }
                                if (yoyaku01.getPlaceName() == chk_places.get(2) || yoyaku01.getPlaceName().equals(chk_places.get(2))) {
                                    Collections.addAll(reserveDays,school03.getReserveDate01(),school03.getReserveDate02(),
                                            school03.getReserveDate03(),school03.getReserveDate04(),
                                            school03.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
//                                    System.out.println("デバック５"+yoyaku01.getPlaceName());
                                }
                                if (yoyaku01.getPlaceName().equals(chk_places.get(3))) {
                                    Collections.addAll(reserveDays,school04.getReserveDate01(),school04.getReserveDate02(),
                                            school04.getReserveDate03(),school04.getReserveDate04(),
                                            school04.getReserveDate05());//DateByPlaceクラスからリストに日にちを格納
//                                    System.out.println("デバック６"+yoyaku01.getPlaceName());
                                }

                                //ここから繰り返し処理
                                for (String reserveDay : reserveDays) {
                                    if(reserveDay.equals("") || reserveDay == null || reserveDay.equals("none")){
                                        System.out.println("日にちの指定がありません");
                                        break;
                                    }

                                    //時間帯を指定------------------------------------------------------------------------
                                    //日付から曜日を取得して曜日によって指定する時間帯を変える　曜日の取得　土日は13時から・平日は18時から
                                    ReserveDateController rdc = new ReserveDateController(); //曜日取得のオブジェクトを生成
                                    String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay); //曜日を取得する
//                                    String cellNo; //時間帯指定のテーブルデータの何番目かを指定。(0~5)
//                                    if(yoyaku01.getPlaceName() =="新陵中" || yoyaku01.getPlaceName().equals("新陵中")){
//                                        cellNo = "0";
//                                    }else if (youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
//                                        cellNo = "3";
//                                    } else { //平日の時id:ctl00_ContentPlaceHolder1_JikantaiSel0
//                                        cellNo = "0";
//                                    }

                                    //申し込み申請確認----------------------------------------------------------------------
                                    System.out.println(yoyaku01.getPlaceName() + "/" + reserveMonth + "月" + reserveDay + "日" + youbi + "をID"
                                            + id + "で予約しました。");
                                    //”別の日を指定して予約する”ボタンをクリックする
                                }
                            }

                            Thread.sleep(1500);
                            //ログアウト---------------------------------------------------------

                        }
                        msg = Name + "コーチの抽選申し込み完了しました\r\n";
                        System.out.println(msg);
                    }
                    //lineNotify.notify(msg);

                } catch (EncryptedDocumentException | IOException e) {
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }

