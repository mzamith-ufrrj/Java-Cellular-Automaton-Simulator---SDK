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
import java.awt.geom.Rectangle2D;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.ufrrj.dcc.ca.models.one.Elementary;



public class GUI1DCA extends JPanel{
	private float 	mScaleX = 0.0f,
					mScaleY = 0.0f,
					mWidth = 0.0f,
				    mHeight = 0.0f;    

    private GUI1DCA mPtr = null;
	private Elementary mElementaryCA = null;

	public void setElementaryCA(Elementary ca){
		mElementaryCA = ca;
		printMesh();
	}


	private void printMesh() {
		if (mElementaryCA == null) return;

		float w = (float) mElementaryCA.getX_Len();
		float h = (float) mElementaryCA.getTimestep();
		mScaleX = mWidth / w;
		mScaleY = mHeight / h;	
		repaint();
		repaint();
	}//public void loadMesh() {
    public GUI1DCA(){
        super();
        mPtr = this;
        mWidth = 1020.0f;  mHeight = 700.0f;
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
		if (mElementaryCA == null) return;

 
    	for (int j = 0; j < mElementaryCA.getTimestep(); j++) {
    		for (int i = 0; i < mElementaryCA.getX_Len(); i++) {
    			int s = mElementaryCA.getStateCell(i, j);
    			if (s > 0) {
    				g2d.setColor(Color.GREEN);
    				g2d.fill(new Rectangle2D.Float(i * mScaleX, j * mScaleY, mScaleX, mScaleY ));
    			}
    				
    		}//for (int i = 0; i < mCA.getWidth(); i++) {
    	}//for (int j = 0; j < mCA.getHeight(); j++) {


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
			if (mElementaryCA ==  null) return;
			mScaleX = mWidth / (float) mElementaryCA.getX_Len();
			mScaleY = mHeight / (float) mElementaryCA.getTimestep();	
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
