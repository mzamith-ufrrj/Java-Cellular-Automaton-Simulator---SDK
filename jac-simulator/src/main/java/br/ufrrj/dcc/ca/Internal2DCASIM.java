package br.ufrrj.dcc.ca;


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
//import java.io.File;
//import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
//import java.util.Locale;
//import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
//import javax.swing.JComboBox;
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
//import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
//import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
//import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
//import javax.swing.event.MenuEvent;

import br.ufrrj.dcc.ca.models.two.LogicSimpleCA2D;

public class Internal2DCASIM extends JInternalFrame{
    private static final int ITENS  			= 4;
	private static final int NEXT_STEP 			= 0;
	private static final int RUN_CA       		= 1;
	private static final int MODEL_CA_BG	    = 2;
	private static final int INIT_CA     	    = 3;
    
    private Internal2DCASIM mPtr = null;
    private int []mMenuHash;
    private JMenuBar mMenuBar = null;
    private JMenuItem mMenuItem = null;
    private JMenu     mMenu     = null;
                     // mSubmenu  = null;
    
    private JList<String>mLayerList  = null;
    private DefaultListModel mLayerModel = null;
    //private Vector<Layer>mLayers = null;
    private GUI2DCA mGUI2DCA = null;
    private LogicSimpleCA2D mCA = null;
	private String mModelName = null;
    
    private BGThread mThreadBG = null;
    private FGThread mThreadFG = null;
    private JDesktopPane mPainel =  null;

    public Internal2DCASIM (String modelName){ 
		super(modelName, true, true, true, false);
		mModelName = new String(modelName);
		this.mPtr = this; 
		closeFrameEvent();
	}

	public String getModelName() { return mModelName; };
	public void setCAModel(LogicSimpleCA2D ca){ this.mCA = ca;}
	public void init_window(JDesktopPane p){
        
		boolean ret = this.mCA.settings();
		if (ret){
			this.mPainel = p;
			
			createMenu(); 
			
			this.getContentPane().add(createLayer(), BorderLayout.LINE_START);
			this.getContentPane().add(createLattice(), BorderLayout.CENTER);
			
			//this.pack();
			this.setLocation(30, 30);
			this.setSize(1046, 700);
			this.setVisible(true);
			this.show();
			mGUI2DCA.setCellularAutomataModel(this.mCA);
		}

	}

    public LogicSimpleCA2D getModel() { return this.mCA; }

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

	 /*
     * Creates layer GUI component - It is a List
     * 
     */
    private JPanel createLayer() {
    	int w = 140,
    	    h = 530;
    	
    	mLayerModel =  new DefaultListModel();
    	List<String> layers = mCA.getLayers();
		for (String layer : layers) {
 		   mLayerModel.addElement(new String(layer));
		}

        mLayerList = new<String> JList();
		mLayerList.setModel(mLayerModel);
		
		
		
		Dimension d = new Dimension(w, h);
		mLayerList.setSize(d);
		mLayerList.setMinimumSize(d);
		mLayerList.setMaximumSize(d);
		mLayerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//mLayerList.addMouseListener(null);
		mLayerList.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            if (e.getButton() == MouseEvent.BUTTON3) {
	                JPopupMenu menu = new JPopupMenu();
	                JMenuItem item = new JMenuItem("Data");
	                
	                item.addActionListener(new ActionListener() {
	                    public void actionPerformed(ActionEvent e) {
							String text = mLayerList.getModel().getElementAt(mLayerList.getSelectedIndex());
							System.out.println("Your option was: " + Integer.toString(mLayerList.getSelectedIndex()) + " -> value: " + text);
							Internal2DCAData swd = new Internal2DCAData(text);
							mPainel.add(swd);
	                    	swd.toFront();
							text = new String(mCA.getLogBasedOnLayer(text));
							swd.setLog(text);
							//Change here 2026/01/16
							/*
							
	                    	mPainel.add(swd);
	                    	swd.toFront();
	                    	swd.setLog(mCA.getLogBasedOnLayer(mLayerList.getSelectedIndex()));
							 */
	                    	
	                    }//public void actionPerformed(ActionEvent e) {
	                });// item.addActionListener(new ActionListener() {
	                menu.add(item);
	                //tem erro aqui, possivelmente colocar a Jframe pai deste item no lugar
	                
	                menu.show(mPtr, e.getX()+10, e.getY()+60);
	                //menu.show(this, 5, mLayerList.getCellBounds(
	                //		mLayerList.getSelectedIndex() + 1,
	                //		mLayerList.getSelectedIndex() + 1).y);
	            }
	        }
	    });
		
		/*
		mLayerList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {
				// TODO Auto-generated method stub
				if (!event.getValueIsAdjusting()){
					int a = event.getFirstIndex();
					int b = event.getLastIndex();
		            JList source = (JList)event.getSource();
		            System.out.println("!getValueIsAdjusting");
		            System.out.println(source.getSelectedValue().toString());
		            System.out.println(a);
		            System.out.println(b);
		        }else {
		        	int a = event.getFirstIndex();
					int b = event.getLastIndex();
		            JList source = (JList)event.getSource();
		            System.out.println("getValueIsAdjusting");
		            System.out.println(source.getSelectedValue().toString());
		            System.out.println(a);
		            System.out.println(b);
		        }
			}
			
		});
		*/
		LogPanel panel = new LogPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		mLayerList.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Layers/Attributes"));
		panel.add(Box.createVerticalStrut(2));
        panel.add(mLayerList);
        panel.add(Box.createVerticalStrut(2));
        d = new Dimension(w+5, h+5);
        panel.setSize(d);
        panel.setMinimumSize(d);
        panel.setMaximumSize(d);
        panel.setPreferredSize(d);

        d = null;
        return panel;
        
    }
    /*
     * Creates layer GUI components
     */
    private JPanel createLattice() {
    	JPanel panel = new JPanel();
    	
    	GUI2DCA p1 = new GUI2DCA();
    	mGUI2DCA = p1;
		
		//mGUIA.setCellularAutomataModel(new models.CellularAutomataModel());
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder( BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Lattice"));
		panel.add(Box.createVerticalStrut(2));
        panel.add(p1);
        panel.add(Box.createVerticalStrut(2));

        //BoxLayout.PAGE_AXIS -> horizontal orientation
		//BoxLayout.LINE_AXIS -> vertical orientation
		return panel;
        
    }
    private void runSimulationFG() {
    	if (mThreadFG == null) {
    		mThreadFG = new FGThread(mCA, mGUI2DCA);
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
    	protected LogicSimpleCA2D mCA = null;
    	protected GUI2DCA mGUI2DCA = null;
    	private int mTS = -1;
    	public BGThread(LogicSimpleCA2D ca, GUI2DCA guica, int ts) {
    		super();
    		this.mCA = ca;
    		this.mGUI2DCA = guica;
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
			this.mGUI2DCA.repaint();
		}
     
    }//private class BGThread extends Thread{
    
    /*! \class FGThread
		\brief FGThread computes Cellular Automata in foreground. It updates JPanel lattice state for each time step
    */
    private class FGThread extends BGThread{
    	
		public FGThread(LogicSimpleCA2D ca, GUI2DCA guica) {
    		super(ca, guica, 0);
    		
    	}
	
		@Override
		public void run() {
			this.setIsRunningTrue();
			while(this.getIsRunning()) {
				this.mCA.update();
        		this.mGUI2DCA.repaint();
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


    private void runSimulationBG() {
    	
    	if (mThreadBG != null) {
    		boolean ret =  mThreadBG.isAlive();
    		mThreadBG.setIsRunningFalse();
    		try {
    			mThreadBG.join();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		mThreadBG = null;
    		if (ret) return;
    	}//if (mThreadBG != null) {
    	
    	NumberFormat format = NumberFormat.getInstance();
    	format.setGroupingUsed(false);
	    NumberFormatter formattert = new NumberFormatter(format);
	    formattert.setValueClass(Integer.class);
	    formattert.setMinimum(0);
	    formattert.setMaximum(Integer.MAX_VALUE);
	    formattert.setAllowsInvalid(false);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formattert.setCommitsOnValidEdit(true);

	    JFormattedTextField timesteps = new JFormattedTextField(formattert);
	    timesteps.setText("1000");
		
		JComponent[] inputs = new JComponent[] {
		        new JLabel("Timesteps: "),timesteps,
		};
		
		int result = JOptionPane.showConfirmDialog(null, inputs, "CA in background timesteps", JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
		    System.out.println("Your options:" + timesteps.getText());
		    int ts = Integer.parseInt(timesteps.getText());
		    mThreadBG = new BGThread(mCA, mGUI2DCA, ts);
		    mThreadBG.start();
		} else {
		    System.out.println("User canceled / closed the dialog, result = " + result);
		    mThreadBG = null;
		}
    }//private void runSimulationBG() {
    
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
                    		mGUI2DCA.repaint();	
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
                		mGUI2DCA.loadMesh();
                		mGUI2DCA.repaint();
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

	private void closeFrameEvent() {
    	 addInternalFrameListener(new InternalFrameAdapter(){
             public void internalFrameClosing(InternalFrameEvent e) {
            	 mPtr.dispose();
            	 //mPainel.remove(mPtr);
             }
         });
    }

}
