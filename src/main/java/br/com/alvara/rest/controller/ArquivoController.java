package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.enums.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
import br.com.alvara.rest.dto.ArquivoDownloadDTO;
import br.com.alvara.rest.dto.ArquivoFilterDTO;
import br.com.alvara.rest.dto.ArquivoResponseDTO;
import br.com.alvara.rest.mapper.ArquivoMapper;
import br.com.alvara.service.implementation.ArquivoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.Part;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("api/alvara")
public class ArquivoController {


    private static final String CAMPO_ID_OBRIGATORIO = "O campo id é obrigatório!";
    private static final String SERVER_ERROR = "Erro interno do servidor!";
    private static final String ARQUIVO_NOTFOUND = "Arquivo não localizado!";

    @Autowired
    private ArquivoServiceImpl arquivoService;
    @Autowired
    private ArquivoMapper arquivoMapper;

    @PostMapping("/pdf")
    @Operation(summary = "salvar PDF", description = "salvar PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "PDF Salvo!"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<ArquivoResponseDTO> salvarArquivo(@RequestParam("pdf") Part arquivo) {
        Arquivo salvo = arquivoService.salvarArquivo(arquivo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(arquivoMapper.arquivoToArquivoResponseDTO(salvo));

    }

    @PostMapping("/{id}/pdf-update")
    @Operation(summary = "Atualiza PDF", description = "Atualizar PDF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF Atualizado!"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = ARQUIVO_NOTFOUND)
    })
    public ResponseEntity<Void> atualizarPdf(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id,
            @RequestParam("pdf") Part arquivo
    ) {
        arquivoService.atualizarPdf(arquivo, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/listar-matcher")
    @Operation(summary = "Listar com Filtros", description = "Este endpoint Lista Paginandodo + Filtros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filtros aplicados"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<ArquivoProjection>> listarTodosFilterMatcher(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestBody ArquivoFilterDTO dto
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarTodosFilterMatcher(page, size, dto);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/vencidos")
    @Operation(summary = "Listar Vencidos", description = "Este endpoint Lista Vencidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de vencidos"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<ArquivoProjection>> listarVencidos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencidos(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/vencerate60dias")
    @Operation(summary = "Lista que vencem até 60 dias", description = "Este endpoint Lista os que vencem até 60 dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vencem em até 60 dias"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<ArquivoProjection>> listarVencerEmAte60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencerEmAte60Dias(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/seminfo")
    @Operation(summary = "Lista os sem Informações", description = "Este endpoint Lista os sem Informações")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listgem dos sem Informações"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<ArquivoProjection>> listarDocumentosSemInfo(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarDocumentosSemInfo(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/vencerapos60dias")
    @Operation(summary = "Lista que vencem Após 60 dias", description = "Este endpoint Lista os que vencem Após 60 dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vencem Após 60 dias"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Page<ArquivoProjection>> listarVencerApos60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencerApos60Dias(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("{id}")
    @Operation(summary = "Busca Arquivo pelo id", description = "Arquivo pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = ARQUIVO_NOTFOUND)
    })
    public ResponseEntity<ArquivoDownloadDTO> buscarPorId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        Arquivo arquivo = arquivoService.buscarrPorId(id);
        return ResponseEntity.ok().body(arquivoMapper.arquivoToArquivoDownloadDTO(arquivo));
    }

    @PutMapping("/atualizar")
    @Operation(summary = "Atualizar Arquivo ", description = "Arquivo update")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Arquivo atualizado"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = ARQUIVO_NOTFOUND)
    })
    public ResponseEntity<Void> atualizarPorBody(
            @RequestBody @Valid ArquivoDTO dto
    ) {
        arquivoService.atualizarPorId(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deletar Arquivo ", description = "Arquivo Delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Arquivo deletado"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR),
            @ApiResponse(responseCode = "404", description = ARQUIVO_NOTFOUND)
    })
    public ResponseEntity<Void> deletarPorId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        arquivoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deletar-multiplos")
    @Operation(summary = "Deletar vários Arquivos ", description = "Arquivo Delete vários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Arquivos deletado"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Void> deletarPorId(@RequestBody List<String> listaDeletar) {
        arquivoService.deletarPorLista(listaDeletar);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipodocumento")
    @Operation(summary = "Listar tipos de Documento ", description = "Listagem tipos de Documento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado."),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoList() {
        List<TipoDocumento> tiposDocs = arquivoService.listaTipoDoc();
        return ResponseEntity.ok().body(tiposDocs);
    }

    @GetMapping("/status-documento")
    @Operation(summary = "Listar Status Documento ", description = "Listagem Status Documento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<List<StatusDocumento>> statusDocumentoList() {
        List<StatusDocumento> statusDocumentoList = arquivoService.listaStatusDocumento();
        return ResponseEntity.ok().body(statusDocumentoList);
    }

    @GetMapping("/totalarquivos")
    @Operation(summary = "QTDE Total de arquivos", description = "QTDE Total")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total de arquivos"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Long> totalArquivos() {
        long total = arquivoService.qtdeArquivos();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencidos")
    @Operation(summary = "QTDE Total de arquivos vencidos", description = "QTDE Total vencidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total arquivos vencidos"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Integer> totalArquivosVencidos() {
        int total = arquivoService.totalVencidos();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencerem60dias")
    @Operation(summary = "QTDE Total vencer em 60 dias", description = "QTDE Total vencer em 60 dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Total vencer em 60 dias"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Integer> totalArquivosVencerEm60Dias() {
        int total = arquivoService.totalArquivosVencerEm60Dias();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalseminformacoes")
    @Operation(summary = "QTDE Total sem info.", description = "QTDE Total sem info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QTDE Total vencer em 60 dias"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Integer> totalArquivosSemInformacoes() {
        int total = arquivoService.totaDocumentosSemInfo();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencerapos60dias")
    @Operation(summary = "QTDE Total vencer após 60 dias.", description = "QTDE Total vencer após 60 dias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QTDE Total vencer após 60 dias"),
            @ApiResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public ResponseEntity<Integer> totalArquivosVencerApos60Dias() {
        int total = arquivoService.totalArquivosVencerApos60Dias();
        return ResponseEntity.ok().body(total);
    }


}
