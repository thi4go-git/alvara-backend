package br.com.alvara.rest.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

import br.com.alvara.model.enums.TipoDocumento;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.rest.dto.ArquivoDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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
    private static final String MSG_NOTFOUND = "404 NOT_FOUND";
    private static final String MSG_ARQUIVO_NAO_ENCONTRADO = "Arquivo não localizado!";


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
            assertTrue(responseBody.contains(MSG_ARQUIVO_NAO_ENCONTRADO));
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

    @ParameterizedTest
    @MethodSource("arquivosParaTeste")
    @DisplayName("Testar atualizacao de Arquivos")
    @Order(4)
    void testeAtualizacaoArquivo(ArquivoDTO dto, int indexArquivo) {
        var resposta = given()
                .body(dto)
                .contentType(ContentType.JSON)
                .when()
                .put(URL_PATH.concat("/atualizar"))
                .then()
                .extract()
                .response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);

        if (indexArquivo == 1) {
            assertEquals(HttpStatus.SC_NO_CONTENT, resposta.statusCode());
        } else if (indexArquivo == 2) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_NOTFOUND));
            assertTrue(responseBody.contains(MSG_ARQUIVO_NAO_ENCONTRADO));
        } else if (indexArquivo == 3) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());
            assertTrue(responseBody.contains("campo.nomeEmpresa.obrigatorio"));
            assertTrue(responseBody.contains("campo.numeroAlvara.obrigatorio"));
            assertTrue(responseBody.contains("campo.nomeArquivo.obrigatorio"));
            assertTrue(responseBody.contains("campo.cnpjEmpresa.invalido"));
        }
    }

    @ParameterizedTest
    @CsvSource({ID_ARQUIVO, ID_ARQUIVO_INEXISTENTE})
    @DisplayName("Testar Buscar arquivo por ID")
    @Order(5)
    void testarBuscarArquivoPorId(String userId) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH.concat("/" + userId))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        if (userId.equals(ID_ARQUIVO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_NOTFOUND));
            assertTrue(responseBody.contains(MSG_ARQUIVO_NAO_ENCONTRADO));
        } else {
            ArquivoDTO dto = getArquivoUpdate();
            assertEquals(HttpStatus.SC_OK, resposta.statusCode());
            assertEquals(dto.getId().toString(), resposta.jsonPath().getString("id"));
            assertEquals(dto.getTipoDoc().toString(), resposta.jsonPath().getString("tipoDoc"));
            assertEquals(dto.getNomeArquivo(), resposta.jsonPath().getString("nomeArquivo"));
            assertEquals(dto.getNumeroAlvara(), resposta.jsonPath().getString("numeroAlvara"));
            assertEquals(dto.getNomeEmpresa(), resposta.jsonPath().getString("nomeEmpresa"));
            assertEquals(dto.getCnpjEmpresa(), resposta.jsonPath().getString("cnpjEmpresa"));
            assertEquals(dto.getDataVencimento().toString(), resposta.jsonPath().getString("dataVencimento"));
            assertNull(resposta.jsonPath().getString("expira"));
            assertNull(resposta.jsonPath().getString("observacao"));
            assertNull(resposta.jsonPath().getString("statusDocumento"));
            assertNull(resposta.jsonPath().getString("expira"));
            assertEquals("QVJRVUlWTyBBdHVhbGl6YWRvIGJ5dGVzIG1vY2s=", resposta.jsonPath().getString("pdf"));
        }
    }

    @Test
    @DisplayName("Deve Listar Vencidos")
    @Order(6)
    void deveListarVencidos() {
        Page<ArquivoProjection> pageArquivoProjectionMock = new PageImpl<>(Collections.emptyList());
        when(arquivoService.listarVencidos(anyInt(), anyInt()))
                .thenReturn(pageArquivoProjectionMock);
        ResponseEntity<Page<ArquivoProjection>> responseEntity = arquivoController.listarVencidos(0, 10);
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve Listar vencer ate 60 dias")
    @Order(7)
    void deveListarVencerate60dias() {
        Page<ArquivoProjection> pageArquivoProjectionMock = new PageImpl<>(Collections.emptyList());
        when(arquivoService.listarVencerEmAte60Dias(anyInt(), anyInt()))
                .thenReturn(pageArquivoProjectionMock);
        ResponseEntity<Page<ArquivoProjection>> responseEntity = arquivoController.listarVencerEmAte60Dias(0, 10);
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve listar Documentos Sem Info")
    @Order(8)
    void deveListarDocumentosSemInfo() {
        Page<ArquivoProjection> pageArquivoProjectionMock = new PageImpl<>(Collections.emptyList());
        when(arquivoService.listarDocumentosSemInfo(anyInt(), anyInt()))
                .thenReturn(pageArquivoProjectionMock);
        ResponseEntity<Page<ArquivoProjection>> responseEntity = arquivoController.listarDocumentosSemInfo(0, 10);
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve listar vencer apos 60 dias")
    @Order(9)
    void deveListarVencerApos60dias() {
        Page<ArquivoProjection> pageArquivoProjectionMock = new PageImpl<>(Collections.emptyList());
        when(arquivoService.listarVencerApos60Dias(anyInt(), anyInt()))
                .thenReturn(pageArquivoProjectionMock);
        ResponseEntity<Page<ArquivoProjection>> responseEntity = arquivoController.listarVencerApos60Dias(0, 10);
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve obter total de arquivos")
    @Order(10)
    void deveObterTotalDeArquivos() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH.concat("/totalarquivos"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertEquals("1", responseBody);
    }

    @Test
    @DisplayName("Deve obter total de arquivos vencidos")
    @Order(11)
    void deveObterTotalDeArquivosVencidos() {
        when(arquivoService.totalVencidos()).thenReturn(4);
        ResponseEntity<Integer> responseEntity = arquivoController.totalArquivosVencidos();
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve obter total de arquivos vencer em 60 dias")
    @Order(12)
    void deveObterTotalDeArquivosVencerEm60Dias() {
        when(arquivoService.totalArquivosVencerEm60Dias()).thenReturn(1);
        ResponseEntity<Integer> responseEntity = arquivoController.totalArquivosVencerEm60Dias();
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve obter total de arquivos sem informações")
    @Order(13)
    void deveObterTotalDeArquivosSemInfo() {
        when(arquivoService.totaDocumentosSemInfo()).thenReturn(2);
        ResponseEntity<Integer> responseEntity = arquivoController.totalArquivosSemInformacoes();
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve obter total de arquivos vencer após 60 dias")
    @Order(14)
    void deveObterTotalDeArquivosVencerApos60Dias() {
        when(arquivoService.totalArquivosVencerApos60Dias()).thenReturn(3);
        ResponseEntity<Integer> responseEntity = arquivoController.totalArquivosVencerApos60Dias();
        LOG.info(responseEntity.toString());
        assertNotNull(responseEntity);
        assertEquals("200 OK", responseEntity.getStatusCode().toString());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DisplayName("Deve listar tipos de documento")
    @Order(15)
    void deveListarTiposDeDocumento() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH.concat("/tipodocumento"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(responseBody.contains("ALVARA_FUNCIONAMENTO"));
        assertTrue(responseBody.contains("LICENCA_SANITARIA"));
        assertTrue(responseBody.contains("LICENCA_AMBIENTAL"));
        assertTrue(responseBody.contains("DESCONHECIDO"));
        assertTrue(responseBody.contains("ALVARA_BOMBEIRO"));
    }

    @Test
    @DisplayName("Deve listar tipos status documentos")
    @Order(16)
    void deveListarTiposStatusDocumento() {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL_PATH.concat("/status-documento"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(responseBody.contains("ANALISE"));
        assertTrue(responseBody.contains("EXIGENCIA"));
        assertTrue(responseBody.contains("DEFERIDO"));
    }


    @ParameterizedTest
    @CsvSource({ID_ARQUIVO_INEXISTENTE, ID_ARQUIVO})
    @DisplayName("Testar deleção de arquivos")
    @Order(17)
    void testarDeletarArquivoPorId(String arquivoId) {
        var resposta = given()
                .contentType(ContentType.JSON)
                .when()
                .delete(URL_PATH.concat("/delete/" + arquivoId))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        if (arquivoId.equals(ID_ARQUIVO_INEXISTENTE)) {
            assertEquals(HttpStatus.SC_NOT_FOUND, resposta.statusCode());
            assertTrue(responseBody.contains(MSG_NOTFOUND));
            assertTrue(responseBody.contains(MSG_ARQUIVO_NAO_ENCONTRADO));
        } else {
            assertEquals(HttpStatus.SC_NO_CONTENT, resposta.statusCode());
        }
    }

    @Test
    @DisplayName("Testar deleção de arquivos através de LISTA")
    @Order(18)
    void testarDeletarArquivoPorLista() {

        List<String> listaDeletar = new ArrayList<>();
        listaDeletar.add(ID_ARQUIVO);

        var resposta = given()
                .contentType(ContentType.JSON)
                .body(listaDeletar)
                .when()
                .post(URL_PATH.concat("/deletar-multiplos"))
                .then()
                .extract()
                .response();
        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);
        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_NO_CONTENT, resposta.statusCode());
    }


    private static Stream<Arguments> arquivosParaTeste() {
        return Stream.of(
                Arguments.of(getArquivoUpdate(), 1),
                Arguments.of(getArquivoUpdateIdInexistente(), 2),
                Arguments.of(getArquivoUpdateCamposInvalidos(), 3)
        );
    }

    private static ArquivoDTO getArquivoUpdate() {
        return ArquivoDTO.builder()
                .id(1)
                .tipoDoc(TipoDocumento.LICENCA_AMBIENTAL)
                .nomeArquivo("nome teste")
                .numeroAlvara("1234567890")
                .nomeEmpresa("EMpresa LTDA")
                .cnpjEmpresa("06139890000101")
                .dataVencimento(LocalDate.of(2022, 1, 12))
                .build();
    }

    private static ArquivoDTO getArquivoUpdateIdInexistente() {
        return ArquivoDTO.builder()
                .id(50)
                .tipoDoc(TipoDocumento.LICENCA_AMBIENTAL)
                .nomeArquivo("nome teste1")
                .numeroAlvara("1234567891")
                .nomeEmpresa("EMpresa LTDA")
                .cnpjEmpresa("15406009000174")
                .build();
    }

    private static ArquivoDTO getArquivoUpdateCamposInvalidos() {
        return ArquivoDTO.builder()
                .id(1)
                .tipoDoc(TipoDocumento.LICENCA_AMBIENTAL)
                .nomeArquivo("")
                .numeroAlvara("")
                .nomeEmpresa("")
                .cnpjEmpresa("96139895006100")
                .build();
    }

}