package br.com.alvara.model.repository;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {

    @Query(value = "SELECT  " +
            "id,tipo_doc,nome_arquivo,numero_alvara,nome_empresa, cnpj_empresa,data_emissao,data_vencimento, " +
            "(CASE WHEN data_vencimento is null THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) as expira, observacao, status_documento " +
            "FROM ARQUIVO ", nativeQuery = true)
    List<ArquivoProjection> listarTodosList();


    @Query(value =
            "SELECT id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, " +
                    "cnpj_empresa, data_emissao, data_vencimento, " +
                    "(CASE WHEN data_vencimento IS NULL THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) " +
                    "AS expira, observacao, status_documento " +
                    "FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :tipo is null OR tipo_doc = :tipo )" +
                    "AND ( :status is null OR status_documento = :status )",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :tipo is null OR tipo_doc = :tipo )" +
                    "AND ( :status is null OR status_documento = :status )",
            nativeQuery = true)
    Page<ArquivoProjection> buscarArquivosPaginadosFilterComTipoDocEStatusDoc(
            @Param("nome") String empresa,
            @Param("numero") String numero,
            @Param("cnpj") String cnpj,
            @Param("tipo") int tipo,
            @Param("status") int status,
            Pageable pageable);

    @Query(value =
            "SELECT id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, " +
                    "cnpj_empresa, data_emissao, data_vencimento, " +
                    "(CASE WHEN data_vencimento IS NULL THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) " +
                    "AS expira, observacao, status_documento " +
                    "FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :tipo is null OR tipo_doc = :tipo )",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :tipo is null OR tipo_doc = :tipo )",
            nativeQuery = true)
    Page<ArquivoProjection> buscarArquivosPaginadosFilterComTipoDoc(
            @Param("nome") String empresa,
            @Param("numero") String numero,
            @Param("cnpj") String cnpj,
            @Param("tipo") int tipo,
            Pageable pageable);


    @Query(value =
            "SELECT id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, " +
                    "cnpj_empresa, data_emissao, data_vencimento, " +
                    "(CASE WHEN data_vencimento IS NULL THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) " +
                    "AS expira, observacao, status_documento " +
                    "FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :status is null OR status_documento = :status )",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) " +
                    "AND ( :status is null OR status_documento = :status )",
            nativeQuery = true)
    Page<ArquivoProjection> buscarArquivosPaginadosFilterComStatusDoc(
            @Param("nome") String empresa,
            @Param("numero") String numero,
            @Param("cnpj") String cnpj,
            @Param("status") int status,
            Pageable pageable);

    @Query(value =
            "SELECT id, tipo_doc, nome_arquivo, numero_alvara, nome_empresa, " +
                    "cnpj_empresa, data_emissao, data_vencimento, " +
                    "(CASE WHEN data_vencimento IS NULL THEN 0 ELSE (data_vencimento-CURRENT_DATE) END) " +
                    "AS expira, observacao, status_documento  " +
                    "FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) ",
            countQuery = "SELECT COUNT(*) FROM ARQUIVO " +
                    "WHERE 1=1 " +
                    "AND (:nome IS NULL OR nome_empresa ILIKE %:nome% ) " +
                    "AND (:numero IS NULL OR numero_alvara ILIKE %:numero% ) " +
                    "AND (:cnpj IS NULL OR cnpj_empresa ILIKE %:cnpj% ) ",
            nativeQuery = true)
    Page<ArquivoProjection> buscarArquivosPaginadosFilterSemTipoDoc(
            @Param("nome") String empresa,
            @Param("numero") String numero,
            @Param("cnpj") String cnpj,
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
