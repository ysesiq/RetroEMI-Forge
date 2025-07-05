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

    public static String replaceCharAt(String s, int index, char c) {
        return s.substring(0, index) + c + s.substring(index + 1);
    }
}
