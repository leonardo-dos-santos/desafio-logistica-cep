package com.logistica.parte2.service;

import com.logistica.parte2.model.Aresta;
import com.logistica.parte2.model.RotaResultado;

import java.util.*;

public class CalculadorRotaService {

    /**
     * Aplica o Algoritmo de Dijkstra para encontrar a rota com o menor custo total.
     *
     * @param grafo   O mapa representando a lista de adjacência (Cidade -> Lista de Conexões).
     * @param origin  Nome da cidade de partida.
     * @param destino Nome da cidade de destino.
     * @return RotaResultado contendo o caminho e o custo, ou null se não houver caminho.
     */
    public RotaResultado calcularMenorCusto(Map<String, List<Aresta>> grafo, String origin, String destino) {
        // Guarda a menor distância/custo conhecida para cada cidade
        Map<String, Double> custos = new HashMap<>();
        // Guarda o rastro de quem veio de onde para reconstruir o caminho no final
        Map<String, String> antecessores = new HashMap<>();
        // Fila de prioridade para processar sempre a cidade com o menor custo acumulado atual
        PriorityQueue<String> filaPrioridade = new PriorityQueue<>(Comparator.comparingDouble(custos::get));

        // Inicializa todas as cidades conhecidas no grafo com custo "infinito"
        for (String cidade : grafo.keySet()) {
            custos.put(cidade, Double.MAX_VALUE);
        }

        // Se a cidade de origem não estiver no grafo, não há o que calcular
        if (!custos.containsKey(origin)) {
            return null;
        }

        // Configura o ponto de partida
        custos.put(origin, 0.0);
        filaPrioridade.add(origin);

        while (!filaPrioridade.isEmpty()) {
            String atual = filaPrioridade.poll();

            // Se chegamos ao destino, podemos parar a busca
            if (atual.equals(destino)) {
                break;
            }

            // Explora os vizinhos da cidade atual
            List<Aresta> vizinhos = grafo.getOrDefault(atual, Collections.emptyList());
            for (Aresta aresta : vizinhos) {
                double novoCusto = custos.get(atual) + aresta.custo();

                // Se encontramos um caminho mais barato para o vizinho, atualizamos
                if (novoCusto < custos.getOrDefault(aresta.destino(), Double.MAX_VALUE)) {
                    custos.put(aresta.destino(), novoCusto);
                    antecessores.put(aresta.destino(), atual);

                    // Força a fila a se reordenar com o novo custo baixo
                    filaPrioridade.remove(aresta.destino());
                    filaPrioridade.add(aresta.destino());
                }
            }
        }

        // Se o custo do destino continuou infinito, a rota é impossível
        if (custos.getOrDefault(destino, Double.MAX_VALUE) == Double.MAX_VALUE) {
            return null;
        }

        // Reconstrói o caminho de trás para frente (do destino até a origem)
        LinkedList<String> caminho = new LinkedList<>();
        String passo = destino;
        while (passo != null) {
            caminho.addFirst(passo);
            passo = antecessores.get(passo);
        }

        return new RotaResultado(caminho, custos.get(destino));
    }
}
