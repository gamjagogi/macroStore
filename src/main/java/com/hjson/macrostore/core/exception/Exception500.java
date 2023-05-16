package com.hjson.macrostore.core.exception;

import com.hjson.macrostore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

public class Exception500 extends RuntimeException{

    public Exception500(String msg){
        super(msg);
    }

    public ResponseDTO<?>body(){
        ResponseDTO<?>responseDTO = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR,"internalServerError",getMessage());
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
