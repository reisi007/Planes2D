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
    class Explosion
    {
        Texture2D spritesheet;
        public bool Delete;
        int pic_w = 222;
        Vector2 pic_r_c = new Vector2(4, 7);
        Rectangle source_rectangle, target_rectangle;
        SpriteBatch Sb;
        Vector2 Resize;
       public Explosion(Vector2 pos, Texture2D sprite,SpriteBatch sb, Vector2 resize)
        {
            spritesheet = sprite;
            source_rectangle = new Rectangle(0, 0, pic_w, pic_w);
            currentsprite = new int[2];
            Sb = sb;
            Resize = resize;
            target_rectangle = new Rectangle((int)(pos.X - pic_w * Resize.X), (int)(pos.Y - (pic_w * Resize.X) / 2), (int)(pic_w * Resize.X), (int)(pic_w * Resize.Y));
        }
       int ms;
       int next = 16*3;
       int[] currentsprite;
       public void Update(GameTime gt)
       {
           ms += gt.ElapsedGameTime.Milliseconds;
           if (next < ms && !Delete)
           {
               ms -= next;
               currentsprite[0]++;
               if (currentsprite[0] > 3)
               {
                   currentsprite[0] = 0;
                   currentsprite[1]++;
               }
               if (currentsprite[1] > 6)
               {
                   currentsprite[1] = 6;
                   Delete = true;
               }
               source_rectangle = new Rectangle(currentsprite[0] * pic_w, currentsprite[1] * pic_w, pic_w, pic_w);
               target_rectangle = new Rectangle((int)(target_rectangle.X - 5 * Resize.X), target_rectangle.Y, (int)(pic_w * Resize.X), (int)(pic_w * Resize.Y));
           }
       }
       public void Draw()
       {
           Sb.Draw(spritesheet, target_rectangle, source_rectangle, Color.White);
       }
    }
}
