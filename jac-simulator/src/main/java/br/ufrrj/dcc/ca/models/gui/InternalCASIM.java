package br.ufrrj.dcc.ca.models.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;

import br.ufrrj.dcc.ca.models.logic.SimpleCA2DModel;

public class InternalCASIM extends JInternalFrame{
    private static final int ITENS  			= 4;
	private static final int NEXT_STEP 			= 0;
	private static final int RUN_CA       		= 1;
	private static final int MODEL_CA_BG	    = 2;
	private static final int INIT_CA     	    = 3;
    
    private InternalCASIM mPtr = null;
    private int []mMenuHash;
    private JMenuBar mMenuBar = null;
    private JMenuItem mMenuItem = null;
    private JMenu     mMenu     = null;
                     // mSubmenu  = null;
    
    private JList<String>mLayerList  = null;
    private DefaultListModel mLayerModel = null;
    //private Vector<Layer>mLayers = null;
    private GUICA mGUICA = null;
    private SimpleCA2DModel mCA = null;
    																																																																																																														
    
    private BGThread mThreadBG = null;
    private FGThread mThreadFG = null;
    private JDesktopPane mPainel =  null;

    public InternalCASIM (SimpleCA2DModel ca, int w, int h, JDesktopPane p){
        this.mCA = ca;
        this.mPainel = p;
		this.mPtr = this;
        createMenu(); 
    }

    public SimpleCA2DModel getModel() { return this.mCA; }

    private void createMenu() {
		mMenuHash = new int[ITENS];
         
        
        MenuEvent menuEvent = new MenuEvent();
        mMenuBar = new JMenuBar();
//--------------------------------------------------------------------------------------------------        
        mMenu    = new JMenu("Commands");
        mMenuBar.add(mMenu);
 
        mMenuItem = new JMenuItem("initial condition");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[INIT_CA] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("run fg/stop");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[RUN_CA] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("run bg/stop");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[MODEL_CA_BG] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);// index = 3

        mMenuItem = new JMenuItem("next step");
        mMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        mMenuItem.addActionListener(menuEvent);
        mMenuHash[NEXT_STEP] = mMenuItem.hashCode();
        mMenu.add(mMenuItem);
        this.setJMenuBar(mMenuBar);
//--------------------------------------------------------------------------------------------------
	}

    private void runSimulationFG() {
    	if (mThreadFG == null) {
    		mThreadFG = new FGThread(mCA, mGUICA);
    		mThreadFG.start();
    	}else {
    		mThreadFG.setIsRunningFalse();
    		try {
    			mThreadFG.join();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		mThreadFG = null;
    	}
    }//private void runSimula


    //--------------------------------------------------------------------------------------------------
    //Private classes
      private class BGThread extends Thread{
    	protected boolean mIsRunning = false;
    	public synchronized void setIsRunningTrue() {mIsRunning = true;}
    	public synchronized void setIsRunningFalse() {mIsRunning = false;}
    	public synchronized boolean getIsRunning() {return mIsRunning;}
    	protected SimpleCA2DModel mCA = null;
    	protected GUICA mGUICA = null;
    	private int mTS = -1;
    	public BGThread(SimpleCA2DModel ca, GUICA guica, int ts) {
    		super();
    		this.mCA = ca;
    		this.mGUICA = guica;
    		this.mTS = ts;
    		
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.setIsRunningTrue();
			int ts = 0;
			
			JFrame frame = new JFrame("Simulation progress");
			JProgressBar progressbar = new JProgressBar(0, this.mTS);
			progressbar.setStringPainted(true);
			frame.add(progressbar);
		    
		    frame.setSize(400, 60);
		    frame.setVisible(true);
		    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    
		    int w = (int)(screenSize.getWidth() / 2) - 200;
		    int h = (int)(screenSize.getHeight() / 2) - 30;
		    frame.setResizable(false);
		    frame.setLocation(w, h);
		    
		    
			while(this.getIsRunning() && ts < this.mTS) {
				this.mCA.update();
				progressbar.setValue(ts);
				ts++;
			}

			progressbar.setValue(ts);
			frame.setVisible(false);
			frame = null;
			this.mGUICA.repaint();
		}
     
    }//private class BGThread extends Thread{
    
    /*! \class FGThread
		\brief FGThread computes Cellular Automata in foreground. It updates JPanel lattice state for each time step
    */
    private class FGThread extends BGThread{
    	
		public FGThread(SimpleCA2DModel ca, GUICA guica) {
    		super(ca, guica, 0);
    		
    	}
	
		@Override
		public void run() {
			this.setIsRunningTrue();
			while(this.getIsRunning()) {
				this.mCA.update();
        		this.mGUICA.repaint();
        		//System.out.println(i);
        		try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}//public void run() {
    	
    }//private class FGThread extends BGThread{

    /*! \class LogPanel
		\brief LogPanelEvent is used to personalize the window and allowing it to resize
    */
    private class LogPanel extends JPanel implements ComponentListener{

    	public LogPanel() {
    		this.addComponentListener(this);
    	}
		@Override
		public void componentHidden(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentMoved(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentResized(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			Component c = (Component)arg0.getSource();
			System.out.println("LogPanel");
			System.out.println(c.getWidth());
			System.out.println(c.getHeight());
			Dimension d = new Dimension(c.getWidth()-12, c.getHeight()-30);
			mLayerList.setSize(d);
			mLayerList.setMinimumSize(d);
			mLayerList.setMaximumSize(d);
			
		}

		@Override
		public void componentShown(ComponentEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
    private class MenuEvent implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            int option = -1;
            for (int i = 0; i < ITENS; i++){
                if (e.getSource().hashCode() == mMenuHash[i]){
                    option = i;
                    i = ITENS + 1;
                }
            }
    
            switch (option){
            
                
                case NEXT_STEP://	JOptionPane.showMessageDialog(null,"next step","TITULO", JOptionPane.INFORMATION_MESSAGE);
                		if (mCA != null) {
                			mCA.update();
                    		mGUICA.repaint();	
                		}
                		
                	break;
                case RUN_CA:
                	if (mCA != null) {
                		runSimulationFG();	
            		}
                	break;
                	
                case INIT_CA:
                	if (mCA != null) {
                		mCA.initialCondition();
                		mGUICA.loadMesh();
                		mGUICA.repaint();
            		}
                	
                	break;
                case MODEL_CA_BG:
                	runSimulationBG();
                	/*
                	mCA = new models.GameOfLife();
                	mCA.initialCondition();
                	mGUICA.setCellularAutomataModel(mCA);
                	setTitle("Game of life model");
                	*/
                	break;
                	
                //case 4:JOptionPane.showMessageDialog(null,"MENSAGEM AQUI","TITULO", JOptionPane.INFORMATION_MESSAGE);;break;

            }
            

        }//end-public void actionPerformed(ActionEvent e) {
    }//private class MenuEvent implements ActionListener{
}
