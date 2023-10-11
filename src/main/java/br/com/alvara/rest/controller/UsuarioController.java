package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Part;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;


@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private static final String CAMPO_ID_OBRIGATORIO = "O campo id é obrigatório!";

    @Autowired
    private UsuarioServiceImpl service;


    @PostMapping("/novo")
    public ResponseEntity<Usuario> salvar(
            @RequestBody @Valid UsuarioDTO dto
    ) {
        Usuario novo = service.salvarDto(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novo.getId())
                .toUri();
        return ResponseEntity.created(location).body(novo);
    }

    @GetMapping()
    public ResponseEntity<Page<Usuario>> listarTodos(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Page<Usuario> pageUsuario = service.listarTodos(page, size);
        return ResponseEntity.ok().body(pageUsuario);
    }

    @PutMapping("/foto/{id}")
    public ResponseEntity<byte[]> adicionarFoto(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id,
            @RequestParam("foto") Part arquivo
    ) {
        byte[] byteFoto = service.adicionarFoto(id, arquivo);
        return ResponseEntity.ok().body(byteFoto);
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<Void> desativarAtivarUsuario(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.ativarDesativarUsuario(id);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/ativardesativaradm/{id}")
    public ResponseEntity<Void> ativarDesativarUsuarioAdm(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.ativarDesativarUsuarioAdm(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarporId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
