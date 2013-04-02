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
    public partial class Game1 : Microsoft.Xna.Framework.Game
    {
        SpriteFont[] Fonts;
        #region Get the right fonts
        private void Initialize_Helper()
        {
            Fonts = get_recommended_font(new string[] { textl1, textl2, textl3, msg_over, msg_continue1, msg_continue2, msg_music_by, score, missed,  "-18" /*This is GPY*/});
        }

        private SpriteFont[] get_recommended_font(string[] text)
        {
            Vector2 tmp_l_w = new Vector2(0f);
            List<SpriteFont> Final_fontlist = new List<SpriteFont>();
            for (int i = 0; i < Font4TextLength; i++)
            {
                // Maximal width
                switch (i)
                {
                    // All cases, when needed
                   /* case (int)Font4Text.Welcome1:
                    case (int)Font4Text.Welcome2:
                    case (int)Font4Text.Welcome3:
                    case (int)Font4Text.Over1:
                    case (int)Font4Text.Continue1:
                    case (int)Font4Text.Continue2:
                    case (int)Font4Text.Music_by:
                    case (int)Font4Text.Score:
                    case (int)Font4Text.Missed:
                    case (int)Font4Text.GPY:*/
                    default:
                        tmp_l_w.X = Window.ClientBounds.Width;
                        break;
                }
                // Maximal height
                switch (i)
                {
                    default:
                        tmp_l_w.Y = Window.ClientBounds.Height;
                        break;
                }
                Final_fontlist.Add(get_recommended_font(text[i], tmp_l_w));
            }
            return Final_fontlist.ToArray();
        }
        private SpriteFont get_recommended_font(string text, Vector2 max_dimension)
        {
            Vector2 tmp_dimension = new Vector2();
            SpriteFont sf = Score_fonts[0];
            foreach (SpriteFont f in Score_fonts)
            {
                tmp_dimension.X = f.MeasureString(text).X;
                tmp_dimension.Y = f.MeasureString(text).Y;
                if ((tmp_dimension.X < max_dimension.X)&& (tmp_dimension.Y < max_dimension.Y))
                    sf = f;
            }
            
            return sf;
        }

        // Enum for some texts, starting with 0
        enum Font4Text
        {
            Welcome1 = 0,   // textl1
            Welcome2,       // textl2
            Welcome3,       // textl3
            Over1,          // msg_over
            Continue1,      // msg_continue1
            Continue2,      // msg_continue2
            Music_by,       // msg_music_by
            Score,          // score
            Missed,         // missed
            GPY             // Gamepad + / -
        }
        const short Font4TextLength = 10;
        #endregion
        // Variables needed for reset()
        int number_of_dots_needed;
        string tmp_score;
        SpriteFont Fin_Scorefont;
        // Helper for resetting the game


        private void reset(GameTime gameTime)
        {
            Current_GameState = GameStates.Over;
            Enemies_Passed = 0;
            get_commas4score();
            Fin_Scorefont = get_recommended_font(score, new Vector2(Window.ClientBounds.Width - 50, Window.ClientBounds.Height));
            correct_time = (float)gameTime.TotalGameTime.TotalSeconds + 1.5f;
            current_4_nextlive = 0;
            bg_music.Stop(AudioStopOptions.Immediate);
            bg_music.Dispose();
            bg_music = sb.GetCue("bg");
            BonusTracker.Clear();
            spawn_time_multip = 1;

        }
        // Helper for adding one life
        private void addlive()
        {
            if (Lives <= 16)
            {
                Life.Add(new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life * ResizeFactor.X + 5 * ResizeFactor.X) * Lives + 5, 0.1f * Window.ClientBounds.Height), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue, ResizeFactor));
                Lives++;
                spawn_time_multip *= 1.25f;
            }
            else
            {
                Score *= 1.0898d;
                spawn_time_multip *= 1.7f;
            }
        }
        // Helper for resetting clouds
        private void reset_clouds()
        {
            Clouds.Clear();
            for (int i = 0; i < 6; i++)
                Clouds.Add(new Cloud(new Vector2(rand.Next(0, Window.ClientBounds.Width), rand.Next(10, 60)), (float)((rand.NextDouble() * 3) + 1), (float)(0.3 + rand.NextDouble() / 3), cloud, Window.ClientBounds.Width, spriteBatch));
        }
        // Helper for starting the game
        private void start_game(GameTime gameTime)
        {
            UBombs.Clear();
            EBomb.Clear();
            Enemies.Clear();
            Life.Clear();
            Explosions.Clear();
            Score = 0;
            Current_GameState = GameStates.Game;
            Lives = 0;
            GP_isConnected = GP_state.IsButtonDown(9);
            for (int i = 0; i < 3; i++)
            {
                addlive();
            }
            correct_time = gameTime.TotalGameTime.Seconds;
            reset_clouds();
            bg_music.Play();
            Fin_Scorefont = null;
            NextSpawn = 2000;
            // Spawn BonusTracker and Player plane
            Player = new UserPlane(plane[Userplanecolor], new Vector2(Window.ClientBounds.Width / 10, Window.ClientBounds.Height / 2), spriteBatch, Window.ClientBounds, ResizeFactor);
            BonusTracker = new Bonus(spriteBatch, star, (int)(Window.ClientBounds.Height * 0.1f));

        }
        // Temporary value
        int tmp;
        private void get_commas4score()
        {
            score = Convert.ToString(Math.Round(Math.Pow(Score, 0.4f), 0));
            number_of_dots_needed = (int)(score.Length / 3);
            tmp_score = "";
            for (int i = 0; i <= number_of_dots_needed; i++)
            {
                if (i == 0)
                {
                    tmp = score.Length % 3;
                    tmp_score = score.Substring(0, tmp);

                }
                else
                {
                    if (tmp_score != "")
                        tmp_score += ".";
                    tmp_score += score.Substring(tmp, 3);
                    tmp += 3;

                }

            }
            score = tmp_score;
        }
    }
}
