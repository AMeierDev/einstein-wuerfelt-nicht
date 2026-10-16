package spiel;


import java.awt.*;

import javax.swing.text.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.*;
import javax.swing.*;
;

/**
 * @author Chikage
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpielMenu extends JFrame{
	
	public SpielMenu(){
	  setSize(850,750);
      setVisible(true);
      setResizable(false);
      setTitle("Einstein wuerfelt nicht");
      addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
          System.exit(0);
      }
      }
      );
      
	}
}
