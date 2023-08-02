package br.com.alvara.service.implementation;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.model.repository.UsuarioRepository;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.service.UsuarioService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {


    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login Não encontrado!"));

        if (usuario.isAtivo()) {
            return User
                    .builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(usuario.getRole())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuário desativado!");
        }
    }


    @Override
    public Usuario salvar(Usuario usuario) {
        String senhaCript = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(senhaCript);
        return repository.save(usuario);
    }

    @Override
    public Usuario salvarDto(UsuarioDTO dto) {
        String senhaCript = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(senhaCript);

        Usuario usuario = new Usuario(dto);

        return repository.save(usuario);
    }

    @Override
    public Usuario buscarPorUsuername(String username) {
        return repository.
                findByUsername(username)
                .orElse(new Usuario());
    }

    @Override
    public Usuario buscarPorCpf(String cpf) {
        return repository.
                findByCpf(cpf)
                .orElse(new Usuario());
    }

    @Override
    public Page<Usuario> listarTodos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size,
                Sort.Direction.ASC,
                "id");
        return repository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public byte[] adicionarFoto(Integer idUser, Part arquivo) {
        Optional<Usuario> usuario = repository.findById(idUser);
        return usuario.map(c -> {
            try {
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[(int) arquivo.getSize()];
                IOUtils.readFully(is, bytes);
                c.setFoto(bytes);
                repository.save(c);
                is.close();
                return bytes;
            } catch (IOException ex) {
                return null;
            }
        }).orElse(null);
    }

    @Override
    public void ativarDesativarUsuario(Integer id) {
        Optional<Usuario> usuario = repository.findById(id);
        usuario.ifPresent(c -> {
            c.setAtivo(!c.isAtivo());
            repository.save(c);
        });
    }

    @Override
    public void ativarDesativarUsuarioAdm(Integer id) {
        repository.
                findById(id)
                .map(usuarioAchado -> {
                    if (usuarioAchado.getRole().equals("ADMIN")) {
                        usuarioAchado.setRole("USER");
                    } else {
                        usuarioAchado.setRole("ADMIN");
                    }
                    return repository.save(usuarioAchado);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado para definir Role!"));

    }

    @Override
    public void deletarUsuario(Integer id) {
        repository.
                findById(id)
                .map(cliente -> {
                    repository.delete(cliente);
                    return Void.TYPE;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado para deletar!"));
    }


}
