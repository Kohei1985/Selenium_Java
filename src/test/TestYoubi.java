package test;

import reserve.ReserveDateController;


public class TestYoubi {

    public static void main(String[] args) {
        String reserveMonth = "1";
        String rd = "3";
        int rmInt = Integer.parseInt(reserveMonth);
        int rdInt = Integer.parseInt(rd);
        String rmStr = String.format("%02d", rmInt);
        String rdStr = String.format("%02d", rdInt);
        String youbi = ReserveDateController.getYoubi("2021"+ rmStr + rdStr );
        if (youbi.equals("日曜")|| youbi.equals("土曜")){ //土曜日日曜日の時
            System.out.println("土日の予約を実行");

        }else{ //平日の時
            System.out.println("平日の予約を実行");


        }
    }

}
