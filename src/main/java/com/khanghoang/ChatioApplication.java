package com.khanghoang;

import com.khanghoang.di.AppContext;

import java.sql.SQLException;

public class ChatioApplication {
    public static void run()  {
        AppContext context = new AppContext();
        context.startAll();
    }
}
