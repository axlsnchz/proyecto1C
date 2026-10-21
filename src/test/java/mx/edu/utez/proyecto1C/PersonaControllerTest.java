package mx.edu.utez.proyecto1C;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.edu.utez.proyecto1C.controller.persona.PersonaController;
import mx.edu.utez.proyecto1C.controller.persona.dto.RequestActualizarPersonaDto;
import mx.edu.utez.proyecto1C.exception.ErrorHandler;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.service.persona.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PersonaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonaService personaService;

    @InjectMocks
    private PersonaController personaController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Persona samplePersona;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(personaController)
                .setControllerAdvice(new ErrorHandler())
                .build();

        samplePersona = new Persona(1L, "Maria", "Lopez", "Garcia", new Date(), "maria@mail.com", "CURP123");
    }

    @Test
    void testGetAllPersonas() throws Exception {
        when(personaService.getAllPersonas()).thenReturn(List.of(samplePersona));

        mockMvc.perform(get("/persona"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombres").value("Maria"));
    }

    @Test
    void testGetPersonaById_Success() throws Exception {
        when(personaService.getPersonaById(1L)).thenReturn(samplePersona);

        mockMvc.perform(get("/persona/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombres").value("Maria"));
    }

    @Test
    void testGetPersonaById_NotFound() throws Exception {
        when(personaService.getPersonaById(99L)).thenThrow(new BadRequestException("La persona no existe"));

        mockMvc.perform(get("/persona/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("La persona no existe"));
    }

    @Test
    void testUpdatePersona_Success() throws Exception {
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd").parse("1995-05-15");
        RequestActualizarPersonaDto dto = new RequestActualizarPersonaDto(
                1L,
                "Maria Fernanda",
                "Lopez",
                "Garcia",
                birthDate,
                "fernanda@mail.com",
                "CURP123456"
        );

        samplePersona.setNombres("Maria Fernanda");
        samplePersona.setCorreo("fernanda@mail.com");

        when(personaService.updatePersona(eq(1L), any(RequestActualizarPersonaDto.class))).thenReturn(samplePersona);

        mockMvc.perform(put("/persona/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Maria Fernanda"))
                .andExpect(jsonPath("$.correo").value("fernanda@mail.com"));
    }

    @Test
    void testUpdatePersona_ValidationError_BlankName() throws Exception {
        RequestActualizarPersonaDto dto = new RequestActualizarPersonaDto(
                1L,
                "",
                "Lopez",
                "Garcia",
                new Date(System.currentTimeMillis() - 1000000000L),
                "maria@mail.com",
                "CURP123"
        );

        mockMvc.perform(put("/persona/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombres").value("El nombre es obligatorio"));
    }
}
