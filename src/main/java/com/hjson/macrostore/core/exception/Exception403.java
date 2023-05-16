package com.hjson.macrostore.core.exception;

import com.hjson.macrostore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

public class Exception403 extends RuntimeException{

    public Exception403(String msg) {
        super(msg);
    }

    public ResponseDTO<?>body(){
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.FORBIDDEN,"forbidden",getMessage());
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.FORBIDDEN;
    }
}
