package com.khanghoang.server;

import com.khanghoang.server.di.AppContext;

public class ChatioApplication {
    public static void run()  {
        AppContext context = new AppContext();
        context.startAll();
    }
}
