package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseAnimation implements IDrawable, IUpdateAble {
    private Coordinates direction;
    protected int changeAfter, iterator = 0, Nrows, NColumns, curRow = 0, curColumn = 0, runs = 0,sizeX, sizeY;
    protected Sprite sprite;

    public BaseAnimation(Texture texture, Coordinates position, Coordinates direction, int size, Coordinates Npictures, int changeAfter) {
        this(texture, position, direction, size, size, Npictures, changeAfter);
    }

    public BaseAnimation(Texture texture, Coordinates position, Coordinates direction, int sizeX, int sizeY, Coordinates Npictures, int changeAfter) {
        sprite = new Sprite(texture, 0, 0, this.sizeX = sizeX, this.sizeY = sizeY);
        sprite.setPosition(position.X(), position.Y());
        this.direction = direction;
        this.changeAfter = changeAfter;
        Nrows = Npictures.X();
        NColumns = Npictures.Y();
    }

    public void Draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        sprite.setPosition(sprite.getX() + direction.X(), sprite.getY() + direction.Y());
        iterator++;
        if (iterator == changeAfter) {
            iterator = 0;
            if (curRow < Nrows - 1) {
                curRow++;
            } else if (curColumn < NColumns - 1) {
                curRow = 0;
                curColumn++;
            } else {
                curColumn = curRow = 0;
                runs++;
            }
            // Adapt sprite
            sprite.setRegion(sizeX * curColumn, sizeY * curColumn, sizeX, sizeY);
        }
    }
}
