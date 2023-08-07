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
import java.util.ArrayList;
import java.util.List;


@Service
public class ArquivoServiceImpl implements ArquivoService {

    Pdf pdfClass = new Pdf();

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoServiceImpl.class);


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

            if (arqPdf.delete()) {
                LOG.info("PDF Deletado!");
            } else {
                arqPdf.deleteOnExit();
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
        arquivoRepository.
                findById(id)
                .map(arquivo -> arquivo.getPdf())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado para deletar!"));
        return new byte[0];
    }


    @Override
    public Page<ArquivoProjection> listarTodos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "EXPIRA");
        return arquivoRepository.listarTodos(pageRequest);
    }

    @Override
    public Page<ArquivoProjection> listarTodosMatcher(int page, int size, ArquivoDTO dto) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ArquivoProjection> resultadoPaginado = arquivoRepository
                .buscarArquivosPaginados(dto.getCnpj_empresa().trim(), dto.getNome_empresa().trim(),
                        dto.getNumero_alvara().trim(), dto.getNome_arquivo().trim(), pageable);

        return resultadoPaginado;
    }

    @Override
    public Page<ArquivoProjection> listarVencidos(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "EXPIRA");
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getData_vencimento() != null && arquivo.getExpira() <= 0) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<ArquivoProjection>(lista, pageRequest, size);

    }

    @Override
    public Page<ArquivoProjection> listarVencerEmAte60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "EXPIRA");
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getExpira() > 0 &&
                    arquivo.getExpira() <= 60) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<ArquivoProjection>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarDocumentosSemInfo(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "EXPIRA");
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getData_vencimento() == null) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<ArquivoProjection>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarVencerApos60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                "EXPIRA");
        List<ArquivoProjection> lista = new ArrayList<>();
        for (ArquivoProjection arquivo : arquivoRepository.listarTodosList()) {
            if (arquivo.getExpira() > 60) {
                lista.add(arquivo);
            }
        }
        return new PageImpl<ArquivoProjection>(lista, pageRequest, size);
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

                    clienteAchado.setTipo_doc(dto.getTipo_doc());
                    clienteAchado.setNome_arquivo(dto.getNome_arquivo());
                    clienteAchado.setNumero_alvara(dto.getNumero_alvara());
                    clienteAchado.setNome_empresa(dto.getNome_empresa());
                    clienteAchado.setCnpj_empresa(dto.getCnpj_empresa());
                    clienteAchado.setData_emissao(dto.getData_emissao());
                    clienteAchado.setData_vencimento(dto.getData_vencimento());
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
