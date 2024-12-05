package consumindo.api.rest_template.service;

import consumindo.api.rest_template.dto.EnderecoResultDTO;
import consumindo.api.rest_template.entity.EnderecoEntity;
import consumindo.api.rest_template.exception.ConsultaCepException;
import consumindo.api.rest_template.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    public EnderecoEntity consultaEGravaCep(String cep) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EnderecoResultDTO> response = restTemplate.getForEntity(
                String.format("https://viacep.com.br/ws/%s/json/", cep), EnderecoResultDTO.class);
        EnderecoResultDTO enderecoResultDTO = response.getBody();

        if (enderecoResultDTO != null) {
            EnderecoEntity enderecoEntity = new EnderecoEntity();
            enderecoEntity.setCep(enderecoResultDTO.getCep());
            enderecoEntity.setLogradouro(enderecoResultDTO.getLogradouro());
            enderecoEntity.setComplemento(enderecoResultDTO.getComplemento());
            enderecoEntity.setBairro(enderecoResultDTO.getBairro());
            enderecoEntity.setLocalidade(enderecoResultDTO.getLocalidade());
            enderecoEntity.setUf(enderecoResultDTO.getUf());
            enderecoEntity.setEstado(enderecoResultDTO.getEstado());
            enderecoEntity.setRegiao(enderecoResultDTO.getRegiao());
            enderecoEntity.setGia(enderecoResultDTO.getGia());
            enderecoEntity.setDdd(enderecoResultDTO.getDdd());
            enderecoEntity.setSiafi(enderecoResultDTO.getSiafi());
            return enderecoRepository.save(enderecoEntity);
        }
        throw new ConsultaCepException("Não foi possível consultar o CEP: " + cep);
    }

    public EnderecoEntity buscarPorId(Integer id) {
        return enderecoRepository.findById(id).orElseThrow(() -> new ConsultaCepException("Endereço não encontrado com ID: " + id));
    }
}
