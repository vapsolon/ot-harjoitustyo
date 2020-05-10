# RIG - Testausdokumentti

Ohjelman kaikki testaaminen tapahtuu JUnitin kautta. Ohjelmaa on myös ajettu runsaasti kehityksen aikana uusia toiminnallisuuksia ja muutoksia toteutettaessa, mutta näistä omalla koneella ajoista ei ole dokumentaatiota.

## Yksikkötestaus

Testiluokkia on kaksi, **TestImageGenerator** ja **TestValidator**. **TestImageGenerator** on paljon laajempi, sillä se testaa ohjelman ydintoiminnallisuutta, kun taas **TestValidator** kattaa ainoastaan hyvin yksinkertaisen lisäosan testaamisen.

Suurin osa testaamisesta keskittyy **ImageGenerator**-luokan **genPixel**-metodin testaamiseen. Pikseligeneroinnin testit toimivat kaikki samalla periaatteella. Testi alustaa **ImageGenerator**-olion ja luo ja testaa sitten tämän olion avulla 1000 pikseliä. Jokaisen pikselin kohdalla tarkistetaan että sen väriarvot ovat kyseisen generaatiomoodin sallimissa rajoissa, tai ne vastaavan moodin oletettuja arvoja. Lisäksi läpinäkyvyyttä tukeville generaatiomoodeille on erillinen alpha-testi, jossa luotavan kuvageneraattoriolion läpinäkyvyyslippu on laitettu päälle.

Lisäksi **ImageGenerator**-luokasta testataan tiedostoonkirjoittamismetodien toiminnallisuus, staattisen datan kuten otsakkeen generoinnin toimivuus ja luotavan kuvan olemassaolo.

**TestValidator** yksinkertaisesti varmistaa, että validaattori toimii oikein täysin numeerisen ja osittain numeerisen datan suhteen ja palauttaa tarkistuksessaan oikean totuusarvon.

### Testikattavuus

Repositorion nykyistä tilaa vastaava testikattavuusraportti löytyy aina tiedostosta [dokumentaatio/jacoco/index.html](https://vapsolon.github.io/ot-harjoitustyo/dokumentaatio/jacoco/index.html).

## Järjestelmätestaus

Käyttöliittymää ei ole testattu automaattisesti, mutta kaikki toiminnallisuudet on käyty manuaalisesti läpi sekä Windows- että Linux-pohjalla. Samoin projektin rakennus, kaikki muutkin maven-komennot sekä releaseissa olevien .jar-tiedostojen ajaminen on kokeiltu Windowsilla ja Linuxilla.

## Laatuongelmat

Käyttöliittymässä on virheraportointitoiminnallisuus, mutta joitain kuvageneraattorin virheitä - kuten vaikkapa väliaikaistiedoston kirjoittamisoikeuksien puuttumista - ei välitetä käyttöliittymälle eikä niitä täten näytetä käyttäjälle muuten kuin konsolitulosteena.