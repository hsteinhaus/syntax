/**********************************************************************
 *
 * Copyright (c) by Olaf Willuhn
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.fibu.ext.hibiscus;

import java.util.Date;
import java.util.List;

import de.willuhn.datasource.rmi.DBIterator;
import de.willuhn.jameica.fibu.Fibu;
import de.willuhn.jameica.fibu.Settings;
import de.willuhn.jameica.fibu.gui.action.BuchungNeu;
import de.willuhn.jameica.fibu.rmi.Buchung;
import de.willuhn.jameica.fibu.rmi.Geschaeftsjahr;
import de.willuhn.jameica.fibu.rmi.Konto;
import de.willuhn.jameica.fibu.rmi.Mandant;
import de.willuhn.jameica.fibu.server.Math;
import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.extension.Extendable;
import de.willuhn.jameica.gui.extension.Extension;
import de.willuhn.jameica.gui.extension.ExtensionRegistry;
import de.willuhn.jameica.gui.parts.CheckedContextMenuItem;
import de.willuhn.jameica.gui.parts.ContextMenu;
import de.willuhn.jameica.gui.parts.ContextMenuItem;
import de.willuhn.jameica.hbci.messaging.ObjectChangedMessage;
import de.willuhn.jameica.hbci.rmi.Umsatz;
import de.willuhn.jameica.hbci.rmi.UmsatzTyp;
import de.willuhn.jameica.messaging.StatusBarMessage;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.BackgroundTask;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;
import de.willuhn.util.ProgressMonitor;

/**
 * Erweitert das Kontextmenu der Umsatzliste.
 * BUGZILLA 140 http://www.willuhn.de/bugzilla/show_bug.cgi?id=140
 */
public class UmsatzListMenu implements Extension
{
  private final static I18N i18n = Application.getPluginLoader().getPlugin(Fibu.class).getResources().getI18N();
  
  /**
   * @see de.willuhn.jameica.gui.extension.Extension#extend(de.willuhn.jameica.gui.extension.Extendable)
   */
  public void extend(Extendable extendable)
  {
    if (extendable == null || !(extendable instanceof ContextMenu))
    {
      Logger.warn("invalid extendable, skipping extension");
      return;
    }
    
    ContextMenu menu = (ContextMenu) extendable;
    menu.addItem(ContextMenuItem.SEPARATOR);
    
    menu.addItem(new MyContextMenuItem(i18n.tr("in SynTAX �bernehmen..."), new Action() {
    
      public void handleAction(Object context) throws ApplicationException
      {
        if (context == null)
          return;

        Umsatz[] umsaetze = null;
        if (context instanceof Umsatz)
          umsaetze = new Umsatz[]{(Umsatz)context};
        else if (context instanceof Umsatz[])
          umsaetze = (Umsatz[]) context;
        
        if (umsaetze == null || umsaetze.length == 0)
          return;

        // Wenn wir mehr als 1 Buchung haben, fuehren wir das
        // im Hintergrund aus. 
        Worker worker = new Worker(umsaetze);
        if (umsaetze.length > 1)
          Application.getController().start(worker);
        else
          worker.run(null);
      }
    }));
  }

  /**
   * Erzeugt eine einzelne Buchung.
   * Sie wird jedoch noch nicht gespeichert.
   * @param u die zu erzeugende Buchung.
   * @param auto true, wenn wir mehr als eine Buchung haben und im Automatik-Modus laufen.
   * In dem Fall wird die Erstellung der Buchung mit einer ApplicationException
   * abgebrochen, wenn keine Umsatz-Kategorie vorhanden ist oder dieser keine
   * Buchungsvorlage zugeordnet ist.
   * "Keine Buchungsvorlage zugeordnet" geworfen.
   * @return die erzeugte Buchung.
   * @throws Exception
   */
  private Buchung createBuchung(Umsatz u, boolean auto) throws Exception
  {
    // Checken, ob der Umsatz eine Kategorie hat
    UmsatzTyp typ = u.getUmsatzTyp();
    
    if (typ == null && auto)
      throw new ApplicationException(i18n.tr("Keine Kategorie zugeordnet"));
    
    de.willuhn.jameica.fibu.rmi.Buchungstemplate template = null;
    if (typ != null)
    {
      // Vorlage suchen
      DBIterator i = Settings.getDBService().createList(de.willuhn.jameica.fibu.rmi.Buchungstemplate.class);
      i.addFilter("hb_umsatztyp_id = ?",new Object[]{typ.getID()});
      if (i.hasNext())
        template = (de.willuhn.jameica.fibu.rmi.Buchungstemplate) i.next();
    }
    
    if (template == null && auto)
      throw new ApplicationException(i18n.tr("Keine Buchungsvorlage ermittelbar"));

    Geschaeftsjahr jahr = null;
    
    // BUGZILLA 1593 - Ermitteln, zu welchem Mandanten das Template gehoert.
    // Wir nehmen dann dessen aktives Geschaeftsjahr
    if (template != null)
    {
      Mandant m = template.getMandant();
      DBIterator jahre = m.getGeschaeftsjahre();
      jahre.addFilter("(closed is null or closed = 0)");
      jahre.setOrder("order by beginn");
      if (!jahre.hasNext())
        throw new ApplicationException(i18n.tr("Kein offenes Gesch�ftsjahr zum Mandanten der Buchungsvorlage gefunden"));
      
      jahr = (Geschaeftsjahr) jahre.next();
    }
    
    if (jahr == null)
      jahr = Settings.getActiveGeschaeftsjahr();

    final Buchung buchung = (Buchung) Settings.getDBService().createObject(Buchung.class,null);
    buchung.setGeschaeftsjahr(jahr);
    buchung.setHibiscusUmsatzID(u.getID());
    buchung.setBelegnummer(buchung.getBelegnummer()); // Das erzeugt eine neue Belegnummer
    buchung.setKommentar(u.getKommentar());
    
    // Wir nehmen erstmal die Daten der Vorlage.
    if (template != null)
    {
      Konto soll = template.getSollKonto();
      if (auto && soll == null)
        throw new ApplicationException(i18n.tr("Buchungsvorlage \"{0}\" enth�lt kein Soll-Konto",template.getName()));
      Konto haben = template.getHabenKonto();
      if (auto && haben == null)
        throw new ApplicationException(i18n.tr("Buchungsvorlage \"{0}\" enth�lt kein Haben-Konto",template.getName()));

      buchung.setBruttoBetrag(template.getBetrag());
      buchung.setBetrag(new Math().netto(template.getBetrag(),template.getSteuer()));
      buchung.setDatum(new Date());
      buchung.setSollKonto(soll);
      buchung.setHabenKonto(haben);
      buchung.setSteuer(template.getSteuer());
      buchung.setText(template.getText());
    }

    // Wenn der Umsatz Werte mitbringt, ersetzen wir die gegen die von der Vorlage
    double brutto = u.getBetrag();
    if (brutto != 0.0 && !Double.isNaN(brutto))
    {
      boolean makeAbsolute = Settings.SETTINGS.getBoolean("hibiscus.import.betrag.absolut",true);
      if (makeAbsolute)
        brutto = java.lang.Math.abs(brutto);
      buchung.setBruttoBetrag(brutto);
      
      if (template != null)
        buchung.setBetrag(new Math().netto(brutto,template.getSteuer()));
      else
        buchung.setBetrag(brutto);
    }
    
    Date date = u.getDatum();
    if (date != null)
      buchung.setDatum(date);
    
    // Wir werfen alle Verwendungszwecke zusammen
    String zweck = (String) u.getAttribute("mergedzweck");
    if (zweck != null && zweck.length() > 0)
    {
      // Kontoinhaber noch anhaengen, falls vorhanden
      String name = u.getGegenkontoName();
      if (name != null && name.length() > 0)
        zweck = zweck.trim() + ", " + name;
      // Noch abschneiden, falls er zu lang ist
      if (zweck.length() > 255)
        zweck = zweck.substring(0,255);
      if (buchung.getText().isEmpty())
          // Buchungstext aus Verwendungszweck nur �bernehmen, falls nicht bereits durch Template vorgegeben
          buchung.setText(zweck);
    }
    return buchung;
  }

  /**
   * Hilfsklasse, um den Menupunkt zu deaktivieren, wenn die Buchung bereits zugeordnet ist.
   */
  private class MyContextMenuItem extends CheckedContextMenuItem
  {
    /**
     * ct.
     * @param text
     * @param a
     */
    public MyContextMenuItem(String text, Action a)
    {
      super(text, a);
    }

    /**
     * @see de.willuhn.jameica.gui.parts.CheckedContextMenuItem#isEnabledFor(java.lang.Object)
     */
    public boolean isEnabledFor(Object o)
    {
      if (o == null)
        return false;

      // Wenn wir eine ganze Liste von Buchungen haben, pruefen
      // wir nicht jede einzeln, ob sie schon in SynTAX vorhanden
      // ist. Die werden dann beim Import (weiter unten) einfach ausgesiebt.
      if (o instanceof Umsatz[])
        return super.isEnabledFor(o);
      
      if (!(o instanceof Umsatz))
        return false;

      boolean found = false;
      try
      {
        found = isAssigned((Umsatz) o);
      }
      catch (ApplicationException ae)
      {
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(ae.getMessage(), StatusBarMessage.TYPE_ERROR));
      }
      catch (Exception e)
      {
        Logger.error("unable to detect if buchung is allready assigned",e);
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(i18n.tr("Fehler beim Pr�fen, ob die Buchung bereits in SynTAX zugeordnet ist"), StatusBarMessage.TYPE_ERROR));
      }
      return !found && super.isEnabledFor(o);
    }
    
  }
  
  /**
   * Prueft, ob der Umsatz bereits einer Hibiscus-Buchung zugeordnet ist.
   * @param u der zu pruefende Umsatz.
   * @return true, wenn es bereits eine Buchung gibt.
   * @throws Exception
   */
  private boolean isAssigned(Umsatz u) throws Exception
  {
    // BUGZILLA 1593 - Wir suchen Mandanten-uebergreifend
    DBIterator list = Settings.getDBService().createList(Buchung.class);
    list.addFilter("hb_umsatz_id = ?",new Object[]{u.getID()});
    return list.hasNext();
  }

  
  /**
   * Damit koennen wir lange Vorgaenge ggf. im Hintergrund laufen lassen
   */
  private class Worker implements BackgroundTask
  {
    private boolean cancel = false;
    private Umsatz[] list = null;

    /**
     * ct.
     * @param list
     */
    private Worker(Umsatz[] list)
    {
      this.list = list;
    }
    
    /**
     * @see de.willuhn.jameica.system.BackgroundTask#interrupt()
     */
    public void interrupt()
    {
      this.cancel = true;
    }

    /**
     * @see de.willuhn.jameica.system.BackgroundTask#isInterrupted()
     */
    public boolean isInterrupted()
    {
      return this.cancel;
    }

    /**
     * @see de.willuhn.jameica.system.BackgroundTask#run(de.willuhn.util.ProgressMonitor)
     */
    public void run(ProgressMonitor monitor) throws ApplicationException
    {
      try
      {
        if (monitor != null)
          monitor.setStatusText(i18n.tr("Buche {0} Ums�tze",""+list.length));

        double factor = 100d / list.length;
        
        int created = 0;
        int error   = 0;
        int skipped = 0;

        for (int i=0;i<list.length;++i)
        {
          if (monitor != null)
          {
            monitor.setPercentComplete((int)((i+1) * factor));
            monitor.log("  " + i18n.tr("Erstelle Buchung {0}",Integer.toString(i+1)));
          }

          Buchung buchung = null;
          try
          {
            // Checken, ob der Umsatz schon einer Buchung zugeordnet ist
            if (isAssigned(list[i]))
            {
              skipped++;
              continue;
            }
            
            buchung = createBuchung(list[i],list.length > 1);
            buchung.store();
            created++;
            
            // Mit der Benachrichtigung wird dann gleich die Buchungsnummer in der Liste
            // angezeigt. Vorher muessen wir der anderen Extension aber noch die neue
            // Buchung mitteilen
            List<Extension> extensions = ExtensionRegistry.getExtensions("de.willuhn.jameica.hbci.gui.parts.UmsatzList");
            if (extensions != null)
            {
              for (Extension e:extensions)
              {
                if (e instanceof UmsatzListPart)
                {
                  ((UmsatzListPart)e).add(buchung);
                  break;
                }
              }
            }
            Application.getMessagingFactory().sendMessage(new ObjectChangedMessage(list[i]));
          }
          catch (ApplicationException ae)
          {
            // Wenn wir nur eine Buchung hatten und eine
            // ApplicationException, dann fehlen noch Eingaben
            // Da wir nur eine Buchung haben, oeffnen wir
            // die Erfassungsmaske.
            if (list.length == 1)
            {
              Application.getMessagingFactory().sendMessage(new StatusBarMessage(ae.getMessage(),StatusBarMessage.TYPE_ERROR));
              new BuchungNeu().handleAction(buchung);
              return;
            }
            
            if (monitor != null)
              monitor.log("    " + ae.getMessage());
            error++;
          }
          catch (Exception e)
          {
            Logger.error("unable to import umsatz",e);
            if (monitor != null)
              monitor.log("    " + i18n.tr("Fehler: {0}",e.getMessage()));
            error++;
          }
        }
        
        String text = i18n.tr("Umsatz importiert");
        if (list.length > 1)
          text = i18n.tr("{0} Ums�tze importiert, {1} fehlerhaft, {2} bereits vorhanden", new String[]{Integer.toString(created),Integer.toString(error),Integer.toString(skipped)});
        
        Application.getMessagingFactory().sendMessage(new StatusBarMessage(text,StatusBarMessage.TYPE_SUCCESS));
        if (monitor != null)
        {
          monitor.setStatusText(text);
          monitor.setStatus(ProgressMonitor.STATUS_DONE);
        }
        
      }
      catch (Exception e)
      {
        Logger.error("error while importing objects",e);
        throw new ApplicationException(i18n.tr("Fehler beim Import der Ums�tze"));
      }
    }
  }
}
