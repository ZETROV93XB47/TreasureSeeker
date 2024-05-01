package org.example.constants;

import java.util.Arrays;
import lombok.Getter;
import java.util.Optional;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum Orientation {
    NORD(1),
    EST(2),
    SUD(3),
    OUEST(4);

    private final Integer directionValue;

    public static Optional<Orientation> getOrientation(Integer orientation) {
        return Arrays.stream(values())
                .filter(orientation1 -> orientation1.getDirectionValue().equals(orientation))
                .findFirst();
    }
}
