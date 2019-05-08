package com.example.peach.pojo.pay;

public class TemplateData {
    //keyword1：订单类型，keyword2：下单金额，keyword3：配送地址，keyword4：取件地址，keyword5备注
    private String value;//,,依次排下去

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
