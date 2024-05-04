package org.example;

import org.example.logic.Simulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String SIMULATION_RULES_FILE_PATH_RELATIVE = "src/main/resources/simulationRules.txt";

    public static void main(String[] args) {
        LOGGER.info("Welcome to Treasure Seeker 😁");

        try {
            Simulator simulator = new Simulator();
            simulator.startSimulation(SIMULATION_RULES_FILE_PATH_RELATIVE);

        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }
}