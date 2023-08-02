package br.com.alvara.service;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.rest.dto.UsuarioDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.Part;


public interface UsuarioService {
    Usuario salvar(Usuario usuario);

    Usuario salvarDto(UsuarioDTO dto);

    Usuario buscarPorUsuername(String username);

    Usuario buscarPorCpf(String cpf);

    Page<Usuario> listarTodos(int page, int size);

    public byte[] adicionarFoto(Integer idUser, Part arquivo);

    void ativarDesativarUsuario(Integer id);

    void ativarDesativarUsuarioAdm(Integer id);

    void deletarUsuario(Integer id);
}
