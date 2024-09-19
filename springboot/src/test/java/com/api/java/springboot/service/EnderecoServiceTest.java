package com.api.java.springboot.service;

import com.api.java.springboot.entities.Endereco;
import com.api.java.springboot.exception.EnderecoInvalidoException;
import com.api.java.springboot.integration.ViaCepClient;
import com.api.java.springboot.repositories.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarEnderecoPorCep_Sucesso() {
        // Arrange
        String cep = "12345678";
        Endereco enderecoMock = new Endereco();
        enderecoMock.setCep(cep);
        when(viaCepClient.buscarEnderecoPorCep(cep)).thenReturn(enderecoMock);

        Endereco endereco = enderecoService.buscarEnderecoPorCep(cep);

        assertNotNull(endereco);
        assertEquals(cep, endereco.getCep());
        verify(viaCepClient, times(1)).buscarEnderecoPorCep(cep);
    }

    @Test
    void testBuscarEnderecoPorCep_EnderecoNaoEncontrado() {
        String cep = "12345678";
        when(viaCepClient.buscarEnderecoPorCep(cep)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> enderecoService.buscarEnderecoPorCep(cep));

        assertEquals("Endereço não encontrado para o CEP informado.", exception.getMessage());
        verify(viaCepClient, times(1)).buscarEnderecoPorCep(cep);
    }

    @Test
    void testBuscarEnderecoPorCep_CepInvalido() {
        String cep = "";

        EnderecoInvalidoException exception = assertThrows(EnderecoInvalidoException.class, () -> enderecoService.buscarEnderecoPorCep(cep));

        assertEquals("CEP é obrigatório.", exception.getMessage());
        verify(viaCepClient, never()).buscarEnderecoPorCep(anyString());
    }


    @Test
    void testAlterarEnderecoExistente_Sucesso() {
        Long idEndereco = 1L;
        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(idEndereco);
        enderecoExistente.setCep("12345678");

        Endereco novoEndereco = new Endereco();
        novoEndereco.setCep("87654321");

        when(enderecoRepository.findById(idEndereco)).thenReturn(java.util.Optional.of(enderecoExistente));

        Endereco enderecoAtualizado = enderecoService.alterarEnderecoExistente(novoEndereco, idEndereco);

        assertNotNull(enderecoAtualizado);
        assertEquals(novoEndereco.getCep(), enderecoAtualizado.getCep());
        verify(enderecoRepository, times(1)).findById(idEndereco);
    }

    @Test
    void testAlterarEnderecoExistente_EnderecoNaoEncontrado() {
        Long idEndereco = 1L;
        Endereco novoEndereco = new Endereco();
        novoEndereco.setCep("87654321");

        when(enderecoRepository.findById(idEndereco)).thenReturn(java.util.Optional.empty());

        EnderecoInvalidoException exception = assertThrows(EnderecoInvalidoException.class, () -> enderecoService.alterarEnderecoExistente(novoEndereco, idEndereco));

        assertEquals("Endereço não encontrado.", exception.getMessage());
        verify(enderecoRepository, times(1)).findById(idEndereco);
    }
}
