//Based on https://github.com/nablaa/boids-simulation

package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import logic.Simulation;

public class OptionsPanel extends JPanel implements Observer {
    private Simulation sim;
    private DrawingArea area;
    private JSpinner numberOfBoids;
    private JSpinner boidSpeed;
    private JSpinner boidPredatorSpeed;
    private JSpinner boidSepRadius;
    private JSpinner boidFovDist;
    private JSpinner boidSeparationFactor;
    private JSpinner boidAlignmentFactor;
    private JSpinner boidCohesionFactor;
    private JSpinner sleepTime;
    private JSpinner maxForce;
    private JButton startButton;
    private JButton addPredatorButton;
    private JCheckBox wrapArea;
	private JButton addObstacleButton;
	private JButton removeObstaclesButton;
	private JButton removePredatorsButton;
    
    /**
     * Creates new options panel.
     * 
     * @param simulation simulation
     * @param thread simulation thread
     * @param area drawing area
     * @param messages messages
     */
    public OptionsPanel(final Simulation simulation, final Thread thread, final DrawingArea area) {
        super();
        this.sim = simulation;
        this.area = area;
        this.setLayout(new GridLayout(8, 4));
        
        startButton = new JButton("Pause");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!thread.isAlive()) {
                    thread.start();
                    startButton.setText("Pause");
                } else {
                    if (area.isStopped()) {
                        area.start();
                        startButton.setText("Pause");
                    } else {
                        area.stop();
                        startButton.setText("Resume");
                    }
                }
            }
        });
        
        this.add(new JLabel("Pause/Resume"));
        this.add(startButton);
        
        boidSpeed = new JSpinner(new SpinnerNumberModel(this.sim.getSettings().getMaxSpeed(), 1, 100, 0.01));

        boidSpeed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setMaxSpeed(((SpinnerNumberModel)boidSpeed.getModel()).getNumber().intValue());
            }
        });
        
        this.add(new JLabel("Speed"));
        this.add(boidSpeed);
        
        boidPredatorSpeed = new JSpinner(new SpinnerNumberModel(this.sim.getSettings().getMaxPredatorSpeed(), 1, 100, 0.01));
        
        boidPredatorSpeed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setMaxPredatorSpeed(((SpinnerNumberModel)boidPredatorSpeed.getModel()).getNumber().intValue());
            }
        });
        
        this.add(new JLabel("Predator Speed"));
        this.add(boidPredatorSpeed);
        
        /*
        this.add(new JLabel("View Angle"));
        this.add(boidFovAngle);
        */
        
        addPredatorButton = new JButton("Add Predator");
        addPredatorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.addRandomPredator();
            }
        });
        
        this.add(new JLabel(""));
        this.add(addPredatorButton);
        
        removePredatorsButton = new JButton("Remove Predators");
        removePredatorsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.removePredators();
            }
        });
        
        this.add(new JLabel(""));        
        this.add(removePredatorsButton);
        
        addObstacleButton = new JButton("Add Obstacle");
        addObstacleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.addRandomObstacle();
            }
        });
        
        this.add(new JLabel(""));
        this.add(addObstacleButton);
        
        removeObstaclesButton = new JButton("Remove Obstacles");
        removeObstaclesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sim.removeObstacles();
            }
        });
        
        this.add(new JLabel(""));
        this.add(removeObstaclesButton);
        
        
        
        boidFovDist = new JSpinner(new SpinnerNumberModel(sim.getSettings().getViewRadius(), 5, 500, 1));
        
        boidFovDist.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setViewRadius(((SpinnerNumberModel)boidFovDist.getModel()).getNumber().intValue());
            }
        });
        
        this.add(new JLabel("View Distance"));
        this.add(boidFovDist);
        
        boidSeparationFactor = new JSpinner(new SpinnerNumberModel(sim.getSettings().getSeparationFactor(), 0, 10000, 0.01));
        boidSeparationFactor.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setSeparationFactor(((SpinnerNumberModel)boidSeparationFactor.getModel()).getNumber().doubleValue());
            }
        });
        
        this.add(new JLabel("Separation Factor"));
        this.add(boidSeparationFactor);
        
        boidAlignmentFactor = new JSpinner(new SpinnerNumberModel(sim.getSettings().getAlignmentFactor(), 0, 10000, 0.01));
        boidAlignmentFactor.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setAlignmentFactor(((SpinnerNumberModel)boidAlignmentFactor.getModel()).getNumber().doubleValue());
            }
        });
        
        this.add(new JLabel("Alignment Factor"));
        this.add(boidAlignmentFactor);
        
        boidCohesionFactor = new JSpinner(new SpinnerNumberModel(sim.getSettings().getCohesionFactor(), 0, 10000, 0.01));
        boidCohesionFactor.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setCohesionFactor(((SpinnerNumberModel)boidCohesionFactor.getModel()).getNumber().doubleValue());
            }
        });
        
        this.add(new JLabel("Cohesion Factor"));
        this.add(boidCohesionFactor);
        
        maxForce = new JSpinner(new SpinnerNumberModel(sim.getSettings().getMaxForce(), 0.1, 1000, 0.01));
        maxForce.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setMaxForce(((SpinnerNumberModel)maxForce.getModel()).getNumber().doubleValue());
            }            
        });
        
        this.add(new JLabel("Max Force"));
        this.add(maxForce);
        
        boidSepRadius = new JSpinner(new SpinnerNumberModel(sim.getSettings().getSepRadius(), 0, 10000, 0.01));
        boidSepRadius.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                sim.getSettings().setMaxForce(((SpinnerNumberModel)boidSepRadius.getModel()).getNumber().doubleValue());
            }            
        });
        
        this.add(new JLabel("Separation Radius"));
        this.add(boidSepRadius);
        
        /*
        this.add(new JLabel("Mass"));
        this.add(mass);
        */
        
        sleepTime = new JSpinner(new SpinnerNumberModel(area.getSleepTime(), 0, 10000, 1));
        sleepTime.addChangeListener(new ChangeListener() {
           public void stateChanged(ChangeEvent e) {
               area.setSleepTime(((SpinnerNumberModel)sleepTime.getModel()).getNumber().longValue());
           }
        });
        
        this.add(new JLabel("Sleep time"));
        this.add(sleepTime);
        
        //TODO: WrapArea
        
        /*
        wrapArea = new JCheckBox("Wrap area");
        wrapArea.setSelected(sim.getSettings().isWrapArea());
        wrapArea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox item = (JCheckBox) e.getSource();
                sim.getSettings().setWrapArea(item.isSelected());
            }
        });
        
        this.add(wrapArea);
        */
    }

    /**
     * Updates the number of boids. This will be called when the number of boids
     * changes in the flock.
     */
    public void update(Observable obs, Object object) {
    }

    /**
     * Resets the gui components. This must be called after a simulation is
     * loaded from a file.
     */
    
    public void reset() {
        //numberOfBoids.setValue(this.sim.getFlock().getSize());
        boidSpeed.setValue(this.sim.getSettings().getMaxSpeed());
        //boidFovAngle.setValue(this.sim.getSettings().getViewAngle() * 2);
        boidFovDist.setValue(this.sim.getSettings().getViewRadius());
        boidSeparationFactor.setValue(this.sim.getSettings().getSeparationFactor());
        boidAlignmentFactor.setValue(this.sim.getSettings().getAlignmentFactor());
        boidCohesionFactor.setValue(this.sim.getSettings().getCohesionFactor());
        sleepTime.setValue(this.area.getSleepTime());
        //wrapArea.setSelected(this.sim.getSettings().isWrapArea());
    }
    
    /**
     * Sets the simulation. This must be called after a simulation is
     * loaded from a file.
     * 
     * @param sim simulation
     */
    
    /*
    public void setSim(Simulation sim) {
        this.sim = sim;
    }
    */

    /**
     * Changes the pause button text to 'Resume'. This should be called whenever
     * the game is paused.
     */
    public void pause() {
        this.startButton.setText("Resume");
    }
    
}
