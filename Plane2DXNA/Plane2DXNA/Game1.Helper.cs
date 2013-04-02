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
            Missed          // missed
        }
        // Variables needed for reset()
        int number_of_dots_needed;
        string tmp_score;
        SpriteFont Fin_Scorefont;
        int tmp;
        int font;
        // Helper for resetting the game
        private void reset(GameTime gameTime)
        {
            Current_GameState = GameStates.Over;
            Enemies_Passed = 0;
            get_commas4score();
            font = 0;
            tmp = Window.ClientBounds.Width - 50;
            foreach (SpriteFont f in Score_fonts)
            {
                font = (int)f.MeasureString(score).X;
                if (font < tmp)
                    Fin_Scorefont = f;
            }
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
                Life.Add(new BasicPlanes(plane[Userplanecolor], new Vector2((plane[0].Width * plane_resize_life * ResizeFactor.X + 5 * ResizeFactor.X) * Lives + 5, Font.MeasureString("|").Y + 10), spriteBatch, new Rectangle(), plane_resize_life, int.MaxValue, ResizeFactor));
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
            BonusTracker = new Bonus(spriteBatch, star, (int)((Font.MeasureString("|").Y) + star.Height * ResizeFactor.Y));

        }
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
