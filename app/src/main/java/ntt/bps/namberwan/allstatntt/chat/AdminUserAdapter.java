package ntt.bps.namberwan.allstatntt.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import ntt.bps.namberwan.allstatntt.DatabaseHelper;
import ntt.bps.namberwan.allstatntt.R;
import ntt.bps.namberwan.allstatntt.RecyclerViewClickListener;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.Holder> {

    private List<User> list;
    private Context context;
    private DatabaseHelper db;
    private RecyclerViewClickListener listener;

    public AdminUserAdapter(List<User> list, Context context, RecyclerViewClickListener listener) {
        this.list = list;
        this.context = context;
        db = new DatabaseHelper(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_admin_user_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = list.get(position);
        holder.bind(user, listener);
        holder.nama.setText(user.getName());
        String adminNoString = "Admin-"+ (position+1);
        holder.adminNo.setText(adminNoString);
        holder.lastSeen.setText(user.getLastSeen());

        if (!user.getAvatar().equals("")){
            Picasso.get()
                    .load(user.getAvatar())
                    .error(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_broken_image))
                    .placeholder(new IconicsDrawable(context).color(ContextCompat.getColor(context, R.color.md_grey_300)).icon(GoogleMaterial.Icon.gmd_image))
                    .fit()
                    .into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        TextView nama;
        TextView adminNo;
        TextView lastSeen;
        ImageView photo;

        public Holder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            adminNo = itemView.findViewById(R.id.admin_no);
            lastSeen = itemView.findViewById(R.id.last_seen);
            photo = itemView.findViewById(R.id.foto);
        }

        public void bind(final User user, final RecyclerViewClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(user);
                }
            });
        }
    }
}
