#Beschreibung der REST-API für das Anlegen von Exemplaren

###### Aufgabe:
Entwerfen und implementieren Sie einen REST-Endpoint fur das Anlegen von Exemplaren. Dazu
benotigen Sie wieder eine Resourcen-Klasse und einen Service.<br />
Zum Eintragen muss eine ISBN/BArcode und ein Benutzername angegeben werden. Gepruft werden
muss lediglich, ob das Medium zur gegebenen ISBN/Barcode vorhanden ist.

###### REST-API
| URI-Template      | Verb          | Wirkung  |
| -------------     |-------------  | ------   |
| uri               | POST          | neues Buchexemplar anlegen<br />Mögliche Fehler: Buch existiert nicht...|