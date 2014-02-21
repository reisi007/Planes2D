package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BaseAnimation implements IMoveableGameObject {
    protected int Nrows, NColumns, curRow = 0, curColumn = 0, runs = 0, sizeX, sizeY;
    protected double iterator = 0, changeAfter;
    protected Moveable obj;
    private TextureRegion textureRegion;

    public BaseAnimation(BaseAnimation baseAnimation) {
        this(baseAnimation.textureRegion.getTexture(), new Vector2(baseAnimation.obj.getX(), baseAnimation.obj.getY()), baseAnimation.obj.speed, baseAnimation.obj.getFspeed(), baseAnimation.sizeX, baseAnimation.sizeY, (int) (baseAnimation.changeAfter / GameTime.FRAME + 0.5d), baseAnimation.Nrows, baseAnimation.NColumns, baseAnimation.obj.getHeight(), false);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int size, int changeAfter, int NRows, int NColumns, float setHeight) {
        this(texture, position, direction, speed, size, size, changeAfter, NRows, NColumns, setHeight, false);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth) {
        obj = new Moveable((textureRegion = new TextureRegion(texture, 0, 0, this.sizeX = sizeX, this.sizeY = sizeY)), position, direction, speed, Anchor.LowLeft, setSide, setWidth, false, false);
        this.changeAfter = changeAfter * GameTime.FRAME;
        this.Nrows = NRows;
        this.NColumns = NColumns;
    }

    public void Draw(SpriteBatch spriteBatch) {
        obj.Draw(spriteBatch);
    }

    public void setSpeed(Vector2 direction, float speed) {
        obj.setSpeed(direction, speed);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {
        obj.setPosition(a, x, y);
    }

    @Override
    public void updatePosition(float x, float y) {
        obj.updatePosition(x, y);
    }

    @Override
    public void setScale(float newScale) {
        Drawable.setSclae(newScale, this);
    }

    protected void setRegion(int x, int y, int w, int h) {
        // System.out.println("Set Region Args:\tX: " + x + "\tY: " + y + "\tW: " + w + "\tH:" + h);
        textureRegion.setRegion(x, y, w, h);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        // System.out.println("Base animation (" + toString() + ") updated");
        obj.Update(gameTimeArgs);
        iterator += gameTimeArgs.ELapsedMSSinceLastFrame;
        // System.out.println("MS since last frame:" + gameTimeArgs.ELapsedMSSinceLastFrame);
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
            // Adapt Textureregion
            setRegion(curRow * sizeX, curColumn * sizeY, sizeX, sizeY);
            // System.out.println("TextureRegion:\tX: " + textureRegion.getRegionX() + "\tY: " + textureRegion.getRegionY() + "\tW: " + textureRegion.getRegionWidth() + "\tH:" + textureRegion.getRegionHeight());

        }
    }
}
