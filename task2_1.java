//*Получить исходную json строку из файла, используя FileReader или Scanner
//        Дана json строка вида:
//        String json = "[{\"фамилия\":\"Иванов\",\"оценка\":\"5\",\"предмет\":\"Математика\"}," +
//        "{\"фамилия\":\"Петрова\",\"оценка\":\"4\",\"предмет\":\"Информатика\"}," +
//        "{\"фамилия\":\"Краснов\",\"оценка\":\"5\",\"предмет\":\"Физика\"}]";
//
//        Задача написать метод(ы), который распарсить строку и выдаст ответ вида:
//        Студент Иванов получил 5 по предмету Математика.
//        Студент Петрова получил 4 по предмету Информатика.
//        Студент Краснов получил 5 по предмету Физика.
//
//        Используйте StringBuilder для подготовки ответа. Далее создайте метод, который запишет
//        результат работы в файл. Обработайте исключения и запишите ошибки в лог файл с помощью Logger.
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class task2_1 {
    static Logger logger;
    public static void main(String[] args) {
        String filePath = "json.txt";
        String []symbol= new String[]{"{","}","[","]","\"","\\","+"};
        startLogger();
        String str = readInFile(filePath);
        manipulation(symbol,str);
        System.out.println(manipulation(symbol,str));
        filePath = "result.txt";
        writeToFile(manipulation(symbol,str), filePath);
        finishLogger();
    }

    static String manipulation(String[]symbol, String str){
        for (int i = 0; i < symbol.length; i++) {
            str = str.replace(symbol[i],"");
        }
        str = str.replace(":",",");
        String [] xxx = str.split(",");
        StringBuilder student = new StringBuilder();
        for (int i = 0; i < xxx.length; i+=6) {
         try {
             student.append("Cтудент " + xxx[i + 1]);
             student.append(" получил " + xxx[i + 3]);
             student.append(" по предмету " + xxx[i + 5] + "\n");
         }
         catch (Exception e){
             e.printStackTrace();
             logger.warning("Проверьте файл с заданной последовательностью");
             logger.warning(e.toString());
             System.exit(1);
         }
        }


        return (student.toString());
    }
    static String readInFile(String filePath) {
        File file = new File(filePath);
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNext()){
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
        } catch (IOException e){
//            e.printStackTrace();
            logger.warning(e.toString());
            System.exit(1);

        }
        return stringBuilder.toString();
    }

    static void finishLogger() {
        for (Handler handler: logger.getHandlers()){
            handler.close();
        }
    }
    static void startLogger() {
        logger = Logger.getAnonymousLogger();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("log.txt", true);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleFormatter formatter = new SimpleFormatter();
        if (fileHandler != null){
            fileHandler.setFormatter(formatter);
        }
    }

    static void writeToFile(String s1, String filePath) {

        try (FileWriter writer = new FileWriter(filePath, true)){
            writer.write(s1);
            writer.write("\n");
            writer.flush();
            logger.info("Запись успешно выполнена");
        } catch (Exception e){
            e.printStackTrace();
            logger.warning("Не удалось записать в файл");
        }
    }
}
