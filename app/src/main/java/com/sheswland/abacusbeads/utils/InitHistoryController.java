package com.sheswland.abacusbeads.utils;

import android.content.Context;

import com.sheswland.abacusbeads.FileController;
import com.sheswland.abacusbeads.database.DataBaseManager;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class InitHistoryController {
    private final String TAG = "InitHistoryController";

    public void init(Context context) {
        String lab324Path = Const.DocumentPath + File.separator + "lab324" + File.separator;
        File[] files = FileController.getSubFiles(lab324Path);
        for (File file : files) {
            DebugLog.d(TAG, "file name " + file.getName());
            if (".txt".equals(TextUtil.getExtensionWithDot(file.getName()))){
                handleFile(file);
            }
        }
    }

    private void handleFile(File file) {
        String fileName = file.getName();

        String timeString = fileName.substring("record".length(), fileName.lastIndexOf("."));
        int year = Integer.parseInt(timeString.substring(0, 4));
        int month = Integer.parseInt(timeString.substring(4, 6));
        int day = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            int lineNumber = 1;
            while ( (line = reader.readLine()) != null ) {
                String[] lines = line.split("\\s+");
                if (lines.length < 4) {
                    DebugLog.d(TAG, "not line at " + lineNumber);
                    continue;
                }

                day = Integer.parseInt(lines[0].substring(lines[0].lastIndexOf("-") + 1));
                StringBuilder sb = new StringBuilder();
                for (int index = 1; index < lines.length - 2; index++) {
                    sb.append(lines[index]);
                    sb.append(" ");
                }
                String content = sb.toString();
                float money = Float.parseFloat(lines[lines.length-2]);
                float remainMoney = Float.parseFloat(lines[lines.length-1]);

                DebugLog.d(TAG, "year month day " + year + " " + month + " " + day);

                Date date = TimeUtil.getDate(new int[]{year, month, day});
                OperateDataTable table = (OperateDataTable) DataBaseManager.getInstance().produceTable(Const.TableType.OPERATE_TAB, date, null);

                table.setIncome(money < 0);
                table.setSpend(Math.abs(money));
                table.setContent(content);
                table.setRemain(remainMoney);

                DataBaseManager.getInstance().saveTable(table, false);

                lineNumber++;
            }
            reader.close();

            int[] ydm = new int[] {year, month, day};
            FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_DAY, Const.FilterAccuracy.month, ydm);
            FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_month, ydm);
            FileController.getInstance().printTable2SD(Const.TableType.ACCOUNT_MONTH_AND_YEAR, Const.FilterAccuracy.all_year, ydm);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
