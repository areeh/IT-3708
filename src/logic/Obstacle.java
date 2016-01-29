package logic;

import java.util.Random;

public class Obstacle {
	
	private Vector2D position;
	private int radius;
	private Random rng;

	//No arguments gives random obstacle
	public Obstacle() {
		rng = new Random();
		
		this.position = new Vector2D(rng.nextDouble()*100, rng.nextDouble()*100);
		this.radius = 5;
	}
	
	public Vector2D getPosition() {
		return this.position;
	}
	
	public int getRadius() {
		return this.radius;
	}

}
