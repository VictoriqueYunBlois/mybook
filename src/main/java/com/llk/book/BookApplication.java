package com.llk.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

@Slf4j
@SpringBootApplication
public class BookApplication {

    public static void main(String[] args) {

        SpringApplication.run(BookApplication.class, args);
    }

}
