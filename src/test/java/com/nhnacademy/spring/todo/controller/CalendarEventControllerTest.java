package com.nhnacademy.spring.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.spring.todo.config.RootConfig;
import com.nhnacademy.spring.todo.config.WebConfig;
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
        RegisterRequest request = new RegisterRequest("rest-Api 공부하기","2023-04-20");
        mockMvc.perform(post("/api/calendar/events")
                .header("X-USER-ID","marco")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void viewEventById() {
    }

    @Test
    void getDayEvent() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteDayEventAll() {
    }

    @Test
    void countDayEvent() {
    }
}