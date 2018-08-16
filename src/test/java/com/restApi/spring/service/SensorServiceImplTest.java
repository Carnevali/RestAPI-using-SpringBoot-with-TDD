package com.restApi.spring.service;

import com.restApi.spring.model.Beer;
import com.restApi.spring.model.Containers;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceImplTest {

    @Autowired
    private SensorServiceImpl testInstance;

    @MockBean
    private BeerService beerService;

    @MockBean
    private ContainerService containerService;

    @Test
    public void shouldDoNothingWhenThereIsNoContainers() {
        given(containerService.findAllContainers()).willReturn(emptyList());

        testInstance.changeTemperature();

        verify(beerService, never()).findBeersByContainers(any(Containers.class));
        verify(containerService, never()).updateContainer(any(Containers.class));
    }

    @Test
    public void shouldSetContainersTemperatureTo2WhenContainersTemperatureIs15() {

        Containers container = new Containers();
        container.setTemperature(valueOf(15));

        given(containerService.findAllContainers()).willReturn(singletonList(container));

        testInstance.changeTemperature();

        assertThat(container.getTemperature(), is(valueOf(2.0)));

        verify(beerService).findBeersByContainers(container);
        verify(containerService).updateContainer(container);
    }
}