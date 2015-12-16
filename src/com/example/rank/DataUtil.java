package com.example.rank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 13/07/2015
 * Time: 09:41
 */
public class DataUtil {

    private static int[] retornaDataMesAno(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new int[]{calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)};
    }

    public static boolean datasIguais(Date d1, Date d2) {
        int[] d1Valores = retornaDataMesAno(d1);
        int[] d2Valores = retornaDataMesAno(d2);
        return d1Valores[0] == d2Valores[0] && d1Valores[1] == d2Valores[1] && d1Valores[2] == d2Valores[2];
    }

    public static List<Date> getDatasEntreDatas(Date dataInicial, Date dataFinal) {
        Calendar dataIni = Calendar.getInstance();
        dataIni.setTime(dataInicial);
        Calendar dataFim = Calendar.getInstance();
        dataFim.setTime(dataFinal);
        List<Date> datas = new ArrayList<>();

        boolean done = false;
        datas.add(dataInicial);
        while (!done) {
            dataIni.add(Calendar.DATE, 1);
            if (dataIni.getTime().before(dataFinal) || datasIguais(dataIni.getTime(), dataFinal)) {
                datas.add(dataIni.getTime());
            } else {
                done = true;
            }
        }
        return datas;
    }
}
