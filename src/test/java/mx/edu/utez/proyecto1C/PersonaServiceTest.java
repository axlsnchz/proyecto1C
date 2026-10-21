package mx.edu.utez.proyecto1C;

import mx.edu.utez.proyecto1C.controller.persona.dto.RequestActualizarPersonaDto;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.persona.PersonaRepository;
import mx.edu.utez.proyecto1C.service.persona.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    private Persona existingPersona;

    @BeforeEach
    void setUp() {
        existingPersona = new Persona(
                1L,
                "Ana",
                "Hernandez",
                "Ruiz",
                new Date(),
                "ana@correo.com",
                "HERA000101MDFR00"
        );
    }

    @Test
    void testGetPersonaById_Success() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(existingPersona));

        Persona persona = personaService.getPersonaById(1L);

        assertNotNull(persona);
        assertEquals("Ana", persona.getNombres());
        assertEquals("HERA000101MDFR00", persona.getCurp());
        verify(personaRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPersonaById_NotFound() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(BadRequestException.class, () -> personaService.getPersonaById(99L));
        assertEquals("La persona no existe", ex.getMessage());
    }

    @Test
    void testUpdatePersona_Success() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(existingPersona));
        when(personaRepository.save(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Date newDate = new Date(System.currentTimeMillis() - 1000000000L);
        RequestActualizarPersonaDto dto = new RequestActualizarPersonaDto(
                1L,
                "Ana Maria",
                "Hernandez",
                "Morales",
                newDate,
                "anamaria@correo.com",
                "HERA000101MDFR99"
        );

        Persona updated = personaService.updatePersona(1L, dto);

        assertEquals("Ana Maria", updated.getNombres());
        assertEquals("Morales", updated.getSegundoApellido());
        assertEquals("anamaria@correo.com", updated.getCorreo());
        assertEquals("HERA000101MDFR99", updated.getCurp());
        verify(personaRepository, times(1)).save(existingPersona);
    }

    @Test
    void testUpdatePersona_NotFound() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());

        RequestActualizarPersonaDto dto = new RequestActualizarPersonaDto(
                99L,
                "Test",
                "Test",
                "Test",
                new Date(),
                "test@test.com",
                "TEST123456"
        );

        BadRequestException ex = assertThrows(BadRequestException.class, () -> personaService.updatePersona(99L, dto));
        assertEquals("La persona no existe", ex.getMessage());
        verify(personaRepository, never()).save(any());
    }

    @Test
    void testGetAllPersonas() {
        when(personaRepository.findAll()).thenReturn(List.of(existingPersona));

        List<Persona> result = personaService.getAllPersonas();
        assertEquals(1, result.size());
        assertEquals("Ana", result.get(0).getNombres());
    }
}
