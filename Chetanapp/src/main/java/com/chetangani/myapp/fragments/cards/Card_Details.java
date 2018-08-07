package com.chetangani.myapp.fragments.cards;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chetangani.myapp.R;
import com.chetangani.myapp.values.FunctionCalls;

import static com.chetangani.myapp.values.Constants.SEARCH_ID;

public class Card_Details extends Fragment {

    View view;

    TextView tv_card_type, tv_card_number, tv_card_expiry, tv_card_cvv, tv_card_holder;
    ImageView card_bank_logo, card_type_logo;

    GetSet_CardDetails getSetCardDetails;
    FunctionCalls functionCalls;

    public Card_Details() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_card_details, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            getSetCardDetails = (GetSet_CardDetails) bundle.getSerializable(SEARCH_ID);
        }

        initialize();

        setvalues();

        copyCardnumber(getActivity());

        return view;
    }

    private void initialize() {
        functionCalls = new FunctionCalls();

        tv_card_type = view.findViewById(R.id.details_card_type);
        tv_card_number = view.findViewById(R.id.details_card_number);
        tv_card_number.setTextIsSelectable(true);
        tv_card_expiry = view.findViewById(R.id.details_card_expiry);
        tv_card_cvv = view.findViewById(R.id.details_card_cvv);
        tv_card_holder = view.findViewById(R.id.details_card_holder);

        card_bank_logo = view.findViewById(R.id.details_bank_logo);
        card_type_logo = view.findViewById(R.id.details_card_logo);
    }

    private void setvalues() {
        tv_card_type.setText(functionCalls.getCard_type(getSetCardDetails.getCard_type()));
        tv_card_number.setText(functionCalls.showcardnumber(getSetCardDetails.getCard_number()));
        tv_card_expiry.setText(getSetCardDetails.getCard_expiry());
        tv_card_cvv.setText(getSetCardDetails.getCard_cvv());
        tv_card_holder.setText(getSetCardDetails.getCard_name());

        card_bank_logo.setImageResource(functionCalls.getBank_logo(getSetCardDetails.getBank_code()));
        card_type_logo.setImageResource(functionCalls.getCard_logo(getSetCardDetails.getCard_number()));
    }

    private void copyCardnumber(final Context context) {
        tv_card_number.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                assert cm != null;
                cm.setText(functionCalls.getcardnumber(tv_card_number.getText().toString()));
                functionCalls.showtoast(getActivity(), "Copied to clipboard");
                return true;
            }
        });
    }
}
