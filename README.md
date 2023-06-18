Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

### Solution

I tried to write clean and maintainable code, easy to deploy and scale-up, production ready.

I added a RequestParam to the method in the controller used for saving the weather for a city and validated it using annotations on the parameter.

I used the Single Responsibility Principle and extracted the mapper method from the controller into a separate class adding other mappings. I also added another layer, the service layer where I put the business logic for saving the weather for a city, leaving only the method call in the controller.

I created a DTO class for the weather in order to transfer data between components and display to the user, avoiding using the more heavy WeatherResponse object or the entity.

I handled the unchecked RestClientException when calling the openweathermap api.

I created 2 tests for the saveCityWeather method in the service, one checking that the weather entity is saved in the database, the other checking that an exception is thrown when a bad city name is used.

I also created a class AppProperties to get the url and appId that I moved in the yml file.

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution**

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
