package net.xylose.emi;

public class REMIForge {
    public static String sanitizeNBT(String nbt) {
        nbt = nbt.replace(" ", "");
        if (!nbt.startsWith("{")) {
            nbt = "{" + nbt;
        }
        if (!nbt.endsWith("}")) {
            nbt = nbt + "}";
        }
        return nbt;
    }
}
