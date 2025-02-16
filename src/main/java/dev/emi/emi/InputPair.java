package dev.emi.emi;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record InputPair(Prototype ingredient, Prototype potion) {}
