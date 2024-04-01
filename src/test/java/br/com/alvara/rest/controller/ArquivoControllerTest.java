package br.com.alvara.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.rest.dto.ArquivoFilterDTO;
import br.com.alvara.rest.dto.UsuarioDTO;
import br.com.alvara.service.implementation.ArquivoServiceImpl;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(MockitoJUnitRunner.class)
class ArquivoControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Mock
    private ArquivoServiceImpl arquivoService;
    @InjectMocks
    private ArquivoController arquivoController;

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoControllerTest.class);
    private static final String URL_PATH = "api/alvara";
    private static final String ID_ARQUIVO = "1";
    private static final String ID_ARQUIVO_INEXISTENTE = "4";


    @Test
    @DisplayName("Deve salvar arquivo")
    @Order(1)
    void deveSalvarArquivo() {
        byte[] conteudoArquivoMock = "ARQUIVO teste para gerar bytes mock".getBytes();
        InputStream inputStream = new ByteArrayInputStream(conteudoArquivoMock);
        var resposta = given()
                .multiPart("pdf", "pdf.jpg", inputStream)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post(URL_PATH.concat("/pdf"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_CREATED, resposta.statusCode());
        byte[] byteFoto = resposta.asByteArray();
        assertTrue(byteFoto.length > 0);
        assertEquals(ID_ARQUIVO, resposta.jsonPath().getString("id"));
        LOG.info("Tamanho do array de bytes ARQUIVO: " + byteFoto.length);
    }

    @ParameterizedTest
    @CsvSource({ID_ARQUIVO, ID_ARQUIVO_INEXISTENTE})
    @DisplayName("Testar atualização de arquivos")
    @Order(2)
    void testarAtualizaçãoDoArquivo(String idArquivo) {
        byte[] conteudoArquivoUpdateMock = "ARQUIVO Atualizado bytes mock".getBytes();
        InputStream inputStream = new ByteArrayInputStream(conteudoArquivoUpdateMock);
        var resposta = given()
                .multiPart("pdf", "pdf.jpg", inputStream)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .when()
                .post(URL_PATH.concat("/" + idArquivo + "/pdf-update"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        if (idArquivo.equals(ID_ARQUIVO)) {
            assertNotNull(responseBody);
            assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        } else {
            assertNotNull(responseBody);
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains("Arquivo não localizado!"));
        }
    }

    @Test
    @DisplayName("Deve Listar Todos Filter Matcher Mock")
    @Order(3)
    void deveListarTodosFilterMatcherMock() {
        Page<ArquivoProjection> pageArquivoProjectionMock = new PageImpl<>(Collections.emptyList());
        when(arquivoService.listarTodosFilterMatcher(anyInt(), anyInt(), any(ArquivoFilterDTO.class)))
                .thenReturn(pageArquivoProjectionMock);
        ArquivoFilterDTO filtroDto = ArquivoFilterDTO
                .builder()
                .nomeEmpresa("")
                .cnpjEmpresa("")
                .numeroAlvara("")
                .tipoDoc("")
                .statusDocumento("")
                .statusDocumento("")
                .build();
        ResponseEntity<Page<ArquivoProjection>> responseEntity = arquivoController.listarTodosFilterMatcher(0, 10, filtroDto);
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

}