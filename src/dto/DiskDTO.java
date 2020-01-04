package dto;

import models.Resurs;
import models.enums.TipDiska;
import models.enums.TipResursa;

public class DiskDTO extends ResursDTO {
    private TipDiska tip;
    private double kapacitet;
    private String vm;

    public DiskDTO(String id, String ime, TipDiska tip, double kapacitet, String vm) {
        super(id, ime);
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

    public String getVm() {
        return vm;
    }

    public void setVm(String vm) {
        this.vm = vm;
    }


    public static final class Builder {

        private String id;
        private String ime;
        private TipResursa tipResursa;
        private TipDiska tip;
        private double kapacitet;
        private String vm;

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

        public Builder withTipResursa(TipResursa tipResursa) {
            this.tipResursa = tipResursa;
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

        public Builder withVm(String vm) {
            this.vm = vm;
            return this;
        }

        public DiskDTO build() {
            DiskDTO diskDTO = new DiskDTO(id, ime, tip, kapacitet, vm);
            diskDTO.tipResursa = this.tipResursa;
            return diskDTO;
        }
    }
}
