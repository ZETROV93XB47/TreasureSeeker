package org.example.logic;

import org.example.map.Location;
import org.example.map.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.enums.Action.AVANCER;
import static org.example.enums.Orientation.*;

class PlayerTest {

    @Test
    void shouldChangeLocation() {
        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of(AVANCER, AVANCER));

        Position expectedPosition = new Position(new Location(1, 1), SUD);
        player0.moveForward(expectedPosition.getLocation());

        assertThat(player0.getCurrentPosition()).isEqualTo(expectedPosition);
    }

    @Test
    void shouldChangeOrientationToNorth() {
        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of(AVANCER, AVANCER));
        player0.turn(EST);

        assertThat(player0.getCurrentPosition().getOrientation()).isEqualTo(EST);
    }

    @Test
    void shouldChangeOrientationToWest() {
        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of(AVANCER, AVANCER));
        player0.turn(OUEST);

        assertThat(player0.getCurrentPosition().getOrientation()).isEqualTo(OUEST);
    }

    @Test
    void isThereAnotherMoveShouldReturnFalse() {
        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of());
        player0.turn(OUEST);

        assertThat(player0.isThereAnotherMove()).isFalse();
    }

    @Test
    void isThereAnotherMoveShouldReturnTrue() {
        Player player0 = new Player(0, "Player0", new Position(new Location(1, 0), SUD), List.of(AVANCER));
        player0.turn(OUEST);

        assertThat(player0.isThereAnotherMove()).isTrue();
    }

}