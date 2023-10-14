package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.rest.dto.UsuarioResponseDTO;
import br.com.alvara.rest.mapper.UsuarioMapper;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    private static final String CAMPO_ID_OBRIGATORIO = "O campo ID é obrigatório!";
    private static final String SERVER_ERROR = "Internal Server Error";
    private static final String MSG_USER_NOTFOUND = "Usuário não localizado.";


    @Autowired
    private UsuarioServiceImpl service;

    @Autowired
    private UsuarioMapper usuarioMapper;


    @PostMapping("/novo")
    @Operation(summary = "Criar Usuário", description = "Este endpoint Cria um Usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado!"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<UsuarioResponseDTO> salvar(
            @RequestBody @Valid UsuarioDTO dto
    ) {
        Usuario novo = service.salvarDto(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novo.getId())
                .toUri();
        return ResponseEntity.created(location).body(usuarioMapper.usuarioToUsuarioResponseDTO(novo));
    }

    @GetMapping
    @Operation(summary = "Lsitar usuários", description = "Este endpoint Lista os usuários.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem concluída"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<Usuario>> listarTodos(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        Page<Usuario> pageUsuario = service.listarTodos(page, size);
        return ResponseEntity.ok().body(pageUsuario);
    }


    @PutMapping("/{id}/adicionar-foto")
    @Operation(summary = "Adicionar Foto", description = "Adicionar foto para o Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Foto adicionada."),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = MSG_USER_NOTFOUND)
    })
    public ResponseEntity<byte[]> adicionarFoto(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id,
            @RequestParam("foto") Part arquivo
    ) {
        byte[] byteFoto = service.adicionarFoto(id, arquivo);
        return ResponseEntity.ok().body(byteFoto);
    }

    @PatchMapping("/ativar/{id}")
    @Operation(summary = "Ativar/Desativar Usuário", description = "Aqui você desativará ou ativará o usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concluído"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = MSG_USER_NOTFOUND)
    })
    public ResponseEntity<Void> desativarAtivarUsuario(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.ativarDesativarUsuario(id);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/ativardesativaradm/{id}")
    @Operation(summary = "Ativar/Desativar Usuário ADM", description = "Aqui você desativará ou ativará o usuário ADM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Concluído."),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = MSG_USER_NOTFOUND)
    })
    public ResponseEntity<Void> ativarDesativarUsuarioAdm(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.ativarDesativarUsuarioAdm(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "delete", description = "Deletar o usuário pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso."),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = MSG_USER_NOTFOUND)
    })
    public ResponseEntity<Void> deletarporId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
