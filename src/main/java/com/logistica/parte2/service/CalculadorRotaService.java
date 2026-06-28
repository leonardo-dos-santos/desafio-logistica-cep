package com.logistica.parte2.service;

// Importações corrigidas apontando para o pacote 'parte2'
import com.logistica.parte2.model.Aresta;
import com.logistica.parte2.model.RotaResultado;

import java.util.*;

public class CalculadorRotaService {

    /**
     * Aplica o Algoritmo de Dijkstra para encontrar a rota com o menor custo total.
     */
    public RotaResultado calcularMenorCusto(Map<String, List<Aresta>> grafo, String origem, String destino) {
        Map<String, Double> custos = new HashMap<>();
        Map<String, String> antecessores = new HashMap<>();
        PriorityQueue<String> filaPrioridade = new PriorityQueue<>(Comparator.comparingDouble(custos::get));

        for (String cidade : grafo.keySet()) {
            custos.put(cidade, Double.MAX_VALUE);
        }

        if (!custos.containsKey(origem)) {
            return null;
        }

        custos.put(origem, 0.0);
        filaPrioridade.add(origem);

        while (!filaPrioridade.isEmpty()) {
            String atual = filaPrioridade.poll();

            if (atual.equals(destino)) {
                break;
            }

            List<Aresta> vizinhos = grafo.getOrDefault(atual, Collections.emptyList());
            for (Aresta aresta : vizinhos) {
                double novoCusto = custos.get(atual) + aresta.custo();

                if (novoCusto < custos.getOrDefault(aresta.destino(), Double.MAX_VALUE)) {
                    custos.put(aresta.destino(), novoCusto);
                    antecessores.put(aresta.destino(), atual);

                    filaPrioridade.remove(aresta.destino());
                    filaPrioridade.add(aresta.destino());
                }
            }
        }

        if (custos.getOrDefault(destino, Double.MAX_VALUE) == Double.MAX_VALUE) {
            return null;
        }

        LinkedList<String> caminho = new LinkedList<>();
        String passo = destino;
        while (passo != null) {
            caminho.addFirst(passo);
            passo = antecessores.get(passo);
        }

        return new RotaResultado(caminho, custos.get(destino));
    }
}
