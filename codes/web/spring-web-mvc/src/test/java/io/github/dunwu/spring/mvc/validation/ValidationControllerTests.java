package io.github.dunwu.spring.mvc.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ValidationControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(new ValidationController()).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void validateSuccess() throws Exception {
        this.mockMvc.perform(get("/validate?number=3&date=2029-07-04")).andExpect(content().string("No errors"));
    }

    @Test
    public void validateErrors() throws Exception {
        this.mockMvc.perform(get("/validate?number=&date=2010-07-01"))
                    .andExpect(content().string("Object has validation errors"));
    }

}
