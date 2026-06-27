package com.logistica.parte1;

import com.logistica.parte1.model.Cidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CidadeTest {

    @Test
    @DisplayName("Deve validar com sucesso quando o CEP fornecido estiver no meio da faixa limite")
    void deveValidarCepDentroDaFaixa() {
        Cidade cidade = new Cidade("Cidade Teste", 50000000, 50001000);
        assertTrue(cidade.contemCep(50000500));
    }

    @Test
    @DisplayName("Deve retornar falso se o CEP estiver fora das extremidades da faixa informada")
    void deveRejeitarCepForaDaFaixa() {
        Cidade cidade = new Cidade("Cidade Fora", 50000000, 50001000);
        assertFalse(cidade.contemCep(49999999));
        assertFalse(cidade.contemCep(50001001));
    }
}
