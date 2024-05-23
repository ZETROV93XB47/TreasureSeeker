package org.example.map;

import lombok.*;
import org.example.enums.Orientation;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Position {
    private Location location;
    private Orientation orientation;
}
