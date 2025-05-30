package com.dra.inventory_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class CommonUtil {

    public static String generateNewProductCode(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + "-" + randomNum;
    }

}
