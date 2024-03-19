package br.com.alvara.service.implementation;

import br.com.alvara.exception.GeralException;
import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.rest.dto.ArquivoFilterDTO;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.repository.ArquivoRepository;
import br.com.alvara.model.enums.TipoDocumento;
import br.com.alvara.rest.dto.ArquivoDTO;
import br.com.alvara.service.ArquivoService;
import br.com.alvara.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ArquivoServiceImpl implements ArquivoService {

    private static final String EXPIRA_STR = "EXPIRA";
    private static final String ARQUIVO_NOTFOUND = "Arquivo não localizado!";
    private static final String TXT_TODOS = "TODOS";


    @Autowired
    private ArquivoRepository arquivoRepository;

    @Override
    @Transactional
    public Arquivo salvarArquivo(Part arquivoNovo) {
        Arquivo novo = FileUtils.converterPdfParaAquivo(arquivoNovo);
        if (novo != null) {
            return arquivoRepository.save(novo);
        } else {
            throw new GeralException("Erro ao salvar Arquivo");
        }
    }

    @Override
    @Transactional
    public void atualizarPdf(Part pdfUpdate, Integer id) {
        Arquivo novo = FileUtils.converterPdfParaAquivo(pdfUpdate);
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
                            new ResponseStatusException(HttpStatus.NOT_FOUND, ARQUIVO_NOTFOUND));
        }
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

        if (Objects.nonNull(dto.getTipo_doc()) && !dto.getTipo_doc().equals(TXT_TODOS) &&
                !dto.getTipo_doc().isEmpty()) {
            tipoDocumento = TipoDocumento.valueOf(dto.getTipo_doc());
        }

        StatusDocumento statusDocumento = null;

        if (Objects.nonNull(dto.getStatus_documento()) && !dto.getStatus_documento().equals(TXT_TODOS) &&
                !dto.getStatus_documento().isEmpty()) {
            statusDocumento = StatusDocumento.valueOf(dto.getStatus_documento());
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

        List<ArquivoProjection> lista = arquivoRepository.listarTodosList().stream()
                .filter(arquivo -> arquivo.getData_vencimento() != null && arquivo.getExpira() <= 0)
                .collect(Collectors.toList());

        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarVencerEmAte60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);

        List<ArquivoProjection> lista = arquivoRepository.listarTodosList().stream()
                .filter(arquivo -> arquivo.getExpira() > 0 && arquivo.getExpira() <= 60)
                .collect(Collectors.toList());

        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarDocumentosSemInfo(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);

        List<ArquivoProjection> lista = arquivoRepository.listarTodosList().stream()
                .filter(arquivo -> arquivo.getData_vencimento() == null)
                .collect(Collectors.toList());

        return new PageImpl<>(lista, pageRequest, size);
    }

    @Override
    public Page<ArquivoProjection> listarVencerApos60Dias(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.Direction.ASC,
                EXPIRA_STR);

        List<ArquivoProjection> lista = arquivoRepository.listarTodosList().stream()
                .filter(arquivo -> arquivo.getExpira() > 60)
                .collect(Collectors.toList());

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
                    clienteAchado.setTipoDoc(dto.getTipo_doc());
                    clienteAchado.setNomeArquivo(dto.getNome_arquivo());
                    clienteAchado.setNumeroAlvara(dto.getNumero_alvara());
                    clienteAchado.setNomeEmpresa(dto.getNome_empresa());
                    clienteAchado.setCnpjEmpresa(dto.getCnpj_empresa());
                    clienteAchado.setDataEmissao(dto.getData_emissao());
                    clienteAchado.setDataVencimento(dto.getData_vencimento());
                    clienteAchado.setObservacao(dto.getObservacao());
                    clienteAchado.setStatusDocumento(dto.getStatus_documento());

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
