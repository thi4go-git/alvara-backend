package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.tipo.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
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

    @Autowired
    private ArquivoServiceImpl arquivoService;

    @Autowired
    private ArquivoMapper arquivoMapper;

    @PostMapping("/pdf")
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
    public ResponseEntity<Void> atualizarPdf(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id,
            @RequestParam("pdf") Part arquivo
    ) {
        arquivoService.atualizarPdf(arquivo, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> baixarArquivo(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        byte[] bytesArquivo = arquivoService.baixarArquivo(id);
        return ResponseEntity.ok().body(bytesArquivo);
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
    public ResponseEntity<Page<ArquivoProjection>> listarVencidos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencidos(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/vencerate60dias")
    public ResponseEntity<Page<ArquivoProjection>> listarVencerEmAte60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencerEmAte60Dias(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/seminfo")
    public ResponseEntity<Page<ArquivoProjection>> listarDocumentosSemInfo(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarDocumentosSemInfo(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("/vencerapos60dias")
    public ResponseEntity<Page<ArquivoProjection>> listarVencerApos60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Page<ArquivoProjection> pageArquivoProjection = arquivoService.listarVencerApos60Dias(page, size);
        return ResponseEntity.ok().body(pageArquivoProjection);
    }

    @GetMapping("{id}")
    public ResponseEntity<Arquivo> buscarPorId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        Arquivo arquivo = arquivoService.buscarrPorId(id);
        return ResponseEntity.ok().body(arquivo);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Void> atualizarPorBody(
            @RequestBody @Valid ArquivoDTO dto
    ) {
        arquivoService.atualizarPorId(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletarPorId(
            @PathVariable("id") @NotBlank(message = CAMPO_ID_OBRIGATORIO) final Integer id
    ) {
        arquivoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/deletar-multiplos")
    public ResponseEntity<Void> deletarPorId(@RequestBody List<String> listaDeletar) {
        arquivoService.deletarPorLista(listaDeletar);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipodocumento")
    public ResponseEntity<List<TipoDocumento>> tipoDocumentoList() {
        List<TipoDocumento> tiposDocs = arquivoService.listaTipoDoc();
        return ResponseEntity.ok().body(tiposDocs);
    }

    @GetMapping("/totalarquivos")
    public ResponseEntity<Long> totalArquivos() {
        long total = arquivoService.qtdeArquivos();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencidos")
    public ResponseEntity<Integer> totalArquivosVencidos() {
        int total = arquivoService.totalVencidos();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencerem60dias")
    public ResponseEntity<Integer> totalArquivosVencerEm60Dias() {
        int total = arquivoService.totalArquivosVencerEm60Dias();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalseminformacoes")
    public ResponseEntity<Integer> totalArquivosSemInformacoes() {
        int total = arquivoService.totaDocumentosSemInfo();
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/totalvencerapos60dias")
    public ResponseEntity<Integer> totalArquivosVencerApos60Dias() {
        int total = arquivoService.totalArquivosVencerApos60Dias();
        return ResponseEntity.ok().body(total);
    }


}
