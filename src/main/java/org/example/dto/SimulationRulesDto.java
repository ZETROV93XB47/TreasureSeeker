package org.example.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.logic.Player;
import org.example.map.TreasureMap;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class SimulationRulesDto {
    private final TreasureMap map;
    private final List<Player> players;
}
