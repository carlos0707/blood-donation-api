package br.com.carlos.blooddonationapi.helper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateHelper {

    private static final String ZONE_ID = "America/Sao_Paulo";

    public static String formatDate(String date, String pattern) {
        try{

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            LocalDateTime parsedDate    = LocalDateTime.ofInstant(dateFormat.parse(date).toInstant(), ZoneId.of(ZONE_ID));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

            return parsedDate.format(formatter);
        } catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatedDateYear(Integer years, String ofPattern )
    {
        LocalDateTime localdate     = LocalDateTime.now(ZoneId.of(ZONE_ID)).minusYears(years);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ofPattern);

        return localdate.format(formatter);
    }

}
