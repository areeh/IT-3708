//Author: Are Haartveit

package logic;

import java.util.ArrayList;

public class Simulation {
	
	private ArrayList<Boid> boids;
	private ArrayList<Predator> predators;
	private ArrayList<Obstacle> obstacles;
	private Settings settings;
	
	public Simulation() {
		obstacles = new ArrayList<Obstacle>();
		predators = new ArrayList<Predator>();
		settings = new Settings();
		boids = addRandomBoids(100);
	}
	
	public void reset() {
		boids = addRandomBoids(100);
	}
	
	public ArrayList<Boid> addRandomBoids(int boidsAmount) {
		
		ArrayList<Boid> res = new ArrayList<Boid>();
		
		while (boidsAmount > 0) {
			res.add(new Boid(settings));
			
			boidsAmount--;
		}
		
		return res;
				
	}
	
	
	public void addRandomPredator() {
		synchronized (predators) {
			predators.add(new Predator(settings));			
		}
	}
	
	public void removePredators() {
		synchronized (predators) {
			predators.clear();			
		}
	}
	
	public void addRandomObstacle() {
		synchronized (obstacles) {
			boolean ret = false;
			
			while (!ret) {
				Obstacle current = new Obstacle(settings);
				if (!current.anyOverlap(obstacles)) {
					this.obstacles.add(current);
					ret = true;
				}
			}			
		}
	}
	
	public void removeObstacles() {
		synchronized (obstacles) {
			obstacles.clear();
		}
	}
	
	public ArrayList<Boid> getBoids() {
		return this.boids;
	}
	
	public ArrayList<Obstacle> getObstacles() {
		return this.obstacles;
	}
	
	public ArrayList<Predator> getPredators() {
		return this.predators;
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public void updateBoids() {
		for (Boid b : this.boids) {
			b.calcSteer(this.boids, this.obstacles, this.predators);
			
			b.updatePosition(obstacles);
		}
		
	}
	
	public void updatePredators() {
		for (Predator p : this.predators) {
			p.calcPredSteer(this.boids, this.obstacles, this.predators);
			
			p.updatePosition(obstacles);
		}
	}


}
