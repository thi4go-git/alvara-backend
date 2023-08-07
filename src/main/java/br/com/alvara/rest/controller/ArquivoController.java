package br.com.alvara.rest.controller;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.tipo.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
import br.com.alvara.service.implementation.ArquivoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("api/alvara")
public class ArquivoController {


    @Autowired
    private ArquivoServiceImpl arquivoService;


    @PostMapping("/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public void salvarArquivo(@RequestParam("pdf") Part arquivo) {
        arquivoService.salvarArquivo(arquivo);
    }

    @PostMapping("/{id}/pdf-update")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarPdf(@PathVariable Integer id, @RequestParam("pdf") Part arquivo) {
        arquivoService.atualizarPdf(arquivo, id);
    }

    @GetMapping("/download/{id}")
    public byte[] baixarArquivo(@PathVariable Integer id) {
        return arquivoService.baixarArquivo(id);
    }

    @GetMapping
    public Page<ArquivoProjection> listarTodos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return arquivoService.listarTodos(page, size);
    }

    @PostMapping("/listar-matcher")
    public Page<ArquivoProjection> listarTodosMatcher(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestBody ArquivoDTO dto) {
        return arquivoService.listarTodosMatcher(page, size, dto);
    }

    @GetMapping("/vencidos")
    public Page<ArquivoProjection> listarVencidos(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return arquivoService.listarVencidos(page, size);
    }

    @GetMapping("/vencerate60dias")
    public Page<ArquivoProjection> listarVencerEmAte60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return arquivoService.listarVencerEmAte60Dias(page, size);
    }

    @GetMapping("/seminfo")
    public Page<ArquivoProjection> listarDocumentosSemInfo(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return arquivoService.listarDocumentosSemInfo(page, size);
    }

    @GetMapping("/vencerapos60dias")
    public Page<ArquivoProjection> listarVencerApos60Dias(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return arquivoService.listarVencerApos60Dias(page, size);
    }

    @GetMapping("{id}")
    public Arquivo buscarPorId(@PathVariable Integer id) {
        return arquivoService.buscarrPorId(id);
    }

    @PutMapping("/atualizar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPorBody(@RequestBody @Valid ArquivoDTO dto) {
        arquivoService.atualizarPorId(dto);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPorId(@PathVariable Integer id) {
        arquivoService.deletarPorId(id);
    }

    @GetMapping("/tipodocumento")
    public List<TipoDocumento> tipoDocumentoList() {
        return arquivoService.listaTipoDoc();
    }

    @GetMapping("/totalarquivos")
    public long totalArquivos() {
        return arquivoService.qtdeArquivos();
    }

    @GetMapping("/totalvencidos")
    public int totalArquivosVencidos() {
        return arquivoService.totalVencidos();
    }

    @GetMapping("/totalvencerem60dias")
    public int totalArquivosVencerEm60Dias() {
        return arquivoService.totalArquivosVencerEm60Dias();
    }

    @GetMapping("/totalseminformacoes")
    public int totalArquivosSemInformacoes() {
        return arquivoService.totaDocumentosSemInfo();
    }

    @GetMapping("/totalvencerapos60dias")
    public int totalArquivosVencerApos60Dias() {
        return arquivoService.totalArquivosVencerApos60Dias();
    }


}
