package org.example.logic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.constants.Orientation;
import org.example.dto.PlayerActionsDto;
import org.example.map.MapDimensions;
import org.example.map.Point;
import org.example.map.Position;
import org.example.map.TreasureMap;

import java.util.Optional;

import static org.example.constants.Orientation.getOrientation;

@Getter
@RequiredArgsConstructor
public class Player {
    private final Integer order;
    private final String playerName;
    private Position currentPosition;
    private final Position startPosition;
    private final TreasureMap treasureMap;
    private final PlayerActionsDto playerActionsDto;

    /**
     * Méthode pour faire avancer notre petit chercheur de trésors
     */
    public void avancer() {
        this.currentPosition.setLocation(getPositionFromOrientation(this.currentPosition, this.treasureMap.getMapDimensions()));
    }

    /**
     * Méthode pour faire tourner à gauche notre petit chercheur de trésors
     */
    public void tournerAGauche() {
        final Integer orientationIntValue = ((this.currentPosition.getOrientation().getDirectionValue() - 1) % 4);
        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        orientation.ifPresent(orientation1 -> this.currentPosition.setOrientation(orientation1));

        if (orientation.isPresent()) {
            this.currentPosition.setOrientation(orientation.get());
        } else {
            throw new IllegalArgumentException("L'orientation donnée ne correspond à aucune Orientation existante ! ");
        }
    }

    /**
     * Méthode pour faire tourner à droite notre petit chercheur de trésors
     */
    public void tournerADroite() {
        final Integer orientationIntValue = ((this.currentPosition.getOrientation().getDirectionValue() + 1) % 4);
        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        if (orientation.isPresent()) {
            this.currentPosition.setOrientation(orientation.get());
        } else {
            throw new IllegalArgumentException("L'orientation donnée ne correspond à aucune Orientation existante ! ");
        }
    }

    /**
     * Méthode pour calculer la position x et y de notre petit chercheur de trésors
     */
    private Point getPositionFromOrientation(final Position currentPosition, final MapDimensions map) {
        Integer currentX = currentPosition.getLocation().getX();
        Integer currentY = currentPosition.getLocation().getY();

        switch (currentPosition.getOrientation()) {
            case NORD -> currentY = exceedMapSize(map.getMapDimensionY(), currentY -= 1) ? currentY : currentY - 1;
            case EST -> currentX = exceedMapSize(map.getMapDimensionX(), currentX += 1) ? currentX : currentX + 1;
            case SUD -> currentY = exceedMapSize(map.getMapDimensionY(), currentY += 1) ? currentY : currentY + 1;
            case OUEST -> currentX = exceedMapSize(map.getMapDimensionX(), currentX -= 1) ? currentX : currentX - 1;
        }

            return new Point(currentX, currentY);
    }

    /**
     * Méthode pour vérifier si les futures coordonnées dépassent la taille de la carte
     */
    private boolean exceedMapSize(Integer mapSize, Integer coordinateToEvaluate) {
        return ((coordinateToEvaluate > mapSize) || (coordinateToEvaluate <0));
    }
}
