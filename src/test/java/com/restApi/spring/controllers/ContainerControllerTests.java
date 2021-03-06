package com.restApi.spring.controllers;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import com.restApi.spring.service.BeerService;
import com.restApi.spring.service.ContainerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ContainerControllerTests {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private List<Containers> containersList = new ArrayList<>();

	@Autowired
	private ContainerService containerService;

	@Autowired
	private BeerService beerService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny()
				.orElse(null);

		assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		this.containerService.deleteAllContainers();

		beerService.createBeersDefault();

		Containers containers = new Containers();
		containers.setDescription("Containers 1");
		containers.setTemperature(BigDecimal.valueOf(5.0));
		containerService.saveContainer(containers);

		List<Beer> beerList = beerService.findAllBeers();

		for (Beer b: beerList) {
			b.setContainers(containers);
			beerService.updateBeer(b);
		}

		containers.setBeers(beerList);
		this.containersList.add(containerService.updateContainer(containers));
	}

	@Test
	public void containerNotFound() throws Exception {
		mockMvc.perform(get("/api/container/-1")
				.content("")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void readSingleContainer() throws Exception {
		mockMvc.perform(get("/api/container/" + this.containersList.get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.containersList.get(0).getId().intValue())))
				.andExpect(jsonPath("$.temperature", is(this.containersList.get(0).getTemperature().doubleValue())))
				.andExpect(jsonPath("$.beers[0].id", is(this.containersList.get(0).getBeers().get(0).getId().intValue())))
				.andExpect(jsonPath("$.beers[0].min", is(this.containersList.get(0).getBeers().get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[0].max", is(this.containersList.get(0).getBeers().get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[0].description", is(this.containersList.get(0).getBeers().get(0).getDescription())))
				.andExpect(jsonPath("$.beers[0].status", is(this.containersList.get(0).getBeers().get(0).getStatus())))
				.andExpect(jsonPath("$.beers[1].id", is(this.containersList.get(0).getBeers().get(1).getId().intValue())))
				.andExpect(jsonPath("$.beers[1].min", is(this.containersList.get(0).getBeers().get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[1].max", is(this.containersList.get(0).getBeers().get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[1].description", is(this.containersList.get(0).getBeers().get(1).getDescription())))
				.andExpect(jsonPath("$.beers[1].status", is(this.containersList.get(0).getBeers().get(1).getStatus())))
				.andExpect(jsonPath("$.beers[2].id", is(this.containersList.get(0).getBeers().get(2).getId().intValue())))
				.andExpect(jsonPath("$.beers[2].min", is(this.containersList.get(0).getBeers().get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[2].max", is(this.containersList.get(0).getBeers().get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[2].description", is(this.containersList.get(0).getBeers().get(2).getDescription())))
				.andExpect(jsonPath("$.beers[2].status", is(this.containersList.get(0).getBeers().get(2).getStatus())))
				.andExpect(jsonPath("$.beers[3].id", is(this.containersList.get(0).getBeers().get(3).getId().intValue())))
				.andExpect(jsonPath("$.beers[3].min", is(this.containersList.get(0).getBeers().get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[3].max", is(this.containersList.get(0).getBeers().get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[3].description", is(this.containersList.get(0).getBeers().get(3).getDescription())))
				.andExpect(jsonPath("$.beers[3].status", is(this.containersList.get(0).getBeers().get(3).getStatus())))
				.andExpect(jsonPath("$.beers[4].id", is(this.containersList.get(0).getBeers().get(4).getId().intValue())))
				.andExpect(jsonPath("$.beers[4].min", is(this.containersList.get(0).getBeers().get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[4].max", is(this.containersList.get(0).getBeers().get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[4].description", is(this.containersList.get(0).getBeers().get(4).getDescription())))
				.andExpect(jsonPath("$.beers[4].status", is(this.containersList.get(0).getBeers().get(4).getStatus())))
				.andExpect(jsonPath("$.beers[5].id", is(this.containersList.get(0).getBeers().get(5).getId().intValue())))
				.andExpect(jsonPath("$.beers[5].min", is(this.containersList.get(0).getBeers().get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[5].max", is(this.containersList.get(0).getBeers().get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[5].description", is(this.containersList.get(0).getBeers().get(5).getDescription())))
				.andExpect(jsonPath("$.beers[5].status", is(this.containersList.get(0).getBeers().get(5).getStatus())))
				.andExpect(jsonPath("$.description", is(this.containersList.get(0).getDescription())))
				.andExpect(jsonPath("$.status", is(this.containersList.get(0).getStatus())));
	}

	@Test
	public void readContainer() throws Exception {
		mockMvc.perform(get("/api/container/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(this.containersList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].temperature", is(this.containersList.get(0).getTemperature().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].id", is(this.containersList.get(0).getBeers().get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[0].min", is(this.containersList.get(0).getBeers().get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].max", is(this.containersList.get(0).getBeers().get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].description", is(this.containersList.get(0).getBeers().get(0).getDescription())))
				.andExpect(jsonPath("$[0].beers[0].status", is(this.containersList.get(0).getBeers().get(0).getStatus())))
				.andExpect(jsonPath("$[0].beers[1].id", is(this.containersList.get(0).getBeers().get(1).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[1].min", is(this.containersList.get(0).getBeers().get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[1].max", is(this.containersList.get(0).getBeers().get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[1].description", is(this.containersList.get(0).getBeers().get(1).getDescription())))
				.andExpect(jsonPath("$[0].beers[1].status", is(this.containersList.get(0).getBeers().get(1).getStatus())))
				.andExpect(jsonPath("$[0].beers[2].id", is(this.containersList.get(0).getBeers().get(2).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[2].min", is(this.containersList.get(0).getBeers().get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[2].max", is(this.containersList.get(0).getBeers().get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[2].description", is(this.containersList.get(0).getBeers().get(2).getDescription())))
				.andExpect(jsonPath("$[0].beers[2].status", is(this.containersList.get(0).getBeers().get(2).getStatus())))
				.andExpect(jsonPath("$[0].beers[3].id", is(this.containersList.get(0).getBeers().get(3).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[3].min", is(this.containersList.get(0).getBeers().get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[3].max", is(this.containersList.get(0).getBeers().get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[3].description", is(this.containersList.get(0).getBeers().get(3).getDescription())))
				.andExpect(jsonPath("$[0].beers[3].status", is(this.containersList.get(0).getBeers().get(3).getStatus())))
				.andExpect(jsonPath("$[0].beers[4].id", is(this.containersList.get(0).getBeers().get(4).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[4].min", is(this.containersList.get(0).getBeers().get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[4].max", is(this.containersList.get(0).getBeers().get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[4].description", is(this.containersList.get(0).getBeers().get(4).getDescription())))
				.andExpect(jsonPath("$[0].beers[4].status", is(this.containersList.get(0).getBeers().get(4).getStatus())))
				.andExpect(jsonPath("$[0].beers[5].id", is(this.containersList.get(0).getBeers().get(5).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[5].min", is(this.containersList.get(0).getBeers().get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[5].max", is(this.containersList.get(0).getBeers().get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[5].description", is(this.containersList.get(0).getBeers().get(5).getDescription())))
				.andExpect(jsonPath("$[0].beers[5].status", is(this.containersList.get(0).getBeers().get(5).getStatus())))
				.andExpect(jsonPath("$[0].description", is(this.containersList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].status", is(this.containersList.get(0).getStatus())));
	}

	@Test
	public void createContainer() throws Exception {
		Containers containers = new Containers();
		containers.setDescription("New Containers " + UUID.randomUUID());
		containers.setTemperature(BigDecimal.ZERO);

		String containerJson = json(containers);

		this.mockMvc.perform(post("/api/container/")
				.contentType(contentType)
				.content(containerJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void deleteAllContainers() throws Exception {
		this.mockMvc.perform(delete("/api/container/")
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void updateContainer() throws Exception {
		Containers containers = this.containersList.get(0);
		containers.setTemperature(BigDecimal.TEN);

		this.mockMvc.perform(put("/api/container/" + this.containersList.get(0).getId().intValue())
				.contentType(contentType)
				.content(json(containers)))
				.andExpect(status().isOk());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}