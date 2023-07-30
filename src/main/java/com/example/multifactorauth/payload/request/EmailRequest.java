package com.example.multifactorauth.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {

    private String recipient;
    private String body;
    private String subject;
    private String attachment;
}
