package com.restApi.spring.controllers;

import com.restApi.spring.enums.StatusType;
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
import java.nio.charset.Charset;
import java.util.*;

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

	private final String httpUrl = "/api/container/";
	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private List<Containers> containersList = new ArrayList<>();
	private List<Beer> beerList = new ArrayList<>();

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
		this.beerService.deleteAllBeers();
		this.beerList = this.beerService.createBeersDefault();

		this.containersList.add(createContainerInternal("Container 1", 6.0, this.beerService.createBeersDefault(), StatusType.OK));
		this.containersList.add(createContainerInternal("Container 2", 6.0, this.beerService.createBeersDefault(), StatusType.OK));
		this.containersList.add(createContainerInternal("Container 3", 7.0, this.beerService.createBeersDefault(), StatusType.OK));
	}

	private Containers createContainerInternal(String description, Double temperature, List<Beer> beers, StatusType status) {
		Containers containers = new Containers(description, temperature, beers, status);
		return containerService.saveContainer(containers);
	}

	@Test
	public void containerNotFound() throws Exception {
		mockMvc.perform(get(httpUrl + "-1")
				.content("")
				.contentType(contentType))
				.andExpect(status().isNotFound());
	}

	@Test
	public void readSingleContainer() throws Exception {
		this.containersList.forEach(container -> {
			try {
				List<Beer> beerList = new ArrayList<>(container.getBeers());

				mockMvc.perform(get(httpUrl + container.getId()))
						.andExpect(status().isOk())
						.andExpect(content().contentType(contentType))
						.andExpect(jsonPath("$.id", is(container.getId().intValue())))
						.andExpect(jsonPath("$.temperature", is(container.getTemperature().doubleValue())))
						.andExpect(jsonPath("$.beers[0].id", is(beerList.get(0).getId().intValue())))
						.andExpect(jsonPath("$.beers[0].min", is(beerList.get(0).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[0].max", is(beerList.get(0).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[0].description", is(beerList.get(0).getDescription())))
						.andExpect(jsonPath("$.beers[0].status.value", is(beerList.get(0).getStatus().getValue())))
						.andExpect(jsonPath("$.beers[1].id", is(beerList.get(1).getId().intValue())))
						.andExpect(jsonPath("$.beers[1].min", is(beerList.get(1).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[1].max", is(beerList.get(1).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[1].description", is(beerList.get(1).getDescription())))
						.andExpect(jsonPath("$.beers[1].status.value", is(beerList.get(1).getStatus().getValue())))
						.andExpect(jsonPath("$.beers[2].id", is(beerList.get(2).getId().intValue())))
						.andExpect(jsonPath("$.beers[2].min", is(beerList.get(2).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[2].max", is(beerList.get(2).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[2].description", is(beerList.get(2).getDescription())))
						.andExpect(jsonPath("$.beers[2].status.value", is(beerList.get(2).getStatus().getValue())))
						.andExpect(jsonPath("$.beers[3].id", is(beerList.get(3).getId().intValue())))
						.andExpect(jsonPath("$.beers[3].min", is(beerList.get(3).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[3].max", is(beerList.get(3).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[3].description", is(beerList.get(3).getDescription())))
						.andExpect(jsonPath("$.beers[3].status.value", is(beerList.get(3).getStatus().getValue())))
						.andExpect(jsonPath("$.beers[4].id", is(beerList.get(4).getId().intValue())))
						.andExpect(jsonPath("$.beers[4].min", is(beerList.get(4).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[4].max", is(beerList.get(4).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[4].description", is(beerList.get(4).getDescription())))
						.andExpect(jsonPath("$.beers[4].status.value", is(beerList.get(4).getStatus().getValue())))
						.andExpect(jsonPath("$.beers[5].id", is(beerList.get(5).getId().intValue())))
						.andExpect(jsonPath("$.beers[5].min", is(beerList.get(5).getMin().doubleValue())))
						.andExpect(jsonPath("$.beers[5].max", is(beerList.get(5).getMax().doubleValue())))
						.andExpect(jsonPath("$.beers[5].description", is(beerList.get(5).getDescription())))
						.andExpect(jsonPath("$.beers[5].status.value", is(beerList.get(5).getStatus().getValue())))
						.andExpect(jsonPath("$.description", is(container.getDescription())))
						.andExpect(jsonPath("$.status.value", is(container.getStatus().getValue())));
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void readContainer() throws Exception {
		List<Containers> list = new ArrayList<>(this.containersList);
		List<Beer> beerList = new ArrayList<>(list.get(0).getBeers());

		mockMvc.perform(get(httpUrl))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].id", is(list.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].temperature", is(list.get(0).getTemperature().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].id", is(beerList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[0].min", is(beerList.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].max", is(beerList.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[0].description", is(beerList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].beers[0].status.value", is(beerList.get(0).getStatus().getValue())))
				.andExpect(jsonPath("$[0].beers[1].id", is(beerList.get(1).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[1].min", is(beerList.get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[1].max", is(beerList.get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[1].description", is(beerList.get(1).getDescription())))
				.andExpect(jsonPath("$[0].beers[1].status.value", is(beerList.get(1).getStatus().getValue())))
				.andExpect(jsonPath("$[0].beers[2].id", is(beerList.get(2).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[2].min", is(beerList.get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[2].max", is(beerList.get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[2].description", is(beerList.get(2).getDescription())))
				.andExpect(jsonPath("$[0].beers[2].status.value", is(beerList.get(2).getStatus().getValue())))
				.andExpect(jsonPath("$[0].beers[3].id", is(beerList.get(3).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[3].min", is(beerList.get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[3].max", is(beerList.get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[3].description", is(beerList.get(3).getDescription())))
				.andExpect(jsonPath("$[0].beers[3].status.value", is(beerList.get(3).getStatus().getValue())))
				.andExpect(jsonPath("$[0].beers[4].id", is(beerList.get(4).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[4].min", is(beerList.get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[4].max", is(beerList.get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[4].description", is(beerList.get(4).getDescription())))
				.andExpect(jsonPath("$[0].beers[4].status.value", is(beerList.get(4).getStatus().getValue())))
				.andExpect(jsonPath("$[0].beers[5].id", is(beerList.get(5).getId().intValue())))
				.andExpect(jsonPath("$[0].beers[5].min", is(beerList.get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$[0].beers[5].max", is(beerList.get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$[0].beers[5].description", is(beerList.get(5).getDescription())))
				.andExpect(jsonPath("$[0].beers[5].status.value", is(beerList.get(5).getStatus().getValue())))
				.andExpect(jsonPath("$[0].description", is(list.get(0).getDescription())))
				.andExpect(jsonPath("$[0].status.value", is(list.get(0).getStatus().getValue())));
	}

	@Test
	public void createContainer() throws Exception {
		Containers containers = new Containers("New Containers " + UUID.randomUUID(), 5.0, this.beerList, StatusType.OK);
		String containerJson = json(containers);

		this.mockMvc.perform(post(httpUrl)
				.contentType(contentType)
				.content(containerJson))
				.andExpect(status().isCreated());
	}

	@Test
	public void deleteSingleContainer() throws Exception {
		this.containersList.forEach(container -> {
			try {
				this.mockMvc.perform(delete(httpUrl + container.getId())
						.contentType(contentType)
						.content(""))
						.andExpect(jsonPath("$.success", is(true)))
						.andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void deleteAllContainers() throws Exception {
		this.mockMvc.perform(delete(httpUrl)
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.success", is(true)))
				.andExpect(status().isOk());
	}

	@Test
	public void updateContainer() throws Exception {
		this.containersList.forEach(container -> {
			container.resetTemperature();

			try {
				this.mockMvc.perform(put(httpUrl + container.getId().intValue())
						.contentType(contentType)
						.content(json(container)))
						.andExpect(status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void createDefault() throws Exception {
		List<Beer> list = new ArrayList<>(this.beerList);

		this.mockMvc.perform(get(httpUrl + "default/")
				.contentType(contentType)
				.content(""))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.temperature", is(5.0)))
				.andExpect(jsonPath("$.beers[0].min", is(list.get(0).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[0].max", is(list.get(0).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[0].description", is(list.get(0).getDescription())))
				.andExpect(jsonPath("$.beers[0].status.value", is(list.get(0).getStatus().getValue())))
				.andExpect(jsonPath("$.beers[1].min", is(list.get(1).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[1].max", is(list.get(1).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[1].description", is(list.get(1).getDescription())))
				.andExpect(jsonPath("$.beers[1].status.value", is(list.get(1).getStatus().getValue())))
				.andExpect(jsonPath("$.beers[2].min", is(list.get(2).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[2].max", is(list.get(2).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[2].description", is(list.get(2).getDescription())))
				.andExpect(jsonPath("$.beers[2].status.value", is(list.get(2).getStatus().getValue())))
				.andExpect(jsonPath("$.beers[3].min", is(list.get(3).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[3].max", is(list.get(3).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[3].description", is(list.get(3).getDescription())))
				.andExpect(jsonPath("$.beers[3].status.value", is(list.get(3).getStatus().getValue())))
				.andExpect(jsonPath("$.beers[4].min", is(list.get(4).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[4].max", is(list.get(4).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[4].description", is(list.get(4).getDescription())))
				.andExpect(jsonPath("$.beers[4].status.value", is(list.get(4).getStatus().getValue())))
				.andExpect(jsonPath("$.beers[5].min", is(list.get(5).getMin().doubleValue())))
				.andExpect(jsonPath("$.beers[5].max", is(list.get(5).getMax().doubleValue())))
				.andExpect(jsonPath("$.beers[5].description", is(list.get(5).getDescription())))
				.andExpect(jsonPath("$.beers[5].status.value", is(list.get(5).getStatus().getValue())))
				.andExpect(jsonPath("$.description", is("Containers 1")))
				.andExpect(jsonPath("$.status.value", is(StatusType.WARNING.getValue())))
				.andExpect(status().isCreated());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}