package consumindo.api.rest_template.controller;

import consumindo.api.rest_template.entity.EnderecoEntity;
import consumindo.api.rest_template.exception.ConsultaCepException;
import consumindo.api.rest_template.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {
    @Autowired
    private EnderecoService enderecoService;

    @Operation(summary = "Consulta endereço pelo CEP")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @GetMapping("/consulta/{cep}")
    public ResponseEntity<EnderecoEntity> consultaCep(@PathVariable("cep") String cep) {
        try {
            EnderecoEntity enderecoEntity = enderecoService.consultaEGravaCep(cep);
            return ResponseEntity.ok(enderecoEntity);
        } catch (ConsultaCepException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Busca endereço por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado com o ID fornecido"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoEntity> buscarPorId
            (@PathVariable("id") Integer id) {
        try {
            EnderecoEntity enderecoEntity = enderecoService.buscarPorId(id);
            return ResponseEntity.ok(enderecoEntity);
        } catch (ConsultaCepException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
