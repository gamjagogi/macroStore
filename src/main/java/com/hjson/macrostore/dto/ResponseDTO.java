package com.hjson.macrostore.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDTO<T> {

    private Integer status;

    private String msg;

    private T data;

    public ResponseDTO(){
        this.status = HttpStatus.OK.value();
        this.msg = "성공";
    }

    public ResponseDTO(T data){
        this.status = HttpStatus.OK.value();
        this.msg = "성공";
        this.data = data;
    }

    // 실패시 사용.
    public ResponseDTO(HttpStatus httpStatus, String msg, T data){
        this.status = httpStatus.value();
        this.msg = msg;
        this.data = data;
    }
}
