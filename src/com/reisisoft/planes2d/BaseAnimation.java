package com.reisisoft.planes2d;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BaseAnimation implements IMoveableFullGameObject {
    protected int Nrows, NColumns, curRow = 0, curColumn = 0, runs = 0, sizeX, sizeY;
    protected double iterator = 0, changeAfter;
    protected Moveable animation;
    private TextureRegion textureRegion;
    private int missinglastRow;

    public BaseAnimation(BaseAnimation baseAnimation) {
        this(baseAnimation.textureRegion.getTexture(), new Vector2(baseAnimation.animation.getX(), baseAnimation.animation.getY()), baseAnimation.animation.speed, baseAnimation.animation.getFspeed(), baseAnimation.sizeX, baseAnimation.sizeY, (int) (baseAnimation.changeAfter / GameTime.FRAME + 0.5d), baseAnimation.Nrows, baseAnimation.NColumns, baseAnimation.animation.getHeight(), false, baseAnimation.missinglastRow);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int size, int changeAfter, int NRows, int NColumns, float setHeight) {
        this(texture, position, direction, speed, size, size, changeAfter, NRows, NColumns, setHeight, false);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth) {
        this(texture, position, direction, speed, sizeX, sizeY, changeAfter, NRows, NColumns, setSide, setWidth, 0);
    }

    public BaseAnimation(Texture texture, Vector2 position, Vector2 direction, float speed, int sizeX, int sizeY, int changeAfter, int NRows, int NColumns, float setSide, boolean setWidth, int missingLastRow) {
        animation = new Moveable(textureRegion = new TextureRegion(texture, 0, 0, this.sizeX = sizeX, this.sizeY = sizeY), position, direction, speed, Anchor.LowLeft, setSide, setWidth, false, false);
        this.changeAfter = changeAfter * GameTime.FRAME;
        Nrows = NRows;
        this.NColumns = NColumns;
        missinglastRow = missingLastRow;
    }

    @Override
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
        // System.out.println("Set Region Args:\tX: " + x + "\tY: " + y +
        // "\tW: " + w + "\tH:" + h);
        textureRegion.setRegion(x, y, w, h);
    }

    @Override
    public void Update(GameTime.GameTimeArgs gameTimeArgs) {
        animation.Update(gameTimeArgs);
        iterator += gameTimeArgs.ELapsedMSSinceLastFrame;
        if (iterator >= changeAfter) {
            iterator -= changeAfter;
            if (curColumn < NColumns - 1) {
                curColumn++;
            } else if (curRow < Nrows - 1) {
                curColumn = 0;
                curRow++;
            } else {
                curColumn = curRow = 0;
                runs++;
            }
            if (curRow == Nrows - 1) {
                // Last row
                if (curColumn > NColumns - missinglastRow - 1) {
                    curRow = curColumn = 0;
                    runs++;
                }
            }
            // Adapt Textureregion
            setRegion(curColumn * sizeX, curRow * sizeY, sizeX, sizeY);
        }
    }

    @Override
    public String toString() {
        return animation.toString() + " as Animation";
    }
}
