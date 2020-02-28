package komunikacija;

import dto.KorisnikDTO;
import services.KorisnikService;

public class LoginPorukaDTO extends Poruka {
    KorisnikDTO k;

    public LoginPorukaDTO(String poruka, boolean status) {
        super(poruka, status);
    }

    public LoginPorukaDTO(String poruka, boolean status, KorisnikDTO k) {
        super(poruka, status);
        this.k = k;
    }

    public static LoginPorukaDTO mapLPtoLPDTO(LoginPoruka lp){

        return new LoginPorukaDTO(lp.poruka,lp.status, KorisnikService.mapKorisniktoKorisnikDTO(lp.k));
    }

}
