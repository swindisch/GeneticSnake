package game;

import lombok.Data;

@Data
public class SnakeHead {
    private int posX = 0;
    private int posY = 0;
    private Direction dir = Direction.NOP;
}
