package game;

import lombok.Getter;

import java.awt.*;
import java.util.ArrayList;

@Getter
public class Perspective {

    public static int sights = 8;
    ArrayList<Point> sightsList;

    public Perspective() {
        sightsList = new ArrayList<>(sights);

        sightsList.add(new Point(0, -1));
        sightsList.add(new Point(1, -1));
        sightsList.add(new Point(1, 0));
        sightsList.add(new Point(1, 1));
        sightsList.add(new Point(0, 1));
        sightsList.add(new Point(-1, 1));
        sightsList.add(new Point(-1, 0));
        sightsList.add(new Point(-1, -1));
    }
}
