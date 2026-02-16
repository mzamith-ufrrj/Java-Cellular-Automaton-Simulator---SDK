package br.ufrrj.dcc.ca;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;



public class GUI1DCA extends JPanel{
	private float 	mScaleX = 0.0f,
					mScaleY = 0.0f,
					mWidth = 0.0f,
				    mHeight = 0.0f;    

    private GUI1DCA mPtr = null;
    public GUI1DCA(){
        super();
        mPtr = this;
        mWidth = 858.0f;  mHeight = 530.0f;
        setPreferredSize(new Dimension((int)mWidth, (int)mHeight));
		setBackground(Color.BLACK);
		setAlignmentX(Component.CENTER_ALIGNMENT);
		//Window event listener
		addComponentListener(new MyJPanelEvent());
		addMouseListener(new MyMouseListener());
    }

     protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        drawGrid( (Graphics2D) g);
    }//protected void paintComponent(Graphics g) {

    private void drawGrid(Graphics2D g2d) {

    	g2d.setBackground(Color.black);
        /*
    	if (!mHasCA) return;
 
    	for (int j = 0; j < mCA.getHeight(); j++) {
    		for (int i = 0; i < mCA.getWidth(); i++) {
    			int s = mCA.getStateCell(i, j);
    			if (s > 0) {
    				g2d.setColor(COLOR[s]);
    				g2d.fill(new Rectangle2D.Float(i * mScaleX, j * mScaleY, mScaleX, mScaleY ));
    			}
    				
    		}//for (int i = 0; i < mCA.getWidth(); i++) {
    	}//for (int j = 0; j < mCA.getHeight(); j++) {
        */


    }//drawgrid

    private class MyMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("mouseClicked:" + arg0.getX() + "," + arg0.getY());
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("mouseEntered");
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("mouseExited");
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("mousePressed");
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.println("mouseReleased");
		}
    	
    }
    private class MyJPanelEvent implements ComponentListener{

		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			mWidth = e.getComponent().getSize().width;
			mHeight = e.getComponent().getSize().height;
			
			
		
	    	repaint();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
    	
    }
}
