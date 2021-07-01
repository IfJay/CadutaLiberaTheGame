import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class MenuPage extends JFrame implements ActionListener {            // classe che si comporta da JFrame e quindi eredita tutti i metodi della classe JFrame inotre "avviso" la classe che esite un interfaccia ovvero un insime di metodi astratti (metodi ancora da implementare, non hanno codice all' interno e non sanno cosa fare)

    //--------------------------- FIELDS ------------------------------//

    private JPanel header = new JPanel();           // per il titolo
    private JPanel footer = new JPanel();           // per il footer con copyright;
    private JPanel center = new JPanel();           // il centro della finestra
    private JButton play = new JButton();           // bottone per giocare
    private JButton regole = new JButton();         // bottone per le opzioni
    private JButton quit = new JButton();           // bottone per chiudere l'app
    private JLabel titolo = new JLabel();           // titolo del gioco
    private JLabel subtitolo = new JLabel();        // presentato da:
    private JLabel copyright = new JLabel();        // copyright da mettere nel footer

    private ImageIcon frame_icon = new ImageIcon(new ImageIcon("imgs//logo.png").getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT));       // icona JFrame

    private final Color purple = new Color(27, 11, 61);           // colore
    private final Color yellow = new Color( 252, 238, 33);        // colore

    MenuPage(){
        setTitle("CADUTA LIBERA the GAME");                                     // titolo della finesra
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                         // chiudere la JVM correttamente
        setSize(new Dimension(500,300));                            // dimensione della JFrame (finestra)
        setResizable(false);                                                    // non si può ridimensionare
        setLocationRelativeTo(null);                                            // posizione di creazione finestra al centro dello schermo
        setIconImage(frame_icon.getImage());                                    // setto una il logo della finestra
        setLayout(new BorderLayout());                                          // imposto un tipo di layout (rispetto ai bordi)

        //--------------------------- HEADER -------------------------//

        header.setBackground(purple);                                           // setto il colore dell panello header
        header.setPreferredSize(new Dimension(0,80));               // setto un dimensione predefinita
        header.setLayout(new GridBagLayout());                                  // setto un nuovo layout
        add(header,BorderLayout.NORTH);                                         // aggiungo l'header al mio frame nella posizione nord (in alto)

        GridBagConstraints gbc_buttons = new GridBagConstraints();              // Valori costanti per il gridbaglayout per i bottoni regole e quit
        gbc_buttons.gridwidth = GridBagConstraints.REMAINDER;                   // ogni componente è risiede nell' ultima cella quindi si alliniano uno sotto l'altro
        gbc_buttons.fill = GridBagConstraints.HORIZONTAL;                       // ogni componente si estende per la lunghezza orizzontale della cella
        gbc_buttons.insets = new Insets(10,0,0,0);         // aggiungo del padding tra gli elementi

        GridBagConstraints gbc_null = new GridBagConstraints();                 // Valori costanti per il gridbaglayout che applico solo al bottone "play"
        gbc_null.gridwidth = GridBagConstraints.REMAINDER;
        gbc_null.fill = GridBagConstraints.HORIZONTAL;

        titolo.setText("CADUTA LIBERA");                                        // setto il testo della label
        titolo.setForeground(yellow);                                           // cambio il colore del testo
        titolo.setFont(new Font("Consolas",Font.BOLD,40));            // cambio il font, modifico la sua proprioetà in BOLD e lo rendo più grande
        header.add(titolo,gbc_null);                                            //  aggiungo la label al header con rispettando le costanti dell layout

        subtitolo.setText("by Gerry Scotty");                                   // setto il testo della label
        subtitolo.setForeground(yellow);                                        // cambio il colore del testo
        subtitolo.setHorizontalAlignment(JLabel.CENTER);                        // la allineo orizzontalmente al centro
        header.add(subtitolo,gbc_null);                                         // aggiungo la label al header rispettando le costanti dell layout

        //--------------------------- CENTER -------------------------//

        center.setBackground(purple);                                           // setto il colore dell panello header
        center.setLayout(new GridBagLayout());                                  // setto un nuovo layout
        add(center,BorderLayout.CENTER);                                        // aggiungo il pannello al mio frame nella posione centrale

        play.setText("GIOCA");                                                  // setto il testo del bottone
        play.setBackground(purple);                                             // cambio il colore del background
        play.setForeground(yellow);                                             // cambio il colore del testo
        play.setFocusable(false);                                               // impedisco che si evidenzi quando ci clicco
        play.addActionListener(this);                                        // gli aggiungo un actionlistener (così è sempre in ascolto di qualche input) (uno "THIS" perche faccio riferimento all'actionlistener implementato precedentemente in questa classe)
        center.add(play,gbc_null);                                              // aggiungo il bottone al header rispettando le costanti dell layout

        regole.setText("REGOLE");                                               // setto il testo del bottone
        regole.setBackground(purple);                                           // cambio il colore del background
        regole.setForeground(yellow);                                           // cambio il colore del testo
        regole.setFocusable(false);                                             // impedisco che si evidenzi quando ci clicco
        regole.addActionListener(this);                                      // gli aggiungo un actionlistener (così è sempre in ascolto di qualche input)
        center.add(regole,gbc_buttons);                                         // aggiungo il bottone al header rispettando le costanti dell layout

        quit.setText("ESCI");                                                   // setto il testo del bottone
        quit.setBackground(purple);                                             // cambio il colore del background
        quit.setForeground(yellow);                                             // cambio il colore del testo
        quit.setFocusable(false);                                               // impedisco che si evidenzi quando ci clicco
        quit.addActionListener(this);                                         // gli aggiungo un actionlistener (così è sempre in ascolto di qualche input)
        center.add(quit,gbc_buttons);                                           // aggiungo il bottone al header rispettando le costanti dell layout

        //--------------------------- FOOTER -------------------------//

        footer.setBackground(purple);                                           // setto il colore dell panello header
        footer.setPreferredSize(new Dimension(0,40));               // setto un dimensione predefinita
        footer.setLayout(new GridLayout());                                     // setto un nuovo layout
        add(footer,BorderLayout.SOUTH);                                         // aggiungo l'header al mio frame nella posizione sud (in basso)

        copyright.setText("Copyright Jay 2020-2025");                           // setto il testo del bottone
        copyright.setForeground(yellow);                                        // cambio il colore del testo
        copyright.setFont(new Font("Consolas",Font.PLAIN,15));        // cambio il font,e lo rendo più grande
        copyright.setHorizontalAlignment(JLabel.CENTER);                        // la allineo orizzontalmente al centro
        footer.add(copyright);                                                  // aggiungo la label al footer

        //-------------------------------------------------------------//

        setVisible(true);                                                       // rendo visibile il mio frame

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {                      // sovrascrivo e richiamo il metodo che viene richiamato ogni volta che viene eseguita una azione (click di un bottone)
        if (actionEvent.getSource() == play) {                                  // se l'azione proviene dal bottone play
            setVisible(false);                                                  // rendo invisibile la finestra
            try {
                new Game();                                                     // chiamo il costruttore della classe Game (che mi crea la finestra di gioco)
            } catch (IOException e) {                                           // se non ci riesco (errore di lettura file)
                e.printStackTrace();                                            // stampo errore
                System.exit(0);                                           // chiudo il programma
            }
        }
        if (actionEvent.getSource() == regole) {                                // se l'azione proviene dal bottone regole
            JOptionPane.showMessageDialog(null,                   // creao una finestra di messaggio

                    "Questo giochino e' la replica dei famosi '10 PASSI' de 'Caduta Libera'\n\n" +               // messaggio
                            "Dieci quesiti a cui l'utente e' chiamato a rispondere correttamente in soli TRE minuti.\n" +
                            "Se l'utente non ha la rispota pronta oppure ci vuole ripensare in seguito può skippare la domanda per non perdere tempo.\n\n" +
                            "N.B. La domanda skippata sara' chiesta nel seguente giro di domande",

                    "REGOLAMENTO",                                          // titolo del mesbox
                    JOptionPane.PLAIN_MESSAGE                                    // tipo del msg box
            );
        }
        if (actionEvent.getSource() == quit) {                                  // se l'azione proviene dal bottone quit
            System.exit(0);                                               // chiudo il programma
        }
    }
}
