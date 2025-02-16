package dev.emi.emi.data;

import com.github.bsideup.jabel.Desugar;
import dev.emi.emi.api.stack.EmiIngredient;

import java.util.List;

@Desugar
public record EmiAlias(List<EmiIngredient> stacks, List<String> keys) {
}
