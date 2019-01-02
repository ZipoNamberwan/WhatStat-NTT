package ntt.bps.namberwan.allstatntt.indikator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.evrencoskun.tableview.sort.SortState;

import ntt.bps.namberwan.allstatntt.R;

public class TabelViewAdapter extends AbstractTableAdapter<ColumnHeader, RowHeader, Cell> {

    private final LayoutInflater mInflater;

    public TabelViewAdapter(Context context) {
        super(context);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.table_view_cell_layout, parent, false);
        return new CellViewHolder(layout);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        Cell cell = (Cell) cellItemModel;
        CellViewHolder viewHolder = (CellViewHolder) holder;
        viewHolder.setCell(cell);
    }

    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.table_view_column_header_layout, parent, false);

        // Create a ColumnHeader ViewHolder
        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        ColumnHeader columnHeader = (ColumnHeader) columnHeaderItemModel;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;
        columnHeaderViewHolder.setColumnHeader(columnHeader);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(R.layout.table_view_row_header_layout, parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        RowHeader rowHeader = (RowHeader) rowHeaderItemModel;

        // Get the holder to update row header item text
        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(String.valueOf(rowHeader.getData()));
    }

    @Override
    public View onCreateCornerView() {
        // Get Corner xml layout
        View corner = mInflater.inflate(R.layout.table_view_corner_layout, null);
        corner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortState sortState = getTableView()
                        .getRowHeaderSortingStatus();
                if (sortState != SortState.ASCENDING) {
                    Log.d("TableViewAdapter", "Order Ascending");
                    getTableView().sortRowHeader(SortState.ASCENDING);
                } else {
                    Log.d("TableViewAdapter", "Order Descending");
                    getTableView().sortRowHeader(SortState.DESCENDING);
                }
            }
        });
        return corner;
    }

    public class CellViewHolder extends AbstractViewHolder {

        public final TextView cell_textview;
        public final LinearLayout cell_container;
        private Cell cell;

        public CellViewHolder(View itemView) {
            super(itemView);
            cell_textview = (TextView) itemView.findViewById(R.id.cell_data);
            cell_container = (LinearLayout) itemView.findViewById(R.id.cell_container);
        }

        public void setCell(Cell cell) {
            this.cell = cell;
            cell_textview.setText(String.valueOf(cell.getData()));

            // If your TableView should have auto resize for cells & columns.
            // Then you should consider the below lines. Otherwise, you can ignore them.

            // It is necessary to remeasure itself.
            cell_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            cell_textview.requestLayout();
        }
    }

    public class ColumnHeaderViewHolder extends AbstractSorterViewHolder {

        public final LinearLayout column_header_container;
        public final TextView column_header_textview;
        public final ImageButton column_header_sortButton;
        public final ITableView tableView;

        public final Drawable arrow_up, arrow_down;

        public ColumnHeaderViewHolder(View itemView, ITableView tableView) {
            super(itemView);
            this.tableView = tableView;
            column_header_textview = (TextView) itemView.findViewById(R.id.column_header_textView);
            column_header_container = (LinearLayout) itemView.findViewById(R.id
                    .column_header_container);
            column_header_sortButton = (ImageButton) itemView.findViewById(R.id
                    .column_header_sortButton);

            // initialize drawables
            arrow_up = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_up);//up
            arrow_down = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_down);//down

            // Set click listener to the sort button
            column_header_sortButton.setOnClickListener(mSortButtonClickListener);
        }


        /**
         * This method is calling from onBindColumnHeaderHolder on TableViewAdapter
         */
        public void setColumnHeader(ColumnHeader columnHeader) {
            column_header_textview.setText(String.valueOf(columnHeader.getData()));

            // If your TableView should have auto resize for cells & columns.
            // Then you should consider the below lines. Otherwise, you can remove them.

            // It is necessary to remeasure itself.
            column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
            column_header_textview.requestLayout();
        }

        private View.OnClickListener mSortButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSortState() == SortState.ASCENDING) {
                    tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
                } else if (getSortState() == SortState.DESCENDING) {
                    tableView.sortColumn(getAdapterPosition(), SortState.ASCENDING);
                } else {
                    // Default one
                    tableView.sortColumn(getAdapterPosition(), SortState.DESCENDING);
                }

            }
        };

        @Override
        public void onSortingStatusChanged(SortState sortState) {
            Log.e(ColumnHeaderViewHolder.class.getSimpleName(), " + onSortingStatusChanged : x:  " + getAdapterPosition() + " old state "
                    + getSortState() + " current state : " + sortState + " visiblity: " +
                    column_header_sortButton.getVisibility());

            super.onSortingStatusChanged(sortState);

            // It is necessary to remeasure itself.
            column_header_container.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;

            controlSortState(sortState);

            Log.e(ColumnHeaderViewHolder.class.getSimpleName(), " - onSortingStatusChanged : x:  " + getAdapterPosition() + " old state "
                    + getSortState() + " current state : " + sortState + " visiblity: " +
                    column_header_sortButton.getVisibility());

            column_header_textview.requestLayout();
            column_header_sortButton.requestLayout();
            column_header_container.requestLayout();
            itemView.requestLayout();
        }

        private void controlSortState(SortState sortState) {
            if (sortState == SortState.ASCENDING) {
                column_header_sortButton.setVisibility(View.VISIBLE);
                column_header_sortButton.setImageDrawable(arrow_down);

            } else if (sortState == SortState.DESCENDING) {
                column_header_sortButton.setVisibility(View.VISIBLE);
                column_header_sortButton.setImageDrawable(arrow_up);
            }
        }
    }

    public class RowHeaderViewHolder extends AbstractViewHolder {
        public final TextView row_header_textview;

        public RowHeaderViewHolder(View itemView) {
            super(itemView);
            row_header_textview = (TextView) itemView.findViewById(R.id.row_header_textview);
        }
    }
}

