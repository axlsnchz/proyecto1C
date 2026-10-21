package mx.edu.utez.proyecto1C.controller.usuario.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestActualizarUsuarioDto {

    private Long id;

    @NotBlank(message = "El username es obligatorio")
    private String username;

    private String descripcion;

    private Boolean isEnable;


}
