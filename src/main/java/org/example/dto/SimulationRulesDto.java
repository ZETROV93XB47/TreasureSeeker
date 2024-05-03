package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.logic.Player;
import org.example.map.TreasureMap;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class SimulationRulesDto {
    private final TreasureMap map;
    private final List<Player> players;
}
