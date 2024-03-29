package br.com.alvara.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.com.alvara.rest.dto.UsuarioDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioControllerTest.class);
    private static final String URL_PATH = "/api/usuarios";
    private static final String ID_USUARIO = "1";
    private static final String ID_USUARIO_INEXISTENTE = "4";
    private static final String NOME_FOTO_MOCK = "foto";
    private static final String MSG_USER_NOTFOUND = "Usuário não encontrado com o ID informado!";


    @ParameterizedTest
    @MethodSource("usuariosParaTeste")
    @DisplayName("Testar Cadastro de usuários")
    @Order(1)
    void testarCadastroDeUsuarios(UsuarioDTO dto, int indexUsuario) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(URL_PATH.concat("/novo"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);

        if (indexUsuario == 1) {
            assertEquals(HttpStatus.SC_CREATED, resposta.statusCode());
            assertEquals(ID_USUARIO, resposta.jsonPath().getString("id"));
            assertEquals(dto.getUsername(), resposta.jsonPath().getString("username"));
            assertEquals(dto.getPassword(), resposta.jsonPath().getString("password"));
            assertEquals("USER", resposta.jsonPath().getString("role"));
            assertEquals("false", resposta.jsonPath().getString("ativo"));
            assertEquals(dto.getNome(), resposta.jsonPath().getString("nome"));
            assertEquals(dto.getCpf(), resposta.jsonPath().getString("cpf"));
            assertEquals(dto.getEmail(), resposta.jsonPath().getString("email"));
            assertEquals(dto.getCelular(), resposta.jsonPath().getString("celular"));
        } else if (indexUsuario == 2) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
            assertTrue(responseBody.contains("Já existe um Usuário cadastrado com o Username informado"));
            assertTrue(responseBody.contains("Já existe um Usuário cadastrado com o CPF informado"));
        } else if (indexUsuario == 3) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
            assertTrue(responseBody.contains("O campo nome é Obrigatório!"));
            assertTrue(responseBody.contains("O campo username é Obrigatório!"));
            assertTrue(responseBody.contains("O campo password é Obrigatório!"));
            assertTrue(responseBody.contains("O campo cpf é Obrigatório!"));
        } else {
            assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
            assertTrue(responseBody.contains("O campo cpf é invalido!"));
        }
    }

    @Test
    @DisplayName("Deve listar todos usuários sem Parâmetros")
    @Order(2)
    void deveListarTodosUsuariosSemParametros() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH)
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(responseBody.contains("\"empty\":false"));
        assertTrue(responseBody.contains("\"pageSize\":10"));
        assertTrue(responseBody.contains("\"pageNumber\":0"));
        assertTrue(responseBody.contains("\"totalElements\":1"));
        assertTrue(responseBody.contains("\"numberOfElements\":1"));
    }

    @Test
    @DisplayName("Não Deve listar usuários com parâmetros incorretos")
    @Order(3)
    void naoDeveListarUsuariosComParametrosIncoretos() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .param("page", 1)
                .param("size", 20)
                .when()
                .get(URL_PATH)
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(responseBody.contains("\"empty\":false"));
        assertTrue(responseBody.contains("\"pageNumber\":1"));
        assertTrue(responseBody.contains("\"pageSize\":20"));
        assertTrue(responseBody.contains("\"totalElements\":1"));
        assertTrue(responseBody.contains("\"numberOfElements\":0"));
    }

    @ParameterizedTest
    @CsvSource({ID_USUARIO, ID_USUARIO_INEXISTENTE})
    @DisplayName("Testar cadastro de foto do usuário")
    @Order(4)
    void testarCadastroDeFoto(String userId) {
        byte[] conteudoFotoMock = "FOTO teste para gerar bytes mock".getBytes();
        InputStream inputStream = new ByteArrayInputStream(conteudoFotoMock);
        var resposta = given()
                .multiPart(NOME_FOTO_MOCK, "fototeste.jpg", inputStream)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .put(URL_PATH.concat("/" + userId + "/adicionar-foto"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);

        if (userId.equals(ID_USUARIO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_USER_NOTFOUND));
        } else {
            assertEquals(HttpStatus.SC_OK, resposta.statusCode());
            byte[] byteFoto = resposta.asByteArray();
            assertTrue(byteFoto.length > 0);
            LOG.info(responseBody);
            LOG.info("Tamanho do array de bytes FOTO: " + byteFoto.length);
        }

    }

    @ParameterizedTest
    @CsvSource({ID_USUARIO, ID_USUARIO_INEXISTENTE})
    @DisplayName("Testar ativação do usuário")
    @Order(5)
    void testarAtivarDesativarUsuario(String userId) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .patch(URL_PATH.concat("/ativar/" + userId))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        assertNotNull(responseBody);
        if (userId.equals(ID_USUARIO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_USER_NOTFOUND));
        } else {
            assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({ID_USUARIO, ID_USUARIO_INEXISTENTE})
    @DisplayName("Testar ativação do usuário ADMINISTRADOR")
    @Order(6)
    void testarAtivarDesativarUsuarioAdministrador(String userId) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .patch(URL_PATH.concat("/ativardesativaradm/" + userId))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        assertNotNull(responseBody);
        if (userId.equals(ID_USUARIO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_USER_NOTFOUND));
        } else {
            assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        }
    }

    @ParameterizedTest
    @CsvSource({ID_USUARIO_INEXISTENTE, ID_USUARIO})
    @DisplayName("Testar exclusão do usuário")
    @Order(7)
    void testarExclusaoUsuario(String userId) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(URL_PATH.concat("/delete/" + userId))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        assertNotNull(responseBody);
        if (userId.equals(ID_USUARIO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_USER_NOTFOUND));
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, resposta.statusCode());
        }
    }


    private static Stream<Arguments> usuariosParaTeste() {
        return Stream.of(
                Arguments.of(getNovoUsuario(), 1),
                Arguments.of(getUsuarioUsernameCpfExistente(), 2),
                Arguments.of(getUsuarioCamposInvalidos(), 3),
                Arguments.of(getUsuarioCpfInvalido(), 4)
        );
    }

    private static UsuarioDTO getNovoUsuario() {
        return UsuarioDTO
                .builder()
                .username("bernardo")
                .password("123456")
                .nome("Bernardo Levi Figueiredo")
                .cpf("74229043309")
                .email("nelson@infouai.com")
                .celular("8125563381")
                .build();
    }

    private static UsuarioDTO getUsuarioUsernameCpfExistente() {
        return getNovoUsuario();
    }

    private static UsuarioDTO getUsuarioCamposInvalidos() {
        return new UsuarioDTO();
    }

    private static UsuarioDTO getUsuarioCpfInvalido() {
        UsuarioDTO userCpfInvalido = new UsuarioDTO();
        userCpfInvalido.setCpf("067451");
        return userCpfInvalido;
    }
}