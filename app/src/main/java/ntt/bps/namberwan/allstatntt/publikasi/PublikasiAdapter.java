package ntt.bps.namberwan.allstatntt.publikasi;

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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import ntt.bps.namberwan.allstatntt.AppUtil;
import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;
import ntt.bps.namberwan.allstatntt.brs.BrsItem;

public class PublikasiAdapter extends RecyclerView.Adapter<PublikasiAdapter.Holder> {

    private List<PublikasiItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public PublikasiAdapter(List<PublikasiItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        private TextView judul;
        private TextView tgl;
        private TextView nomerKatalog;
        private TextView abstrak;
        private ImageView cover;
        private ImageButton download;
        private ImageButton bookmark;
        private ImageButton expand;
        private CardView cardView;

        private Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            nomerKatalog = itemView.findViewById(R.id.nomer_katalog);
            tgl = itemView.findViewById(R.id.tanggal);
            abstrak = itemView.findViewById(R.id.abstrak);
            cover = itemView.findViewById(R.id.cover_publikasi);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            download = itemView.findViewById(R.id.download_button);
            expand = itemView.findViewById(R.id.expand_button);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final PublikasiItem item, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_publikasi_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PublikasiItem item = list.get(position);
        holder.bind(item, listener);
        holder.judul.setText(Html.fromHtml(item.getJudul()));
        holder.tgl.setText(AppUtil.getDate(item.getTanggal(), false));
        holder.nomerKatalog.setText(String.format("%s/%s", item.getIsbn(), item.getNomerKatalog()));
        holder.abstrak.setText(Html.fromHtml(item.getAbstrak()));

        Picasso.get()
                .load(item.getUrlCover())
                .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .fit()
                .into(holder.cover);

        setupButton(holder.download, holder.bookmark, holder.expand, holder.abstrak, item);

        setupCardView(holder.cardView);

        holder.nomerKatalog.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setupCardView(CardView cardView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(5, 4, 5, 4);
            cardView.setLayoutParams(layoutParams);
        }
    }

    private void setupButton(ImageButton download, ImageButton bookmark, final ImageButton expand, final TextView abstrak, final PublikasiItem item) {
        if (item.isBookmarked()){
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        download.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.blue)).icon(GoogleMaterial.Icon.gmd_file_download));

        if(!item.isExpanded()){
            expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.expand_button)).icon(GoogleMaterial.Icon.gmd_expand_more));
            abstrak.setVisibility(View.GONE);
        } else {
            expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.expand_button)).icon(GoogleMaterial.Icon.gmd_expand_less));
            abstrak.setVisibility(View.VISIBLE);
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (item.isBookmarked()){
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    item.setIsBookmarked(false);
                    db.unbookmarkPublikasi(item.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }else {
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    item.setIsBookmarked(true);
                    db.bookmarkPublikasi(item.getId(), item.getJudul(), item.getTanggal(), item.getIsbn(),
                            item.getAbstrak(), item.getNomerKatalog(), item.getUrlCover(), item.getUrlPdf(),
                            System.currentTimeMillis());
                    Snackbar.make(v, "Publikasi di-bookmark", Snackbar.LENGTH_LONG).show();
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
                                AppUtil.downloadFile((Activity) context, item.getUrlPdf(), item.getJudul());
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

        /*expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublikasiItem item = getItem((int) v.getTag());
                //Do Your Code Here
                if (!item.isExpanded()) {
                    abstrak.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in);
                    animation.setDuration(400);
                    abstrak.startAnimation(animation);
                    expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_600)).icon(GoogleMaterial.Icon.gmd_expand_less));
                    item.setIsExpanded(true);
                } else {
                    abstrak.setVisibility(View.GONE);
                    expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_600)).icon(GoogleMaterial.Icon.gmd_expand_more));
                    item.setIsExpanded(false);
                }
            }
        });*/
    }

}