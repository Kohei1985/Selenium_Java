package test;

import java.util.ArrayList;
import java.util.List;

import system.Yoyakukun;

public class Test2 {

    public static void main(String[] args) {
        Yoyakukun yoyaku01 = new Yoyakukun("スポーツ（屋内）", "サロンフットボール・フットサル", "札幌市", null,"2021/02/01","2021/02/27",null);
        String placeName = yoyaku01.getPlaceName();
        if(placeName.equals("スポーツ交流")){
            System.out.println("等しい");
        }
        List<String> days = new ArrayList<>();
        days.add("4");

        String month = "2";
        for(String day : days){
            System.out.println(month + "月" + day + "日");
        }

    }

}
