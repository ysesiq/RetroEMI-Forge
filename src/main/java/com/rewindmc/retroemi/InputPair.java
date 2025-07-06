package com.rewindmc.retroemi;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record InputPair(Prototype ingredient, Prototype potion) {}
