import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.imageio.ImageIO;

public class Game implements ActionListener, MouseListener, KeyListener {
  
  public static Game game;
  public final int WIDTH = 800, HEIGHT = 600;
  public Renderer renderer;
  public Rectangle backg;
  public ArrayList<Rectangle> enemies;
  public int ammunition, shoots_fired, score, ammo_type;
  public Rectangle ufo;
  public int key, mouse;
  public boolean action, fire;
  public boolean gameover = false;
  public int enemy_y_n;
  public BufferedImage img = null;
  public BufferedImage img_enemy = null;
  public BufferedImage img_background = null;
  public FileReader fr = null;
  public BufferedReader br = null;
  public PrintWriter writer = null;
  public ArrayList<Integer> highscore;
  public int remaining_lives;
  public Timer timer;
  public int remaining_lives_x_offset = 0;
  public boolean is_traitor = false;

  // Border
  public ArrayList<Rectangle> border;
  public Random randgen = new Random();
  public int border_width_r = 100;
  public int border_width_l = 100;
  public int soll_r = (randgen.nextInt(100) - 50) + border_width_r;
  public int soll_l = (randgen.nextInt(100) - 50) + border_width_l;
  public int min_border_width = 25;
  public int max_border_width = 100;
  public int border_height = 3;

  // Boss
  public ArrayList<String> boss_files;
  public boolean still_last_boss;
  public int boss_number;
  public int boss_life = 100;
  public Rectangle Boss;
  public BufferedImage boss_image;
  public BufferedImage boss_shot;
  public int boss_shoot = 30;
  public ArrayList<Rectangle> boss_shoots;
  public int boss_tilt = 1;

  // Drops
  public ArrayList<Rectangle> ammo_crates;
  public ArrayList<Rectangle> medpacks;
  public ArrayList<Rectangle> upgrades;
  public BufferedImage ammo_crate_image;
  public BufferedImage medpack_image;
  public BufferedImage upgrade_image;

  // Ship Upgrades
  public int upgrade_number = 0;

  // NCC-1701
  public Rectangle phaser_ncc1701;
  public BufferedImage img_shots_enterprise;
  public BufferedImage img_ufo_enterprise = null;

  // Voyager
  public ArrayList<Rectangle> shoots;
  public BufferedImage img_shots_voyager;
  public BufferedImage img_ufo_voyager;
  public int firerate_voyager = 0;

  // Defiant
  public ArrayList<Rectangle> big_shots;
  public BufferedImage img_ufo_defiant;
  public int firerate = 0;
  public BufferedImage img_shots_defiant;

  // Game Start
  public boolean game_started = false;
  
  public Game() {
    JFrame jframe = new JFrame();
    timer = new Timer(15, this);
    renderer = new Renderer();
    //Mein Stuff
    // Ship Upgrades
    // Enterprise
    phaser_ncc1701 = new Rectangle(0,0,0,0);
    //Voyager
    shoots = new ArrayList<Rectangle>();
    //Defiant
    big_shots = new ArrayList<Rectangle>();

    backg = new Rectangle(0, 0, WIDTH, HEIGHT);
    border = new ArrayList<Rectangle>();
    border.add(new Rectangle(0, 0, border_width_r, border_height));
    border.add(new Rectangle(WIDTH - border_width_l, 0, border_width_l, border_height));
    shoots = new ArrayList<Rectangle>();
    enemies = new ArrayList<Rectangle>();
    remaining_lives = 20;
  
    //pressed = new HashSet<Character>();
    
    // Texturen
    try {
      // WALL
      img = ImageIO.read(new File("Bilder/sand_stone_texture_small.jpg"));
      // ENEMIES
      img_enemy = ImageIO.read(new File("Bilder/klingon_1.png"));
      // WEAPONS
      img_shots_enterprise = ImageIO.read(new File("Bilder/laser_2_1.png"));
      img_shots_voyager = ImageIO.read(new File("Bilder/laser_1.png"));
      img_shots_defiant = ImageIO.read(new File("Bilder/Defiant_shot.png"));
      // SHIPS / UFOS
      img_ufo_enterprise = ImageIO.read(new File("Bilder/enterprise-ncc-1701-a_small.png"));
      img_ufo_voyager = ImageIO.read(new File("Bilder/Intrepid_class_overhead_view.png"));
      img_ufo_defiant = ImageIO.read(new File("Bilder/Defiant_USS_Defiant09.png"));
      // BACKGROUND
      img_background = ImageIO.read(new File("Bilder/all_1_800x600.png"));
    } catch (IOException e) {}

    int ufo_height = 125;  
    int ufo_width = 62;
    ufo = new Rectangle(WIDTH / 2 - 25, HEIGHT - 175, ufo_width, ufo_height);

    // Ammunition
    ammunition = 500;
    shoots_fired = 0;
    score = 0;
    ammo_type = 0;

    // Highscore
    highscore = new ArrayList<Integer>();
    try {
      fr = new FileReader("highscore.txt");
      br = new BufferedReader(fr);
      String buffer = br.readLine();
      while (buffer != null) {
        highscore.add(Integer.parseInt(buffer));
        buffer = br.readLine();
        //System.out.println(highscore);
      }
      br.close();
      //writer = new PrintWriter("highscore.txt");
    } catch (IOException e) {
    } //catch (FileNotFoundException fnfe) {}
    //System.out.println(highscore);

    // BOSS
    boss_files = new ArrayList<String>();
    boss_files.add("Bilder/Borg_tactical_cube.png");
    boss_files.add("Bilder/borg_1.png");
    boss_number = randgen.nextInt(boss_files.size());
    try {
      boss_image = ImageIO.read(new File(boss_files.get(boss_number)));
      boss_shot = ImageIO.read(new File("Bilder/laser_2.png"));
    } catch (IOException IOEX) {}
    boss_shoots = new ArrayList<Rectangle>();

    // Drops
    ammo_crates = new ArrayList<Rectangle>();
    medpacks = new ArrayList<Rectangle>();
    upgrades = new ArrayList<Rectangle>();

    try {
      ammo_crate_image = ImageIO.read(new File("Bilder/ammo_symbol.png"));
      medpack_image = ImageIO.read(new File("Bilder/med_symbol_1.png"));
      upgrade_image = ImageIO.read(new File("Bilder/upgrade_Platzhalter.png"));
    } catch (IOException IOEXC) {}

    //highscore = new int[fr.];
    //System.out.println(highscore);
    
    jframe.add(renderer);
    jframe.addMouseListener(this);
    jframe.addKeyListener(this);
    
    jframe.setTitle("XENON");
    jframe.setSize(WIDTH, HEIGHT);
    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setResizable(false);    
    jframe.setVisible(true);
    
    timer.start();    
  }

  public int Solle(int n, int e) {
    //System.out.println("Soll wird ausgeführt");
    if (n > 0) {
      int i;
      do {
        i = (randgen.nextInt(100) - 50) + border_width_r;
      } while (i < n || i > e);
      return i;
    }
    else {
      return (randgen.nextInt(100) - 50) + border_width_r;
    }
  }

  /* Hier wird auf User-Interaktionen reagiert (ActionListener).
   * entsprechende Veränderungen durchführen (z.B. Positionswerte aktualisieren)
   * Dann neu zeichnen (-> renderer.repaint() steht schon da) und die Änderungen
   * werden umgesetzt.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (key == KeyEvent.VK_SPACE) {
      game_started = true;
    }
    if (gameover && key == KeyEvent.VK_SPACE) {
      //System.out.println("NEUES FENSTER");
      gameover = false;
      ufo.x = WIDTH / 2;
      ufo.y = HEIGHT - ufo.height;
      border.clear();
      shoots.clear();
      big_shots.clear();
      upgrade_number = 0;
      enemies.clear();
      ammunition = 600;
      remaining_lives = 20;
      score = 0;
      still_last_boss = false;
    }
    if (game_started) {
    //System.out.println("Upgrade Level: " + upgrade_number);
    // What Ship Hitbox
    if (upgrade_number == 0) {
      int ufo_height = 125;  
      int ufo_width = 62;
      ufo = new Rectangle(ufo.x, ufo.y, ufo_width, ufo_height);
    }
    else if (upgrade_number == 1) {
      int ufo_height = 125;  
      int ufo_width = 49;
      ufo = new Rectangle(ufo.x, ufo.y, ufo_width, ufo_height);
    }
    else if (upgrade_number == 2) {
      int ufo_height = 75;  
      int ufo_width = 60;
      ufo = new Rectangle(ufo.x, ufo.y, ufo_width, ufo_height);
    }
    //SHOOT
    if (fire && mouse == MouseEvent.BUTTON1 && ammunition > 0) {
      if (upgrade_number == 0) {
        phaser_ncc1701.width = 6;
        phaser_ncc1701.x = ufo.x + ufo.width / 2 - phaser_ncc1701.width / 2;
        if (!(phaser_ncc1701.y <= 0)) {
          phaser_ncc1701.y -= 30; phaser_ncc1701.height += 30;}
        if (phaser_ncc1701.y + phaser_ncc1701.height > ufo.y || phaser_ncc1701.y + phaser_ncc1701.height < ufo.y + 40) {
          phaser_ncc1701.height = -(phaser_ncc1701.y - ufo.y);
        }
        ammunition -= 1;
      }
      else if (upgrade_number == 1) {
        if (firerate_voyager == 2) {
          ammunition -= 1;
          shoots_fired += 1;
          shoots.add(new Rectangle(ufo.x + (ufo.width / 2), ufo.y, 5, 8));
          firerate_voyager = 0;
        }
        firerate_voyager++;
      }
      else if (upgrade_number == 2) {
        if (firerate == 3) {
          ammunition -= 1;
          big_shots.add(new Rectangle(ufo.x + (ufo.width / 2) - ufo.width / 3, ufo.y + 5, 10, 15));
        }
        else if (firerate == 6) {
          ammunition -= 1;
          big_shots.add(new Rectangle(ufo.x + (ufo.width / 2) + ufo.width / 3, ufo.y + 5, 10, 15));
          firerate = 0;
        }
        firerate++;
      }
    }
    else {
      if (upgrade_number == 0) {
        phaser_ncc1701.x = ufo.x + ufo.width / 2 - phaser_ncc1701.width / 2;
        phaser_ncc1701.y = ufo.y + 40;
        phaser_ncc1701.width = 0;
        phaser_ncc1701.height = 0;
      }
    }
    if (upgrade_number == 0) {
      for (int i = 0; i < enemies.size(); i++) {
        if (phaser_ncc1701.intersects(enemies.get(i))) {
          phaser_ncc1701.y = enemies.get(i).y + enemies.get(i).height;
          phaser_ncc1701.height = ufo.y - enemies.get(i).y;
          enemies.remove(i);
          
          score += 1; //score per kill
        }
      }
    }
    else if (upgrade_number == 1) {
      for (Rectangle shoot : shoots) {
        shoot.y -= 10;
        shoot.x += randgen.nextInt(6) - 3;  // --> shot spray
      }
      for (int i = 0; i < shoots.size(); i++) {
        if (shoots.get(i).y < 0) {
          shoots.remove(i);
        }
      }
      for (int i = 0; i < enemies.size(); i++) {
        for (int z = 0; z < shoots.size(); z++) {
          if (shoots.get(z).intersects(enemies.get(i))) {
              shoots.remove(z);
              enemies.remove(i);
              score += 1;  // SCORE PER KILL
              //ammunition += 20;
              break;
          }
        }
      }
    }
    else if (upgrade_number == 2) {
      for (Rectangle big_shot : big_shots) {
        big_shot.y -= 10;
      }
      for (int i = 0; i < big_shots.size(); i++) {
        if (big_shots.get(i).y < 0) {
          big_shots.remove(i);
        }
      }
        for (int i = 0; i < enemies.size(); i++) {
          for (int z = 0; z < big_shots.size(); z++) {
            if (big_shots.get(z).intersects(enemies.get(i))) {
                //big_shots.remove(z);
                enemies.remove(i);
                score += 1;  // SCORE PER KILL
                //ammunition += 20;
                break;
            }
          }
        }
    }

    
    //SHOOT - END

    //WAND MOVE
    for ( Rectangle rect : border) {
      rect.y = rect.y + rect.height;
    } // end of for
    //WAND MOVE END
    
    //SOLLWERT
    //Sollwert rechts - start
    if (border_width_r < soll_r) {
      border_width_r += 5;

      if (border_width_r >= soll_r) {
        soll_r = Solle(min_border_width, max_border_width);
      }
    }
    else if (border_width_r > soll_r) {
      border_width_r -= 5;
      if (border_width_r <= soll_r) {
        soll_r = Solle(min_border_width, max_border_width);
      }
    }
    else {
      soll_r = Solle(min_border_width, max_border_width);
    }
    border.add(new Rectangle(0, 0, border_width_r, border_height));
    //Sollwert rechts - end
    //Sollwert links - start
    if (border_width_l < soll_l) {
      border_width_l += 5;
      if (border_width_l >= soll_l) {
        soll_l = Solle(min_border_width, max_border_width);
      }
    }
    else if (border_width_l > soll_l) {
      border_width_l -= 5;
      if (border_width_l <= soll_l) {
        soll_l = Solle(min_border_width, max_border_width);
      }
    }
    else {
      soll_l = Solle(min_border_width, max_border_width);
    }
    border.add(new Rectangle(WIDTH - border_width_l, 0, border_width_l, border_height));
    // Sollwert links - end

    // Drops - Start
    int drops_y_n = randgen.nextInt(1500); // <-- drop ratio
    if (drops_y_n == 50 || drops_y_n == 150 || drops_y_n == 250 || drops_y_n == 350 || drops_y_n == 450) {
      ammo_crates.add(new Rectangle(randgen.nextInt(WIDTH - (max_border_width * 2)) + max_border_width, -20, 30, 30));
    }
    else if (drops_y_n == 100 || drops_y_n == 200 || drops_y_n == 300 || drops_y_n == 400 || drops_y_n == 500) {
      medpacks.add(new Rectangle(randgen.nextInt(WIDTH - (max_border_width * 2)) + max_border_width, -20, 30, 30));
    }
    else if (drops_y_n == 1400 && upgrade_number < 2) {
      upgrades.add(new Rectangle(randgen.nextInt(WIDTH - (max_border_width *2)) + max_border_width, -20, 30, 30));
    }
    for (Rectangle ammo_crate : ammo_crates) {
      ammo_crate.y += 5;
    }
    for (Rectangle medpack : medpacks) {
      medpack.y += 5;
    }
    for (Rectangle upgrade : upgrades) {
      upgrade.y += 5;
    }
    for (int i = 0; i < ammo_crates.size(); i++) {
      if (ammo_crates.get(i).y > HEIGHT) {
        ammo_crates.remove(i);
      }
      else if (ufo.intersects(ammo_crates.get(i))) {
        ammo_crates.remove(i);
        if (ammunition < 1000) { ammunition += 50; }
      }
    }
    for (int i = 0; i < medpacks.size(); i++) {
      if (medpacks.get(i).y > HEIGHT) {
        medpacks.remove(i);
      }
      else if (ufo.intersects(medpacks.get(i))) {
        medpacks.remove(i);
        if (remaining_lives < 20) {
          remaining_lives += 1;
        }
      }
    }
    for (int i = 0; i<upgrades.size(); i++) {
      if (upgrades.get(i).y > HEIGHT) {
        upgrades.remove(i);
      }
      else if (ufo.intersects(upgrades.get(i))) {
        upgrades.remove(i);
        if (upgrade_number < 2) { upgrade_number += 1; }
      }
    }
    // Drops - End

    //drop out of bounds rects
    /*for (int i = 0; i < wand.size(); i++) {
      if (wand.get(i).y > HEIGHT) {
        wand.remove(i);
      }
    }*/
    // New more efficient...theoretical
    if ((border.size() / 2 ) * border_height >= HEIGHT ) {
      border.remove(0);
      border.remove(0);
    }

    //ENEMYS - START
    enemy_y_n = randgen.nextInt(25);
    if (enemy_y_n == 1 && !still_last_boss) {
      int enemy_x = randgen.nextInt(HEIGHT - max_border_width) + max_border_width;
      enemies.add(new Rectangle(enemy_x, -50, 50, 75)); // ENEMY SIZE
      //System.out.println("enemy_x: " + enemy_x);
    }
    for (Rectangle enemy : enemies) {
      enemy.y += 5;
    }
    for (int i = 0; i < enemies.size(); i++) {
      if (enemies.get(i).y > HEIGHT) {
        enemies.remove(i);
      }
    }
    //ENEMIES - END
    
    // ENEMIES COLLAPSE - END
    // Game Over - start
    for (Rectangle rect : border) {
      if (ufo.intersects(rect)) {
        gameover = true;
      }
    }
    for (int i = 0; i < enemies.size(); i++) {
      if(ufo.intersects(enemies.get(i))) {
        if (remaining_lives == 1) {
          remaining_lives -= 1;
          enemies.remove(i);
          gameover = true;
        }
        else {
          remaining_lives -= 1; enemies.remove(i);}
      }
    }
    if (gameover) {
      highscore.add(score);
      Collections.sort(highscore, Collections.reverseOrder());
      try {
        writer = new PrintWriter("highscore.txt");
      } catch (IOException IOe) {}
      for (Integer highsc : highscore) {
        writer.println(highsc);
      }
      writer.close();
      game_started = false;
    }
    // Game Over - end
    // not game over so maybe a traitor - start
    if (ufo.y > HEIGHT + 35 || ufo.y < - 50) {
      is_traitor = true;
      //timer.stop();
      game_started = false;
    }
    // not game over so maybe a traitor - end

    // BOSS
    if (!still_last_boss && score % 50 == 0 && score > 0) {
      enemies.clear();
      boss_number = randgen.nextInt(boss_files.size());
      //System.out.println("boss_number: " + boss_number);
      try {
        boss_image = ImageIO.read(new File(boss_files.get(boss_number)));
        //System.out.println("Er tried es und macht es");
      } catch(IOException IOEX) {}
      boss_life = score;
      int boss_width = 200;
      int relationship_boss = boss_image.getWidth() / boss_image.getHeight();
      if (relationship_boss == 0) { relationship_boss = 1; }
      //System.out.println("relationship: " + relationship_boss);
      Boss = new Rectangle(WIDTH / 2 - boss_width / 2, 25, boss_width, boss_width * relationship_boss );
      //System.out.println("DER BOSS: " + boss_number + " : " + boss_files.get(boss_number) + " : " + Boss);
      still_last_boss = true;
      boss_number++;
    }
    if (score % 50 != 0) {
      still_last_boss = false;
    }
    if (still_last_boss) {
      if (upgrade_number == 0) {
        if (phaser_ncc1701.intersects(Boss)) {
          boss_life -= 1;
          phaser_ncc1701.y = Boss.y + Boss.height - 40;
          phaser_ncc1701.height = ufo.y - phaser_ncc1701.y;
        }
        if (boss_life <= 0) {
          still_last_boss = false;
          score += 20;
        }
      }
      else if (upgrade_number == 1) {
        for (int i = 0; i<shoots.size();i++) {
          if (shoots.get(i).intersects(Boss)) {
            boss_life -= 1;
            shoots.remove(i);
          }
          if (boss_life <= 0) {
            still_last_boss = false;
            score += 20;
            break;
          }
        }
      }
      else if (upgrade_number == 2) {
        for (int i = 0; i < big_shots.size(); i++) {
          if (big_shots.get(i).intersects(Boss)) {
            boss_life -= 5;
            big_shots.remove(i);
          }
          if (boss_life <= 0) {
            still_last_boss = false;
            score += 20;
            break;
          }
        }
      }
      
      if (boss_shoot == 1) {
        boss_shoots.add(new Rectangle(Boss.x + (Boss.width/2), Boss.y + (Boss.height - 10), 10,20));
        //System.out.println("ADDED BOSS SHOT");
        boss_shoot = 20;
      }
      else {
        boss_shoot -= 1;
      }
      for (int i = 0; i<boss_shoots.size(); i++) {
        if (ufo.intersects(boss_shoots.get(i))) {
          remaining_lives -= 1;
          boss_shoots.remove(i);
          if (remaining_lives <= 0) { gameover = true; }
        }
      }
      for (Rectangle shoot : boss_shoots) {
        shoot.y += 5;
        shoot.x += randgen.nextInt(6) - 3;
      }
      if (Boss.x <= max_border_width) { boss_tilt = 1; }
      else if (Boss.x >= WIDTH - max_border_width - Boss.width) { boss_tilt = 0; }
      if (boss_tilt == 0) {
        Boss.x -= 5;
      }
      else if (boss_tilt == 1) { //(boss_tilt == 1)
        Boss.x += 5;
      }
    }
    
    renderer.repaint();  // ruft indirekt die Methode repaint (s.u.) auf
  }
  }
  
  /* Hier werden alle Grafik-Elemente ausgegeben.
  * mit if's und boolean-Variablen ihr (Nicht)Vorhandensein steuern
  */  
  public void repaint(Graphics g) {
    //background
    g.setColor(new Color(200,200,200));
    g.drawImage(img_background, backg.x, backg.y, backg.width, backg.height, Color.RED, null);
    //g.fillRect(backg.x, backg.y, backg.width, backg.height);
    //background end
    g.setColor(Color.GRAY);
    for ( Rectangle rect : border) {
      //g.fillRect(rect.x, rect.y, rect.width, rect.height);
      g.drawImage(img, rect.x, rect.y, rect.width, rect.height, null);
    } // end of for
    g.setColor(Color.ORANGE);
    if ( upgrade_number == 0) {
      g.drawImage(img_shots_enterprise, phaser_ncc1701.x, phaser_ncc1701.y, phaser_ncc1701.width, phaser_ncc1701.height, Color.RED, null);
    }
    else if (upgrade_number == 1) {
      for (Rectangle shoot : shoots) {
        g.drawImage(img_shots_voyager, shoot.x, shoot.y, shoot.width, shoot.height, new Color(0,0,0,0), null);
      }
    }
    else if (upgrade_number == 2) {
      for (Rectangle big_shot : big_shots) {
        g.drawImage(img_shots_defiant, big_shot.x, big_shot.y, big_shot.width, big_shot.height, new Color(0,0,0,0), null);
      }
    }

    //UFO
    if (!action) {}
    else {
      if (key == KeyEvent.VK_W) {
        ufo.y -= 8;
        if (upgrade_number == 2) { ufo.y -= 4; } // Additional speed for Defiant
      }
      else if (key == KeyEvent.VK_S) {
        ufo.y += 6;
        if (upgrade_number == 2) { ufo.y += 4; } // Additional speed for Defiant
      }
      if (key == KeyEvent.VK_A) {
        ufo.x -= 7;
        if (upgrade_number == 2) { ufo.x -= 3; } // Additional speed for Defiant
      }
      else if (key == KeyEvent.VK_D) {
        ufo.x += 7;
        if (upgrade_number == 2) { ufo.x += 3; } // Additional speed for Defiant
      }
    }
      g.setColor(Color.RED);
      if (!gameover) {
        if (upgrade_number == 0) {
          g.drawImage(img_ufo_enterprise, ufo.x, ufo.y, ufo.width, ufo.height, new Color(0,0,0,0), null);
        }
        else if (upgrade_number == 1) {
          //g.drawImage(img_ufo_Voyager, );
          g.drawImage(img_ufo_voyager, ufo.x, ufo.y, ufo.width, ufo.height, new Color(0,0,0,0), null);
        }
        else if (upgrade_number == 2) {
          g.drawImage(img_ufo_defiant, ufo.x, ufo.y, ufo.width, ufo.height, new Color(0,0,0,0), null);
        }
        //g.fillOval(ufo.x, ufo.y, ufo.width, ufo.height);
      }
    //UFO End

    // Enemys - start
    g.setColor(Color.GREEN);
    for (Rectangle enemy : enemies) {
      g.drawImage(img_enemy, enemy.x, enemy.y, enemy.width, enemy.height, new Color(0,0,0,0), null);
      //g.drawImage(img_enemy, enemy.x, enemy.y, img_enemy.getWidth(), img_enemy.getHeight(), Color.BLACK, null);
      //g.fillOval(enemy.x, enemy.y, enemy.width, enemy.height);
    }
    // Enemys - end

    // Drops - Start
    g.setColor(Color.BLUE);
    for (Rectangle ammo : ammo_crates) {
      //g.fillRect(ammo.x, ammo.y, ammo.width, ammo.height);
      g.drawImage(ammo_crate_image, ammo.x, ammo.y, ammo.width, ammo.height, new Color(0,0,0,0), null);
    }
    g.setColor(Color.RED);
    for (Rectangle med : medpacks) {
      //g.fillRect(med.x, med.y, med.width, med.height);
      g.drawImage(medpack_image, med.x, med.y, med.width, med.height, new Color(0,0,0,0), null);
    }
    for (Rectangle upgrade : upgrades) {
      //g.fillRect(ammo.x, ammo.y, ammo.width, ammo.height);
      g.drawImage(upgrade_image, upgrade.x, upgrade.y, upgrade.width, upgrade.height, new Color(0,0,0,0), null);
    }
    // Drops - End
    
    //BOSS
    if (still_last_boss) {
      g.drawImage(boss_image, Boss.x, Boss.y, Boss.width, Boss.height, new Color(0,0,0,0), null);
      g.setColor(Color.YELLOW);
      for (Rectangle shot : boss_shoots) {
        //g.fillRect(shot.x, shot.y, shot.width, shot.height);
        g.drawImage(boss_shot, shot.x, shot.y, shot.width, shot.height, new Color(0,0,0,0), null);
      }
      g.setColor(Color.RED);
      g.setFont(new Font("Impact", 1, 20));
      g.drawString(boss_life + "/" + score, Boss.x, Boss.y + 30);
    }
    //BOSS END
    // game over / traitor start
    if (gameover) {
      g.setColor(Color.DARK_GRAY);
      g.fillRect(backg.x, backg.y, backg.width, backg.height);
      g.setColor(Color.MAGENTA);
      g.setFont(new Font("Arial", 1, 100));
      g.drawString("Game Over", 100, HEIGHT / 2 - 50);
      g.setFont(new Font("Arial", 1, 25));
      try {
        g.drawString("Place 1: " + highscore.get(0), 110, HEIGHT / 2 + 0);
      } catch (Exception e) {
        g.drawString("Place 1: " + score, 110, HEIGHT / 2 + 0);
      }
      try {
        g.drawString("Place 2: " + highscore.get(1), 110, HEIGHT / 2 + 50);
      } catch (Exception e) {
        g.drawString("Place 2: 0", 110, HEIGHT / 2 + 50);
      }
      try {
        g.drawString("Place 3: " + highscore.get(2), 110, HEIGHT / 2 + 100);
      } catch (Exception e) {
        g.drawString("Place 3: 0", 110, HEIGHT / 2 + 100);
      }
      
      g.drawString("Your Score: " + score, 110, HEIGHT / 2 + 150);
      g.drawString("Press SPACE to restart.", WIDTH / 2, HEIGHT / 2 + 45);
    }
    // game over / traitor end
    g.setFont(new Font("Impact", 1, 25));
    if (!gameover && game_started) {
      g.setColor(Color.GREEN);
      g.setFont(new Font("Impact", 1, 25));
      g.drawString("Munition: " + ammunition, 1, 25);
      //g.drawString("Shoots fired: " + shoots_fired, 1, HEIGHT - 40);
      g.drawString("Score: " + score, WIDTH - 150, 25);
      //g.setColor(Color.RED);
      for (int i = 0; i < remaining_lives; i++) {
        //System.out.println("my lifes: " + my_lifes);
        if (remaining_lives == 5 || remaining_lives == 4) {
          g.setColor(Color.ORANGE);
        }
        else if (remaining_lives <= 3) {
          g.setColor(Color.RED);
        }
        g.fillRect(10 + remaining_lives_x_offset, HEIGHT - 60, 10, 20);
        remaining_lives_x_offset += 15;
      }
      remaining_lives_x_offset = 0;
    }
    if (is_traitor) {
    
      g.setColor(Color.DARK_GRAY);
      g.fillRect(backg.x, backg.y, backg.width, backg.height);
      g.setColor(Color.MAGENTA);
      g.setFont(new Font("Arial", 1, 30));
      g.drawString("You died and cannot restart, traitor!", 60, HEIGHT / 2 + 10);

    }

    if (!game_started && !gameover && !is_traitor) {
      g.setColor(Color.RED);
      g.drawString("Press Space to start!", WIDTH / 2 - 170, HEIGHT/ 2);
      g.drawString("The boldly go where no man has gone before!", WIDTH / 2 - 320, HEIGHT / 2 + 25);
    }
    //last_transmission += 1;
    //System.out.println("Last transmission by repaint: " + last_transmission);
    //g.drawImage(img, 0, 0, null);

    // Different Boxes
    /*g.setColor(new Color(0,0,0,100));
    g.fillRect(ufo.x, ufo.y, ufo.width, ufo.height);
    for (Rectangle enemy : enemys) {
      g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
    }
    if (still_last_boss) {
      g.fillRect(Boss.x, Boss.y, Boss.width, Boss.height);
    }
    
    g.setColor(Color.BLACK);
    g.drawLine(100, 0, 100, HEIGHT);
    g.drawLine(200, 0, 200, HEIGHT);
    g.drawLine(WIDTH - 100, 0, WIDTH -100, HEIGHT);
    g.drawLine(WIDTH - 200, 0, WIDTH - 200, HEIGHT);
    //g.drawImage(Test_Boss, 50, 50, 200, 200, new Color(0,0,0,0), null);*/
  }
  /************************************************************
  * Mouse Event Methoden: 
  * müssen vorhanden sein, aber nicht alle implementiert werden
  *************************************************************/
  @Override
  public void mouseClicked(MouseEvent e) {
  }
  
  @Override
  public void mousePressed(MouseEvent e) {
    mouse = e.getButton();
    fire = true;
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    fire = false;
  }
  
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  
  @Override
  public void mouseExited(MouseEvent e) {
  }
  
  /************************************************************
  * Keyboard Event Methoden: 
  * müssen vorhanden sein, aber nicht alle implementiert werden
  *************************************************************/
  @Override
  public void keyTyped(KeyEvent e) {
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    key = e.getKeyCode();
    //System.out.println(e);
    action = true;
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    action = false;
  }
  
  /************************************************************
  * Hauptprogramm: erzeugt das Fenster
  * Hier muss ggf. der Name des Klasse (Game) geändert werden.
  *************************************************************/
  public static void main(String[] args) {
    game = new Game();
  }
}
