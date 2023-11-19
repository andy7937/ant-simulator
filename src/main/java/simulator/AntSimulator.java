package simulator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import organisms.Ant;
import organisms.Food;
import organisms.Pheromone;
import organisms.Queen;
import simulator.Point;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AntSimulator extends JPanel {
    int mapSize = 900;
    public List<Ant> ants = new ArrayList<>();
    public List<Food> foods = new ArrayList<>();
    public List<Pheromone> foodPheromones = new ArrayList<>();
    public List<Pheromone> homePheromones = new ArrayList<>();
    public Queen queen;

    private static AntSimulator instance;

    private AntSimulator() {
        initializeSimulation();
    }

    public static AntSimulator getInstance() {
        if (instance == null) {
            instance = new AntSimulator();
        }
        return instance;
    }

    private void initializeSimulation() {
        Random random = new Random();
        int x = random.nextInt(mapSize);
        int y = random.nextInt(mapSize);

        // spawn queen at random location
        queen = new Queen(100, 0, new Point (x, y));

        // spawn 10 at queen location
        for (int i = 0; i < 10; i++) {
            ants.add(new Ant(100, 500, new Point(x, y)));
        }

        // how many clumps of food you want to spawn on the map. E.g 20 clumps will make 20 groups around the map of random foods each
        spawnFoodGroup(25);

    }

    private void updateSimulation() {
        updateAnts();
        updateQueen(); 
        updatePheromones();
    }

    private void updateAnts() {
        Iterator<Ant> iterator = ants.iterator();
        while (iterator.hasNext()) {
            Ant ant = iterator.next();
            ant.move();
            if (ant.energy <= 0) {
                ant.health--;
                if (ant.health <= 0) {
                    iterator.remove(); // Use iterator to remove the current element
                }
            }else{
                ant.health++;
            }

            // not implementing health drop for now, want to get the basics implementation of ant behaviour first. Need to balance energy system 
            ant.health++;
        }
    }

    private void updateQueen() {
        queen.layEgg();
    }

    private void updatePheromones() {
        Iterator<Pheromone> foodIterator = foodPheromones.iterator();
        Iterator<Pheromone> homeIterator = homePheromones.iterator();
    
        // change the probability of pheromones disappearing here
        double probability = 0.005;
        while (foodIterator.hasNext()) {
            Pheromone pheromone = foodIterator.next();
            if (Math.random() < probability) {
                foodIterator.remove(); // Remove pheromones with a certain probability
            }
        }
    
        while (homeIterator.hasNext()) {
            Pheromone pheromone = homeIterator.next();
            if (Math.random() < probability) {
                homeIterator.remove(); // Remove pheromones with a certain probability
            }
        }
    }

    private void spawnFoodGroup(int numGroups) {
        Random random = new Random();
    
        // how many max food you want in each group
        int numFoods = 100;
    
        // radius of each food group
        int groupRadius = 100;
    
        for (int i = 0; i < numGroups; i++) {
            int groupX = random.nextInt(mapSize);
            int groupY = random.nextInt(mapSize);
    
            // Corrected the calculation of num
            int num = random.nextInt(numFoods) + 1;
    
            for (int j = 0; j < num; j++) {
                int foodX = groupX + random.nextInt(groupRadius); // Adjust the range as needed
                int foodY = groupY + random.nextInt(groupRadius); // Adjust the range as needed
    
                foods.add(new Food(new Point(foodX, foodY)));
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPheromones(g);
        drawFoods(g);
        drawAnts(g);
        drawQueen(g);
    }

    private void drawQueen(Graphics g) {
        // Draw body
        g.setColor(Color.RED);
        g.fillRect(queen.position.x, queen.position.y, 20, 20);
    
        // Draw mid part
        Point midBody = new Point(queen.position.x + 20, queen.position.y + 4);
        g.fillRect(midBody.x, midBody.y, 16, 10);
    
        // Draw head
        Point head = new Point(queen.position.x + 34, queen.position.y - 3);
        g.fillOval(head.x, head.y, 20, 20);
    
         // Draw legs
        g.setColor(Color.BLACK);
        g.drawLine(midBody.x + 7, midBody.y + 10, midBody.x - 4, midBody.y + 20);
        g.drawLine(midBody.x + 15, midBody.y + 10, midBody.x + 26, midBody.y + 20);
    
        // Draw small antennas
        g.drawLine(head.x + 10, head.y + 2, head.x + 5, head.y - 3);
        g.drawLine(head.x + 14, head.y + 2, head.x + 19, head.y - 3);
    }
    

    private void drawAnts(Graphics g) {
        for (Ant ant : ants) {
            // Draw body
            g.setColor(Color.RED);
            g.fillRect(ant.position.x, ant.position.y, 5, 5);
    
            // Draw mid part
            Point midBody = new Point(ant.position.x + 5, ant.position.y);
            g.fillRect(midBody.x, midBody.y, 4, 4);
    
            // Draw head
            Point head = new Point(ant.position.x + 8, ant.position.y);
            g.fillOval(head.x, head.y, 5, 5);

            // Draw legs
            g.setColor(Color.BLACK);
            g.drawLine(midBody.x, midBody.y + 4, midBody.x - 2, midBody.y + 6);
            g.drawLine(midBody.x + 4, midBody.y + 4, midBody.x + 6, midBody.y + 6);
    
            // Draw small antennas
            g.drawLine(head.x + 2, head.y, head.x + 1, head.y - 2);
            g.drawLine(head.x + 2, head.y, head.x + 4, head.y - 2);
        }
    }
    
    
    private void drawFoods(Graphics g) {
        g.setColor(Color.GREEN);
        for (Food food : foods) {
            g.fillRect(food.position.x, food.position.y, 5, 5);
        }
    }

    private void drawPheromones(Graphics g) {
        g.setColor(Color.BLUE);
        for (Pheromone pheromone : foodPheromones) {
            g.fillOval(pheromone.position.x, pheromone.position.y, 3, 3);
        }

        g.setColor(Color.darkGray);
        for (Pheromone pheromone : homePheromones) {
            g.fillOval(pheromone.position.x, pheromone.position.y, 3, 3);
        }
    }

    public void runSimulation() {
        JFrame frame = new JFrame("Ant Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(mapSize, mapSize);
        frame.getContentPane().add(this);
        frame.setVisible(true);
    
        Timer timer = new Timer(10, e -> {
            updateSimulation();
            repaint();
        });
    
        timer.start();
    }
}