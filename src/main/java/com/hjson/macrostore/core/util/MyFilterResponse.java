package com.hjson.macrostore.core.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjson.macrostore.dto.ResponseDTO;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilterResponse {

    public static void unAuthrized(HttpServletResponse resp, Exception e)throws IOException {
        resp.setStatus(401);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDTO<?>responseDTO = new ResponseDTO<>(HttpStatus.UNAUTHORIZED,"인증 실패", e.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        String responseBody = mapper.writeValueAsString(responseDTO);
        resp.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse resp,Exception e) throws IOException {
        resp.setStatus(403);
        resp.setContentType("application/json; charset=utf-8");
        ResponseDTO< ? > responseDTO = new ResponseDTO<>(HttpStatus.FORBIDDEN,"권한 없음",e.getMessage());
        ObjectMapper mapper = new ObjectMapper();
        String responseBody = mapper.writeValueAsString(responseDTO);
        resp.getWriter().println(responseBody);
    }
}
