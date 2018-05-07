package com.guard;

import android.os.AsyncTask;

/**
 * @author: jackli
 * @version: V1.0
 * @project: My360
 * @package: com.guard
 * @description: description
 * @date: 2018/5/6
 * @time: 22:31
 */
public class MyObject {

    public void doInbackgroud() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }
        }.execute();
    }
}
