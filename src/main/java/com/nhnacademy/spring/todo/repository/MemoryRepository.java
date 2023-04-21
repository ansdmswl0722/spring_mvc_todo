package com.nhnacademy.spring.todo.repository;

import com.nhnacademy.spring.todo.domain.Event;

import java.util.List;

public interface MemoryRepository {

    long save(String userId, String subject, String eventAt);
    Event getEvent(long id);
    List<Event> getDayEventList(String todoDate);
    List<Event> getMonthEventList(String userId);
    void deleteById(long id);
    void deleteByDate(String todoDate);
    int countByTodoDate(String todoDate);

}
