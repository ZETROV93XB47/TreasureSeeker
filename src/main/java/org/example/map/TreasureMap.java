package org.example.map;

import java.util.List;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TreasureMap {
    private final MapDimensions mapDimensions;
    private final List<Treasure> treasures;
    private final List<Mountain> mountains;
}
