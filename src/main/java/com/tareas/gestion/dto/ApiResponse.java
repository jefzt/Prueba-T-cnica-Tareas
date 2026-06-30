package com.tareas.gestion.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private boolean success;
    private String mensaje;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String mensaje, T data) {
        this.success = success;
        this.mensaje = mensaje;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(String mensaje, T data) {
        return new ApiResponse<>(true, mensaje, data);
    }

    public static <T> ApiResponse<T> error(String mensaje, T data) {
        return new ApiResponse<>(false, mensaje, data);
    }
}