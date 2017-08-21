package com.chetangani.myapp.fragments.cards;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.CardAdapter;
import com.chetangani.myapp.database.Database;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllCards extends Fragment {
    View view;
    private LinearLayout addcards_btn;
    RecyclerView cards_view;
    ArrayList<CardDetails> cards_list;
    CardAdapter cardAdapter;
    CardDetails cardDetails;
    Database database;
    Cursor cards;
    Spinner sp_cardtype, sp_banks;
    ArrayList<String> cardtype_list, banks_list;
    ArrayAdapter<String> cardtype_adapter, banks_adapter;

    public AllCards() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.allcards_layout, container, false);

        database = new Database(getActivity());
        database.open();

        addcards_btn = (LinearLayout) view.findViewById(R.id.addcards_btn);
        cards_view = (RecyclerView) view.findViewById(R.id.allcards_view);
        cards_list = new ArrayList<>();
        cardAdapter = new CardAdapter(getActivity(), cards_list, AllCards.this, database);
        cards_view.setHasFixedSize(true);
        cards_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        cards_view.setAdapter(cardAdapter);
        sp_cardtype = (Spinner) view.findViewById(R.id.sp_card_type);
        sp_banks = (Spinner) view.findViewById(R.id.sp_bank);
        cardtype_list = new ArrayList<>();
        banks_list = new ArrayList<>();

        cardtype_list.add("Both");
        cardtype_list.add("Credit");
        cardtype_list.add("Debit");
        cardtype_adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, cardtype_list);
        sp_cardtype.setAdapter(cardtype_adapter);

        banks_list.add("All");
        getcardsdetails();

        addcards_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_main, new AddCardFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private void getcardsdetails() {
        cards = database.getcardsdetails();
        if (cards.getCount() > 0) {
            while (cards.moveToNext()) {
                String id = cards.getString(cards.getColumnIndex("_id"));
                String card_number = cards.getString(cards.getColumnIndex("card_number"));
                String card_expiry = cards.getString(cards.getColumnIndex("card_expiry"));
                String card_cvv = cards.getString(cards.getColumnIndex("card_cvv"));
                String card_name = cards.getString(cards.getColumnIndex("card_name"));
                String bank_code = cards.getString(cards.getColumnIndex("bank_code"));
                Cursor getimage = database.getbankdetailsbycode(bank_code);
                getimage.moveToNext();
                String bank_image = getimage.getString(getimage.getColumnIndex("bank_image"));
                String bank_name = getimage.getString(getimage.getColumnIndex("bank_name"));
                banks_list.add(bank_name);
                cardDetails = new CardDetails(id, "", card_number, card_expiry, card_cvv, card_name, bank_image, "", "", "");
                cards_list.add(cardDetails);
                cardAdapter.notifyDataSetChanged();
            }
        }
        banks_adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, banks_list);
        sp_banks.setAdapter(banks_adapter);
    }

}
