package com.logistica.parte2.model;

import java.util.List;

/**
 * Guarda o resultado final do cálculo de roteamento.
 */
public record RotaResultado(List<String> rota, double custoTotal) {}
