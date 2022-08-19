//******************************************************************************
// -----------------------------------------------------------
// JVMine.java		                 
// -----------------------------------------------------------
//   Main Applet class for Demineur JVMine Applet
// -----------------------------------------------------------
// Author : R. BERTHOU
// E-Mail : rbl@berthou.com
// URL    : http://www.javaside.com
// -----------------------------------------------------------
// Ver  * Author     *  DATE    * Description
// ....................DD/MM/YY...............................
// 1.02 * R. BERTHOU * 28/01/99 * Change mail address
// 1.01 * R. BERTHOU * 28/08/97 * Correct minor bug
// 1.00 * R. BERTHOU * 31/07/97 * Creation 
//******************************************************************************
import java.applet.Applet;
import java.util.Random ;
import java.awt.*;

import java.net.URL;
import java.net.MalformedURLException;

//==============================================================================
// Main Class
//==============================================================================
public class JVMine extends  java.applet.Applet
{
	//--------------------------------------------------------------------------
	private int[]	    bStr ;
	private int			iEtat ;
	private int			iCpt ;

	private	Image		buffer;
	private	Graphics	offScreen;
	private long		startTick ;

	private Image[]		img ;		

	private int aWidth, aHeight;

	private int		m_bgcolor = 0 ;
	public  int		m_mine = 99 ;
	public  int		m_ligne = 16 ;
	public  int		m_col = 30 ;
	private int     iMax ;

	// Font control variables
	private Font wFont;
	private FontMetrics wMetrics;
	private int	 iPas, iX ;

    //--------------------------------------------------------------------------
	public String getAppletInfo()
	{
		return "Name: JVMine V 1.02\r\n" +
		       "Author: R. BERTHOU\r\n" +
			   "E-Mail : rbl@berthou.com\r\n" +
			   "URL : http://www.javaside.com/" ;
	}

    //--------------------------------------------------------------------------
	public String[][] getParameterInfo()
	{
		String[][] info =
		{
			{ "bgImg",	"String",	"bgImage" },
			{ "bgcolor", "int",		"bgColor" },
			{ "nbmine",  "int",		"nb de mine" },
			{ "nbcol",	 "int",		"nb de col" },
			{ "nbligne", "int",		"nb de ligne" },
			{ "font",	"String",	"Font Name" },
			{ "fontsize", "int",	"Font Size" },
		};
		return info;		
	}


    //--------------------------------------------------------------------------
	// The init() method is called by the AWT when an applet is first loaded or
	// reloaded.  Override this method to perform whatever initialization your
	// applet needs, such as initializing data structures, loading images or
	// fonts, creating frame windows, setting the layout manager, or adding UI
	// components.
    //--------------------------------------------------------------------------
	public void init()
	{
		String param;

		try {
			aWidth = size().width;
			aHeight = size().height;
			buffer = createImage (aWidth, aHeight);
			offScreen = buffer.getGraphics ();
		} catch (Exception e) {
			offScreen = null;
		}

		// nbMine : nombre de mines
		//----------------------------------------------------------------------
		param = getParameter("nbmine");
		if (param != null)
			m_mine = Integer.parseInt(param);

		// nbcol : nombre de colones
		//----------------------------------------------------------------------
		param = getParameter("nbcol");
		if (param != null)
			m_col = Integer.parseInt(param);

		// nbligne : nombre de colones
		//----------------------------------------------------------------------
		param = getParameter("nbligne");
		if (param != null)
			m_ligne = Integer.parseInt(param);


		// bgcolor: Couleur du fond
		//----------------------------------------------------------------------
		param = getParameter("bgcolor");
		if (param != null)
			m_bgcolor = Integer.parseInt(param);

		MediaTracker  trk = new MediaTracker(this) ;

		// bImg: Bouton Image
		//----------------------------------------------------------------------
		param = getParameter("bImg");
		img = new Image[13];
		if (param != null) {
			for (int i = 0 ; i < 13 ; i++) {
		    	img[i] = getImage(getCodeBase(), param + (i) + ".gif");
				trk.addImage(img[i], 0) ;
			}
		}
		else
			for (int i = 0 ; i < 13 ; i++) 
		    	img[i] = null ;


		try	  { trk.waitForAll() ; }
		catch (InterruptedException e) {	}

		// Font
		//----------------------------------------------------------------------
		param = getParameter("fontsize");
		int iSize = 12 ;
		if (param != null)
			iSize = Integer.parseInt(param);

		param = getParameter("font");
		if (param == null)
			param = new String("Arial") ;

		wFont = new Font(param, Font.BOLD, iSize);
		if (wFont == null) wFont = getFont() ;

		wMetrics = getFontMetrics(wFont);

		iPas = wMetrics.getHeight() * 2 ;
		newGame() ;
			

  }
  //----------------------------------------------------------------------
  
  // start : 						  random
  //----------------------------------------------------------------------
  public void newGame() {
	    Random a ; 
		int i = 0 ;
		int j = 0 ;
		int iE ;
		a = new Random() ;
		iEtat = 0 ;
		iCpt = m_mine ;
		int k = 0 ;

		iMax = m_ligne*m_col ;
		bStr = new int[iMax] ;

		iX = (aWidth - (m_col * 15)) / 2 ;
		if ( (m_col * 15) > aWidth ) iX = 0 ;

		for (i = 0 ; i < iMax; i++) bStr[i] = 10 ;

		// calculate the tick to wait for
		startTick = -1 ;

		i = 0 ;
		while ( i < m_mine) {
			j = (int)(iMax * a.nextDouble()) ;
			if ( (j < iMax) && (bStr[j] != 19 ) ) {
				iE = j % m_col ;
				bStr[j] = 19 ;
				i++ ;

				if (iE > 0) { // pas la 1er colonne
					k = j-1-m_col ;	// -31
					if ( k >= 0 )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;
					k = j - 1 ;		// -1
					if ( k >= 0 )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;

					k = j+m_col-1 ; // +29
					if ( k < iMax )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;
				}

				k = j - m_col ;
				if ( k >= 0 )  
					if ( bStr[k] != 19 ) bStr[k] += 1 ;
				k = j + m_col ;
				if ( k < iMax )  
					if ( bStr[k] != 19 ) bStr[k] += 1 ;

				if (iE < (m_col-1)) {  // Pas la derniere
					k = j-m_col+1 ;		// -29
					if ( k >= 0 )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;
					k = j + m_col + 1 ; // +31
					if ( k < iMax )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;
					k = j + 1 ;			// +1
					if ( k < iMax )  
						if ( bStr[k] != 19 ) bStr[k] += 1 ;
				}

			}
		}

  		repaint();
  		
  }

  // start : 
  //----------------------------------------------------------------------
  public void start() {
  		repaint();
  }


  // mouseDown : 
  //----------------------------------------------------------------------
  public boolean mouseDown(Event e, int x, int y){
	  int iC = (x-iX) / 15 ;
	  int iL = (y-iPas) / 15 ;
	  boolean rep = false ;


	  if ( (x > ((aWidth/2)-20)) && (x < ((aWidth/2)+40)) &&
		   (y > (iPas/4))        && (y < (iPas*3/4) ) ) {
		  URL clickDest ;
	  	  try{
			clickDest = new URL("http://www.javaside.com/") ;
			getAppletContext().showDocument(clickDest, "rbl");
		  }catch(MalformedURLException mal){
			System.out.println("Bad URL!");
		  }
		  return(true) ;
	  }

	  if (iEtat == 1) {
		  newGame() ;
	      repaint() ;
		  return(true);
	  }

	  if ( (y > iPas) && (x > iX) && (x < aWidth - iX))
	  if ( (e.modifiers & Event.META_MASK) == Event.META_MASK) {
		  if ( bStr[(iL*m_col)+iC] > 9 ) {
			rep = true ;
			if ( bStr[(iL*m_col)+iC] < 20 ) {
				if (iCpt > 0) {
					bStr[(iL*m_col)+iC] += 10 ;
					iCpt-- ;
				}
				else
					showStatus("Erreur trop de mines...");
			}
			else {
					bStr[(iL*m_col)+iC] -= 10 ;
					iCpt++ ;
			}
		}
		else 
			rep = testCase((iL*m_col)+iC ) ;
		
	  }
	  else {
		  if ( bStr[(iL*m_col)+iC] > 9) {
				if (startTick == -1)
		  			startTick = System.currentTimeMillis() ;
				bStr[(iL*m_col)+iC] -= 10 ;
				rep = true ;
				if (bStr[(iL*m_col)+iC] == 9)
					iEtat = 1 ;
				if (bStr[(iL*m_col)+iC] == 0) 
					verifPos( (iL*m_col) + iC) ;
		  }
	  }

	  if (rep)  repaint() ;

	  return(true);
  }
  //----------------------------------------------------------------------

  public void verifPos(int j) {
		int iE = j % m_col ;
		int k ;

		if (iE > 0) {	// Pas la 1ere col
			k = j - m_col -1 ;	// -31
			if ( k >= 0 )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 
			k = j - 1 ;			// -1
			if ( k >= 0 )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 
			k = j + m_col - 1 ; // +29
			if ( k < iMax )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 
		}

		k = j - m_col ;
		if ( k >= 0 )  
			if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
				bStr[k] -= 10 ;
				if (bStr[k] == 0) verifPos( k ) ;
		    } 

		k = j + m_col ;
		if ( k < iMax )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 

		if (iE < (m_col-1)) {	// Pas la derniere
			k = j - m_col+1 ;	// -29
			if ( k >= 0 )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 

			k = j + m_col + 1 ;	// +31
			if ( k < iMax )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 

			k = j + 1 ;			// +1
			if ( k < iMax )  
				if (( bStr[k] > 9 ) && ( bStr[k] < 20 ) )  {
					bStr[k] -= 10 ;
					if (bStr[k] == 0) verifPos( k ) ;
			    } 
		}

  }

  public boolean testCase(int j) {
		int iE = j % m_col ;
		int i = 0 ;
		int k ;

		if (iE > 0) {		// Pas la 1ere col
			k = j - m_col - 1 ;   // -31
			if ( k >= 0 )  
				if ( bStr[k] > 19 ) i++ ;
			k = j - 1 ;		// -1
			if ( k >= 0 )  
				if ( bStr[k] > 19 ) i++ ;
			k = j + m_col - 1 ;  // +29
			if ( k < iMax )  
				if ( bStr[k] > 19 ) i++ ;
		}

		k = j - m_col ;
		if ( k >= 0 )  
				if ( bStr[k] > 19 ) i++ ;
		
		k = j + m_col ;
		if ( k < iMax )  
				if ( bStr[k] > 19 ) i++ ;

		if (iE < (m_col-1) ) {
			k = j - m_col + 1 ;  // -29
			if ( k >= 0 )  
				if ( bStr[k] > 19 ) i++ ;
			k = j + m_col + 1 ;  // +31
			if ( k < iMax )  
				if ( bStr[k] > 19 ) i++ ;
			k = j + 1 ;			// +1
			if ( k < iMax )  
				if ( bStr[k] > 19 ) i++ ;
		}

		if (i == bStr[j])  {
			verifPos(j) ;
			return true ;
		}
		else
			return false ;

  }



  // paint :  Called when the applet needs to be painted
  //		  calls the flicker free updating system
  //----------------------------------------------------------------------
  public void paint (Graphics g) {
		update(g);
  } // end of paint
  //----------------------------------------------------------------------


  // update :  Draw the applet without flicker
  //----------------------------------------------------------------------
  public synchronized void update(Graphics g) {
		if (offScreen!=null) {
			paintApplet(offScreen);
			g.drawImage(buffer,0,0,this);
		} else
			paintApplet(g);
  } // end of update
  //----------------------------------------------------------------------

  // paintApplet :  Paint the applet into whatever image
  //----------------------------------------------------------------------
  public void paintApplet(Graphics g) {

	  int i = 0 ;
	  int j = 0 ;
	  int a = 0 ;
	  int k = 0 ;

	  g.setColor( new Color(m_bgcolor) );
	  g.fillRect(0, 0, aWidth, aHeight) ;

	  for (i = 0 ; i < m_ligne; i++) {
	     for (j = 0 ; j < m_col; j++) {
			 a = bStr[(i*m_col)+j] ;
			 if ( (iEtat == 0) && (a == 9) ) iEtat = 1 ; 
			 if (iEtat == 1) {
				 if (a == 19) a = 9 ; 
				 if ((a > 19) && (a != 29) ) {
					 iEtat = 1 ;
					 a = 41 ; 
				 }
			 }
				  
			 if (a > 40) a = 12 ; else
			 if (a > 19) a = 11 ; else
			 if (a > 9) { a = 10 ;	k++; }

			 if (img[a] != null)
				g.drawImage(img[a], iX+(j*15), iPas + (i*15) , this) ;
		 }
	  }


	  long thisTick ;

		// calculate the tick to wait for
	  if (startTick != -1)
		thisTick = (System.currentTimeMillis() - startTick ) / 1000;
	  else
		thisTick = 0 ;

	  g.setColor( new Color(0) );
	  g.fill3DRect(iPas,
	  			   iPas/4,
				   iPas,
				   iPas/2, 
				   true ) ;

	  g.fill3DRect(aWidth-iPas-iPas,
	  			   iPas/4,
				   iPas,
				   iPas/2, 
				   true ) ;

	  g.setFont( wFont );
	  g.setColor( Color.green );

	  g.drawString (" " + iCpt, iPas+5, iPas*2/3 ) ;
	  g.drawString (" " + thisTick, aWidth-iPas-iPas+5, iPas*2/3 ) ;
	  g.setColor( Color.black );
	  if (k == 0) {
		  iEtat = 1 ;
		  g.drawString (" " + iCpt, iPas+5, (iPas*2/3) ) ;
		  g.drawString ("Win", (aWidth/2)-5, (iPas*2/3) ) ;
	  }
	  else
		  if (iEtat == 1)
			 g.drawString ("Lose", (aWidth/2)-5, (iPas*2/3) ) ;
		  else {
			 g.setColor( new Color(0xC0C0C0) );
			 g.fill3DRect( (aWidth / 2)-20, iPas/4, 40, iPas/2, true) ; 
			 g.setColor( new Color(0) );
			 g.drawString ("?", (aWidth/2)-3, (iPas*2/3) ) ;
		  }


  }

}

