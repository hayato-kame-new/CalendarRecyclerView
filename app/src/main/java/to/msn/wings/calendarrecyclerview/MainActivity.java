package to.msn.wings.calendarrecyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        RecyclerView rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        GridLayoutManager manager = new GridLayoutManager(this, 7);
        rv.setLayoutManager(manager);

//        RecyclerView.Adapter adapter = new CalendarAdapter(data);
//        rv.setAdapter(adapter);
    }
}