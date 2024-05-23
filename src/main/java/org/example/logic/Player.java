package org.example.logic;

import lombok.*;
import org.example.constants.Action;
import org.example.constants.Orientation;
import org.example.map.Location;
import org.example.map.Position;
import org.example.map.Treasure;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Player {
    private final Integer order;
    private final String name;
    private final Position currentPosition;
    private final List<Action> playerMoves;
    private final List<Treasure> treasureChest = new ArrayList<>();

    /**
     * Méthode pour faire avancer notre petit chercheur de trésors
     * @param location
     */
    public void moveForward(Location location) {
        this.currentPosition.setLocation(location);
    }

    /**
     * Méthode pour faire tourner  notre petit chercheur de trésors dans un sens ou l'autre (gauche ou droite)
     * @param orientation orientation du joueur
     */
    public void turn(Orientation orientation) {
        this.currentPosition.setOrientation(orientation);
    }

    /**
     * Méthode qui indique si le joueur a encore des actions à effectuer ou pas
     * @return boolean
     */
    public boolean isThereAnotherMove() {
        return !playerMoves.isEmpty();
    }
}
