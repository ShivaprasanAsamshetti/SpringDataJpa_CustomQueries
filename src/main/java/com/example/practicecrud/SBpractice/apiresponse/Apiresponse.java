package com.example.practicecrud.SBpractice.apiresponse;

import lombok.Data;
import lombok.Getter;

@Data
public class Apiresponse<T>  {
    private Integer status;
    private String message;
    private T data;
}
