package course.labs.classroomquery.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by asus on 2017/1/23.
 */
public class IViewHolder extends RecyclerView.ViewHolder{
    public IViewHolder(View itemView) {
        super(itemView);
    }
    @Deprecated
    public final int getIAdapterPosition() {
        return getAdapterPosition() ;
    }
}
