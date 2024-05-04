package org.example.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
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
