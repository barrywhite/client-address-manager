package com.api.java.springboot.service;

import com.api.java.springboot.entities.Endereco;
import com.api.java.springboot.integration.ViaCepClient;
import com.api.java.springboot.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {

    private final ViaCepClient viaCepClient;
    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(ViaCepClient viaCepClient, EnderecoRepository enderecoRepository) {
        this.viaCepClient = viaCepClient;
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.isEmpty()) {
            throw new IllegalArgumentException("CEP é obrigatório.");
        }
        Endereco endereco = viaCepClient.buscarEnderecoPorCep(cep);
        return endereco;
    }

    public Endereco alterarEnderecoExistente(Endereco novoEndereco, Long idEndereco) {
        Endereco enderecoExistente = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado."));
        enderecoExistente.setCep(novoEndereco.getCep());
        enderecoExistente.setLogradouro(novoEndereco.getLogradouro());
        enderecoExistente.setComplemento(novoEndereco.getComplemento());
        enderecoExistente.setUnidade(novoEndereco.getUnidade());
        enderecoExistente.setBairro(novoEndereco.getBairro());
        enderecoExistente.setLocalidade(novoEndereco.getLocalidade());
        enderecoExistente.setUf(novoEndereco.getUf());
        enderecoExistente.setEstado(novoEndereco.getEstado());
        enderecoExistente.setRegiao(novoEndereco.getRegiao());
        enderecoExistente.setIbge(novoEndereco.getIbge());
        enderecoExistente.setGia(novoEndereco.getGia());
        enderecoExistente.setDdd(novoEndereco.getDdd());
        enderecoExistente.setSiafi(novoEndereco.getSiafi());
        return enderecoExistente;
    }

    public Endereco verificarNovoCep(String novoCep) {
        if (novoCep != null && !novoCep.isEmpty()) {
            return buscarEnderecoPorCep(novoCep);
        }
        return null;
    }
}
