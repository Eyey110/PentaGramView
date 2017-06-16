package com.eyey.pentagramview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengliao on 2017/6/1 0001.
 * Email:dantemustcry@126.com
 */

public abstract class PentagramAdapter {
    private PentagramView view;

    public void setView(PentagramView view) {
        this.view = view;
    }

    /**
     * 返回顶点的文字列表
     * 请按照top、right、BottomRight、BottomLeft、Left的顺序返回
     *
     * @return
     */
    public abstract List<String> getVertexText();

    /**
     * 返回各顶点的评分，评分在0-1之间
     * 请按照top、right、BottomRight、BottomLeft、Left的顺序返回
     *
     * @return
     */
    public abstract List<Float> getVertexScores(int position);

    public abstract int getCount();

    /**
     * 使用默认返回-1
     *
     * @param position
     * @return
     */
    public abstract int getViewColor(int position);

    public void notifyDataSetChange() {
        if (view != null) {
            view.invalidate();
        }
    }
}
