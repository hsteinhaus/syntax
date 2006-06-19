/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/gui/action/Attic/Welcome.java,v $
 * $Revision: 1.3 $
 * $Date: 2006/06/19 16:25:42 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.gui.action;

import de.willuhn.jameica.fibu.Settings;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

/**
 * Action zum Laden der Startseite.
 * @author willuhn
 */
public class Welcome implements Action
{

  /**
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
    if (!Application.inServerMode() && Settings.isFirstStart())
      new FirstStart().handleAction(context);
    else
      GUI.startView(de.willuhn.jameica.fibu.gui.views.Welcome.class,null);
  }

}


/*********************************************************************
 * $Log: Welcome.java,v $
 * Revision 1.3  2006/06/19 16:25:42  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2006/05/08 22:44:18  willuhn
 * @N Debugging
 *
 * Revision 1.1  2005/08/10 17:48:02  willuhn
 * @C refactoring
 *
 *********************************************************************/