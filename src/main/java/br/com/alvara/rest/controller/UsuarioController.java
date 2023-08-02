package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.validation.Valid;


@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceImpl service;


    @PostMapping("/novo")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid UsuarioDTO dto) {
        return service.salvarDto(dto);
    }

    @GetMapping()
    public Page<Usuario> listarTodos(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size
    ) {
        return service.listarTodos(page, size);
    }

    @PutMapping("/foto/{id}")
    public byte[] adicionarFoto(@PathVariable Integer id,
                                @RequestParam("foto") Part arquivo) {
        return service.adicionarFoto(id, arquivo);
    }

    @PatchMapping("/ativar/{id}")
    public void desativarAtivarUsuario(@PathVariable Integer id) {
        service.ativarDesativarUsuario(id);
    }


    @PatchMapping("/ativardesativaradm/{id}")
    public void ativarDesativarUsuarioAdm(@PathVariable Integer id) {
        service.ativarDesativarUsuarioAdm(id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarporId(@PathVariable Integer id) {
        service.deletarUsuario(id);
    }

}
