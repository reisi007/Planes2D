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
using Nuclex.Input;
using Nuclex.Input.Devices;

namespace Plane2DXNA
{
     class BasicPlanes
    {
         protected Texture2D Texture;
         public Vector2 Position;
         protected SpriteBatch spritebatch;
         protected SpriteEffects plane_effect = SpriteEffects.None;
         private Rectangle ClientBounds;
         public Rectangle Collision;
         protected float Size;
         protected int PlaneMid;
         public bool Shooting = false, KillBill = false;
         protected bool Automatic;
         protected enum PlaneType { User, Enemy}
         protected PlaneType Type;
         protected int NextShoot, LastShoot;
         protected bool shoot_okay = false;
         public ExtendedGamePadState GP_state;
         protected Vector2 Resize;
         public  BasicPlanes(Texture2D texture, Vector2 position, SpriteBatch sb, Rectangle clientbounds, float size, int next_shot, Vector2 resize_factor)
       {
          Texture = texture;
          Position = position;
          spritebatch = sb;
          ClientBounds = new Rectangle(clientbounds.X, (int)(clientbounds.Y + 0.05f * clientbounds.Height), (int)(clientbounds.Width), (int)(clientbounds.Height * 0.9f));
          Size = size;
          NextShoot = next_shot;
          Resize = resize_factor;
       }
         

         public virtual void Update(GameTime time)
      {
          // Top Bottom Collision detection
              if (Position.Y < 0)
                  Position.Y = 0;
              if (Position.Y + (Texture.Height * Size * Resize.Y) > ClientBounds.Height)
                  Position.Y = ClientBounds.Height - (Texture.Height * Size * Resize.Y);
              // Collision Rectangle update
              Collision = new Rectangle((int)(Position.X + 0.1f *(Texture.Width * Size * Resize.X)), (int)(Position.Y + Texture.Height * Size * Resize.Y *0.05f ), (int)(Texture.Width * Size * Resize.X *0.85f), (int)(Texture.Height * Size * Resize.Y *0.9f));
              PlaneMid = (int)(Position.Y + (Texture.Height * Size * Resize.Y) / 2);
              LastShoot += time.ElapsedGameTime.Milliseconds;
              if (LastShoot > NextShoot)
              {
                  shoot_okay = true;
                  LastShoot -= NextShoot;
              }
              if (shoot_okay && (Automatic || (Mouse.GetState().LeftButton == ButtonState.Pressed) || (Keyboard.GetState().IsKeyDown(Keys.Space)) || GP_state.IsButtonDown(0) || GP_state.IsButtonDown(1) || GP_state.IsButtonDown(2) ||GP_state.IsButtonDown(3)))
              {
                  Shooting = true;
                  shoot_okay = false;
              }
      }
      public virtual void  Draw()
      {
          spritebatch.Draw(Texture, new Rectangle((int)Position.X, (int)Position.Y, (int)(Texture.Width * Size * Resize.X), (int)(Texture.Height * Size * Resize.Y)), null, Color.White, 0, Vector2.Zero, plane_effect, 0);
      }
    }
     class UserPlane : BasicPlanes
     {
         public UserPlane(Texture2D texture, Vector2 position, SpriteBatch sb, Rectangle clientbounds, Vector2 resizef)
             : base(texture, position, sb, clientbounds,1,(int)(800 * resizef.X),resizef)
         {
             plane_effect = SpriteEffects.FlipHorizontally;
             Type = PlaneType.User;
             Automatic = false;
             prevMS = Mouse.GetState();
         }
         public int Bonus = 0;
         float ydiff;
         bool moving_mouse;
         MouseState prevMS;
         float maxydiff { get { return ((6 * Resize.X+ Bonus / 4) * Size); } }
         float minydiff { get { return ((-8 * Resize.X - Bonus / 4) * Size); } }
         public float gpy;
         public override void Update(GameTime time)
         {
             
                 gpy = GP_state.Z;
                 if (gpy > -0.3f && gpy < 0.3f)
                     gpy = 0;
                 gpy *= (8 * Size);
                 if (GP_state.IsButtonDown(4) || GP_state.IsButtonDown(5) || GP_state.IsButtonDown(6) ||GP_state.IsButtonDown(7))
                     gpy /= 2;
                 moving_mouse = true;
                 Mouse.SetPosition(50, (int)(Mouse.GetState().Y + gpy));
            
             if(!moving_mouse)    
                if (Mouse.GetState().X != prevMS.X && Mouse.GetState().Y != prevMS.Y)
                     moving_mouse = true;
             else
                if (Keyboard.GetState().IsKeyDown(Keys.Up) || Keyboard.GetState().IsKeyDown(Keys.Down))
                     moving_mouse = false;
             
                 if (moving_mouse)
                 {
                     ydiff = Mouse.GetState().Y - PlaneMid;
                     if (ydiff > maxydiff)
                         ydiff = maxydiff;
                     else if (ydiff < minydiff)
                         ydiff = minydiff;
                 }
                 else
                 {
                     if (Keyboard.GetState().IsKeyDown(Keys.Down) || Keyboard.GetState().IsKeyDown(Keys.S))
                         ydiff = maxydiff;
                     else if (Keyboard.GetState().IsKeyDown(Keys.Up) || Keyboard.GetState().IsKeyDown(Keys.W))
                         ydiff = minydiff;

                 }
                 prevMS = Mouse.GetState();
                 gpy = (float)Math.Round(gpy, 2);
             Position.Y += ydiff;
             if (!moving_mouse)
                 ydiff = 0;
             base.Update(time);
         }
     }
     class EnemyPlane : BasicPlanes
     {
         public float Speed;
         public bool DELETIONREQUEST;

         public EnemyPlane(Texture2D texture, double rand_y, SpriteBatch sb, Rectangle clientbounds, float size, float speed, Vector2 resizef)
             : base(texture, new Vector2(clientbounds.Width, (float)(clientbounds.Height/20 + 0.9f * rand_y * clientbounds.Height)), sb, clientbounds, size,1000, resizef)
         {
             Speed = speed;
             Type = PlaneType.Enemy;
             Automatic = true;
         }
         public override void Update(GameTime time)
         {
             Position.X -= Speed;
             
             if (Position.X < -Texture.Width)
                 DELETIONREQUEST = true;
            
             base.Update(time);
         }

     }
}
