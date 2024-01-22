package com.cakefactory.service;

import com.cakefactory.model.Catalog;
import com.cakefactory.model.Category;
import com.cakefactory.model.Pastry;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CatalogService {

    @Autowired
    private Catalog catalog;

    public List<Pastry> getPastries(Optional<String> category) {
        List<Pastry> pastries = catalog.getPastries();
        if (category.isPresent()) {
            pastries = pastries.stream().filter(pastry -> pastry.getCategory().equals(Category.valueOf(category.get().toUpperCase()))).toList();
        }
        return pastries;
    }

    public String[] getCategories() {
        return Arrays.stream(Category.values()).map(Enum::name).map(String::toLowerCase).map(WordUtils::capitalizeFully).toArray(String[]::new);
    }

    public Images extractImagesWithIndex(List<Pastry> pastries) {
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

    //TODO: hier lassen oder in eine View auslagern?
    public class Images {
        public List<Image> images;
        public Image firstImage;

        public Images(List<Image> images) {
            this.images = images;
            firstImage = images.get(0);
            this.images.remove(firstImage);
        }
    }

    private class Image {
        String imagePath;
        int index;

        private Image(String imagePath, int index) {
            this.imagePath = imagePath;
            this.index = index;
        }
    }
}
