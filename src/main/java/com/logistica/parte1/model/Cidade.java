package com.logistica.parte1.model;

/**
 * Representa uma cidade do sistema logístico e seu intervalo inclusivo de CEPs.
 */
public record Cidade(String nome, int cepInicial, int cepFinal) {

    /**
     * Valida se um determinado CEP está compreendido na faixa de atuação desta cidade.
     *
     * @param cep CEP em formato numérico inteiro.
     * @return true se o CEP estiver contido na faixa (inclusivo), false caso contrário.
     */
    public boolean contemCep(int cep) {
        return cep >= cepInicial && cep <= cepFinal;
    }
}
