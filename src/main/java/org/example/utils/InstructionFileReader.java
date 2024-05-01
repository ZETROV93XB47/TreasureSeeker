package org.example.utils;

import lombok.RequiredArgsConstructor;
import org.example.constants.Orientation;
import org.example.dto.PlayerActionsDto;
import org.example.dto.SimulationRulesDto;
import org.example.logic.Player;
import org.example.map.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.constants.Orientation.*;


@RequiredArgsConstructor
public class InstructionFileReader {
    private static final String MAP_LINE_TYPE = "C";
    private static final String PLAYER_LINE_TYPE = "A";
    private static final String TREASURE_LINE_TYPE = "T";
    private static final String MOUNTAIN_LINE_TYPE = "M";
    private static final String SIMULATION_RULES_FILE_PATH = "simulationRules.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(InstructionFileReader.class);

    public SimulationRulesDto initialize() {
        Integer order = 0;
        MapDimensions dimensions = null;
        final List<Player> players = new ArrayList<>();
        final List<Treasure> treasures = new ArrayList<>();
        final List<Mountain> mountains = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(SIMULATION_RULES_FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().replace(" ", "").split("-");

                switch (line[0]) {
                    case MAP_LINE_TYPE -> dimensions = createMapFromLine(line);
                    case TREASURE_LINE_TYPE -> treasures.add(createTreasureFromLine(line));
                    case PLAYER_LINE_TYPE -> players.add(createPlayerFromLine(line, order++));
                    case MOUNTAIN_LINE_TYPE -> mountains.add(createMountainFromLine(line));

                    default -> throw new IllegalArgumentException("Unknown line type");
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("File not found at {}", SIMULATION_RULES_FILE_PATH);
            LOGGER.error("Exception Message {}", e.getMessage());
            //e.printStackTrace();
        }

        return new SimulationRulesDto(new TreasureMap(dimensions, treasures, mountains), players);
    }

    private Player createPlayerFromLine(String line[], Integer order) {

        Orientation orientation = switch (line[4]) {
            case "S" -> SUD;
            case "N" -> NORD;
            case "O" -> OUEST;
            case "E" -> EST;

            default -> throw new IllegalStateException("Unexpected value: " + line[4]);
        };

        final String playerName = line[1];
        final PlayerActionsDto playerActionsDto = new PlayerActionsDto(line[5]);
        final Position playerStartPosition = new Position(new Point(Integer.valueOf(line[2]), Integer.valueOf(line[3])), orientation);
        final MapDimensions mapDimensions = new MapDimensions(null, null);

        return new Player(order, playerName, mapDimensions, playerStartPosition, playerActionsDto);
    }

    private MapDimensions createMapFromLine(String line[]) {
        return new MapDimensions(Integer.valueOf(line[1]), Integer.valueOf(line[2]));
    }

    private Treasure createTreasureFromLine(String line[]) {
        return new Treasure(Integer.valueOf(line[3]), new Point(Integer.valueOf(line[1]), Integer.valueOf(line[2])));
    }

    private Mountain createMountainFromLine(String line[]) {
        return new Mountain(new Point(Integer.valueOf(line[1]), Integer.valueOf(line[2])));
    }

}
