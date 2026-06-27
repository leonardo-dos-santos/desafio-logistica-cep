package com.logistica.parte1.service;

import com.logistica.parte1.model.Cidade;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuscadorCepService {

    public record ResultadoProcessamento(List<String> cidadesPertencentes, int cepBuscado) {}

    /**
     * Lê o arquivo de entrada seguindo o padrão especificado no desafio da Parte 1,
     * monta o catálogo de cidades e descobre a qual cidade o CEP final pertence.
     *
     * @param caminhoArquivo Caminho completo ou relativo do arquivo de entrada.
     * @return Um record contendo a lista de cidades encontradas e o CEP que foi buscado.
     * @throws IOException Caso ocorra algum erro na leitura do arquivo.
     */
    public ResultadoProcessamento processarArquivo(String caminhoArquivo) throws IOException {
        List<Cidade> catalogoCidades = new ArrayList<>();
        int cepBusca = 0;
        boolean lendoCidades = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty()) {
                    continue;
                }

                // Identifica a linha divisora
                if (linha.equals("--")) {
                    lendoCidades = false;
                    continue;
                }

                if (lendoCidades) {
                    // Processa a linha da cidade: "Nome, CEPInicial, CEPFinal"
                    String[] partes = linha.split(",");
                    if (partes.length == 3) {
                        String nome = partes[0].trim();
                        int inicial = Integer.parseInt(partes[1].trim());
                        int ateCep = Integer.parseInt(partes[2].trim());
                        catalogoCidades.add(new Cidade(nome, inicial, ateCep));
                    }
                } else {
                    // Processa o CEP alvo (última linha após o --)
                    cepBusca = Integer.parseInt(linha);
                }
            }
        }

        // Filtra todas as cidades do catálogo que contém o CEP buscado
        final int cepAlvo = cepBusca;
        List<String> cidadesEncontradas = catalogoCidades.stream()
                .filter(cidade -> cidade.contemCep(cepAlvo))
                .map(Cidade::nome)
                .toList();

        return new ResultadoProcessamento(cidadesEncontradas, cepAlvo);
    }
}
