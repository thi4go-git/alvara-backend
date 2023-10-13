package br.com.alvara.rest.mapper;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.rest.dto.UsuarioResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioResponseDTO usuarioToUsuarioResponseDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    public Usuario UsuarioDTOtoUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
        usuario.setRole("USER");
        usuario.setAtivo(false);
        return usuario;
    }
}
