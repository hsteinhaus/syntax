/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/server/KontoartImpl.java,v $
 * $Revision: 1.4 $
 * $Date: 2003/12/12 21:11:27 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.fibu.server;

import java.rmi.RemoteException;

import de.willuhn.jameica.ApplicationException;
import de.willuhn.jameica.fibu.rmi.Kontoart;
import de.willuhn.jameica.server.AbstractDBObject;

/**
 * @author willuhn
 */
public class KontoartImpl extends AbstractDBObject implements Kontoart
{

  /**
   * Erzeugt eine neue Kontoart.
   * @throws RemoteException
   */
  public KontoartImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#getTableName()
   */
  protected String getTableName()
  {
    return "kontoart";
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#getPrimaryField()
   */
  public String getPrimaryField() throws RemoteException
  {
    return "name";
  }

  /**
   * @see de.willuhn.jameica.fibu.objects.Steuer#getName()
   */
  public String getName() throws RemoteException
  {
    return (String) getField("name");
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#deleteCheck()
   */
  protected void deleteCheck() throws ApplicationException
  {
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#insertCheck()
   */
  protected void insertCheck() throws ApplicationException
  {
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#updateCheck()
   */
  protected void updateCheck() throws ApplicationException
  {
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#getForeignObject(java.lang.String)
   */
  protected Class getForeignObject(String field) throws RemoteException
  {
    return null;
  }

  /**
   * @see de.willuhn.jameica.fibu.objects.Kontoart#getKontoArt()
   */
  public int getKontoArt() throws RemoteException
  {
    try {
      return Integer.parseInt(getID());
    }
    catch (NumberFormatException ne)
    {
      return KONTOART_UNGUELTIG;
    }
  }

}

/*********************************************************************
 * $Log: KontoartImpl.java,v $
 * Revision 1.4  2003/12/12 21:11:27  willuhn
 * @N ObjectMetaCache
 *
 * Revision 1.3  2003/12/11 21:00:34  willuhn
 * @C refactoring
 *
 * Revision 1.2  2003/12/10 23:51:52  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2003/12/05 17:11:58  willuhn
 * @N added GeldKonto, Kontoart
 *
 **********************************************************************/