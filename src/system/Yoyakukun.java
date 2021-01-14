package system;

public class Yoyakukun {
    //フィールド
    private String usePlace;
    private String usePurpose;
    private String district = "札幌市";
    private String placeName ;
    private String startDate;
    private String endDate;
    private String reserveDate;

    //引数なしコンストラクタ
    public Yoyakukun (){
    }
    //引数ありコンストラクタ
    public Yoyakukun (String usePlace, String usePurpose,
                    String district, String placeName,
                    String startDate, String endDate,
                    String reserveDate){
        this.usePlace = usePlace;
        this.usePurpose = usePurpose;
        this.district = district;
        this.placeName = placeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reserveDate = reserveDate;
    }
    //getter/setter
    public String getUsePlace() {
        return usePlace;
    }
    public void setUsePlace(String usePlace) {
        this.usePlace = usePlace;
    }
    public String getUsePurpose() {
        return usePurpose;
    }
    public void setUsePurpose(String usePurpose) {
        this.usePurpose = usePurpose;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getReserveDate() {
        return reserveDate;
    }
    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

}
