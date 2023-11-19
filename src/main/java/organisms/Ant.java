package organisms;
import java.util.Iterator;
import java.util.Random;

import simulator.AntSimulator;
import simulator.Point;
import organisms.Pheromone;


public class Ant {
    public int health;
    public int energy;
    public String direction;
    public Point position;
    public boolean hasFood;
    public int mapSize = 900;
    private Point lastRandomPoint;

    public Ant(int health, int energy, Point position) {
        this.health = health;
        this.energy = energy;
        this.position = position;
        this.direction = "N";
        this.hasFood = false;
    }

    public void move() {

        if (checkFood(position) == true){
            eat();
            return;
        }

        if (position.x == AntSimulator.getInstance().queen.position.x && position.y == AntSimulator.getInstance().queen.position.y){
            if (hasFood == true){
                AntSimulator.getInstance().queen.addEnergy();
                hasFood = false;
                return;
            }
        }

        if (hasFood == true){
            findFarthestHomePheromone();
            releaseFoodPheromone();
        return;
        }
        else{
           findFarthestFoodPheromone();
           releaseHomePheromone();
        }
        
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

    public void findFarthestHomePheromone() {
        int minDistance = 50; // Minimum distance range
        int maxDistance = 150; // Maximum distance range
    
        Point farthestHomePheromone = null;
        double farthestHomePheromoneDistance = 0;
    
        for (Pheromone pheromone : AntSimulator.getInstance().homePheromones) {
            int dx = pheromone.position.x - position.x;
            int dy = pheromone.position.y - position.y;
            // Calculate the distance between the current position and the target
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance >= minDistance && distance <= maxDistance &&
                (farthestHomePheromone == null || distance > farthestHomePheromoneDistance)) {
                // Check if the distance is within the specified range and greater than the current maxDistance
                farthestHomePheromone = pheromone.position;
                farthestHomePheromoneDistance = distance;
            }
        }
    
        if (!goToQueen()) {
            if (farthestHomePheromone != null) {
                moveDirection(farthestHomePheromone);
            } else {
                randomPoint();
            }
        }
    }
    

    public void findFarthestFoodPheromone() {
        int minDistance = 50; // Minimum distance range
        int maxDistance = 150; // Maximum distance range
    
        Point farthestFoodPheromone = null;
        double farthestFoodPheromoneDistance = 0;
    
        for (Pheromone pheromone : AntSimulator.getInstance().foodPheromones) {
            int dx = pheromone.position.x - position.x;
            int dy = pheromone.position.y - position.y;
            // Calculate the distance between the current position and the target
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance >= minDistance && distance <= maxDistance &&
                (farthestFoodPheromone == null || distance > farthestFoodPheromoneDistance)) {
                // Check if the distance is within the specified range and greater than the current maxDistance
                farthestFoodPheromone = pheromone.position;
                farthestFoodPheromoneDistance = distance;
            }
        }
    
        if (!lookAround()) {
            if (farthestFoodPheromoneDistance > minDistance) {  // Check if a valid farthest food pheromone is found
                moveDirection(farthestFoodPheromone);
            } else {
                randomPoint();
            }
        }
    }
    
    public boolean lookAround(){
        // look for the nearest food around in a radius of 50

        int maxDistance = 50; // Maximum distance range
        Point nearestFood = null;

        for (Food food : AntSimulator.getInstance().foods) {
            int distance = Math.abs(food.position.x - position.x) + Math.abs(food.position.y - position.y);
            if (distance <= maxDistance && (nearestFood == null || distance < maxDistance)) {
                // Check if the distance is within range and less than the current maxDistance
                maxDistance = distance;
                nearestFood = food.position;
            }
        }

        if (nearestFood != null) {
            moveDirection(nearestFood);
            return true;
        } else {
            return false;
        }
    }

    public boolean goToQueen(){
        // look for the queen in a radius of 50
        int maxDistance = 50; // Maximum distance range

        for (Ant ant : AntSimulator.getInstance().ants) {
            int distance = Math.abs(ant.position.x - position.x) + Math.abs(ant.position.y - position.y);
            if (distance <= maxDistance && (ant.hasFood == true)) {
                // Check if the distance is within range and less than the current maxDistance
                maxDistance = distance;
                moveDirection(AntSimulator.getInstance().queen.position);
                return true;
            }
        }

        return false;
    }

    public void randomPoint() {
        Random random = new Random();

        // Generate a new random point only if it's the first call or the ant reached the previous random point
        if (lastRandomPoint == null || position.equals(lastRandomPoint)) {
            int x = random.nextInt(mapSize);
            int y = random.nextInt(mapSize);
            lastRandomPoint = new Point(x, y);
        }

        moveDirection(lastRandomPoint);
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

            System.out.println("moving to " + target.x + " " + target.y);
        }
    }
    


}

