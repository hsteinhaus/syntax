/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/gui/controller/Attic/SettingsControl.java,v $
 * $Revision: 1.6 $
 * $Date: 2004/01/25 19:44:03 $
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

import de.willuhn.datasource.rmi.DBObject;
import de.willuhn.jameica.Application;
import de.willuhn.jameica.fibu.Settings;
import de.willuhn.jameica.fibu.gui.views.Welcome;
import de.willuhn.jameica.fibu.rmi.Mandant;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.util.I18N;

/**
 * Diese Klasse behandelt alle Button-Drueckungen(sic!) ;) des
 * Dialogs "Einstellungen".
 * @author willuhn
 */
public class SettingsControl extends Controller
{

  /**
   * Erzeugt einen neuen Controller der fuer die Settings zustaendig ist.
   * @param object die Buchung.
   */
  public SettingsControl(DBObject object)
  {
    super(object);
  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleDelete(java.lang.String)
   */
  public void handleDelete(String id)
  {
    // nothing to delete
  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleDelete()
   */
  public void handleDelete()
  {
    // nothing to delete
  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleCancel()
   */
  public void handleCancel()
  {
    GUI.startView(Welcome.class.getName(),null);
  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleStore()
   */
  public void handleStore()
  {

    Settings.setCurrency(getField("currency").getValue());

    try {
      Mandant m = (Mandant) Settings.getDatabase().createObject(Mandant.class,getField("mandant").getValue());
      Settings.setActiveMandant(m);
      GUI.setActionText(I18N.tr("Einstellungen gespeichert."));
    }
    catch (RemoteException e)
    {
      Application.getLog().error("error while saving dafeult mandant",e);
      GUI.setActionText(I18N.tr("Fehler bei der Speicherung des aktiven Mandanten"));
    }

  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleChooseFromList(java.lang.String)
   */
  public void handleLoad(String id)
  {
    // nothing to load
  }

  /**
   * @see de.willuhn.jameica.views.parts.Controller#handleCreate()
   */
  public void handleCreate()
  {
    // nothing to create
  }

}

/*********************************************************************
 * $Log: SettingsControl.java,v $
 * Revision 1.6  2004/01/25 19:44:03  willuhn
 * *** empty log message ***
 *
 * Revision 1.5  2004/01/03 18:07:22  willuhn
 * @N Exception logging
 *
 * Revision 1.4  2003/12/15 19:08:04  willuhn
 * *** empty log message ***
 *
 * Revision 1.3  2003/12/11 21:00:35  willuhn
 * @C refactoring
 *
 * Revision 1.2  2003/12/10 23:51:53  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2003/11/24 23:02:11  willuhn
 * @N added settings
 *
 **********************************************************************/