package mx.edu.utez.proyecto1C.controller;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1C.dto.RequestBodyDTO;
import mx.edu.utez.proyecto1C.dto.RequestCalculadoraDTO;
import mx.edu.utez.proyecto1C.service.MyService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})//todos los origenes
@RequestMapping("/my-services")
public class MyController {

    private final MyService service;

    public MyController (MyService service) {
        this.service = service;
    }

    @GetMapping
    public String miPrimerServicio(){
        return "helloWorld";
    }

    @GetMapping("/servicio2")
    public String servicio2(){
        return "segundoServicio";
    }

    @PostMapping
    public String servicio3(){
        return "este es el servicio 3";
    }

    //servicio4

    @GetMapping("/path/{id}")
    public String pathVariable(@PathVariable String id){
        return "el path variable es: "+ id;
    }

    @PostMapping("/body")
    public ResponseEntity<RequestBodyDTO> body(@RequestBody @Valid RequestBodyDTO payload){
        System.out.println(payload.getEdad());
        System.out.println(payload.getNombre());
        System.out.println(payload.getEmail());
        System.out.println(payload.getCurp());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(payload);
    }

    @PostMapping("/calculadora")
    public double calculadora(@RequestBody @Valid RequestCalculadoraDTO payload){
        return service.calculadora(payload);
    }

}


