package com.hjson.macrostore.core.exception;

import com.hjson.macrostore.dto.ResponseDTO;
import com.hjson.macrostore.dto.valid.ValidDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Exception400 extends RuntimeException{
    private String key;

    private String value;

    public Exception400(String key, String value){
        super(value);
        this.key = key;
        this.value = value;
    }

    public ResponseDTO<?>body(){
        ValidDTO validDTO = new ValidDTO(key,value);
        ResponseDTO responseDTO = new ResponseDTO<>(HttpStatus.BAD_REQUEST,"badRequest",validDTO);
        return responseDTO;
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}
