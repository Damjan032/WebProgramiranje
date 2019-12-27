package dao.model;

import java.util.Arrays;

public enum PoljeZaPretragu {
    IME("ime"),
    CPU_JEZGRA_OD("cpuOd"),
    CPU_JEZGRA_DO("cpuDo"),
    RAM_OD("ramOd"),
    RAM_DO("ramDo"),
    GPU_JEZGRA_OD("gpuOd"),
    GPU_JEZGRA_DO("gpuDo");

    String value;

    PoljeZaPretragu(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PoljeZaPretragu toEnum(final String s){
        return Arrays.stream(values()).filter(value -> value.getValue().equalsIgnoreCase(s)).findFirst().orElse(null);
    }
}
