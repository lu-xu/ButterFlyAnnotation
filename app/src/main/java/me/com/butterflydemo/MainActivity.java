package me.com.butterflydemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import me.com.butterflydemo.annotation.ButterFly;
import me.com.butterflydemo.annotation.ButterFlyEventListener;

public class MainActivity extends AppCompatActivity {
    @ButterFly(id = R.id.text_hello)
    TextView text_hello;
    @ButterFly(id = R.id.fab, click = "click")
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initButterFly();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        text_hello.setText("hello reflect");
    }

    public void click(View v) {
        Snackbar.make(v, "click reflect", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void initButterFly() {
        Field[] fields = getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    if (field.get(this) != null) {
                        continue;
                    }
                    final ButterFly butterFly = field.getAnnotation(ButterFly.class);
                    if (butterFly != null) {
                        int id = butterFly.id();
                        field.set(this, findViewById(id));
                        Object obj = field.get(this);
//                        setListener(field, butterFly.click());
                        if (obj instanceof View) {
                            ((View) obj).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        Method method = MainActivity.this.getClass().getDeclaredMethod(butterFly.click(), View.class);
                                        if (method != null) {
                                            method.invoke(MainActivity.this, v);
                                        }
                                    } catch (NoSuchMethodException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setListener(Field field, String methodName) throws IllegalAccessException {
        if (methodName == null || methodName.trim().length() == 0)
            return;
        Object obj = field.get(this);
        if (obj instanceof View) {
            ((View) obj).setOnClickListener(new ButterFlyEventListener(this)
                    .click(methodName));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
