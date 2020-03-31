# RIG - Määrittelydokumentti

## Yleiskuvaus
Projektin tarkoituksena on toteuttaa Random Image Generator eli satunnaiskuvageneraattori, tuttavallisemmin RIG. Perustoiminnallisuutena on luoda tietyn kokoinen, luultavasti 256x256 tai 512x512 kuvatiedosto arpomalla jokaisen pikselin väriarvo, jolloin tuloksena on satunnainen sekasotku värikästä lumisadetta. Perustoiminnallisuuden valmistuttua toimintaa voidaan hioa ja erikoistaa, esimerkiksi tarjoamalla mahdollisuus käyttäjäsyötteeseen kuvan resoluution, käytettävien värien määrän ja väripaletin sekä mahdollisesti arvontasääntöjen osalta.

Kuvatiedosto olisi tarkoitus pystyä näyttämään sovelluksen käyttöliittymässä suoraan ja sen tallentamiseen tulisi olla mahdollisuus napin kautta, joten kuvaformaatti on alustavasti joko PNG, JPEG tai GIF. Jos yksikään näistä binääridataformaateista ei kuitenkaan jostain syystä toimi, PPM on kohtalainen fallback, joskin tällöin kuvan näyttämiseen käyttöliittymässä täytyisi keksiä jokin luova kiertotie.

## Toiminnallisuudet
Vielä lyhyesti ja tiiviisti

* Satunnaiskuvan luominen ilman parametreja
* Käyttäjäsyöte ainakin kuvan resoluution ja värien määrän suhteen
* Erilaiset generointisäännöt tai generointimoodit ohjaamaan satunnallisuutta
  * Esimerkiksi "tietyn värinen pikseli voi elää vain itsestään korkeintaan +-10 väriarvon päässä olevan pikselin naapurina" tai "tiettyyn kohtaan arvottu pikseli kasvattaa ympärilleen viiden pikselin säteelle perheen"