package ntt.bps.namberwan.allstatntt.berita;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.Holder> {

    private List<BeritaItem> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public BeritaAdapter(List<BeritaItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_berita_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        BeritaItem item = list.get(position);
        holder.bind(item, listener);
        holder.judul.setText(Html.fromHtml(item.getJudul()));
        holder.tgl.setText(AppUtil.getDate(item.getTanggal(), false));
        holder.jenis.setText(item.getJenis());
        holder.rincian.setText(Html.fromHtml(item.getRincian()));

        Picasso.get()
                .load(item.getUrlFoto())
                .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                .fit()
                .into(holder.photo);

        setupButton(holder.share, holder.bookmark, holder.expand, holder.rincian, item);
        setupCardView(holder.cardView);
    }

    private void setupCardView(CardView cardView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.setMargins(5,4,5,4);
            cardView.setLayoutParams(layoutParams);
        }
    }

    private void setupButton(ImageButton share, ImageButton bookmark, final ImageButton expand, final TextView rincian, final BeritaItem item){
        if (item.isBookmarked()){
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
        }else {
            bookmark.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
        }

        share.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.green)).icon(GoogleMaterial.Icon.gmd_share));

        if(!item.isExpanded()){
            expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.expand_button)).icon(GoogleMaterial.Icon.gmd_expand_more));
            rincian.setVisibility(View.GONE);
        } else {
            expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.expand_button)).icon(GoogleMaterial.Icon.gmd_expand_less));
            rincian.setVisibility(View.VISIBLE);
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (item.isBookmarked()){
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_plus_outline));
                    item.setIsBookmarked(false);
                    db.unbookmarkBerita(item.getId());
                    Snackbar.make(v, "Bookmark dihapus", Snackbar.LENGTH_LONG)
                            .setAction("Undo", null).show();
                }else {
                    ((ImageButton)v).setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.bookmark_button)).icon(CommunityMaterial.Icon.cmd_bookmark_check));
                    item.setIsBookmarked(true);
                    db.bookmarkBerita(item.getId(), item.getJenis(), item.getTanggal(), item.getUrlFoto(),
                            item.getJudul(),item.getRincian(), item.getUrlShare(), System.currentTimeMillis());
                    Snackbar.make(v, "Berita di-bookmark", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                String urlShare =  AppUtil.getUrlShare(context.getString(R.string.web_share_news), item.getTanggal(), item.getId(), item.getJudul());
                AppUtil.share((Activity) context, item.getJudul(), urlShare);
            }
        });

        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do Your Code Here
                if (!item.isExpanded()) {
                    rincian.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_fade_in);
                    animation.setDuration(400);
                    rincian.startAnimation(animation);
                    expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_600)).icon(GoogleMaterial.Icon.gmd_expand_less));
                    item.setIsExpanded(true);
                } else {
                    rincian.setVisibility(View.GONE);
                    expand.setImageDrawable(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.material_grey_600)).icon(GoogleMaterial.Icon.gmd_expand_more));
                    item.setIsExpanded(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView judul;
        TextView tgl;
        TextView jenis;
        TextView rincian;
        ImageButton share;
        ImageButton bookmark;
        ImageButton expand;
        ImageView photo;
        CardView cardView;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul_berita);
            jenis = itemView.findViewById(R.id.jenis_berita);
            tgl = itemView.findViewById(R.id.tanggal_berita);
            rincian = itemView.findViewById(R.id.rincian_berita);
            photo = itemView.findViewById(R.id.foto_berita);
            bookmark = itemView.findViewById(R.id.bookmark_button);
            share = itemView.findViewById(R.id.share_button);
            expand = itemView.findViewById(R.id.desc_button);
            cardView = itemView.findViewById(R.id.card_view);
        }

        public void bind(final BeritaItem item, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
