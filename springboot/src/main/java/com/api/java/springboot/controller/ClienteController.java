package com.api.java.springboot.controller;

import com.api.java.springboot.entities.Cliente;
import com.api.java.springboot.exception.ClientesNaoEncontradosException;
import com.api.java.springboot.repositories.ClienteRepository;
import com.api.java.springboot.service.ClienteService;
import com.api.java.springboot.dto.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteService clienteService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.clienteRepository = clienteRepository;
    }

    @PostMapping
    public ResponseEntity<?> adicionarCliente(@RequestBody @Valid Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.adicionarCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body("Cliente cadastrado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obterCliente(@PathVariable Long id) {
        try {
            Cliente cliente = clienteService.obterCliente(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obterTodosClientes() {
        try {
            List<Cliente> clientes = clienteService.obterTodosClientes();
            return ResponseEntity.ok(clientes);
        } catch (ClientesNaoEncontradosException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Ocorreu um erro inesperado."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody @Valid Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarCliente(id, cliente);
            return ResponseEntity.ok("Cliente atualizado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        try {
            clienteService.excluirCliente(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
