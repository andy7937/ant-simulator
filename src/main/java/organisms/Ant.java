package organisms;
import java.util.Iterator;

import simulator.AntSimulator;
import simulator.Point;

public class Ant {
    public int health;
    public int energy;
    public String direction;
    public Point position;
    public boolean hasFood;

    public Ant(int health, int energy, Point position) {
        this.health = health;
        this.energy = energy;
        this.position = position;
        this.direction = "N";
        this.hasFood = false;
    }

    public void move() {

        if (hasFood == true){
            queenDirection();

             if (direction == "N") {
            position.y--;
        } else if (direction == "S") {
            position.y++;
        } else if (direction == "E") {
            position.x++;
        } else if (direction == "W") {
            position.x--;
        }
        releaseFoodPheromone();
        return;

        }

        findNearestFood();
        if (direction == "N") {
            position.y--;
        } else if (direction == "S") {
            position.y++;
        } else if (direction == "E") {
            position.x++;
        } else if (direction == "W") {
            position.x--;
        }

        if (checkFood(position) == true){
            eat();
        }
        releaseHomePheromone();

        energy--;
    }

    public void releaseHomePheromone() {
        AntSimulator.getInstance().homePheromones.add(new Pheromone(new Point(position.x, position.y)));
    }

    public void releaseFoodPheromone(){
        AntSimulator.getInstance().foodPheromones.add(new Pheromone(new Point(position.x, position.y)));
    }

    public void eat(){
        energy += 50;
        for (Food food : AntSimulator.getInstance().foods) {
            if (food.position.x == position.x && food.position.y == position.y) {
                AntSimulator.getInstance().foods.remove(food);
                break;
            }
        }
        hasFood = true;
    }

    public void queenDirection(){
        if (position.x == AntSimulator.getInstance().queen.position.x && position.y == AntSimulator.getInstance().queen.position.y) {
            AntSimulator.getInstance().queen.energy += 50;
            hasFood = false;
        } else {
            direction = moveDirection(AntSimulator.getInstance().queen.position);
        }
    }

    public void findNearestFood(){
        int minDistance = 1000000;
        Point nearestFood = new Point(0,0);
        for (Food food : AntSimulator.getInstance().foods) {
            int distance = Math.abs(food.position.x - position.x) + Math.abs(food.position.y - position.y);
            if (distance < minDistance) {
                minDistance = distance;
                nearestFood = food.position;
            }
        }
        direction = moveDirection(nearestFood);
    }

    public boolean checkFood(Point position) {
        for (Food food : AntSimulator.getInstance().foods) {
            if (food.position.x == position.x && food.position.y == position.y) {
                return true;
            }
        }
        return false;
    }

    public String moveDirection(Point point){
        if (point.x > position.x) {
            return "E";
        } else if (point.x < position.x) {
            return "W";
        } else if (point.y > position.y) {
            return "S";
        } else if (point.y < position.y) {
            return "N";
        }
        return "N";
    }


}