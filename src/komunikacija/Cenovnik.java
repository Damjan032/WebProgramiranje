package komunikacija;

public class Cenovnik {
    Double vmCore;
    Double vmRAM;
    Double vmCUDA;
    Double diskHDD;
    Double diskSSD;

    public Cenovnik(Double vmCore, Double vmRAM, Double vmCUDA, Double diskHDD, Double diskSSD) {
        this.vmCore = vmCore;
        this.vmRAM = vmRAM;
        this.vmCUDA = vmCUDA;
        this.diskHDD = diskHDD;
        this.diskSSD = diskSSD;
    }

    public Double getVmCore() {
        return vmCore;
    }

    public void setVmCore(Double vmCore) {
        this.vmCore = vmCore;
    }

    public Double getVmRAM() {
        return vmRAM;
    }

    public void setVmRAM(Double vmRAM) {
        this.vmRAM = vmRAM;
    }

    public Double getVmCUDA() {
        return vmCUDA;
    }

    public void setVmCUDA(Double vmCUDA) {
        this.vmCUDA = vmCUDA;
    }

    public Double getDiskHDD() {
        return diskHDD;
    }

    public void setDiskHDD(Double diskHDD) {
        this.diskHDD = diskHDD;
    }

    public Double getDiskSSD() {
        return diskSSD;
    }

    public void setDiskSSD(Double diskSSD) {
        this.diskSSD = diskSSD;
    }
}
