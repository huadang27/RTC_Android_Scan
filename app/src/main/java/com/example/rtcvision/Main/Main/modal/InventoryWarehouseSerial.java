package com.example.rtcvision.Main.Main.modal;

import java.util.Date;
import java.util.List;

public class InventoryWarehouseSerial {
    private String code;

    private String materialCode;

    private String areaCode;

    private int quantity;

    private List<String> serialCode;

    private String loginName;

    public InventoryWarehouseSerial(String code, String materialCode, String areaCode, int quantity, List<String> serialCode, String loginName) {
        this.code = code;
        this.materialCode = materialCode;
        this.areaCode = areaCode;
        this.quantity = quantity;
        this.serialCode = serialCode;
        this.loginName = loginName;
    }

    public InventoryWarehouseSerial() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(List<String> serialCode) {
        this.serialCode = serialCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
