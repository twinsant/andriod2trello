package com.twinsant.android2trello;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddCardDialogFragment extends DialogFragment {
	public void setListener(Fragment fragment) {
		mListener = (AddCardDialogListener)fragment;
	}

	public interface AddCardDialogListener {
		public void onDialogPositiveClick(String text);
	}
	
	AddCardDialogListener mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_add_card, null);
		final EditText editText = (EditText)view.findViewById(R.id.name);
		builder.setView(view)
			.setPositiveButton(R.string.add_card, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String text = editText.getText().toString();
					if (!text.trim().equals("")) {
						mListener.onDialogPositiveClick(text);
					}
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//
				}			
			});
		return builder.create();
	}
}
