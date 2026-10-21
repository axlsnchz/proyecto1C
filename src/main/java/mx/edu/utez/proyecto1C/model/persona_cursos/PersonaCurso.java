package mx.edu.utez.proyecto1C.model.persona_cursos;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.proyecto1C.model.persona.Persona;
import mx.edu.utez.proyecto1C.model.cursos.Curso;


@Entity
@Table(name = "persona_curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonaCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double calificacion;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "personas_id")
    private Persona persona;

    @ManyToOne
    @JoinColumn(name = "cursos_id")
    private Curso curso;

}
