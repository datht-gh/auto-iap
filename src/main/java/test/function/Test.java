/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.function;

import com.fpt.datht.iap.AutoIAP;
import com.fpt.datht.iap.Config;
import java.io.IOException;
import play.libs.Crypto;

/**
 *
 * @author datht2@fpt.edu.vn
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
           Crypto.secretKey="123456789abcdatA";
        AutoIAP.writeConfigFile(new Config("dat","123456"));
        Config con=AutoIAP.loadConfigFile();
        System.out.println(con.pass);
    }
}
