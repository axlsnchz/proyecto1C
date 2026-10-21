package mx.edu.utez.proyecto1C.controller.usuario;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestActualizarUsuarioDto;
import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestCrearUsuarioDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.usuario.Usuario;
import mx.edu.utez.proyecto1C.service.usuario.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/usuario", "/usuarios"})
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Usuario> createUser(@RequestBody @Valid RequestCrearUsuarioDto payload) {
        return ResponseEntity
                .status(201)
                .body(this.usuarioService.createUser(payload));
    }

    @RequestMapping(value = {"/{id}/activar", "/activar/{id}"}, method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> activateUser(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.activarUsuario(id));
    }

    @RequestMapping(value = "/activar", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> activateUserByParam(@RequestParam Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.activarUsuario(id));
    }

    @RequestMapping(value = {"/{id}/desactivar", "/desactivar/{id}"}, method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> deactivateUser(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.desactivarUsuario(id));
    }

    @RequestMapping(value = "/desactivar", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> deactivateUserByParam(@RequestParam Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.desactivarUsuario(id));
    }

    @RequestMapping(value = {"/{id}/cambiar-estado", "/{id}/status"}, method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.cambiarEstado(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deleteUser(@PathVariable Long id) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.desactivarUsuario(id));
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Usuario> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid RequestActualizarUsuarioDto payload
    ) {
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.actualizarUsuario(id, payload));
    }

    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<Usuario> updateUserWithBodyId(
            @RequestBody @Valid RequestActualizarUsuarioDto payload
    ) {
        if (payload.getId() == null) {
            throw new BadRequestException("El id del usuario es obligatorio");
        }
        return ResponseEntity
                .status(200)
                .body(this.usuarioService.actualizarUsuario(payload.getId(), payload));
    }
}
