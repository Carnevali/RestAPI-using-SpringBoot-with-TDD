package com.restApi.spring.controllers;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.service.BeerService;
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
public class BeerControllerTests {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8"));

	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private List<Beer> beerList = new ArrayList<>();

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

		this.beerService.deleteAllBeers();

		this.beerList.add(createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)", 0));
		this.beerList.add(createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)", 0));
		this.beerList.add(createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)", 0));
		this.beerList.add(createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)", 1));
		this.beerList.add(createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)", 0));
		this.beerList.add(createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)", 0));
	}

	private Beer createBeerInternal(BeerType beerType, Double min, Double max, String description, Integer status) {
		Beer beer = new Beer();
		beer.setType(beerType);
		beer.setMin(BigDecimal.valueOf(min));
		beer.setMax(BigDecimal.valueOf(max));
		beer.setDescription(description);
		beer.setStatus(status);

		return beerService.saveBeer(beer);
	}


	@Test
	public void beerNotFound() throws Exception {
		mockMvc.perform(get("/api/beer/-1")
				.content("")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void readSingleBeer() throws Exception {
		mockMvc.perform(get("/api/beer/" + this.beerList.get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.beerList.get(0).getId().intValue())))
				.andExpect(jsonPath("$.min", is(this.beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$.max", is(this.beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$.description", is(this.beerList.get(0).getDescription())))
				.andExpect(jsonPath("$.status", is(this.beerList.get(0).getStatus())));
	}

	@Test
	public void readBeer() throws Exception {
		mockMvc.perform(get("/api/beer/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(6)))
				.andExpect(jsonPath("$[0].id", is(this.beerList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].min", is(this.beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].max", is(this.beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].description", is(this.beerList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].status", is(this.beerList.get(0).getStatus())))
				.andExpect(jsonPath("$[1].id", is(this.beerList.get(1).getId().intValue())))
				.andExpect(jsonPath("$[1].min", is(this.beerList.get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$[1].max", is(this.beerList.get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$[1].description", is(this.beerList.get(1).getDescription())))
				.andExpect(jsonPath("$[1].status", is(this.beerList.get(1).getStatus())))
				.andExpect(jsonPath("$[2].id", is(this.beerList.get(2).getId().intValue())))
				.andExpect(jsonPath("$[2].min", is(this.beerList.get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$[2].max", is(this.beerList.get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$[2].description", is(this.beerList.get(2).getDescription())))
				.andExpect(jsonPath("$[2].status", is(this.beerList.get(2).getStatus())))
				.andExpect(jsonPath("$[3].id", is(this.beerList.get(3).getId().intValue())))
				.andExpect(jsonPath("$[3].min", is(this.beerList.get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$[3].max", is(this.beerList.get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$[3].description", is(this.beerList.get(3).getDescription())))
				.andExpect(jsonPath("$[3].status", is(this.beerList.get(3).getStatus())))
				.andExpect(jsonPath("$[4].id", is(this.beerList.get(4).getId().intValue())))
				.andExpect(jsonPath("$[4].min", is(this.beerList.get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$[4].max", is(this.beerList.get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$[4].description", is(this.beerList.get(4).getDescription())))
				.andExpect(jsonPath("$[4].status", is(this.beerList.get(4).getStatus())))
				.andExpect(jsonPath("$[5].id", is(this.beerList.get(5).getId().intValue())))
				.andExpect(jsonPath("$[5].min", is(this.beerList.get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$[5].max", is(this.beerList.get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$[5].description", is(this.beerList.get(5).getDescription())))
				.andExpect(jsonPath("$[5].status", is(this.beerList.get(5).getStatus())));
	}

	@Test
	public void createBeer() throws Exception {
		Beer beer = new Beer();
		beer.setDescription("New Beer " + UUID.randomUUID());
		beer.setMin(BigDecimal.ZERO);
		beer.setMax(BigDecimal.TEN);
		beer.setType(BeerType.NONE);

		String beerJson = json(beer);

		this.mockMvc.perform(post("/api/beer/")
				.contentType(contentType)
				.content(beerJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void createBeerDuplicate() throws Exception {
		Beer beer = new Beer();
		beer.setDescription("Beer 2 (IPA)");
		beer.setType(BeerType.IPA);
		beer.setMin(BigDecimal.valueOf(5.0));
		beer.setMax(BigDecimal.valueOf(6.0));

		String beerJson = json(beer);

		this.mockMvc.perform(post("/api/beer/")
				.contentType(contentType)
				.content(beerJson))
				.andExpect(status().isConflict());
	}

	@Test
	public void deleteBeer() throws Exception {
		this.mockMvc.perform(delete("/api/beer/" + this.beerList.get(0).getId().intValue())
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteAllBeers() throws Exception {
		this.mockMvc.perform(delete("/api/beer/")
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void createDefault() throws Exception {
		this.mockMvc.perform(get("/api//beer/default/")
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.temperature", is(5.0)))
				.andExpect(jsonPath("$.beers[0].min", is(this.beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[0].max", is(this.beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[0].description", is(this.beerList.get(0).getDescription())))
				.andExpect(jsonPath("$.beers[0].status", is(this.beerList.get(0).getStatus())))
				.andExpect(jsonPath("$.beers[1].min", is(this.beerList.get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[1].max", is(this.beerList.get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[1].description", is(this.beerList.get(1).getDescription())))
				.andExpect(jsonPath("$.beers[1].status", is(this.beerList.get(1).getStatus())))
				.andExpect(jsonPath("$.beers[2].min", is(this.beerList.get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[2].max", is(this.beerList.get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[2].description", is(this.beerList.get(2).getDescription())))
				.andExpect(jsonPath("$.beers[2].status", is(this.beerList.get(2).getStatus())))
				.andExpect(jsonPath("$.beers[3].min", is(this.beerList.get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[3].max", is(this.beerList.get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[3].description", is(this.beerList.get(3).getDescription())))
				.andExpect(jsonPath("$.beers[3].status", is(this.beerList.get(3).getStatus())))
				.andExpect(jsonPath("$.beers[4].min", is(this.beerList.get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[4].max", is(this.beerList.get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[4].description", is(this.beerList.get(4).getDescription())))
				.andExpect(jsonPath("$.beers[4].status", is(this.beerList.get(4).getStatus())))
				.andExpect(jsonPath("$.beers[5].min", is(this.beerList.get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[5].max", is(this.beerList.get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[5].description", is(this.beerList.get(5).getDescription())))
				.andExpect(jsonPath("$.beers[5].status", is(this.beerList.get(5).getStatus())))
				.andExpect(jsonPath("$.description", is("Containers 1")))
				.andExpect(jsonPath("$.status", is(1)))
				.andExpect(status().isCreated());
	}

	@Test
	public void updateBeer() throws Exception {
		Beer beer = this.beerList.get(0);
		beer.setMax(BigDecimal.TEN);
		beer.setMin(BigDecimal.ZERO);

		this.mockMvc.perform(put("/api/beer/" + this.beerList.get(0).getId().intValue())
				.contentType(contentType)
				.content(json(beer)))
				.andExpect(status().isOk());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}