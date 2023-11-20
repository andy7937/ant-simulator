package organisms;

import simulator.AntSimulator;
import simulator.Point;

public class Pheromone {
    public Point position;
    public int Strength = 1000;

    Pheromone(Point position) {
        this.position = position;
    }

    public void update(){
        Strength -= 1;

        if (Strength <= 0){
            AntSimulator.getInstance().homePheromones.remove(this);
            AntSimulator.getInstance().foodPheromones.remove(this);
        }
    }

    public void strengthen(){
        Strength += 300;
    }
}