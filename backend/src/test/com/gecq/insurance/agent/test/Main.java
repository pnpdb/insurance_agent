package com.gecq.insurance.agent.test;

import com.gecq.insurance.agent.service.utils.EncryptionUtils;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by gecha on 2017/1/3.
 */

public class Main {
    @Test
    public void test(){
        EncryptionUtils encryptionUtils = new EncryptionUtils();
        String pwd = "123456";
        byte[] salts = encryptionUtils.nextSalt();
        String saltsStr = encryptionUtils.byteToHexString(salts);
        String md5 = encryptionUtils.getEncryptedChar(pwd,salts);
        System.out.println("salt:"+saltsStr+" ---- pwd:"+md5);
    }
    @Test
    public void testId(){
        System.out.println(UUID.randomUUID().toString().replace("-",""));
    }
}
