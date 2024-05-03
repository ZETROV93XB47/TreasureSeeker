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
    private final String playerName;
    private final Position currentPosition;
    private final List<Action> playerMoves;
    private final List<Treasure> treasureChest = new ArrayList<>();

    /**
     * Méthode pour faire avancer notre petit chercheur de trésors
     */
    public void avancer(Location location) {
        this.currentPosition.setLocation(location);
    }

    /**
     * Méthode pour faire tourner à gauche notre petit chercheur de trésors
     */
    public void tournerAGauche(Orientation orientation) {
        this.currentPosition.setOrientation(orientation);
    }

    /**
     * Méthode pour faire tourner à droite notre petit chercheur de trésors
     */
    public void tournerADroite(Orientation orientation) {
        this.currentPosition.setOrientation(orientation);
    }


    public boolean isThereAnotherMove() {
        return !playerMoves.isEmpty();
    }
}
