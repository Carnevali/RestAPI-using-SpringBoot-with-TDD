package com.restApi.spring.service;

import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Containers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceImplTest {
    private Containers container;

    @Autowired
    private SensorServiceImpl testInstance;

    @MockBean
    private BeerService beerService;

    @MockBean
    private ContainerService containerService;

    @Before
    public void setup() {
        beerService.deleteAllBeers();
        container = new Containers("New Container", 6.0, beerService.createBeersDefault(), StatusType.OK);
    }

    @Test
    public void shouldDoNothingWhenThereIsNoContainers() {
        given(containerService.findAllContainers()).willReturn(emptyList());

        testInstance.changeBehavior();

        verify(beerService, never()).findBeersByContainers(any(Containers.class));
        verify(containerService, never()).updateContainer(any(Containers.class));
    }

    @Test
    public void shouldUpdateContainersTemperatureIs7() {
        container.updateTemperature();
        given(containerService.findAllContainers()).willReturn(singletonList(container));

        assertThat(container.getTemperature(), is(7.0));

        testInstance.changeBehavior();

        verify(containerService).updateContainer(container);
    }

    @Test
    public void shouldResetContainersTemperatureWhenContainersTemperatureIs15() {
        while (container.getTemperature().intValue() < 15) {
            container.updateTemperature();
        }

        given(containerService.findAllContainers()).willReturn(singletonList(container));
        container.resetTemperature();

        assertThat(container.getTemperature(), is(2.0));

        testInstance.changeBehavior();

        container.updateStatus();
        verify(containerService).updateContainer(container);
    }

    @Test
    public void shouldUpdateStatusContainersWhenContainersTemperatureIs2() {
        given(containerService.findAllContainers()).willReturn(singletonList(container));

        container.resetTemperature();
        container.updateStatus();

        assertThat(container.getStatus(), is(StatusType.OK));

        testInstance.changeBehavior();

        verify(containerService).updateContainer(container);
    }
}