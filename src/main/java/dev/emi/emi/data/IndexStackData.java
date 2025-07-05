package dev.emi.emi.data;

import com.github.bsideup.jabel.Desugar;
import dev.emi.emi.api.stack.EmiIngredient;

import java.util.List;
import java.util.function.Predicate;

@Desugar
public record IndexStackData(List<Added> added, List<EmiIngredient> removed) {

    @Desugar
	public static record Added(EmiIngredient added, EmiIngredient after) {
	}

    @Desugar
    public static record Filter(Predicate<String> filter) {
    }
}
