package consumindo.api.rest_template.service;

import consumindo.api.rest_template.dto.EnderecoResultDTO;
import consumindo.api.rest_template.entity.EnderecoEntity;
import consumindo.api.rest_template.exception.ConsultaCepException;
import consumindo.api.rest_template.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve Salvar o Endereço")
    public void consultaEGravaCep() {
        String cep = "12345678";
        EnderecoResultDTO enderecoResultDTO = new EnderecoResultDTO();
        enderecoResultDTO.setCep(cep);
        enderecoResultDTO.setLogradouro("Rua Teste");
        ResponseEntity<EnderecoResultDTO> response = new ResponseEntity<>(enderecoResultDTO, HttpStatus.OK);

        when(restTemplate.getForEntity(any(String.class), eq(EnderecoResultDTO.class))).thenReturn(response);
        when(enderecoRepository.save(any(EnderecoEntity.class))).thenReturn(new EnderecoEntity());

        EnderecoEntity endereco = enderecoService.consultaEGravaCep(cep);

        assertNotNull(endereco);
        verify(enderecoRepository, times(1)).save(any(EnderecoEntity.class));
    }

    @Test
    @DisplayName("Quando o ID existe, deve retornar o endereço")
    void buscarPorId_IdExistente_DeveRetornarEndereco() {

        Integer id = 1;
        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setId(id);
        when(enderecoRepository.findById(id)).thenReturn(Optional.of(enderecoEntity));

        EnderecoEntity result = enderecoService.buscarPorId(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Quando o ID não existe, deve lançar exceção")
    void buscarPorId_IdNaoExistente_DeveLancarExcecao() {
        Integer id = 1;
        when(enderecoRepository.findById(id)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ConsultaCepException.class, () -> {
            enderecoService.buscarPorId(id);
        });

        String expectedMessage = "Endereço não encontrado com ID: " + id;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
