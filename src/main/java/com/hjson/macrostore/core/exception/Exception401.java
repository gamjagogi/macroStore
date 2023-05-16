package com.hjson.macrostore.core.exception;

import com.hjson.macrostore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

public class Exception401 extends RuntimeException {

    public Exception401(String msg){
        super(msg);
    }

    public ResponseDTO<?> body(){
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.UNAUTHORIZED,"unAuthorized",getMessage());
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}
