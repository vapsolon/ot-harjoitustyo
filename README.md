# Ohjelmistotekniikka 2020 palautukset

## RIG

[Määrittelydokumentti](https://github.com/vapsolon/ot-harjoitustyo/blob/master/dokumentaatio/Määrittelydokumentti.md)

[Arkkitehtuuridokumentti](https://github.com/vapsolon/ot-harjoitustyo/blob/master/dokumentaatio/Arkkitehtuuri.md)

[Käyttöohje](https://github.com/vapsolon/ot-harjoitustyo/blob/master/dokumentaatio/Käyttöohje.md)

[Työaikakirjanpito](https://github.com/vapsolon/ot-harjoitustyo/blob/master/dokumentaatio/Työaikakirjanpito.md)

---

[Javadoc](https://vapsolon.github.io/ot-harjoitustyo/dokumentaatio/javadoc/index.html)

[Jacoco](https://vapsolon.github.io/ot-harjoitustyo/dokumentaatio/jacoco/index.html)

[Checkstyle](https://vapsolon.github.io/ot-harjoitustyo/dokumentaatio/checkstyle/checkstyle.html)

## Releaset

[Viikko 5](https://github.com/vapsolon/ot-harjoitustyo/releases/tag/Viikko5)

[Viikko 6](https://github.com/vapsolon/ot-harjoitustyo/releases/tag/Viikko6)

**[Loppupalautus](https://github.com/vapsolon/ot-harjoitustyo/releases/tag/Loppupalautus)**

## Komentorivitoiminnot

### Testaus

Testit voidaan ajaa komennolla

```
mvn test
```

ja testikattavuusraportti luoda komennolla

```
mvn jacoco:report
```

jolloin raporttia voi tarkastella selaimen kautta avaamalla tiedoston **target/site/jacoco/index.html**

### Rakentaminen

Projektin saa rakennettua komennolla

```
mvn package
```

jolloin hakemistoon **target** rakentuu kaksi .jar-tiedostoa, **RIG-1.0-SNAPSHOT.jar** ja **RIG-1.0-SNAPSHOT-jar-with-dependencies.jar**. Näistä **with-dependencies** sisältää kaikki projektin riippuvuudet pakattuna mukaan, jolloin tiedoston voi ajaa tuosta vain, mutta se on varsinaista pakettia paljon raskaampi. Pelkkä **SNAPSHOT** on sen sijaan ultrakevyt, mutta vaatii käyttäjältä olemassaolevan **openjfx**-asennuksen.

### JavaDoc

Javadoc-generointiin komento on

```
mvn javadoc:javadoc
```

joka tuottaa valmiin dokumentaation kansioon **target/site/apidocs/**. Tulosta voi parhaiten tarkastella avaamalla tiedoston **index.html** selaimella.

### Checkstyle

Projektin Checkstyle-määrittely löytyy tiedostosta [checkstyle.xml](https://github.com/vapsolon/ot-harjoitustyo/blob/master/checkstyle.xml), ja varsinainen tarkistuskomento on

```
 mvn jxr:jxr checkstyle:checkstyle
```

jolloin tulosta pääsee tarkistelemaan selaimella tiedoston **target/site/checkstyle.html** kautta