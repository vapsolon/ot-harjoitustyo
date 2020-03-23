package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void alkusaldoOnOikein() {
        assertEquals("saldo: 10.0", kortti.toString());
    }
    
    @Test
    public void rahanLisaaminenKasvattaaSaldoa() {
        kortti.lataaRahaa(250);
        assertEquals("saldo: 12.50", kortti.toString());
    }
    
    @Test
    public void maksaminenVahentaaSaldoa() {
        kortti.otaRahaa(500);
        assertEquals("saldo: 5.0", kortti.toString());
    }
    
    @Test
    public void liianSuuriSummaEiMeneLapi() {
        kortti.otaRahaa(1500);
        assertEquals(1000, kortti.saldo());
    }
    
    @Test
    public void riittavatRahatPalauttaaTrue() {
        assertTrue(kortti.otaRahaa(100));
    }
    
    @Test
    public void vahaisetRahatPalauttaaFalse() {
        assertTrue(!(kortti.otaRahaa(10000)));
    }
    
}
