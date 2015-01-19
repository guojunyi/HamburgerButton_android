package com.hamburgerbutton;

import java.util.ArrayList;
import java.util.List;

import com.hamburgerbutton.HamburgerButton.Layer;
import com.hamburgerbutton.HamburgerButton.Layer.Item;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class HamburgerButton extends View {

	Context mContext;

	PathFactory mPathFactory;
	Paint mPaint;
	float progressStart;
	float progressEnd;

	private float MARGIN;
	private int SIZE;

	static final int STORKE_WIDTH = 6;
	static final int STORKE_COLOR = 0xffffffff;

	boolean isShowMenu = false;

	int topY, bottomY;

	public HamburgerButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		progressStart = 0.0f;
		progressEnd = 0.26f;
		mPathFactory = new PathFactory();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(STORKE_COLOR);
		mPaint.setStrokeWidth(STORKE_WIDTH);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setStrokeJoin(Join.ROUND);

	}

	public void initPath() {
		mPathFactory.reset();
		mPathFactory.addPoint(MARGIN, SIZE / 2);

		for (int i = 0; i <= 100; i += 4) {
			float p = (float) i / 100.0f;
			mPathFactory.addPoint(MARGIN + (SIZE - MARGIN * 2) * p, SIZE / 2);
		}

		for (float i = 0.5f; i > -0.12; i -= 0.04) {

			float a1 = (float) (Math.cos(Math.PI * 0.2) * SIZE / 2 + SIZE / 2);
			float b1 = (float) (SIZE / 2 - Math.sin(Math.PI * 0.2) * SIZE / 2);

			float a2 = SIZE - MARGIN;
			float b2 = SIZE / 2;
			float radius = (float) (Math.sqrt(Math.abs((a1 - a2) * (a1 - a2)
					+ (b1 - b2) * (b1 - b2))) / 2);

			float offsetX = (a1 + a2) / 2 - 5;

			Log.e("my", "*********" + offsetX);
			float x = (float) (Math.cos(Math.PI * i) * radius + offsetX);
			float y = (float) (Math.sin(Math.PI * i) * radius + SIZE / 2 - radius);
			mPathFactory.addPoint(x, y);
		}

		for (float i = 0.2f; i < 0.6f; i += 0.04) {
			float x = (float) (Math.cos(Math.PI * i) * SIZE / 2 + SIZE / 2);
			float y = (float) (SIZE / 2 - Math.sin(Math.PI * i) * SIZE / 2);
			mPathFactory.addPoint(x, y);
		}

		for (float i = 0.6f; i < 2.6f; i += 0.04) {
			float x = (float) (Math.cos(Math.PI * i) * SIZE / 2 + SIZE / 2);
			float y = (float) (SIZE / 2 - Math.sin(Math.PI * i) * SIZE / 2);
			mPathFactory.addPoint(x, y);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		SIZE = this.getMeasuredWidth() < this.getMeasuredHeight() ? this
				.getMeasuredWidth() : this.getMeasuredHeight();
		SIZE -= STORKE_WIDTH;
		topY = SIZE / 4;
		bottomY = SIZE / 4 * 3;
		MARGIN = SIZE / 4;
		initPath();
	}

	@Override
	public void onDraw(Canvas canvas) {

		Path path = mPathFactory.getPath(progressStart, progressEnd);
		Bitmap tempBitmap = Bitmap.createBitmap(SIZE + STORKE_WIDTH, SIZE
				+ STORKE_WIDTH, Config.ARGB_8888);
		Canvas tempCanvas = new Canvas();
		tempCanvas.setBitmap(tempBitmap);

		Matrix matrix = new Matrix();
		matrix.setTranslate(STORKE_WIDTH / 2, STORKE_WIDTH / 2);
		path.transform(matrix);

		tempCanvas.drawPath(path, mPaint);
		path.reset();
		path.moveTo(MARGIN, topY);
		path.lineTo(SIZE - MARGIN, SIZE / 4);
		path.transform(matrix);
		tempCanvas.drawPath(path, mPaint);

		path.reset();
		path.moveTo(MARGIN, bottomY);
		path.lineTo(SIZE - MARGIN, SIZE / 4 * 3);
		path.transform(matrix);
		tempCanvas.drawPath(path, mPaint);

		canvas.drawBitmap(tempBitmap, (getWidth() - tempBitmap.getWidth()) / 2,
				(getHeight() - tempBitmap.getHeight()) / 2, mPaint);
		tempBitmap.recycle();

	}

	static class PathFactory {
		class Item {
			public float x;
			public float y;

			public Item(float x, float y) {
				this.x = x;
				this.y = y;
			}

		}

		private List<Item> points = new ArrayList<Item>();

		public Path getPath(float startProgress, float endProgress) {
			Path path = new Path();
			int startIndex = (int) (points.size() * startProgress);
			int endIndex = (int) (points.size() * endProgress);

			Item startPoint = points.get(startIndex);

			path.moveTo(startPoint.x, startPoint.y);

			while (startIndex < endIndex) {
				Item item = points.get(startIndex);
				path.quadTo(item.x, item.y, item.x, item.y);
				startIndex++;
			}

			return path;
		}

		public void addPoint(float x, float y) {
			Item item = new Item(x, y);
			points.add(item);
		}

		public void reset() {
			points.clear();
		}

	}

	public void toggle() {

		if (!isShowMenu) {
			isShowMenu = true;

			ValueAnimator animator1 = ValueAnimator.ofFloat(0.0f, 0.51f);
			animator1.setDuration(100);
			animator1.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					progressStart = frameValue;
					if (progressStart > 0.51) {
						progressStart = 0.51f;
					}
					invalidate();
				}

			});
			animator1.start();

			ValueAnimator animator2 = ValueAnimator.ofFloat(0.51f, 1.0f);
			animator2.setDuration(200);
			animator2.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					progressEnd = frameValue;
					if (progressEnd > 1.0) {
						progressEnd = 1.0f;
					}
					invalidate();
				}

			});
			animator2.start();

			ValueAnimator animator3 = ValueAnimator.ofFloat(SIZE / 4,
					SIZE / 4 * 3 + 10, SIZE / 4 * 3);
			animator3.setDuration(200);
			animator3.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					topY = (int) frameValue;
					invalidate();
				}

			});
			animator3.start();

			ValueAnimator animator4 = ValueAnimator.ofFloat(SIZE / 4 * 3,
					SIZE / 4 + 10, SIZE / 4);
			animator4.setDuration(200);
			animator4.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub
					float frameValue = (Float) animation.getAnimatedValue();
					bottomY = (int) frameValue;
					invalidate();
				}

			});
			animator4.start();
		} else {
			isShowMenu = false;

			ValueAnimator animator1 = ValueAnimator.ofFloat(0.51f, 0.0f);
			animator1.setDuration(100);
			animator1.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					progressStart = frameValue;
					invalidate();
				}

			});
			animator1.start();

			ValueAnimator animator2 = ValueAnimator.ofFloat(1.0f, 0.20f, 0.26f);
			animator2.setDuration(200);
			animator2.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					progressEnd = frameValue;
					invalidate();
				}

			});

			ValueAnimator animator3 = ValueAnimator.ofFloat(SIZE / 4 * 3,
					SIZE / 4 + 10, SIZE / 4);
			animator3.setDuration(200);
			animator3.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub

					float frameValue = (Float) animation.getAnimatedValue();
					topY = (int) frameValue;
					invalidate();
				}

			});
			animator3.start();

			ValueAnimator animator4 = ValueAnimator.ofFloat(SIZE / 4,
					SIZE / 4 * 3 + 10, SIZE / 4 * 3);
			animator4.setDuration(200);
			animator4.addUpdateListener(new AnimatorUpdateListener() {

				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					// TODO Auto-generated method stub
					float frameValue = (Float) animation.getAnimatedValue();
					bottomY = (int) frameValue;
					invalidate();
				}

			});
			animator4.start();
			animator2.start();
		}

	}
}
