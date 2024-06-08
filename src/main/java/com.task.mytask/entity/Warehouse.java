package com.task.mytask.entitiy;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@Getter
public class Warehouse {
    private final String id;
    private final List<String> sellers;
    private final ConcurrentHashMap<LocalDate, Integer> capacities;
}
