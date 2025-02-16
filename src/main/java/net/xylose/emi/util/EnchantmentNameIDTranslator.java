package net.xylose.emi.util;

import net.minecraft.enchantment.Enchantment;

import java.util.Arrays;
import java.util.Objects;

public class EnchantmentNameIDTranslator {
    public static Enchantment getEnchantmentByText(int i) {
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment == null || !enchantment.getName().replace(" ", "").substring(12).equalsIgnoreCase(String.valueOf(i))) continue;
            return enchantment;
        }
        return Enchantment.enchantmentsList[i];
    }

    public static String[] getEnchantmentTexts() {
        return (String[]) Arrays.stream(Enchantment.enchantmentsList).filter(Objects::nonNull).map(x -> x.getName().replace(" ", "")).map(x -> x.substring(12)).toArray(String[]::new);
    }
}
