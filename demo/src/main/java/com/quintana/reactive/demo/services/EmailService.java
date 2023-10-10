package com.quintana.reactive.demo.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class EmailService {

    @Data
    @Builder
    public static class Email{
        private String to;
        private String from;
        private String content;

        @Override
        public String toString() {
            return "to: " + to +" from: " + from;
        }
    }

    public void sendEmail(Email email){
        System.out.println(email);
    }
}
