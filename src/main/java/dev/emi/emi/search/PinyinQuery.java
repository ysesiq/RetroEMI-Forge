package dev.emi.emi.search;

import com.google.common.collect.Sets;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.text.Text;

import java.util.Set;

public class PinyinQuery extends Query {
    private final String name;
    private final Set<EmiStack> valid;

    public PinyinQuery(String name) {
        this.name = name.toLowerCase();
        this.valid = Sets.newHashSet(EmiSearch.names.findAll(name.toLowerCase()));
    }

    @Override
    public boolean matches(EmiStack stack) {
        boolean contains = false;
//        if (Loader.isModLoaded("pinin")) {
//            try {
//                contains = me.towdium.pinin.PinyinMatch.contains(PinyinQuery.getText(stack).getString(), this.name);
//            }
//            catch (Exception ignored) {
//            }
//        } else {
            contains = valid.contains(stack);
//        }
        return contains;
    }

//    @Override
//    public boolean matchesUnbaked(EmiStack stack) {
//        return PinyinMatch.toPinyin(getText(stack).getString()).contains(name);
//    }

    public static Text getText(EmiStack stack) {
        return stack.getName();
    }
}
