package organisms;
import java.util.Iterator;
import java.util.Random;

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
        releaseFoodPheromone();
        return;
        }

        findNearestFood();

        if (checkFood(position) == true){
            eat();
            return;
        }
        releaseHomePheromone();
        
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
            moveDirection(AntSimulator.getInstance().queen.position);
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
        moveDirection(nearestFood);
    }

    public boolean checkFood(Point position) {
        for (Food food : AntSimulator.getInstance().foods) {
            if (food.position.x == position.x && food.position.y == position.y) {
                return true;
            }
        }
        return false;
    }

    public void moveDirection(Point target) {
        double speed = 2.0;  // Adjust the speed as needed
        double randomness = 1;  // Adjust the randomness as needed
    
        int dx = target.x - position.x;
        int dy = target.y - position.y;
    
        // Calculate the distance between the current position and the target
        double distance = Math.sqrt(dx * dx + dy * dy);
    
        // Check if the ant is already at the target
        if (distance > 0) {
            // Calculate the step towards the target
            double stepX = speed * dx / distance;
            double stepY = speed * dy / distance;
    
            // Add some randomness to the step
            stepX += (Math.random() * 2 - 1) * randomness;
            stepY += (Math.random() * 2 - 1) * randomness;
    
            // Update the position
            position.x += (int) stepX;
            position.y += (int) stepY;
        }
    }
    


}