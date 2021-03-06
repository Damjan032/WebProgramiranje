package dto;

import models.Organizacija;
import models.VirtuelnaMasina;
import models.enums.TipDiska;
import models.enums.TipResursa;

public class DiskDTO extends ResursDTO {
    private TipDiska tip;
    private double kapacitet;
    private VirtuelnaMasina vm;

    public DiskDTO(String id, String ime, TipDiska tip, double kapacitet, VirtuelnaMasina vm, Organizacija org) {
        super(id, ime, TipResursa.DISK);
        setOrganizacija(org);
        this.tip = tip;
        this.kapacitet = kapacitet;
        this.vm = vm;
    }

    public TipDiska getTip() {
        return tip;
    }

    public void setTip(TipDiska tip) {
        this.tip = tip;
    }

    public double getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(double kapacitet) {
        this.kapacitet = kapacitet;
    }

    public VirtuelnaMasina getVm() {
        return vm;
    }

    public void setVm(VirtuelnaMasina vm) {
        this.vm = vm;
    }


    public static final class Builder {

        private String id;
        private String ime;
        private TipResursa tipResursa;
        private TipDiska tip;
        private double kapacitet;
        private VirtuelnaMasina vm;
        private Organizacija org;

        public Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withIme(String ime) {
            this.ime = ime;
            return this;
        }

        public Builder withTip(TipDiska tip) {
            this.tip = tip;
            return this;
        }

        public Builder withKapacitet(double kapacitet) {
            this.kapacitet = kapacitet;
            return this;
        }

        public Builder withVm(VirtuelnaMasina vm) {
            this.vm = vm;
            return this;
        }
        
        public Builder withOrg(Organizacija o){
            this.org = o;
            return this;
        }

        public DiskDTO build() {
            DiskDTO diskDTO = new DiskDTO(id, ime, tip, kapacitet, vm, org);
            diskDTO.tipResursa = this.tipResursa;
            return diskDTO;
        }

        public Builder withTipResursa(TipResursa tip) {
            this.tipResursa = tip;
            return this;
        }
    }
}
