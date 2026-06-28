package com.logistica.parte2.service;

// Importa o modelo territorial da Parte 1
import com.logistica.parte1.model.Cidade;

// Importa as estruturas de roteamento da Parte 2
import com.logistica.parte2.model.Aresta;
import com.logistica.parte2.model.RotaResultado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LogisticaArquivoParser {

    private final CalculadorRotaService calculadorRotaService = new CalculadorRotaService();

    public record RequisicaoBuscaCep(int cepOrigem, int cepDestino) {}

    public RotaResultado processarECalcularRota(String caminhoArquivo) throws IOException {
        List<Cidade> cidades = new ArrayList<>();
        Map<String, List<Aresta>> grafo = new HashMap<>();
        RequisicaoBuscaCep buscaCeps = null;

        int secaoAtual = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty()) {
                    continue;
                }

                if (linha.equals("--")) {
                    secaoAtual++;
                    continue;
                }

                switch (secaoAtual) {
                    case 0 -> {
                        String[] partes = linha.split(",");
                        if (partes.length == 3) {
                            cidades.add(new Cidade(
                                    partes[0].trim(),
                                    Integer.parseInt(partes[1].trim()),
                                    Integer.parseInt(partes[2].trim())
                            ));
                        }
                    }
                    case 1 -> {
                        String[] partes = linha.split(",");
                        if (partes.length == 3) {
                            String origem = partes[0].trim();
                            String destino = partes[1].trim();
                            double custo = Double.parseDouble(partes[2].trim());

                            grafo.computeIfAbsent(origem, k -> new ArrayList<>()).add(new Aresta(destino, custo));
                            grafo.computeIfAbsent(destino, k -> new ArrayList<>());
                        }
                    }
                    case 2 -> {
                        String[] partes = linha.split(",");
                        if (partes.length == 2) {
                            buscaCeps = new RequisicaoBuscaCep(
                                    Integer.parseInt(partes[0].trim()),
                                    Integer.parseInt(partes[1].trim())
                            );
                        }
                    }
                }
            }
        }

        if (buscaCeps == null) {
            throw new IllegalArgumentException("Arquivo mal formatado: CEPs de busca não encontrados.");
        }

        // 1. Descobre TODAS as cidades possíveis para a origem e para o destino (Tratando sobreposição)
        List<String> cidadesOrigemPossiveis = descobrirCidadesPorCep(cidades, buscaCeps.cepOrigem());
        List<String> cidadesDestinoPossiveis = descobrirCidadesPorCep(cidades, buscaCeps.cepDestino());

        if (cidadesOrigemPossiveis.isEmpty() || cidadesDestinoPossiveis.isEmpty()) {
            return null;
        }

        // 2. Testa todas as combinações e escolhe a que tiver o menor custo total real
        RotaResultado melhorResultadoGeral = null;

        for (String origem : cidadesOrigemPossiveis) {
            for (String destino : cidadesDestinoPossiveis) {
                RotaResultado resultadoAtual = calculadorRotaService.calcularMenorCusto(grafo, origem, destino);

                if (resultadoAtual != null) {
                    if (melhorResultadoGeral == null || resultadoAtual.custoTotal() < melhorResultadoGeral.custoTotal()) {
                        melhorResultadoGeral = resultadoAtual;
                    }
                }
            }
        }

        return melhorResultadoGeral;
    }

    // Altera a função auxiliar para retornar uma lista de cidades compatíveis com o CEP
    private List<String> descobrirCidadesPorCep(List<Cidade> cidades, int cep) {
        return cidades.stream()
                .filter(c -> c.contemCep(cep))
                .map(Cidade::nome)
                .toList();
    }
}
