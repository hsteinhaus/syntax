/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/server/KontenrahmenImpl.java,v $
 * $Revision: 1.8 $
 * $Date: 2004/01/25 19:44:03 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by  bbv AG
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.server;

import java.rmi.RemoteException;

import de.willuhn.datasource.db.AbstractDBObject;
import de.willuhn.jameica.fibu.rmi.Kontenrahmen;
import de.willuhn.util.ApplicationException;

/**
 * @author willuhn
 * 24.11.2003
 */
public class KontenrahmenImpl extends AbstractDBObject implements Kontenrahmen
{

  /**
   * Erzeugt einen neuen Kontorahmen.
   * @throws RemoteException
   */
  public KontenrahmenImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#getTableName()
   */
  protected String getTableName()
  {
    return "kontenrahmen";
  }

  /**
   * @see de.willuhn.jameica.rmi.DBObject#getPrimaryField()
   */
  public String getPrimaryField() throws RemoteException
  {
    return "name";
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#getForeignObject(java.lang.String)
   */
  public Class getForeignObject(String field) throws RemoteException
  {
    return null;
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#deleteCheck()
   */
  public void deleteCheck() throws ApplicationException
  {
    throw new ApplicationException("Kontenrahmen d�rfen nicht gel�scht werden.");
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#insertCheck()
   */
  public void insertCheck() throws ApplicationException
  {
    updateCheck();
  }

  /**
   * @see de.willuhn.jameica.rmi.AbstractDBObject#updateCheck()
   */
  public void updateCheck() throws ApplicationException
  {
    try {
      String name = (String) getField("name");
      if (name == null || "".equals(name))
        throw new ApplicationException("Bitte geben Sie einen Namen f�r den Kontenrahmen ein.");
    }
    catch (RemoteException e)
    {
      throw new ApplicationException("Fehler bei der Pr�fung der Pflichtfelder.",e);
    }
  }

}


/*********************************************************************
 * $Log: KontenrahmenImpl.java,v $
 * Revision 1.8  2004/01/25 19:44:03  willuhn
 * *** empty log message ***
 *
 * Revision 1.7  2003/12/12 21:11:27  willuhn
 * @N ObjectMetaCache
 *
 * Revision 1.6  2003/12/11 21:00:34  willuhn
 * @C refactoring
 *
 * Revision 1.5  2003/11/30 16:23:11  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2003/11/27 00:21:05  willuhn
 * @N Checks via insertCheck(), deleteCheck() updateCheck() in Business-Logik verlagert
 *
 * Revision 1.3  2003/11/24 23:02:11  willuhn
 * @N added settings
 *
 * Revision 1.2  2003/11/24 16:26:16  willuhn
 * @N AbstractDBObject is now able to resolve foreign keys
 *
 * Revision 1.1  2003/11/24 15:18:21  willuhn
 * *** empty log message ***
 *
 *********************************************************************/