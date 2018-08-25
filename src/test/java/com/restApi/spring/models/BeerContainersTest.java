package com.restApi.spring.models;

import com.restApi.spring.helper.BeerFactory;
import com.restApi.spring.model.Beer;
import com.restApi.spring.service.BeerService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by felipe on 19/8/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BeerContainersTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private BeerService beerService;

    private static String urlEndPoint = "http://localhost:8080/api/";
    private List<Beer> beerList = new ArrayList<>();

    @Before
    public void setup() {
        this.beerService.deleteAllBeers();

        BeerFactory beerFactory = new BeerFactory();
        beerFactory.createBeers(beerList);
    }

    @Test
    public void whenSaveManyToManyRelationshipThenCorrect() throws JSONException {
        beerList.forEach(b -> template.postForEntity(urlEndPoint + "beer", b, Beer.class));

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-type", "text/uri-list");
        HttpEntity<String> httpEntity = new HttpEntity<>(urlEndPoint + "beer" + "/1\n" +
                                                         urlEndPoint + "beer" + "/2\n" +
                                                         urlEndPoint + "beer" + "/3", requestHeaders);

        template.exchange(urlEndPoint + "container/1/beer", HttpMethod.PUT, httpEntity, String.class);

        String jsonResponse = template.getForObject(urlEndPoint + "beer/1/container", String.class);
        JSONObject jsonObj = new JSONObject(jsonResponse).getJSONObject("_embedded");
        JSONArray jsonArray = jsonObj.getJSONArray("containers");
        assertEquals("beer is incorrect", jsonArray.getJSONObject(0).getString("name"), urlEndPoint + "container");
    }
}
