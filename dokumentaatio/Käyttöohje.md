# RIG - Käyttöohje

Ohjelma toimii graafisen käyttöliittymän kautta. Mahdolliset syötteet ovat:

* Kuvan leveys
* Kuvan korkeus
* Generoinnin variaatio (rajoitettu **+-** -generaatiomoodiin)
* Generaatiomoodi
  * Regular - Luo tavallisen kuvan jossa koko värimaailma on käytettävissä
  * Black and White - Luo mustavalkoisen kuvan jossa tuotetut pikselit on rajattu olemaan joko (0,0,0) tai (255,255,255)
  * +- - Arpoo alkutilaksi täysin satunnaisen pikselin, ja arpoo aina seuraavan pikselin väriarvoiksi aiemman pikselin arvot -variaatio jos satunnaislukugeneraattori tuottaa nollan, +variaatio jos se tuottaa ykkösen
  
Luotu kuva näytetään ruudulla, ja se myös tallennetaan levylle väliaikaistiedostona. Tiedoston sijainti on **%temp%/RIG.png**.

Tuotetun kuvan voi myös halutessaan tallentaa haluamaansa sijaintiin erillisellä napilla. Tämä tallenusmetodi tosin vain kopioi väliaikaistiedoston käyttäjän määrittelemään sijaintiin.