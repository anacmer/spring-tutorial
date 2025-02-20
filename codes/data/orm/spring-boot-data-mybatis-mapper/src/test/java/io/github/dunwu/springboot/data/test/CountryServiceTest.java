package io.github.dunwu.springboot.data.test;

import io.github.dunwu.springboot.data.SpringBootDataMybatisMapperApplication;
import io.github.dunwu.springboot.data.model.Country;
import io.github.dunwu.springboot.data.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * @author liuzh
 * @since 2017/9/2.
 */
@SpringBootTest
@Import(SpringBootDataMybatisMapperApplication.class)
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Test
    public void test() {
        Country country = new Country();
        List<Country> all = countryService.getAll(country);
        for (Country c : all) {
            System.out.println(c.getCountryname());
        }
    }

}
