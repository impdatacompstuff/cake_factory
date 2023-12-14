package com.cakefactory.controller;

import com.cakefactory.model.Catalog;
import com.cakefactory.model.Category;
import com.cakefactory.model.Pastry;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class CatalogController {

    @Autowired
    private Catalog catalog;
    
    @GetMapping("${path.catalog}")
    public ModelAndView catalog(@RequestParam Optional<String> category, Map<String, Object> model) {
        List<Pastry> pastries = catalog.getPastries();
        if (category.isPresent()) {
            pastries = pastries.stream().filter(pastry -> pastry.getCategory().equals(Category.valueOf(category.get().toUpperCase()))).toList();
        }
        model.put("pastries", pastries);

        Images images = extractImagesWithIndex(pastries);
        model.put("images", images.images);
        model.put("first_image", images.firstImage);

        String[] categories = Arrays.stream(Category.values()).map(Enum::name).map(String::toLowerCase).map(WordUtils::capitalizeFully).toArray(String[]::new);
        model.put("categories", categories);

        return new ModelAndView("catalog", model);
    }


    private Images extractImagesWithIndex(List<Pastry> pastries) {
        ArrayList<Image> images = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        counter.getAndIncrement();
        pastries.forEach(p -> {
            images.add(new Image(p.getImagePath(), counter.get()));
            counter.getAndIncrement();
        }
        );
        return new Images(images);
    }

    private class Images {
    List<Image> images;
    Image firstImage;

        public Images(List<Image> images) {
            this.images = images;
            firstImage = images.get(0);
            this.images.remove(firstImage);
        }
    }

    private class Image {
        String imagePath;
        int index;

        public Image(String imagePath, int index) {
            this.imagePath = imagePath;
            this.index = index;
        }
    }

}
