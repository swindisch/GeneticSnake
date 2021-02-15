package game;

import lombok.Getter;

import java.awt.*;

@Getter
public class Apple {
    private Point position;

    public Apple(Point position) {
        this.position = position;
    }
}
