package test;

import java.util.ArrayList;
import java.util.List;

import system.ReserveDateController;

public class TestYoubi {

    public static void main(String[] args) {
        ReserveDateController rdc = new ReserveDateController();
        String reserveMonth = "1"; //<-月を指定
        List<String> reserveDays = new ArrayList<>();
        reserveDays.add("13");//<--予約したい日を指定。
        reserveDays.add("14");//<--予約したい日を指定。
        reserveDays.add("15");//<--予約したい日を指定。
        reserveDays.add("16");//<--予約したい日を指定。

        for (String reserveDay : reserveDays) {
            String youbi = rdc.getYoubi("2021", reserveMonth, reserveDay);

            System.out.println(reserveMonth + "月" + reserveDay + "日" + youbi);
            if (youbi.equals("日曜") || youbi.equals("土曜")) { //土曜日日曜日の時
                System.out.println("土日の予約を実行");

            } else { //平日の時
                System.out.println("平日の予約を実行");

            }
        }
    }
}