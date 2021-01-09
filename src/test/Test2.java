package test;

import java.util.ArrayList;
import java.util.List;

import ReserveSystem.Yoyakukun;

public class Test2 {

    public static void main(String[] args) {
        Yoyakukun yoyaku01 = new Yoyakukun("スポーツ（屋内）", "サロンフットボール・フットサル", "札幌市", "中島","2021/02/01","2021/02/27");
        String placeName = yoyaku01.getPlaceName();
        if(placeName.equals("スポーツ交流")){
            System.out.println("等しい");
        }
        List<String> reserveDates = new ArrayList<>();
        reserveDates.add("2月4日");
        reserveDates.add("2月11日");
        reserveDates.add("2月18日");
        reserveDates.add("2月25日");

        for(String reserveDate : reserveDates){
            System.out.println(reserveDate);
        }

    }

}
