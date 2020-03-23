package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;

    @Before
    public void setUp(){
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
    }
    
    @Test
    public void alkusaldoOnOikein(){
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void alussaMaukkaitaMyytyNolla(){
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void alussaEdullisiaMyytyNolla(){
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasOstoKasvattaaSaldoa(){
        kassa.syoMaukkaasti(400);
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
    @Test
    public void maukasOstoPalauttaaVaihtorahaa(){
        int vaihto = kassa.syoMaukkaasti(600);
        assertEquals(200, vaihto);
    }
    
    @Test
    public void maukasOstoKasvattaaOstojenMaaraa(){
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void riittamatonOstoPalautetaan(){
        int vaihto = kassa.syoMaukkaasti(300);
        assertEquals(300, vaihto);
    }
    
    @Test
    public void riittamatonEdullinenOstoPalautetaan(){
        int vaihto = kassa.syoEdullisesti(150);
        assertEquals(150, vaihto);
    }
    
    @Test
    public void riittamatonOstoEiKasvataOstojenMaaraa(){
        kassa.syoMaukkaasti(300);
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void riittamatonOstoEiKasvataSaldoa(){
        kassa.syoMaukkaasti(300);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void edullinenOstoKasvattaaSaldoa(){
        kassa.syoEdullisesti(240);
        assertEquals(100240, kassa.kassassaRahaa());
    }
    
    @Test
    public void edullinenOstoKasvattaaOstojenMaaraa(){
        kassa.syoEdullisesti(250);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKorttiostoToimii(){
        assertTrue(kassa.syoEdullisesti(kortti));
    }
    
    @Test
    public void liianPieniSaldoKortillaEiToimi(){
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        assertTrue(!(kassa.syoEdullisesti(kortti)));
    }
    
    @Test
    public void liianPieniSaldoKortillaEiToimiMaukkaassa(){
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertTrue(!(kassa.syoMaukkaasti(kortti)));
    }
    
    @Test
    public void edullinenKorttiostoVeloittaaSumman(){
        kassa.syoEdullisesti(kortti);
        assertEquals(760, kortti.saldo());
    }
    
    @Test
    public void edullinenKorttiostoKasvattaaMyyntimaaraa(){
        kassa.syoEdullisesti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void liianPieniSaldoEiKasvataMyyntimaaraa(){
        kassa.syoMaukkaasti(kortti);
        kassa.syoMaukkaasti(kortti);
        kassa.syoEdullisesti(kortti);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void rahanLataaminenLisaaKortinSaldoa(){
        kassa.lataaRahaaKortille(kortti, 1000);
        assertEquals(2000, kortti.saldo());
    }
    
    @Test
    public void rahanLataaminenLisaaKassanSaldoa(){
        kassa.lataaRahaaKortille(kortti, 1000);
        assertEquals(101000, kassa.kassassaRahaa());
    }
    
    @Test
    public void NegatiivisenSummanLataaminenEiMuutaMitaan(){
        kassa.lataaRahaaKortille(kortti, -10);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
}
