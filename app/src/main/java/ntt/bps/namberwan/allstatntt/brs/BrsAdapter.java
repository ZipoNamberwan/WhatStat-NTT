package ntt.bps.namberwan.allstatntt.brs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.indikator.IndikatorItem;

public class BrsAdapter extends RecyclerView.Adapter<BrsAdapter.Holder>{

    private List<BrsItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public BrsAdapter(List<BrsItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        private TextView judul;
        private TextView tgl;
        private TextView subjek;
        private ImageButton download;
        private ImageButton bookmark;
        private ImageButton share;
        private CardView cardView;
        private TextView section;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            subjek = itemView.findViewById(R.id.subjek);
            tgl = itemView.findViewById(R.id.tanggal);
            section = itemView.findViewById(R.id.tanggal_brs_section);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            download = itemView.findViewById(R.id.download_button);
            share = itemView.findViewById(R.id.share_button);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final BrsItem brsItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(brsItem);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brs_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final BrsItem brsItem = list.get(position);
        holder.bind(brsItem, listener);
        holder.judul.setText(brsItem.getJudul());
        holder.subjek.setText(brsItem.getSubjek());
        holder.tgl.setText(AppUtil.getDate(brsItem.getTanggalRilis(), false));

        if (brsItem.isSection()){
            holder.section.setText(AppUtil.getDate(brsItem.getTanggalRilis(),true));
            holder.section.setClickable(false);
            holder.section.setVisibility(View.VISIBLE);
        }else {
            holder.section.setVisibility(View.GONE);
        }

        if (brsItem.isBookmarked()){
            holder.bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            holder.bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        holder.download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

        holder.share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
            layoutParams.setMargins(5, 4, 5, 4);
            holder.cardView.setLayoutParams(layoutParams);
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (brsItem.isBookmarked()) {
                    ((ImageButton) v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    brsItem.setIsBookmarked(false);
                    db.unbookmarkBrs(brsItem.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                } else {
                    ((ImageButton) v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    brsItem.setIsBookmarked(true);
                    db.bookmarkBrs(brsItem.getId(), brsItem.getJudul(), brsItem.getAbstrak(), brsItem.getTanggalRilis(),
                            brsItem.getUrlPdf(), brsItem.getSubjek(), brsItem.getUrlShare(), brsItem.getKategori(),
                            System.currentTimeMillis());
                    Snackbar.make(v, "BRS di-bookmark", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                AppUtil.downloadFile((Activity) context, brsItem.getUrlPdf(), brsItem.getJudul());
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setTitle("Download")
                        .setMessage("Download " + brsItem.getJudul() + "?")
                        .setPositiveButton("Ya", onClickListener)
                        .setNegativeButton("Tidak", onClickListener)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

