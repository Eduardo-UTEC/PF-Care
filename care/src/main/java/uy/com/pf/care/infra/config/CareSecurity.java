package uy.com.pf.care.infra.config;

import org.jasypt.util.text.BasicTextEncryptor;

public class CareSecurity {
    private final BasicTextEncryptor basicTextEncryptor;

    public CareSecurity(){
        basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(System.getenv("carekey"));
    }

    public String encrypt(String data){
        return basicTextEncryptor.encrypt(data);
    }

    public String decrypt(String data){
        return basicTextEncryptor.decrypt(data);
    }

}
