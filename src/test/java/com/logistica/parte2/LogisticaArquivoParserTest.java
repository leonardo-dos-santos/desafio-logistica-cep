package com.logistica.parte2;

// Importa os modelos e o parser da Parte 2
import com.logistica.parte2.model.RotaResultado;
import com.logistica.parte2.service.LogisticaArquivoParser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LogisticaArquivoParserTest {

    // Injeta uma pasta temporária isolada para o teste ler o arquivo sem sujar o seu PC
    @TempDir
    Path pastaTemporaria;

    @Test
    @DisplayName("Deve ler a entrada completa da Parte 2, mapear os CEPs e calcular a rota de menor custo")
    void deveCalcularMelhorRotaComExemploReal() throws IOException {
        Path arquivoTeste = pastaTemporaria.resolve("entrada_completa_parte2.txt");

        // Esta é a sua lista exata fornecida no enunciado do desafio
        List<String> conteudoArquivo = List.of(
                "K,50000000,50000500",
                "A,00000000,00000500",
                "L,50000125,50000375",
                "Q,80000000,80000500",
                "B,00000125,00000375",
                "E,20000000,20000500",
                "M,60000000,60000500",
                "D,10000375,10001125", // O CEP de origem (10001086) cai aqui -> Cidade D
                "H,30000125,30000375",
                "O,70000000,70001000",
                "I,40000000,40001000",
                "N,60000125,60000375",
                "S,90000000,90001500",
                "F,20000125,20000375",
                "P,70000250,70000750",
                "G,30000000,30000500",
                "C,10000000,10001500",
                "J,40000250,40000750",
                "T,90000375,90001125",
                "R,80000125,80000375", // O CEP de destino (80000245) cai aqui -> Cidade R
                "--",
                "O,D,44.27",
                "I,T,27.68",
                "S,C,26.34",
                "S,E,17.41",
                "B,O,45.89",
                "S,I,39.53",
                "E,S,28.40",
                "E,N,28.10",
                "M,I,45.30",
                "S,G,40.30",
                "B,I,49.07",
                "O,P,21.38",
                "J,S,22.40",
                "I,N,48.42",
                "K,N,40.63",
                "S,O,38.53",
                "G,I,41.27",
                "A,H,44.85",
                "K,F,41.38",
                "T,M,29.51",
                "L,Q,43.44",
                "B,R,30.36",
                "I,E,46.10",
                "H,T,27.77",
                "I,P,16.24",
                "R,L,45.11",
                "T,J,48.42",
                "G,J,43.37",
                "D,M,15.38",
                "O,R,30.39",
                "M,B,29.43",
                "D,G,30.46",
                "A,C,38.98",
                "K,B,48.46",
                "F,N,15.53",
                "Q,J,37.77",
                "B,H,40.48",
                "O,G,48.95",
                "O,I,26.53",
                "R,K,24.64",
                "J,K,21.34",
                "T,J,46.43",
                "D,C,42.10",
                "L,N,46.63",
                "S,N,46.14",
                "K,N,19.13",
                "G,I,44.50",
                "N,T,26.27",
                "G,N,23.63",
                "S,O,15.70",
                "--",
                "10001086,80000245" // CEPs finais para descobrir a rota entre D e R
        );

        Files.write(arquivoTeste, conteudoArquivo);

        // Instancia o nosso processador e executa o cálculo completo
        LogisticaArquivoParser parser = new LogisticaArquivoParser();
        RotaResultado resultado = parser.processarECalcularRota(arquivoTeste.toString());

        // Validações com asserções profissionais
        assertNotNull(resultado, "O resultado não deveria ser nulo para uma rota válida.");
        assertFalse(resultado.rota().isEmpty(), "A lista da rota percorrida não pode vir vazia.");

        // O algoritmo de Dijkstra deve garantir que o ponto inicial seja D e o ponto final seja R
        assertEquals("D", resultado.rota().get(0), "A rota precisa começar mapeada na Cidade D.");
        assertEquals("R", resultado.rota().get(resultado.rota().size() - 1), "A rota precisa terminar mapeada na Cidade R.");

        // Printa no console da IDE para você visualizar a saída exata calculada por debaixo dos panos
        System.out.println("\n========================================");
        System.out.println("Caminho mais barato calculado: " + String.join(" -> ", resultado.rota()));
        System.out.printf("Custo total do frete logístico: R$ %.2f\n", resultado.custoTotal());
        System.out.println("========================================\n");
    }
}
