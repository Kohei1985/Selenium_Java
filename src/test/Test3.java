package test;

import java.util.ArrayList;
import java.util.List;

import system.DateByPlaceMaster;
import system.ReserveDateController;
import system.Yoyakukun;

public class Test3 {

    public static void main(String[] args) {
        Yoyakukun yoyaku01 = new Yoyakukun("学校開放(屋内)", "サロンフットボール・フットサル", "札幌市", null, "2021/02/01", "2021/02/27",
                null);
        //割り当て↑("-施設区分-",    "-利用目的-",      "-札幌市（固定）-","-施設名称01-","-検索範囲開始日-","-検索範囲終了日-")
        String reserveMonth = "2"; //<-月を指定
        DateByPlaceMaster shinkotoni = new DateByPlaceMaster("1", "2", "3", "4", "5", "6"); //
        DateByPlaceMaster teine = new DateByPlaceMaster("11", "12", "13", "14", "15", "16");
        DateByPlaceMaster shinryo = new DateByPlaceMaster("21", "22", "23", "24", "25", "26");

        List<String> places = new ArrayList<>();
        places.add("新琴似");
        places.add("手稲山口");
        places.add("新陵中");

        for (String place : places) {
            yoyaku01.setPlaceName(place);
            List<String> reserveDays = new ArrayList<>();
            if (yoyaku01.getPlaceName() == "新琴似") {
                reserveDays.add(shinkotoni.getReserveDate01());//<--予約したい日を指定。
                reserveDays.add(shinkotoni.getReserveDate02());//<--予約したい日を指定。
                reserveDays.add(shinkotoni.getReserveDate03());//<--予約したい日を指定。
                reserveDays.add(shinkotoni.getReserveDate04());//<--予約したい日を指定。
                reserveDays.add(shinkotoni.getReserveDate05());//<--予約したい日を指定。
                reserveDays.add(shinkotoni.getReserveDate06());//<--予約したい日を指定。
            }
            if (yoyaku01.getPlaceName() == "手稲山口") {
                reserveDays.add(teine.getReserveDate01());//<--予約したい日を指定。
                reserveDays.add(teine.getReserveDate02());//<--予約したい日を指定。
                reserveDays.add(teine.getReserveDate03());//<--予約したい日を指定。
                reserveDays.add(teine.getReserveDate04());//<--予約したい日を指定。
                reserveDays.add(teine.getReserveDate05());//<--予約したい日を指定。
                reserveDays.add(teine.getReserveDate06());//<--予約したい日を指定。
            }
            if (yoyaku01.getPlaceName() == "新陵中") {
                reserveDays.add(shinryo.getReserveDate01());//<--予約したい日を指定。
                reserveDays.add(shinryo.getReserveDate02());//<--予約したい日を指定。
                reserveDays.add(shinryo.getReserveDate03());//<--予約したい日を指定。
                reserveDays.add(shinryo.getReserveDate04());//<--予約したい日を指定。
                reserveDays.add(shinryo.getReserveDate05());//<--予約したい日を指定。
                reserveDays.add(shinryo.getReserveDate06());//<--予約したい日を指定。
            }

            for (String reserveDay : reserveDays) {
                yoyaku01.setReserveDate(reserveMonth + "月" + reserveDay + "日");
                System.out.println("会場:" + yoyaku01.getPlaceName() + "/" + yoyaku01.getReserveDate());
                ReserveDateController rdc = new ReserveDateController();

                String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay);
                if (youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
                    System.out.println("土日の予約を実行");

                } else { //平日の時
                    System.out.println("平日の予約を実行");
                }
            }
        }
    }

}
