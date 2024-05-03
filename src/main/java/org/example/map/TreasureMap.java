package org.example.map;

import java.util.List;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class TreasureMap {
    private final MapDimensions mapDimensions;
    private final List<Treasure> treasures;
    private final List<Mountain> mountains;
}
