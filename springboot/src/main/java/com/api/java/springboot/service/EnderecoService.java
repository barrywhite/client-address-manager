package com.api.java.springboot.service;

import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.entities.Endereco;
import com.api.java.springboot.exception.EnderecoInvalidoException;
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
    public Endereco validarEndereco(Endereco endereco) {
        if (endereco == null || endereco.getCep() == null || endereco.getCep().isEmpty()) {
            throw new EnderecoInvalidoException("Endereço ou CEP deve ser informado.");
        }
        return buscarEnderecoPorCep(endereco.getCep());
    }

    public Endereco buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.isEmpty()) {
            throw new EnderecoInvalidoException("CEP é obrigatório.");
        }
        Endereco endereco = viaCepClient.buscarEnderecoPorCep(cep);
        if (endereco == null) {
            throw new EnderecoInvalidoException("Endereço não encontrado para o CEP informado.");
        }
        return endereco;
    }

    public Endereco alterarEnderecoExistente(Endereco novoEndereco, Long idEndereco) {
        Endereco enderecoExistente = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new EnderecoInvalidoException("Endereço não encontrado."));
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
}
