package com.nhnacademy.spring.todo.service;

import com.nhnacademy.spring.todo.domain.CountResponse;
import com.nhnacademy.spring.todo.domain.Event;
import com.nhnacademy.spring.todo.domain.RegisterResponse;
import com.nhnacademy.spring.todo.domain.ViewResponse;
import com.nhnacademy.spring.todo.exception.NotFoundEventException;
import com.nhnacademy.spring.todo.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EventService {
    private final MemoryRepository memoryRepository;

    public RegisterResponse save(String userId, String subject, String eventAt) {
        if (userId == null || userId.isEmpty()) {
            throw new NullPointerException();
        } else {
            return new RegisterResponse(memoryRepository.save(userId, subject, eventAt));
        }
    }
    public ViewResponse getEvent(long id) {
        Event event = memoryRepository.getEvent(id);
        if(Objects.isNull(event)){
            throw new NotFoundEventException(id);
        }else {
            return new ViewResponse(event.getId(),
                    event.getSubject(),
                    event.getEventAt(),
                    event.getCreatedAt());
        }
    }
    public List<Event> getDayEvent(int year,int month, String day){
        List<Event> events;
        if (day==null) {
            String date = year + "-" + converter(month);
            events = memoryRepository.getMonthEventList(date);
        } else {
            String date = year + "-" + converter(month) + "-" + converter(Integer.parseInt(day));
            events = memoryRepository.getDayEventList(date);
        }
        return events;
    }

    public void deleteById(String id){
        long converterId = Long.parseLong(id);
        if(id==null){
            throw new NullPointerException();
        }
        if(Objects.isNull(memoryRepository.getEvent(converterId))){
            throw new NotFoundEventException(converterId);
        }
        memoryRepository.deleteById(converterId);
    }

    public void deleteDayEventAll(String day){
        if(day==null){
            throw new NullPointerException();
        }
        memoryRepository.deleteByDate(day);
    }
    public CountResponse countDayEvent(String day){
        int num = memoryRepository.countByTodoDate(day);
        return new CountResponse(num);
    }

    public String converter(int day) {
        String converterNum = String.valueOf(day);
        if (day < 10) {
            converterNum = "0" + day;
        }
        return converterNum;
    }
}
