package mx.edu.utez.proyecto1C.controller.persona;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1C.controller.persona.dto.RequestActualizarPersonaDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.service.persona.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/persona", "/personas"})
@CrossOrigin("*")
public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    @GetMapping
    public ResponseEntity<List<Persona>> getAllPersonas() {
        return ResponseEntity
                .status(200)
                .body(this.personaService.getAllPersonas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.personaService.getPersonaById(id));
    }

    @PostMapping
    public ResponseEntity<Persona> createPersona(@RequestBody @Valid RequestActualizarPersonaDto payload) {
        return ResponseEntity
                .status(201)
                .body(this.personaService.createPersona(payload));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Persona> updatePersona(
            @PathVariable Long id,
            @RequestBody @Valid RequestActualizarPersonaDto payload
    ) {
        return ResponseEntity
                .status(200)
                .body(this.personaService.updatePersona(id, payload));
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Persona> updatePersonaWithBodyId(
            @RequestBody @Valid RequestActualizarPersonaDto payload
    ) {
        if (payload.getId() == null) {
            throw new BadRequestException("El id de la persona es obligatorio");
        }
        return ResponseEntity
                .status(200)
                .body(this.personaService.updatePersona(payload.getId(), payload));
    }
}
