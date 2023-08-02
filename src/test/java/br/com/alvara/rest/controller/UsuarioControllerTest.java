package br.com.alvara.rest.controller;


import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test") // Usar o perfil de testes para escapar da autenticação
class UsuarioControllerTest {


    @Mock
    private UsuarioServiceImpl usuarioService;

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioControllerTest.class);


    @BeforeEach
    void setUp() {
    }

    @Test
    void salvar() {

//        Usuario usuario = new Usuario();
//        usuario.setUsername("user");
//        usuario.setPassword("asdasd");
//        usuario.setRole("ADMIN");
//        usuario.setAtivo(true);
//        usuario.setCelular("6232856908");
//
//        Mockito.when(usuarioService.salvarDto(any())).thenReturn(usuario);
//
//        var resposta = given()
//                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
//                .body(new UsuarioDTO())
//                .when()
//                .post("api/usuarios/novo")
//                .then()
//                .extract()
//                .response();
//
//        String responseBody = resposta.getBody().asString();
//        LOG.info(responseBody);

    }

    @Test
    void listarTodos() {
    }

    @Test
    void adicionarFoto() {
    }

    @Test
    void desativarAtivarUsuario() {
    }

    @Test
    void ativarDesativarUsuarioAdm() {
    }

    @Test
    void deletarporId() {
    }
}