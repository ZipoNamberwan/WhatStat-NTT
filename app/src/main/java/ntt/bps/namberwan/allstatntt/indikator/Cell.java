package ntt.bps.namberwan.allstatntt.indikator;

import com.evrencoskun.tableview.filter.IFilterableModel;
import com.evrencoskun.tableview.sort.ISortableModel;

public class Cell implements ISortableModel, IFilterableModel {

    private String id;
    private String data;
    private String mFilterKeyword;

    public Cell (String id){
        this.id =id;
    }

    public Cell (String id, String data){
        this.id = id;
        this.data = data;
        this.mFilterKeyword = String.valueOf(data);
    }

    @Override
    public String getFilterableKeyword() {
        return mFilterKeyword;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getContent() {
        return getData();
    }

    public String getData() {
        return data;
    }
}
