package br.com.devdojo.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component  //Scaneada pelo ComponentScan usado na ApplicationStart
//@Repository //anotação especialização do @Component deve ser usado quando trabalhar com DAO para retornar e tratar as exceptions
//@Service  //tbm especialização do @Component para uso de serviços
public class DateUtil {
    public String formaLocalDateTimeToDataBaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }
}
