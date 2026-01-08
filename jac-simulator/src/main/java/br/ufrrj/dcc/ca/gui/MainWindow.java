/**
 * 
 */
package br.ufrrj.dcc.ca.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ufrrj.dcc.ca.models.gui.*;
import br.ufrrj.dcc.ca.models.logic.GameOfLife;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * 
 */
public class MainWindow  extends JFrame {
		private static final int BOUNDARY = 50;
		 
		
		 
		private static final int EXIT_CLOSE				= 0;
		public static final int GAME_OF_LIFE   			= 1;
	 	private static final int ABOUT          		= 2;
	
	    private JMenuBar mMenuBar = null;
	    private JMenu    mMenu    = null,
	                     mSubmenu = null;
	    private JMenuItem mMenuItem = null;
	    
	    private JDesktopPane mPainel =  null;
	    
		private Vector<Integer> mMenuHash = null;

		private Map<String, SimpleCA2DGUI> mMapCA2D = null;
	    
    	public MainWindow() {
            this.setTitle("Cellular automata simulation");
            //this.setExtendedState(JFrame.MAXIMIZED_BOTH); // To maximize a frame
            //int bordas = 50;
            //--------------------------------------------------------------------------------
			//brefore menu creation - Attention when you load jar file
			mMapCA2D = new HashMap<>();
			mMapCA2D.put("Game of Life Version 2.0", new GameOfLifeGUI());
			
			mMenuHash = new Vector<Integer>();

			//--------------------------------------------------------------------------------

            Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(BOUNDARY, BOUNDARY, tela.width  - BOUNDARY * 2 , tela.height - BOUNDARY * 2);
            
            mPainel = new JDesktopPane(); 
            mPainel.setBackground(Color.LIGHT_GRAY);
            this.setContentPane(mPainel);
            this.createMenu();
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}//public MainWIndow() {
    	

    	
		 
		private void createMenu() {
			mMenuHash.clear();
            //int index = 0;
            
            MenuEvent menuEvent = new MenuEvent();
            mMenuBar = new JMenuBar();
            mMenu    = new JMenu("Simulation");
            mMenuBar.add(mMenu);

//--------------------------------------------------------------------
            mSubmenu = new JMenu("Autômatos Celulares 2D");

			for (Map.Entry<String, SimpleCA2DGUI> entry : mMapCA2D.entrySet()) {
				String chave = entry.getKey();
				mMenuItem = new JMenuItem(chave);
				mMenuItem.addActionListener(menuEvent);
				mMenuHash.add(mMenuItem.hashCode());
				mSubmenu.add(mMenuItem);
			}
			
            mMenu.add(mSubmenu);
			
            mMenu.addSeparator();
            mMenuItem = new JMenuItem("Exit");
            mMenuItem.addActionListener(menuEvent);
            mMenuHash.add(mMenuItem.hashCode());
            mMenu.add(mMenuItem);

            mMenu    = new JMenu("About");
            mMenuHash.add(mMenuItem.hashCode());
            mMenuBar.add(mMenu);
            
            this.setJMenuBar(mMenuBar);
    	}
    	
    	
    	/*
    	 * Class listens menu click event
    	 */
        private class MenuEvent implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
				boolean found = false;
				int i = 0;
				int option = -1;
				while ((i < mMenuHash.size()) && (!found)){
					Integer opt = mMenuHash.get(i);
					if (e.getSource().hashCode() == opt){
                        option = i;
                        found = true;
                    }
					i++;
				}
				if (option == mMenuHash.size() - 2){
					System.exit(0);
					return;
				}

				if (option == mMenuHash.size() - 1){
					JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				i = 0;
				for (Map.Entry<String, SimpleCA2DGUI> entry : mMapCA2D.entrySet()) {
					String chave = entry.getKey();
					SimpleCA2DGUI valor = entry.getValue();
					if (option == i){
						valor.init_component();
						break;
					}
					
				}
				System.out.println("All your bases are belong to us!");
				/*
            	JFileChooser fileChooser = null;
            	FileNameExtensionFilter filter = null;
                int option = -1;
                for (int i = 0; i < ITENS; i++){
                    if (e.getSource().hashCode() == mMenuHash[i]){
                        option = i;
                        i = ITENS + 1;
                    }
                }
                switch (option){
                
                	case EXIT_CLOSE:
                	case ABOUT:JOptionPane.showMessageDialog(null,"ABOUT","TITULO", JOptionPane.INFORMATION_MESSAGE);break;
                
                	//case GAME_OF_LIFE:call_game_of_life();break;

                }
					 */
            }//end-public void actionPerformed(ActionEvent e) {


        }//end- private class MenuEvent implements ActionListener{	
}//public class MainWIndow  extends JFrame {
