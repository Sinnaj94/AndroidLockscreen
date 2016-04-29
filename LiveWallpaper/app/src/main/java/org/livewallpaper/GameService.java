package org.livewallpaper;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.WindowManager;

/**
 * GameService runs in background and update the shown wallpaper.
 */
public class GameService extends WallpaperService {

    /**
     * Key of the prefs file.
     */
    public static final String SHARED_PREFS_NAME = "livewallpaper";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new GameEngine();
    }

    /**
     * GameEngine handling drawing and manage game logic.
     */
    class GameEngine extends Engine implements
            SharedPreferences.OnSharedPreferenceChangeListener, SensorEventListener {


        /** */
        private SensorManager sensorMan;
        /** */
        private Sensor accelerometer;
        /** */
        private float[] gravity;
        /**  */
        private final Handler handler = new Handler();
        /**  */
        private final Paint paint = new Paint();
        /**   */
        private final Runnable drawPattern = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        /**  */
        private boolean visible;
        /**  */
        private SharedPreferences preferences;
        /**  */
        private Rect rectFrame;
        /**  */
        Player player;
        /**  */
        Enemy enemy;
        /**  */
        Grid grid;
        /**  */
        float accX;

        /**
         * Main constructor
         */
        GameEngine() {
            final Paint paint = this.paint;
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            preferences = GameService.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            preferences.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(preferences, null);
            initFrameParams();

            // Set window width and height
            float windowSizeX = rectFrame.width();
            float windowSizeY = rectFrame.height();

            // In onCreate method
            sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
            accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            player = new Player(windowSizeX, windowSizeY, 100, 50, .7f);
            enemy = new Enemy(windowSizeX, windowSizeY, 100, 100, .1f);
            grid = new Grid(windowSizeX, windowSizeY);
        }

        /**
         * Listens to changed settings
         *
         * @param prefs the new preferences
         * @param key   the changed key
         */
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                                              String key) {

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            setTouchEventsEnabled(true);
            sensorMan.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_UI);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDestroy() {
            super.onDestroy();
            handler.removeCallbacks(drawPattern);
            sensorMan.unregisterListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                sensorMan.registerListener(this, accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                drawFrame();
            } else {
                sensorMan.unregisterListener(this);
                handler.removeCallbacks(drawPattern);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                                     int width, int height) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceChanged(format: %d, width: %d, height: %d)", format, width, height));
            super.onSurfaceChanged(holder, format, width, height);

            initFrameParams();
            drawFrame();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceCreated()"));
            super.onSurfaceCreated(holder);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            Log.d(GameEngine.class.getSimpleName(), String.format("entered onSurfaceDestroyed()"));
            super.onSurfaceDestroyed(holder);
            visible = false;
            handler.removeCallbacks(drawPattern);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xStep,
                                     float yStep, int xPixels, int yPixels) {
            drawFrame();
        }

        /**
         * Store the position of the touch event so we can use it for drawing
         * later
         *
         * @param event the motion event
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                player.shoot();
            } else
                super.onTouchEvent(event);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values.clone();

                float x = gravity[0];
                float y = gravity[1];
                float z = gravity[2];

                Log.v(GameEngine.class.getSimpleName(), String.format("onSensorChanged: x: %f, y: %f, z: %f", x, y, z));

                setAccX(x);
            }

        }

        /**
         * Setter for accX.
         * @param accX the value
         */
        private void setAccX(float accX) {
            this.accX = accX;
        }

        /**
         * Getter for accX.
         * @return accX
         */
        private float getAccX() {
            return accX;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // required method
        }

        /**
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    //update
                    updateAll();
                    //draw
                    drawAll(c);
                }
            } finally {
                if (c != null)
                    holder.unlockCanvasAndPost(c);
            }

            handler.removeCallbacks(drawPattern);
            if (visible) {
                handler.postDelayed(drawPattern, 1000 / 25);
            }
        }

        /**
         * Update game objects.
         */
        void updateAll() {

            player.moveX(getAccX());
            player.update();
            enemy.update();
        }

        /**
         * Draw game objects and background on the canvas.
         *
         * @param c the canvas
         */
        void drawAll(Canvas c) {
            refreshAll(c);
            drawBackground(c);
            grid.draw(c);
            player.draw(c);
            enemy.draw(c);
        }

        /**
         * Clear the canvas.
         *
         * @param c the canvas
         */
        void refreshAll(Canvas c) {
            c.save();

            //to refresh the background?
            c.drawColor(0xff000000);
            c.restore();
        }

        /**
         * Draw the background on a given canvas.
         *
         * @param c the canvas
         */
        void drawBackground(Canvas c) {
            c.drawARGB(255, 0, 0, 10);
        }

        /**
         * Update the frame parameters.
         */
        void initFrameParams() {
            DisplayMetrics metrics = new DisplayMetrics();
            Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
            display.getMetrics(metrics);

            rectFrame = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);
        }
    }

}

