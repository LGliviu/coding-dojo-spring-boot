package com.assignment.spring;

import com.assignment.spring.api.WeatherResponse;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public WeatherDTO toDTO(WeatherResponse weatherResponse) {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setCity(weatherResponse.getName());
        weatherDTO.setCountry(weatherResponse.getSys().getCountry());
        weatherDTO.setTemperature(weatherResponse.getMain().getTemp());

        return weatherDTO;
    }

    public WeatherDTO toDTO(WeatherEntity weatherEntity) {
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setCity(weatherEntity.getCity());
        weatherDTO.setCountry(weatherEntity.getCountry());
        weatherDTO.setTemperature(weatherEntity.getTemperature());

        return weatherDTO;
    }

    public WeatherEntity toWeatherEntity(WeatherDTO weatherDTO) {
        WeatherEntity weatherEntity  = new WeatherEntity();
        weatherDTO.setCity(weatherDTO.getCity());
        weatherDTO.setCountry(weatherDTO.getCountry());
        weatherDTO.setTemperature(weatherDTO.getTemperature());

        return weatherEntity;
    }

}
