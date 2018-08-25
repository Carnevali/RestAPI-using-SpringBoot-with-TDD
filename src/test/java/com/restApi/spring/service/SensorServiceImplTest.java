package com.restApi.spring.service;

import com.restApi.spring.enums.BeerType;
import com.restApi.spring.enums.StatusType;
import com.restApi.spring.model.Beer;
import com.restApi.spring.model.BeerContainers;
import com.restApi.spring.model.Containers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceImplTest {
    private Containers container;
    private Beer ipa;
    private Beer paleAle;
    private List<BeerContainers> bc = new ArrayList<>();

    @Autowired
    private SensorServiceImpl testInstance;

    @MockBean
    private BeerContainersService beerContainersService;

    @MockBean
    private BeerService beerService;

    @MockBean
    private ContainerService containerService;

    @Before
    public void setup() {
        container = new Containers("New Container", 6.0, StatusType.OK);

        ipa = new Beer("IPA", BeerType.IPA, 5.0, 6.0);
        paleAle = new Beer("Pale Ale", BeerType.PALEALE, 4.0, 6.0);
    }

    @Test
    public void shouldDoNothingWhenThereIsNoContainers() {
        given(containerService.findAllContainers()).willReturn(emptyList());

        testInstance.changeBehavior();

        verify(beerContainersService, never()).findByContainersId(any(Containers.class));
        verify(containerService, never()).updateContainer(any(Containers.class));
    }

    @Test
    public void shouldDoNothingWhenThereIsContainersWithoutBeers() {
        given(containerService.findAllContainers()).willReturn(singletonList(container));

        testInstance.changeBehavior();

        verify(beerContainersService, never()).findByBeerId(any(Beer.class));
        verify(beerContainersService, never()).update(any(BeerContainers.class));
    }

    @Test
    public void shouldReturnStatusBeerOkWithContainerTemperatureIs5() {
        container.resetTemperature();
        container.updateTemperature();
        container.updateTemperature();

        bc.add(new BeerContainers(ipa, container));
        bc.add(new BeerContainers(paleAle, container));

        testInstance.changeBehavior();

        given(containerService.findAllContainers()).willReturn(singletonList(container));

        assertThat(container.getTemperature(), is(5.0));
        assertThat(container.getStatus(), is(StatusType.OK));
        assertThat(bc.get(0).getStatusBeer(), is(StatusType.OK));
        assertThat(bc.get(1).getStatusBeer(), is(StatusType.OK));

        verify(containerService, any()).updateContainer(any(Containers.class));
    }

    @Test
    public void shouldReturnStatusContainerWarningBeerWarning() {
        container = new Containers("Container status ok", 3.0, StatusType.OK);

        bc.add(new BeerContainers(ipa, container));
        bc.add(new BeerContainers(paleAle, container));

        container = new Containers(container.getDescription(), container.getTemperature(), bc, container.getStatus());

        given(containerService.findAllContainers()).willReturn(singletonList(container));

        testInstance.changeBehavior();

        assertThat(container.getStatus(), is(StatusType.WARNING));
        assertThat(bc.get(0).getStatusBeer(), is(StatusType.WARNING));
        assertThat(bc.get(1).getStatusBeer(), is(StatusType.OK));

        verify(containerService, any()).updateContainer(any(Containers.class));
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