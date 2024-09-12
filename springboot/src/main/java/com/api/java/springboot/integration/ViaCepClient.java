package com.api.java.springboot.integration;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.api.java.springboot.entities.Endereco;

@Component
public class ViaCepClient {

    private final RestTemplate restTemplate;

    public ViaCepClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Endereco buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            Endereco endereco = restTemplate.getForObject(url, Endereco.class);
            if (endereco == null || endereco.getCep() == null || endereco.getCep().isEmpty()) {
                throw new RuntimeException("Endereço não encontrado para o CEP informado.");
            }
            return endereco;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro ao consultar o CEP: " + e.getStatusCode());
        }
    }
}
