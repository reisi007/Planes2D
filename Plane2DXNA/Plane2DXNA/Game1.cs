using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace Plane2DXNA
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        Texture2D grass, cloud, bomb;
        Texture2D[] plane;
        enum PlaneColor {Red = 0, Green = 1, Blue = 2};
        Random rand;
        UserPlane Player;
        int Userplanecolor;
        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            Content.RootDirectory = "Content";
            rand = new Random();
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        int NextSpawn;
        protected override void Initialize()
        {
            cloud = Content.Load<Texture2D>("cloud");
            grass = Content.Load<Texture2D>("grass");
            bomb = Content.Load<Texture2D>("bullet");
            plane = new Texture2D[3];
            plane[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"planes/red");
            plane[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"planes/green");
            plane[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"planes/blue");
            Mouse.SetPosition(Window.ClientBounds.Width / 2, Window.ClientBounds.Height / 2);
            Userplanecolor = rand.Next(0, 2);
            NextSpawn = 1000;
#if DEBUG
            // Debug screen resolution
#else
            // Release screen resolution
#endif
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);
            Player = new UserPlane(plane[Userplanecolor], new Vector2(Window.ClientBounds.Width / 10, Window.ClientBounds.Height / 2), spriteBatch,Window.ClientBounds);
            for (int i = 0; i < 3; i ++)
                Clouds.Add(new Cloud(new Vector2(rand.Next(0, Window.ClientBounds.Width), rand.Next(10, 60)), (int)((rand.NextDouble() * 3)+1), (float)(0.5 + rand.NextDouble() / 2), cloud, Window.ClientBounds.Width, spriteBatch));
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        float p_grass = 0;
        float Speed = 1;
        int ElapsedMS;
        List<EnemyPlane> Enemies = new List<EnemyPlane>();
        List<Cloud> Clouds = new List<Cloud>();
        List<UserBomb> UBombs = new List<UserBomb>();
        List<EnemyBomb> EBomb = new List<EnemyBomb>();
        int index;
        protected override void Update(GameTime gameTime)
        {
            // Allows the game to exit
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                this.Exit();
            // Spawn enemies every x ms
            ElapsedMS += gameTime.ElapsedGameTime.Milliseconds;
            if(ElapsedMS > NextSpawn)
            {
                ElapsedMS -= NextSpawn;
                index = (int)(3*rand.NextDouble()-0.001f);
                Enemies.Add(new EnemyPlane(plane[index], rand.NextDouble(), spriteBatch, Window.ClientBounds, (float)(0.75f + rand.NextDouble() / 2f), (float)(3f + 5 * (-1 / 3 * rand.NextDouble()))));
            }
            //Update Clouds
            foreach (Cloud c in Clouds)
                c.Update();
            // First grass position
            p_grass -= Speed;
            if (p_grass <= -grass.Width)
                p_grass += grass.Width;
            for (int i = 0; i < Enemies.Count; i++)
            {
                Enemies[i].Update(gameTime);
                if (Enemies[i].Shooting)
                {
                    EBomb.Add(new EnemyBomb(bomb, Enemies[i].Position, spriteBatch, Enemies[i].Speed +1, Enemies[i].Collision));
                    Enemies[i].Shooting = false;
                }

                if (Enemies[i].DELETIONREQUEST)
                {
                    Enemies.RemoveAt(i);
                    i--;
                }
            }
            Player.Update(gameTime);
            if (Player.Shooting)
            {
                Player.Shooting = false;
                UBombs.Add(new UserBomb(bomb, Player.Position, spriteBatch, 1,Player.Collision));
            }
            foreach (UserBomb b in UBombs)
                b.Update();
            foreach (EnemyBomb b in EBomb)
                b.Update();
            base.Update(gameTime);
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);
            spriteBatch.Begin();
            // Draw grass
            for (int i = (int)p_grass; i <= Window.ClientBounds.Width; i += grass.Width)
                spriteBatch.Draw(grass, new Rectangle(i, Window.ClientBounds.Height - grass.Height, grass.Width, grass.Height), new Rectangle(0, 0, grass.Width, grass.Height), Color.White, 0, Vector2.Zero, SpriteEffects.None, 0);
            foreach (Cloud c in Clouds)
                c.Draw();
            //Draw enemies
            foreach (EnemyPlane e in Enemies)
                e.Draw();
            foreach (UserBomb b in UBombs)
                b.Draw();
            foreach (EnemyBomb b in EBomb)
                b.Draw();
            Player.Draw();
            spriteBatch.End();
            base.Draw(gameTime);
        }
    }
}
