package br.com.alvara.service.implementation;

import br.com.alvara.exception.GeralException;
import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.rest.dto.ArquivoFilterDTO;
import br.com.alvara.util.Pdf;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.repository.ArquivoRepository;
import br.com.alvara.model.enums.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
import br.com.alvara.service.ArquivoService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ArquivoServiceImpl implements ArquivoService {

    Pdf pdfClass = new Pdf();
    private static final Logger LOG = LoggerFactory.getLogger(ArquivoServiceImpl.class);

    private static final String EXPIRA_STR = "EXPIRA";

    private static final String ARQUIVO_NOTFOUND = "Arquivo não localizado!";


    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    @Transactional
    public Arquivo salvarArquivo(Part arquivoNovo) {
        Arquivo novo = converterPartParaAquivo(arquivoNovo);
        if (novo != null) {
            return arquivoRepository.save(novo);
        } else {
            throw new GeralException("Erro ao salvar Arquivo");
        }
    }

    @Override
    @Transactional
    public void atualizarPdf(Part pdfUpdate, Integer id) {
        Arquivo novo = converterPartParaAquivo(pdfUpdate);
        if (novo != null) {
            arquivoRepository.
                    findById(id)
                    .map(achado -> {

                        achado.setTipo_doc(novo.getTipo_doc());
                        achado.setNome_arquivo(novo.getNome_arquivo());
                        achado.setNumero_alvara(novo.getNumero_alvara());
                        achado.setNome_empresa(novo.getNome_empresa());
                        achado.setCnpj_empresa(novo.getCnpj_empresa());
                        achado.setData_emissao(novo.getData_emissao());
                        achado.setData_vencimento(novo.getData_vencimento());
                        achado.setPdf(novo.getPdf());

                        return arquivoRepository.save(achado);

                    })
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
        }
    }

    public Arquivo converterPartParaAquivo(Part arquivoPart) {
        InputStream is = null;
        try {
            is = arquivoPart.getInputStream();
            byte[] bytes = new byte[(int) arquivoPart.getSize()];
            IOUtils.readFully(is, bytes);

            File arqPdf = pdfClass.byteTofile(bytes, arquivoPart.getSubmittedFileName());
            Arquivo arquivoObtido = pdfClass.lerPdf(arqPdf, bytes);

            if (arqPdf.exists()) {
                Path pdfPath = arqPdf.toPath();
                try {
                    Files.delete(pdfPath);
                    LOG.info("Arquivo excluído com sucesso.");
                } catch (IOException e) {
                    LOG.error("Falha ao excluir o arquivo. " + e.getMessage());
                }
            }

            is.close();

            return arquivoObtido;

        } catch (IOException e) {
            try {
                is.close();
            } catch (IOException ex) {
                LOG.error("Erro método partToArquivo " + ex.getCause());
            }
        }
        return null;
    }

    @Override
    public byte[] baixarArquivo(int id) {
        return arquivoRepository
                .findById(id)
                .map(Arquivo::getPdf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
    }

    @Override
    public Page<ArquivoProjection> listarTodosFilterMatcher(int page, int size, ArquivoFilterDTO dto) {

        TipoDocumento tipoDocumento = null;

        if (Objects.nonNull(dto.getTipo_doc())) {
            if (!dto.getTipo_doc().equals("TODOS") &&
                    !dto.getTipo_doc().equals("")) {
                tipoDocumento = TipoDocumento.valueOf(dto.getTipo_doc());
            }
        }

        StatusDocumento statusDocumento = null;

        if (Objects.nonNull(dto.getStatus_documento())) {
            if (!dto.getStatus_documento().equals("TODOS") &&
                    !dto.getStatus_documento().equals("")) {
                statusDocumento = StatusDocumento.valueOf(dto.getStatus_documento());
            }
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);

        if (tipoDocumento != null && statusDocumento != null) {
            return this.aplicarFiltroComTipoDocEStatusDocEResto(dto, tipoDocumento, statusDocumento, pageRequest);
        } else {
            if (tipoDocumento != null) {
                return this.aplicarFiltroComTipoDocEResto(dto, tipoDocumento, pageRequest);
            } else {
                if (statusDocumento != null) {
                    return this.aplicarFiltroComStatusDocEResto(dto, statusDocumento, pageRequest);
                } else {
                    return this.aplicarFiltroSemTipoDocEStatusDoc(dto, pageRequest);
                }
            }
        }
    }

    private Page<ArquivoProjection> aplicarFiltroComTipoDocEStatusDocEResto(
            ArquivoFilterDTO dto, TipoDocumento tipoDocumento, StatusDocumento statusDocumento,
            PageRequest pageRequest
    ) {
        return arquivoRepository
                .buscarArquivosPaginadosFilterComTipoDocEStatusDoc(
                        dto.getNome_empresa().trim(), dto.getNumero_alvara().trim(),
                        dto.getCnpj_empresa().trim(), tipoDocumento.ordinal(),
                        statusDocumento.ordinal(), pageRequest);
    }

    private Page<ArquivoProjection> aplicarFiltroComTipoDocEResto(
            ArquivoFilterDTO dto, TipoDocumento tipoDocumento, PageRequest pageRequest
    ) {
        return arquivoRepository
                .buscarArquivosPaginadosFilterComTipoDoc(
                        dto.getNome_empresa().trim(), dto.getNumero_alvara().trim(),
                        dto.getCnpj_empresa().trim(), tipoDocumento.ordinal()
                        , pageRequest);
    }

    private Page<ArquivoProjection> aplicarFiltroComStatusDocEResto(
            ArquivoFilterDTO dto, StatusDocumento statusDocumento, PageRequest pageRequest
    ) {
        return arquivoRepository
                .buscarArquivosPaginadosFilterComStatusDoc(
                        dto.getNome_empresa().trim(), dto.getNumero_alvara().trim(),
                        dto.getCnpj_empresa().trim(), statusDocumento.ordinal()
                        , pageRequest);
    }

    private Page<ArquivoProjection> aplicarFiltroSemTipoDocEStatusDoc(
            ArquivoFilterDTO dto, PageRequest pageRequest
    ) {
        return arquivoRepository
                .buscarArquivosPaginadosFilterSemTipoDoc(
                        dto.getNome_empresa().trim(), dto.getNumero_alvara().trim(),
                        dto.getCnpj_empresa().trim(), pageRequest);
    }

    @Override
    public Page<ArquivoProjection> listarVencidos(int page, int size) {

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getData_vencimento() != null && arquivo.getExpira() <= 0) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarVencerEmAte60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getExpira() > 0 &&
                    arquivo.getExpira() <= 60) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarDocumentosSemInfo(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getData_vencimento() == null) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarVencerApos60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getExpira() > 60) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Arquivo buscarrPorId(Integer id) {
        return arquivoRepository.
                findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
    }

    @Override
    public void atualizarPorId(ArquivoDTO dto) {
        arquivoRepository.
                findById(dto.getId())
                .map(clienteAchado -> {
                    clienteAchado.setTipo_doc(dto.getTipo_doc());
                    clienteAchado.setNome_arquivo(dto.getNome_arquivo());
                    clienteAchado.setNumero_alvara(dto.getNumero_alvara());
                    clienteAchado.setNome_empresa(dto.getNome_empresa());
                    clienteAchado.setCnpj_empresa(dto.getCnpj_empresa());
                    clienteAchado.setData_emissao(dto.getData_emissao());
                    clienteAchado.setData_vencimento(dto.getData_vencimento());
                    clienteAchado.setObservacao(dto.getObservacao());
                    clienteAchado.setStatus_documento(dto.getStatus_documento());

                    return arquivoRepository.save(clienteAchado);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
    }


    @Override
    public List<TipoDocumento> listaTipoDoc() {
        return List.of(TipoDocumento.values());
    }

    @Override
    public List<StatusDocumento> listaStatusDocumento() {
        return List.of(StatusDocumento.values());
    }

    @Override
    @Transactional
    public void deletarPorId(Integer id) {
        arquivoRepository.
                findById(id)
                .map(documento -> {
                    arquivoRepository.delete(documento);
                    return Void.TYPE;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
    }

    @Override
    public void deletarPorLista(List<String> listaDeletar) {
        if (!listaDeletar.isEmpty()) {
            for (String id : listaDeletar) {
                Integer idDeletar = Integer.parseInt(id.trim());
                arquivoRepository.
                        findById(idDeletar)
                        .map(documento -> {
                            arquivoRepository.delete(documento);
                            return Void.TYPE;
                        });
            }
        }
    }

    @Override
    public int qtdeArquivos() {
        return arquivoRepository.totalDocumentos();
    }

    @Override
    public int totalArquivosVencerApos60Dias() {
        return arquivoRepository.totalArquivosVencerApos60Dias();
    }

    @Override
    public int totalArquivosVencerEm60Dias() {
        return arquivoRepository.totalArquivosVencerEm60Dias();
    }

    @Override
    public int totaDocumentosSemInfo() {
        return arquivoRepository.totaDocumentosSemInfo();
    }

    @Override
    public int totalVencidos() {
        return arquivoRepository.totalVencidos();
    }


}
