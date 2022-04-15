import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Position {     //Класс для позиции
    private final String text;  //Исходный текст позиции
    private final List<String> word;    //Все слова входящие в позицию
    private int indCorr;    //индекс коррелирующего соответствия

    public Position (String text) {     //Конструктор позиции
        this.text = text;
        indCorr = -1;   //значит соответствия нет
        word = new ArrayList<>(Arrays.stream(text.split(" ")).toList());    //Добавляем слова в список
//        System.out.println(Arrays.toString(word.toArray()));    //
    }

    public int compare(Position position) {     //Сравнение на "похожесть" и возврат целочисленного условного индекса "похожести" (на практике лучше использовать числа с плавающей запятой)
        int ret = 0;
        if (text.equalsIgnoreCase(position.getText())) {    //сравнение двух строк на идентичность (без учета регистра)
            return 2000;     //Если совпадает вернуть индекс 2000 (условная оценка 2 * 1000)
        }
        // Сбор статистики "похожести"
        int wlw = 1000 / word.size();   //Определение весовое для одного локального слова
        int wow = 1000 / position.getWord().size(); //для одного внешнего слова
        for (String lw : word) {

            for (String ow : position.getWord()) {
                if (lw.equalsIgnoreCase(ow)) {  //если слова совпадают
                    ret += wlw + wow;       //добаавить их весовые коэф к результату
                } else {
                    ret += wordComp(lw.toLowerCase(), wlw, ow.toLowerCase(), wow);
                }
            }
        }
        return ret;
    }

    private int wordComp(String first, int weightFirst, String second, int weightSecond) {      //Сравнение двух слов с весовыми коэф.
        if (!(first.length() == 1 || second.length() == 1)) {   //не сравниваем если любое слово всего 1 символ
            String vol1;
            String vol2;
            int wVol1;
            int wVol2;
            if (first.length() > second.length()) {     //Первому значению назначается слово наименьшей длинны
                vol2 = first;
                wVol2 = weightFirst;
                vol1 = second;
                wVol1 = weightSecond;
            } else {
                vol1 = first;
                wVol1 = weightFirst;
                vol2 = second;
                wVol2 = weightSecond;
            }
            if (vol1.length() < 4) {    //Если первое слово 3 и менее знаков
                if (vol2.contains(vol1)) {  //и содержится во втором слове
                    return wVol2 * vol1.length() / vol2.length() + wVol1;   //пересчитать веса
                }
            } else {
                for (int i = 0; i < vol1.length() - 3; i++) {   //проход с уменьшением первых знаков
                    for (int k = 0; k < vol1.length() - i - 3; k++) {   //но первично отбрасывание последних знаков
                        if (vol2.contains(vol1.substring(i, vol1.length() - k))) {
                            int len = vol1.length() - i - k;    //расчет длинны совпадения
                            return (wVol1 * len / vol1.length()) + (wVol2 * len / vol2.length());   //пересчет коэфф. совпадения
                        }
                    }
                }
            }
        }
        return 0;
    }

    public String getText() {
        return text;
    }

    public List<String> getWord() {
        return word;
    }

    public int getIndCorr() {
        return indCorr;
    }

    public void setIndCorr(int indCorr) {
        this.indCorr = indCorr;
    }
}
