package mx.edu.utez.proyecto1C.controller.usuario.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCrearUsuarioDto {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número"
    )
    @NotBlank(message = "La contraseña  es obligatoria")
    private String password;

    @Pattern(
            regexp = "^(ADMIN|GESTOR|ESTUDIANTE)$",
            message = "El rol debe ser ADMIN, GESTOR o ESTUDIANTE"
    )
    @NotBlank(message = "El rol es obligatorio")
    private String rol;


    private String descripcion;


    @NotBlank(message = "El nombre es obligario")
    private String nombres;

    @NotBlank(message = "El primer apellido es obligario")
    private String primerApellido;

    private String segundoApellido;


    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo es invalido")
    private String correo;

    @NotBlank(message = "El curp es obligatorio")
    private String curp;
}
