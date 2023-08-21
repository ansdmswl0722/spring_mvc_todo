package com.nhnacademy.spring.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.nhnacademy.spring.todo.config.RootConfig;
import com.nhnacademy.spring.todo.config.WebConfig;
import com.nhnacademy.spring.todo.domain.Event;
import com.nhnacademy.spring.todo.domain.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextHierarchy(value = {
        @ContextConfiguration(classes = {RootConfig.class}),
        @ContextConfiguration(classes = {WebConfig.class})
})
class CalendarEventControllerTest {
    @Autowired
    WebApplicationContext context;
    ObjectMapper objectMapper = new ObjectMapper();
    MockMvc mockMvc;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    @Test
    void register() throws Exception {
        RegisterRequest request = new RegisterRequest("rest-Api 공부하기","2023-05-20");
        mockMvc.perform(post("/api/calendar/events")
                .header("X-USER-ID","marco")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(status().isCreated())
                .andReturn();
    }
    @Test
    void register_missing_userId_exception() throws Exception {
        RegisterRequest request = new RegisterRequest("rest-Api 공부하기","2023-06-20");
        mockMvc.perform(post("/api/calendar/events")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode").value(401))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Unauthorized"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
    @Test
    void viewEventById() throws Exception {
        mockMvc.perform(get("/api/calendar/events/0")
                .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.subject").value("할일0"))
                .andExpect(jsonPath("$.eventAt").value("2023-04-19"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void viewEventById_invalid_owner_exception() throws Exception {
        mockMvc.perform(get("/api/calendar/events/0")
                        .header("X-USER-ID","moon"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.statusCode").value(403))
                .andExpect(jsonPath("$.subject").value("할일0"))
                .andExpect(jsonPath("$.eventAt").value("2023-04-19"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getDayEvent() throws Exception {
        String result = mockMvc.perform(get("/api/calendar/events")
                .param("year","2023")
                .param("month","05")
                .param("day","20")
                .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Event> events= JsonPath.parse(result).read("$");
        assertTrue(events.size()>0);
    }

    @Test
    void getMonthEvent() throws Exception {
        String result = mockMvc.perform(get("/api/calendar/events")
                        .param("year","2023")
                        .param("month","04")
                        .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Event> events= JsonPath.parse(result).read("$");
        assertTrue(events.size()>0);
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete("/api/calendar/events/10")
                .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void deleteDayEventAll() throws Exception {
        mockMvc.perform(delete("/api/calendar/events/daily/2023-04-19")
                .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void countDayEvent() throws Exception{
        mockMvc.perform(get("/api/calendar/daily-register-count")
                .param("date","2023-04-20")
                .header("X-USER-ID","marco"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5))
                .andReturn();
    }
}