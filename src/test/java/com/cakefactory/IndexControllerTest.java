package com.cakefactory;

import com.cakefactory.model.Category;
import com.cakefactory.model.InMemoryCatalog;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.Assert;

@SpringBootTest
@AutoConfigureMockMvc
class IndexControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	@DisplayName("index page returns the landing page")
	void returnsLandingPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Cake Factory")));
	}

	@Test
	public void homePage() throws Exception {
		try (final WebClient webClient = new WebClient()) {
			final HtmlPage page = webClient.getPage("http://localhost:8080/");
			Assert.isTrue("Cake Factory - Start Bootstrap Template".equals(page.getTitleText()), "Page title not present.");

			final String pageAsXml = page.asXml();
			Assert.isTrue(pageAsXml.contains("  <nav class=\"navbar navbar-expand-lg navbar-dark bg-dark fixed-top\">"), "Navbar not present");

			final String pageAsText = page.asNormalizedText();
			Assert.isTrue(pageAsText.contains("We bake cakes since 1908."), "Introductory text not present.");
		}
	}

}
