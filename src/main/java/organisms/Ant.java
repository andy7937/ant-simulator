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
            }
        }

        if (hasFood == true){
            findFarthestHomePheromone();
            releaseFoodPheromone();
        return;
        }
        else{
            releaseHomePheromone();
           findFarthestFoodPheromone();

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
        int maxDistance = 100; // Maximum distance range
        Point farthestHomePheromone = null;
    
        for (Pheromone pheromone : AntSimulator.getInstance().homePheromones) {
            int distance = Math.abs(pheromone.position.x - position.x) + Math.abs(pheromone.position.y - position.y);
            if (distance <= maxDistance && (farthestHomePheromone == null || distance > maxDistance)) {
                // Check if the distance is within range and greater than the current maxDistance
                maxDistance = distance;
                farthestHomePheromone = pheromone.position;
            }
        }

        if (!goToQueen()){
            if (farthestHomePheromone != null) {
                moveDirection(farthestHomePheromone);
            } else {
                randomPoint();
            }
        }
    }

    public void findFarthestFoodPheromone() {
        int maxDistance = 100; // Maximum distance range
        Point farthestFoodPheromone = null;
    
        for (Pheromone pheromone : AntSimulator.getInstance().foodPheromones) {
            int distance = (int) Math.sqrt(Math.pow(pheromone.position.x - position.x, 2) + Math.pow(pheromone.position.y - position.y, 2));
            if (distance <= maxDistance && (farthestFoodPheromone == null || distance > maxDistance)) {
                // Check if the distance is within range and greater than the current maxDistance
                maxDistance = distance;
                farthestFoodPheromone = pheromone.position;
            }
        }

        if (!lookAround()){
            if (farthestFoodPheromone != null) {               
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

    // method for finding the closest food to the ant
    public void findClosestFood(){
        Point closestFood = null;

        for (Food food : AntSimulator.getInstance().foods) {
            int distance = Math.abs(food.position.x - position.x) + Math.abs(food.position.y - position.y);
            if (closestFood == null || distance < Math.abs(closestFood.x - position.x) + Math.abs(closestFood.y - position.y)) {
                // Check if the distance is within range and less than the current maxDistance
                closestFood = food.position;
            }
        }

        moveDirection(closestFood);

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