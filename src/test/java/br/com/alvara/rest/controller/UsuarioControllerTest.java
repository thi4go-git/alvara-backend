package br.com.alvara.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.com.alvara.rest.dto.UsuarioDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    @DisplayName("Deve salvar Usuario com Sucesso!")
    @Order(1)
    void salvarUsuarioSucesso() {
        UsuarioDTO dto = gerarUsuarioDTO();
        var resposta = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(URL_PATH.concat("/novo"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info("RESPONSE => ".concat(responseBody));
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_CREATED, resposta.statusCode());
        assertEquals("1", resposta.jsonPath().getString("id"));
        assertEquals(dto.getUsername(), resposta.jsonPath().getString("username"));
        assertEquals(dto.getPassword(), resposta.jsonPath().getString("password"));
        assertEquals("USER", resposta.jsonPath().getString("role"));
        assertEquals("false", resposta.jsonPath().getString("ativo"));
        assertEquals(dto.getNome(), resposta.jsonPath().getString("nome"));
        assertEquals(dto.getCpf(), resposta.jsonPath().getString("cpf"));
        assertEquals(dto.getEmail(), resposta.jsonPath().getString("email"));
        assertEquals(dto.getCelular(), resposta.jsonPath().getString("celular"));
    }

    @Test
    @DisplayName("Não Deve salvar Usuario com CPF e Username já cadastrado!")
    @Order(2)
    void naoSalvarUsuarioComCpfUsernameJaCadastrado() {
        UsuarioDTO dto = gerarUsuarioDTO();
        var resposta = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(URL_PATH.concat("/novo"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info("RESPONSE => ".concat(responseBody));
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
        assertTrue(responseBody.contains("Já existe um Usuário cadastrado com o CPF informado"));
        assertTrue(responseBody.contains("Já existe um Usuário cadastrado com o Username informado"));
    }

    @Test
    @DisplayName("Não Deve salvar Usuario com dados incompletos!")
    @Order(3)
    void naoSalvarUsuarioComDadosIncompletos() {
        UsuarioDTO dto = new UsuarioDTO();
        var resposta = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(URL_PATH.concat("/novo"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info("RESPONSE => ".concat(responseBody));
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
        assertTrue(responseBody.contains("O campo cpf é Obrigatório!"));
        assertTrue(responseBody.contains("O campo nome é Obrigatório!"));
        assertTrue(responseBody.contains("O campo username é Obrigatório!"));
        assertTrue(responseBody.contains("O campo password é Obrigatório!"));
    }

    @Test
    @DisplayName("Deve listar todos usuários sem Parâmetros")
    @Order(4)
    void deveListarTodosUsuariosSemParametros() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH)
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info("RESPONSE => ".concat(responseBody));
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
    @Order(5)
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
        LOG.info("RESPONSE => ".concat(responseBody));
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(responseBody.contains("\"empty\":false"));
        assertTrue(responseBody.contains("\"pageNumber\":1"));
        assertTrue(responseBody.contains("\"pageSize\":20"));
        assertTrue(responseBody.contains("\"totalElements\":1"));
        assertTrue(responseBody.contains("\"numberOfElements\":0"));
    }

    private UsuarioDTO gerarUsuarioDTO() {
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
}