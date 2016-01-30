package logic;

import java.util.Random;

public class Obstacle {
	
	private Vector2D position;
	private int radius;
	private Random rng;

	//Round obstacle in a random position
	public Obstacle(Settings settings) {
		rng = new Random();
		
		//Limit position to simulation size		
		double x = settings.getObstacleRadius() + (800-settings.getObstacleRadius() -settings.getObstacleRadius())*rng.nextDouble();
		double y = settings.getObstacleRadius() + (800-settings.getObstacleRadius() -settings.getObstacleRadius())*rng.nextDouble();
		
		this.position = new Vector2D(x, y);
		this.radius = settings.getObstacleRadius();
	}
	
	public Vector2D getPosition() {
		return this.position;
	}
	
	public int getRadius() {
		return this.radius;
	}

}
