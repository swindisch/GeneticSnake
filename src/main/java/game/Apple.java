package game;

import lombok.Getter;

@Getter
public class Apple {
    private int posX;
    private int posY;

    public Apple(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }
}
