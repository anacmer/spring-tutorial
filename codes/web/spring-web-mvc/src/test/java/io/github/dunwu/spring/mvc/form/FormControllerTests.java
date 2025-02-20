package io.github.dunwu.spring.mvc.form;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class FormControllerTests {

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");

        this.mockMvc = standaloneSetup(new FormController()).setViewResolvers(viewResolver).build();
    }

    @Test
    public void submitSuccess() throws Exception {
        String timezone = getTimezone(1941, 12, 16);
        this.mockMvc
            .perform(post("/form").param("name", "Joe").param("age", "56").param("birthDate", "1941-12-16")
                                  .param("phone", "(347) 888-1234").param("currency", "$123.33").param("percent", "89%")
                                  .param("inquiry", "comment").param("inquiryDetails", "what is?")
                                  .param("additionalInfo[mvc]", "true").param("_additionalInfo[mvc]", "on")
                                  .param("additionalInfo[java]", "true").param("_additionalInfo[java]", "on")
                                  .param("subscribeNewsletter", "false"))
            .andDo(print()).andExpect(status().isMovedTemporarily()).andExpect(redirectedUrl("/form"))
            .andExpect(flash().attribute("message",
                "Form submitted successfully.  Bound properties name='Joe', age=56, "
                    + "birthDate=Tue Dec 16 00:00:00 " + timezone + " 1941, phone='(347) 888-1234', "
                    + "currency=123.33, percent=0.89, inquiry=comment, inquiryDetails='what is?',"
                    + " subscribeNewsletter=false, additionalInfo={java=true, mvc=true}"));
    }

    private String getTimezone(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        TimeZone timezone = TimeZone.getDefault();
        boolean inDaylight = timezone.inDaylightTime(date);
        return timezone.getDisplayName(inDaylight, TimeZone.SHORT);
    }

    @Test
    public void submitSuccessAjax() throws Exception {
        String timezone = getTimezone(1941, 12, 16);
        this.mockMvc
            .perform(post("/form").header("X-Requested-With", "XMLHttpRequest").param("name", "Joe")
                                  .param("age", "56").param("birthDate", "1941-12-16").param("phone", "(347) 888-1234")
                                  .param("currency", "$123.33").param("percent", "89%").param("inquiry", "comment")
                                  .param("inquiryDetails", "what is?").param("additionalInfo[mvc]", "true")
                                  .param("_additionalInfo[mvc]", "on").param("additionalInfo[java]", "true")
                                  .param("_additionalInfo[java]", "on").param("subscribeNewsletter", "false"))
            .andExpect(status().isOk()).andExpect(view().name("form")).andExpect(model().hasNoErrors())
            .andExpect(model().attribute("message",
                "Form submitted successfully.  Bound properties name='Joe', age=56, "
                    + "birthDate=Tue Dec 16 00:00:00 " + timezone + " 1941, phone='(347) 888-1234', "
                    + "currency=123.33, percent=0.89, inquiry=comment, inquiryDetails='what is?',"
                    + " subscribeNewsletter=false, additionalInfo={java=true, mvc=true}"));
    }

    @Test
    public void submitError() throws Exception {
        this.mockMvc.perform(post("/form"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("form"))
                    .andExpect(model().errorCount(2))
                    .andExpect(model().attributeHasFieldErrors("formBean", "name", "age"));
    }

}
