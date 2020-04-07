# RIG - Käyttöohje

Tominnallisuus on vielä tällä hetkellä hyvin yksinkertainen. Mahdolliset syötteet ovat:

* Kuvan leveys (ei validoitu)
* Kuvan korkeus (ei validoitu)
* Generaatiomoodi
  * Regular - Luo tavallisen kuvan jossa koko värimaailma on käytettävissä
  * Black and White - Luo mustavalkoisen kuvan jossa tuotetut pikselit on rajattu olemaan joko (0,0,0) tai (255,255,255)
  
Luotu kuva näytetään ruudulla, ja se myös tallennetaan levylle väliaikaistiedostona. Tiedoston sijainti on **%temp%/RIG.png**.