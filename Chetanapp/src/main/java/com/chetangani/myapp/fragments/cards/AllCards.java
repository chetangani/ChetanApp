package com.chetangani.myapp.fragments.cards;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.CardAdapter;
import com.chetangani.myapp.database.Database;

import java.util.ArrayList;
import java.util.Objects;

public class AllCards extends Fragment {
    View view;

    Toolbar toolbar;
    AppBarLayout.LayoutParams params;

    RecyclerView cards_view;
    ArrayList<GetSet_CardDetails> cards_list;
    CardAdapter cardAdapter;
    GetSet_CardDetails getSetCardDetails;
    Database database;
    Cursor cards;
    Spinner sp_cardtype, sp_banks;
    ArrayList<String> cardtype_list, banks_list;
    ArrayAdapter<String> cardtype_adapter, banks_adapter;

    FloatingActionButton addcards_btn;
    String selected_bank_code="";
    int selected_card_type=0;

    public AllCards() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.allcards_layout, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        toolbar = view.findViewById(R.id.main_toolbar);
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getActivity().getResources().getString(R.string.nav_cards));

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        addcards_btn = view.findViewById(R.id.addcards_btn);
        cards_view = view.findViewById(R.id.allcards_view);
        cards_list = new ArrayList<>();
        cardAdapter = new CardAdapter(getActivity(), cards_list, AllCards.this, database);
        cards_view.setHasFixedSize(true);
        cards_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        cards_view.setAdapter(cardAdapter);
        sp_cardtype = view.findViewById(R.id.sp_card_type);
        sp_banks = view.findViewById(R.id.sp_bank);
        cardtype_list = new ArrayList<>();
        banks_list = new ArrayList<>();

        cardtype_list.add("Both");
        cardtype_list.add("Credit");
        cardtype_list.add("Debit");
        cardtype_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, cardtype_list);
        sp_cardtype.setAdapter(cardtype_adapter);

        banks_list.add("All");
        getcardsdetails();

        addcards_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft;
                if (getFragmentManager() != null) {
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container_main, new AddCardFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        getSpinners_details();

        return view;
    }

    private void getcardsdetails() {
        cards = database.getcardsdetails();
        if (cards.getCount() > 0) {
            getSorteddetails(cards);
            Cursor banklist = database.getbankcodelist();
            while (banklist.moveToNext()) {
                String bank_code = banklist.getString(banklist.getColumnIndex("bank_code"));
                Cursor getbankname = database.getbankdetailsbycode(bank_code);
                getbankname.moveToNext();
                String bank_name = getbankname.getString(getbankname.getColumnIndex("bank_name"));
                banks_list.add(bank_name);
                getbankname.close();
            }
            banklist.close();
        }
        banks_adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, banks_list);
        sp_banks.setAdapter(banks_adapter);
    }

    private void getSpinners_details() {
        sp_cardtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_card_type = position;
                Cursor data = database.sorting_cards(selected_card_type, selected_bank_code);
                getSorteddetails(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sp_banks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selected_bank_code = "";
                } else {
                    Cursor code = database.getbankdetailsbyname(banks_list.get(position));
                    code.moveToNext();
                    selected_bank_code = code.getString(code.getColumnIndex("bank_code"));
                    code.close();
                }
                Cursor data = database.sorting_cards(selected_card_type, selected_bank_code);
                getSorteddetails(data);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getSorteddetails(Cursor data) {
        if (data.getCount() > 0) {
            cards_list.clear();
            while (data.moveToNext()) {
                String id = data.getString(data.getColumnIndex("_id"));
                String card_type = data.getString(data.getColumnIndex("card_type"));
                String card_number = data.getString(data.getColumnIndex("card_number"));
                String card_expiry = data.getString(data.getColumnIndex("card_expiry"));
                String card_cvv = data.getString(data.getColumnIndex("card_cvv"));
                String card_name = data.getString(data.getColumnIndex("card_name"));
                String bank_code = data.getString(data.getColumnIndex("bank_code"));
                getSetCardDetails = new GetSet_CardDetails(id, card_type, card_number, card_expiry, card_cvv, card_name,
                        bank_code, "", "", "");
                cards_list.add(getSetCardDetails);
                cardAdapter.notifyDataSetChanged();
            }
        } else {
            cards_list.clear();
            cardAdapter.notifyDataSetChanged();
        }
        data.close();
    }
}
