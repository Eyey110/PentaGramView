package com.eyey.pentagramview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengliao on 2017/5/31 0031.
 * Email:dantemustcry@126.com
 */

public class PentagramView extends View {
    public PentagramView(Context context) {
        this(context, null);
    }

    public PentagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PentagramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //留出0.15的来绘制文字
    private static final float MAX_SCALE = 0.8f;
    private Paint mPaint;
    //mR:半径,mCx:圆心的x坐标，mCy:圆心的y坐标
    private float mR, mCx, mCy;
    private static final int mN = 5;
    private static final float DEGREES_UNIT = 360 / mN;
    //diagonalPath绘制圆心到各顶点的线，framePath绘制外框线,scorePath绘制评分
    private Path diagonalPath, framePath, scorePath;
    private static final int DEFAULT_POINT_RADIUS = 8;
    private PointF top;
    private PointF left;
    private PointF right;
    private PointF bLeft;
    private PointF bRight;
    private PentagramAdapter pentagramAdapter;
    private int textColor;
    private int pentaColor;

    public void setPentagramAdapter(PentagramAdapter pentagramAdapter) {
        this.pentagramAdapter = pentagramAdapter;
        this.pentagramAdapter.setView(this);
//        invalidate();
    }

    public PentagramAdapter getPentagramAdapter() {
        return pentagramAdapter;
    }

    //顺序依次为top、right、bRight、bLeft、left
    private List<PointF> points = new ArrayList<>(mN);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float mW = getMeasuredWidth();
        float mH = getMeasuredHeight();
        mCx = mW / 2;
        mCy = mH / 2;
        mR = Math.min(mCx, mCy) * MAX_SCALE;
        diagonalPath = new Path();
        framePath = new Path();
        scorePath = new Path();
        calculatePoint();
    }

    public void setPentaColor(int color) {
        this.pentaColor = color;
//        invalidate();
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PentagramView);
        textColor = typedArray.getColor(R.styleable.PentagramView_textColor, ContextCompat.getColor(this.getContext(), android.R.color.black));
        pentaColor = typedArray.getColor(R.styleable.PentagramView_pentaColor, ContextCompat.getColor(this.getContext(), android.R.color.black));
        typedArray.recycle();
        top = new PointF();
        left = new PointF();
        right = new PointF();
        bLeft = new PointF();
        bRight = new PointF();
        points.add(top);
        points.add(right);
        points.add(bRight);
        points.add(bLeft);
        points.add(left);
        mPaint = new Paint();
    }

    protected void drawPentagram(Canvas canvas, float scale) {
        diagonalPath.reset();
        framePath.reset();
        mPaint.reset();
        mPaint.setColor(pentaColor);
        mPaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < points.size(); i++) {
            diagonalPath.moveTo(mCx, mCy);
            PointF pointF = new PointF();
            pointF.x = mCx * (1 - scale) + points.get(i).x * scale;
            pointF.y = mCy * (1 - scale) + points.get(i).y * scale;
            diagonalPath.lineTo(pointF.x, pointF.y);
            if (i == 0) {
                framePath.moveTo(pointF.x, pointF.y);
            } else {
                framePath.lineTo(pointF.x, pointF.y);
            }
        }
        framePath.close();
        if (scale >= MAX_SCALE) {
            canvas.drawPath(diagonalPath, mPaint);
        }
        canvas.drawPath(framePath, mPaint);
    }

    //计算出最大正五边形顶点的坐标
    protected void calculatePoint() {
        for (int i = 0; i < mN; i++) {
            points.get(i).x = mCx + (float) (mR * Math.sin((DEGREES_UNIT * i) * Math.PI / 180));
            points.get(i).y = mCy - (float) (mR * Math.cos((DEGREES_UNIT * i) * Math.PI / 180));
        }
    }


    protected void drawAllScores(Canvas canvas) {
        if (pentagramAdapter == null) {
            return;
        }
        for (int i = 0; i < pentagramAdapter.getCount(); i++) {
            if (pentagramAdapter.getViewColor(i) == -1) {
                drawScores(canvas, pentagramAdapter.getVertexScores(i));
            } else {
                drawScores(canvas, pentagramAdapter.getVertexScores(i), pentagramAdapter.getViewColor(i));
            }
        }
    }

    protected void drawScores(Canvas canvas, List<Float> scores) {
        drawScores(canvas, scores, pentaColor);
    }

    protected void drawScores(Canvas canvas, List<Float> scores, int color) {
        if (pentagramAdapter == null || scores == null) {
            return;
        }
        mPaint.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(color);
        scorePath.rewind();
//        List<Float> scores = pentagramAdapter.getVertexScore();
        for (int i = 0; i < Math.min(mN, scores.size()); i++) {
            PointF pointF = new PointF();
            float realScale = scores.get(i) * MAX_SCALE;
            if (scores.get(i) == null) {
                pointF.x = mCx;
                pointF.x = mCy;
            } else {
                pointF.x = mCx * (1 - realScale) + points.get(i).x * realScale;
                pointF.y = mCy * (1 - realScale) + points.get(i).y * realScale;
            }
            if (i == 0) {
                scorePath.moveTo(pointF.x, pointF.y);
            } else {
                scorePath.lineTo(pointF.x, pointF.y);
            }
            canvas.drawCircle(pointF.x, pointF.y, DEFAULT_POINT_RADIUS, mPaint);
        }
        scorePath.close();
        mPaint.setAlpha(155);
        canvas.drawPath(scorePath, mPaint);
    }

    //在五个顶点绘制文字
    protected void drawText(Canvas canvas) {
        if (pentagramAdapter == null || pentagramAdapter.getVertexText() == null) {
            return;
        }
        List<String> texts = pentagramAdapter.getVertexText();
        float r = mR;
        float bx, by;
        mPaint.reset();
        mPaint.setColor(textColor);
        mPaint.setTextSize(mR / 15);
        for (int i = 0; i < Math.min(mN, texts.size()); i++) {
            bx = mCx + (float) (r * Math.sin((DEGREES_UNIT * i) * Math.PI / 180));
            by = mCy - (float) (r * Math.cos((DEGREES_UNIT * i) * Math.PI / 180));
            String text = texts.get(i);
            if (TextUtils.isEmpty(text)) {
                continue;
            }
            float tw = mPaint.measureText(text);
            float tx = bx - tw / 2;
            float ty;
            if (i == 0) {
                ty = by - mPaint.getFontMetricsInt().top;
            } else {
                ty = by + mPaint.getFontMetricsInt().bottom;
            }
            canvas.drawText(text, tx, ty, mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (float i = 1f; i <= 5; i++) {
            drawPentagram(canvas, i / 5 * MAX_SCALE);
        }
        drawText(canvas);
        drawAllScores(canvas);
    }
}
