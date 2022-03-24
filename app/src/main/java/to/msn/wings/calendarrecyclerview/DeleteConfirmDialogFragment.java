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
import android.view.Window;
import android.view.WindowManager;
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

        // TextViewを 一つ付け足すには
        Activity parentActivity = getActivity();
        TextView strId = new TextView(parentActivity);
        strId.setText("これ付け足した");  // 後でここに動的に _idカラムの値をString型にしたものをセットして、非表示にする
        // アダプターで、削除ボタンを押した時に、Bundleで 値を引き渡すようにする それから show()をする




        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogTitle);
        builder.setMessage(R.string.dialogMsg);
        builder.setPositiveButton(R.string.dialogBtnOk, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogBtnNg, new DialogButtonClickListener());
        // 追加
        builder.setView(strId);  // １つ追加できた

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
