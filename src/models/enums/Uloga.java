package models.enums;

public enum Uloga {
    SUPER_ADMIN,
    ADMIN,
    KORISNIK;


    @Override
    public String toString() {
        switch (this){

            case SUPER_ADMIN:
                return "super_admin";
            case ADMIN:
                return "admin";
            default:
                return "korisnik";
        }
    }
}
