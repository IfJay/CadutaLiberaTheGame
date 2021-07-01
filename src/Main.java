import javax.swing.*;

public class Main{

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MenuPage();     // mi richiamo il costruttore della classe MenuPage (crea la finestra di menu)

    }
}
