package com.nhnacademy.spring.todo.controller;

import com.nhnacademy.spring.todo.domain.*;
import com.nhnacademy.spring.todo.repository.MemoryRepository;
import com.nhnacademy.spring.todo.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarEventController {
    private final EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<RegisterResponse> register(HttpServletRequest servletRequest,
                                                     @RequestBody RegisterRequest request) {
        RegisterResponse response = eventService.save(servletRequest.getHeader("X-USER-ID"), request.getSubject(),request.getEventAt());
        ResponseEntity<RegisterResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.CREATED);
        return responseEntity;
    }
    @GetMapping("/events/{id}")
    public ResponseEntity<ViewResponse> viewEventById(@PathVariable long id) {
        ViewResponse response = eventService.getEvent(id);
        ResponseEntity<ViewResponse> responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getDayEvent(@RequestParam("year") Integer year,
                                                   @RequestParam("month") Integer month,
                                                   @RequestParam(value = "day", required = false) String day) {
        List<Event> events = eventService.getDayEvent(year,month,day);
        ResponseEntity<List<Event>> responseEntity = new ResponseEntity<>(events, HttpStatus.OK);
        return responseEntity;
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id){
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/events/daily/{day}")
    public ResponseEntity<Void> deleteDayEventAll(@PathVariable("day") String day){
        eventService.deleteDayEventAll(day);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/daily-register-count")
    public ResponseEntity<CountResponse> countDayEvent(@RequestParam(value="date")String day){
        CountResponse countResponse = eventService.countDayEvent(day);
        return new ResponseEntity<>(countResponse,HttpStatus.OK);
    }

}
