/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.domain;

/**
 *
 * @author josujosu
 */
public class Viesti {

    private Integer id;
    private String sisalto;
    private Integer keskustelu;
    private String lahettaja;

    public Viesti(Integer id, String sisalto, Integer keskustelu, String lahettaja) {
        this.id = id;
        this.sisalto = sisalto;
        this.keskustelu = keskustelu;
        this.lahettaja = lahettaja;
    }

    public Viesti(String sisalto, Integer keskustelu, String lahettaja) {
        this(null, sisalto, keskustelu, lahettaja);
    }

    public Integer getId() {
        return id;
    }

    public Integer getKeskustelu() {
        return keskustelu;
    }

    public String getLahettaja() {
        return lahettaja;
    }

    public void setLahettaja(String lahettaja) {
        this.lahettaja = lahettaja;
    }

    public String getSisalto() {
        return sisalto;
    }
    
    public String getShortsisalto() {
        if (this.sisalto.length() > 200) {
            StringBuilder uusi = new StringBuilder("");
            for (int i = 0; i < 200; i++) {
                uusi.append(this.sisalto.charAt(i));
            }
            uusi.append("...");
            return uusi.toString();
        }
        return sisalto;
    }

    public String getShortlahettaja() {
        if (this.lahettaja.length() > 40) {
            StringBuilder uusi = new StringBuilder("");
            for (int i = 0; i < 40; i++) {
                uusi.append(this.lahettaja.charAt(i));
            }
            uusi.append("...");
            return uusi.toString();
        }
        return lahettaja;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setKeskustelu(Integer keskustelu) {
        this.keskustelu = keskustelu;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.sisalto + " | " + this.keskustelu;
    }

}
