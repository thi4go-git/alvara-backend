package br.com.alvara.model.repository;

import br.com.alvara.model.entity.Arquivo;

import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.repository.projection.impl.ArquivoProjectionImpl;
import com.sun.istack.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

    @Query(value = "SELECT * FROM ARQUIVO  WHERE PDF =:pdf LIMIT 1", nativeQuery = true)
    Optional<Arquivo> findByPdf(byte[] pdf);

    @Query(value = "SELECT  " +
            "id,tipo_doc,nome_arquivo,numero_alvara,nome_empresa, cnpj_empresa,data_emissao,data_vencimento, " +
            "(CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) as expira, observacao " +
            "FROM ARQUIVO ", nativeQuery = true)
    Page<ArquivoProjection> listarTodos(Pageable pageable);

    @Query(value = "SELECT  " +
            "id,tipo_doc,nome_arquivo,numero_alvara,nome_empresa, cnpj_empresa,data_emissao,data_vencimento, " +
            "(CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) as expira, observacao " +
            "FROM ARQUIVO ", nativeQuery = true)
    List<ArquivoProjection> listarTodosList();

    @Query(value = "SELECT " +
            "id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, cnpj_empresa, data_emissao, data_vencimento, " +
            "(CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento - CURRENT_DATE) END) as expira, observacao " +
            "FROM ARQUIVO ",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO ",
            nativeQuery = true)
    Page<ArquivoProjection> listarTodosMatcher(@Nullable Example<ArquivoProjection> example, Pageable pageable);


    @Query(value = "SELECT id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, cnpj_empresa, data_emissao, data_vencimento, " +
            "(CASE WHEN data_vencimento IS NULL THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) AS expira, observacao " +
            "FROM ARQUIVO " +
            "WHERE 1=1 " +
            "AND (:cnpjEmpresa IS NULL OR cnpj_empresa ILIKE %:cnpjEmpresa% ) " +
            "AND (:nomeEmpresa IS NULL OR nome_empresa  ILIKE %:nomeEmpresa% ) " +
            "AND (:numeroAlvara IS NULL OR numero_alvara   ILIKE %:numeroAlvara% ) " +
            "AND (:nomeArquivo IS NULL OR nome_arquivo   ILIKE %:nomeArquivo% ) ",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:cnpjEmpresa IS NULL OR cnpj_empresa ILIKE %:cnpjEmpresa% ) " +
                    "AND (:nomeEmpresa IS NULL OR nome_empresa  ILIKE %:nomeEmpresa% ) " +
                    "AND (:numeroAlvara IS NULL OR numero_alvara   ILIKE %:numeroAlvara% ) " +
                    "AND (:nomeArquivo IS NULL OR nome_arquivo   ILIKE %:nomeArquivo% ) ",
            nativeQuery = true)
    Page<ArquivoProjection> buscarArquivosPaginados(
            @Param("cnpjEmpresa") String cnpjEmpresa,
            @Param("nomeEmpresa") String nomeEmpresa,
            @Param("numeroAlvara") String numeroAlvara,
            @Param("nomeArquivo") String nomeArquivo,
            Pageable pageable);


    @Query(value = "SELECT count(*) FROM ARQUIVO ", nativeQuery = true)
    int totalDocumentos();


    @Query(value = "SELECT count(*) " +
            "FROM ARQUIVO WHERE " +
            "CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END > 60 ", nativeQuery = true)
    int totalArquivosVencerApos60Dias();


    @Query(value = "SELECT count(*) " +
            "FROM ARQUIVO WHERE " +
            "CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END > 0 and  " +
            "CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END <= 60", nativeQuery = true)
    int totalArquivosVencerEm60Dias();


    @Query(value = "SELECT count(*) FROM ARQUIVO WHERE DATA_VENCIMENTO IS NULL ", nativeQuery = true)
    int totaDocumentosSemInfo();


    @Query(value = "SELECT count(*)  FROM ARQUIVO WHERE data_vencimento is not null AND " +
            " CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END <= 0", nativeQuery = true)
    int totalVencidos();
}
