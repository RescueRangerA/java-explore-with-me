package ru.practicum.stats.repository;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsProjection {
    private String appId;

    private String uri;

    private Long hitsCount;
}