package models.enums;

public enum TipDiska {
    SSD,
    HDD;

    public static TipDiska fromString(String tip) {
        if (tip.equals("SSD")){
            return SSD;
        }else{
            return HDD;
        }
    }
}
