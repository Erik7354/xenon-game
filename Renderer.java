import java.awt.Graphics;
import javax.swing.JPanel;

public class Renderer extends JPanel {
  private static final long serialVersionUID = 1L;
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Game.game.repaint(g);
  }
  
  /* Der "Renderer" ist eigentlich nur ein JPanel, also eine Fläche (Panel) auf
  * der man dann grafische Elemente plazieren kann. Die Grafik-Elemente stellt
  * die Klasse Graphics zur Verfügung. Instanzen von Graphics werden immer mit 
  * der Variable g bezeichnet. Dann kann man mit g.methodenName(Parameter) die 
  * Grafik-Methoden aufrufen.
  * Bei User-Aktionen und wenn der Timer tickt werden repaints (Neuzeichnen)
  * erforderlich. Das führt zum Aufruf der Methode paintComponent. Diese zeichnet
  * das Panel (Zeile 9) und ruft dann die Methode repaint in Game auf (Zeile 10).
  * Sieht umständlich aus, führt aber dazu, dass man einfach in Game arbeiten 
  * kann und in Renderer nichts groß verändert werden muss. 
  * Die einzige Anderung betrifft Zeile 10 -> Klasse Game umbenennen (empfohlen).
  * Die Umbennenung muss in beiden Klassen konsistent sein (Suchen und Ersetzen).
  * Die Variable game kann man so lassen.
  */
}