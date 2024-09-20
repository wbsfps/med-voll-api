package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;

public class ValidadorMedicoAtivo {

    private MedicoRepository repository;

    public void validador(DadosAgendamentoConsulta dados) {
        var medico = dados.idMedico();

        if (medico == null) {
            return;
        }

        var medicoAtivo = repository.findAtivoById(medico);

        if (!medicoAtivo) {
            throw new ValidacaoException("Consulta não pode ser agendada com médico excluído");
        }
    }
}
