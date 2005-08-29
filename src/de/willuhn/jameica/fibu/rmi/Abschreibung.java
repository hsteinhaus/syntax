/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/rmi/Abschreibung.java,v $
 * $Revision: 1.2 $
 * $Date: 2005/08/29 22:26:19 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.rmi;

import java.rmi.RemoteException;

import de.willuhn.datasource.rmi.DBObject;

/**
 * Interface fuer eine einzelne Abschreibungsbuchung.
 */
public interface Abschreibung extends DBObject
{
  /**
   * Liefert das zu dieser Abschreibung gehoerende Anlagevermoegen.
   * @return Anlagevermoegen.
   * @throws RemoteException
   */
  public Anlagevermoegen getAnlagevermoegen() throws RemoteException;
  
  /**
   * Liefert die zugehoerige Buchung.
   * @return Buchung.
   * @throws RemoteException
   */
  public Buchung getBuchung() throws RemoteException;
  
  /**
   * Speichert das Anlagevermoegen zu dieser Abschreibung.
   * @param av Anlagevermoegen.
   * @throws RemoteException
   */
  public void setAnlagevermoegen(Anlagevermoegen av) throws RemoteException;
  
  /**
   * Speichert die Buchung der Abschreibung.
   * @param b Buchung.
   * @throws RemoteException
   */
  public void setBuchung(Buchung b) throws RemoteException;
  
  /**
   * Liefert das Geschaeftsjahr, in dem die Abschreibung vorgenommen wurde.
   * @return Geschaeftsjahr.
   * @throws RemoteException
   */
  public Geschaeftsjahr getGeschaeftsjahr() throws RemoteException;
  
  /**
   * Speichert das Geschaeftsjahr, in dem die Abschreibung vorgenommen wurde.
   * @param jahr Geschaeftsjahr.
   * @throws RemoteException
   */
  public void setGeschaeftsjahr(Geschaeftsjahr jahr) throws RemoteException;
}


/*********************************************************************
 * $Log: Abschreibung.java,v $
 * Revision 1.2  2005/08/29 22:26:19  willuhn
 * @N Jahresabschluss
 *
 * Revision 1.1  2005/08/29 14:26:57  willuhn
 * @N Anlagevermoegen, Abschreibungen
 *
 **********************************************************************/