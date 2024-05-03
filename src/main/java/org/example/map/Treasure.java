package org.example.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Treasure {
    private Integer numberOfTreasure;
    private final Location treasureCoordinates;
}
