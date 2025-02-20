package io.github.dunwu.springboot.web;

import io.github.dunwu.springboot.web.entity.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:forbreak@163.com">Zhang Peng</a>
 * @since 2019-11-9
 */
@Service
public class WeatherService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Weather getWeather(String cityCode) {
        return restTemplate.getForObject("http://t.weather.sojson.com/api/weather/city/" + cityCode, Weather.class);
    }

    public void printBasicWeatherInfo(Weather weather) {
        log.info("时间：{}", weather.getTime());
        log.info("空气质量：{}", weather.getData().getQuality());
        log.info("湿度：{}", weather.getData().getShidu());
        log.info("PM25数值：{}", weather.getData().getPm25());
        log.info("温度：{}", weather.getData().getWendu());
        log.info("建议：{}", weather.getData().getGanmao());
    }

}
