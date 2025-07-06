package net.xylose.emi;

import dev.emi.emi.EmiPort;
import net.minecraft.item.Item;
import net.minecraft.util.RegistryNamespaced;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        RegistryNamespaced itemRegistry = Item.itemRegistry;
        for (Object o : itemRegistry) {
            if (o instanceof Item) {
                itemList.add((Item) o);
            }
        }
        return itemList;
    }
}
