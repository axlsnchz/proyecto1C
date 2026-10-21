package mx.edu.utez.proyecto1C.service;

import mx.edu.utez.proyecto1C.dto.RequestCalculadoraDTO;
import mx.edu.utez.proyecto1C.exception.customExceptions.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    public double calculadora(RequestCalculadoraDTO payload){
        double resultado = 0;

        if (!payload.getOperacion().equals("DIVISION")
        && !payload.getOperacion().equals("SUMA")
                && !payload.getOperacion().equals("RESTA")
                && !payload.getOperacion().equals("MULTIPLICACION")){
            throw new BadRequestException("la operacion no es valida");
        }

        switch (payload.getOperacion()){
            case "MULTIPLICACION":
                resultado = payload.getNum1()* payload.getNum2();
                break;
            case "SUMA":
                resultado = payload.getNum1()+payload.getNum2();
                break;
            case "RESTA":
                resultado = payload.getNum1()- payload.getNum2();
                break;
            case "DIVISION":
                resultado = payload.getNum1()/ payload.getNum2();
                break;

        }
        return resultado;
    }
}
