package org.example.logic;

import lombok.NoArgsConstructor;
import org.example.constants.Orientation;
import org.example.dto.SimulationRulesDto;
import org.example.exceptions.SimulationDataNotInitializedException;
import org.example.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.example.constants.Orientation.getOrientation;

@NoArgsConstructor
public class Simulator {
    private List<Player> playerList;
    private Integer tourCounter = 0;
    private TreasureMap treasureMap;
    private static final Logger LOGGER = LoggerFactory.getLogger(Simulator.class);

    /**
     * Méthode qui lance la simulation
     * @param simulationRulesFilePath The path to the file containing the simulation rules
     * @return The simulation final result on String format
     */
    public String startSimulation(String simulationRulesFilePath) {
        Initializer initializer = new Initializer();
        SimulationRulesDto simulationRulesDto = initializer.initializeSimulationRules(simulationRulesFilePath);

        LOGGER.info("SimulationRules : {}", simulationRulesDto);

        initializeSimulatorData(simulationRulesDto);

        final String simulationReport;
        List<Player> playersFreeForNextMove = playersFreeForNextMove(this.playerList);

        LOGGER.info("Tour number at start : {}", tourCounter++);
        LOGGER.info("PlayerList at start : {}", playerList);

        while (!playersFreeForNextMove.isEmpty()) {
            Map<Integer, Player> playersFreeForNextMoveMap = playersFreeForNextMove.stream().collect(Collectors.toMap(Player::getOrder, player -> player));

            this.playerList.replaceAll(player -> playersFreeForNextMoveMap.containsKey(player.getOrder()) ? makeNextMove(playersFreeForNextMoveMap.get(player.getOrder())) : player);

            playersFreeForNextMove = playersFreeForNextMove(this.playerList);

            LOGGER.info("Tour number : {}", tourCounter++);
            LOGGER.info("PlayerList : {}", playerList);
        }

        simulationReport = reportSimulationResult();
        LOGGER.info("\n Simulation result {}", simulationReport);

        return simulationReport;
    }

    private void initializeSimulatorData(SimulationRulesDto simulationRulesDto) {
        if (!nonNull(simulationRulesDto)) {
            throw new SimulationDataNotInitializedException("Simulation data not initialized, check the simulation rules file path");
        }
        else {
            simulationRulesDto.getPlayers().sort(Comparator.comparing(Player::getOrder));

            this.tourCounter = 0;
            this.playerList = simulationRulesDto.getPlayers();
            this.treasureMap = simulationRulesDto.getMap();

            LOGGER.info("Simulation Data initialisation done ! ");
        }
    }

    private Player makeNextMove(Player player) {

        switch (player.getPlayerMoves().getFirst()) {
            case GAUCHE -> player.turn(this.turnLeft(player.getCurrentPosition()).getOrientation());
            case DROITE -> player.turn(this.turnRight(player.getCurrentPosition()).getOrientation());
            case AVANCER ->
                    player.moveForward(moveForwardByCheckingIfTreasureMountainOrAnotherPlayerInFront(player));
        }

        player.getPlayerMoves().removeFirst();

        return player;
    }

    private Location moveForwardByCheckingIfTreasureMountainOrAnotherPlayerInFront(Player player) {

        if(!isThereAMountain(player) && !isThereAPlayerInFrontOfMe(player)) {

            addTreasurIfTherIsOneOnNextLocation(player);
            return moveForward(this.treasureMap.getMapDimensions(), player.getCurrentPosition()).getLocation();
        }
        else {
            return player.getCurrentPosition().getLocation();
        }
    }

    private void addTreasurIfTherIsOneOnNextLocation(Player player) {
        Location nextLocation = moveForward(this.treasureMap.getMapDimensions(), player.getCurrentPosition()).getLocation();

        if (isThereATreasure(nextLocation)) {

            player.getTreasureChest().add(new Treasure(1, nextLocation));

            Treasure treasureToUpdate = this.treasureMap.getTreasures().stream().filter(treasure -> treasure.getTreasureCoordinates().equals(nextLocation)).toList().getFirst();

            this.treasureMap.getTreasures().remove(treasureToUpdate);
            treasureToUpdate.setNumberOfTreasure(treasureToUpdate.getNumberOfTreasure() - 1);
            this.treasureMap.getTreasures().add(treasureToUpdate);
        }
    }

    private boolean isThereAMountain(Player player) {
        return this.treasureMap.getMountains()
                .stream()
                .map(Mountain::getMountainCoordinates)
                .toList()
                .contains(processNextCoordinates(player, this.treasureMap).getLocation());
    }

    private boolean isThereAPlayerInFrontOfMe(Player player) {
        return this.playerList.stream().anyMatch(player1 -> player1.getCurrentPosition().getLocation().equals(processNextCoordinates(player, this.treasureMap).getLocation()));
    }

    private List<Player> playersFreeForNextMove(List<Player> players) {
        //On regroupe tous les joeurs par leur prochaines positions de façon  à avoir d'un côté les positions et de l'autre, la liste des joueurs dont ce sera la prochaine position
        Map<Location, List<Player>> groupedPlayersBySameLocation = players.stream()
                .filter(Player::isThereAnotherMove)
                .collect(Collectors.groupingBy(player -> processNextCoordinates(player, this.treasureMap).getLocation()));

        //on trie cette liste de joueurs par leur ordre d'apparition sur la carte avec comme 0, ordre du 1er joueur
        Map<Location, List<Player>> groupedPlayersBySameLocationAndSortedPlayersByOrder = groupedPlayersBySameLocation.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream().sorted(Comparator.comparingInt(Player::getOrder)).toList()));

        //On prend à chaque fois le 1er joueur des liste de joueurs parce quece sont eux qui ont lapriorité sur la prochaine position à atteindre
        return new ArrayList<>(groupedPlayersBySameLocationAndSortedPlayersByOrder.keySet().stream()
                .map(location -> groupedPlayersBySameLocationAndSortedPlayersByOrder.get(location).getFirst())
                .toList());
    }

    private Position processNextCoordinates(Player player, TreasureMap treasureMap) {
        Position nextPosition;

        nextPosition = switch (player.getPlayerMoves().getFirst()) {
            case DROITE -> turnRight(player.getCurrentPosition());
            case GAUCHE -> turnLeft(player.getCurrentPosition());
            case AVANCER -> moveForward(treasureMap.getMapDimensions(), player.getCurrentPosition());
        };

        return nextPosition;
    }

    /**
     * Méthode pour faire avancer notre petit chercheur de trésors
     */
    private Position moveForward(MapDimensions mapDimensions, Position currentPosition) {
        return new Position(getNextPositionFromOrientation(currentPosition, mapDimensions), currentPosition.getOrientation());
    }

    /**
     * Méthode pour faire tourner à gauche notre petit chercheur de trésors
     */
    private Position turnLeft(Position currentPosition) {
        int orientationIntValue;

        if(((currentPosition.getOrientation().getDirectionValue() - 1) < 1)) {
            orientationIntValue = 4;
        }
        else {
            orientationIntValue = currentPosition.getOrientation().getDirectionValue() - 1;
        }

        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        if (orientation.isPresent()) {
            return new Position(currentPosition.getLocation(), orientation.get());
        } else {
            throw new IllegalArgumentException(" The orientation given does not correspond to any existing orientation ! ");
        }
    }

    /**
     * Méthode pour faire tourner à droite notre petit chercheur de trésors
     */
    private Position turnRight(Position currentPosition) {
        int orientationIntValue;

        if(((currentPosition.getOrientation().getDirectionValue() + 1) > 4)) {
            orientationIntValue = 1;
        }
        else {
            orientationIntValue = currentPosition.getOrientation().getDirectionValue() + 1;
        }

        Optional<Orientation> orientation = getOrientation(orientationIntValue);

        if (orientation.isPresent()) {
            return new Position(currentPosition.getLocation(), orientation.get());
        } else {
            throw new IllegalArgumentException(" The orientation given does not correspond to any existing orientation ! ");
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

        return  new Location(nextX, nextY);
    }

    private boolean isThereATreasure(Location nextLocation) {
        List<Treasure> treasuresAtPlayerLocation = this.treasureMap.getTreasures().stream()
                .filter(treasure -> treasure.getTreasureCoordinates().equals(nextLocation))
                .toList();

        return !treasuresAtPlayerLocation.isEmpty() && !treasuresAtPlayerLocation.getFirst().getNumberOfTreasure().equals(0);
    }

    /**
     * Méthode pour vérifier si les futures coordonnées dépassent la taille de la carte
     */
    private boolean exceedMapSize(Integer mapSize, Integer coordinateToEvaluate) {
        return ((coordinateToEvaluate > (mapSize-1)) || (coordinateToEvaluate < 0));
    }

    private String reportMap() {
        final String mapPrefix = "C";
        final String mapDimensionX = this.treasureMap.getMapDimensions().getMapDimensionX().toString();
        final String mapDimensionY = this.treasureMap.getMapDimensions().getMapDimensionY().toString();

        return Stream.of(mapPrefix, mapDimensionX, mapDimensionY).map(s -> s + " ").collect(Collectors.joining("- "));
    }

    private String createMountainLine(Mountain mountain) {
        final String mountainPrefix = "M";
        final String mountainCoordinatesX = mountain.getMountainCoordinates().getX().toString();
        final String mountainCoordinatesY = mountain.getMountainCoordinates().getY().toString();

        return Stream.of(mountainPrefix, mountainCoordinatesX, mountainCoordinatesY).map(s -> s + " ").collect(Collectors.joining("- "));
    }

    private String createTreasureLine(Treasure treasure) {
        final String treasurePrefix = "T";
        final String treasureCoordinatesX = treasure.getTreasureCoordinates().getX().toString();
        final String treasureCoordinatesY = treasure.getTreasureCoordinates().getY().toString();
        final String numberOfTreasure = treasure.getNumberOfTreasure().toString();

        return Stream.of(treasurePrefix, treasureCoordinatesX, treasureCoordinatesY, numberOfTreasure).map(s -> s + " ").collect(Collectors.joining("- "));
    }

    private String createPlayerLine(Player player) {
        final String playerPrefix = "A";
        final String playerName = player.getName();
        final String playerPositionX = player.getCurrentPosition().getLocation().getX().toString();
        final String playerPositionY = player.getCurrentPosition().getLocation().getY().toString();
        final String playerOrientation = String.valueOf(player.getCurrentPosition().getOrientation().toString().charAt(0));
        final String playerTreasureChest = String.valueOf(player.getTreasureChest().size());

        return Stream.of(playerPrefix, playerName, playerPositionX, playerPositionY, playerOrientation, playerTreasureChest).map(s -> s + " ").collect(Collectors.joining("- "));
    }

    private String reportPlayer() {
        return this.playerList.stream().map(this::createPlayerLine).collect(Collectors.joining("\n"));
    }

    private String reportTreasure() {
        return this.treasureMap.getTreasures().stream().map(this::createTreasureLine).collect(Collectors.joining("\n"));
    }

    private String reportMountain() {
        return this.treasureMap.getMountains().stream().map(this::createMountainLine).collect(Collectors.joining("\n"));
    }

    private String reportSimulationResult() {
        final String mapReport = reportMap();
        final String moutainReport = reportMountain();
        final String treasureReport = reportTreasure();
        final String playerReport = reportPlayer();

        return Stream.of(mapReport, moutainReport, treasureReport, playerReport)
                .filter(report -> !report.isBlank())
                .collect(Collectors.joining("\n"));
    }

}
