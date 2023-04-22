package com.nhnacademy.spring.todo.repository;

import com.nhnacademy.spring.todo.domain.Event;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MapMemoryRepository implements MemoryRepository{

    private final ConcurrentMap<String,List<Event>> eventMap = new ConcurrentHashMap<>();
    private final AtomicLong autoIdx = new AtomicLong();

    @Override
    public long save(String userId,String subject,String eventAt) {
        Event event = new Event(userId,subject,eventAt);
        Long id = autoIdx.getAndIncrement();
        event.setId(id);
        if(eventMap.containsKey(eventAt)){
            List<Event> events = eventMap.get(eventAt);
            events.add(event);
        } else{
            List<Event> events = new ArrayList<>();
            events.add(event);
            eventMap.put(eventAt,events);
        }
        return id;
    }
    @Override
    public Event getEvent(long id) {
        List<Event> eventList = getAllEvent();
        return eventList.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst().orElse(null);
    }
    @Override
    public List<Event> getDayEventList(String todoDate) {
            List<Event> events = getAllEvent();
            return events.isEmpty()? Collections.emptyList():events.stream()
                    .filter(o->o.getEventAt().equals(todoDate))
                    .collect(Collectors.toList());
    }
    @Override
    public List<Event> getMonthEventList(String todoDate) {
        List<Event> events = getAllEvent();
        if (!events.isEmpty()) {
            return events.stream()
                    .filter(o->o.getEventAt().startsWith(todoDate))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<Event> getAllEvent() {
      return eventMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        List<Event> events = getAllEvent();
        Event event = getEvent(id);
        events.remove(event);
        eventMap.replace(event.getEventAt(),events);
    }

    @Override
    public void deleteByDate(String todoDate) {
       eventMap.get(todoDate).clear();
    }

    @Override
    public int countByTodoDate(String todoDate) {
        if(Objects.isNull(eventMap.get(todoDate))){
            return 0;
        }else {
        return eventMap.get(todoDate).size();
        }
    }

}
