//Based on https://github.com/nablaa/boids-simulation

package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import logic.Simulation;


/**
 * This class provides a graphical user interface to the boids simulation
 */
public class Gui extends JFrame {
    private Simulation sim;
    private DrawingArea area;
    private Thread thread;
    private OptionsPanel options;
    
    /**
     * Creates a new gui window. Loads Gui strings from 'messages' file.
     * 
     * @param sim simulation
     */
    public Gui(Simulation sim) {
        this.setTitle("Boids");
        this.sim = sim;
        this.area = new DrawingArea(this.sim, 800, 800);
        this.thread = new Thread(this.area);
        
        JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(area);
        
        options = new OptionsPanel(this.sim, this.thread, this.area);
        panel.add(options);
        
        //TODO:
        //this.sim.getPredators().addObserver(options);
        
        this.createMenu();
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    
    /**
     * Creates the menubar.
     */
    private void createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu simulationMenu = new JMenu("Simulation");
        JMenu optionsMenu = new JMenu("Options");
        simulationMenu.setMnemonic(KeyEvent.VK_S);
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        
        JMenuItem newSimulation = new JMenuItem("New simulation", KeyEvent.VK_N);
        newSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sim.reset();
                options.reset();
                options.pause();
                area.stop();
                setTitle("Boids");
            }
        });
        
        JMenuItem clear = new JMenuItem("Clear", KeyEvent.VK_C);
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sim.getObstacles().clear();
                //sim.getFlock().removeBoids(sim.getFlock().getSize());
                options.pause();
                area.stop();
                setTitle("Boids");
            }
        });
        
        JMenuItem quit = new JMenuItem("Quit", KeyEvent.VK_Q);
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JCheckBoxMenuItem control = new JCheckBoxMenuItem("Show force vectors");
        control.setSelected(this.area.isShowControlVector());
        control.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                area.setShowControlVector(item.isSelected());
            }           
        });
        
        JCheckBoxMenuItem velocity = new JCheckBoxMenuItem("Show velocity vectors");
        velocity.setSelected(this.area.isShowVelocityVector());
        velocity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                area.setShowVelocityVector(item.isSelected());
            }           
        });
        
        
        JCheckBoxMenuItem boidSight = new JCheckBoxMenuItem("Show boid sight");
        boidSight.setSelected(this.area.isShowBoidSight());
        boidSight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                area.setShowBoidSight(item.isSelected());
            }           
        });
        
        simulationMenu.add(newSimulation);
        simulationMenu.add(clear);
        simulationMenu.addSeparator();
        simulationMenu.add(quit);
        
        optionsMenu.add(control);
        optionsMenu.add(velocity);
        optionsMenu.add(boidSight);

        simulationMenu.getPopupMenu().setLightWeightPopupEnabled(false); // avoid drawing area overlap
        optionsMenu.getPopupMenu().setLightWeightPopupEnabled(false); // avoid drawing area overlap
        
        menubar.add(simulationMenu);
        menubar.add(optionsMenu);
        this.setJMenuBar(menubar);
    }

    /**
     * Creates a gui and starts the simulation.
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        Simulation sim = new Simulation();
        Gui gui = new Gui(sim);
        gui.thread.start();
    }
    
}
