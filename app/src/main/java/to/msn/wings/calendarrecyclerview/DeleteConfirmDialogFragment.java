package to.msn.wings.calendarrecyclerview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class DeleteConfirmDialogFragment extends DialogFragment {

    private TimeScheduleDatabaseHelper _helper;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // TimeScheduleListAdapterから、Bundleでデータを引き渡してるので、取得する
        Bundle args = requireArguments();
        // String型のデータを渡しているので
        String strId = args.getString("strId");
        String scheduleTitle = args.getString("scheduleTitle");


        // 既存のダイアログに TextViewを 付け足すには TextViewだけでも追加できるが、マージンとかつけたければ、ビューグループから作る必要がある
        // ビューグループから作る必要がある

        Activity parentActivity = getActivity();  // TimeScheduleActivity
        // ビュールグループの LinearLayoutを準備
        LinearLayout linearLayout = new LinearLayout(parentActivity);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // TextViewを準備する
        TextView textViewStrId = new TextView(parentActivity);  // 既存のダイアログに 追加するTextView インスタンスを生成する
        TextView textViewScheduleTitle = new TextView(parentActivity);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        textViewStrId.setLayoutParams(lp);
        // 今度はマージンを設定したいのでキャストする
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;
        mlp.setMargins(mlp.leftMargin, 10, mlp.rightMargin, 10);
          //  マージンを設定
        textViewStrId.setLayoutParams(mlp);
        textViewScheduleTitle.setLayoutParams(mlp);



        textViewStrId.setText("id: " + strId );  // ここに動的に _idカラムの値をString型にしたものをセットして、非表示にする
        textViewScheduleTitle.setText("選択されたスケジュールのタイトル:" + scheduleTitle );
        // ビューグループにTextViewをセットする！！！
        linearLayout.addView(textViewStrId);
        linearLayout.addView(textViewScheduleTitle);
        // ビューグループをアクティビティにセットする
        parentActivity.setContentView(linearLayout);


//        TextView textViewScheduleTitle = new TextView(parentActivity);
//        textViewScheduleTitle.setText("選択されたスケジュールのタイトル:" + scheduleTitle );



    //    ViewGroup.LayoutParams lp = textViewStrId.getLayoutParams();  // null
     //   ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;  // null
         // getLayoutParams()すると null　になるので、まだ onCreateDialogコールバックメソッドの中では、ビューが生成し終わってないため

//        ViewGroup.LayoutParams lp = textViewStrId.getLayoutParams(); // プログラムで作成しているので nullになりますので nullチェックをします
//        if (lp != null) {
//            lp.width= ViewGroup.LayoutParams.MATCH_PARENT;
//            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        } else {  // nullになるのでこちらです
//            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)lp;
//            mlp.setMargins(mlp.leftMargin, 10, mlp.rightMargin, 10);
//            //  マージンを設定
//            textViewStrId.setLayoutParams(mlp);
//            textViewScheduleTitle.setLayoutParams(mlp);
//        }

     //   textViewStrId.setVisibility(View.GONE);  // 非表示にして場所を詰める
        textViewStrId.setVisibility(View.VISIBLE); // 表示する


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogTitle);
        builder.setMessage(R.string.dialogMsg);
        builder.setPositiveButton(R.string.dialogBtnOk, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogBtnNg, new DialogButtonClickListener());
        // 追加
        builder.setView(textViewStrId);  // これでTextView１つ追加できた
        builder.setView(textViewScheduleTitle);

        AlertDialog dialog = builder.create();


        return dialog;
    }

    // リスナークラスを クラスメンバとして定義する ダイアログのボタンをクリックした時のリスナー
    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            switch(i) {
                case DialogInterface.BUTTON_POSITIVE:
                    // ここで、削除の処理を実行するので データベースへ接続をします。 すぐに helperをclose()してください。
                    Log.i("Dialog", "削除ボタン押しました");




                    Activity parentActivity = getActivity();
                    _helper = new TimeScheduleDatabaseHelper(parentActivity);  // _helper は　同じonClickの中で解放する
                    //  データベースを取得する try-catch-resources構文 finallyを書かなくても必ず close()処理をしてくれます
                    try (SQLiteDatabase db = _helper.getWritableDatabase()) {  // dbはきちんとクローズ自動でしてくれます

                        // ここにデータベースの処理を書く
                        String sqlDelete = "DELETE FROM timeschedule WHERE _id = ?";

                        SQLiteStatement stmt = db.compileStatement(sqlDelete);
                       // stmt.bindLong(1, );


                    }
                    _helper.close();
                break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // 元の日付の タイムスケジュール一覧画面へ戻ります ダイアログ消えるだけ
                    // 自分自身の所属するアクティビティの終了はしない。フラグメントからフラグメントを生成しただけなので
                    // 何にも処理はしない
                break;
            }
        }
    }
}
