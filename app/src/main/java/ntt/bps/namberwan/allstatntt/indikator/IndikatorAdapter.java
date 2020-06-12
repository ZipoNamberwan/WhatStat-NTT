package ntt.bps.namberwan.allstatntt.indikator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntt.bps.namberwan.allstatntt.AppUtils;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;

public class IndikatorAdapter extends RecyclerView.Adapter<IndikatorAdapter.Holder> {

    private List<IndikatorItem> list;
    private Context context;
    private RecyclerViewClickListener listener;

    public IndikatorAdapter(List<IndikatorItem> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_indikator_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final IndikatorItem item = list.get(position);
        holder.bind(item, listener);
        holder.judul.setText(item.getJudul());

        holder.nilai.setText(AppUtils.formatNumberSeparator(Float.parseFloat(item.getNilai())));

        if (item.getSatuan().equals("Tidak Ada Satuan") | item.getSatuan().equals("")){
            holder.satuan.setText("");
            //holder.satuan.setVisibility(View.GONE);
        } else {
            holder.satuan.setText(item.getSatuan());
        }
        String s = "*Sumber: "+item.getSumber();
        holder.sumber.setText(s);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.button);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Intent i = new Intent(context, IndikatorViewActivity.class);
                        i.putExtra(IndikatorViewActivity.VAR_ID, AppUtils.getVarId(item.getId()));
                        context.startActivity(i);
                        return true;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        private TextView judul;
        private TextView nilai;
        private TextView satuan;
        private TextView sumber;
        private ImageButton button;

        public Holder(View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judul);
            nilai = itemView.findViewById(R.id.nilai);
            satuan = itemView.findViewById(R.id.satuan);
            sumber = itemView.findViewById(R.id.sumber);
            button = itemView.findViewById(R.id.more_button);
        }

        public void bind(final IndikatorItem bukuItem, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(bukuItem);
                }
            });
        }
    }
}
