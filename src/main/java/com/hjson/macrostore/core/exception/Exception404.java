package com.hjson.macrostore.core.exception;

import com.hjson.macrostore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

public class Exception404 extends RuntimeException{

    public Exception404(String msg) {
        super(msg);
    }

    public ResponseDTO<?>body(){
        ResponseDTO responseDTO = new ResponseDTO(HttpStatus.NOT_FOUND,"notFound",getMessage());
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}
