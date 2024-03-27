package br.com.alvara.utils;

import br.com.alvara.extrator.Pdf;
import br.com.alvara.model.entity.Arquivo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@Component
public abstract class FileUtils {


    private FileUtils() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    public static Arquivo convertPartToArquivo(Part arquivoPart) {
        Pdf pdf = new Pdf();
        try (InputStream is = arquivoPart.getInputStream()) {
            byte[] bytes = new byte[(int) arquivoPart.getSize()];
            IOUtils.readFully(is, bytes);

            File arqPdf = byteTofile(bytes, arquivoPart.getSubmittedFileName());
            Arquivo arquivoObtido = pdf.lerPdf(arqPdf, bytes);

            if (arqPdf != null && arqPdf.exists()) {
                excluirArquivoFisico(arqPdf);
            }

            return arquivoObtido;

        } catch (IOException e) {
            LOG.error(String.format("Erro no método converterPdfParaArquivo. %s", e.getMessage()));
        }
        return null;
    }

    private static void excluirArquivoFisico(File arqPdf) {
        Path pdfPath = arqPdf.toPath();
        try {
            Files.delete(pdfPath);
            LOG.info("Arquivo excluído com sucesso.");
        } catch (IOException e) {
            LOG.error(String.format("Falha ao excluir o arquivo. %s", e.getMessage()));
        }
    }

    private static File byteTofile(byte[] bytesArquivo, String nome) {
        File pdfExistente = new File(nome);
        if (pdfExistente.exists()) {
            Path pdfPath = pdfExistente.toPath();
            try {
                Files.delete(pdfPath);
                LOG.info("Arquivo excluído com sucesso.");
            } catch (IOException e) {
                LOG.error("Falha ao excluir o arquivo.");
            }
        }
        File file;
        try {
            file = new File(nome);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytesArquivo);
            }
            return file;
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

}
