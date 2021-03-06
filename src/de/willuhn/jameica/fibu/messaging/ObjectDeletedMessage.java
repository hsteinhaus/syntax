/**********************************************************************
 * $Source: /cvsroot/syntax/syntax/src/de/willuhn/jameica/fibu/messaging/ObjectDeletedMessage.java,v $
 * $Revision: 1.1 $
 * $Date: 2010/08/27 11:19:40 $
 * $Author: willuhn $
 *
 * Copyright (c) by willuhn - software & services
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.messaging;

import de.willuhn.jameica.messaging.QueryMessage;

/**
 * Wird verschickt, wenn ein Datensatz geloescht wurde.
 */
public class ObjectDeletedMessage extends QueryMessage
{
  /**
   * ct.
   * @param data
   */
  public ObjectDeletedMessage(Object data)
  {
    super(data);
  }
}



/**********************************************************************
 * $Log: ObjectDeletedMessage.java,v $
 * Revision 1.1  2010/08/27 11:19:40  willuhn
 * @N Import-/Export-Framework incl. XML-Format aus Hibiscus portiert
 *
 **********************************************************************/