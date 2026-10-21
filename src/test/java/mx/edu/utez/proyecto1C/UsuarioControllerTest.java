package mx.edu.utez.proyecto1C;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.edu.utez.proyecto1C.controller.usuario.UsuarioController;
import mx.edu.utez.proyecto1C.controller.usuario.dto.RequestActualizarUsuarioDto;
import mx.edu.utez.proyecto1C.exception.ErrorHandler;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.usuario.Roles;
import mx.edu.utez.proyecto1C.model.usuario.Usuario;
import mx.edu.utez.proyecto1C.service.usuario.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Usuario sampleUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(usuarioController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        Persona persona = new Persona(1L, "Juan", "Perez", "Lopez", new Date(), "juan@mail.com", "CURP123");
        sampleUser = new Usuario(1L, "juanperez", "Pass123", true, Roles.ADMIN, null, "Mi descripcion", persona);
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(usuarioService.getAllUsers()).thenReturn(List.of(sampleUser));

        mockMvc.perform(get("/usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("juanperez"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(usuarioService.getUserById(1L)).thenReturn(sampleUser);

        mockMvc.perform(get("/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("juanperez"))
                .andExpect(jsonPath("$.persona.nombres").value("Juan"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(usuarioService.getUserById(99L)).thenThrow(new BadRequestException("El usuario no existe"));

        mockMvc.perform(get("/usuario/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El usuario no existe"));
    }

    @Test
    void testActivateUser_Patch() throws Exception {
        sampleUser.setEnable(true);
        when(usuarioService.activarUsuario(1L)).thenReturn(sampleUser);

        mockMvc.perform(patch("/usuario/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enable").value(true));
    }

    @Test
    void testDeactivateUser_Patch() throws Exception {
        sampleUser.setEnable(false);
        when(usuarioService.desactivarUsuario(1L)).thenReturn(sampleUser);

        mockMvc.perform(patch("/usuario/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enable").value(false));
    }

    @Test
    void testToggleUserStatus() throws Exception {
        sampleUser.setEnable(false);
        when(usuarioService.cambiarEstado(1L)).thenReturn(sampleUser);

        mockMvc.perform(patch("/usuario/1/cambiar-estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enable").value(false));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        RequestActualizarUsuarioDto dto = new RequestActualizarUsuarioDto(1L, "nuevoUser", "Nueva desc", null);
        sampleUser.setUsername("nuevoUser");
        sampleUser.setDescription("Nueva desc");

        when(usuarioService.actualizarUsuario(eq(1L), any(RequestActualizarUsuarioDto.class))).thenReturn(sampleUser);

        mockMvc.perform(put("/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nuevoUser"))
                .andExpect(jsonPath("$.description").value("Nueva desc"));
    }

    @Test
    void testUpdateUser_ValidationError_BlankUsername() throws Exception {
        RequestActualizarUsuarioDto dto = new RequestActualizarUsuarioDto(1L, "", "Desc", null);

        mockMvc.perform(put("/usuario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.username").value("El username es obligatorio"));
    }
}
