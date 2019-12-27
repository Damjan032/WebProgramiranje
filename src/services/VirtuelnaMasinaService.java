package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.VMKategorijaDAO;
import dao.VirtuelnaMasinaDAO;
import dao.model.PoljeZaPretragu;
import dto.VirtuelnaMasinaDTO;
import dto.VirtuelnaMasinaDTO.Builder;
import exceptions.BadRequestException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import models.Disk;
import models.VMKategorija;
import models.VirtualMachine;

public class VirtuelnaMasinaService implements Service<String, String> {

    private Gson g = new Gson();
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO = new VirtuelnaMasinaDAO();
    private DiskDAO diskDTO = new DiskDAO();
    private VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();

    @Override
    public List<String> fetchAll() {
        return virtuelnaMasinaDAO.fetchAll().stream().map((vm) -> g.toJson(mapToVirtuelnaMasinaDAO(vm))).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String id) {
        return g.toJson(mapToVirtuelnaMasinaDAO(virtuelnaMasinaDAO.fetchById(id)));
    }

    @Override
    public String create(String req) throws IOException {
        VirtualMachine virtualnaMasina = g.fromJson(req, VirtualMachine.class);
        if (virtuelnaMasinaDAO.fetchByIme(virtualnaMasina.getIme()).isPresent()) {
            throw new BadRequestException("Organizacija sa imenom: " + virtualnaMasina.getIme() + " posotji");
        }
        return g.toJson(mapToVirtuelnaMasinaDAO(virtuelnaMasinaDAO.create(virtualnaMasina)));
    }

    @Override
    public String update(String organizacija, String id) throws IOException {
        return g.toJson(mapToVirtuelnaMasinaDAO(virtuelnaMasinaDAO.update(g.fromJson(organizacija, VirtualMachine.class), id)));
    }

    @Override
    public void delete(String id) throws IOException {
        virtuelnaMasinaDAO.delete(id);
    }

    public List<String> search(Map<String, String[]> params) {

        return virtuelnaMasinaDAO.fetchAll().stream().filter(vm -> {
            boolean toReturn = true;
            for (Map.Entry<String, String[]> param : params.entrySet()) {
                if (!filter(mapToVirtuelnaMasinaDAO(vm), param)) {
                    toReturn = false;
                }
            }
            return toReturn;
        }).map((vm -> g.toJson(mapToVirtuelnaMasinaDAO(vm))))
            .collect(Collectors.toList());
    }


    private VirtuelnaMasinaDTO mapToVirtuelnaMasinaDAO(VirtualMachine virtuelnaMasina) {
        List<Disk> diskovi = new ArrayList<>();

        List<String> diskoviId = virtuelnaMasina.getDiskovi();

        if (diskoviId != null) {
            virtuelnaMasina.getDiskovi().forEach(diskId -> {
                diskovi.add(diskDTO.fetchById(diskId));
            });
        }

        VMKategorija vmKategorija = vmKategorijaDAO.fetchById(virtuelnaMasina.getKategorija()).orElseGet(() -> new VMKategorija(null,
            null, null, null, null));

        return
            new Builder().withId(virtuelnaMasina.getId()).withIme(virtuelnaMasina.getIme()).withDiskovi(diskovi)
                .withKategorija(vmKategorija).build();
    }

    private boolean filter(VirtuelnaMasinaDTO vm, Map.Entry<String, String[]> param) {
        switch (PoljeZaPretragu.toEnum(param.getKey())) {
            case IME:
                return vm.getIme().equalsIgnoreCase(param.getValue()[0]);
            case RAM_OD:
                return Double.parseDouble(param.getValue()[0]) <= vm.getKategorija().getRAM();
            case RAM_DO:
                return Double.parseDouble(param.getValue()[0]) >= vm.getKategorija().getRAM();
            case CPU_JEZGRA_OD:
                return Integer.parseInt(param.getValue()[0]) <= vm.getKategorija().getBrJezgra();
            case CPU_JEZGRA_DO:
                return Integer.parseInt(param.getValue()[0]) >= vm.getKategorija().getBrJezgra();
            case GPU_JEZGRA_OD:
                return Integer.parseInt(param.getValue()[0]) <= vm.getKategorija().getBrGPU();
            case GPU_JEZGRA_DO:
                return Integer.parseInt(param.getValue()[0]) >= vm.getKategorija().getBrGPU();
            default:
                return true;
        }
    }
}
