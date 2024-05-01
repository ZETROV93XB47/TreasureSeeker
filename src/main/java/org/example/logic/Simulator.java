package org.example.logic;

import lombok.AllArgsConstructor;
import org.example.map.TreasureMap;

import java.util.List;

@AllArgsConstructor
public class Simulator {
    private final List<Player> playerList;
    private final TreasureMap treasureMap;

}
