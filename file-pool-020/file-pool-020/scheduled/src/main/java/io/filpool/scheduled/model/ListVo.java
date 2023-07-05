package io.filpool.scheduled.model;

import lombok.Data;

import java.util.List;

@Data
public class ListVo<T> {
    private List<T> miners;
}
