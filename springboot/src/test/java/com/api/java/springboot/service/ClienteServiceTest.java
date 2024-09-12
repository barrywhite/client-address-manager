package com.api.java.springboot.service;

import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.exception.ClientesNaoEncontradosException;
import com.api.java.springboot.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarListaDeClientesQuandoExistemClientes() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // Act
        List<Cliente> clientes = clienteService.obterTodosClientes();

        // Assert
        assertEquals(1, clientes.size());
        assertEquals("João", clientes.get(0).getNome());
    }

    @Test
    void deveLancarClientesNaoEncontradosExceptionQuandoNaoExistemClientes() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ClientesNaoEncontradosException.class, () -> clienteService.obterTodosClientes());
    }
}
