package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Plane extends Game {

	public SpriteBatch batch;
	public BitmapFont font;



	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // use libGDX's default Arial font
		this.setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}

/*public class Plane extends game {
	private int yAngle;
	//public SpriteBatch batch;
	public Plane(float x, float y, int yAngle) {
		super(x, y, 90, 50);
		this.yAngle = yAngle;
	}

	public int getyAngle() {
		return yAngle;
	}
	public void updatePosition(float deltaTime) {
		y -= yAngle * deltaTime;
		x -= 200 * deltaTime;
	}
}
*/