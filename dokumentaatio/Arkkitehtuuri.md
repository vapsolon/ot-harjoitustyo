# RIG - Arkkitehtuuridokumentti

## Rakenne

![Pakkauskaavio](https://raw.githubusercontent.com/vapsolon/ot-harjoitustyo/master/dokumentaatio/kuvat/Pakkauskaavio.png)

**rig.rig** sisältää **Main**-luokan, jonka ainoa tehtävä on GUI-olion alustaminen ja käynnistäminen.

**rig.ui** sisältää **GUI**-luokan, joka luo, asettelee ja lopulta näyttää käyttöliittymän. Luokka sisältää myös käyttöliittymäkomponenttien tapahtumakuuntelun.

**rig.utils** sisältää apuluokkia jotka suorittavat ohjelman varsinaisen toiminnallisuuden. **ImageGenerator** luo satunnaiskuvia ja **Validator** tarkistaa käyttöliittymän saamien käyttäjäsyötteiden laillisuuden.

## Luokkakaavio

![Luokkakaavio](https://raw.githubusercontent.com/vapsolon/ot-harjoitustyo/master/dokumentaatio/kuvat/Luokkakaavio.png)

## Sovelluslogiikka

Sovelluksen pääasiallinen tarkoitus on luoda satunnaiskuvia käyttäjäsyötteen perusteella. Käyttäjäsyötettä varten on graafinen käyttöliittymä **GUI**. **GUI** taas luo tarpeen mukaan omaan käyttöönsä **ImageGenerator**-olioita, jotka toteuttavat varsinaisen toiminnallisuuden.

Ennen kuvan generoinnin aloittamista **GUI** tarkistaa vielä käyttäjäsyötteen kelpoisuuden **Validator**-luokan avulla. Jos syöte kelpasi (eli oli siis tässä tapauksessa numeerista), **GUI** voi luoda tarvitsemansa **ImageGenerator**-olion ilman vaaraa epäkelvon syötteen aiheuttamista kaatumisista.

## Toiminnallisuudet

#### Satunnaiskuvan generointi
Oletetaan alkutilanteeksi oletussyöte, eli kuvan kooksi **512x512**, generaatiomoodiksi **Regular** ja variaatioksi **5**

![Sekvenssikaavio](https://raw.githubusercontent.com/vapsolon/ot-harjoitustyo/master/dokumentaatio/kuvat/Sekvenssikaavio.png)

Kuvan generointi alkaa **Generate Image**-napin painamisella. Nappiin liitetty tapahtumakäsittelijä aktivoituu ja selvittää ensin mikä generaatiomoodi on aktiivisena. Tämän jälkeen UI luo kuvageneraattoriolion ja aloittaa varsinaisen generaatioprosessin kutsumalla olion **generate()**-metodia. Generaattori kirjoittaa luomansa kuvan levylle väliaikaistiedostoksi, josta UI sitten generaation valmistumisen jälkeen lataa sen näytettäväksi käyttäjälle.