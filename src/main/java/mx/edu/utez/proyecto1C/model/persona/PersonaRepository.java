package mx.edu.utez.proyecto1C.model.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface PersonaRepository extends JpaRepository<Persona, Long>{

    }
