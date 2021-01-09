package test;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
    public static void main(String[] args) {


        try {

            //エクセルファイルへアクセスするためのオブジェクト
            Workbook excel;
            excel = WorkbookFactory.create(new File("data_file/ReserveData.xlsx"));

            // シート名がわかっている場合
            Sheet sheet = excel.getSheet("Sheet1");

            for (int i = 1; i < 50; i++) { //行目から10行目までを繰り返し
                Row row = sheet.getRow(i); //2~10行目まで行を読み込み
                Cell cell = row.getCell(2); //Cellを指定
                String value = cell.getStringCellValue(); //指定した場所の文字列を取得
                System.out.println(i); //件数
                System.out.println(value); //取得したデータを出力

            }

        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
    }

}
