package br.ufrrj.dcc.ca;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MenuEvent;

public class Internal1DCA extends JInternalFrame{
    private Internal1DCA mPtr = null;
    private JDesktopPane mPainel =  null;
    private int []mMenuHash;
    private JMenuBar mMenuBar = null;
    private JMenuItem mMenuItem = null;
    private JMenu     mMenu     = null;

    private static final int ITENS  			= 4;
	private static final int NEXT_STEP 			= 0;
	private static final int RUN_CA       		= 1;
	private static final int MODEL_CA_BG	    = 2;
	private static final int INIT_CA     	    = 3;
    private GUI1DCA mGui1DCA = null;
    public Internal1DCA(JDesktopPane p){
        super("Elememtary 1D", true, true, true, false);
        this.mPainel = p;
		mPtr = this;
        this.setLocation(30, 30);
        this.setSize(1046, 700);
        this.setVisible(true);
        this.show();
		createMenu(); 
        JPanel panel = new JPanel();
        mGui1DCA = new GUI1DCA();
        panel.add(mGui1DCA);
        this.getContentPane().add(panel);

		closeFrameEvent();
    }


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


    private class MenuEvent implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
         
            

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
