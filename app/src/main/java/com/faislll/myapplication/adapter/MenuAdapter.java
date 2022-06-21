package com.faislll.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.faislll.myapplication.MenuDetailMasakan;
import com.faislll.myapplication.MenuDetailSarapan;
import com.faislll.myapplication.R;
import com.faislll.myapplication.model.Reseps;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {
    private Context context;
    private ArrayList<Reseps> reseps = new ArrayList<>();
    private View viewBottomSheet;
    public static final String ACTION_STATE = "state";
    public static final String RESEPS_DATA = "reseps";
    public static final String ACTION_NEW = "NEW";
    public static final String ACTION_UPDATE = "UPDATE";

    public MenuAdapter(Context context, ArrayList<Reseps> reseps) {
        this.context = context;
        this.reseps = reseps;
    }

    @NonNull
    @Override
    public MenuAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_menu, parent, false);
        viewBottomSheet = inflater.inflate(R.layout.detail_menu, parent, false);
        return new MenuAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapterViewHolder holder, int position) {
        Reseps resepsData = reseps.get(position);

        CircularProgressDrawable loading = new CircularProgressDrawable(context);
        loading.setStrokeWidth(5f);
        loading.setCenterRadius(30f);
        loading.start();

        holder.textViewNama.setText(resepsData.getNama_menu());
        Glide.with(context).load(resepsData.getUrl_image()).placeholder(loading).into(holder.imageViewMenu);
        holder.textViewDeskripsi.setText(resepsData.getDeskripsi());

        holder.itemView.setOnClickListener(v -> {
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
//            bottomSheetDialog.setContentView(viewBottomSheet);
//            bottomSheetDialog.create();
//            bottomSheetDialog.show();
            Intent goToDetail = new Intent(context.getApplicationContext(), MenuDetailMasakan.class);
            goToDetail.putExtra(RESEPS_DATA, resepsData);
            goToDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(goToDetail);
        });
    }

    @Override
    public int getItemCount() {
        return reseps.size();
    }

    public static class MenuAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNama, textViewDeskripsi;
        private ImageView imageViewMenu;
        public MenuAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDeskripsi = itemView.findViewById(R.id.t_view_deskripsi);
            textViewNama = itemView.findViewById(R.id.t_view_nama);
            imageViewMenu = itemView.findViewById(R.id.image_view_menu);
        }
    }

    public void addData(ArrayList<Reseps> reseps) {
        this.reseps = reseps;
    }
}
