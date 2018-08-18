package com.restApi.spring.controllers;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
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

	private final String httpUrl = "/api/beer/";
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

		this.beerList.add(createBeerInternal(BeerType.PILSNER, 4.0, 6.0, "Beer 1 (Pilsner)", StatusType.OK));
		this.beerList.add(createBeerInternal(BeerType.IPA, 5.0, 6.0, "Beer 2 (IPA)", StatusType.OK));
		this.beerList.add(createBeerInternal(BeerType.LARGER, 4.0, 7.0, "Beer 3 (Larger)", StatusType.OK));
		this.beerList.add(createBeerInternal(BeerType.STOUT, 6.0, 8.0, "Beer 4 (Stout)", StatusType.WARNING));
		this.beerList.add(createBeerInternal(BeerType.WHEATBEER, 3.0, 5.0, "Beer 5 (Wheat beer)", StatusType.OK));
		this.beerList.add(createBeerInternal(BeerType.PALEALE, 4.0, 6.0, "Beer 6 (Pale Ale)", StatusType.OK));
	}

	private Beer createBeerInternal(BeerType beerType, Double min, Double max, String description, StatusType status) {
		Beer beer = new Beer(description, beerType, min, max, null, status);
		return beerService.saveBeer(beer);
	}

	@Test
	public void beerNotFound() throws Exception {
		mockMvc.perform(get(httpUrl + "-1")
				.content("")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void readSingleBeer() throws Exception {
		mockMvc.perform(get(httpUrl + this.beerList.get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", is(this.beerList.get(0).getId().intValue())))
				.andExpect(jsonPath("$.min", is(this.beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$.max", is(this.beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$.description", is(this.beerList.get(0).getDescription())))
				.andExpect(jsonPath("$.status.value", is(this.beerList.get(0).getStatus().getValue())));
	}

	@Test
	public void readBeer() throws Exception {
		mockMvc.perform(get(httpUrl))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(6)))
				.andExpect(jsonPath("$[0].id", is(this.beerList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].min", is(this.beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].max", is(this.beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].description", is(this.beerList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].status.value", is(this.beerList.get(0).getStatus().getValue())))
				.andExpect(jsonPath("$[1].id", is(this.beerList.get(1).getId().intValue())))
				.andExpect(jsonPath("$[1].min", is(this.beerList.get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$[1].max", is(this.beerList.get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$[1].description", is(this.beerList.get(1).getDescription())))
				.andExpect(jsonPath("$[1].status.value", is(this.beerList.get(1).getStatus().getValue())))
				.andExpect(jsonPath("$[2].id", is(this.beerList.get(2).getId().intValue())))
				.andExpect(jsonPath("$[2].min", is(this.beerList.get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$[2].max", is(this.beerList.get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$[2].description", is(this.beerList.get(2).getDescription())))
				.andExpect(jsonPath("$[2].status.value", is(this.beerList.get(2).getStatus().getValue())))
				.andExpect(jsonPath("$[3].id", is(this.beerList.get(3).getId().intValue())))
				.andExpect(jsonPath("$[3].min", is(this.beerList.get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$[3].max", is(this.beerList.get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$[3].description", is(this.beerList.get(3).getDescription())))
				.andExpect(jsonPath("$[3].status.value", is(this.beerList.get(3).getStatus().getValue())))
				.andExpect(jsonPath("$[4].id", is(this.beerList.get(4).getId().intValue())))
				.andExpect(jsonPath("$[4].min", is(this.beerList.get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$[4].max", is(this.beerList.get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$[4].description", is(this.beerList.get(4).getDescription())))
				.andExpect(jsonPath("$[4].status.value", is(this.beerList.get(4).getStatus().getValue())))
				.andExpect(jsonPath("$[5].id", is(this.beerList.get(5).getId().intValue())))
				.andExpect(jsonPath("$[5].min", is(this.beerList.get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$[5].max", is(this.beerList.get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$[5].description", is(this.beerList.get(5).getDescription())))
				.andExpect(jsonPath("$[5].status.value", is(this.beerList.get(5).getStatus().getValue())));
	}

	@Test
	public void createBeer() throws Exception {
		Beer beer = new Beer("New Beer " + UUID.randomUUID(), BeerType.NONE, 0.0, 10.0, null, StatusType.OK);
		String beerJson = json(beer);

		this.mockMvc.perform(post(httpUrl)
				.contentType(contentType)
				.content(beerJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void createBeerDuplicate() throws Exception {
		Beer beer = new Beer("Beer 2 (IPA)", BeerType.IPA, 5.0, 6.0, null, StatusType.OK);
		String beerJson = json(beer);

		this.mockMvc.perform(post(httpUrl)
				.contentType(contentType)
				.content(beerJson))
				.andExpect(status().isConflict());
	}

	@Test
	public void deleteBeer() throws Exception {
		this.mockMvc.perform(delete(httpUrl + this.beerList.get(0).getId().intValue())
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void deleteAllBeers() throws Exception {
		this.mockMvc.perform(delete(httpUrl)
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void updateBeer() throws Exception {
		Beer beer = this.beerList.get(0);

		this.mockMvc.perform(put(httpUrl + this.beerList.get(0).getId().intValue())
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