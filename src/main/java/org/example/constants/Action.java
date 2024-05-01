package org.example.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    AVANCER(1),
    GAUCHE(2),
    DROITE(3);

    private final Integer actionValue;
}
