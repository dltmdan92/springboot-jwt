package com.seungmoo.spring_jwt.runner;

import com.seungmoo.spring_jwt.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final InitService initService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initService.init();
    }

}
