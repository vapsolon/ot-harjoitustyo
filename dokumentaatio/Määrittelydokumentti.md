# RIG - Määrittelydokumentti

## Yleiskuvaus
Projektin tarkoituksena on toteuttaa Random Image Generator eli satunnaiskuvageneraattori, tuttavallisemmin RIG. Perustoiminnallisuutena on luoda tietyn kokoinen, oletuksena 512x512 kuvatiedosto arpomalla jokaisen pikselin väriarvo, jolloin tuloksena on satunnainen sekasotku värikästä lumisadetta. Käyttäjä voi itse valita kuvan resoluution, ja tavallisen täysin satunnaisen generaation lisäksi tarjolla on neljä vaihtoehtoista generaatiomoodia. Lisäksi käyttäjä voi valita arvotaanko myös kuvan läpinäkyvyysarvot satunnaisiksi.

Kuvatiedostot tallennetaan väliaikaistiedostoon ja näytetään käyttöliittymässä. Halutessaan kuvan voi myös tallentaa levylle, jolloin ohjelma yksinkertaisesti kopioi jo tallennetun väliaikaistiedoston käyttäjän määrittelemään tiedostopolkuun. Ainoa tuettu kuvaformaatti on **PNG**.

## Toiminnallisuudet
Seuraava lista sisältää kaikki loppupalautuksen tukemat toiminnallisuudet.

* Satunnaiskuvien luominen
* Käyttäjäsyöte kuvan resoluution ja läpinäkyvyysarvonnan suhteen
* Erilaiset generointimoodit
* Mahdollisuus kuvien tallentamiseen haluttuun sijaintiin