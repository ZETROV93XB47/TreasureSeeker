package org.example.map;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Treasure {
    private Integer numberOfTreasure;
    private final Location treasureCoordinates;
}
