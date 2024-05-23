package org.example.logic;

import org.example.dto.SimulationRulesDto;
import org.example.exceptions.LineLengthNotMatchingException;
import org.example.map.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.example.enums.Action.AVANCER;
import static org.example.enums.Orientation.EST;
import static org.example.enums.Orientation.SUD;

class InitializerTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("expectedExceptionBasedOnEachKindOfErrorInSimulationRulesFile")
    void shouldthrowMatchingExceptionWithTheIssueOnTheSimulationRulesFile(String testCaseName, String simulationRulesSourceFile, String expectedMessage, Class<? extends Throwable> expectedExceptionClass) {
        final Initializer initializer = new Initializer();

        assertThatExceptionOfType(expectedExceptionClass)
                .isThrownBy(() -> initializer.initializeSimulationRules(simulationRulesSourceFile))
                .withMessage(expectedMessage);
    }

    @Test
    void shouldCreateSimulationRulesDto() {
        final Initializer initializer = new Initializer();

        SimulationRulesDto simulationRulesDto = initializer.initializeSimulationRules("src/test/resources/passingTestsFiles/simulationRulesForNominalCase.txt");

        Mountain expectedMountain = new Mountain(new Location(2, 2));
        Treasure expectedTreasure = new Treasure(2, new Location(1, 1));
        TreasureMap expectedTreasureMap = new TreasureMap(new MapDimensions(3, 3), List.of(expectedTreasure), List.of(expectedMountain));

        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of(AVANCER, AVANCER));
        Player player1 = new Player(1, "Player1", new Position(new Location(0, 1), EST), List.of(AVANCER, AVANCER));

        SimulationRulesDto expectedSimulationRulesDto = SimulationRulesDto.builder()
                .map(expectedTreasureMap)
                .players(List.of(player0, player1))
                .build();

        assertThat(simulationRulesDto).isEqualTo(expectedSimulationRulesDto);
    }

    static Stream<Arguments> expectedExceptionBasedOnEachKindOfErrorInSimulationRulesFile() {

        String expectedMessageForUnknowLineType = "Unknown line type[K, 3, 3]";
        String expectedMessageForFakeOrientationValue = "Unexpected value for Orientation : Z";
        String expectedMessageForFakeActionValue = "Unexpected value for Action : R";
        String expectedMessageForMalformedMapLine = "The Map creation line does not correspond to the expected format";
        String expectedMessageForMalformedPlayerLine = "Player creation line does not match expected format";
        String expectedMessageForMalformedTreasureLine = "The line for creating Treasure coordinates does not correspond to the expected format";
        String expectedMessageForMalformedMountainLine = "The Mountain creation line does not correspond to the expected format";

        return Stream.of(
                Arguments.of("Unknown line type Case", "src/test/resources/blockingTestsFiles/simulationRulesForUnkownLineTypeException.txt", expectedMessageForUnknowLineType, IllegalArgumentException.class),
                Arguments.of("Malformed player line Case", "src/test/resources/blockingTestsFiles/simulationRulesForMalformedPlayerLine.txt", expectedMessageForMalformedPlayerLine, LineLengthNotMatchingException.class),
                Arguments.of("Malformed treasure line Case", "src/test/resources/blockingTestsFiles/simulationRulesForMalformedTreasureLine.txt", expectedMessageForMalformedTreasureLine, LineLengthNotMatchingException.class),
                Arguments.of("Malformed mountain line Case", "src/test/resources/blockingTestsFiles/simulationRulesForMalformedMountainLine.txt", expectedMessageForMalformedMountainLine, LineLengthNotMatchingException.class),
                Arguments.of("Unexpected Value for Orientation Case", "src/test/resources/blockingTestsFiles/simulationRulesForUnexpectedValueForOrientation.txt", expectedMessageForFakeOrientationValue, IllegalArgumentException.class),
                Arguments.of("Unexpected Value for Action Case", "src/test/resources/blockingTestsFiles/simulationRulesForUnexpectedValueForAction.txt", expectedMessageForFakeActionValue, IllegalArgumentException.class),
                Arguments.of("Malformed Map line Case", "src/test/resources/blockingTestsFiles/simulationRulesForMalformedMapLine.txt", expectedMessageForMalformedMapLine, LineLengthNotMatchingException.class)
        );
    }

}