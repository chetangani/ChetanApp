package com.chetangani.myapp.fragments.fueltracker;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.adapters.FuelAdapter;
import com.chetangani.myapp.adapters.MileageStatusAdapter;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.GetSetValues;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class AllFuelDetails extends Fragment {
    private static final int NODATA_DLG = 1;
    private static final int READING_STATUS_DLG = 2;
    private static final int FUEL_STATUS_DLG = 3;

    View view;

    Toolbar toolbar;
    AppBarLayout.LayoutParams params;

    RecyclerView trackers_view;
    ArrayList<GetSet_Fueldetails> trackers_list;
    FuelAdapter fuelAdapter;
    GetSet_Fueldetails getSetFueldetails;
    GetSetValues getSetValues;
    Database database;
    Cursor trackers;
    String fuelid="", startreading="", endreading="", fuelprice="", fuelfilled="", fueldate="", fuelamount="", fuellastdate="", fuelbrand="";
    TextInputLayout til_reading;
    TextInputEditText et_reading;

    public AllFuelDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.allfueltrackers_layout, container, false);

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

        ((MainActivity) Objects.requireNonNull(getActivity())).setActionBarTitle(getActivity().getResources().getString(R.string.nav_fuel));

        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();
        getSetValues = new GetSetValues();

        FloatingActionButton addfuels_btn = view.findViewById(R.id.addfuels_btn);
        trackers_view = view.findViewById(R.id.allfuelTrackers_view);
        trackers_list = new ArrayList<>();
        fuelAdapter = new FuelAdapter(getActivity(), trackers_list, database);
        trackers_view.setHasFixedSize(true);
        trackers_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        trackers_view.setAdapter(fuelAdapter);

        addfuels_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
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
                fuelbrand = trackers.getString(trackers.getColumnIndex("fuel_brand"));
                getSetFueldetails = new GetSet_Fueldetails(fuelid, startreading, endreading, fuelprice, fuelfilled, fueldate, fuelamount,
                        fuellastdate, fuelbrand);
                trackers_list.add(getSetFueldetails);
                fuelAdapter.notifyDataSetChanged();
            }
        } else showdialog(NODATA_DLG);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fuel_tracker_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_check_mileage:
                showdialog(READING_STATUS_DLG);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showdialog(int id) {
        final AlertDialog alertDialog;
        switch (id) {
            case NODATA_DLG:
                AlertDialog.Builder nodata = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                nodata.setTitle("Fuel Trackers");
                nodata.setCancelable(false);
                nodata.setMessage("No Data is available to show up.. \nPlease add the fuel details to get data here.. " +
                        "\nDo you want to add fuel details..?? ");
                nodata.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction ft;
                        if (getFragmentManager() != null) {
                            ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.container_main, new VehicleFuelTracker());
                            ft.commit();
                        }
                    }
                });
                nodata.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                alertDialog = nodata.create();
                alertDialog.show();
                break;

            case READING_STATUS_DLG:
                AlertDialog.Builder reading_dialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                reading_dialog.setTitle(getResources().getString(R.string.check_mileage));
                @SuppressLint("InflateParams")
                LinearLayout reading_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.check_mileage_layout, null);
                reading_dialog.setView(reading_layout);
                til_reading = reading_layout.findViewById(R.id.til_enter_reading);
                et_reading = reading_layout.findViewById(R.id.et_enter_reading);
                TextView tv_last_reading = reading_layout.findViewById(R.id.dlg_fuel_last_reading);
                Cursor reading = database.fueldetails();
                if (reading.getCount() > 0) {
                    reading.moveToNext();
                    getSetValues.setLast_reading(reading.getInt(reading.getColumnIndex("start_reading")));
                    tv_last_reading.setText(String.valueOf(getSetValues.getLast_reading()));
                }
                reading_dialog.setPositiveButton("Mileage", null);
                reading_dialog.setNeutralButton("Cancel", null);
                alertDialog = reading_dialog.create();
                validate_reading(alertDialog);
                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button neutral = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                check_reading(alertDialog);
                            }
                        });
                        neutral.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
                alertDialog.show();
                break;

            case FUEL_STATUS_DLG:
                AlertDialog.Builder mileage_status = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                @SuppressLint("InflateParams")
                LinearLayout status_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.mileage_status_layout, null);
                mileage_status.setView(status_layout);
                TextView tv_distance = status_layout.findViewById(R.id.dlg_fuel_distance);
                TextView tv_mileage = status_layout.findViewById(R.id.dlg_fuel_mileage);
                tv_distance.setText(getSetValues.getDistance());
                tv_mileage.setText(getSetValues.getMileage());
                RecyclerView status_view = status_layout.findViewById(R.id.mileage_status_view);
                ArrayList<GetSet_Fueldetails> status_list = new ArrayList<>();
                MileageStatusAdapter statusAdapter = new MileageStatusAdapter(status_list);
                status_view.setHasFixedSize(true);
                status_view.setLayoutManager(new LinearLayoutManager(getActivity()));
                status_view.setAdapter(statusAdapter);
                double fuel = 0;
                int status_reading = 0, mileage = 30, except_reading;
                Cursor data = database.fueldetails();
                if (data.getCount() > 0) {
                    data.moveToNext();
                    status_reading = Integer.parseInt(data.getString(data.getColumnIndexOrThrow("start_reading")));
                    fuel = data.getDouble(data.getColumnIndex("fuel_filled"));
                }
                data.close();
                for (int i = 0; i < 9; i++) {
                    GetSet_Fueldetails getSetFueldetails = new GetSet_Fueldetails();
                    if (i == 0) {
                        except_reading = (int) (status_reading + (fuel * mileage));
                        getSetFueldetails.setTest_mileage(""+mileage);
                        getSetFueldetails.setTest_distance(""+except_reading);
                    } else {
                        if ((i % 2) == 0) {
                            mileage = mileage + 3;
                            except_reading = (int) (status_reading + (fuel * mileage));
                            getSetFueldetails.setTest_mileage(""+mileage);
                            getSetFueldetails.setTest_distance(""+except_reading);
                        }
                        if ((i % 2) == 1) {
                            mileage = mileage + 2;
                            except_reading = (int) (status_reading + (fuel * mileage));
                            getSetFueldetails.setTest_mileage(""+mileage);
                            getSetFueldetails.setTest_distance(""+except_reading);
                        }
                    }
                    status_list.add(getSetFueldetails);
                    statusAdapter.notifyDataSetChanged();
                }
                mileage_status.setPositiveButton("OK", null);
                alertDialog = mileage_status.create();
                alertDialog.show();
                break;
        }
    }

    private void mileage_result(String reading, GetSetValues getSetValues) {
        Cursor mileage = database.fueldetails();
        if (mileage.getCount() > 0) {
            mileage.moveToNext();
            String previous_reading = mileage.getString(mileage.getColumnIndex("start_reading"));
            int distance = Integer.parseInt(reading) - Integer.parseInt(previous_reading);
            getSetValues.setDistance(String.valueOf(distance)+" Kms");
            double fuel = mileage.getDouble(mileage.getColumnIndex("fuel_filled"));
            getSetValues.setMileage(new DecimalFormat("##0.##").format(distance / fuel)+" Kms/L");
        } else showdialog(NODATA_DLG);
        mileage.close();
    }

    private void validate_reading(final AlertDialog alertDialog) {
        et_reading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 1) {
                    if (til_reading.isErrorEnabled())
                        til_reading.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_reading.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    check_reading(alertDialog);
                }
                return false;
            }
        });
    }

    private void check_reading(AlertDialog alertDialog) {
        if (!TextUtils.isEmpty(et_reading.getText())) {
            if (Integer.parseInt(et_reading.getText().toString()) > getSetValues.getLast_reading()) {
                mileage_result(et_reading.getText().toString(), getSetValues);
                alertDialog.dismiss();
                showdialog(FUEL_STATUS_DLG);
            } else {
                til_reading.setErrorEnabled(true);
                til_reading.setError(getResources().getString(R.string.enter_valid_reading));
            }
        } else {
            til_reading.setErrorEnabled(true);
            til_reading.setError(getResources().getString(R.string.enter_reading_error));
        }
    }

}
