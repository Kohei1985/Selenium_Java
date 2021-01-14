package system;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReserveDateController {


    public String getYoubi(String yyyy, String mm, String dd){
        try{
            int rmInt = Integer.parseInt(mm);       //月をint型に変換※intにしないとゼロ埋めができないため
            int rdInt = Integer.parseInt(dd);       //日をint型に変換
            String rmStr = String.format("%02d", rmInt);    //月をゼロ埋めしてStringに戻す
            String rdStr = String.format("%02d", rdInt);    //日をゼロ埋めしてStringに戻す
            String ymd = yyyy + rmStr + rdStr;
          //曜日
          String youbi[] = {"日曜","月曜","火曜","水曜","木曜","金曜","土曜"};

          //日付チェック
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          sdf.setLenient(false);
          sdf.parse(ymd);

          //年・月を取得する
          int y = Integer.parseInt(ymd.substring(0,4));
          int m = Integer.parseInt(ymd.substring(4,6))-1;
          int d = Integer.parseInt(ymd.substring(6,8));

          //取得した年月の最終年月日を取得する
          Calendar cal = Calendar.getInstance();
          cal.set(y, m, d);

          //YYYYMMDD形式にして変換して返す
          return youbi[cal.get(Calendar.DAY_OF_WEEK)-1];

        }catch(Exception ex){
          return null;
        }


    }
}
