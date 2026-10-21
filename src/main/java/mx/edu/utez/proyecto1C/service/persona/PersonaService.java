package mx.edu.utez.proyecto1C.service.persona;

import mx.edu.utez.proyecto1C.controller.persona.dto.RequestActualizarPersonaDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.persona.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    public Persona getPersonaById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("La persona no existe"));
    }

    public Persona findById(Long id) {
        return getPersonaById(id);
    }

    public Persona obtenerPersonaPorId(Long id) {
        return getPersonaById(id);
    }

    public Persona createPersona(RequestActualizarPersonaDto payload) {
        Persona persona = new Persona();
        persona.setNombres(payload.getNombres());
        persona.setPrimerApellido(payload.getPrimerApellido());
        persona.setSegundoApellido(payload.getSegundoApellido());
        persona.setFechaNacimiento(payload.getFechaNacimiento());
        persona.setCorreo(payload.getCorreo());
        persona.setCurp(payload.getCurp());

        return personaRepository.save(persona);
    }

    public Persona updatePersona(Long id, RequestActualizarPersonaDto payload) {
        Persona persona = getPersonaById(id);

        persona.setNombres(payload.getNombres());
        persona.setPrimerApellido(payload.getPrimerApellido());
        persona.setSegundoApellido(payload.getSegundoApellido());
        persona.setFechaNacimiento(payload.getFechaNacimiento());
        persona.setCorreo(payload.getCorreo());
        persona.setCurp(payload.getCurp());

        return personaRepository.save(persona);
    }

    public Persona actualizarPersona(Long id, RequestActualizarPersonaDto payload) {
        return updatePersona(id, payload);
    }
}
