package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BaseAnimation implements IGameObject {
    private Coordinates direction;
    protected int Nrows, NColumns, curRow = 0, curColumn = 0, runs = 0, sizeX, sizeY;
    protected double iterator = 0, changeAfter;
    protected Sprite sprite;

    public BaseAnimation(Texture texture, Coordinates position, Coordinates direction, int size, Coordinates Npictures, int changeAfter) {
        this(texture, position, direction, size, size, Npictures, changeAfter);
    }

    public BaseAnimation(Texture texture, Coordinates position, Coordinates direction, int sizeX, int sizeY, Coordinates Npictures, int changeAfter) {
        sprite = new Sprite(texture, 0, 0, this.sizeX = sizeX, this.sizeY = sizeY);
        sprite.setPosition(position.X(), position.Y());
        this.direction = direction;
        this.changeAfter = changeAfter * GameTime.FRAME;
        Nrows = Npictures.X();
        NColumns = Npictures.Y();
    }

    public void Draw(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {

    }

    @Override
    public void setScale(float newScale) {
        Drawable.setSclae(newScale, this);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        sprite.setPosition(sprite.getX() + direction.X(), sprite.getY() + direction.Y());
        iterator += gameTimeArgs.ELapsedMSSinceLastFrame;
        if (iterator >= changeAfter) {
            iterator -= changeAfter;
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
