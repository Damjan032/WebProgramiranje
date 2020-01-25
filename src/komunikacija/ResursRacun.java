package komunikacija;

import models.enums.TipResursa;

public class ResursRacun {
    String imeResursa;
    TipResursa tipResursa;
    Double cena;

    public ResursRacun(String imeResursa, TipResursa tipResursa, Double cena) {
        this.imeResursa = imeResursa;
        this.tipResursa = tipResursa;
        this.cena = cena;
    }
}
