package org.example.dto;

import lombok.AllArgsConstructor;
import org.example.logic.Player;
import org.example.map.TreasureMap;

import java.util.List;

@AllArgsConstructor
public class SimulationRulesDto {
    private final TreasureMap map;
    private final List<Player> players;
}
