package organisms;

import simulator.AntSimulator;
import simulator.Point;

public class Pheromone {
    public Point position;
    public int Strength;

    // increase the strength based on how far away it is from the queen
    Pheromone(Point position) {
        this.position = position;

        // increase strength based on distance from queen
        this.Strength = 1000 + ((int) Math.sqrt(Math.pow(AntSimulator.getInstance().queen.position.x - position.x, 2) + Math.pow(AntSimulator.getInstance().queen.position.y - position.y, 2)) * 2000) ;
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