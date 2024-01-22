package com.cakefactory;

import com.cakefactory.model.Catalog;
import com.cakefactory.model.Pastry;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

//@Profile("test")
@Primary
@Component
public class TestCatalog implements Catalog {

    public List<Pastry> testPastries;

    public TestCatalog(List<Pastry> testPastries) {
        this.testPastries = testPastries;
    }

    @Override
    public List<Pastry> getPastries() {
        return testPastries;
    }

    public void addPastries(Collection<Pastry> pastries) {
        testPastries.addAll(pastries);
    }
}
