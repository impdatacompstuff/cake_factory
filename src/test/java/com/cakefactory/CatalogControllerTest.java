package com.cakefactory;

import com.cakefactory.model.Catalog;
import com.cakefactory.model.Category;
import com.cakefactory.model.InMemoryCatalog;
import com.cakefactory.model.Pastry;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value ("${path.catalog}")
    private String path;
    @Value("${baseurl}")
    private String baseurl;

    @Autowired
    TestCatalog catalog;

    @Test
    @DisplayName("Catalog is available")
    void pathIsAvailable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("The catalog controller returns a list of pastries")
    void returnPastries() throws IOException {

        //TODO: mock returned elements
        CurrencyUnit pound = Monetary.getCurrency("GBP");

        Pastry pastry1 = new Pastry("pastry1", Category.DANISH, Money.of(2.00, pound), "testpath", "delicious", 5);
        Pastry pastry2 = new Pastry("pastry2", Category.CAKES, Money.of(3.00, pound), "testpath", "delicious", 5);
        Pastry pastry3 = new Pastry("pastry3", Category.COOKIES, Money.of(4.00, pound), "testpath", "delicious", 5);

        List<Pastry> expectedPastries = List.of(pastry1, pastry2, pastry3);
        //TODO wofür brauche ich den? Wie entsteht die Verknüpfung zwischen TestCatalog und App?
        //Catalog catalog = new TestCatalog(expectedPastries);

        //Versuche, Applikation zu starten
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CakeFactoryApplication.class);
        catalog.addPastries(expectedPastries);

        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(baseurl + path);
            Assert.isTrue("Cake Factory - Catalog".equals(page.getTitleText()), "Title does not match");

            List<HtmlElement> pastries = page.getBody().getElementsByAttribute("div", "class", "col-lg-4 col-md-6 mb-4");
            Assert.isTrue(pastries.size() == expectedPastries.size(), "Page does not contain correct number of pastries");

            ArrayList<String> expectedPastryTitles = new ArrayList<>();
            expectedPastries.forEach(p -> expectedPastryTitles.add(p.getTitle()));

            ArrayList<String> actualPastryTitles = new ArrayList<>();
            pastries.forEach(p -> {
                actualPastryTitles.add(p.getElementsByAttribute("h4", "class", "card-title")
                        .get(0).getElementsByAttribute("a", "href", "#")
                        .get(0)
                        .getFirstChild()
                        .getNodeValue())
            ;});

            actualPastryTitles.sort(null);
            System.out.println(actualPastryTitles);
            expectedPastryTitles.sort(null);
            Assertions.assertIterableEquals(expectedPastryTitles, actualPastryTitles);
        }
    }




}
