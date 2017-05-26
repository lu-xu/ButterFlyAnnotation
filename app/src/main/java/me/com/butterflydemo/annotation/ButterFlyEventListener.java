package me.com.butterflydemo.annotation;

import android.view.View;

import java.lang.reflect.Method;

/**
 * Created by xulu on 2017/5/26.
 */

public class ButterFlyEventListener implements View.OnClickListener{
    Object object;
    String methodName;
    public ButterFlyEventListener(Object object) {
        this.object = object;
    }
    public ButterFlyEventListener click(String methodName){
        this.methodName = methodName;
        return this;
    }

    public void onClick(View v) {
        if(object == null)
            return;
        Method method = null;
        try{
            method = object.getClass().getDeclaredMethod(methodName,View.class);
            if(method!=null)
                method.invoke(object, v);
            else
                throw new Exception("no such method:"+methodName);
        }catch(Exception e){
            e.printStackTrace();
        }

        return ;
    }
}
