package com.cakefactory.controller;

import com.cakefactory.model.Catalog;
import com.cakefactory.model.Pastry;
import com.cakefactory.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class CatalogController {

    @Autowired
    private Catalog catalog;

    @Autowired
    CatalogService catalogService;

    @GetMapping("${path.catalog}")
    public ModelAndView catalog(@RequestParam Optional<String> category, Map<String, Object> model) {
        List<Pastry> pastries = catalogService.getPastries(category);
        model.put("pastries", pastries);

        CatalogService.Images images = catalogService.extractImagesWithIndex(pastries);
        model.put("images", images.images);
        model.put("first_image", images.firstImage);

        String[] categories = catalogService.getCategories();
        model.put("categories", categories);

        return new ModelAndView("catalog", model);
    }

}
