package br.com.alvara.service.implementation;

import br.com.alvara.model.entity.Usuario;
import br.com.alvara.model.repository.UsuarioRepository;
import br.com.alvara.rest.dto.UsuarioDTO;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioServiceImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioServiceImplTest.class);

    @InjectMocks // para testar os métodos da classe mock
    private UsuarioServiceImpl usuarioService;

    @Mock// Repositorio Mock para não alterar nada no Banco de fato
    //O service chamará ele mock de mentira
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        // Antes de tudo ele executa o que está aqui dentro
    }

    @Test
    @DisplayName("Deve obter usuário pelo username")
    @Order(2)
    void loadUserByUsername() {

        String username = "username2";
        Usuario novo = new Usuario();
        novo.setUsername(username);
        novo.setPassword(username);
        novo.setAtivo(true);
        novo.setRole("ADMIN");

        Optional<Usuario> optionUser = Optional.of(novo);

        //Mock no retorno do repository
        Mockito.when(usuarioRepository.findByUsername(any())).thenReturn(optionUser);

        UserDetails ret =  usuarioService.loadUserByUsername(username);

        LOG.info(ret.toString());

        assertNotNull(ret);
        assertEquals(username, ret.getUsername());

    }

    @Test
    @DisplayName("Deve Salvar um Usuário")
    @Order(2)
    void salvar() {

        String cpf = "04955806790";
        String pass = "pass2";
        String nome = "nome2";
        String username = "username2";

        Usuario seraSalvo = new Usuario();
        seraSalvo.setCpf(cpf);
        seraSalvo.setPassword(pass);
        seraSalvo.setNome(nome);
        seraSalvo.setUsername(username);
        seraSalvo.setAtivo(true);

        //Mock no retorno do repository
        Mockito.when(usuarioRepository.save(any())).thenReturn(seraSalvo);

        Usuario dtoSalvo = usuarioService.salvar(new Usuario());

        LOG.info(dtoSalvo.toString());

        assertNotNull(dtoSalvo);
        assertEquals(cpf, dtoSalvo.getCpf());
        assertEquals(pass, dtoSalvo.getPassword());
        assertEquals(nome, dtoSalvo.getNome());
        assertEquals(username, dtoSalvo.getUsername());
    }

    @Test
    @DisplayName("Deve Salvar um Usuário Vindo de DTO")
    @Order(1)
    void salvarDto() {

        String cpf = "76151774051";
        String pass = "pass1";
        String nome = "nome1";
        String username = "username1";

        Usuario seraSalvo = new Usuario();
        seraSalvo.setCpf(cpf);
        seraSalvo.setPassword(pass);
        seraSalvo.setNome(nome);
        seraSalvo.setUsername(username);

        //Mock no retorno do repository
        Mockito.when(usuarioRepository.save(any())).thenReturn(seraSalvo);

        Usuario dtoSalvo = usuarioService.salvarDto(new UsuarioDTO());

        LOG.info(dtoSalvo.toString());

        assertNotNull(dtoSalvo);
        assertEquals(cpf, dtoSalvo.getCpf());
        assertEquals(pass, dtoSalvo.getPassword());
        assertEquals(nome, dtoSalvo.getNome());
        assertEquals(username, dtoSalvo.getUsername());

    }

}