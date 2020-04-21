# RIG - Arkkitehtuuridokumentti

## Luokkakaavio

![Luokkakaavio](https://raw.githubusercontent.com/vapsolon/ot-harjoitustyo/master/dokumentaatio/kuvat/Luokkakaavio.png)

##Toiminnallisuudet

##### Satunnaiskuvan generointi
Oletetaan alkutilanteeksi oletussyöte, eli kuvan kooksi **512x512**, generaatiomoodiksi **Regular** ja variaatioksi **5**

![Sekvenssikaavio](https://raw.githubusercontent.com/vapsolon/ot-harjoitustyo/master/dokumentaatio/kuvat/Sekvenssikaavio.png)

Kuvan generointi alkaa **Generate Image**-napin painamisella. Nappiin liitetty tapahtumakäsittelijä aktivoituu ja selvittää ensin mikä generaatiomoodi on aktiivisena. Tämän jälkeen UI luo kuvageneraattoriolion ja aloittaa varsinaisen generaatioprosessin kutsumalla olion **generate()**-metodia. Generaattori kirjoittaa luomansa kuvan levylle väliaikaistiedostoksi, josta UI sitten generaation valmistumisen jälkeen lataa sen näytettäväksi käyttäjälle.