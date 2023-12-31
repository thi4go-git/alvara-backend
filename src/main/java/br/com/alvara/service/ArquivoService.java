package br.com.alvara.service;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.enums.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
import br.com.alvara.rest.dto.ArquivoFilterDTO;
import org.springframework.data.domain.Page;

import javax.servlet.http.Part;
import java.util.List;

public interface ArquivoService {

    Arquivo salvarArquivo(Part pdf);

    void atualizarPdf(Part pdfNovo, Integer id);

    byte[] baixarArquivo(int id);

    Page<ArquivoProjection> listarTodosFilterMatcher(int page, int size, ArquivoFilterDTO dto);

    Page<ArquivoProjection> listarVencidos(int page, int size);

    Page<ArquivoProjection> listarVencerEmAte60Dias(int page, int size);

    Page<ArquivoProjection> listarDocumentosSemInfo(int page, int size);

    Page<ArquivoProjection> listarVencerApos60Dias(int page, int size);

    Arquivo buscarrPorId(Integer id);

    void atualizarPorId(ArquivoDTO dto);

    List<TipoDocumento> listaTipoDoc();

    List<StatusDocumento> listaStatusDocumento();

    void deletarPorId(Integer id);

    void deletarPorLista(List<String> listaDeletar);

    int qtdeArquivos();

    int totalArquivosVencerApos60Dias();

    int totalArquivosVencerEm60Dias();

    int totaDocumentosSemInfo();

    int totalVencidos();
}

