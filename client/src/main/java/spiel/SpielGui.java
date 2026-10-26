package spiel;

import java.awt.*;

import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.*;
import javax.swing.*;




import Interfaces.Bildschirmanzeige;

/*
 *Erstellt am 27.12.2005 von Sebastian Horchler, Pidiktaki Maria
 *Änderungen immer von Pidiktaki Maria, Sebastian Horchler
 *Änderungen: 28.12.2005  2 Weitere JPanles eingefüg, Methode spieldrann (aendereSpieler2, aendereSpieler1), verbesserungen in Methoden SpielAufbau, spielerEingabeMenue, brettInit
 *			  29.12.2005  Panels für die Spielernamen in Methoden gepackt (initPanelSpieler1, initPanelSpieler2) um abwechlend sie je nach dem
 *						  wer drann ist den Spielernamen einblenden
 *						  Menubar entworfen, Methode erzeugeMenu hinzugefügt, ausarbeitung der Methode actionPerformed
 *						  Methode wuerfel, für die Anzeige des  Würfelergebnis 
 *			  30.12.3005  Hinzufügen der Methode nachrichtenAnzeige
 *			  02.01.2006  ausarbeitung der Methoden:neuerSpielerName, gibspielerName, spieler2EingabeName. 
 *						  Hinzufügen der Methode erSpielerName, gibspielerName, spieler2EingabeName.	  			
 *			  05.01.2006  Hinzugefügen der Methoden: ausgabenachricht,eingabe,textValueChanged. 
 *							Abbarbeitung der Methode nachrichtenAnzeige.
 *			  07.01.2006  Überarbeitung der Methoden brettintialisierung,ausgabeMessages,brettinit,wuerfel.
 *						  Hinzugefügen labels für die Steine und methode wuerfelinit
 *			  10.01.2006  Hinzugfügen der Methoden: steinInitialisierung, temploeschenSteine und bearbeitung der Methode brettinitialisierung
 *			  11.01.2006  Hinzugfügen der Methoden: eingabeIP, mouseClicked, konvertiere[...]ZuKoordinate(x2),
 *												    getAuswahlStein, getAuswahlZug und abarbeitung der Methode nächsterZug
 *			 			 Änderungen in der Klassen: BildschirmAnzeige, Spieler und Konsole für das hinzufügen des spielerSteinPosArray in der 
 *						 Methode naechsterZug.
 *			 12.01.2006 Verbesserungen in der Methode naechsterZug
 *			 14.01.2006 änderungen in der Methoden: eingabe und actionperformed(einfuegen des Buttons senden)
 *                                                  eindeutschung der eng. Bezeichner
 */
/**
 * @author  Bigmama
 */
public class SpielGui extends JFrame implements ActionListener,
		TextListener, Bildschirmanzeige, MouseListener
{
	/** Laedt ein Bild aus dem Classpath-Ordner images/. */
	/** Kurze Pause in Warteschleifen, damit Aenderungen aus dem Event-Thread sichtbar werden. */
	private static void pause() {
		try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
	}

	private static javax.swing.ImageIcon icon(String name) {
		return new javax.swing.ImageIcon(SpielGui.class.getResource("/images/" + name));
	}


	private JLayeredPane ebeneM; /* Spielbrett Ebende in der Mitte */
	private JPanel panelW; // West für Spieler mit Zugehöri
	private JPanel panelE; // Ost für Spieler mit Zugehörigkeit 0
	private JPanel panelWuerfel;// Nord Ost für Würfelergebniss
	private JPanel panelAusgabe; // Zeigt jeder Art von Nachricht
																// an(Ausgabe,
	// Eingabe)
	private JLabel wuerfel; // Label für das Bild für den ensprechenden
	// Würfelergebnis
	private Font schrift1; // Schriftart für den Label l1
	private Font schrift2;// Schriftart für den Label l2
	private JLabel l1; // Label für die Anzeige des SpielerNames mit der
	// Zugehörigeit 1
	private JLabel l2; // Label für die Anzeige des SpielerNames mit der
	// Zugehörigeit 0
	private volatile int spielArt; // Hier wird die Auswahl für den
	/**
	 * @uml.property  name="wnd"
	 * @uml.associationEnd  
	 */
	private SpielMenu wnd; // Hauptfenster des Spiels
	private String spielerName; // der Name des Spielers der das Programm
															// start
	private String spielerName2 = "Spieler 2"; // Default Initialisierung
																							// bis
	// zur Eingabe
	private String spielerZugeh1 = "spielerName";// Default Initialisierung
	// falls man Netzwerkspiel
	// spielen will
	private String spielerZugeh2 = spielerName2;
	private TextArea textAusgabe; // Textfeld für Ausgabenachrichten
	private TextField eing; // TextFeld für die Eingabe
	private volatile String eingabe;// Speichert den eingegebenen Text
	private Button senden;
	private JLabel s11; // labels
	private JLabel s12;
	private JLabel s13;
	private JLabel s14;
	private JLabel s15;
	private JLabel s16;
	private JLabel s01;
	private JLabel s02;
	private JLabel s03;
	private JLabel s04;
	private JLabel s05;
	private JLabel s06;
	// für
	// die
	// Steine
	private int [] steinKoordinate = new int [2]; // Hier soll das Array
	// koordinaten gespeichert
	// werden ,falls vom ersten
	// Mausklick
	private int [] zugKoordinate = new int [2];// Hier soll das Array
																							// koordinaten
	// gespeichert werden ,falls vom
	// zweiten Mausklick

	private boolean mouseListenerSperre = true; // Ist nützlich um zu
																							// erkennen
	// wenn einen 2. oder 3. Spiel
	// ge spiel Wird so dass: 1.
	// Kein 2.Mouselisterner erzeugt
	// wird und 2. Kein neues
	// Gesamtfenster
	private volatile boolean spielSperreAuswahlStein = true; // Erlabaut das erste
	// Mausklick vom Spieler als
	// Steinauswahl zu erkennen
	private volatile boolean spielSperreAuswahlZug = true;// Erlabaut das erste
																								// Mausklick
	// vom Spieler als
	// Zugauswahl zu erkennen
	private volatile boolean spielSperreModus = true; /*
																						 * False: man darf nichts
																						 * mehr an der Spielauswahl
																						 * ändern
																						 */

	/*
	 * True: man darf nur ein Mal eine Spielauswahl treffen, wenn das spiel
	 * zu ende ist dann wird sie wieder von False auf true gestzt
	 */

	public SpielGui ()
	{
		spielerName = JOptionPane
				.showInputDialog ("Bitte geben Sie Ihren Spielernamen ein");
	}

	/*
	 * Initialisiert den Inhalt von dem Gesammtfenster, für die Anzeige der
	 * Menüs, Spielernamen, Brettfeld, Würfel und Textfelder
	 */
	public void spielAufbau ()
	{

		wnd.setJMenuBar (erzeugeMenu ());
		brettinit ();
		wuerfelInit ();
		steinInitialisierung ();
		initPanelSpieler1 (spielerName, 0);
		initPanelSpieler2 ();
		nachrichtenAnzeige ("Ausgabefeld");

		if (mouseListenerSperre)
		{
			ebeneM.addMouseListener (this);
			mouseListenerSperre = false;
		}

		// wnd.pack();
		wnd.setVisible (true);

	}

	/*
	 * Wird von der Steuerung aufgerufen und übergibt die Auswahl des
	 * Spielers für ein bestimmtes Spiel
	 */
	public int spielerEingabeMenue ()
	{
		if (!mouseListenerSperre)
			wnd.dispose ();

		wnd = new SpielMenu ();
		spielAufbau ();

		/* wartet bis der Spieler eine Auswahl getroffen hat */
		do
		{
			auswahlMenue ();
			pause ();
		} while (auswahlMenue () == 0);

		return auswahlMenue ();
	}

	/* kriegt die werte vom Actionlistener aus dem Menu */

	/* übergibt den Aktuellen Stand für die Auswahl */
	public int auswahlMenue ()
	{

		return spielArt;
	}

	/* Nur Brett Sichtbar und immer in der Mitte */
	public void brettinit ()
	{
		ebeneM = getLayeredPane ();
		ebeneM.setVisible (true);
		wnd.getContentPane ().add ("Center", ebeneM);
		JLabel spielbrett = new JLabel (icon("spielb.gif"));
		spielbrett.setBounds (60, 1, 472, 472);
		ebeneM.add (spielbrett, JLayeredPane.DEFAULT_LAYER);

		ebeneM.setOpaque (false);
	}

	/* initialisiert das Panel für das Würfelergebnis */
	public void wuerfelInit ()
	{
		panelWuerfel = new JPanel ();
		wnd.getContentPane ().add ("North", panelWuerfel);
		wuerfel = new JLabel (icon("wuerfel1.gif"));
		panelWuerfel.add (wuerfel);
		panelWuerfel.setVisible (true);
		panelWuerfel.setLayout (new FlowLayout (FlowLayout.RIGHT, 15, 15));

	}

	/* Für das wechseln der Würfelergebnisse */
	public void wuerfel (int wuerfelErgebnis)
	{
		panelWuerfel.remove (wuerfel);
		switch (wuerfelErgebnis)
		{
		case 1:
		{
			wuerfel = new JLabel (icon("wuerfel1.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		case 2:
		{
			wuerfel = new JLabel (icon("wuerfel2.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		case 3:
		{
			wuerfel = new JLabel (icon("wuerfel3.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		case 4:
		{
			wuerfel = new JLabel (icon("wuerfel4.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		case 5:
		{
			wuerfel = new JLabel (icon("wuerfel5.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		case 6:
		{
			wuerfel = new JLabel (icon("wuerfel6.gif"));
			panelWuerfel.add (wuerfel);
			break;
		}
		default:
			System.out.println ("FEHLER CASE WÜRFELAUSGABE GIF");
		}
		panelWuerfel.setVisible (false);

		repaint ();
		panelWuerfel.setVisible (true);
	}// ENDE wuerfel

	/* initialisiert das Panel für den Namen für den ersten Spieler */
	public void initPanelSpieler1 (String spielerName1, int dran)
	{
		panelW = new JPanel ();
		wnd.getContentPane ().add ("West", panelW);
		panelW.setVisible (true);
		panelW.setLayout (new FlowLayout (FlowLayout.LEFT, 0, 0));
		schrift1 = new Font ("Serif", Font.BOLD, 20);
		l1 = new JLabel ("  " + spielerName1);
		l1.setForeground (Color.lightGray);
		l1.setFont (schrift1);
		panelW.add (l1);

	}// ENDE initPanelSpieler1

	/* initialisiert das Panel für den Namen für den zweiten Spieler */
	public void initPanelSpieler2 ()
	{
		panelE = new JPanel ();
		wnd.getContentPane ().add ("East", panelE);
		panelE.setVisible (true);
		panelE.setLayout (new FlowLayout (FlowLayout.LEFT, 0, 410));
		schrift2 = new Font ("Serif", Font.BOLD, 20);
		l2 = new JLabel (spielerZugeh2 + " ");
		l2.setForeground (Color.lightGray);
		l2.setFont (schrift2);
		panelE.add (l2);

	}// //ENDE initPanelSpieler2

	/*
	 * Das Label l2 in PanelE, bekommt Namen vom Spieler mit der
	 * Zugehörigkeit 0
	 */
	public void aendereSpieler2 (int dran)
	{
		panelE.setVisible (false);
		panelE.remove (l2);
		l2 = new JLabel (spielerZugeh2 + " ");

		// Farbewechsel Spieler 2 Aktiv
		if (dran == 1)
		{
			l2.setForeground (Color.blue);
		}
		// Farbewechsel Spieler 2 ist Inaktiv
		if (dran == 0)
		{
			l2.setForeground (Color.lightGray);
		}

		l2.setFont (schrift2);
		panelE.add (l2);
		repaint ();
		panelE.setVisible (true);
	}// ENDE aendereSpieler2

	/*
	 * Das Label l1 in PanelW, bekommt Namen vom Spieler mit der
	 * Zugehörigkeit 0
	 */
	public void aendereSpieler1 (int dran)
	{
		panelW.setVisible (false);
		panelW.remove (l1);
		l1 = new JLabel (" " + spielerZugeh1 + " ");

		// Farbewechsel Spieler 1 Aktiv
		if (dran == 1)
		{
			l1.setForeground (Color.red);
		}
		// Farbewechsel Spieler 1 Inaktiv
		if (dran == 0)
		{
			l1.setForeground (Color.lightGray);
		}

		l1.setFont (schrift1);
		panelW.add (l1);
		repaint ();
		panelW.setVisible (true);

	}// ENDE aendereSpieler1

	/* Initialisiert die Felder für die Ein- und Ausgabnachrichten */
	public void nachrichtenAnzeige (String nachricht)
	{
		/* Feld für die Ausgabe erzeugen */
		panelAusgabe = new JPanel ();
		panelAusgabe.setPreferredSize (new Dimension (472, 100));

		wnd.getContentPane ().add ("South", panelAusgabe);
		panelAusgabe.setVisible (true);
		textAusgabe = new TextArea (nachricht, 3, 100);
		textAusgabe.setBackground (Color.LIGHT_GRAY);
		ScrollPane scrollpane = new ScrollPane (
				ScrollPane.SCROLLBARS_AS_NEEDED);
		add (scrollpane);
		panelAusgabe.add (textAusgabe);
		textAusgabe.setEditable (false);

		/* Feld für die Eingabe erzeuge */
		eing = new TextField (
				"Eingabefeld, Bitte rein klicken für die Eingabe", 90);
		panelAusgabe.add (eing);

		/* zusätzliches Button für die eingabe */
		senden = new Button ("Abschicken");
		panelAusgabe.add (senden, BorderLayout.EAST);
		senden.addActionListener (this);

	}// ENDE nachrichtenAnzeige

	/* Zeigt die Nachricht in die TextArea: textAusgabe */
	public void ausgabeNachricht (String ausgabe)
	{
		/* Bestimmter Spielablauf muss hier berücksichtig werden */
		if (ausgabe.endsWith ("hat das Spiel gewonnen."))
		{
			JOptionPane.showMessageDialog (this, "         GAME OVER" + "\n"
					+ ausgabe);
			spielerName2 = "Spieler 2";
			spielerZugeh2 = spielerName2;

			if (! (spielArt == 1 || spielArt == 2))
			{ /*
				 * Zurücksetung falls man ein Netzwerkspiel gespielt hat
				 */
				spielerZugeh1 = "Spieler 1";
				spielerZugeh2 = "Spieler 2";
			}
			aendereSpieler2 (0);
			aendereSpieler1 (0);
			spielArt = 0;
			spielSperreModus = true;
			ebeneM.setVisible (false);
			temploeschenSteine ();
			repaint ();
			ebeneM.setVisible (true);

		}
		else
			textAusgabe.append ("\n" + ausgabe);

	}// ENDE Ausgabemessages

	/*
	 * wird für bestimmte Situation vom Stuerung auf eine spielArt
	 * gewartet, die hier erzeugt wird
	 */
	public String eingabe ()
	{

		eingabe = "0";
		eing.addTextListener (this);

		do
		{
			/*
			 * Man wartet solange bis die eingabe einen Text bekommen hat.Das
			 * passiert wenn man den Button "abschicken" drückt.
			 */
			pause ();
		} while ("0".equals (eingabe));

		return eingabe;
	}

	/* benötigte Methode für den TextListener */
	public void textValueChanged (TextEvent black)
	{

	}

	/* Menü erzeugen */
	public JMenuBar erzeugeMenu ()
	{
		JMenuBar menubar = new JMenuBar ();

		// Das Spielmöglickeiten-Menue
		JMenu menu = new JMenu ("Spiel");
		menubar.add (menu);
		JMenu single = new JMenu ("Singleplayer");
		menu.add (single);
		JMenuItem item1 = new JMenuItem ("Spieler vs Computer an einem PC");
		single.add (item1);
		item1.addActionListener (this);

		JMenu multi = new JMenu ("Multiplayer");
		menu.add (multi);
		/* Unterpunkte für die Option Multiplayer */
		JMenuItem item2 = new JMenuItem ("Spieler vs Spieler an einem PC");
		multi.add (item2);
		item2.addActionListener (this);

		JMenuItem item3 = new JMenuItem ("Spieler vs Netzwerk");
		multi.add (item3);
		item3.addActionListener (this);

		JMenuItem item4 = new JMenuItem ("Computer vs Netzwerk");
		multi.add (item4);
		item4.addActionListener (this);

		menu.addSeparator ();

		JMenuItem ende = new JMenuItem ("Beenden");
		ende.addActionListener (this);
		menu.add (ende);

		// Das Hilfe-Menu
		JMenu hilfe = new JMenu ("Hilfe");
		menubar.add (hilfe);

		JMenuItem einl = new JMenuItem ("Einleitung");
		hilfe.add (einl);
		einl.addActionListener (this);

		JMenu wms = new JMenu ("Wie man spielt");
		hilfe.add (wms);

		/* Unterpunkte für die Option Wie man Spielt */
		JMenuItem hilfe1 = new JMenuItem ("Wer ist am Zug?");
		hilfe1.addActionListener (this);
		wms.add (hilfe1);

		JMenuItem hilfe2 = new JMenuItem ("Bewegen von Steinen");
		hilfe2.addActionListener (this);
		wms.add (hilfe2);

		JMenuItem hilfe3 = new JMenuItem ("Siegbedingungen");
		hilfe3.addActionListener (this);
		wms.add (hilfe3);

		JMenuItem hilfe4 = new JMenuItem ("Multiplayerhilfe");
		hilfe4.addActionListener (this);
		wms.add (hilfe4);
		return menubar;
	}// ENDE erzeugemenu

	/* initialisierungen für den ActionListener */
	public void actionPerformed (ActionEvent blackByte)
	{
		String command = blackByte.getActionCommand ();
		/* Aktionen für das Menu Spiel */
		if (command.equals ("Abschicken"))
		{

			eingabe = eing.getText ();
			// txt=null;
		}
		if (command.equals ("Beenden"))
			System.exit (0);

		if (command.equals ("Spieler vs Computer an einem PC"))
		{
			if (spielSperreModus)
			{
				spielArt = 2;
				spielerZugeh1 = spielerName;
				spielerZugeh2 = "KI BlackByte";
				aendereSpieler1 (0);
				aendereSpieler2 (0);
				spielSperreModus = false;
				ausgabeNachricht ("Spiel VS Computer Startet");
			}
			else
				JOptionPane
						.showMessageDialog (
								this,
								"Sie haben schon eine Spielauswahl getroffen!"
										+ "\nWarten sie bis das Spiel zu ende ist, um eine neues spiel anzufangen!");
		}

		if (command.equals ("Spieler vs Spieler an einem PC"))
		{

			if (spielSperreModus)
			{
				spielArt = 1;
				spielSperreModus = false;
				ausgabeNachricht ("Spiel  zwischen 2 spieler Startet");
			}
			else
				JOptionPane
						.showMessageDialog (
								this,
								"Sie haben schon eine Spielauswahl getroffen!"
										+ "\nWarten sie bis das Spiel zu ende ist, um eine neues spiel anzufangen!");
		}

		if (command.equals ("Spieler vs Netzwerk"))
		{
			if (spielSperreModus)
			{
				spielArt = 3;
				spielSperreModus = false;
				ausgabeNachricht ("Spiel gegen das Netzwerk kann starten");
			}
			else
				JOptionPane
						.showMessageDialog (
								this,
								"Sie haben schon eine Spielauswahl getroffen!"
										+ "\nWarten sie bis das Spiel zu ende ist, um eine neues spiel anzufangen!");
		}

		if (command.equals ("Computer vs Netzwerk"))
		{
			if (spielSperreModus)
			{
				spielArt = 4;
				spielSperreModus = false;
				ausgabeNachricht ("Spiel gegen das Netzwerk startet");
			}
			else
				JOptionPane
						.showMessageDialog (
								this,
								"Sie haben schon eine Spielauswahl getroffen!"
										+ "\nWarten sie bis das Spiel zu ende ist, um eine neues spiel anzufangen!");
		}

		/* Aktionen für das Menu Hilfe */
		if (command.equals ("Einleitung"))
			JOptionPane
					.showMessageDialog (
							this,
							"\n\n\n\n„EinStein würfelt nicht“ ist ein Spiel für 2 Personen. Es wurde vom deutschen "
									+ "\nMathematiker XXXX entwickelt, der an der Uni ?Göttingen unterrichtet, "
									+ "\nentwickelt."
									+ "\nDie sechs Spielsteine der beiden Spieler befinden sich in einem Raster von "
									+ "\nfünf mal fünf Spielfeldern in den Ecken, die sich gegenüberstehen. Jeder Stein "
									+ "\nkann sich immer nur 1 Feld bewegen, aber es ist möglich, sowohl eigene als "
									+ "\nauch gegnerische Spielsteine zu schlagen, in dem man auf sie drauf zieht. Die "
									+ "\nbeiden Spieler ziehen abwechselnd, ein Würfelergebnis bestimmt, welcher Stein "
									+ "\ngezogen werden darf. \n"
									+ "\nDie Besonderheit ist, dass es auch möglich ist, selbst zu entscheiden, welchen "
									+ "\nSpielstein man bewegen darf, wenn der Stein für das Würfelergebnis nicht mehr "
									+ "\nim Spiel ist. Wenn nur noch ein Stein bei einem Spieler im Spiel ist, wird das "
									+ "\nWürfelergebnis ignoriert, denn dieser Stein darf immer ziehen, denn „Ein Stein "
									+ "\nwürfelt nicht“.\n"
									+ "\nEin Beispiel:"
									+ "\nAuf dem Spielfeld existieren nur noch die Steine 1, 3 und sechs eines "
									+ "\nSpielers. Der Würfel gibt als Ergebnis eine 2 aus. Nun hat dieser Spieler die "
									+ "\nWahl, ob er den Stein Nr. 1 oder den Stein Nr 3 bewegen möchte.\n"
									+ "\nDieses Spiel wurde von der Entwicklergruppe BlackByte des 3. Semesters der"
									+ "\nFachhochschule Fulda im Rahmen des Programmieren-Praktikums entwickelt"
									+ "\nund getestet.");

		if (command.equals ("Wer ist am Zug?"))
			JOptionPane
					.showMessageDialog (
							this,
							"\n\n\nWer ist am Zug?\n"
									+ "\nEs wird abwechselnd gespielt. Jeder Spieler erhält ein Würfelergebnis, welches "
									+ "\ndie möglichen Steine bestimmt, die Sie ziehen können. Wenn Sie am Zug sind, "
									+ "\nwerden Sie aufgefordert ihren Zug zu machen. ");
		if (command.equals ("Bewegen von Steinen"))
			JOptionPane
					.showMessageDialog (
							this,
							"\n\n\nBewegen von Steinen\n"
									+ "\nWährend des Spiels wird das Programm Ihnen alle Spielmöglichkeiten zeigen, die "
									+ "\nihnen jeweils zur Verfügung stehen. Sie werden dann z.B. aufgefordert, sich "
									+ "\nzwischen den Steinen 1 und 3 zu entscheiden, die Sie bewegen wollen. Nachdem "
									+ "\nsie diese Entscheidung durch die Eingabe der Spielsteinnummer getroffen haben, "
									+ "\nwird das Programm ihnen auch alle möglichen Züge vorgeben, aus der Sie "
									+ "\nwiederum auswählen dürfen. Dabei wird das Programm von Ihnen lediglich die "
									+ "\nneue Zielkoordinate des Steins verlangen. \n"
									+ "\nWenn sie während der Steinbewegungsphase eine Null statt der Spielsteinnummer "
									+ "\neingeben, wird das Spiel sofort beendet.");
		if (command.equals ("Siegbedingungen"))
			JOptionPane
					.showMessageDialog (
							this,
							"\n\n\nSiegbedingungen\n"
									+ "\nDas Ziel des Spiel ist es, einen der eigenen Steine in die gegnerische Ecke zu "
									+ "\nbewegen oder einfach alle gegnerischen Steine zu schlagen.");
		if (command.equals ("Multiplayerhilfe"))
			JOptionPane
					.showMessageDialog (
							this,
							"\n\n\nMultiplayerhilfe\n"
									+ "\nDie Auswahl des Multiplayerspiels ohne Netzwerk erlaubt Ihnen gegen einem "
									+ "\nFreund an einem Rechner zu spielen. Die Option Multiplayerspiel mit Netzwerk "
									+ "\ngestattet Ihnen über das Netzwerk gegen einen anderen Spieler an seinem "
									+ "\nComputer oder dessen Künstliche Intelligenz zu spielen.");

	}// ENDE actionPerformed

	/* Falls der angegebener Name im Netzwerk schon existiert */
	public void neuerSpielerName ()
	{

		spielerName = JOptionPane
				.showInputDialog ("Bitte geben Sie einen anderen Spielernamen ein");

	}

	/* Liefert SpielerName(erster Spieler) zurück */
	public String gibspielerName ()
	{

		return spielerName;
	}

	/* Name Abfrage, für den Fall das 2 Spieler auf einen Pc spielen wollen */
	public String spieler2EingabeName ()
	{
		spielerName2 = JOptionPane
				.showInputDialog ("Bitte geben Sie einen Spielernamen ein");
		spielerZugeh1 = spielerName;
		spielerZugeh2 = spielerName2;
		aendereSpieler1 (0);
		aendereSpieler2 (0);

		return spielerName2;
	}

	/*
	 * Zeichnet das gesamte Spielfeld(Brett und Steine) wärend des Spiels
	 * neu, nach jedem Zug
	 */
	public void brettinitialisierung (int [] steinArray, String Spieler1,
			String Spieler2, int spieler1Zugehoerigkeit)
	{
		int attributeAllerSteine = 48; // groesse des Feldes wenn beide
		// SpielerSteine + Eigenschaften
		// enthalten sind
		int steinNummer, // Nummer des Steins (Werte 1 bis 6)
		zugehoerigkeit, // Zugehoerigkeit der Steine zu welchem Spieler
		zeile, // zeilenPosition des Steins (Werte: 1 bis 5)
		spalte; // spaltenPosition des Steins (Werte: 1 bis 5)

		int zeileKonstante = 0; // Konstante für die Pixelkorrdinaten. Diese
		// Dient für die Positionierung der Steine auf
		// dem Spielbrett.
		int spaltenKonstante = 60;// wie oben.
		int steinGroesse = 94; // ist die Pixelgroesse (breite u. höhe) jedes
		// Steinbildes.

		/*
		 * Diese Zusätzliche Initialisierung ist notwenidig für Spiele gegen
		 * Netzwerk wo wir nicht wissen können welcher Spielername welche
		 * Zugehörigkeit hat, damit es an der Richtige Brettseite angezeigt
		 * werden kann
		 */
		if (spieler1Zugehoerigkeit == 1)
		{
			spielerZugeh1 = Spieler1;
			spielerZugeh2 = Spieler2;

		}
		else if (spieler1Zugehoerigkeit == 0)
		{
			spielerZugeh1 = Spieler2;
			spielerZugeh2 = Spieler1;
		}
		aendereSpieler1 (0);
		aendereSpieler2 (0);

		ebeneM.setVisible (false);
		temploeschenSteine ();

		/*
		 * Diese Schliefe erzeugt eine neues SteinArray welches die Steine
		 * die Zeile und Spalte 0 haben vernachlässigt, so dass im nächsten
		 * schritt nur die notwendigen Steine für die Darstellung der
		 * aktuelle Situation sind, auch erzeugt werden
		 */
		for (int i = 0; i < attributeAllerSteine; i += 4)
		{
			if (steinArray[i + 2] != 0)
			{ // solange keine Zeile den Wert 0 hat
				/* gib Steinnummer, dazugehoerige Zeile und Spalte aus */
				zugehoerigkeit = steinArray[i];
				steinNummer = steinArray[i + 1];
				zeile = steinArray[i + 2];
				spalte = steinArray[i + 3];

				/*
				 * Spielerzugehoerigkeit = Spieler 1 oder 2 bestimmen, beide
				 * erhalten unterschiedliche Steinbielder
				 */
				if (zugehoerigkeit == 1)
				{

					switch (steinNummer)
					{
					case 1:
					{
						s11 = new JLabel (icon("Stein-Seite1-Nr-1.gif"));
						s11
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s11, JLayeredPane.DRAG_LAYER);
						break;
					}
					case 2:
					{
						s12 = new JLabel (icon("Stein-Seite1-Nr-2.gif"));
						s12
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s12, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 3:
					{
						s13 = new JLabel (icon("Stein-Seite1-Nr-3.gif"));
						s13
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s13, JLayeredPane.DRAG_LAYER);
						break;
					}
					case 4:
					{
						s14 = new JLabel (icon("Stein-Seite1-Nr-4.gif"));
						s14.setBackground (Color.pink);
						s14
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s14, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 5:
					{
						s15 = new JLabel (icon("Stein-Seite1-Nr-5.gif"));
						s15.setBackground (Color.pink);
						s15
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s15, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 6:
					{
						s16 = new JLabel (icon("Stein-Seite1-Nr-6.gif"));
						s16.setBackground (Color.pink);
						s16
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s16, JLayeredPane.DRAG_LAYER);
						break;
					}

					default:
						ausgabeNachricht ("Steingibt es nicht!");
						break;
					}// switch
				}// if==1

				else if (zugehoerigkeit == 0)
				{ /* 0 = rechts unten = X */

					switch (steinNummer)
					{
					case 1:
					{
						s01 = new JLabel (icon("Stein-Seite2-Nr-1.gif"));
						s01
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s01, JLayeredPane.DRAG_LAYER);
						break;
					}
					case 2:
					{
						s02 = new JLabel (icon("Stein-Seite2-Nr-2.gif"));
						s02
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s02, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 3:
					{
						s03 = new JLabel (icon("Stein-Seite2-Nr-3.gif"));
						s03
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s03, JLayeredPane.DRAG_LAYER);
						break;
					}
					case 4:
					{
						s04 = new JLabel (icon("Stein-Seite2-Nr-4.gif"));
						s04
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s04, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 5:
					{
						s05 = new JLabel (icon("Stein-Seite2-Nr-5.gif"));
						s05
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s05, JLayeredPane.DRAG_LAYER);
						break;
					}

					case 6:
					{
						s06 = new JLabel (icon("Stein-Seite2-Nr-6.gif"));
						s06
								.setBounds (
										(spaltenKonstante + (steinGroesse * (spalte - 1))),
										(zeileKonstante + (steinGroesse * (zeile - 1))),
										94, 94);
						ebeneM.add (s06, JLayeredPane.DRAG_LAYER);
						break;
					}
					default:
						ausgabeNachricht ("Steingibt es nicht!");
						break;
					}// switch
				}// if==0
			}// !==0
		}// for

		repaint ();
		ebeneM.setVisible (true);
	}// ENDE Brettinitialisierung

	/*
	 * hier werden die Labels der Steine Initialisiert, damit die Brett
	 * initialisierungsmethode keine Probleme hat, wenn sie die Labels
	 * löscht
	 */
	public void steinInitialisierung ()
	{
		s11 = new JLabel (icon("Stein-Seite1-Nr-1.gif"));
		s12 = new JLabel (icon("Stein-Seite1-Nr-2.gif"));
		s13 = new JLabel (icon("Stein-Seite1-Nr-3.gif"));
		s14 = new JLabel (icon("Stein-Seite1-Nr-4.gif"));
		s15 = new JLabel (icon("Stein-Seite1-Nr-5.gif"));
		s16 = new JLabel (icon("Stein-Seite1-Nr-6.gif"));

		s01 = new JLabel (icon("Stein-Seite2-Nr-1.gif"));
		s02 = new JLabel (icon("Stein-Seite2-Nr-2.gif"));
		s03 = new JLabel (icon("Stein-Seite2-Nr-3.gif"));
		s04 = new JLabel (icon("Stein-Seite2-Nr-4.gif"));
		s05 = new JLabel (icon("Stein-Seite2-Nr-5.gif"));
		s06 = new JLabel (icon("Stein-Seite2-Nr-6.gif"));
	}

	/*
	 * Entfernd die Labels von der Ebene des Spielbrettes, damit die
	 * Brettinitialisierung nur die wieder erzeugt die auch tatsächlich
	 * noch existieren wärend des Spielablaufs
	 */
	public void temploeschenSteine ()
	{
		ebeneM.remove (s11);
		ebeneM.remove (s12);
		ebeneM.remove (s13);
		ebeneM.remove (s14);
		ebeneM.remove (s15);
		ebeneM.remove (s16);
		ebeneM.remove (s01);
		ebeneM.remove (s02);
		ebeneM.remove (s03);
		ebeneM.remove (s04);
		ebeneM.remove (s05);
		ebeneM.remove (s06);
	}

	/**
	 * Methode liest die IP Adresse ein
	 * 
	 * @return ip = String mit der eingegebenen IP Adresse
	 */
	public String eingabeIP ()
	{
		String iP = JOptionPane
				.showInputDialog ("Bitte geben Sie eine IP Nummer ein: ");
		return iP;
	}

	/**
	 * Methode, die von dem Spieler einen gültigen Zugauswahl erwartet
	 * anhand seiner Mousecklicks.
	 * 
	 * @param moeglicheZuege
	 *          = int Array, welches die Zuege enthaelt, die der Stein
	 *          (wuerfelzahl) machen kann
	 * @param wuerfelzahl
	 *          = wuerfezahlergebnis entweder Server oder Intern je nach
	 *          SpielArt
	 * @return zug[] = intArray mit dem Zug, den der Spieler schließlich
	 *         machen will
	 */
	public int naechsterZug (int [] moeglicheZuege, int wuerfelzahl,
			int SpielerSteinPos[])
	{

		/*
		 * Methode MouseClicked ist wieder bereit 2 Klicks hintereinader zu
		 * speichern
		 */
		spielSperreAuswahlStein = true;
		spielSperreAuswahlZug = true;

		/*
		 * Abbruchbedingung, falls die 2 Mausklicks hinter einander, (1.
		 * klick für Stein Auswahl, 2. klick für Zugauswahl) mit den
		 * Möglichkeiten in den moeglicheZuegeArray, übereinstimmen
		 */
		boolean ergebnis = false;
		int [] gewaelterStein = new int [2]; /*
																					 * übernimmt die Werte für die
																					 * Steinkoordinate
																					 */
		int [] gewaelterzug = new int [2]; /*
																				 * übernimmt die Werte für
																				 * zugKoordinate
																				 */
		int steinNummer = 0; /*
													 * Speicher die Nummer des Steins der für die
													 * Auswahl angeklickt wurde auch gültig ist
													 */
		int zug = 0; /* kann 1-6 Werte einnehmen, je nach Zugmöglichkeiten */
		int auswahlmoeglickeit = 0; /*
																 * Ist die Position im moeglicheZuege wo
																 * alles übereinstimmt
																 */

		ausgabeNachricht ("Gewürfelt wurde eine: " + wuerfelzahl);
		wuerfel (wuerfelzahl);

		/*
		 * Hier wird der Spielername Rot angezeigt,jenachdem welcher Spieler
		 * dran ist
		 */
		if (moeglicheZuege[0] == 1)
		{
			ausgabeNachricht ("Spieler " + spielerName + " ist dran!");
			aendereSpieler1 (1);
			aendereSpieler2 (0);
		}
		else if (moeglicheZuege[0] == 0)
		{
			ausgabeNachricht ("Spieler " + spielerName2 + " ist dran!");
			aendereSpieler1 (0);
			aendereSpieler2 (1);
		}

		/*
		 * Hier findet die Prüfung statt´.Hier wird erlaubt so lange auf dem
		 * Spielbrett draufzuklicken,bis ein richtiger Zug erkannt wird
		 */
		do
		{
			boolean obVorhandenerStein = false;
			boolean obVorhandenerZug = false;

			/*
			 * hier wird gewartet auf einen vollständigen mouseClick für
			 * Zugauswahl und Steinauswahl
			 */
			do
			{
				gewaelterzug = gibtAuswahlZug ();
				gewaelterStein = gibAuswahlStein ();
				pause ();

			} while (gewaelterzug[0] == 0);

			ausgabeNachricht ("gewählter Stein:" + gewaelterStein[0] + ""
					+ gewaelterStein[1]);
			ausgabeNachricht ("gewählter Zug  :" + gewaelterzug[0] + ""
					+ gewaelterzug[1]);

			/*
			 * Hier wird geprüft, ob dort wo für einen ausgewählten Stein
			 * gedrückt wurde, auch wirklich ein Stein existiert.Wenn ja wird
			 * dieser gespeichert, um ihn nachher mit dem Array,
			 * moeglicheZuege, zu vergleichen
			 */
			for (int i = 0; i < 48; i = i + 4)
			{
				if ( (SpielerSteinPos[i + 2] == gewaelterStein[0])
						&& (SpielerSteinPos[i + 3] == gewaelterStein[1]))
				{
					steinNummer = SpielerSteinPos[i + 1];
					// ausgabeMessages("gefundene  Steinnummer. im SteinArray :"+steinNummer);
				}
			}

			/* Vergleich steinNummer in moeglicheZuege */
			for (int i = 0; i < 24; i += 4)
			{

				if (moeglicheZuege[i + 2] != 0)
				{/*
					 * verhindert das obvorhandenerStein=true, wenn die gefundene
					 * steinNummer=0 und die andere Hälfte des arrays nur 0'en
					 * enthält
					 */

					if (steinNummer == moeglicheZuege[i + 1])
					{
						// ausgabeMessages("gefunde Steinnummer in moegliche Zuege: "+steinNummer+"\n moglZuege:"+moeglicheZuege[i+1]);
						obVorhandenerStein = true;
					}
				}
			}

			/* Vergleich gewaelterZug in moeglicheZuege */
			for (int i = 0; i < 24; i += 4)
			{
				if (moeglicheZuege[i + 2] != 0)
				{
					if ( (gewaelterzug[0] == moeglicheZuege[i + 2])
							&& (gewaelterzug[1] == moeglicheZuege[i + 3]))
					{
						obVorhandenerZug = true;
					}
				}
			}// For

			/*
			 * Sucht die Position an der die steinNummer und der gewaelterZug
			 * in moeglicheZuege übereinstimmen und speichert dies in
			 * auswahlmöglickeit
			 */
			for (int i = 0/* j=1 */; i < 24; i += 4)
			{

				if ( (steinNummer == moeglicheZuege[i + 1])
						&& (gewaelterzug[0] == moeglicheZuege[i + 2])
						&& (gewaelterzug[1] == moeglicheZuege[i + 3]))
				{
					auswahlmoeglickeit = i;
					// ausgabeMessages("Auswahlmoeglickeit:"
					// +auswahlmoeglickeit);
				}
				// else
				// ausgabeMessages("Auswahlmoeglickeit wurde nicht gefunden");
			}// For

			/*
			 * Entscheidung ob der Zug gültig ist oder nichtEs war nötig diese
			 * Prüfung in 2 schleifen zu teilen, und nicht alles auf ein Mal.
			 */
			if (obVorhandenerStein && obVorhandenerZug)
			{
				ausgabeNachricht ("Richtige Auswahl");
				ergebnis = true;
				/*
				 * bei ergebnis=true erste do while schleife kann jetzt
				 * abgebrochen werden, da der Mausklick für steinund zug Gültig
				 * ist mit den vorhandenen Möglickeiten
				 */

				/*
				 * Alle Koordinaten müssen zurückgesetzt werden auf 0, damit für
				 * den nächsten aufruf der Methode nächsterZug, die Prüfungen in
				 * den do while schleifen mit richtigen Werten beginnen können.
				 */
				gewaelterzug[0] = 0;
				gewaelterzug[1] = 0;
				gewaelterStein[0] = 0;
				gewaelterStein[1] = 0;

				steinKoordinate[0] = 0;
				steinKoordinate[1] = 0;
				zugKoordinate[0] = 0;
				zugKoordinate[1] = 0;
			}
			else
			{
				ausgabeNachricht ("Falsche Auswahl");
				/*
				 * Alle Koordinaten müssen zurückgesetzt werden auf 0, damit die
				 * kleine do while schleife innerhalb der große do while Schleife
				 * in dieser Methode richtige neue Werte bekommt
				 */
				gewaelterzug[0] = 0;
				gewaelterzug[1] = 0;
				gewaelterStein[0] = 0;
				gewaelterStein[1] = 0;

				steinKoordinate[0] = 0;
				steinKoordinate[1] = 0;
				zugKoordinate[0] = 0;
				zugKoordinate[1] = 0;

				/*
				 * Methode mouseClicked kann wieder neue Werte für die
				 * steinKoordinate und zugKoordinate übernehmen
				 */
				spielSperreAuswahlStein = true;
				spielSperreAuswahlZug = true;
			}

		} while (!ergebnis);

		switch (auswahlmoeglickeit)
		{
		case 0:
		{
			zug = 1;
			break;
		}
		case 4:
		{
			zug = 2;
			break;
		}

		case 8:
		{
			zug = 3;
			break;
		}
		case 12:
		{
			zug = 4;
			break;
		}

		case 16:
		{
			zug = 5;
			break;
		}

		case 20:
		{
			zug = 6;
			break;
		}

		default:
			ausgabeNachricht ("FEHLER, Ausgewählter Zug gibt es nicht");
			break;
		}// switch

		return zug;
	}// ENDE naechsterZug

	public void mouseClicked (MouseEvent ML)
	{
		int pixelKoordinateZeile;
		int pixelKoordinateSpalte;
		int [] koordinaten = new int [2]; /*
																			 * Ist das array mit den
																			 * Umwandelden koordinatenin Zeile
																			 * und Spalte, je nachdem wo der
																			 * Spielerauf dem Spielbrett klickt
																			 */

		pixelKoordinateSpalte = ML.getX ();
		pixelKoordinateZeile = ML.getY ();

		koordinaten[0] = pixelKoordinateZeile;
		koordinaten[1] = pixelKoordinateSpalte;

		int [] neueKoord = mausKlickKonvertierung (koordinaten);

		if (spielSperreAuswahlStein)
		{
			steinKoordinate = neueKoord;
			spielSperreAuswahlStein = false;
			ausgabeNachricht ("1.Geklicktes Feld: " + neueKoord[0] + ""
					+ neueKoord[1]);
		}
		else if (spielSperreAuswahlZug)
		{
			zugKoordinate = neueKoord;
			spielSperreAuswahlZug = false;
			ausgabeNachricht ("2.Geklicktes Feld: " + neueKoord[0] + ""
					+ neueKoord[1]);
		}

	}// ENDE MouseClicked

	/* Konvertierung der Einzelkoordinate zu Feldkoordinate */
	public int [] mausKlickKonvertierung (int [] koordinaten)
	{
		int koordinatenArray[] = new int [2];
		int zeile;
		int spalte;
		int pixelProFeld = 94;

		zeile = Math.round (koordinaten[0] / pixelProFeld);
		spalte = Math.round ( (koordinaten[1] - 60) / pixelProFeld);

		koordinatenArray[0] = zeile + 1;
		koordinatenArray[1] = spalte + 1;

		return koordinatenArray;
	}

	/* gibt den Array zurück für die geklickte steinKoordinate zurück */
	public int [] gibAuswahlStein ()
	{
		return steinKoordinate;
	}

	/* gibt den Array zurück für die geklickte zugKoordinate zurück */
	public int [] gibtAuswahlZug ()
	{
		return zugKoordinate;
	}

	/* benötigte Methode für den MouseListener */
	public void mousePressed (MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseReleased (MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseEntered (MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited (MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}