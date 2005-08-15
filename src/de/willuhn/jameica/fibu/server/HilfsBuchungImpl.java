/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/server/HilfsBuchungImpl.java,v $
 * $Revision: 1.11 $
 * $Date: 2005/08/15 23:38:27 $
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

import de.willuhn.jameica.fibu.rmi.BaseBuchung;
import de.willuhn.jameica.fibu.rmi.HilfsBuchung;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;

/**
 * @author willuhn
 */
public class HilfsBuchungImpl extends AbstractBaseBuchungImpl implements HilfsBuchung
{

  /**
   * @throws RemoteException
   */
  public HilfsBuchungImpl() throws RemoteException
  {
    super();
  }

  /**
   * @see de.willuhn.jameica.fibu.rmi.HilfsBuchung#getHauptBuchung()
   */
  public BaseBuchung getHauptBuchung() throws RemoteException
  {
    return (BaseBuchung) getAttribute("buchung_id");
  }

  /**
   * @see de.willuhn.jameica.fibu.rmi.HilfsBuchung#setHauptBuchung(de.willuhn.jameica.fibu.rmi.BaseBuchung)
   */
  public void setHauptBuchung(BaseBuchung buchung) throws RemoteException
  {
    setAttribute("buchung_id",buchung);
  }

  /**
   * @see de.willuhn.datasource.db.AbstractDBObject#getForeignObject(java.lang.String)
   */
  public Class getForeignObject(String field) throws RemoteException
  {
    if ("buchung_id".equals(field))
      return BaseBuchung.class;
    return super.getForeignObject(field);
  }

  /**
   * @see de.willuhn.datasource.db.AbstractDBObject#insertCheck()
   */
  public void insertCheck() throws ApplicationException
  {
    try {
      if (getHauptBuchung() == null)
        throw new ApplicationException("Keine Haupt-Buchung zugewiesen.");
    }
    catch (RemoteException e)
    {
			Logger.error("error while reading hilfsbuchung",e);
      throw new ApplicationException("Fehler bei der Pr�fung der Hilfs-Buchung.",e);
    }
    super.insertCheck();
  }
  
  /**
   * @see de.willuhn.datasource.db.AbstractDBObject#updateCheck()
   */
  public void updateCheck() throws ApplicationException
  {
    insertCheck();
  }

  /**
   * Ueberschrieben von AbstractBaseBuchungImpl weil die Funktion in AbstractBaseBuchungImpl alle Buchungen
   * _ausser_ Hilfs-Buchungen findet. Wir wollen aber genau die ;).
   * @see de.willuhn.datasource.db.AbstractDBObject#getListQuery()
   */
  protected String getListQuery()
  {
    return super.getListQuery() + " and buchung_id is NOT NULL";          // nur Hilfs-Buchungen
  }

}

/*********************************************************************
 * $Log: HilfsBuchungImpl.java,v $
 * Revision 1.11  2005/08/15 23:38:27  willuhn
 * *** empty log message ***
 *
 * Revision 1.10  2005/08/15 13:18:44  willuhn
 * *** empty log message ***
 *
 * Revision 1.9  2005/08/12 00:10:59  willuhn
 * @B bugfixing
 *
 * Revision 1.8  2005/08/10 17:48:02  willuhn
 * @C refactoring
 *
 * Revision 1.7  2005/08/08 22:54:16  willuhn
 * @N massive refactoring
 *
 * Revision 1.6  2005/08/08 21:35:46  willuhn
 * @N massive refactoring
 *
 * Revision 1.5  2004/01/29 01:05:09  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2004/01/25 19:44:03  willuhn
 * *** empty log message ***
 *
 * Revision 1.3  2004/01/03 18:07:22  willuhn
 * @N Exception logging
 *
 * Revision 1.2  2003/12/19 19:45:06  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2003/12/16 02:27:33  willuhn
 * @N BuchungsEngine
 *
 **********************************************************************/