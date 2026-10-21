package mx.edu.utez.proyecto1C.service.usuario;

import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestActualizarUsuarioDto;
import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestCrearUsuarioDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.persona.PersonaRepository;
import mx.edu.utez.proyecto1C.model.usuario.Roles;
import mx.edu.utez.proyecto1C.model.usuario.Usuario;
import mx.edu.utez.proyecto1C.repository.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final PersonaRepository personaRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(PersonaRepository personaRepository, UsuarioRepository usuarioRepository) {
        this.personaRepository = personaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("El usuario no existe"));
    }



    public Usuario createUser(RequestCrearUsuarioDto payload) {
        if (usuarioRepository.findByUsername(payload.getUsername()).isPresent()) {
            throw new BadRequestException("Ese usuario ya esta en uso");
        }

        Persona newPersona = new Persona();
        newPersona.setNombres(payload.getNombres());
        newPersona.setPrimerApellido(payload.getPrimerApellido());
        newPersona.setSegundoApellido(payload.getSegundoApellido());
        newPersona.setFechaNacimiento(payload.getFechaNacimiento());
        newPersona.setCorreo(payload.getCorreo());
        newPersona.setCurp(payload.getCurp());

        newPersona = personaRepository.save(newPersona);

        Usuario newUser = new Usuario();
        newUser.setUsername(payload.getUsername());
        newUser.setPassword(payload.getPassword());
        newUser.setEnable(true);
        newUser.setRol(Roles.valueOf(payload.getRol()));
        newUser.setDescription(payload.getDescripcion());
        newUser.setPersona(newPersona);

        return usuarioRepository.save(newUser);
    }

    public Usuario activarUsuario(Long id) {
        Usuario usuario = getUserById(id);
        usuario.setEnable(true);
        return usuarioRepository.save(usuario);
    }


    public Usuario desactivarUsuario(Long id) {
        Usuario usuario = getUserById(id);
        usuario.setEnable(false);
        return usuarioRepository.save(usuario);
    }

    public Usuario cambiarEstado(Long id) {
        Usuario usuario = getUserById(id);
        usuario.setEnable(!usuario.isEnable());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, RequestActualizarUsuarioDto payload) {
        Usuario usuario = getUserById(id);

        if (payload.getUsername() != null && !payload.getUsername().isBlank()) {
            if (!payload.getUsername().equals(usuario.getUsername())) {
                Optional<Usuario> existing = usuarioRepository.findByUsername(payload.getUsername());
                if (existing.isPresent() && existing.get().getId() != usuario.getId()) {
                    throw new BadRequestException("Ese usuario ya esta en uso");
                }
                usuario.setUsername(payload.getUsername());
            }
        }

        if (payload.getDescripcion() != null) {
            usuario.setDescription(payload.getDescripcion());
        }

        if (payload.getIsEnable() != null) {
            usuario.setEnable(payload.getIsEnable());
        }
        return usuarioRepository.save(usuario);
    }

}
