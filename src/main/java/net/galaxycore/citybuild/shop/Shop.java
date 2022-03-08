package net.galaxycore.citybuild.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
public class Shop implements Serializable {
    private int player;
    private long price;
    private Map<String, Object> itemStack;
    private int len;
    private int cx;
    private int cy;
    private int cz;
}
