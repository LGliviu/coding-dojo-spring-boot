package com.assignment.spring;

import com.assignment.spring.api.Main;
import com.assignment.spring.api.Sys;
import com.assignment.spring.api.WeatherResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    public static final String TEST_URL = "http://api.openweathermap.org/data/2.5/weather?q={city}&APPID={appid}";
    public static final String TEST_APP_ID = "91924c4d0a2db83";
    @Autowired
    private WeatherService underTest;
    @Mock
    private AppProperties appProperties;
    @Mock
    RestTemplate restTemplate;
    @Mock
    WeatherRepository weatherRepository;
    @Mock
    Mapper mapper;

    @Before
    public void setUp() {
        underTest = new WeatherService(appProperties, restTemplate, weatherRepository, mapper);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void givenCityNameSaveCityWeather() throws Exception {
        // given
        String city = "Auxerre";
        when(appProperties.getUrl()).thenReturn(TEST_URL);
        when(appProperties.getApp_ID()).thenReturn(TEST_APP_ID);
        String url = appProperties.getUrl().replace("{city}", city).replace("{appid}", appProperties.getApp_ID());
        WeatherResponse weatherResponse = new WeatherResponse();
        Sys sys = new Sys();
        Main main = new Main();
        weatherResponse.setName(city);
        sys.setCountry("FR");
        main.setTemp(298.48);
        ResponseEntity<WeatherResponse> responseEntity = new ResponseEntity<>(weatherResponse, HttpStatus.OK);
        given(restTemplate.getForEntity(url, WeatherResponse.class))
            .willReturn(responseEntity);

        // when
        WeatherDTO expectedWeatherDTO = underTest.saveCityWeather(city).getBody();

        // then
        ArgumentCaptor<WeatherEntity> weatherEntityArgumentCaptor = ArgumentCaptor.forClass(WeatherEntity.class);
        verify(weatherRepository).save(weatherEntityArgumentCaptor.capture());
        WeatherEntity weatherEntity = weatherEntityArgumentCaptor.getValue();
        WeatherDTO savedWeatherDTO = underTest.getMapper().toDTO(weatherEntity);
        assertEquals(expectedWeatherDTO, savedWeatherDTO);
    }

    @Test
    public void givenBadCityNameThrowException() {
        // given
        String city = "BadCityName";
        when(appProperties.getUrl()).thenReturn(TEST_URL);
        when(appProperties.getApp_ID()).thenReturn(TEST_APP_ID);
        String url = appProperties.getUrl().replace("{city}", city).replace("{appid}", appProperties.getApp_ID());
        given(restTemplate.getForEntity(url, WeatherResponse.class))
                .willThrow(new RestClientException("Error 400 - Bad request. Either some mandatory parameters in the"
                        + "request are missing or some of request parameters have incorrect format or values out"
                        + "of allowed range."));

        // when
        // then
        assertThatThrownBy(() -> underTest.saveCityWeather(city))
                .hasMessageContaining("There was an error while calling the OpenWeather API with the following error message:\n");
        verify(weatherRepository, never()).save(any());
    }

}