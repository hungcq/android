package com.example.mrbom.intentservicedemo;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by MrBom on 7/31/2016.
 */
public class MyService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyService(String name) {
        super(name);
    }

    public MyService() {
        super("hihi");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int a = intent.getIntExtra(MainActivity.NUMBER_A, 0);
        int b = intent.getIntExtra(MainActivity.NUMBER_B, 0);
        String operator = intent.getStringExtra(MainActivity.OPERATOR);
        int result = 0;

        switch (operator) {
            case "+": {
                result = a + b;
                break;
            }
            case "-": {
                result = a - b;
                break;
            }
            case "*": {
                result = a * b;
                break;
            }
            case "/": {
                result = a / b;
                break;
            }
            default:
                break;
        }

        Intent intent1 = new Intent();
        intent1.putExtra(MainActivity.KEY, result);
        intent1.setAction(MainActivity.MyReceiver.RESPONSE_ACTION);
        intent1.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(intent1);
    }
}
