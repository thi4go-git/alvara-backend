package br.com.alvara.rest.mapper;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.rest.dto.ArquivoResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArquivoMapper {

    @Autowired
    private ModelMapper modelMapper;

    public ArquivoResponseDTO arquivoToArquivoResponseDTO(Arquivo arquivo) {
        return modelMapper.map(arquivo, ArquivoResponseDTO.class);
    }

}
