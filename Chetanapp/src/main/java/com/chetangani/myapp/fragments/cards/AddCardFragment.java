package com.chetangani.myapp.fragments.cards;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;
import java.util.Objects;

public class AddCardFragment extends Fragment {
    View view;
    EditText et_cardnumber, et_expiry, et_cvv, et_cardname, et_loginid, et_loginpass, et_transpass;
    TextInputLayout til_cardnumber, til_login_pass, til_trans_pass, til_login_id;
    ImageView card_logo;
    Button btn_addcard;
    boolean updatedetails = false;
    String first="", second="", card_type="", card_number="", card_expiry="", card_cvv="", card_name="", bank_code="", login_id="",
            login_pass="", trans_pass="", card_id="";
    Spinner sp_banknames;
    ArrayList<String> banklist;
    ArrayAdapter<String> bankadapter;
    RadioGroup rg_logindetails, rg_cardtype;
    RadioButton rb_loginyes, rb_loginno, rb_credit, rb_debit;
    FunctionCalls functionCalls;
    Database database;
    boolean american_card = false, diners_card = false, valid_card = false;

    public AddCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_card, container, false);

        til_cardnumber = view.findViewById(R.id.til_cardnumber);
        til_login_id = view.findViewById(R.id.til_loginid);
        til_login_pass = view.findViewById(R.id.til_loginpassword);
        til_trans_pass = view.findViewById(R.id.til_transpassword);
        et_cardnumber = view.findViewById(R.id.et_cardnumber);
        et_cardnumber.requestFocus();
        et_expiry = view.findViewById(R.id.et_expiry);
        et_cvv = view.findViewById(R.id.et_cvv);
        et_loginid = view.findViewById(R.id.et_loginid);
        et_loginpass = view.findViewById(R.id.et_loginpassword);
        et_transpass = view.findViewById(R.id.et_transpassword);
        et_cardname = view.findViewById(R.id.et_cardname);
        sp_banknames = view.findViewById(R.id.spn_banknames);
        banklist = new ArrayList<>();
        rg_logindetails = view.findViewById(R.id.rg_loginpass);
        rb_loginyes = view.findViewById(R.id.rb_yes);
        rb_loginno = view.findViewById(R.id.rb_no);
        rg_cardtype = view.findViewById(R.id.rg_card_type);
        rb_credit = view.findViewById(R.id.rb_credit);
        rb_debit = view.findViewById(R.id.rb_debit);
        card_logo = view.findViewById(R.id.card_logo);
        btn_addcard = view.findViewById(R.id.addcard_btn);

        functionCalls = new FunctionCalls();
        database = ((MainActivity) Objects.requireNonNull(getActivity())).getDatabase();

        logindetails(false);

        banklist.add("SELECT");
        Cursor list = database.getbanks();
        while (list.moveToNext()) {
            banklist.add(list.getString(list.getColumnIndex("bank_name")));
        }
        list.close();

        bankadapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, banklist);
        sp_banknames.setAdapter(bankadapter);

        getBundledetails();

        et_cardnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    if (s.length() == 5) {
                        first = s.toString().substring(0, 4);
                        second = s.toString().substring(4);
                        et_cardnumber.setText(first+" "+second);
                        et_cardnumber.setSelection(s.length()+1);
                    }
                    if (s.length() == 10) {
                        first = s.toString().substring(0, 9);
                        second = s.toString().substring(9);
                        et_cardnumber.setText(first+" "+second);
                        et_cardnumber.setSelection(s.length()+1);
                    }
                    if (s.length() == 15) {
                        first = s.toString().substring(0, 14);
                        second = s.toString().substring(14);
                        et_cardnumber.setText(first+" "+second);
                        et_cardnumber.setSelection(s.length()+1);
                    }
                    if (s.length() == 3) {
                        String digit1 = s.toString().substring(0,1);
                        String digit2 = s.toString().substring(0,2);
                        if (digit1.equals("4")) {
                            card_logo.setImageResource(R.drawable.visacard_logo);
                            card_logo.setVisibility(View.VISIBLE);
                        } else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0) {
                            card_logo.setImageResource(R.drawable.mastercard_logo);
                            card_logo.setVisibility(View.VISIBLE);
                        } else if (digit2.equals("34") || digit2.equals("37")) {
                            card_logo.setImageResource(R.drawable.american_express_logo);
                            card_logo.setVisibility(View.VISIBLE);
                            setEdittext_length(et_cardnumber, 18);
                            setEdittext_length(et_cvv, 4);
                            american_card = true;
                            diners_card = false;
                        } else if (digit2.equals("36")) {
                            card_logo.setImageResource(R.drawable.diners_club_logo);
                            card_logo.setVisibility(View.VISIBLE);
                            setEdittext_length(et_cardnumber, 17);
                            setEdittext_length(et_cvv, 3);
                            diners_card = true;
                            american_card = false;
                        } else {
                            setEdittext_length(et_cardnumber, 19);
                            setEdittext_length(et_cvv, 3);
                            american_card = false;
                            diners_card = false;
                        }
                    }
                }
                if (before == 1) {
                    if (s.length() == 5) {
                        first = s.toString().substring(0, 4);
                        et_cardnumber.setText(first);
                        et_cardnumber.setSelection(first.length());
                    }
                    if (s.length() == 10) {
                        first = s.toString().substring(0, 9);
                        et_cardnumber.setText(first);
                        et_cardnumber.setSelection(first.length());
                    }
                    if (s.length() == 15) {
                        first = s.toString().substring(0, 14);
                        et_cardnumber.setText(first);
                        et_cardnumber.setSelection(first.length());
                    }
                    if (s.length() == 2) {
                        card_logo.setVisibility(View.INVISIBLE);
                        setEdittext_length(et_cardnumber, 19);
                        setEdittext_length(et_cvv, 3);
                        american_card = false;
                        diners_card = false;
                    }
                }
                if (diners_card) {
                    validdate_card(s, 17);
                } else if (american_card) {
                    validdate_card(s, 18);
                } else validdate_card(s, 19);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_expiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    if (s.length() == 2) {
                        first = s.toString().substring(0, 2);
                        et_expiry.setText(first+"/");
                        et_expiry.setSelection(s.length()+1);
                    }
                }
                if (before == 1) {
                    if (s.length() == 3) {
                        first = s.toString().substring(0, 2);
                        et_expiry.setText(first);
                        et_expiry.setSelection(first.length());
                    }
                }
                if (s.length() == 3) {
                    first = s.toString().substring(0, 2);
                    second = s.toString().substring(2, 3);
                    if (!second.equals("/")) {
                        et_expiry.setText(first+"/"+second);
                        et_expiry.setSelection(et_expiry.getText().length());
                    }
                }
                if (s.length() == 1) {
                    int num = Integer.parseInt(s.toString().substring(0, 1));
                    //noinspection StatementWithEmptyBody
                    if (num == 0 || num == 1) {
                    } else et_expiry.setText("");
                }
                if (s.length() == 2) {
                    first = s.toString().substring(0, 1);
                    if (first.equals("1")) {
                        int num = Integer.parseInt(s.toString().substring(1, 2));
                        //noinspection StatementWithEmptyBody
                        if (num == 0 || num == 1 || num == 2) {
                        } else {
                            et_expiry.setText(first);
                            et_expiry.setSelection(first.length());
                        }
                    }
                }
                if (s.length() == 5) {
                    et_cvv.requestFocus();
                    et_cvv.setSelection(et_cvv.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (american_card) {
                    if (s.length() == 4) {
                        et_cardname.requestFocus();
                        et_cardname.setSelection(et_cardname.getText().length());
                    }
                } else {
                    if (s.length() == 3) {
                        et_cardname.requestFocus();
                        et_cardname.setSelection(et_cardname.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        rg_logindetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_loginyes.isChecked()) {
                    logindetails(true);
                }
                if (rb_loginno.isChecked()) {
                    logindetails(false);
                }
            }
        });

        rg_cardtype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (rb_credit.isChecked())
                    card_type = "1";
                if (rb_debit.isChecked())
                    card_type = "0";
            }
        });

        et_cardnumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_expiry.requestFocus();
                    et_expiry.setSelection(et_expiry.getText().length());
                }
                return false;
            }
        });

        et_expiry.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_cvv.requestFocus();
                    et_cvv.setSelection(et_cvv.getText().length());
                }
                return false;
            }
        });

        et_cvv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_cardname.requestFocus();
                    et_cardname.setSelection(et_cardname.getText().length());
                }
                return false;
            }
        });

        sp_banknames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bank_name = parent.getItemAtPosition(position).toString();
                if (!bank_name.equals("SELECT")) {
                    Cursor code = database.getbankdetailsbyname(bank_name);
                    code.moveToNext();
                    bank_code = code.getString(code.getColumnIndex("bank_code"));
                    code.close();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).hidekeyboard();
                addcarddetails(v);
            }
        });

        return view;
    }

    private void validdate_card(CharSequence charSequence, int length) {
        if (charSequence.length() == length) {
            if (checkcard(functionCalls.getcardnumber(charSequence.toString()))) {
                et_cardnumber.setError("Invalid Card Number");
                valid_card = false;
            } else {
                valid_card = true;
                if (!updatedetails) {
                    if (checkcardavailable(functionCalls.getcardnumber(charSequence.toString()))) {
                        et_cardnumber.setError("Card Number already exists");
                    } else {
                        et_expiry.requestFocus();
                        et_expiry.setSelection(et_expiry.getText().length());
                    }
                } else {
                    et_expiry.requestFocus();
                    et_expiry.setSelection(et_expiry.getText().length());
                }
            }
        }
    }

    private void setEdittext_length(EditText editText, int length) {
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(length) });
    }

    private boolean validate_cvv(String cvv) {
        if (american_card) {
            return cvv.length() == 4;
        } else return cvv.length() == 3;
    }

    private void logindetails(boolean view) {
        if (view) {
            til_login_id.setVisibility(View.VISIBLE);
            til_login_pass.setVisibility(View.VISIBLE);
            til_trans_pass.setVisibility(View.VISIBLE);
        } else {
            til_login_id.setVisibility(View.GONE);
            til_login_pass.setVisibility(View.GONE);
            til_trans_pass.setVisibility(View.GONE);
        }
    }

    private boolean checkcard(String card) {
        StringBuilder reversecard = new StringBuilder();
        reversecard.append(card);
        reversecard = reversecard.reverse();
        int result=0;
        for (int i = 0; i < reversecard.length(); i++) {
            int value = Character.getNumericValue(reversecard.charAt(i));
            if ((i%2)==0) {
                result = result + value;
            } else {
                int valuedouble = value * 2;
                if (valuedouble > 9) {
                    valuedouble = valuedouble - 9;
                }
                result = result + valuedouble;
            }
        }
        result = (result % 10);
        return result != 0;
    }

    private void addcarddetails(View view) {
        if (!TextUtils.isEmpty(card_type)) {
            card_number = et_cardnumber.getText().toString();
            if (!TextUtils.isEmpty(card_number)) {
                if (valid_card) {
                    card_expiry = et_expiry.getText().toString();
                    if (!TextUtils.isEmpty(card_expiry)) {
                        card_cvv = et_cvv.getText().toString();
                        if (!TextUtils.isEmpty(card_cvv)) {
                            if (validate_cvv(card_cvv)) {
                                card_name = et_cardname.getText().toString();
                                if (!TextUtils.isEmpty(card_name)) {
                                    if (!TextUtils.isEmpty(bank_code)) {
                                        if (updatedetails) {
                                            Cursor update = database.updatecarddetails(card_type, functionCalls.getcardnumber(card_number), card_expiry, card_cvv, card_name,
                                                    bank_code, login_id, login_pass, trans_pass, card_id);
                                            update.moveToNext();
                                        } else {
                                            if (checkcardavailable(functionCalls.getcardnumber(card_number))) {
                                                et_cardnumber.setError("Card Number already available");
                                                showsnackbar(view, "Card Number already available");
                                                return;
                                            } else database.insertcarddetails(card_type, functionCalls.getcardnumber(card_number), card_expiry, card_cvv,
                                                    card_name, bank_code, login_id, login_pass, trans_pass);
                                        }
                                        FragmentManager fragmentManager = getFragmentManager();
                                        assert fragmentManager != null;
                                        fragmentManager.popBackStack();
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.container_main, new AllCards());
                                        ft.commit();
                                    } else showsnackbar(view, "Select Bank Name other than SELECT");
                                } else {
                                    et_cardname.setError("Enter Card Name");
                                    showsnackbar(view, "Enter Card Name");
                                }
                            } else {
                                et_cvv.setError("Enter Valid CVV");
                                showsnackbar(view, "Enter Valid CVV");
                            }
                        } else {
                            et_cvv.setError("Enter Card CVV");
                            showsnackbar(view, "Enter Card CVV");
                        }
                    } else {
                        et_expiry.setError("Enter Card Expiry Details");
                        showsnackbar(view, "Enter Card Expiry Details");
                    }
                } else {
                    et_cardnumber.setError("Enter Valid Card Number");
                    showsnackbar(view, "Enter Valid Card Number");
                }
            } else {
                et_cardnumber.setError("Enter Card Number");
                showsnackbar(view, "Enter Card Number");
            }
        } else showsnackbar(view, "Select Card type and proceed");
    }

    private void getBundledetails() {
        Bundle bundle;
        bundle = getArguments();
        try {
            assert bundle != null;
            if (bundle.getString("id") != null) {
                card_id = bundle.getString("id");
                updatedetails = true;
                btn_addcard.setText(getResources().getString(R.string.update_card));
                Cursor getdetails = database.getparticularcarddetails(card_id);
                getdetails.moveToNext();
                card_type = getdetails.getString(getdetails.getColumnIndex("card_type"));
                if (card_type.equals("1"))
                    rb_credit.setChecked(true);
                else rb_debit.setChecked(true);
                String cardnumber = getdetails.getString(getdetails.getColumnIndex("card_number"));
                et_cardnumber.setText(functionCalls.showcardnumber(cardnumber));
                et_cardnumber.setSelection(et_cardnumber.getText().length());
                String digit1 = cardnumber.substring(0,1);
                String digit2 = cardnumber.substring(0,2);
                if (digit1.equals("4")) {
                    card_logo.setImageResource(R.drawable.visacard_logo);
                    card_logo.setVisibility(View.VISIBLE);
                } else if (digit2.compareTo("51")>=0 && digit2.compareTo("55")<=0) {
                    card_logo.setImageResource(R.drawable.mastercard_logo);
                    card_logo.setVisibility(View.VISIBLE);
                } else if (digit2.equals("34") || digit2.equals("37")) {
                    card_logo.setImageResource(R.drawable.american_express_logo);
                    card_logo.setVisibility(View.VISIBLE);
                    setEdittext_length(et_cardnumber, 18);
                    setEdittext_length(et_cvv, 4);
                    american_card = true;
                    diners_card = false;
                } else if (digit2.equals("36")) {
                    card_logo.setImageResource(R.drawable.diners_club_logo);
                    card_logo.setVisibility(View.VISIBLE);
                    setEdittext_length(et_cardnumber, 17);
                    setEdittext_length(et_cvv, 3);
                    diners_card = true;
                    american_card = false;
                } else {
                    setEdittext_length(et_cardnumber, 19);
                    setEdittext_length(et_cvv, 3);
                    american_card = false;
                    diners_card = false;
                }
                if (diners_card) {
                    validdate_card(et_cardnumber.getText(), 17);
                } else if (american_card) {
                    validdate_card(et_cardnumber.getText(), 18);
                } else validdate_card(et_cardnumber.getText(), 19);
                et_expiry.setText(getdetails.getString(getdetails.getColumnIndex("card_expiry")));
                et_cvv.setText(getdetails.getString(getdetails.getColumnIndex("card_cvv")));
                et_cardname.setText(getdetails.getString(getdetails.getColumnIndex("card_name")));
                Cursor getid = database.getbankdetailsbycode(getdetails.getString(getdetails.getColumnIndex("bank_code")));
                getid.moveToNext();
                sp_banknames.setSelection(Integer.parseInt(getid.getString(getid.getColumnIndex("_id"))));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean checkcardavailable(String cardnumber) {
        Cursor check = database.checkcarddetails(cardnumber);
        return check.getCount() > 0;
    }

    private void showsnackbar(View view, String message) {
        ((MainActivity) Objects.requireNonNull(getActivity())).showsnackbar(view, message);
    }
}