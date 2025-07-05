package com.khanghoang;

import com.khanghoang.di.AppContext;

public class ChatioApplication {
    public static void run() {
        AppContext context = new AppContext();
        context.startAll();
    }
}
