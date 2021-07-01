import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

public class Game extends JFrame implements ActionListener {

    //--------------------------- FIELDS ------------------------------//

    private JPanel header = new JPanel();                           // per il titolo
    private JPanel footer = new JPanel();                           // per il footer con copyright;
    private JPanel center = new JPanel();                           // il centro della finestra
    private JPanel clock = new JPanel();                            // sapzio per il timer
    private JPanel ask = new JPanel();                             // spazio per domande e suggerimenti
    private JPanel hint = new JPanel();                             // spazio per i soli suggerimenti
    private JPanel input = new JPanel();                            // spazio per l' imput utente (textbox e buttons)
    private JPanel steps = new JPanel();                            // spazio per mostrare i "passi"

    private JLabel time = new JLabel();                             // label per mostrare il tempo rimanente
    private JLabel domande =  new JLabel();                         // spazio per domande
    private JLabel hints = new JLabel();                            // per mostrare alcule lettere della risposta
    private JLabel titolo = new JLabel();                           // titolo del gioco
    private JLabel subtitolo = new JLabel();                        // titolo del gioco
    private JLabel copyright = new JLabel();                        // copyright da mettere nel footer

    private JButton passo = new JButton();                          // bottone per passare la domanda
    private JButton enter = new JButton();                          // bottone per dare la risposta a gerry
    private JButton[] bts_true_false = new JButton[10];             // bottoni che usso per rappresentare graficamente i passi

    private JTextField write_risposta = new JTextField();           // box per la scrittura di testo (risposta)

    private Timer timer = new Timer();                              // timer per tenere traccia del tempo passato
    private javax.swing.Timer wait_next;                            // timer swing per delay
    private Random rnd = new Random();                              // generatore random

    private ImageIcon frame_icon = new ImageIcon(new ImageIcon("imgs//logo.png").getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT));       // icona della finestra
    private ImageIcon img_domande[] = new ImageIcon[2];                                                                                                                 // immagini per le domande gi gerry
    private ImageIcon img_esatto = new ImageIcon(new ImageIcon("imgs//gerry_true.png").getImage().getScaledInstance(280,180,Image.SCALE_DEFAULT));  // immagine in caso di risposta esatta
    private ImageIcon img_errore = new ImageIcon(new ImageIcon("imgs//gerry_false.png").getImage().getScaledInstance(280,190,Image.SCALE_DEFAULT)); // immagine in caso dei risposta errata

    private final Color purple = new Color(27, 11, 61);     // colore
    private final Color yellow = new Color( 252, 238, 33);  //colore

    private String[] DOMANDE = new String[10];                       // array per salvare le domande prese da txt
    private String[] RISPOSTE = new String[10];                      // array per salvare le risposte prese da txt
    private String[] FRASI_YES = new String[10];                    // array per salvare le frasi in caso di risposta esatta prese da txt
    private String[] FRASI_NO = new String[10];                      // array per salvare le frase in caso di risposta errata prese da txt
    private String[] HIDDEN = new String[10];                       // array per salvare i suggerimanti risposte
    private String[] FRASI_WIN = new String[5];                     // array per salvare le frasi in caso di vincita prese da txt
    private String[] FRASI_LOSE = new String[5];                    // array per salvare le frasi in caso di perdita prese da txt

    private final int PASSI = 10;                                   // ovvero le risposte giuste da dare
    private int INDEX = 0;                                          // inddex per lavorrare con la posizione degli array
    private int CORRECT_COUNTER = 0;                                // contatore delle risposte giuste
    private int N_SUGGERIMENTI = 0;                                 // numero di suggerimenti da dare in base alla lunghezza della risposta
    private int LUNGHEZZA_ARRAY = DOMANDE.length;                   // lunghezza array

    boolean win = false;                                            // decretare se si vinve
    boolean out_of_time = false;                                    // decretare se il tempo è finito

    Game () throws IOException {

        //----------------------- LEGGO IL FILE -----------------------//

        FileReader reader = new FileReader("txt//text.txt");       // creo il FileReader che va a prendermi il file desiderato
        BufferedReader read = new BufferedReader(reader);                   //creo il BufferReader che ulizzo per prendere informazioni sul file, in questo caso mi serve il suo contenuto

        for (int i = 0 ;i<10;i++) {                                          // Righe per le domande
            DOMANDE[i] = read.readLine();
            //System.out.println(DOMANDE[i]);
        }
        for (int i =0;i<10;i++) {                                           // Righe per le risposte
            RISPOSTE[i] = read.readLine();
            //System.out.println(RISPOSTE[i]);
        }
        for (int i =0;i<10;i++) {                                           // Righe per le frasi positi  pack(); di gerry
            FRASI_YES[i] = read.readLine();
            //System.out.println(FRASI_YES[i]);
        }
        for (int i =0;i<10;i++) {                                           // Righe per le frasi negative di gerry
            FRASI_NO[i] = read.readLine();
            //System.out.println(FRASI_NO[i]);
        }
        for (int i =0;i<5;i++) {                                           // Righe per le frasi in caso di vincita
            FRASI_WIN[i] = read.readLine();
            //System.out.println(FRASI_WIN[i]);
        }
        for (int i =0;i<5;i++) {                                           // Righe per le frasi in caso di perdita
            FRASI_LOSE[i] = read.readLine();
            //System.out.println(FRASI_LOSE[i]);
        }

        //------------------ CALCOLI + ASSEGNAMENTI -------------------//

        img_domande[0] = new ImageIcon(new ImageIcon("imgs//gerry_dom.png").getImage().getScaledInstance(300,190,Image.SCALE_DEFAULT));     // assesegno le immagini all' array
        img_domande[1] = new ImageIcon(new ImageIcon("imgs//gerry_dom1.png").getImage().getScaledInstance(240,190,Image.SCALE_DEFAULT));

        for (int i = 0 ;i<RISPOSTE.length;i++) {                            // trasformo tutte le risposte in usa string di '_' della stessa lunghezza e la salvo nell'array HIDDEN
            HIDDEN[i]=RISPOSTE[i];
            for (int j = 0;j<RISPOSTE[i].length();j++) {
                if (RISPOSTE[i].charAt(j) == ' ') {
                    continue;
                }
                HIDDEN[i]=HIDDEN[i].replace(RISPOSTE[i].charAt(j),'_');
            }
        }

        for (int i = 0;i<HIDDEN.length;i++) {                               // vado a calcolare il numero dei suggerimenti podssibili per una determinata risposta per ognuna di queste
            int risp_len = (RISPOSTE[i].length()/2);
            switch (risp_len) {
                case 0:
                case 1:
                case 2:
                    N_SUGGERIMENTI = 1;
                    break;
                case 3:
                    N_SUGGERIMENTI = 2;
                    break;
                case 4:
                case 5:
                    N_SUGGERIMENTI = 3;
                    break;
                case 6:
                    N_SUGGERIMENTI = 4;
                    break;
                case 7:
                case 8:
                    N_SUGGERIMENTI = 5;
                    break;
                default:
                    N_SUGGERIMENTI = 6;
                    break;
            }
            char hint_temp[] = HIDDEN[i].toCharArray();                     // vado a sostituire per il numero dei suggerimenti un '_' con una vera lettera della risposta a posizione random
            for (int j = 0;j<N_SUGGERIMENTI;j++) {
                int r = rnd.nextInt(RISPOSTE[i].length());
                hint_temp[r] = RISPOSTE[i].charAt(r);
            }
            HIDDEN[i]=String.valueOf(hint_temp);                            // converto il char[] in stringa
        }

        //-------------------------- JFRAME --------------------------//

        setTitle("CADUTA LIBERA the GAME");                                     // titolo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                         // chiudere la JVM correttamente
        setSize(new Dimension(1000,700));                           // dimensione della JFrame (finestra)
        setResizable(false);                                                    // non si può ridimensionare
        setLocationRelativeTo(null);                                            // posizione di creazione finestra al centro dello schermo
        setIconImage(frame_icon.getImage());                                    // imposto immagine della finestra
        setLayout(new BorderLayout(0,10));                           // imposto un tipo di layout (rispetto ai bordi)

        //--------------------------- HEADER -------------------------//

        header.setBackground(purple);                                           // copiato del MenuPage.java
        header.setPreferredSize(new Dimension(0,100));
        header.setLayout(new GridBagLayout());
        add(header,BorderLayout.NORTH);

        GridBagConstraints gbc_null = new GridBagConstraints();
        gbc_null.gridwidth = GridBagConstraints.REMAINDER;
        gbc_null.fill = GridBagConstraints.HORIZONTAL;

        titolo.setText("CADUTA LIBERA");
        titolo.setForeground(yellow);
        titolo.setHorizontalAlignment(JLabel.CENTER);
        titolo.setFont(new Font("Consolas",Font.BOLD,40));
        header.add(titolo,gbc_null);

        subtitolo.setText("by Gerry Scotty");
        subtitolo.setForeground(yellow);
        subtitolo.setHorizontalAlignment(JLabel.CENTER);
        header.add(subtitolo,gbc_null);

        //--------------------------- CENTER -------------------------//

        center.setBackground(purple);                                       // edito il panel che contiene input, frasi, passi e timer
        center.setLayout(new BorderLayout());
        add(center);

                            //----------SPAZIO TIMER-----------//

        clock.setBackground(purple);                                        // edito un po il panel che conterrà il timer
        clock.setPreferredSize(new Dimension(50,50));
        clock.setLayout(new BorderLayout());
        center.add(clock,BorderLayout.NORTH);

        time.setForeground(yellow);                                         // formatto il testo del timer
        time.setFont(new Font("Consolas",Font.BOLD,30));
        time.setHorizontalAlignment(JLabel.CENTER);
        time.setVerticalAlignment(JLabel.CENTER);
        clock.add(time);

                            //-------SPAZIO GERRY+DOMANDA------//

        ask.setBackground(purple);                                          // edito un po il panel che conterrà l' imagine di gerry e e le sue frasi
        ask.setLayout(new BorderLayout());
        center.add(ask,BorderLayout.CENTER);

        domande.setText(DOMANDE[INDEX]);                                    // formatto il testo delle frasi di gerry
        domande.setForeground(yellow);
        domande.setIcon(img_domande[rnd.nextInt(img_domande.length)]);
        domande.setFont(new Font("Consolas",Font.BOLD,17));
        domande.setHorizontalAlignment(JLabel.CENTER);
        domande.setVerticalAlignment(JLabel.CENTER);
        ask.add(domande);

                            //--------SPAZIO PER PASSI--------//

        steps.setBackground(purple);                                       // edito il pannello che conterrà i passi
        steps.setPreferredSize(new Dimension(100,0));
        steps.setLayout(new GridBagLayout());
        center.add(steps,BorderLayout.EAST);

        GridBagConstraints gbc_bts = new GridBagConstraints();             // Valori costanti che applico ai bottoni che uso per rappresentare graficamente i passi
        gbc_bts.gridwidth = GridBagConstraints.REMAINDER;
        gbc_bts.fill = GridBagConstraints.HORIZONTAL;
        gbc_bts.insets = new Insets(5,0,5,0);         // aggiunngo del padding per distanziarli

        for (int i = 0 ; i<bts_true_false.length;i++) {                     // vado a inizzializzare i bottoni e a editarli
            bts_true_false[i] = new JButton(" ");
            bts_true_false[i].setPreferredSize(new Dimension(60,60));
            bts_true_false[i].setEnabled(false);                            // gli rendo non cliccabili
            steps.add(bts_true_false[i],gbc_bts);
        }
        bts_true_false[0].setBackground(Color.orange);                      // cambio il colore del primo bottone ad arancione (ovvero il colore del passo corrente (numero di domanda che gerry ti sta chiedendo))

                            //----------SPAZIO PER HINTS--------//

        hint.setBackground(purple);                                         // edito il pannello che conterrà gli aiuti della risposta
        hint.setPreferredSize(new Dimension(0,80));
        hint.setLayout(new BorderLayout());
        ask.add(hint,BorderLayout.SOUTH);

        hints.setText(HIDDEN[0]);                                           // imposto la prima domanda e formatto il testo
        hints.setFont(new Font("Consolas",Font.PLAIN,25));
        hints.setForeground(yellow);
        hints.setHorizontalAlignment(JLabel.CENTER);
        hints.setVerticalAlignment(JLabel.CENTER);
        hint.add(hints);

                            //----------SPAZIO INPUT--------//

        GridBagConstraints gbc_input = new GridBagConstraints();                    // Valori costanti per il gridbaglayout della sezione input
        gbc_input.insets = new Insets(0,0,0,20);                // vado a mettere solo del padding per distanziarli

        input.setBackground(purple);                                                // edito il panel che conterrà l' input utente
        input.setPreferredSize(new Dimension(50,100));
        input.setLayout(new GridBagLayout());
        center.add(input,BorderLayout.SOUTH);

        write_risposta.setPreferredSize(new Dimension(300,30));         // formatto la text box per inserire la risposta
        write_risposta.setFont(new Font("Consolas",Font.PLAIN,20));
        write_risposta.setBackground(purple);
        write_risposta.setForeground(Color.WHITE);
        input.add(write_risposta,gbc_input);

        enter.setText("RISPONDI");                                                  // formatto i bottoni
        enter.setBackground(purple);
        enter.setForeground(yellow);
        enter.setFocusable(false);
        enter.addActionListener(this);
        input.add(enter,gbc_input);

        passo.setText("PASSO!");
        passo.setBackground(purple);
        passo.setForeground(yellow);
        passo.setFocusable(false);
        passo.addActionListener(this);
        input.add(passo,gbc_input);

        //--------------------------- FOOTER -------------------------//

        footer.setBackground(purple);                                           // copiato da MenuPage.java
        footer.setPreferredSize(new Dimension(0,40));
        footer.setLayout(new GridLayout());
        add(footer,BorderLayout.SOUTH);

        copyright.setText("Copyright Jay 2020-2025");
        copyright.setForeground(yellow);
        copyright.setFont(new Font("Consolas",Font.PLAIN,15));
        copyright.setHorizontalAlignment(JLabel.RIGHT);
        copyright.setVerticalTextPosition(JLabel.BOTTOM);
        footer.add(copyright);

        //------------------------- TIMER ----------------------------//

        TimerTask task = new TimerTask() {                                      // creo un task per il timer ovvero un compito o una azione che deve eseguire in questo caso alla sua terminazione (run())

            int min = 3;
            int sec = 0;

            @Override
            public void run() {
                String min_s = String.format("%02d",min);                       // creo una string con un formato definito %02d (ovvero converto il numero in decimale a 2 cifre con un padding a sinistra composto da 0 (quindi in caso di numero a una cifra aggiungerà uno 0 a sinista))
                String sec_s = String.format("%02d",sec);
                time.setText(min_s+":"+sec_s);                                  // aggiorno il testo

                if (sec >= 0) {
                    sec--;
                }
                if (sec < 0 && min>0 ){
                    min--;
                    sec=59;
                }
                if (sec < 0 && min == 0) {                                      // se il timer va a 00:00 fermo il timer e lo rimuovo dalla coda di esecuzione e creo la nuova finestra
                    timer.cancel();
                    timer.purge();
                    out_of_time = true;
                    setVisible(false);
                    new Win_Lose(win,out_of_time,CORRECT_COUNTER,DOMANDE,RISPOSTE,FRASI_WIN,FRASI_LOSE);
                }
            }
        };

        timer.scheduleAtFixedRate(task,0,1000);                 // dico al timer di eseguire la task senza alcun tipo di delay ma la deve eseguire dopo 1000ms ovvero 1 secondo creando così un cronometro

        //------------------- GERRY COMMENT TIMER --------------------//

        // timer di swing che mi permette di aspettare un determinato lasso di tempo per eseguire determinate istruzioni (2sec)
        wait_next = new javax.swing.Timer(2000, actionEvent -> {
            if (CORRECT_COUNTER == PASSI) {                                   // se ho risposto correttamente a tutte le domande creo la nuova finestra
                setVisible(false);
                new Win_Lose(win,out_of_time,CORRECT_COUNTER,DOMANDE,RISPOSTE,FRASI_WIN,FRASI_LOSE);
            }
            bts_true_false[INDEX].setBackground(Color.orange);                // setto il colore del passo ad arancione

            passo.setEnabled(true);                                           // riabilito il bottone passo
            enter.setEnabled(true);                                           // riabilito il bottone rispondi
            write_risposta.setEnabled(true);                                  // riabilito il textbox

            domande.setText(DOMANDE[INDEX]);                                  // aggiorono la frase di gerry alla prossima domanda
            domande.setIcon(img_domande[rnd.nextInt(img_domande.length)]);    // aggiorno l'icona

            hints.setForeground(Color.white);                                 // rimetto il colore del testo degli aiuti a bianco
            hints.setText(HIDDEN[INDEX]);                                     // aggiorno per la risposta successiva
            wait_next.stop();                                                 // fermo il timer
        });

        //------------------------------------------------------------//

        setVisible(true);                                                           // Rendo visibile la finestra in se

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if (actionEvent.getSource() == enter) {                                     // se clicco rispondi

            String risposta = write_risposta.getText().toLowerCase();               // prendo la risposta e la converto tutta in minuscolo
            write_risposta.setText("");                                             // cancello la scritta sul textbox

            if (risposta.equals(RISPOSTE[INDEX].toLowerCase())) {                   //controllo se la risposta combacia (convetita tutta in minusolo)

                hints.setForeground(Color.green);                                   // aggiorno e formatto il passo e la label degli aiuti con la risposta completa
                hints.setText(RISPOSTE[INDEX]);
                bts_true_false[INDEX].setBackground(Color.green);

                CORRECT_COUNTER++;                                                  // incremento il contatore delle risposte corrette

                for (int i = INDEX ;i<LUNGHEZZA_ARRAY-1;i++) {                      // vado ad eliminare domande e risposte giuste per non ripeterle
                    DOMANDE[i] = DOMANDE[i+1];                                      // eseguo uni shift a sinistra per eliminare la domanda a cui ho risposto correttamente
                    RISPOSTE[i] = RISPOSTE[i+1];
                    HIDDEN[i] = HIDDEN [i+1];
                    bts_true_false[i] = bts_true_false [i+1];
                }

                LUNGHEZZA_ARRAY--;                                                  // decremento il contatore della lunghezza dell'array
                INDEX--;                                                            // ddecremento l index per avere la domanda giusta

                if (INDEX == LUNGHEZZA_ARRAY-1) {                                   // se incrementando l' index supero la lunghezza dell'array torno all' inizio di questultimo
                    INDEX = 0;
                }
                else {                                                              // se no incremento senza problemi
                    INDEX++;
                }

                passo.setEnabled(false);                                            // disabilito temporaneamente i bottoni e il textbox
                enter.setEnabled(false);
                write_risposta.setEnabled(false);

                domande.setText(FRASI_YES[rnd.nextInt(10)]);                 // faccio dire a gerry una frase
                domande.setIcon(img_esatto);

                wait_next.start();                                                  // faccio partire il timer di 2s

            } else if (!risposta.equals("")) {                                      // se la risposta è sbagliata ma non vuota

                passo.setEnabled(false);                                            // disabilito temporaneamente i bottoni e il textbox
                enter.setEnabled(false);
                write_risposta.setEnabled(false);

                domande.setText(FRASI_NO[rnd.nextInt(10)]);                  // faccio dire a gerry una frase
                domande.setIcon(img_errore);

                wait_next.start();                                                  // faccio partire il timer di 2s
            }
        }

        if (actionEvent.getSource() == passo) {                                     // se clicco passo!

            bts_true_false[INDEX].setBackground(Color.darkGray);                    // cambio il colore del bottone in grigio per dire che ho passato la domanda

            if (INDEX == LUNGHEZZA_ARRAY-1) {                                       // se incrementando l' index supero la lunghezza dell'array torno all' inizio di questultimo
                INDEX = 0;
            }
            else {                                                                  // se no incremento senza problemi
                INDEX++;
            }

            domande.setText(DOMANDE[INDEX]);                                        // aggiorno la domanda
            domande.setIcon(img_domande[rnd.nextInt(img_domande.length)]);          // aggirno la icona
            hints.setText(HIDDEN[INDEX]);                                           // aggiorno gli aiuti

            bts_true_false[INDEX].setBackground(Color.orange);                      // cambio il colore del bottone in cui sono in arancione
        }

        if (CORRECT_COUNTER == PASSI){                                              // se le indovino tutte entro il tempo ho vinto
            domande.setText("BRAVISSIMO HAI COMPLETATO I 10 PASSI");                // aggiorno la frase di gerry
            hints.setVisible(false);                                                // rendo invisibili gli input
            passo.setEnabled(false);
            enter.setEnabled(false);
            win = true;
            timer.cancel();                                                         // fermo il timer di 3 min
            timer.purge();                                                          // lo rimuovo dalla coda
        }
    }

}
