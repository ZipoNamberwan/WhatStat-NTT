package ntt.bps.namberwan.allstatntt.tabelstatis;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.auth.AuthActivity;

public class TabelAdapter extends RecyclerView.Adapter<TabelAdapter.Holder> {

    private List<TabelItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public TabelAdapter(List<TabelItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TabelAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tabel_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabelAdapter.Holder holder, int position) {
        TabelItem item = list.get(position);
        holder.bind(item, listener);
        holder.judul.setText(Html.fromHtml(item.getJudul()));
        holder.subjek.setText(item.getSubjek());
        holder.tanggal.setText(AppUtil.getDate(item.getTanggal(), false));
        holder.theme.setBackgroundColor(ContextCompat.getColor(context, AppUtil.getColorTheme(item.getKategori())));

        if (item.isSection()){
            holder.section.setText(AppUtil.getDate(item.getTanggal(),true));
            holder.section.setClickable(false);
            holder.section.setVisibility(View.VISIBLE);
        }else {
            holder.section.setVisibility(View.GONE);
        }

        setupButton(holder.download, holder.share, holder.bookmark, item);

        setupCardView(holder.cardView);

        holder.theme.setVisibility(View.GONE);
    }

    private void setupButton(ImageButton download, ImageButton share, ImageButton bookmark, final TabelItem item) {
        if (item.isBookmarked()){
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

        share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (item.isBookmarked()){
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    item.setIsBookmarked(false);
                    db.unbookmarkTabel(item.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }else {
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    item.setIsBookmarked(true);
                    db.bookmarkTabel(item.getId(), item.getSubjek(), item.getTanggal(), item.getJudul(),
                            item.getExcel(), item.getUrlShare(), item.getHtml(), item.getKategori(),
                            System.currentTimeMillis());
                    Snackbar.make(v, "Tabel di-bookmark", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent i = new Intent(context, AuthActivity.class);
                                String token = AppUtil.getToken((Activity) context);
                                if (token==null){
                                    context.startActivity(i);
                                }else {
                                    String s = item.getExcel() + token;
                                    String namaFile = item.getJudul().replaceAll("\\W+", "");
                                    AppUtil.downloadFile((Activity) context, s, item.getJudul(), namaFile + ".xls");
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Download")
                        .setMessage("Download "+item.getJudul()+"?")
                        .setPositiveButton("Ya", onClickListener)
                        .setNegativeButton("Tidak", onClickListener)
                        .show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                AppUtil.share((Activity) context, item.getJudul(), item.getUrlShare());
            }
        });
    }

    private void setupCardView(CardView cardView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(5,4,5,4);
            cardView.setLayoutParams(layoutParams);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView judul;
        TextView subjek;
        TextView tanggal;
        TextView section;
        View theme;
        ImageButton share;
        ImageButton bookmark;
        CardView cardView;
        ImageButton download;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            subjek = itemView.findViewById(R.id.subjek_deskripsi);
            tanggal = itemView.findViewById(R.id.tanggal);
            section = itemView.findViewById(R.id.tanggal_tabel_section);
            theme = itemView.findViewById(R.id.color_theme);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            share = itemView.findViewById(R.id.share_button);
            download = itemView.findViewById(R.id.download_button);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final TabelItem tabelItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(tabelItem);
                }
            });
        }
    }
}
