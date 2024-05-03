package org.example.logic;

import org.example.constants.Orientation;
import org.example.dto.SimulationRulesDto;
import org.example.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.example.constants.Orientation.getOrientation;

public class Simulator {
    private Integer tourCounter = 0;
    private final List<Player> playerList;
    private final TreasureMap treasureMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);

    public Simulator(SimulationRulesDto simulationRulesDto) {
        simulationRulesDto.getPlayers().sort(Comparator.comparing(Player::getOrder));

        this.playerList = simulationRulesDto.getPlayers();
        this.treasureMap = simulationRulesDto.getMap();
    }

    public void startSimulation() {

        List<Player> playersFreeForNextMove = playersFreeForNextMove(this.playerList);

        LOGGER.info("nombre de tours : {}", tourCounter);
        LOGGER.info("playerList init : {}", playerList);
        LOGGER.info("playersFreeForNextMove init : {}", playersFreeForNextMove);

        while (!playersFreeForNextMove.isEmpty()) {
            Map<Integer, Player> playersFreeForNextMoveMap = playersFreeForNextMove.stream().collect(Collectors.toMap(Player::getOrder, player -> player));

            this.playerList.replaceAll(player -> playersFreeForNextMoveMap.containsKey(player.getOrder()) ? makeNextMove(playersFreeForNextMoveMap.get(player.getOrder())) : player);

            playersFreeForNextMove = playersFreeForNextMove(this.playerList);

            tourCounter++;
            LOGGER.info("nombre de tours : {}", tourCounter);
            LOGGER.info("playerList : {}", playerList);
            LOGGER.info("playersFreeForNextMove : {}", playersFreeForNextMove);
        }

        //TODO: Reste à implémenter
    }

    private Player makeNextMove(Player player) {

        switch (player.getPlayerMoves().getFirst()) {
            case GAUCHE -> player.tournerAGauche(tournerAGauche(player.getCurrentPosition()).getOrientation());
            case DROITE -> player.tournerADroite(tournerADroite(player.getCurrentPosition()).getOrientation());
            case AVANCER ->
                    player.avancer(avancer(this.treasureMap.getMapDimensions(), player.getCurrentPosition()).getLocation());
        }

        player.getPlayerMoves().removeFirst();

        if (isThereATreasure(player.getCurrentPosition().getLocation())) {
            final Location playerLocation = player.getCurrentPosition().getLocation();

            player.getTreasureChest().add(new Treasure(1, playerLocation));

            Treasure treasureToUpdtae = this.treasureMap.getTreasures().stream().filter(treasure -> treasure.getTreasureCoordinates().equals(playerLocation)).toList().getFirst();

            this.treasureMap.getTreasures().remove(treasureToUpdtae);
            treasureToUpdtae.setNumberOfTreasure(treasureToUpdtae.getNumberOfTreasure() - 1);
            this.treasureMap.getTreasures().add(treasureToUpdtae);
        }

        return player;
    }

    private List<Player> playersFreeForNextMove(List<Player> players) {
        Map<Location, List<Player>> groupedPlayersBySameLocation = players.stream().filter(Player::isThereAnotherMove).collect(Collectors.groupingBy(player -> processNextCoordinates(player, this.treasureMap).getLocation()));

        Map<Location, List<Player>> groupedPlayersBySameLocationAndSortedPlayersByOrder = groupedPlayersBySameLocation.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().sorted(Comparator.comparingInt(Player::getOrder)).toList()));

        return groupedPlayersBySameLocationAndSortedPlayersByOrder.keySet().stream().map(location -> groupedPlayersBySameLocationAndSortedPlayersByOrder.get(location).getFirst()).toList();
    }

    private Position processNextCoordinates(Player player, TreasureMap treasureMap) {
        Position nextPosition;

        nextPosition = switch (player.getPlayerMoves().getFirst()) {
            case DROITE -> tournerADroite(player.getCurrentPosition());
            case GAUCHE -> tournerAGauche(player.getCurrentPosition());
            case AVANCER -> avancer(treasureMap.getMapDimensions(), player.getCurrentPosition());
        };

        return nextPosition;
    }

    /**
     * Méthode pour faire avancer notre petit chercheur de trésors
     */
    private Position avancer(MapDimensions mapDimensions, Position currentPosition) {
        return new Position(getNextPositionFromOrientation(currentPosition, mapDimensions), currentPosition.getOrientation());
    }

    /**
     * Méthode pour faire tourner à gauche notre petit chercheur de trésors
     */
    private Position tournerAGauche(Position currentPosition) {
        final Integer orientationIntValue = ((currentPosition.getOrientation().getDirectionValue() - 1) % 4);
        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        orientation.ifPresent(currentPosition::setOrientation);

        if (orientation.isPresent()) {
            return new Position(currentPosition.getLocation(), orientation.get());
        } else {
            throw new IllegalArgumentException("L'orientation donnée ne correspond à aucune Orientation existante ! ");
        }
    }

    /**
     * Méthode pour faire tourner à droite notre petit chercheur de trésors
     */
    private Position tournerADroite(Position currentPosition) {
        final Integer orientationIntValue = ((currentPosition.getOrientation().getDirectionValue() + 1) % 4);
        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        if (orientation.isPresent()) {
            return new Position(currentPosition.getLocation(), orientation.get());
        } else {
            throw new IllegalArgumentException("L'orientation donnée ne correspond à aucune Orientation existante ! ");
        }
    }

    /**
     * Méthode pour calculer la position x et y de notre petit chercheur de trésors
     */
    private Location getNextPositionFromOrientation(final Position currentPosition, final MapDimensions map) {
        int currentX = currentPosition.getLocation().getX();
        int currentY = currentPosition.getLocation().getY();
        int nextX = currentX;
        int nextY = currentY;

        switch (currentPosition.getOrientation()) {
            case NORD -> nextY = exceedMapSize(map.getMapDimensionY(), currentY - 1) ? currentY : currentY - 1;
            case EST -> nextX = exceedMapSize(map.getMapDimensionX(), currentX + 1) ? currentX : currentX + 1;
            case SUD -> nextY = exceedMapSize(map.getMapDimensionY(), currentY + 1) ? currentY : currentY + 1;
            case OUEST -> nextX = exceedMapSize(map.getMapDimensionX(), currentX - 1) ? currentX : currentX - 1;
        }

        return isThereAMountain(new Location(nextX, nextY)) ? new Location(currentX, currentY) : new Location(nextX, nextY);
    }

    private boolean isThereAMountain(Location nextLocation) {
        return this.treasureMap.getMountains().stream()
                .map(Mountain::getMountainCoordinates)
                .toList()
                .contains(nextLocation);
    }

    private boolean isThereATreasure(Location nextLocation) {
        List<Treasure> treasuresAtPlayerLocation = this.treasureMap.getTreasures().stream()
                .filter(treasure -> treasure.getTreasureCoordinates().equals(nextLocation))
                .toList();

        return !treasuresAtPlayerLocation.isEmpty() &&  !treasuresAtPlayerLocation.getFirst().getNumberOfTreasure().equals(0);
    }

    /**
     * Méthode pour vérifier si les futures coordonnées dépassent la taille de la carte
     */
    private boolean exceedMapSize(Integer mapSize, Integer coordinateToEvaluate) {
        return ((coordinateToEvaluate > mapSize) || (coordinateToEvaluate < 0));
    }

}
