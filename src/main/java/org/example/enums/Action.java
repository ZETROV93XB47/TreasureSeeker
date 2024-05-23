package org.example.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Action {
    AVANCER(1),
    GAUCHE(2),
    DROITE(3);

    private final Integer actionValue;
}
