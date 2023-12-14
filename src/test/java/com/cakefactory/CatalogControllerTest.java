package com.cakefactory;

import com.cakefactory.model.InMemoryCatalog;
import com.cakefactory.model.Pastry;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InMemoryCatalog inMemoryCatalog;

    @Value ("${path.catalog}")
    private String path;
    @Value("${baseurl}")
    private String baseurl;


    @Test
    @DisplayName("Catalog is available")
    void pathIsAvailable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(path))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("The catalog controller returns a list of pastries")
    void returnPastries() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage(baseurl + path);
            Assert.isTrue("Cake Factory - Catalog".equals(page.getTitleText()), "Title does not match");

            List<HtmlElement> pastries = page.getBody().getElementsByAttribute("div", "class", "col-lg-4 col-md-6 mb-4");
            Assert.isTrue(pastries.size() == 6, "Page does not contain correct number of pastries");

            List<Pastry> expectedPastries = inMemoryCatalog.getPastries();
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
