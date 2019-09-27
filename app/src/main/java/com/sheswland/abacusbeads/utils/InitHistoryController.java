package com.sheswland.abacusbeads.utils;

import android.content.Context;

import com.sheswland.abacusbeads.FileController;

import java.io.File;

public class InitHistoryController {


    public void init(Context context) {

        String lab324Path = Const.DocumentPath + File.separator + "lab324" + File.separator;

        File[] files = FileController.getSubFiles(lab324Path);


    }

}
