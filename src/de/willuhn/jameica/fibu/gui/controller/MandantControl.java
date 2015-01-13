/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/gui/controller/MandantControl.java,v $
 * $Revision: 1.25 $
 * $Date: 2010/02/08 15:39:48 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.fibu.gui.controller;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.fibu.Fibu;
import de.willuhn.jameica.fibu.Settings;
import de.willuhn.jameica.fibu.gui.action.GeschaeftsjahrNeu;
import de.willuhn.jameica.fibu.rmi.Finanzamt;
import de.willuhn.jameica.fibu.rmi.Mandant;
import de.willuhn.jameica.gui.AbstractControl;
import de.willuhn.jameica.gui.AbstractView;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.input.Input;
import de.willuhn.jameica.gui.input.LabelInput;
import de.willuhn.jameica.gui.input.SelectInput;
import de.willuhn.jameica.gui.input.TextInput;
import de.willuhn.jameica.system.Application;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Controller fuer den Dialog zum Mandanten.
 */
public class MandantControl extends AbstractControl
{

	// Fach-Objekte
	private Mandant mandant 					= null;

	// Eingabe-Felder
	private Input name1								= null;
	private Input name2								= null;
	private Input firma								= null;
	private Input strasse							= null;
	private Input plz									= null;
	private Input ort									= null;
	private Input steuernummer				= null;
  private Input waehrung            = null;
	private Input kuerzel            = null;
	private Input finanzamtAuswahl		= null;


	private boolean storeAllowed      = false;
  
  private I18N i18n;

  /**
   * @param view
   */
  public MandantControl(AbstractView view)
  {
    super(view);
    i18n = Application.getPluginLoader().getPlugin(Fibu.class).getResources().getI18N();
  }

	/**
	 * Liefert den Mandanten.
   * @return Mandant.
   * @throws RemoteException
   */
  public Mandant getMandant() throws RemoteException
	{
		if (mandant != null)
			return mandant;

		mandant = (Mandant) getCurrentObject();
		if (mandant != null)
			return mandant;

		mandant = (Mandant) Settings.getDBService().createObject(Mandant.class,null);
		return mandant;
	}

  /**
   * Liefert das Eingabe-Feld fuer die Waehrung.
   * @return Eingabe-Feld.
   * @throws RemoteException
   */
  public Input getWaehrung() throws RemoteException
  {
    if (waehrung != null)
      return waehrung;
    
    waehrung = new TextInput(getMandant().getWaehrung());
    return waehrung;
  }


  /**
   * Liefert das Eingabe-Feld fuer die Waehrung.
   * @return Eingabe-Feld.
   * @throws RemoteException
   */
  public Input getKuerzel() throws RemoteException
  {
    if (kuerzel != null)
      return kuerzel;

    kuerzel = new TextInput(getMandant().getKuerzel(),4);
    return kuerzel;
  }

  /**
	 * Liefert das Eingabe-Feld fuer Name1.
   * @return Eingabe-Feld.
   * @throws RemoteException
   */
  public Input getName1() throws RemoteException
	{
		if (name1 != null)
			return name1;
		
		name1 = new TextInput(getMandant().getName1());
		return name1;
	}

	/**
	 * Liefert das Eingabe-Feld fuer Name2.
	 * @return Eingabe-Feld.
	 * @throws RemoteException
	 */
	public Input getName2() throws RemoteException
	{
		if (name2 != null)
			return name2;
		
		name2 = new TextInput(getMandant().getName2());
		return name2;
	}

	/**
	 * Liefert das Eingabe-Feld fuer die Strasse.
	 * @return Eingabe-Feld.
	 * @throws RemoteException
	 */
	public Input getStrasse() throws RemoteException
	{
		if (strasse != null)
			return strasse;
		
		strasse = new TextInput(getMandant().getStrasse());
		return strasse;
	}

	/**
	 * Liefert das Eingabe-Feld fuer die Firma.
	 * @return Eingabe-Feld.
	 * @throws RemoteException
	 */
	public Input getFirma() throws RemoteException
	{
		if (firma != null)
			return firma;
		
		firma = new TextInput(getMandant().getFirma());
		firma.setMandatory(true);
		return firma;
	}

	/**
	 * Liefert das Eingabe-Feld fuer die PLZ.
	 * @return Eingabe-Feld.
	 * @throws RemoteException
	 */
	public Input getPLZ() throws RemoteException
	{
		if (plz != null)
			return plz;
		
		plz = new TextInput(getMandant().getPLZ(),7);
		return plz;
	}

	/**
	 * Liefert das Eingabe-Feld fuer den Ort.
	 * @return Eingabe-Feld.
	 * @throws RemoteException
	 */
	public Input getOrt() throws RemoteException
	{
		if (ort != null)
			return ort;
		
		ort= new TextInput(getMandant().getOrt());
		return ort;
	}


  /**
	 * Liefert das Eingabe-Feld fuer die Steuernummer.
   * @return Eingabe-Feld.
   * @throws RemoteException
   */
  public Input getSteuernummer() throws RemoteException
	{
		if (steuernummer != null)
			return steuernummer;
		
		steuernummer = new TextInput(getMandant().getSteuernummer());
		return steuernummer;
	}

	/**
	 * Liefert das Eingabe-Feld zur Auswahl des Finanzamtes.
   * @return Eingabe-Feld.
   * @throws RemoteException
   */
  public Input getFinanzamtAuswahl() throws RemoteException
	{
		if (finanzamtAuswahl != null)
			return finanzamtAuswahl;

		DBIterator list = Settings.getDBService().createList(Finanzamt.class);
		if (list.hasNext())
		{
			finanzamtAuswahl = new SelectInput(list,getMandant().getFinanzamt());
			finanzamtAuswahl.setMandatory(true);
			storeAllowed = true;
		}
		else {
      String s = i18n.tr("Kein Finanzamt vorhanden. Bitte richten Sie zun�chst eines ein.");
      GUI.getView().setErrorText(s);
			finanzamtAuswahl = new LabelInput(s);
		}
		return finanzamtAuswahl;
	}

	/**
	 * Prueft, ob gespeichert werden kann.
   * @return true, wenn gespeichert werden kann.
   */
  public boolean storeAllowed() 
	{
		return storeAllowed;
	}

  /**
   * Speichert den Mandanten.
   * @return true, wenn der Mandant gespeichert wurde.
   */
  public boolean handleStore()
  {
    try {

      //////////////////////////////////////////////////////////////////////////
      // Finanzamt checken
      getMandant().setFinanzamt((Finanzamt) getFinanzamtAuswahl().getValue());
      //
      //////////////////////////////////////////////////////////////////////////

      getMandant().setName1((String)getName1().getValue());
      getMandant().setName2((String)getName2().getValue());
      getMandant().setFirma((String)getFirma().getValue());
      getMandant().setStrasse((String)getStrasse().getValue());
      getMandant().setPLZ((String)getPLZ().getValue());
      getMandant().setOrt((String)getOrt().getValue());
      getMandant().setSteuernummer((String)getSteuernummer().getValue());
      getMandant().setKuerzel((String)getKuerzel().getValue());

      
      // und jetzt speichern wir.
			getMandant().store();
      GUI.getStatusBar().setSuccessText(i18n.tr("Mandant gespeichert."));
      return true;
    }
    catch (ApplicationException e1)
    {
      GUI.getView().setErrorText(e1.getLocalizedMessage());
    }
    catch (RemoteException e)
    {
			Logger.error("unable to store mandant",e);
      GUI.getView().setErrorText("Fehler beim Speichern des Mandanten.");
    }
    return false;
  }
  
  /**
   * Speichert den Mandanten und legt ein neues Geschaeftsjahr an.
   */
  public void handleNewGJ()
  {
    if (handleStore())
    {
      try
      {
        new GeschaeftsjahrNeu().handleAction(this.getMandant());
      }
      catch (ApplicationException e1)
      {
        GUI.getView().setErrorText(e1.getLocalizedMessage());
      }
      catch (RemoteException e)
      {
        Logger.error("unable to load mandant",e);
        GUI.getView().setErrorText("Fehler beim Laden des Mandanten.");
      }
    }
  }
}

/*********************************************************************
 * $Log: MandantControl.java,v $
 * Revision 1.25  2010/02/08 15:39:48  willuhn
 * @N Option "Geschaeftsjahr abschliessen" in Kontextmenu des Geschaeftsjahres
 * @N Zweispaltiges Layout in Mandant-Details - damit bleibt mehr Platz fuer die Reiter unten drunter
 * @N Anzeige von Pflichtfeldern
 *
 * Revision 1.24  2006/06/19 22:23:47  willuhn
 * @N Wizard
 *
 * Revision 1.23  2005/08/29 14:54:28  willuhn
 * @B bugfixing
 *
 * Revision 1.22  2005/08/29 12:17:29  willuhn
 * @N Geschaeftsjahr
 *
 * Revision 1.21  2005/08/16 17:39:24  willuhn
 * *** empty log message ***
 *
 * Revision 1.20  2005/08/15 23:38:28  willuhn
 * *** empty log message ***
 *
 * Revision 1.19  2005/08/12 00:10:59  willuhn
 * @B bugfixing
 *
 * Revision 1.18  2005/08/10 17:48:03  willuhn
 * @C refactoring
 *
 * Revision 1.17  2005/08/09 23:53:34  willuhn
 * @N massive refactoring
 *
 * Revision 1.16  2005/08/08 22:54:16  willuhn
 * @N massive refactoring
 *
 * Revision 1.15  2005/08/08 21:35:46  willuhn
 * @N massive refactoring
 *
 * Revision 1.14  2004/02/24 22:48:08  willuhn
 * *** empty log message ***
 *
 * Revision 1.13  2004/01/29 00:06:46  willuhn
 * *** empty log message ***
 *
 * Revision 1.12  2004/01/28 00:00:46  willuhn
 * *** empty log message ***
 *
 * Revision 1.11  2004/01/27 23:54:18  willuhn
 * *** empty log message ***
 *
 * Revision 1.10  2004/01/27 00:09:10  willuhn
 * *** empty log message ***
 *
 * Revision 1.9  2004/01/03 18:07:22  willuhn
 * @N Exception logging
 *
 * Revision 1.8  2003/12/15 19:08:04  willuhn
 * *** empty log message ***
 *
 * Revision 1.7  2003/12/12 01:28:07  willuhn
 * *** empty log message ***
 *
 * Revision 1.6  2003/12/11 21:00:35  willuhn
 * @C refactoring
 *
 * Revision 1.5  2003/12/10 23:51:53  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2003/12/01 20:29:00  willuhn
 * @B filter in DBIteratorImpl
 * @N InputFelder generalisiert
 *
 * Revision 1.3  2003/11/27 00:21:05  willuhn
 * @N Checks via insertCheck(), deleteCheck() updateCheck() in Business-Logik verlagert
 *
 * Revision 1.2  2003/11/25 00:22:17  willuhn
 * @N added Finanzamt
 *
 * Revision 1.1  2003/11/24 23:02:11  willuhn
 * @N added settings
 *
 **********************************************************************/