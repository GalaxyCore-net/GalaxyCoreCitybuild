package net.galaxycore.citybuild.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Both<T, R> {
    private final T t;
    private final R r;

    public Both(T t, R r) {
        this.t = t;
        this.r = r;
    }
}
