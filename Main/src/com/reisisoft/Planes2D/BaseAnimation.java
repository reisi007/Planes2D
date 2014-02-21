package com.reisisoft.Planes2D;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BaseAnimation implements IMoveableGameObject {
    protected int Nrows, NColumns, curRow = 0, curColumn = 0, runs = 0, sizeX, sizeY;
    protected double iterator = 0, changeAfter;
    protected Moveable animation;
    private TextureRegion textureRegion;

    public BaseAnimation(BaseAnimation baseAnimation) {
        this(baseAnimation.textureRegion.getTexture(), new Vector2(baseAnimation.animation.getX(), baseAnimation.animation.getY()), baseAnimation.animation.speed, baseAnimation.animation.getFspeed(), baseAnimation.sizeX, baseAnimation.sizeY, (int) (baseAnimation.changeAfter / GameTime.FRAME + 0.5d), baseAnimation.Nrows, baseAnimation.NColumns, baseAnimation.animation.getHeight(), false);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int size, int changeAfter, int NRows, int NColumns, float setHeight) {
        this(texture, position, direction, speed, size, size, changeAfter, NRows, NColumns, setHeight, false);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth) {
        animation = new Moveable((textureRegion = new TextureRegion(texture, 0, 0, this.sizeX = sizeX, this.sizeY = sizeY)), position, direction, speed, Anchor.LowLeft, setSide, setWidth, false, false);
        this.changeAfter = changeAfter * GameTime.FRAME;
        this.Nrows = NRows;
        this.NColumns = NColumns;
    }

    public void Draw(SpriteBatch spriteBatch) {
        animation.Draw(spriteBatch);
    }

    public void setSpeed(Vector2 direction, float speed) {
        animation.setSpeed(direction, speed);
    }

    @Override
    public void setPosition(Anchor a, float x, float y) {
        animation.setPosition(a, x, y);
    }

    @Override
    public void updatePosition(float x, float y) {
        animation.updatePosition(x, y);
    }

    @Override
    public void setScale(float newScale) {
        Drawable.setSclae(newScale, animation);
    }

    public float getHeight() {
        return animation.getHeight();
    }

    public float getWidth() {
        return animation.getWidth();
    }

    @Override
    public float rightMost() {
        return animation.rightMost();
    }

    protected void setRegion(int x, int y, int w, int h) {
        // System.out.println("Set Region Args:\tX: " + x + "\tY: " + y + "\tW: " + w + "\tH:" + h);
        textureRegion.setRegion(x, y, w, h);
    }

    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        // System.out.println("Base animation (" + toString() + ") updated");
        animation.Update(gameTimeArgs);
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

    public String toString() {
        return animation.toString() + " as Animation";
    }
}
