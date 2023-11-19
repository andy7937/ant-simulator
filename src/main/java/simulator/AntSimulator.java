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
    int mapSize = 1080;
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

        queen = new Queen(100, 0, new Point (x, y));

        for (int i = 0; i < 10; i++) {
            ants.add(new Ant(50, 50, new Point(x, y)));
        }

        for (int i = 0; i < 500; i++) {
            foods.add(new Food(new Point(random.nextInt(mapSize), random.nextInt(mapSize))));
        }
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

            // not implementing health drop for now, want to get the basics implementation of ant behaviour first
            ant.health++;
        }
    }

    private void updateQueen() {
        queen.layEgg();
    }

    private void updatePheromones() {
        Iterator<Pheromone> foodIterator = foodPheromones.iterator();
        Iterator<Pheromone> homeIterator = homePheromones.iterator();
    
        while (foodIterator.hasNext()) {
            Pheromone pheromone = foodIterator.next();
            if (Math.random() < 0.02) {
                foodIterator.remove(); // Remove pheromones with a certain probability
            }
        }
    
        while (homeIterator.hasNext()) {
            Pheromone pheromone = homeIterator.next();
            if (Math.random() < 0.02) {
                homeIterator.remove(); // Remove pheromones with a certain probability
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
        g.setColor(Color.RED);
        g.fillRect(queen.position.x, queen.position.y, 20, 20);
    }

    private void drawAnts(Graphics g) {
        g.setColor(Color.BLUE);
        for (Ant ant : ants) {
            g.fillRect(ant.position.x, ant.position.y, 10, 10);
        }
    }

    private void drawFoods(Graphics g) {
        g.setColor(Color.GREEN);
        for (Food food : foods) {
            g.fillRect(food.position.x, food.position.y, 5, 5);
        }
    }

    private void drawPheromones(Graphics g) {
        g.setColor(Color.ORANGE);
        for (Pheromone pheromone : foodPheromones) {
            g.fillOval(pheromone.position.x, pheromone.position.y, 5, 5);
        }

        g.setColor(Color.darkGray);
        for (Pheromone pheromone : homePheromones) {
            g.fillOval(pheromone.position.x, pheromone.position.y, 5, 5);
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