package com.chetangani.myapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chetangani.myapp.MainActivity;
import com.chetangani.myapp.R;
import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.cards.AddCardFragment;
import com.chetangani.myapp.fragments.cards.AllCards;
import com.chetangani.myapp.fragments.cards.GetSet_CardDetails;
import com.chetangani.myapp.values.FunctionCalls;

import java.util.ArrayList;
import java.util.Objects;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private Context context;
    private ArrayList<GetSet_CardDetails> arrayList;
    private FunctionCalls functionCalls = new FunctionCalls();
    private AllCards cardfragment;
    private Database database;

    public CardAdapter(Context context, ArrayList<GetSet_CardDetails> arrayList, AllCards addCardFragment, Database database) {
        this.context = context;
        this.arrayList = arrayList;
        this.cardfragment = addCardFragment;
        this.database = database;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allcardsview, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        GetSet_CardDetails getSetCardDetails = arrayList.get(position);
        String card_number = getSetCardDetails.getCard_number();
        holder.tv_cardnumber.setText(functionCalls.hidecardnumber(card_number));
        holder.tv_cardexpiry.setText(functionCalls.hideexpiry(getSetCardDetails.getCard_expiry()));
        holder.tv_cardname.setText(getSetCardDetails.getCard_name());
        holder.im_banklogo.setImageResource(functionCalls.getBank_logo(getSetCardDetails.getBank_code()));
        holder.im_cardlogo.setImageResource(functionCalls.getCard_logo(card_number));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener,
            MenuItem.OnMenuItemClickListener {
        TextView tv_cardnumber, tv_cardexpiry, tv_cardname;
        ImageView im_banklogo, im_cardlogo;

        CardViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            tv_cardnumber = itemView.findViewById(R.id.cv_cardnumber);
            tv_cardexpiry = itemView.findViewById(R.id.cv_expiry);
            tv_cardname = itemView.findViewById(R.id.cv_cardname);
            im_banklogo = itemView.findViewById(R.id.cv_banklogo);
            im_cardlogo = itemView.findViewById(R.id.cv_cardlogo);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            GetSet_CardDetails getSetCardDetails = arrayList.get(pos);
            ((MainActivity) context).switchCardContent(MainActivity.Steps.FORM4, getSetCardDetails);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int pos = getAdapterPosition();
            GetSet_CardDetails getSetCardDetails = arrayList.get(pos);
            switch (item.getItemId()) {
                case 1:
                    AddCardFragment fragment = new AddCardFragment();
                    Bundle bundle = new Bundle();
                    FragmentTransaction fragmentTransaction = Objects.requireNonNull(cardfragment.getActivity())
                            .getSupportFragmentManager().beginTransaction();
                    bundle.putString("id", getSetCardDetails.getCard_id());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.container_main, fragment).addToBackStack(null).commit();
                    break;

                case 2:
                    Cursor delete = database.deletecarddetails(getSetCardDetails.getCard_id());
                    delete.moveToNext();
                    arrayList.remove(pos);
                    notifyItemRemoved(pos);
                    break;
            }
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(menu.NONE,1,1,"Edit");
            MenuItem delete = menu.add(menu.NONE,2,2,"Delete");
            edit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }
    }
}
