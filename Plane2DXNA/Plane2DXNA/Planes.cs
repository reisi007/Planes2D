using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace Plane2DXNA
{
     class BasicPlanes
    {
         protected Texture2D Texture;
         protected Vector2 Position;
         protected SpriteBatch spritebatch;
         protected SpriteEffects plane_effect = SpriteEffects.None;
         private Rectangle ClientBounds;
         public Rectangle Collision;
         protected float Size;
         protected int PlaneMid;
         protected  BasicPlanes(Texture2D texture, Vector2 position, SpriteBatch sb, Rectangle clientbounds, float size)
       {
          Texture = texture;
          Position = position;
          spritebatch = sb;
          ClientBounds = clientbounds;
          Size = size;
       }
         
         public virtual void Update()
      {
          // Top Bottom Collision detection
          if (Position.Y < 0)
              Position.Y = 0;
          if (Position.Y + Texture.Height > ClientBounds.Height)
              Position.Y = ClientBounds.Height - Texture.Height;
          // Collision Rectangle update
          Collision = new Rectangle((int)Position.X, (int)Position.Y, (int)(Texture.Width * Size), (int)(Texture.Height * Size));
          PlaneMid = (int)(Position.Y + Texture.Height / 2);
      }
      public virtual void  Draw()
      {
          spritebatch.Draw(Texture, new Rectangle((int)Position.X, (int)Position.Y, Texture.Width, Texture.Height), new Rectangle(0, 0, Texture.Width, Texture.Height), Color.White, 0, Vector2.Zero, plane_effect, 0);
      }
    }
     class UserPlane : BasicPlanes
     {
         public UserPlane(Texture2D texture, Vector2 position, SpriteBatch sb, Rectangle clientbounds)
             : base(texture, position, sb, clientbounds,1)
         {
             plane_effect = SpriteEffects.FlipHorizontally;
         }
         int ydiff;
         public override void Update()
         {
             ydiff = Mouse.GetState().Y - PlaneMid;
             if (ydiff > 5)
                 ydiff = 5;
             if (ydiff < -6)
                 ydiff = -6;
             Position.Y += ydiff;
             base.Update();
         }
     }
     class EnemyPlane : BasicPlanes
     {
         public EnemyPlane(Texture2D texture, double rand_y, SpriteBatch sb, Rectangle clientbounds, float size)
             : base(texture, new Vector2(clientbounds.Width, (float)(rand_y * clientbounds.Height)), sb, clientbounds, size)
         {
             // TODO
         }
     }
}
