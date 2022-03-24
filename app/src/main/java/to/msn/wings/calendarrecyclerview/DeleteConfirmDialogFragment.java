package to.msn.wings.calendarrecyclerview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       //  return super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogTitle);
        builder.setMessage(R.string.dialogMsg);
        builder.setPositiveButton(R.string.dialogBtnOk, new DialogButtonClickListener());
        builder.setNeutralButton(R.string.dialogBtnNg, new DialogButtonClickListener());
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
