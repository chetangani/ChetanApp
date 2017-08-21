package com.chetangani.myapp.fragments.fueltracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.FuelAdapter;
import com.chetangani.myapp.database.Database;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFuelDetails extends Fragment {
    public static final int NODATA_DLG = 1;

    View view;
    private LinearLayout addfuels_btn;
    RecyclerView trackers_view;
    ArrayList<Fueldetails> trackers_list;
    FuelAdapter fuelAdapter;
    Fueldetails fueldetails;
    Database database;
    Cursor trackers;
    String fuelid="", startreading="", endreading="", fuelprice="", fuelfilled="", fueldate="", fuelamount="", fuellastdate="";

    public AllFuelDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.allfueltrackers_layout, container, false);

        database = new Database(getActivity());
        database.open();

        addfuels_btn = (LinearLayout) view.findViewById(R.id.addfuels_btn);
        trackers_view = (RecyclerView) view.findViewById(R.id.allfuelTrackers_view);
        trackers_list = new ArrayList<>();
        fuelAdapter = new FuelAdapter(getActivity(), trackers_list, database);
        trackers_view.setHasFixedSize(true);
        trackers_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        trackers_view.setAdapter(fuelAdapter);

        addfuels_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container_main, new VehicleFuelTracker());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        trackers = database.fueldetails();
        trackers_list.clear();
        if (trackers.getCount() > 0) {
            while (trackers.moveToNext()) {
                fuelid = trackers.getString(trackers.getColumnIndex("_id"));
                startreading = trackers.getString(trackers.getColumnIndex("start_reading"));
                fuelprice = trackers.getString(trackers.getColumnIndex("fuel_price"));
                fuelfilled = trackers.getString(trackers.getColumnIndex("fuel_filled"));
                fueldate = trackers.getString(trackers.getColumnIndex("fuel_date"));
                fuelamount = trackers.getString(trackers.getColumnIndex("fuel_amount"));
                try {
                    endreading = trackers.getString(trackers.getColumnIndex("end_reading"));
                } catch (NullPointerException e) {
                    endreading = "";
                }
                try {
                    fuellastdate = trackers.getString(trackers.getColumnIndex("fuel_last_date"));
                } catch (NullPointerException ee) {
                    fuellastdate = "";
                }
                fueldetails = new Fueldetails(fuelid, startreading, endreading, fuelprice, fuelfilled, fueldate, fuelamount, fuellastdate);
                trackers_list.add(fueldetails);
                fuelAdapter.notifyDataSetChanged();
            }
        } /*else showdialog(NODATA_DLG);*/

        return view;
    }

    protected void showdialog(int id) {
        Dialog dialog;
        switch (id) {
            case NODATA_DLG:
                AlertDialog.Builder nodata = new AlertDialog.Builder(getActivity());
                nodata.setTitle("Fuel Trackers");
                nodata.setCancelable(false);
                nodata.setMessage("No Data is available to show up.. \nPlease add the fuel details to get data here.. " +
                        "\nDo you want to add fuel details..?? ");
                nodata.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container_main, new VehicleFuelTracker());
                        ft.commit();
                    }
                });
                nodata.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                dialog = nodata.create();
                dialog.show();
                break;
        }
    }

}
