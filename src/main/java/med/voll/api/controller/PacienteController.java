    package med.voll.api.controller;

    import io.swagger.v3.oas.annotations.security.SecurityRequirement;
    import jakarta.transaction.Transactional;
    import jakarta.validation.Valid;
    import med.voll.api.domain.paciente.*;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.web.PageableDefault;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.util.UriComponentsBuilder;

    @RestController
    @RequestMapping("pacientes")
    @SecurityRequirement(name = "bearer-key")
    public class PacienteController {
        @Autowired
        private PacienteRepository repository;

        @PostMapping
        @Transactional
        public ResponseEntity cadastroPacientes(@RequestBody @Valid DadosPaciente dados, UriComponentsBuilder uriBuilder) {
            var paciente = new Paciente(dados);
            repository.save(paciente);
            var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
            return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
        }

        @GetMapping
        public ResponseEntity<Page<DadosListagemPacientes>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable pageble) {
            var page = repository.findAllByAtivoTrue(pageble).map(DadosListagemPacientes::new);
            return ResponseEntity.ok(page);
        }

        @PutMapping
        @Transactional
        public ResponseEntity atualizarInformacoes(@PageableDefault(size = 10, sort = {"nome"}) @RequestBody @Valid DadosAtualizacaoPaciente dados) {
            var paciente = repository.getReferenceById(dados.id());
            paciente.atualizarInformacoes(dados);
            return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
        }

        @DeleteMapping("/{id}")
        @Transactional
        public ResponseEntity deletar(@PathVariable Long id) {
            var paciente = repository.getReferenceById(id);
            paciente.excluir();
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity detalhamento(@PathVariable Long id) {
            var paciente = repository.getReferenceById(id);
            return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
        }
    }
