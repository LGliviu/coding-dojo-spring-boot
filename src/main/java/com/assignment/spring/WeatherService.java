package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@AllArgsConstructor
@Service
public class WeatherService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private Mapper mapper;

    public ResponseEntity<WeatherDTO> saveCityWeather(String city) throws RuntimeException {
        String url = appProperties.getUrl().replace("{city}", city).replace("{appid}", appProperties.getApp_ID());
        WeatherResponse weatherResponse;
        try {
            weatherResponse = restTemplate.getForEntity(url, WeatherResponse.class).getBody();
        }
        catch (RestClientException exception) {
            throw new RuntimeException(
                    "There was an error while calling the OpenWeather API with the following error message:\n"
                            + exception.getMessage()
            );
        }
        WeatherDTO weatherDTO = mapper.toDTO(weatherResponse);
        WeatherEntity weatherEntity = mapper.toWeatherEntity(weatherDTO);
        weatherRepository.save(weatherEntity);

        return new ResponseEntity<>(weatherDTO, HttpStatus.OK);
    }

}
