package organisms;

import simulator.AntSimulator;
import simulator.Point;

public class Queen {
    int health;
    int energy;
    public Point position;

    public Queen(int health, int energy, Point position) {
        this.health = health;
        this.energy = energy;
        this.position = position;
    }

    public void layEgg() {
        if (energy >= 200) {
            AntSimulator.getInstance().ants.add(new Ant(50, 50, new Point(position.x, position.y)));
            energy -= 200;
        }
    }

    public void addEnergy(){
        energy += 50;
    }
}