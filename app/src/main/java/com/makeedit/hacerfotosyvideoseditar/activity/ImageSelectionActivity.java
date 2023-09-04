package com.makeedit.hacerfotosyvideoseditar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.makeedit.hacerfotosyvideoseditar.MyApplication;
import com.makeedit.hacerfotosyvideoseditar.R;
import com.makeedit.hacerfotosyvideoseditar.adapters.AlbumAdapterById;
import com.makeedit.hacerfotosyvideoseditar.adapters.ImageByAlbumAdapter;
import com.makeedit.hacerfotosyvideoseditar.adapters.OnItemClickListner;
import com.makeedit.hacerfotosyvideoseditar.adapters.SelectedImageAdapter;
import com.makeedit.hacerfotosyvideoseditar.util.Ads;
import com.makeedit.hacerfotosyvideoseditar.view.EmptyRecyclerView;
import com.makeedit.hacerfotosyvideoseditar.view.ExpandIconView;
import com.makeedit.hacerfotosyvideoseditar.view.VerticalSlidingPanel;
import com.makeedit.hacerfotosyvideoseditar.view.VerticalSlidingPanel.PanelSlideListener;

public class ImageSelectionActivity extends AppCompatActivity implements PanelSlideListener {
    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";
    private AlbumAdapterById albumAdapter;
    private ImageByAlbumAdapter albumImagesAdapter;
    private MyApplication application;
    private Button btnClear;
    private ExpandIconView expandIcon;
    boolean fbshow = true;
    Handler handler = new Handler();
    public boolean isFromPreview = false;
    boolean isPause = false;

    private VerticalSlidingPanel panel;
    private View parent;

    private RecyclerView rvAlbum;
    private RecyclerView rvAlbumImages;
    private EmptyRecyclerView rvSelectedImage;
    private SelectedImageAdapter selectedImageAdapter;
    private Toolbar toolbar;
    private TextView tvImageCount;




    class C05822 implements OnClickListener {
        C05822() {
        }

        public void onClick(View v) {
            ImageSelectionActivity.this.clearData();
        }
    }




    class C10173 implements OnItemClickListner<Object> {
        C10173() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.albumImagesAdapter.notifyDataSetChanged();
        }
    }

    class C10184 implements OnItemClickListner<Object> {
        C10184() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.tvImageCount.setText(String.valueOf(ImageSelectionActivity.this.application.getSelectedImages().size()));
            ImageSelectionActivity.this.selectedImageAdapter.notifyDataSetChanged();
        }
    }

    class C10195 implements OnItemClickListner<Object> {
        C10195() {
        }

        public void onItemClick(View view, Object item) {
            ImageSelectionActivity.this.tvImageCount.setText(String.valueOf(ImageSelectionActivity.this.application.getSelectedImages().size()));
            ImageSelectionActivity.this.albumImagesAdapter.notifyDataSetChanged();
        }
    }



    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    private InterstitialAd interstitialAd;
    AdRequest adIRequest;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.image_select_activity);
        getWindow().setFlags(1024, 1024);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        this.application = MyApplication.getInstance();
        this.isFromPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                bindView();
                init();
                addListner();
            }
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(getApplicationContext(), "interstitialAd error" + i, Toast.LENGTH_SHORT).show();
                bindView();
                init();
                addListner();
            }
        });
        if(Ads.mInterstitialAd == null){
            Ads.LoadAd(this);
        }

    }

    private void init() {
        setSupportActionBar(this.toolbar);
        TextView mTitle = (TextView) this.toolbar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.albumAdapter = new AlbumAdapterById(this);
        this.albumImagesAdapter = new ImageByAlbumAdapter(this);
        this.selectedImageAdapter = new SelectedImageAdapter(this);
        this.rvAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        this.rvAlbum.setItemAnimator(new DefaultItemAnimator());
        this.rvAlbum.setAdapter(this.albumAdapter);
        this.rvAlbumImages.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        this.rvAlbumImages.setItemAnimator(new DefaultItemAnimator());
        this.rvAlbumImages.setAdapter(this.albumImagesAdapter);
        this.rvSelectedImage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        this.rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        this.rvSelectedImage.setAdapter(this.selectedImageAdapter);
        this.rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.tvImageCount.setText(String.valueOf(this.application.getSelectedImages().size()));
    }

    private void bindView() {
        this.tvImageCount = (TextView) findViewById(R.id.tvImageCount);
        this.expandIcon = (ExpandIconView) findViewById(R.id.settings_drag_arrow);
        this.rvAlbum = (RecyclerView) findViewById(R.id.rvAlbum);
        this.rvAlbumImages = (RecyclerView) findViewById(R.id.rvImageAlbum);
        this.rvSelectedImage = (EmptyRecyclerView) findViewById(R.id.rvSelectedImagesList);
        this.panel = (VerticalSlidingPanel) findViewById(R.id.overview_panel);
        this.panel.setEnableDragViewTouchEvents(true);
        this.panel.setDragView(findViewById(R.id.settings_pane_header));
        this.panel.setPanelSlideListener(this);
        this.parent = findViewById(R.id.default_home_screen_panel);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.btnClear = (Button) findViewById(R.id.btnClear);
    }

    private AdView mAdView;
    private void addListner() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) { }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        this.btnClear.setOnClickListener(new C05822());
        this.albumAdapter.setOnItemClickListner(new C10173());
        this.albumImagesAdapter.setOnItemClickListner(new C10184());
        this.selectedImageAdapter.setOnItemClickListner(new C10195());
    }

    protected void onResume() {
        super.onResume();

        if (this.isPause) {
            this.isPause = false;
            this.tvImageCount.setText(String.valueOf(this.application.getSelectedImages().size()));
            this.albumImagesAdapter.notifyDataSetChanged();
            this.selectedImageAdapter.notifyDataSetChanged();
        }
    }

    protected void onPause() {
        super.onPause();
        this.isPause = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        if (this.isFromPreview) {
            menu.removeItem(R.id.menu_clear);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.menu_clear:
                clearData();
                break;
            case R.id.menu_done:
                if (this.application.getSelectedImages().size() <= 2) {
                    Toast.makeText(this, "Select more than 2 Images for create video", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!this.isFromPreview) {
                    loadImageEdit();
                    break;
                } else {
                    setResult(-1);
                    finish();
                    return false;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImageEdit() {
        Ads.Loadd(new Ads.Ad_lisoner() {
            @Override
            public void onSucssec(InterstitialAd mInterstitialAd) {
                startActivity(new Intent(ImageSelectionActivity.this, ImageEditActivity.class));
            }

            @Override
            public void onun() {
                startActivity(new Intent(ImageSelectionActivity.this, ImageEditActivity.class));
            }
        });

    }

    public void onPanelSlide(View panel, float slideOffset) {
        if (this.expandIcon != null) {
            this.expandIcon.setFraction(slideOffset, false);
        }
        if (slideOffset >= 0.005f) {
            if (this.parent != null && this.parent.getVisibility() != View.VISIBLE) {
                this.parent.setVisibility(View.VISIBLE);
            }
        } else if (this.parent != null && this.parent.getVisibility() == View.VISIBLE) {
            this.parent.setVisibility(View.GONE);
        }
    }

    public void onPanelCollapsed(View panel) {
        if (this.parent != null) {
            this.parent.setVisibility(View.VISIBLE);
        }
        this.selectedImageAdapter.isExpanded = false;
        this.selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelExpanded(View panel) {
        if (this.parent != null) {
            this.parent.setVisibility(View.GONE);
        }
        this.selectedImageAdapter.isExpanded = true;
        this.selectedImageAdapter.notifyDataSetChanged();
    }

    public void onPanelAnchored(View panel) {
    }

    public void onPanelShown(View panel) {
    }

    public void onBackPressed() {
        if (this.panel.isExpanded()) {
            this.panel.collapsePane();
        } else if (this.isFromPreview) {
            setResult(-1);
            finish();
        } else {
            this.application.videoImages.clear();
            this.application.clearAllSelection();
            super.onBackPressed();
        }
    }

    private void clearData() {
        for (int i = this.application.getSelectedImages().size() - 1; i >= 0; i--) {
            this.application.removeSelectedImage(i);
        }
        this.tvImageCount.setText("0");
        this.selectedImageAdapter.notifyDataSetChanged();
        this.albumImagesAdapter.notifyDataSetChanged();
    }






}
