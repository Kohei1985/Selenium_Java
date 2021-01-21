package system;

public class DateByPlaceMaster {

    private String reserveDate01;
    private String reserveDate02;
    private String reserveDate03;
    private String reserveDate04;
    private String reserveDate05;


    //引数なしコンストラクタ
    public DateByPlaceMaster(){

    }
    //引数ありコンストラクタ
    public DateByPlaceMaster(String reserveDate01,String reserveDate02,String reserveDate03,
            String reserveDate04,String reserveDate05){
        this.reserveDate01 = reserveDate01;
        this.reserveDate02 = reserveDate02;
        this.reserveDate03 = reserveDate03;
        this.reserveDate04 = reserveDate04;
        this.reserveDate05 = reserveDate05;
    }
    public String getReserveDate01() {
        return reserveDate01;
    }
    public void setReserveDate01(String reserveDate01) {
        this.reserveDate01 = reserveDate01;
    }
    public String getReserveDate02() {
        return reserveDate02;
    }
    public void setReserveDate02(String reserveDate02) {
        this.reserveDate02 = reserveDate02;
    }
    public String getReserveDate03() {
        return reserveDate03;
    }
    public void setReserveDate03(String reserveDate03) {
        this.reserveDate03 = reserveDate03;
    }
    public String getReserveDate04() {
        return reserveDate04;
    }
    public void setReserveDate04(String reserveDate04) {
        this.reserveDate04 = reserveDate04;
    }
    public String getReserveDate05() {
        return reserveDate05;
    }
    public void setReserveDate05(String reserveDate05) {
        this.reserveDate05 = reserveDate05;
    }


}
