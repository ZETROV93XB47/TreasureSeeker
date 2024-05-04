package org.example.map;

import java.util.List;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class TreasureMap {
    private final MapDimensions mapDimensions;
    private final List<Treasure> treasures;
    private final List<Mountain> mountains;
}
