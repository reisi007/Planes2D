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
    class BasicBomb
    {
        protected SpriteBatch sb;
        protected Texture2D Texture;
        protected Vector2 Position, Direction;
        protected float Size;
        protected SpriteEffects s_effect;
        public Rectangle Collision_detection;
        protected BasicBomb(Texture2D texture, Vector2 position, Vector2 direction, SpriteBatch draw, float size)
        {
            Texture = texture;
            Position = position;
            Direction = direction;
            Size = size;
            sb = draw;
        }
        public virtual void Update()
        {
            Position += Direction;
        }
        public virtual void Draw()
        {
            sb.Draw(Texture, new Rectangle((int)(Position.X - (Size * Texture.Width) / 2), (int)(Position.Y - (Size * Texture.Height) / 2), (int)(Texture.Width * Size), (int)(Texture.Height * Size)), new Rectangle(0, 0, (int)(Texture.Width * Size), (int)(Texture.Height * Size)), Color.White, 0, new Vector2((int)(Texture.Width * Size), (int)(Texture.Height * Size)), s_effect, 0);
        }
    }
    class EnemyBomb : BasicBomb
    {
        public EnemyBomb(Texture2D texture, Vector2 position, SpriteBatch draw, float random_size, float speed)
            : base(texture, position, new Vector2(-speed, 0), draw, random_size)
        {
            s_effect = SpriteEffects.None;
        }
    }
    class UserBomb : BasicBomb
    {
        public UserBomb(Texture2D texture, Vector2 position, SpriteBatch draw, float random_size, float speed)
            : base(texture, position, new Vector2(speed, 0), draw, random_size)
        {
            s_effect = SpriteEffects.FlipVertically;
        }
    }
}
