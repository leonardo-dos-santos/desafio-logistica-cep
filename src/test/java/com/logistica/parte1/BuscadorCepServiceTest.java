package com.logistica.parte1;

import com.logistica.parte1.service.BuscadorCepService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuscadorCepServiceTest {

    // O JUnit 5 injeta uma pasta temporária segura para criar arquivos de teste
    @TempDir
    Path pastaTemporaria;

    @Test
    @DisplayName("Deve ler o arquivo de exemplo e encontrar as cidades M e N para o CEP 60000330")
    void deveProcessarExemploComSucesso() throws IOException {
        // 1. Cria um arquivo de texto temporário simulando a sua entrada de dados exata
        Path arquivoTeste = pastaTemporaria.resolve("entrada_exemplo.txt");
        List<String> linhasEntrada = List.of(
                "E,20000000,20001000",
                "H,30000125,30000375",
                "T,90000500,90001500",
                "R,80000125,80000375",
                "M,60000000,60000500", // CEP 60000330 está aqui
                "N,60000125,60000375", // CEP 60000330 está aqui também
                "L,50000125,50000375",
                "--",
                "60000330"
        );
        Files.write(arquivoTeste, linhasEntrada);

        // 2. Executa o serviço apontando para o arquivo temporário
        BuscadorCepService service = new BuscadorCepService();
        BuscadorCepService.ResultadoProcessamento resultado = service.processarArquivo(arquivoTeste.toString());

        // 3. Validações
        assertEquals(60000330, resultado.cepBuscado());
        assertEquals(2, resultado.cidadesPertencentes().size());
        assertTrue(resultado.cidadesPertencentes().contains("M"));
        assertTrue(resultado.cidadesPertencentes().contains("N"));
    }
}
