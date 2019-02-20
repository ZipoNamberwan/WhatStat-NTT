package ntt.bps.namberwan.allstatntt.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.Holder> {

    private List<Kontak> list;
    private Context context;
    private RecyclerViewClickListener listener;

    public KontakAdapter(List<Kontak> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public KontakAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kontak_adapter, parent, false);
        return new KontakAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KontakAdapter.Holder holder, int position) {
        final Kontak kontak = getList().get(position);
        holder.bind(kontak, listener);
        holder.jenisKontak.setText(kontak.getJenis());
        holder.deskripsiKontak.setText(kontak.getDeskripsi());
        holder.judulKontak.setText(kontak.getJudul());
        holder.icon.setImageResource(kontak.getIcon());

    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public List<Kontak> getList() {
        return list;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView jenisKontak;
        TextView deskripsiKontak;
        TextView judulKontak;
        ImageView icon;

        public Holder(View itemView) {
            super(itemView);
            jenisKontak = itemView.findViewById(R.id.jenis_kontak);
            deskripsiKontak = itemView.findViewById(R.id.deskripsi_kontak);
            judulKontak = itemView.findViewById(R.id.judul_kontak);
            icon = itemView.findViewById(R.id.icon);
        }

        public void bind(final Kontak kontak, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(kontak);
                }
            });
        }
    }
}
