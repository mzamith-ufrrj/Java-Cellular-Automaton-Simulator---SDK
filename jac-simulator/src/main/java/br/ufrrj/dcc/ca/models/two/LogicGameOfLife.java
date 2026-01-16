package br.ufrrj.dcc.ca.models.two;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class LogicGameOfLife extends LogicSimpleCA2D {
	private final int DEAD = 0;
	private final int LIVE = 1;
	private int [][]mMoore = null;	

	private Map<String, StringBuilder> mLogLayers = null;
	private Vector<Integer> mLive, mDead;
	
	public LogicGameOfLife(int w, int h, int s, String boundary) {
		super(w, h, s, boundary);
		mMoore = new int[3][3];
		
		mLive = new Vector<Integer>();
		mDead = new Vector<Integer>();

		mLogLayers = new HashMap<>();

		StringBuilder sb = new StringBuilder();
		mLogLayers.put("Simulation setup", sb);
		sb = new StringBuilder();
		mLogLayers.put("Simulation data", sb);
		
	}
	

	@Override
	public void initialCondition() {
		System.out.println("initial condition");
		mCAState = CLASS_STATE_INITIAL_CONDITION;
		mLive.clear();
		mDead.clear();
		int clive = 0, cdead = 0;
		for (int j = 0; j < mHeight; j++)
			for (int i = 0; i < mWidth; i++) {
				if (Math.random() < 0.25f) {
					mS0[j][i] = LIVE;
					mS1[j][i] = LIVE;
					clive++;
				}else {
					mS0[j][i] = DEAD;
					mS1[j][i] = DEAD;
					cdead++;
				}
			}//for (int i = 0; mCellX * mCellY; i++) {
		
		mLive.add(clive);
		mDead.add(cdead);
	}//public void initialCondition() {
	
	@Override
	public void update() {
		if (mCAState == CLASS_STATE_INITIAL_CONDITION)
			mCAState = CLASS_STATE_UPDATE;
		int sum;
	    /*
        nw | n | ne
       ----|---|----
        w  | c |  e
       ----|---|----
        sw | s | se
	     */	
		int a = 0, b = 0;
		for (int j = 0; j < mHeight; j++) {
			for (int i = 0; i < mWidth; i++) {
				nw = n = ne = w = e = sw = s =  se = c = -1;
				sum = 0;
				c = mS0[j][i];
				
				if (mBoundary.compareTo("periodic") == 0) periodicBoundary(i, j);
				else if (mBoundary.compareTo("reflexive") == 0) reflexiveBoundary(i, j);
				else if (mBoundary.compareTo("constant [0]") == 0) constantBoundary(i, j, 0);
				else if (mBoundary.compareTo("constant [1]") == 0) constantBoundary(i, j, 1);

				/*
				if (nw == -1) {System.err.println("Error"); System.exit(-1);}
				if (n == -1) {System.err.println("Error"); System.exit(-1);}
				if (ne == -1) {System.err.println("Error"); System.exit(-1);}
				if (w == -1) {System.err.println("Error"); System.exit(-1);}
				if (e == -1) {System.err.println("Error"); System.exit(-1);}
				if (sw == -1) {System.err.println("Error"); System.exit(-1);}
				if (s == -1) {System.err.println("Error"); System.exit(-1);}
				if (se == -1) {System.err.println("Error"); System.exit(-1);}
				*/
				
				sum = nw + n + ne + w + e + sw + s + se;
				if ((sum == 3) && (c == 0)) {
	            	  mS1[j][i] = LIVE;
	            	  a++;
				}else if ((sum >= 2) && (sum <= 3) && (c == 1)) {
	            	  mS1[j][i] = LIVE;
	            	  a++;
				}else {
	            	  mS1[j][i] = DEAD;
	            	  b++;
				}
			
				
			}//for (int i = 0; i < mCellX; i++) {
		}//for (int j = 0; j < mCellX; j++) {
		super.update();
		mLive.add(a);
		mDead.add(b);
		
	}//public void update() {
	
	@Override
	public List<String> getLayers() {return new ArrayList<>(mLogLayers.keySet());  }
	
	@Override
	public StringBuilder getLogBasedOnLayer(String key) { 
		return mLogLayers.get(key);
	}
 
	
	
}
