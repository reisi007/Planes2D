package com.reisisoft.planes2d;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GrassManager implements IGameObject {
    private ArrayList<Moveable> grass = new ArrayList<>();
    private float totalWidth, rightMost = -10;
    private Moveable original;

    public GrassManager(TextureRegion sprite, float setHeight, float totalWidth) {
        original = new Moveable(sprite, Vector2.Zero, new Vector2(-1, 0), 2, IFullGameObject.Anchor.LowLeft, setHeight, false, false, false);
        this.totalWidth = totalWidth;
        fillGrass();
    }

    private void fillGrass() {
        while ((rightMost = grass.isEmpty() ? 0 : grass.get(grass.size() - 1).rightMost()) <= totalWidth) {
            grass.add(getGrass(rightMost));
        }
    }

    @Override
	public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        fillGrass();
        for (int i = 0; i < grass.size(); i++) {
            Moveable moveable = grass.get(i);
            if (moveable.rightMost() < 0f) {
                grass.remove(i);
                i--;
            } else
                moveable.Update(gameTimeArgs);
        }
    }

    @Override
	public void Draw(SpriteBatch spriteBatch) {
        for (Moveable moveable : grass)
            moveable.Draw(spriteBatch);
    }


    private Moveable getGrass(float x) {
        Moveable tmp = new Moveable(original);
        tmp.setPosition(IFullGameObject.Anchor.LowLeft, x, 0);
        return tmp;
    }
}
