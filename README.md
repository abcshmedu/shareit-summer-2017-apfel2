# Starter Code für 2. Pratkikumsaufgabe Software-Architektur Sommer 2017 #
 
Carolin Direnberger, Maximilian Lipp, Juliane Seidl, Florian Tobusch

##### Resource-Server ohne Authorization Server
Link zu Heroku (frontend):<br />
https://soft-arch-lab2-shareit.herokuapp.com<br />
<br />
Link zu Heroku (REST):<br />
https://soft-arch-lab2-shareit.herokuapp.com/shareit<br />

##### Resource-Server (RS) mit Authorization Server (AS)
Link zu Heroku RS (REST):<br />
https://soft-arch-lab2-shareit.herokuapp.com/shareit<br />
<br />
Link zu Heroku (AS) (REST):<br />
https://jularo.herokuapp.com/shareit<br />

##### REST-API
| URI-Template      | Verb          | Wirkung  |
| -------------     |-------------  | ------   |
| /media/books?token={token}               | POST          | Neues Medium Buch anlegen<br />Moeglicher Fehler: Ungueltige ISBN<br />Moeglicher Fehler: ISBN bereits vorhanden<br />Moeglicher Fehler: Autor oder Titel fehlt|
| /media/books/{isbn}?token={token}        | GET           | Eine JSON-Repraesentation eines gespeicherten Buches liefern, falls vorhanden|
| /media/books?token={token}        | GET           | Alle Buecher aufisten |
| /media/books/{isbn}?token={token}        | PUT           | Daten zu vorhandenem Buch modifzieren (JSONDaten enthalten nur die zu modifzierenden Attribute)<br />Moeglicher Fehler: ISBN nicht gefunden<br />Moeglicher Fehler: ISBN soll modifziert werden (also die JSON-Daten enthalten eine andere ISBN als die URI)<br />Moeglicher Fehler: Autor und Titel fehlen |
|   <br />    | <br /> |  <br />  |
| /media/discs?token={token}               | POST          | Neues Medium Disc anlegen<br />Moeglicher Fehler: Ungueltiger Barcode<br />Moeglicher Fehler: Barcode bereits vorhanden<br />Moeglicher Fehler: Titel, Regisseur oder fsk fehlt|
| /media/discs/{barcode}?token={token}        | GET           | Eine JSON-Repraesentation einer gespeicherten Disc liefern, falls vorhanden|
| /media/discs?token={token}        | GET           | Alle Discs aufisten |
| /media/discs/{barcode}?token={token}        | PUT           | Daten zur vorhandener Disc modifzieren (JSONDaten enthalten nur die zu modifzierenden Attribute)<br />Moeglicher Fehler: Barcode nicht gefunden<br />Moeglicher Fehler: Barcode soll modifziert werden (also die JSON-Daten enthalten eine andere ISBN als die URI)<br />Moeglicher Fehler: Titel, Regisseur und fsk fehlen |