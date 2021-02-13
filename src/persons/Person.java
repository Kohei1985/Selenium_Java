package persons;

public class Person {

    private String coach_nm;
    private String sheet_nm;
    private String place_nm;
    private String rsv_info01;
    private String rsv_info02;
    private String rsv_info03;
    private String rsv_info04;

    //引数なしコンストラクタのみ、値は後でセット
    public Person(){
    }
    public Person(String coach_nm, String sheet_nm, String rsv_info01
            , String rsv_info02, String rsv_info03, String rsv_info04) {
        this.coach_nm = coach_nm;
        this.sheet_nm = sheet_nm;
        this.rsv_info01 = rsv_info01;
        this.rsv_info02 = rsv_info02;
        this.rsv_info03 = rsv_info03;
        this.rsv_info04 = rsv_info04;
    }




    public String getCoach_nm() {
        return coach_nm;
    }
    public void setCoach_nm(String coach_nm) {
        this.coach_nm = coach_nm;
    }
    public String getSheet_nm() {
        return sheet_nm;
    }
    public void setSheet_nm(String sheet_nm) {
        this.sheet_nm = sheet_nm;
    }
    public String getPlace_nm() {
        return place_nm;
    }
    public void setPlace_nm(String place_nm) {
        this.place_nm = place_nm;
    }
    public String getRsv_info01() {
        return rsv_info01;
    }
    public void setRsv_info01(String rsv_info01) {
        this.rsv_info01 = rsv_info01;
    }
    public String getRsv_info02() {
        return rsv_info02;
    }
    public void setRsv_info02(String rsv_info02) {
        this.rsv_info02 = rsv_info02;
    }
    public String getRsv_info03() {
        return rsv_info03;
    }
    public void setRsv_info03(String rsv_info03) {
        this.rsv_info03 = rsv_info03;
    }
    public String getRsv_info04() {
        return rsv_info04;
    }
    public void setRsv_info04(String rsv_info04) {
        this.rsv_info04 = rsv_info04;
    }


}
