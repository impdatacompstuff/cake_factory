package com.cakefactory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
public class FooController {

    @GetMapping("/foo")
    public ModelAndView displayArticle(Map<String, Object> model) {

        model.put("title", "test");

        return new ModelAndView("foo", model);
    }
}
