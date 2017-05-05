#Beschreibung der REST-API für das Anlegen von Exemplaren

###### Aufgabe:
Entwerfen und implementieren Sie einen REST-Endpoint fur das Anlegen von Exemplaren. Dazu
benotigen Sie wieder eine Resourcen-Klasse und einen Service.<br />
Zum Eintragen muss eine ISBN/BArcode und ein Benutzername angegeben werden. Gepruft werden
muss lediglich, ob das Medium zur gegebenen ISBN/Barcode vorhanden ist.

###### REST-API
| URI-Template      | Verb          | Wirkung  |
| -------------     |-------------  | ------   |
| /exemp/books/query?user={user}&isbn={isbn}               | POST          | neues Buch-Exemplar anlegen<br />Mögliche Fehler: ISBN nicht vorhanden|
| /exemp/discs/query?user={user}&barcode={barcode}         | POST          | neues CD-Exemplar anlegen<br />Mögliche Fehler: Barcode nicht vorhanden|