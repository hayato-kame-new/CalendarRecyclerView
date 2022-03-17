package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationBarView;

public class ScheduleFormFragment extends Fragment {

    Spinner spinnerStartHour, spinnerStartMinutes,spinnerEndHour, spinnerEndMinutes;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //   return inflater.inflate(R.layout.fragment_schedule_form, container, false);
        Activity parentActivity = getActivity();

        View view = inflater.inflate(R.layout.fragment_schedule_form, container, false);



        spinnerStartHour = view.findViewById(R.id.spinnerStartHour);
        spinnerEndHour = view.findViewById(R.id.spinnerEndHour);
        spinnerStartMinutes = view.findViewById(R.id.spinnerStartMinutes);
        spinnerEndMinutes = view.findViewById(R.id.spinnerEndMinutes);
        // xmlファイルで android:entries="@array/spinnerStartHour"を書くやり方もあるが こっちでもできます。
        // ４つも書くと多くなるので 今回は android:entries="@array/spinnerStartHour"を書く
//        ArrayAdapter<CharSequence> adapterSH = ArrayAdapter.createFromResource(parentActivity,
//                R.array.spinnerStartHour, android.R.layout.simple_spinner_item);
//        adapterSH.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerStartHour.setAdapter(adapterSH);


        // ボタンにリスナーをつけて　ボタンを押した時に、データベースに登録します あり得ないが入力されたときにはToastを表示させたい。




        // 最後ビューをreturnする
        return view;
    }
}