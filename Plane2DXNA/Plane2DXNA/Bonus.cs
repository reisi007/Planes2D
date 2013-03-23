﻿using System;
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
    class Bonus
    {
        List<Rectangle> BonusList;
        Texture2D Star;
        const int space_between_stars = 5;
        int x_star { get { return (Star.Width + space_between_stars) * (BonusList.Count - rows * stars_per_row) + space_between_stars; } }
        int y_Stars;
        int rows = 0;
        const int stars_per_row = 1;
        const int max_stars = 12 * stars_per_row;
        SpriteBatch Spriteb;
       public Bonus(SpriteBatch sb, Texture2D star, int yStart)
        {
            BonusList = new List<Rectangle>();
            Star = star;
            y_Stars = yStart;
            Spriteb = sb;
        }
       public void Draw()
       {
           foreach (Rectangle r in BonusList)
           {
               Spriteb.Draw(Star, r, Color.White);
           }
       }
       public void Add(ref bool addlive)
       {
           if (BonusList.Count <= max_stars)
           {
               BonusList.Add(new Rectangle(x_star, y_Stars + rows * (Star.Height + 5), Star.Width, Star.Height));
               if (BonusList.Count % stars_per_row == 0)
                   rows++;
               addlive = false;
           }
           else
               addlive = true;
       }
       public void Clear()
       {
           BonusList.Clear();
           rows = 0;
       }
       public int Bonus_1 { get { return BonusList.Count + 1; } }
       public int Bonus_0 { get { return BonusList.Count; } }
    }
}
