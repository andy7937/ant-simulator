package simulator;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AntSimulator antSimulator = AntSimulator.getInstance();
            antSimulator.runSimulation();
        });
    }
}