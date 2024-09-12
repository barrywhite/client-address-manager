package com.api.java.springboot.controller;

import com.api.java.springboot.dto.ErrorResponse;
import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.exception.ClientesNaoEncontradosException;
import com.api.java.springboot.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarOkQuandoClientesForemEncontrados() {
        Cliente cliente = new Cliente();
        cliente.setNome("João");
        when(clienteService.obterTodosClientes()).thenReturn(List.of(cliente));

        ResponseEntity<?> response = clienteController.obterTodosClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Cliente> clientes = (List<Cliente>) response.getBody();
        assertEquals(1, clientes.size());
        assertEquals("João", clientes.get(0).getNome());
    }

    @Test
    void deveRetornarNotFoundQuandoClientesNaoForemEncontrados() {
        when(clienteService.obterTodosClientes()).thenThrow(new ClientesNaoEncontradosException("Nenhum cliente encontrado."));

        ResponseEntity<?> response = clienteController.obterTodosClientes();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Nenhum cliente encontrado.", ((ErrorResponse) response.getBody()).getMensagem());
    }
}
