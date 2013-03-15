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
        Texture2D grass, cloud, bomb, explosion;
        Texture2D[] plane;
        enum PlaneColor {Red = 0, Green = 1, Blue = 2};
        enum GameStates { Start, Game, Over };
        GameStates Current_GameState;
        SpriteFont Font, Big;
        Random rand;
        UserPlane Player;
        BasicPlanes[] Life;
        int Userplanecolor;
        int Lives = 3;
        float Score = 0;

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
            explosion = Content.Load<Texture2D>("explosion");
            Font = Content.Load<SpriteFont>("Font");
            Big = Content.Load<SpriteFont>("Font_Big");
            plane = new Texture2D[3];
            plane[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"planes/red");
            plane[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"planes/green");
            plane[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"planes/blue");
            Mouse.SetPosition(Window.ClientBounds.Width / 2, Window.ClientBounds.Height / 2);
            Userplanecolor = rand.Next(0, 2);
            Current_GameState = GameStates.Start;
            NextSpawn = 2000;
            // Min = 1000;
#if DEBUG
            // Debug screen resolution
#else
            // Release screen resolution
            graphics.IsFullScreen = true;
#endif
            graphics.ApplyChanges();
            base.Initialize();
        }
        float plane_resize_life = 0.3f;
        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);
            Player = new UserPlane(plane[Userplanecolor], new Vector2(Window.ClientBounds.Width / 10, Window.ClientBounds.Height / 2), spriteBatch,Window.ClientBounds);
            for (int i = 0; i < 6; i ++)
                Clouds.Add(new Cloud(new Vector2(rand.Next(0, Window.ClientBounds.Width), rand.Next(10, 60)), (float)((rand.NextDouble() * 3)+1), (float)(0.3 + rand.NextDouble() / 3), cloud, Window.ClientBounds.Width, spriteBatch));
            Life = new BasicPlanes[3];
            for (int i = 0; i < 3; i++)
            {
                Life[i] = new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life + 10) * i + 5, Font.MeasureString("|").Y + 5), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue);
            }
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
        List<Explosion> Explosions = new List<Explosion>();
        int index;
        int current_spawn_time;
        int correct_time = 0;
        protected override void Update(GameTime gameTime)
        {
            // Allows the game to exit
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape))
                this.Exit();
            switch(Current_GameState)
            {
                case GameStates.Start:
                    #region Start
                    //Draw lives
                    if (this.IsActive && (Keyboard.GetState().GetPressedKeys().Length > 0 || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed))
                        Current_GameState = GameStates.Game;
                    base.Update(gameTime);
                    break;
                    #endregion
                case GameStates.Game:
                    #region Gameplay
                    if (this.IsActive)
            {
                current_spawn_time = NextSpawn - (gameTime.TotalGameTime.Seconds - correct_time) * 10;
                Score += gameTime.ElapsedGameTime.Milliseconds / 750f;
                foreach (UserBomb b in UBombs)
                    b.Update();
                foreach (EnemyBomb b in EBomb)
                    b.Update();
                // Spawn enemies every x ms
                ElapsedMS += gameTime.ElapsedGameTime.Milliseconds;
                if (ElapsedMS > current_spawn_time)
                {
                    ElapsedMS -= current_spawn_time;
                    index = (int)(3 * rand.NextDouble() - 0.001f);
                    Enemies.Add(new EnemyPlane(plane[index], rand.NextDouble(), spriteBatch, Window.ClientBounds, (float)(0.75f + rand.NextDouble() / 4f), (float)(3f + 5 * (-1 / 3 * rand.NextDouble()))));
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
                        EBomb.Add(new EnemyBomb(bomb, Enemies[i].Position, spriteBatch, Enemies[i].Speed + 1, Enemies[i].Collision));
                        Enemies[i].Shooting = false;
                    }
                    if (Enemies[i].DELETIONREQUEST)
                    {
                        Enemies.RemoveAt(i);
                        Score -= 5;
                        i--;
                    }
                }
                for (int i = 0; i < EBomb.Count; i++)
                {
                    if (Player.Collision.Intersects(EBomb[i].Collision_detection))
                    {
                        Score -= 15;
                        EBomb.RemoveAt(i);
                        i--;
                        Lives--;
                    }
                    if (i >= 0)
                    {
                        if (EBomb[i].Position.X < -EBomb[i].Collision_detection.Width)
                        {
                            EBomb.RemoveAt(i);
                            i--;
                        }
                    }
                }


                for (int i = 0; i < UBombs.Count; i++)
                {

                    for (int y = 0; y < Enemies.Count; y++)
                    {
                        if (UBombs.Count <= 0 || Enemies.Count <= 0)
                            break;
                        if (y <= 0)
                            y = 0;
                        if (i <= 0)
                            i = 0;
                        if (Enemies[y].Collision.Intersects(UBombs[i].Collision_detection))
                        {
                            Explosions.Add(new Explosion(new Vector2(Enemies[y].Position.X + Enemies[y].Collision.Width ,Enemies[y].Position.Y + Enemies[y].Collision.Height/2), explosion, spriteBatch));
                            Score += 2 * Enemies[y].Speed;
                            Enemies.RemoveAt(y);
                            UBombs.RemoveAt(i);
                            i--;
                            y--;
                            
                        }
                    }
                }

                Player.Update(gameTime);
                if (Player.Shooting)
                {
                    Player.Shooting = false;
                    UBombs.Add(new UserBomb(bomb, Player.Position, spriteBatch, rand.Next(2,6)/2f, Player.Collision));
                }
                for( int y = 0; y < Enemies.Count;y++)
                {
                    if (Enemies[y].Collision.Intersects(Player.Collision) && y >= 0)
                    {
                        Enemies.RemoveAt(y);
                        y--;
                        Lives = 0;
                    }
                }
                for (int i = 0; i < Explosions.Count; i++)
                {
                    if (i >= 0)
                    {
                        Explosions[i].Update(gameTime);
                        if (Explosions[i].Delete)
                        {
                            Explosions.RemoveAt(i);
                            i--;
                        }
                    }
                }
                if (Lives <= 0)
                {
                    Current_GameState = GameStates.Over;
                    score = Convert.ToString(Math.Round(Score, 0));
                }
            }
                    
                base.Update(gameTime);
                break;
                    #endregion
                case GameStates.Over:
                    #region GameOver
                if (this.IsActive && (Keyboard.GetState().GetPressedKeys().Length > 0 || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed))
                {
                    correct_time = gameTime.TotalGameTime.Seconds;
                    Lives = 3;
                    Score = 0;
                    Current_GameState = GameStates.Game;
                    UBombs.Clear();
                    EBomb.Clear();
                    Enemies.Clear();
                }
                base.Update(gameTime);
                break;
                #endregion
            }
        }
        
        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        string textl1 = "Welcome to Planes 2D.";
        string textl2 = "Press any button or click with the Mouse to start.";
        string msg_over = "Game over! Your Score:";
        string msg_continue1 = "Press Escape to quit, click with the mouse";
        string msg_continue2 = "or with any other button to restart the game!";
        string score;
        Vector2 text_over;
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);
            spriteBatch.Begin();
            switch(Current_GameState)
            {
                case GameStates.Start:
                    #region Start
                    spriteBatch.DrawString(Font, textl1, new Vector2(Window.ClientBounds.Width / 2 - Font.MeasureString(textl1).X / 2, Window.ClientBounds.Height / 2 - Font.MeasureString(textl1).Y / 2), Color.White);
                    spriteBatch.DrawString(Font, textl2, new Vector2(Window.ClientBounds.Width / 2 - Font.MeasureString(textl2).X / 2, Window.ClientBounds.Height / 2 + Font.MeasureString(textl2).Y / 2), Color.White);
                    break;
                #endregion
                case GameStates.Game:
                    #region Gameplay
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
            foreach (Explosion e in Explosions)
                e.Draw();
            spriteBatch.DrawString(Font, Convert.ToString((int)Score), new Vector2(0), Color.Black);
            for (int i = 0; i < Lives; i++)
            {
                Life[i].Draw();
            }
            break; 
                    #endregion
                case GameStates.Over:
                    #region GameOver
                    text_over = new Vector2(Window.ClientBounds.Width /2 - Font.MeasureString(msg_over).X /2, Window.ClientBounds.Height /10);
                    spriteBatch.DrawString(Font,msg_over,text_over,Color.White);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Big.MeasureString(score).X / 2, Window.ClientBounds.Height /2 - Big.MeasureString(score).Y/2);
                    spriteBatch.DrawString(Big, score, text_over, Color.Red);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Font.MeasureString(msg_continue2).X / 2, Window.ClientBounds.Height - 10 - Font.MeasureString(msg_continue2).Y);
                    spriteBatch.DrawString(Font, msg_continue2, text_over, Color.White);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Font.MeasureString(msg_continue1).X / 2, text_over.Y - 10 - Font.MeasureString(msg_continue1).Y);
                    spriteBatch.DrawString(Font, msg_continue1, text_over, Color.White);
            break;
                #endregion
            }
            spriteBatch.End();
            base.Draw(gameTime);
        }
    }
}
