package com.api.java.springboot.service;

import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.entities.Endereco;
import com.api.java.springboot.exception.ClientesNaoEncontradosException;
import com.api.java.springboot.repositories.ClienteRepository;
import com.api.java.springboot.repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final EnderecoService enderecoService;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, EnderecoService enderecoService) {
        this.clienteRepository = clienteRepository;
        this.enderecoService = enderecoService;
    }

    public Cliente adicionarCliente(Cliente cliente) {
        validarEnderecoCliente(cliente);
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
        clienteExistente.setNome(clienteAtualizado.getNome());
        if (clienteAtualizado.getEndereco() != null && clienteAtualizado.getEndereco().getCep() != null) {
            Endereco novoEndereco = enderecoService.buscarEnderecoPorCep(clienteAtualizado.getEndereco().getCep());
            Endereco EnderecoAlterado =
                    enderecoService.alterarEnderecoExistente(
                            novoEndereco,
                            clienteExistente.getEndereco().getId()
                    );
            clienteExistente.setEndereco(EnderecoAlterado);
        }

        return clienteRepository.save(clienteExistente);
    }

    public List<Cliente> obterTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        if (clientes.isEmpty()) {
            throw new ClientesNaoEncontradosException("Clientes não encontrados.");
        }
        return clientes;
    }

    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    public void excluirCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    private void validarEnderecoCliente(Cliente cliente) {
        if (cliente.getEndereco() == null) {
            throw new IllegalArgumentException("É obrigatório informar o cep do endereço.");
        }
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoService.buscarEnderecoPorCep(cep);
        cliente.setEndereco(endereco);
    }
}
