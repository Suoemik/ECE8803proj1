package com.example.suoemi.ece8803proj;

/**
 * Created by Suoemi on 3/30/2017.
 */

public class LoginData {
    private int id;
    private String username;
    private String password;
    private String number;
    private int evReq;
    private int eBid;
    private int ePrice;
    private int check;
    private int join;

    public LoginData(){

    }

    public LoginData(int id, String username, String password, String number, int quant, int price, int check, int join){
        this.id = id;
        this.username = username;
        this.password = password;
        this.number = number;
        this.evReq = quant;
        this.eBid = quant;
        this.ePrice = price;
        this.check = check;
        this.join = join;
    }

    public LoginData(String username, String password, String number, int quant, int price, int check, int join){
        this.username = username;
        this.password = password;
        this.number = number;
        this.evReq = quant;
        this.eBid = quant;
        this.ePrice = price;
        this.check = check;
        this.join = join;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setEvReq(int evReq){
        this.evReq = evReq;
    }
    public void seteBid(int eBid){
        this.eBid = eBid;
    }
    public void setePrice(int ePrice){
        this.ePrice = ePrice;
    }
    public void setCheck(int check){ this.check = check; }
    public void setJoin(int join){ this.join = join; }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNumber() {
        return number;
    }

    public int getEVReq() {
        return evReq;
    }

    public int geteBid() {
        return eBid;
    }

    public int getePrice() {
        return ePrice;
    }

    public int getCheck(){ return check; }

    public int getJoin(){ return join; }
}
