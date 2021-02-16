package Global;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @author Patrick Pavlenko
 * @version 22.12.2019
 * Haupt JFrame des ganzen Programmes
 */
public class JWindow extends JFrame {


    private JButton buttonStart;
    private JButton buttontest;
    private JButton buttonquit;
    private JButton buttonbackTutor;
    private JButton buttonbackGame;
    private JButton buttonrestart;

    /**
     * Grundeinstellung des Fensters werden erstellt ( Hoehe,Breite,Koordinaten des Fensters,Titel)
     * Alle Buttons werden konfiguriert
     * @param x Breite Fenster
     * @param y Höhe Fenster
     * @param title Titel des Fensters für das Programm
     */
    public JWindow(int x,int y,String title)
    {
        setSize(x,y);
        setLocation(0,0);
        setTitle(title);

        // Buttons werden ihrer Position,Text,Font und Farbe gegeben und dem JFrame zugewiesen
        buttonStart = new JButton();
        buttonStart.setBounds(720,580,160,48);
        buttonStart.setText("Play Game");
        this.add(buttonStart);
        this.setResizable(false);
        buttonStart.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,16));
        buttonStart.setForeground(Color.white);
        buttonStart.setBackground(Color.BLACK);

        buttontest = new JButton();
        buttontest.setBounds(720,640,160,48);
        buttontest.setText("Tutorial");
        this.add(buttontest);
        buttontest.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,16));
        buttontest.setForeground(Color.white);
        buttontest.setBackground(Color.BLACK);

        buttonquit = new JButton();
        buttonquit.setBounds(720, 700, 160, 48);
        buttonquit.setText("Quit Game");
        this.add(buttonquit);
        buttonquit.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,16));
        buttonquit.setForeground(Color.white);
        buttonquit.setBackground(Color.BLACK);

        buttonbackTutor = new JButton();
        buttonbackTutor.setBounds(1130, 770, 160, 48);
        buttonbackTutor.setText("Back");
        this.add(buttonbackTutor);
        buttonbackTutor.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,16));
        buttonbackTutor.setForeground(Color.white);
        buttonbackTutor.setBackground(Color.BLACK);
        buttonbackTutor.setVisible(false);

        buttonbackGame = new JButton();
        buttonbackGame.setBounds(630, 770, 360, 48);
        buttonbackGame.setText("Zurück zum Hauptmenü");
        this.add(buttonbackGame);
        buttonbackGame.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,24));
        buttonbackGame.setForeground(Color.white);
        buttonbackGame.setBackground(Color.BLACK);
        buttonbackGame.setVisible(false);

        buttonrestart = new JButton();
        buttonrestart.setBounds(630, 670, 360, 48);
        buttonrestart.setText("Neustarten");
        this.add(buttonrestart);
        buttonrestart.setFont(new Font("Helvetica Neue", Font.CENTER_BASELINE,24));
        buttonrestart.setForeground(Color.white);
        buttonrestart.setBackground(Color.BLACK);
        buttonrestart.setVisible(false);
    }

    public JButton getButtonStart() { return buttonStart; }
    public JButton getButtontest() { return buttontest; }
    public JButton getButtonquit() {return buttonquit; }
    public JButton getButtonbackTutor() { return buttonbackTutor; }
    public JButton getButtonbackGame() { return buttonbackGame; }
    public JButton getButtonrestart() { return buttonrestart; }

    /**
     * Startmenue Buttons erscheinen
     */
    public void buttonAppear()
    {
        buttonStart.setVisible(true);
        buttontest.setVisible(true);
        buttonquit.setVisible(true);
    }

    /**
     * Startmenu Buttons verschwinden
     */
    public void buttonDisappear()
    {
        buttonStart.setVisible(false);
        buttontest.setVisible(false);
        buttonquit.setVisible(false);
    }

    public void tutorialButtonAppear() { buttonbackTutor.setVisible(true); }

    public void tutorialButtonDisappear() { buttonbackTutor.setVisible(false); }

    /**
     * Buttons des Gameoverscreen erscheinen
     */
    public void gameoverButtonAppear()
    {
        buttonbackGame.setVisible(true);
        buttonrestart.setVisible(true);
    }

    /**
     * Buttons des Gameoverscreen verschwinden
     */
    public void gameoverButtonDisappear()
    {
        buttonbackGame.setVisible(false);
        buttonrestart.setVisible(false);
    }


}
