package org.example.map;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.example.constants.Orientation;

@Getter
@Setter
@AllArgsConstructor
public class Position {
    private Point location;
    private Orientation orientation;
}
