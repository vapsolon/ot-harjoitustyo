# RIG - Käyttöohje

Ohjelma toimii graafisen käyttöliittymän kautta. Mahdolliset syötteet ovat:

* Kuvan leveys
* Kuvan korkeus
* Generoinnin variaatio (rajoitettu **+-** -generaatiomoodiin)
* Generaatiomoodi
  * Regular - Luo tavallisen kuvan jossa koko värimaailma on käytettävissä
  * Black and White - Luo mustavalkoisen kuvan jossa tuotetut pikselit on rajattu olemaan joko (0,0,0) tai (255,255,255)
  * +- - Arpoo alkutilaksi täysin satunnaisen pikselin, ja arpoo aina seuraavan pikselin väriarvoiksi aiemman pikselin arvot -variaatio jos satunnaislukugeneraattori tuottaa nollan, +variaatio jos se tuottaa ykkösen
  * Strong Colours - Arpoo ainoastaan "vahvoja" värejä, eli pikseleitä joissa jokaisen värin arvo on joko **0** tai **255**
  * Elimination - Hieman muokattu *Strong Colours*, jokainen pikseli on aluksi musta, ja väriarvot laitetaan päälle vain jos arvoksi sattuu tulemaan 255
* Lisävaihtoehdot
  * Alpha - Määrittää arvotaanko myös pikseleiden läpinäkyvyysarvo satunnaisesti (laatikko ruksittuna) vai jätetäänkö kaikki pikselit vahvoiksi (laatikko ruksimattomana)
  
Luotu kuva näytetään ruudulla, ja se myös tallennetaan levylle väliaikaistiedostona. Tiedoston sijainti on **%temp%/RIG.png**.

Tuotetun kuvan voi myös halutessaan tallentaa haluamaansa sijaintiin erillisellä napilla. Tämä tallenusmetodi tosin vain kopioi väliaikaistiedoston käyttäjän määrittelemään sijaintiin.