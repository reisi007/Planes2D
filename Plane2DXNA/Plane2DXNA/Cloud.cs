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
    class Cloud
    {
        Vector2 Position;
        float Speed,Resize;
        Texture2D cloud;
        int WindowW;
        bool drawsecond = false;
        SpriteBatch draw;
        public Cloud(Vector2 position, float speed, float size, Texture2D texture, int flywidth, SpriteBatch spriteB)
        {
            Position = position;
            Speed = speed;
            Resize = size;
            cloud = texture;
            WindowW = flywidth;
            draw = spriteB;
        }
        public void Update()
        {
            Position.X -= Speed;
            if (Position.X <= -cloud.Width)
            {
                Position.X += WindowW;
                drawsecond = false;
            }
            if (Position.X <= 0 && Position.X > -cloud.Width)
                drawsecond = true;
        }

        public void Draw()
        {
            draw.Draw(cloud, new Rectangle((int)Position.X, (int)Position.Y, (int)(cloud.Width * Resize), (int)(cloud.Height * Resize)), Color.White);
            if(drawsecond)
                draw.Draw(cloud, new Rectangle((int)(Position.X + WindowW), (int)Position.Y, (int)(cloud.Width * Resize), (int)(cloud.Height * Resize)), Color.White);
        }


    }
}
