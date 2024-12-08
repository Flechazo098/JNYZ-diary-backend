package com.flechazo.jnyzdairy.dto;

import lombok.Data;

@Data
public class CaptchaResult {
    private String code;
    private byte[] imageBytes;

    public CaptchaResult(String code, byte[] imageBytes) {
        this.code = code;
        this.imageBytes = imageBytes;
    }
} 