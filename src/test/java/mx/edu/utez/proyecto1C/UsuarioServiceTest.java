package mx.edu.utez.proyecto1C;

import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestActualizarUsuarioDto;
import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestCrearUsuarioDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.persona.PersonaRepository;
import mx.edu.utez.proyecto1C.model.usuario.Roles;
import mx.edu.utez.proyecto1C.model.usuario.Usuario;
import mx.edu.utez.proyecto1C.repository.usuario.UsuarioRepository;
import mx.edu.utez.proyecto1C.service.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario existingUser;
    private Persona existingPersona;

    @BeforeEach
    void setUp() {
        existingPersona = new Persona(
                1L,
                "Juan",
                "Perez",
                "Lopez",
                new Date(),
                "juan@correo.com",
                "PELJ000101HDFR01"
        );

        existingUser = new Usuario();
        existingUser.setId(1L);
        existingUser.setUsername("juanperez");
        existingUser.setPassword("Secret123");
        existingUser.setEnable(true);
        existingUser.setRol(Roles.ESTUDIANTE);
        existingUser.setDescription("Descripcion inicial");
        existingUser.setPersona(existingPersona);
    }

    @Test
    void testGetUserById_Success() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        Usuario result = usuarioService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("juanperez", result.getUsername());
        assertEquals("Juan", result.getPersona().getNombres());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> usuarioService.getUserById(99L));
        assertEquals("El usuario no existe", ex.getMessage());
    }

    @Test
    void testActivateUser() {
        existingUser.setEnable(false);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario updated = usuarioService.activarUsuario(1L);

        assertTrue(updated.isEnable());
        verify(usuarioRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeactivateUser() {
        existingUser.setEnable(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario updated = usuarioService.desactivarUsuario(1L);

        assertFalse(updated.isEnable());
        verify(usuarioRepository, times(1)).save(existingUser);
    }

    @Test
    void testToggleUserStatus() {
        existingUser.setEnable(true);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario updated = usuarioService.cambiarEstado(1L);
        assertFalse(updated.isEnable());

        Usuario toggledAgain = usuarioService.cambiarEstado(1L);
        assertTrue(toggledAgain.isEnable());
    }

    @Test
    void testUpdateUser_PreservesPersonaAndOtherData() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.findByUsername("nuevoUsername")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RequestActualizarUsuarioDto dto = new RequestActualizarUsuarioDto(
                1L,
                "nuevoUsername",
                "Nueva descripcion",
                null
        );

        Usuario result = usuarioService.actualizarUsuario(1L, dto);

        assertEquals("nuevoUsername", result.getUsername());
        assertEquals("Nueva descripcion", result.getDescription());
        // Verify relationship and sensitive data were preserved
        assertEquals(existingPersona, result.getPersona());
        assertEquals(1L, result.getPersona().getId());
        assertEquals("Secret123", result.getPassword());
        assertEquals(Roles.ESTUDIANTE, result.getRol());
        assertTrue(result.isEnable());
        verify(usuarioRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_DuplicateUsernameThrowsBadRequest() {
        Usuario anotherUser = new Usuario();
        anotherUser.setId(2L);
        anotherUser.setUsername("usuarioExistente");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.findByUsername("usuarioExistente")).thenReturn(Optional.of(anotherUser));

        RequestActualizarUsuarioDto dto = new RequestActualizarUsuarioDto(
                1L,
                "usuarioExistente",
                "Descripcion",
                null
        );

        BadRequestException ex = assertThrows(BadRequestException.class, () -> usuarioService.actualizarUsuario(1L, dto));
        assertEquals("Ese usuario ya esta en uso", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_SameUsername_DoesNotThrow() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RequestActualizarUsuarioDto dto = new RequestActualizarUsuarioDto(
                1L,
                "juanperez", // same as existing
                "Descripcion cambiada",
                null
        );

        Usuario result = usuarioService.actualizarUsuario(1L, dto);
        assertEquals("juanperez", result.getUsername());
        assertEquals("Descripcion cambiada", result.getDescription());
        verify(usuarioRepository, times(1)).save(existingUser);
    }

    @Test
    void testCreateUser_Success() {
        RequestCrearUsuarioDto dto = new RequestCrearUsuarioDto(
                "nuevouser",
                "Password123",
                "ADMIN",
                "Desc",
                "Carlos",
                "Gomez",
                "Diaz",
                new Date(),
                "carlos@mail.com",
                "CURP123"
        );

        when(usuarioRepository.findByUsername("nuevouser")).thenReturn(Optional.empty());
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> {
            Persona p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario created = usuarioService.createUser(dto);

        assertNotNull(created);
        assertEquals("nuevouser", created.getUsername());
        assertEquals("Desc", created.getDescription());
        assertNotNull(created.getPersona());
        assertEquals("Carlos", created.getPersona().getNombres());
        assertEquals("CURP123", created.getPersona().getCurp());
        assertTrue(created.isEnable());
    }
}
