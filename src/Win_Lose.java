import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Win_Lose extends JFrame implements ActionListener {

    //--------------------------- FIELDS ------------------------------//

    private JPanel fine = new JPanel();                 // pannello per gerry frase e risposte non date
    private JPanel action = new JPanel();               // pannello per il bottone

    private GridBagConstraints gbc_lab;                 // costannti per risposte non date
    private GridBagConstraints gbc_tit;                 // costanti per il titolo

    private JLabel risultato = new JLabel();            // label per la frase nagativa o positiva di gerry
    private JLabel thx = new JLabel();                  // label per i ringraziamenti
    private JLabel correzioni[] = new JLabel[11];       // lebels per le risposte non date

    private JButton skip_exit = new JButton();          // bottone per uscire

    private ImageIcon frame_icon = new ImageIcon(new ImageIcon("imgs//logo.png").getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT));       // logo finestra
    private ImageIcon img_vinto = new ImageIcon(new ImageIcon("imgs//gerry_win.png").getImage().getScaledInstance(250,150,Image.SCALE_DEFAULT));    // img gerry in caso di vincita
    private ImageIcon img_perso = new ImageIcon(new ImageIcon("imgs//gerry_lose.png").getImage().getScaledInstance(300,180,Image.SCALE_DEFAULT));   // img di gerry in caso di pertita

    private final Color purple = new Color(27, 11, 61);     //colore
    private final Color yellow = new Color( 252, 238, 33);  //colore

    private Random rnd = new Random();                  // generatore random

    int DOMANDE_LEN;                                    // variabile per salvare la lunghezza dell'array

    boolean can_exit = false;                           // se posso uscire dal programma o meno

    Win_Lose(boolean win,boolean out_of_time,int giuste,String[] DOMANDE,String[] RISPOSTE,String[] FRASI_WIN,String[] FRASI_LOSE) {

        DOMANDE_LEN = DOMANDE.length-giuste;                                    // salvo la lunghezza dell array

        //-------------------------- JFRAME --------------------------//

        setTitle("CADUTA LIBERA the GAME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                         // chiudere la JVM correttamente
        setSize(new Dimension(700,400));                           // dimensione della JFrame (finestra)
        setResizable(false);                                                    // non si può ridimensionare
        setLocationRelativeTo(null);                                            // posizione di creazione finestra al centro dello schermo
        setIconImage(frame_icon.getImage());                                    // imposto il logo del JFrame
        setLayout(new BorderLayout());                                          // imposto un tipo di layout (rispetto ai bordi)

        //-------------------------- TOP --------------------------//

        gbc_lab = new GridBagConstraints();                                     // Valori costanti che applico alle labels (risposte non date)
        gbc_lab.gridwidth = GridBagConstraints.REMAINDER;
        gbc_lab.fill = GridBagConstraints.HORIZONTAL;
        gbc_lab.insets = new Insets(5,0,5,0);

        gbc_tit = new GridBagConstraints();                                     // Valori costanti che applico solo al titolo
        gbc_tit.gridwidth = GridBagConstraints.REMAINDER;
        gbc_tit.fill = GridBagConstraints.HORIZONTAL;
        gbc_tit.insets = new Insets(5,0,15,0);

        fine.setBackground(purple);                                             // edito il penallo
        fine.setLayout(new BorderLayout());
        add(fine);

        risultato.setForeground(yellow);                                        // formatto la frase di gerry
        risultato.setFont(new Font("Consolas",Font.PLAIN,25));

        if (win) {                                                              // se ho vinto faccio dire a gerry una felle frasi e quando clicco il bottone posso direttamente uscire
            risultato.setText(FRASI_WIN[rnd.nextInt(FRASI_WIN.length)]);
            risultato.setIcon(img_vinto);
            can_exit = true;
        }else if (out_of_time) {                                                // se ho perso faccio dire a gerry una delle sue frasi e quando clilicco il bottone non esco direttamente la vado nella sezione domande non date
            risultato.setText(FRASI_LOSE[rnd.nextInt(FRASI_LOSE.length)]);
            risultato.setIcon(img_perso);
        }

        risultato.setHorizontalTextPosition(JLabel.CENTER);                     // allineo il testo rispetto all'icona e al pannello
        risultato.setVerticalTextPosition(JLabel.BOTTOM);
        risultato.setHorizontalAlignment(JLabel.CENTER);
        risultato.setVerticalAlignment(JLabel.CENTER);
        risultato.setIconTextGap(40);                                           // inserisco uno spazio tra icona e testo
        fine.add(risultato);

        thx.setText("GRAZIE PER AVER GIOCATO");                                 // formatto il testo di ringraziamento
        thx.setForeground(yellow);
        thx.setFont(new Font("Consolas",Font.PLAIN,15));
        thx.setHorizontalAlignment(JLabel.CENTER);
        thx.setVerticalAlignment(JLabel.CENTER);
        fine.add(thx,BorderLayout.SOUTH);

        if (out_of_time) {
            for (int i = 0;i<DOMANDE.length;i++) {                                  // inizializzo e formatto le label che conterrano le risposte non date se ho perso
                correzioni[i] = new JLabel();
                correzioni[i].setText(DOMANDE[i]+" : "+RISPOSTE[i]);
                correzioni[i].setForeground(yellow);
                correzioni[i].setHorizontalAlignment(JLabel.CENTER);
                correzioni[i].setFont(new Font("Consolas",Font.PLAIN,14));
            }
            correzioni[correzioni.length-1] = new JLabel();                         // nell' ultima label ci metto un titolo "RISPOSTE NON DATE" per maggiore chiarezza e lo formatto
            correzioni[correzioni.length-1].setText("RISPOSTE NON DATE");
            correzioni[correzioni.length-1].setForeground(yellow);
            correzioni[correzioni.length-1].setHorizontalAlignment(JLabel.CENTER);
            correzioni[correzioni.length-1].setFont(new Font("Consolas",Font.BOLD,20));
        }

        //-------------------------- BOTTOM --------------------------//

        action.setBackground(purple);                                           // edito il pannello per il bottone
        action.setLayout(new GridBagLayout());
        action.setPreferredSize(new Dimension(0,60));
        add(action,BorderLayout.SOUTH);

        if (win) {                                                              // se ho vinto posso su di esso scrivo "esci" se no "AVANTI"
            skip_exit.setText("ESCI");
        } else {
            skip_exit.setText("AVANTI");
        }

        skip_exit.setBackground(purple);
        skip_exit.setForeground(yellow);
        skip_exit.setFocusable(false);
        skip_exit.addActionListener(this);
        action.add(skip_exit);

        setVisible(true);                                                       // Rendo visibile la finestra in se

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (can_exit) {                                                         // se ho vinto e posso uscire chiudo il programma
            System.exit(0);
        }

        if (actionEvent.getSource().equals(skip_exit)) {
            can_exit = true;                                                    // posso uscire

            skip_exit.setText("ESCI");
            risultato.setVisible(false);                                        // rendo invisibili la frase di gerry e anche quella di ringraziamento
            thx.setVisible(false);

            fine.setLayout(new GridBagLayout());                                // imposto un nuovo altro layout
            setSize(new Dimension(700,450));                        // allungo la finestra

            fine.add(correzioni[correzioni.length-1],gbc_tit);                  // aggiungo al pannello il titolo
            for (int i = 0;i<DOMANDE_LEN;i++) {                                 // aggiungo al pannello tutte le label delle domande non date
                fine.add(correzioni[i],gbc_lab);
            }
        }
    }
}
