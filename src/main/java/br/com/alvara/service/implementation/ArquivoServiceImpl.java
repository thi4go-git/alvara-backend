package br.com.alvara.service.implementation;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.entity.Pdf;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.repository.ArquivoRepository;
import br.com.alvara.model.tipo.TipoDocumento;
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


@Service
public class ArquivoServiceImpl implements ArquivoService {

    Pdf pdfClass = new Pdf();
    private static final Logger LOG = LoggerFactory.getLogger(ArquivoServiceImpl.class);

    private static final String EXPIRA_STR = "EXPIRA";


    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    @Transactional
    public void salvarArquivo(Part arquivoNovo) {
        Arquivo novo = partToArquivo(arquivoNovo);
        if (novo != null) {
            arquivoRepository.save(novo);
        }
    }

    @Override
    @Transactional
    public void atualizarPdf(Part pdfUpdate, Integer id) {
        Arquivo novo = partToArquivo(pdfUpdate);
        if (novo != null) {
            arquivoRepository.
                    findById(id)
                    .map(achado -> {

                        achado.setTipoDoc(novo.getTipoDoc());
                        achado.setNomeArquivo(novo.getNomeArquivo());
                        achado.setNumeroAlvara(novo.getNumeroAlvara());
                        achado.setNomeEmpresa(novo.getNomeEmpresa());
                        achado.setCnpjEmpresa(novo.getCnpjEmpresa());
                        achado.setDataEmissao(novo.getDataEmissao());
                        achado.setDataVencimento(novo.getDataVencimento());
                        achado.setPdf(novo.getPdf());

                        return arquivoRepository.save(achado);

                    })
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado para atualizar!"));
        }
    }

    public Arquivo partToArquivo(Part arquivoPart) {
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado para deletar!"));
    }


    @Override
    public Page<ArquivoProjection> listarTodos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);
        return arquivoRepository.listarTodos(pageRequest);
    }

    @Override
    public Page<ArquivoProjection> listarTodosMatcher(int page, int size, ArquivoDTO dto) {
        Pageable pageable = PageRequest.of(page, size);
        return arquivoRepository
                .buscarArquivosPaginados(dto.getCnpjEmpresa().trim(), dto.getNomeEmpresa().trim(),
                        dto.getNumeroAlvara().trim(), dto.getNomeArquivo().trim(), pageable);
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
            if (arquivo.getDataVencimento() != null && arquivo.getExpira() <= 0) {
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
            if (arquivo.getDataVencimento() == null) {
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
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado Pelo ID !"));

    }

    @Override
    public void atualizarPorId(ArquivoDTO dto) {
        arquivoRepository.
                findById(dto.getId())
                .map(clienteAchado -> {

                    clienteAchado.setTipoDoc(dto.getTipoDoc());
                    clienteAchado.setNomeArquivo(dto.getNomeArquivo());
                    clienteAchado.setNumeroAlvara(dto.getNumeroAlvara());
                    clienteAchado.setNomeEmpresa(dto.getNomeEmpresa());
                    clienteAchado.setCnpjEmpresa(dto.getCnpjEmpresa());
                    clienteAchado.setDataEmissao(dto.getDataEmissao());
                    clienteAchado.setDataVencimento(dto.getDataVencimento());
                    clienteAchado.setObservacao(dto.getObservacao());

                    return arquivoRepository.save(clienteAchado);
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado para atualizar!"));
    }


    @Override
    public List<TipoDocumento> listaTipoDoc() {
        return List.of(TipoDocumento.values());
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
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado para deletar!"));
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
