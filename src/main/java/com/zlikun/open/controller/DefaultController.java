package com.zlikun.open.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zlikun
 * @date 2018-09-20 17:49
 */
@RestController
public class DefaultController {

    @GetMapping("/")
    public Object index() {
        return "正在开发中。。。";
    }

}
