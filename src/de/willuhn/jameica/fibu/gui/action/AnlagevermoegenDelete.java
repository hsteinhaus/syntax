/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/gui/action/AnlagevermoegenDelete.java,v $
 * $Revision: 1.4 $
 * $Date: 2011/05/11 10:38:51 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.gui.action;

import de.willuhn.jameica.fibu.Fibu;
import de.willuhn.jameica.fibu.rmi.Anlagevermoegen;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.gui.dialogs.YesNoDialog;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Action zum Loeschen eines Anlagevermoegens.
 */
public class AnlagevermoegenDelete implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    if (context == null || !(context instanceof Anlagevermoegen))
      return;
    
    I18N i18n = Application.getPluginLoader().getPlugin(Fibu.class).getResources().getI18N();

    try
    {
      YesNoDialog d = new YesNoDialog(YesNoDialog.POSITION_CENTER);
      d.setTitle(i18n.tr("Anlage-Gegenstand wirklich l�schen?"));
      d.setText(i18n.tr("Wollen Sie diesen Anlage-Gegenstand wirklich l�schen?\n" +
                        "Hierbei werden auch alle zugeh�rigen Abschreibungsbuchungen gel�scht."));
      
      if (!((Boolean) d.open()).booleanValue())
      {
        Logger.info("operation cancelled");
        return;
      }

      ((Anlagevermoegen)context).delete();
      GUI.getStatusBar().setSuccessText(i18n.tr("Anlage-Gegenstand gel�scht"));
    }
    catch (OperationCanceledException oce)
    {
      Logger.info(oce.getMessage());
      return;
    }
    catch (Exception e)
    {
      Logger.error("unable to delete anlagevermoegen",e);
      throw new ApplicationException(i18n.tr("Fehler beim L�schen der Daten des Anlage-Gegenstandes"));
    }
  }

}


/*********************************************************************
 * $Log: AnlagevermoegenDelete.java,v $
 * Revision 1.4  2011/05/11 10:38:51  willuhn
 * @N OCE fangen
 *
 * Revision 1.3  2005-09-26 15:15:39  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2005/09/01 16:34:45  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2005/08/29 14:26:56  willuhn
 * @N Anlagevermoegen, Abschreibungen
 *
 **********************************************************************/