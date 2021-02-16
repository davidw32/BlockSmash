package Global;

/**
 * @author Patrick Pavlenko
 * @version 22.12.2019
 */
public class User {

    // Leben,Punktanzahl
    // Und Finger die erkannt werden
    private int points;
    private int life;
    private int fingers = 0;


    public User(int points,int life)
    {
        this.points = points;
        this.life = life;
    }

    public void loseLife()
    {
        life--;
    }

    public int getLife() {
        return life;
    }
    public int getFingers() { return fingers; }

    public void setFingers(int fingers) { this.fingers = fingers; }

    // Wird bei Gameover ausgefuehrt. in Game Klasse verwendet
    public void reset()
    {
        life = 3;
        fingers = 0;
    }
}
