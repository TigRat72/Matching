import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String fIn = "input.txt";
        String fOut = "output.txt";
        System.out.println("Written by Petrosyants Lev");
        final List<Position> firstData = new ArrayList<>();
        final List<Position> secondData = new ArrayList<>();
        switch (args.length) {
            case 2:
                fOut = args[1];
            case 1:
                fIn = args[0];
            default:
                System.out.println("Input file  : " + fIn + "\nOutput file : " + fOut);
                break;
        }
        final List<String> allLines;
        System.out.print("Load data...");
        try {
            allLines = Files.readAllLines(Paths.get(fIn), StandardCharsets.UTF_8);
            int first = Integer.parseInt(allLines.get(0));
            int globalIndex = 1;
            for (int i = 0; i < first; i++) {
                firstData.add(new Position(allLines.get(globalIndex++)));
            }
            int second = Integer.parseInt(allLines.get(globalIndex++));
            for (int i = 0; i < second; i++) {
                secondData.add(new Position(allLines.get(globalIndex++)));
            }
        } catch (IOException e) {
            System.out.println("WARNING! Some problems with your input files:\n" + fIn);
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("done.");

//Построение матрицы соответствия
        int[][] matr = new int[firstData.size()][secondData.size()];
        for (int i = 0; i < firstData.size(); i++) {
            for (int k = 0; k < secondData.size(); k++) {
                matr[i][k] = firstData.get(i).compare(secondData.get(k));
            }
        }
//Нахождение наилучшего соответствия через поиск максимума
        for (int l = 0; l < firstData.size(); l++) {
            int indMaxF = 0;
            int indMaxS = 0;
            for (int index = 0; index < firstData.size(); index++) {    //Находим максимум
                for (int innerIndex = 0; innerIndex < secondData.size(); innerIndex++) {
                    if (matr[index][innerIndex] >= matr[indMaxF][indMaxS]) {
                        indMaxF = index;
                        indMaxS = innerIndex;
                    }
                }
            }
            if (matr[indMaxF][indMaxS] >= 0) {   //было ли изменение
                firstData.get(indMaxF).setIndCorr(indMaxS);     //расстановка соответствий
                secondData.get(indMaxS).setIndCorr(indMaxF);
            }
            for (int i = 0; i < firstData.size(); i++) {    //убираем найденные строку/столбец
                matr[i][indMaxS] = -1;
            }
            for (int k = 0; k < secondData.size(); k++) {
                matr[indMaxF][k] = -1;
            }

        }
        try (FileWriter writer = new FileWriter(fOut, StandardCharsets.UTF_8, false);) {    //вывод результатов
            for (Position p : firstData) {
                writer.write(p.getText() + ":" + ((p.getIndCorr() >= 0) ? secondData.get(p.getIndCorr()).getText() : "?") + "\n");
            }
            for (Position p : secondData) {
                if (p.getIndCorr() < 0) {
                    writer.write(p.getText() + ":?\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("WARNING! Some problems with your output files:\n" + fOut);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
