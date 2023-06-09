package ru.practicum.ewm.extension;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ToString
@EqualsAndHashCode
public class CustomPageableParameters {
    private final Long offset;

    private final Integer size;

    protected CustomPageableParameters(Long offset, Integer size) {
        this.offset = offset;
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public Long getOffset() {
        return offset;
    }

    public static CustomPageableParameters of(Long offset, Integer size) {
        return new CustomPageableParameters(offset, size);
    }

    public static CustomPageableParameters of(Integer offset, Integer size) {
        return new CustomPageableParameters(Long.valueOf(offset), size);
    }

    public Pageable toPageable() {
        return ExtendedPageRequest.ofOffset(this.offset, this.size);
    }

    public Pageable toPageable(Sort sort) {
        return ExtendedPageRequest.ofOffset(this.offset, this.size, sort);
    }
}

