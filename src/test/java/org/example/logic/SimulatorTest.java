package org.example.logic;

import org.example.exceptions.SimulationDataNotInitializedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class SimulatorTest {

    @Test
    void shouldThrowException() {
        Simulator simulator = new Simulator();

        assertThatExceptionOfType(SimulationDataNotInitializedException.class)
                .isThrownBy(() -> simulator.startSimulation("nonExistentFilePath"))
                .withMessage("Simulation data not initialized, check the simulation rules file path");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("expectedSimulationOutputBasedOnSimulationRulesSourceFile")
    void shouldTestSimulator(String testCaseName, String simulationRulesSourceFile, String expectedSimulationResults) {
        Simulator simulator = new Simulator();

        assertThat(simulator.startSimulation(simulationRulesSourceFile)).isEqualToIgnoringWhitespace(expectedSimulationResults);
    }

    static Stream<Arguments> expectedSimulationOutputBasedOnSimulationRulesSourceFile() {

        String expectedSimulationResultsWithNoTreasureOnMap = """ 
                \s
                C - 3 - 3\s
                M - 2 - 2\s
                A - Player0 - 1 - 2 - S - 0\s
                A - Player1 - 2 - 1 - E - 0\s
                """;

        String expectedSimulationResultsForMountainsBlockingPlayersMoves = """ 
                \s
                C - 3 - 3\s
                M - 1 - 1\s
                T - 1 - 1 - 2\s
                A - Player0 - 1 - 0 - S - 0\s
                A - Player1 - 0 - 1 - E - 0\s
                """;

        String expectedSimulationResultsForNominalCase = """ 
                \s
                C - 3 - 3\s
                M - 2 - 2\s
                T - 1 - 1 - 0\s
                A - Player0 - 1 - 2 - S - 1\s
                A - Player1 - 2 - 1 - E - 1\s
                """;

        String expectedSimulationResultsWhenPlayerInFrontOfAnotherPlayer = """
                C - 3 - 3\s
                M - 2 - 2\s
                T - 1 - 1 - 1\s
                A - Player0 - 1 - 1 - S - 1\s
                A - Player1 - 1 - 2 - N - 0\s
        """;

        return Stream.of(
                Arguments.of("Nominal Case", "src/test/resources/passingTestsFiles/simulationRulesForNominalCase.txt", expectedSimulationResultsForNominalCase),
                Arguments.of("No Treasure on Map Case", "src/test/resources/passingTestsFiles/simulationRulesWithNoreasureOnMap.txt", expectedSimulationResultsWithNoTreasureOnMap),
                Arguments.of("Blocking Mountains Case", "src/test/resources/passingTestsFiles/simulationRulesWithBlockingMountains.txt", expectedSimulationResultsForMountainsBlockingPlayersMoves),
                Arguments.of("When one player in front of another player", "src/test/resources/passingTestsFiles/simulationRulesWhenTwoPlayersFaces.txt", expectedSimulationResultsWhenPlayerInFrontOfAnotherPlayer)
        );
    }
}