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
        SpriteFont Font, Big, Small;
        Random rand;
        UserPlane Player;
        List<BasicPlanes> Life;
        int Userplanecolor;
        int Lives = 3;
        float Score = 0;
        AudioEngine ae;
        SoundBank sb;
        WaveBank wb;
        Cue bg_music;

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
            Small = Content.Load<SpriteFont>("Small");
            plane = new Texture2D[3];
            plane[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"planes/red");
            plane[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"planes/green");
            plane[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"planes/blue");
            Mouse.SetPosition(Window.ClientBounds.Width / 2, Window.ClientBounds.Height / 2);
            Userplanecolor = rand.Next(0, 2);
            Current_GameState = GameStates.Start;
            NextSpawn = 2000;
            // Init music
            ae = new AudioEngine(@"Content\Music.xgs");
            wb = new WaveBank(ae, @"Content\wMusic.xwb");
            sb = new SoundBank(ae, @"Content\sMusic.xsb");
            bg_music = sb.GetCue("bg");

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
            Life = new List<BasicPlanes>();
            reset_clouds();
            for (int i = 0; i < 3; i++)
            {
                Life.Add(new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life + 10) * i + 5, Font.MeasureString("|").Y + 5), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue));
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
        float ElapsedMS;
        List<EnemyPlane> Enemies = new List<EnemyPlane>();
        List<Cloud> Clouds = new List<Cloud>();
        List<UserBomb> UBombs = new List<UserBomb>();
        List<EnemyBomb> EBomb = new List<EnemyBomb>();
        List<Explosion> Explosions = new List<Explosion>();
        int index;
        int Enemies_Passed;
        int nextlive = 15000;
        int current_4_nextlive = 0;
        float current_spawn_time;
        float correct_time;
        float negative_bonus;
        bool exit = false;
        protected override void Update(GameTime gameTime)
        {
           exit = (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed || Keyboard.GetState().IsKeyDown(Keys.Escape));
            if (exit)
                this.Exit();
            switch(Current_GameState)
            {
                case GameStates.Start:
                    #region Start
                    //Draw lives
                    if (this.IsActive && (Keyboard.GetState().GetPressedKeys().Length > 0 || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed))
                    {
                        Current_GameState = GameStates.Game;
                            bg_music.Play();
                    }
                    base.Update(gameTime);
                    break;
                    #endregion
                case GameStates.Game:
                    #region Gameplay
                    if (this.IsActive)
                     
            {
                if (Score < -100)
                {
                    Lives--;
                    Score += 101;
                }
                current_4_nextlive += gameTime.ElapsedGameTime.Milliseconds;
                if (current_4_nextlive > nextlive)
                {
                    current_4_nextlive -= nextlive;
                    nextlive += 1500;
                    Life.Add(new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life + 10) * Lives + 5, Font.MeasureString("|").Y + 5), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue));
                    Lives++;
                }
                current_spawn_time = (float)(NextSpawn - ((gameTime.TotalGameTime.TotalSeconds - correct_time) * 20 - negative_bonus));

                Score += (float)(Math.Sqrt(gameTime.TotalGameTime.TotalSeconds)/70);
                foreach (UserBomb b in UBombs)
                    b.Update();
                foreach (EnemyBomb b in EBomb)
                    b.Update();
                // Spawn enemies every x ms
                ElapsedMS += gameTime.ElapsedGameTime.Milliseconds;
                if (ElapsedMS >= current_spawn_time)
                {
                    ElapsedMS -=  current_spawn_time;
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
                        Enemies_Passed++;
                        Score *= 0.9f;
                        negative_bonus += (float)(5f * Math.Pow(Enemies_Passed, 0.55f));
                        i--;
                    }
                }
                for (int i = 0; i < EBomb.Count; i++)
                {
                    if (Player.Collision.Intersects(EBomb[i].Collision_detection))
                    {
                        Score *= 0.65f;
                        EBomb.RemoveAt(i);
                        i--;
                        Lives--;
                        Life.RemoveAt(Life.Count - 1);
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
                            Score *= 1.2f;
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
                        Lives -= 3;
                        Score /= 2;
                        try
                        {
                            for (int i = 0; i < 3; i++)
                                Life.RemoveAt(Life.Count - 1);
                        }
                        catch (Exception)
                        {
                            Current_GameState = GameStates.Over;
                            reset(gameTime);
                        }
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
                    reset(gameTime);

                }
            }
                    
                base.Update(gameTime);
                break;
                    #endregion
                case GameStates.Over:
                    #region GameOver
                if (this.IsActive &&  (gameTime.TotalGameTime.TotalSeconds > correct_time) && ( Keyboard.GetState().GetPressedKeys().Length > 0  || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed))
                {
                    correct_time = gameTime.TotalGameTime.Seconds;
                    Lives = 3;
                    Score = 0;
                    Current_GameState = GameStates.Game;
                    UBombs.Clear();
                    EBomb.Clear();
                    Enemies.Clear();
                    Life.Clear();
                    Explosions.Clear();
                    for (int i = 0; i < 3; i++)
                    {
                        Life.Add(new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life + 10) * i + 5, Font.MeasureString("|").Y + 5), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue));
                    }
                    bg_music.Play();

                }
                base.Update(gameTime);
                break;
                #endregion
            }
                        
        }
            private void reset(GameTime gameTime)
            {
                    Current_GameState = GameStates.Over;
                    score = Convert.ToString(Math.Round(Score, 0));
                    correct_time = (float)gameTime.TotalGameTime.TotalSeconds + 1.5f;
                    Score += 50;
                    current_4_nextlive = 0;
                    reset_clouds();
                    bg_music.Stop(AudioStopOptions.Immediate);
                    bg_music.Dispose();
                    bg_music = sb.GetCue("bg");
                       
            }
            
            private void reset_clouds()
            {
                Clouds.Clear();
                for (int i = 0; i < 6; i++)
                    Clouds.Add(new Cloud(new Vector2(rand.Next(0, Window.ClientBounds.Width), rand.Next(10, 60)), (float)((rand.NextDouble() * 3) + 1), (float)(0.3 + rand.NextDouble() / 3), cloud, Window.ClientBounds.Width, spriteBatch));
            }
        
        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        string textl1 = "Welcome to Planes 2D.";
        string textl2 = "Press any key or click with the mouse to start.";
        string msg_over = "Game over! Your Score:";
        string msg_continue1 = "Press Escape to quit, click with the mouse";
        string msg_continue2 = "or with any other button to restart the game.";
        string msg_music_by = "Music:\n- WrathGames Studio [http://wrathgames.com/blog] | Licence: CC-BY 3.0\n" +
            "Images:\n- All images are licenced under CC-0";
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
                    spriteBatch.DrawString(Small, msg_music_by, new Vector2(Window.ClientBounds.Width / 2 - Small.MeasureString(msg_music_by).X / 2, Window.ClientBounds.Height - Small.MeasureString(msg_music_by).Y), Color.White);
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
            foreach (BasicPlanes b in Life)
                b.Draw();
            break; 
                    #endregion
                case GameStates.Over:
                    #region GameOver
                    text_over = new Vector2(Window.ClientBounds.Width /2 - Font.MeasureString(msg_over).X /2, Window.ClientBounds.Height /10);
                    spriteBatch.DrawString(Font,msg_over,text_over,Color.White);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Big.MeasureString(score).X / 2, Window.ClientBounds.Height /2 - Big.MeasureString(score).Y/2);
                    spriteBatch.DrawString(Big, score, text_over, Color.DarkGreen);
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
