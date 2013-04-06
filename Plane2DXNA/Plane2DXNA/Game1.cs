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
#if WINDOWS
using Nuclex.Input;
using Nuclex.Input.Devices;
#endif


namespace Plane2DXNA
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public partial class Game1 : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        Texture2D grass, cloud, bomb, explosion, star;
        Texture2D[] plane, plane_life;
        enum PlaneColor {Red = 0, Green = 1, Blue = 2};
        enum GameStates { Start, Game, Over };
        GameStates Current_GameState;
        Random rand;
        UserPlane Player;
        List<BasicPlanes> Life;
        int Userplanecolor;
        int Lives = 0;
        double Score;
        AudioEngine ae;
        SoundBank sb;
        WaveBank wb;
        Cue bg_music;
        Bonus BonusTracker;
       List<SpriteFont> Score_fonts;
#if WINDOWS
        InputManager ManageI;
#endif
        Vector2 ResizeFactor;
        public Game1()
        {
            graphics = new GraphicsDeviceManager(this);
            rand = new Random();
            Content.RootDirectory = "Content";
#if WINDOWS
            ManageI = new InputManager();
#endif
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        int NextSpawn;
        Vector2 TMP_W_H;
        protected override void Initialize()
        {
            // Set the resolution of the game
#if DEBUG
            graphics.PreferredBackBufferWidth = 1600;
            graphics.PreferredBackBufferHeight = 900;
            TMP_W_H = new Vector2(1600,900);

#else
            graphics.PreferredBackBufferHeight = graphics.GraphicsDevice.DisplayMode.Height;
            graphics.PreferredBackBufferWidth = graphics.GraphicsDevice.DisplayMode.Width;
            TMP_W_H = new Vector2(graphics.GraphicsDevice.DisplayMode.Width, graphics.GraphicsDevice.DisplayMode.Height);
            graphics.IsFullScreen = true;
#endif
            ResizeFactor = new Vector2(TMP_W_H.X / 800f, TMP_W_H.Y / 480f);
            graphics.ApplyChanges();
            //Loading Images and fonts
            // Images for all resolutions
            bomb = Content.Load<Texture2D>("bullet");
            explosion = Content.Load<Texture2D>("explosion");
            // Load small planes -> plane_life
            plane = new Texture2D[3];
            plane_life = new Texture2D[3];
            plane_life[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"x1/planes/red");
            plane_life[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"x1/planes/green");
            plane_life[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"x1/planes/blue");
            if (ResizeFactor.X > 1.2f)
            {
                //Load bigger textures
                plane[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"x2/planes/red");
                plane[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"x2/planes/green");
                plane[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"x2/planes/blue");
                cloud = Content.Load<Texture2D>(@"x2/cloud");
                grass = Content.Load<Texture2D>(@"x2/grass");
                star = Content.Load<Texture2D>(@"x2/star");
            }
            else
            {
                // Stay with the smaller ones
                plane[(int)PlaneColor.Red] = Content.Load<Texture2D>(@"x1/planes/red");
                plane[(int)PlaneColor.Green] = Content.Load<Texture2D>(@"x1/planes/green");
                plane[(int)PlaneColor.Blue] = Content.Load<Texture2D>(@"x1/planes/blue");
                cloud = Content.Load<Texture2D>(@"x1/cloud");
                grass = Content.Load<Texture2D>(@"x1/grass");
                star = Content.Load<Texture2D>(@"x1/star");
            }
            // Loading 
            ae = new AudioEngine(@"Content\Music.xgs");
            wb = new WaveBank(ae, @"Content\wMusic.xwb");
            sb = new SoundBank(ae, @"Content\sMusic.xsb");
            // Load all fonts from Small -> Big
            Score_fonts = new List<SpriteFont>();
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\10"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\15"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\22"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\30"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\35"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\40"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\45"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\50"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\55"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\60"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\70"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\80"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\110"));
            Score_fonts.Add(Content.Load<SpriteFont>(@"scorefonts\140"));
            number_of_fonts = Score_fonts.Count;
            bg_music = sb.GetCue("bg");
            // Center Mouse position
            Mouse.SetPosition(Window.ClientBounds.Width / 2, Window.ClientBounds.Height / 2);
            // Color of the user plane + Initial game state + Beginning spawn time
            Userplanecolor = rand.Next(0, 2);
            Current_GameState = GameStates.Start;
            base.Initialize();

            Initialize_Helper();
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
            Life = new List<BasicPlanes>();
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        // Initialize variables
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
        int number_of_fonts;
        bool exit = false;
        bool addlife;
        bool cut_down_spawntime = false;
        float spawn_time_multip = 1;
        float next_cut = 7.5f;
        const int max_plane_missed = 5;
#if WINDOWS
        ExtendedGamePadState GP_state;
#endif
        MouseState prvmouse;
        bool GP_isConnected;

        protected override void Update(GameTime gameTime)
        {
#if WINDOWS
            if (Current_GameState == GameStates.Game)
            {
                ManageI.Update();
                GP_state = ManageI.GetGamePad(ExtendedPlayerIndex.Five).GetExtendedState();
                Player.GP_state = GP_state;
            }
           exit = (Keyboard.GetState().IsKeyDown(Keys.Escape) || GP_state.GetButton(8) == ButtonState.Pressed);
#else
            // Put code for other platforms here
#endif
           if (exit)
                this.Exit();
           
            switch(Current_GameState)
            {
                case GameStates.Start:
                    #region Start
                    //Draw lives
#if WINDOWS
                    if (this.IsActive && (Keyboard.GetState().GetPressedKeys().Length > 0 || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed) || GP_state.GetButton(9) == ButtonState.Pressed)
#else
                        // Put code for other platforms here
#endif
                    {
                        start_game(gameTime);
                    }
                    base.Update(gameTime);
                    break;
                    #endregion
                case GameStates.Game:
                    #region Gameplay
                    if (this.IsActive)
#if WINDOWS
                         if (Mouse.GetState().Y > Window.ClientBounds.Height)
                                Mouse.SetPosition((int)(Window.ClientBounds.Width / 2f),Window.ClientBounds.Height);
            if(Mouse.GetState().Y < 0)
                Mouse.SetPosition((int)(Window.ClientBounds.Width / 2), 0);
#else
                    // Put code for other platforms here
#endif
                     
            {
                
                current_4_nextlive += gameTime.ElapsedGameTime.Milliseconds;
                if (current_4_nextlive > nextlive)
                {
                    current_4_nextlive -= nextlive;
                    nextlive += 1500;
                    addlive();
                }
                if ((gameTime.TotalGameTime.Seconds - correct_time) > next_cut)
                {
                    if (!cut_down_spawntime)
                    {
                        spawn_time_multip *= 1.02f;
                        cut_down_spawntime = true;
                        next_cut *= 1.25f;
                    }
                }
                else
                    cut_down_spawntime = false;

                current_spawn_time = (float)((NextSpawn - negative_bonus) / Math.Sqrt(spawn_time_multip));
                if (current_spawn_time < 450)
                    current_spawn_time = 450;
                if (GP_isConnected)
                    current_spawn_time *= 2;
                Score += (Math.Sqrt(gameTime.TotalGameTime.TotalSeconds * spawn_time_multip * 3)/80);
                Score += Lives * 4 + BonusTracker.Bonus_0 / 25;
                foreach (UserBomb b in UBombs)
                    b.Update();
                foreach (EnemyBomb b in EBomb)
                    b.Update();
                // Spawn enemies every x ms
                ElapsedMS += gameTime.ElapsedGameTime.Milliseconds;
                if (ElapsedMS >= current_spawn_time)
                {
                    ElapsedMS -=  current_spawn_time + 6 * BonusTracker.Bonus_0;
                    index = (int)(3 * rand.NextDouble() - 0.001f);
                    Enemies.Add(new EnemyPlane(plane[index], rand.NextDouble() * 0.8d, spriteBatch, Window.ClientBounds, (float)(0.75f + rand.NextDouble() / 4f), (float)(2.5f + 5 * (-1 / 3 * rand.NextDouble())),ResizeFactor));
                }
                //Update Clouds
                foreach (Cloud c in Clouds)
                    c.Update();
                // First grass position
                p_grass -= Speed;
                if (p_grass <= -grass.Width)
                    p_grass += grass.Width;
                Player.Bonus = BonusTracker.Bonus_0;
                for (int i = 0; i < Enemies.Count; i++)
                {
                    Enemies[i].Update(gameTime);
                    if (Enemies[i].Shooting)
                    {
                        EBomb.Add(new EnemyBomb(bomb, Enemies[i].Position, spriteBatch, Enemies[i].Speed + 1, Enemies[i].Collision,ResizeFactor));
                        Enemies[i].Shooting = false;
                    }
                    if (Enemies[i].DELETIONREQUEST)
                    {
                        Enemies.RemoveAt(i);
                        Enemies_Passed++;
                        Score *= (0.9f + BonusTracker.Bonus_0 * 0.03f);
                        negative_bonus += (float)( Math.Pow(3.5,1/BonusTracker.Bonus_1) * Math.Pow(Enemies_Passed, 0.55f));
                        i--;
                        BonusTracker.Clear();
                    }
                }
                if (Enemies_Passed > max_plane_missed)
                {
                    try
                    {
                        Lives /= 2;
                        while (Lives < Life.Count)
                        {
                            try
                            {
                                Life.RemoveAt(Life.Count - 1);
                            }
                            catch(Exception)
                            {
                                reset(gameTime);
                            }

                        }
                        
                        Enemies_Passed -= max_plane_missed;
                    }
                    catch (Exception)
                    {
                        reset(gameTime);
                    }
                    
                }
                for (int i = 0; i < EBomb.Count; i++)
                {
                    if (Player.Collision.Intersects(EBomb[i].Collision_detection))
                    {
                        EBomb.RemoveAt(i);
                        i--;
                        Lives--;
                        try
                        {
                            Life.RemoveAt(Life.Count - 1);
                        }
                        catch (Exception)
                        {
                            reset(gameTime);
                        }
                        BonusTracker.Clear();
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
                            Explosions.Add(new Explosion(new Vector2(Enemies[y].Position.X + Enemies[y].Collision.Width ,Enemies[y].Position.Y + Enemies[y].Collision.Height/2), explosion, spriteBatch,ResizeFactor));
                            Score *= (1.2f + BonusTracker.Bonus_0 * 0.02f);
                            Enemies.RemoveAt(y);
                            UBombs.RemoveAt(i);
                            i--;
                            y--;
                            BonusTracker.Add(ref addlife);
                            if (addlife)
                            {
                                addlife = false;
                                addlive();
                                spawn_time_multip *= 1.03f;
                                negative_bonus *= 0.9f;
                            }
                            else
                                negative_bonus *= 0.97f;
                            
                        }
                    }
                }

                Player.Update(gameTime);
                if (Player.Shooting)
                {
                    Player.Shooting = false;
                    UBombs.Add(new UserBomb(bomb, Player.Position, spriteBatch, rand.Next(2,6)/2f * ResizeFactor.X, Player.Collision,ResizeFactor));
                }
                for( int y = 0; y < Enemies.Count;y++)
                {
                    if (Enemies[y].Collision.Intersects(Player.Collision) && y >= 0)
                    {
                        Enemies.RemoveAt(y);
                        y--;
                        Lives -= 3;
                        Score /= 2 - (BonusTracker.Bonus_0 / 24f);
                        BonusTracker.Clear();
                        try
                        {
                            for (int i = 0; i < 3; i++)
                                Life.RemoveAt(Life.Count - 1);
                        }
                        catch (Exception)
                        {
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
                    missed = "Missed planes left: " + (max_plane_missed - Enemies_Passed);
                    get_commas4score();
                base.Update(gameTime);
                break;
                    #endregion
                case GameStates.Over:
                    #region GameOver
#if WINDOWS
                if (this.IsActive &&  (gameTime.TotalGameTime.TotalSeconds > correct_time) && ( Keyboard.GetState().GetPressedKeys().Length > 0  || Mouse.GetState().LeftButton == ButtonState.Pressed || Mouse.GetState().RightButton == ButtonState.Pressed || Mouse.GetState().MiddleButton == ButtonState.Pressed || GP_state.IsButtonDown(9)))
#else
                    // Put code for other platforms here
#endif
                {
                    start_game(gameTime);

                }
                base.Update(gameTime);
                break;
                   
                #endregion
            }
                prvmouse = Mouse.GetState();        
        }


        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        // Text, which will be drawn sometimes
        string textl1 = "Welcome to Planes 2D.";
        string textl2 = "Press any key or click with the mouse to start.";
        string textl3 = "If you want to use a GamePad, press Start on the Gamepad!";
        string msg_over = "Game over! Your Score:";
        string msg_continue1 = "Press Escape to quit, click with the mouse";
        string msg_continue2 = "or with any other button to restart the game.";
        string msg_music_by = "Music:\n- WrathGames Studio [http://wrathgames.com/blog] | Licence: CC-BY 3.0\n" +
            "Images:\n- All images are licenced under CC-0";
        string score = "", missed = "Missed planes left: 20";
        // Vector representing the upper left corner of the text / images
        Vector2 text_over;

        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);
            spriteBatch.Begin();
            switch(Current_GameState)
            {
                case GameStates.Start:
                    #region Start
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Welcome1], textl1, new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Welcome1].MeasureString(textl1).X / 2, Window.ClientBounds.Height / 2 - Fonts[(int)Font4Text.Welcome1].MeasureString(textl1).Y), Color.White);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Welcome2], textl2, new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Welcome2].MeasureString(textl2).X / 2, Window.ClientBounds.Height / 2 + Fonts[(int)Font4Text.Welcome2].MeasureString(textl2).Y / 2), Color.White);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Welcome3], textl3, new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Welcome3].MeasureString(textl3).X / 2, Window.ClientBounds.Height / 2 + Fonts[(int)Font4Text.Welcome3].MeasureString(textl3).Y / 2 + Fonts[(int)Font4Text.Welcome2].MeasureString(textl2).Y / 2 + Fonts[(int)Font4Text.Welcome1].MeasureString(textl1).Y / 2), Color.White);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Music_by], msg_music_by, new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Music_by].MeasureString(msg_music_by).X / 2, Window.ClientBounds.Height - Fonts[(int)Font4Text.Music_by].MeasureString(msg_music_by).Y), Color.White);
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
            spriteBatch.DrawString(Fonts[(int)Font4Text.Score],score, new Vector2(0), Color.Black);
            spriteBatch.DrawString(Fonts[(int)Font4Text.Missed], missed, new Vector2(Window.ClientBounds.Width - Fonts[(int)Font4Text.Missed].MeasureString(missed).X - 3, 3), Color.Red);
                    if(Player.gpy != 0)
                        spriteBatch.DrawString(Fonts[(int)Font4Text.GPY], Convert.ToString(Player.gpy), new Vector2(Window.ClientBounds.Width - Fonts[(int)Font4Text.GPY].MeasureString(Convert.ToString(Player.gpy)).X, Window.ClientBounds.Height - Fonts[(int)Font4Text.GPY].MeasureString(Convert.ToString(Player.gpy)).Y + 25), Color.Black);
            foreach (BasicPlanes b in Life)
                b.Draw();
            BonusTracker.Draw();
            break; 
                    #endregion
                case GameStates.Over:
                    #region GameOver
                    text_over = new Vector2(Window.ClientBounds.Width /2 - Fonts[(int)Font4Text.Over1].MeasureString(msg_over).X /2, Window.ClientBounds.Height /10);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Over1], msg_over, text_over, Color.White);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Fin_Scorefont.MeasureString(score).X / 2, Window.ClientBounds.Height / 2 - Fin_Scorefont.MeasureString(score).Y / 2);
                    spriteBatch.DrawString(Fin_Scorefont, score, text_over, Color.DarkGreen);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Continue2].MeasureString(msg_continue2).X / 2, Window.ClientBounds.Height - 10 - Fonts[(int)Font4Text.Continue2].MeasureString(msg_continue2).Y);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Continue2], msg_continue2, text_over, Color.White);
                    text_over = new Vector2(Window.ClientBounds.Width / 2 - Fonts[(int)Font4Text.Continue1].MeasureString(msg_continue1).X / 2, text_over.Y - 10 - Fonts[(int)Font4Text.Continue1].MeasureString(msg_continue1).Y);
                    spriteBatch.DrawString(Fonts[(int)Font4Text.Continue1], msg_continue1, text_over, Color.White);
            break;
                #endregion
            }
            spriteBatch.End();
            base.Draw(gameTime);
        }
    }
}
