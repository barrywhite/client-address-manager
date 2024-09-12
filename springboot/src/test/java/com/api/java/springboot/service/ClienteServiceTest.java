package com.api.java.springboot.service;

import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.entities.Endereco;
import com.api.java.springboot.exception.ClientesNaoEncontradosException;
import com.api.java.springboot.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdicionarCliente_Sucesso() {
        Cliente cliente = new Cliente();
        Endereco endereco = new Endereco();
        endereco.setCep("12345678");
        cliente.setEndereco(endereco);

        when(enderecoService.buscarEnderecoPorCep("12345678")).thenReturn(endereco);
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente clienteSalvo = clienteService.adicionarCliente(cliente);

        assertNotNull(clienteSalvo);
        verify(enderecoService, times(1)).buscarEnderecoPorCep("12345678");
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testAdicionarCliente_SemEndereco_DeveLancarExcecao() {
        Cliente cliente = new Cliente();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.adicionarCliente(cliente);
        });

        assertEquals("É obrigatório informar o cep do endereço.", exception.getMessage());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testAtualizarCliente_Sucesso() {
        Long id = 1L;
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(id);
        clienteExistente.setNome("Nome Antigo");

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(100L);
        enderecoExistente.setCep("87654321");
        clienteExistente.setEndereco(enderecoExistente);  // Define um endereço existente

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("Nome Novo");

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setCep("12345678");
        clienteAtualizado.setEndereco(enderecoAtualizado);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(clienteExistente));
        when(enderecoService.buscarEnderecoPorCep("12345678")).thenReturn(enderecoAtualizado);
        when(enderecoService.alterarEnderecoExistente(enderecoAtualizado, enderecoExistente.getId())).thenReturn(enderecoAtualizado);
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        Cliente clienteResult = clienteService.atualizarCliente(id, clienteAtualizado);

        assertEquals("Nome Novo", clienteResult.getNome());
        verify(clienteRepository, times(1)).findById(id);
        verify(enderecoService, times(1)).buscarEnderecoPorCep("12345678");
        verify(enderecoService, times(1)).alterarEnderecoExistente(enderecoAtualizado, enderecoExistente.getId());
        verify(clienteRepository, times(1)).save(clienteExistente);
    }


    @Test
    void testAtualizarCliente_ClienteNaoEncontrado_DeveLancarExcecao() {
        Long id = 1L;
        Cliente clienteAtualizado = new Cliente();

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.atualizarCliente(id, clienteAtualizado);
        });

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testObterTodosClientes_Sucesso() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<Cliente> clientes = clienteService.obterTodosClientes();

        assertEquals(2, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testObterTodosClientes_Vazio_DeveLancarExcecao() {
        when(clienteRepository.findAll()).thenReturn(Arrays.asList());

        ClientesNaoEncontradosException exception = assertThrows(ClientesNaoEncontradosException.class, () -> {
            clienteService.obterTodosClientes();
        });

        assertEquals("Clientes não encontrados.", exception.getMessage());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testExcluirCliente_Sucesso() {
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        clienteService.excluirCliente(id);

        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void testExcluirCliente_ClienteNaoEncontrado_DeveLancarExcecao() {
        Long id = 1L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.excluirCliente(id);
        });

        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, never()).deleteById(anyLong());
    }
}
