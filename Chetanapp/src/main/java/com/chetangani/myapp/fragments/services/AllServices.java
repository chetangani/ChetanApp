package com.chetangani.myapp.fragments.services;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.ServicesAdapter;
import com.chetangani.myapp.database.Database;

import java.util.ArrayList;
import java.util.Objects;

public class AllServices extends Fragment {
    public static final int NODATA_DLG = 1;

    View view;
    RecyclerView services_view;
    ArrayList<Servicedetails> services_list;
    ServicesAdapter servicesAdapter;
    Servicedetails servicedetails;
    Database database;
    Cursor services;
    TextView tv_amount;
    String service_date="", service_description="", service_reading="", service_amount="", service_last_reading="";
    int amount=0;

    public AllServices() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.bikeservices_layout, container, false);

        FloatingActionButton addservice_btn = view.findViewById(R.id.addservice_btn);
        tv_amount = view.findViewById(R.id.total_amt);
        services_view = view.findViewById(R.id.bikeservices_view);
        services_list = new ArrayList<>();
        servicesAdapter = new ServicesAdapter(getActivity(), services_list);
        services_view.setHasFixedSize(true);
        services_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        services_view.setAdapter(servicesAdapter);

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        getdetails();

        tv_amount.setText(getResources().getString(R.string.rs)+" "+amount+" /-");

        addservice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                ft.replace(R.id.container_main, new BikeServiceExp());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private void getdetails() {
        amount = 0;
        services = database.getservicedetails();
        if (services.getCount() > 0) {
            while (services.moveToNext()) {
                service_date = services.getString(services.getColumnIndex("service_date"));
                service_description = services.getString(services.getColumnIndex("description"));
                service_amount = services.getString(services.getColumnIndex("service_amount"));
                amount = amount + Integer.parseInt(service_amount);
                try {
                    service_reading = services.getString(services.getColumnIndex("cur_reading"));
                } catch (NullPointerException e) {
                    service_reading = "";
                }
                try {
                    service_last_reading = services.getString(services.getColumnIndex("last_reading"));
                } catch (NullPointerException e) {
                    service_last_reading = "";
                }
                servicedetails = new Servicedetails(service_date, service_description, service_reading, service_amount, service_last_reading);
                services_list.add(servicedetails);
                servicesAdapter.notifyDataSetChanged();
            }
        } else showdialog(NODATA_DLG);
    }

    protected void showdialog(int id) {
        Dialog dialog;
        switch (id) {
            case NODATA_DLG:
                AlertDialog.Builder nodata = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                nodata.setTitle("Service Details");
                nodata.setCancelable(false);
                nodata.setMessage("No Data is available to show up.. \nPlease add the service details to get data here.. " +
                        "\nDo you want to add service details..?? ");
                nodata.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
                        ft.replace(R.id.container_main, new BikeServiceExp());
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
