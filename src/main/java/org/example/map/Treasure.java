package org.example.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Treasure {
    private final Integer numberOfTreasure;
    private final Point treasureCoordinates;
}
