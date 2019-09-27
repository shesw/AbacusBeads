package com.sheswland.abacusbeads;

import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.utils.Const;
import com.sheswland.abacusbeads.utils.DebugLog;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.sheswland.abacusbeads.utils.Const.TableType;
import static com.sheswland.abacusbeads.utils.Const.divide;

public class FileController {

    private final static String TAG = "FileController";

    private static FileController _HOLDER;
    public static FileController getInstance() {
        if (_HOLDER == null) {
            _HOLDER = new FileController();
        }
        return _HOLDER;
    }

    public void printTable2SD(Const.TableType type, Const.FilterAccuracy accuracy, int[] time) {
        String filePath = Const.getPrintPath(Const.subFileName);

        String[] dbFiles = new String[]{Const.dayTableSubFile, Const.monthTableSubFile, Const.yearTableSubFile};

        for (String dbFile : dbFiles) {
            File fileDirectory = new File(filePath + dbFile + File.separator);
            if (!fileDirectory.exists()) {
                DebugLog.d(TAG, "mkdirs " + filePath + " " + String.valueOf(fileDirectory.mkdirs()));
            }
        }

        if (type == TableType.ACCOUNT_DAY) {
            printDayTable(time, filePath);
        } else if (type == TableType.ACCOUNT_MONTH_AND_YEAR) {
            if (accuracy == Const.FilterAccuracy.all_month) {
                printMonthTable(time, filePath);
            } else if (accuracy == Const.FilterAccuracy.all_year) {
                printYearTable(filePath);
            }
        }
    }

    public static File[] getSubFiles(String directory) {
        File file = new File(directory);
        if (file.exists() && file.isDirectory()) {
            if (file.listFiles() != null && file.listFiles().length > 0) {
                for (File file1 : file.listFiles()) {
                    DebugLog.d(TAG, "file name: " + file1.getName());
                }
                return file.listFiles();
            }
        }
        return null;
    }

    /******************** private *****************/
    private void printDayTable(int[] time, String path) {
        if (time.length < 2) return;
        String fileName = "day_table_" + time[0] + "" + time[1] + ".txt";

        File targetFile = new File(path + Const.dayTableSubFile + File.separator + fileName);

        DebugLog.d(TAG, "targetFile " + targetFile.getAbsolutePath() + " " + time[0] + " "  + time[1]);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            int line = 0;
            writer.write("日期" + divide + "内容" + divide + "类型" + divide + "金额" + divide + "余额");
            writer.newLine();
            line++;
            ArrayList<AccountDayTable> list = (ArrayList<AccountDayTable>) DataBaseManager.getInstance().query(TableType.ACCOUNT_DAY, "year = ? and month = ?", String.valueOf(time[0]), String.valueOf(time[1]));

            for (AccountDayTable dayTable : list) {
                DebugLog.d(TAG, "writer line " + line);
                writer.write(dayTable.getDate() + divide +
                        dayTable.getContent() + divide +
                        (dayTable.isIncome() ? "收入" : "支出") + divide +
                        dayTable.getSpend() + divide +
                        dayTable.getRemain());
                writer.newLine();
                line++;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            DebugLog.d(TAG, "exception " + e.toString());
            e.printStackTrace();
        }

    }

    private void printMonthTable(int[] time, String path) {
        if (time.length < 1) return;
        String fileName = "month_table_" + time[0] + ".txt";

        File targetFile = new File(path + Const.monthTableSubFile + File.separator + fileName);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            int line = 0;
            writer.write("日期" + divide + "支出" + divide + "收入" + divide + "盈余");
            writer.newLine();
            line++;
            ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) DataBaseManager.getInstance().query(TableType.ACCOUNT_MONTH_AND_YEAR,
                    "table_id = ? and year = ?", DataBaseManager.getInstance().getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, null, Const.FilterAccuracy.all_month),
                    String.valueOf(time[0]));

            for (AccountMonthAndYearTable dayTable : list) {
                DebugLog.d(TAG, "printMontTable line " + line);
                writer.write(dayTable.getDate() + divide +
                        dayTable.getSpend() + divide +
                        dayTable.getIncome() + divide +
                        dayTable.getRemain());
                writer.newLine();
                line++;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printYearTable(String path) {

        String fileName = "month_table_year_all.txt";

        File targetFile = new File(path + Const.yearTableSubFile + File.separator + fileName);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile));
            int line = 0;
            writer.write("日期" + divide + "支出" + divide + "收入" + divide + "盈余");
            writer.newLine();
            line++;
            ArrayList<AccountMonthAndYearTable> list = (ArrayList<AccountMonthAndYearTable>) DataBaseManager.getInstance().query(TableType.ACCOUNT_MONTH_AND_YEAR,
                    "table_id = ?", DataBaseManager.getInstance().getTableId(TableType.ACCOUNT_MONTH_AND_YEAR, null, Const.FilterAccuracy.all_year));

            for (AccountMonthAndYearTable dayTable : list) {
                writer.write(dayTable.getDate() + divide +
                        dayTable.getSpend() + divide +
                        dayTable.getIncome() + divide +
                        dayTable.getRemain());
                writer.newLine();
                line++;
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
