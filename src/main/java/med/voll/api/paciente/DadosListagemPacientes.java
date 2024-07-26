package med.voll.api.paciente;

import jakarta.validation.constraints.NotNull;

public record DadosListagemPacientes(
        @NotNull
        Long id,
        String nome,
        String cpf
) {
    public DadosListagemPacientes(Paciente dados){
        this(dados.getId(), dados.getNome(), dados.getCpf());
    }
}
