package org.example;

import org.example.dto.SimulationRulesDto;
import org.example.logic.Simulator;
import org.example.logic.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String SIMULATION_RULES_FILE_PATH = "C:\\Users\\guill\\Documents\\IdeaProjects\\TreasureSeeker\\src\\main\\resources\\simulationRules.txt";


    public static void main(String[] args) {
        LOGGER.info("Welcome to Treasure Seeker 😁");

        Initializer initializer = new Initializer();
        SimulationRulesDto rules = initializer.initialize(SIMULATION_RULES_FILE_PATH);
        LOGGER.info(rules.toString());

        Simulator simulator = new Simulator(rules);

        simulator.startSimulation();

    }
}